package com.wemall.apis.jd.service.order.impl;

import com.wemall.apis.jd.dao.area.JDArea;
import com.wemall.foundation.dao.AreaDAO;
import com.wemall.foundation.domain.Address;
import com.wemall.foundation.domain.Area;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wemall.apis.jd.dao.inventory.JDInventory;
import com.wemall.apis.jd.dao.order.JDPurchaseOrder;
import com.wemall.apis.jd.dao.price.JDPrice;
import com.wemall.apis.jd.model.user.UserInfo;
import com.wemall.apis.jd.service.order.JdHandOrderService;
import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.GoodsCart;
import com.wemall.foundation.domain.OrderForm;
import com.wemall.foundation.service.IAreaService;
import com.wemall.foundation.service.impl.AreaServiceImpl;


@Service("jdHandOrderService")
@Transactional
public class JdHandOrderServiceImpl implements JdHandOrderService {

    @Resource(name = "orderFormDAO")
    private IGenericDAO<OrderForm> orderFormDAO;


    @Resource(name = "goodsCartDAO")
    private IGenericDAO<GoodsCart> goodsCartDAO;

    @Resource(name = "userInfoDao")
    private IGenericDAO<UserInfo> userInfoDao;

    @Resource(name = "goodsDAO")
    private IGenericDAO<Goods> goodsDAO;

    @Resource(name = "jDPurchaseOrderDao")
    private JDPurchaseOrder jDPurchaseOrderDao;

    @Resource(name = "jDPriceDao")
    private JDPrice jDPriceDao;

    @Resource(name = "jDInventoryDao")
    private JDInventory jDInventoryDao;

    @Resource(name = "jDAreaDao")
    private JDArea jDAreaDao;

    @Autowired
    private IAreaService areaService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String JdSendHandOrder(long id) {
        OrderForm purchaseOrder = (OrderForm) this.orderFormDAO.get(Long.valueOf(id));
        String orderNum = purchaseOrder.getOrder_id();
        List cartItems = selectCartItemsByOrder(purchaseOrder);
        Address address = purchaseOrder.getAddr();
        Area area = address.getArea();


        StringBuffer sbSku = new StringBuffer("[");
        StringBuffer sbPicture = new StringBuffer("[");
        for (int i = 0; i < cartItems.size(); i++) {
            GoodsCart cartItem = (GoodsCart) cartItems.get(i);
            jointData(sbSku, sbPicture, cartItem);
        }
        String sku = sbSku.append("]").toString();
        String orderPriceSnap = sbPicture.append("]").toString();

        String info = "";
        if (("[]".equals(sku)) || (sku == null) || ("".equals(sku))) {
            purchaseOrder.setJDOrderStatus("1");
            return "此订单无京东商品！";
        }
        UserInfo userInfo = (UserInfo) this.userInfoDao.get(Long.valueOf(1L));
        int level = area.getLevel();
        String province = "", city = "", county = "", town = "";
        Area province_area = null, city_area = null, country_area = null, town_area = null;

        if (level == 3) {
            country_area = areaService.getObjById(area.getParent().getId());
            city_area = areaService.getObjById(country_area.getParent().getId());
            province_area = areaService.getObjById(city_area.getParent().getId());
            province = jDAreaDao.getOneAreaId(province_area.getAreaName());
            city = jDAreaDao.getTwoAreaId(province, city_area.getAreaName());
            county = jDAreaDao.getThreeAreaId(city, country_area.getAreaName());
            town = jDAreaDao.getFourAreaId(county, area.getAreaName());
        } else {
            city_area = areaService.getObjById(area.getParent().getId());
            province_area = areaService.getObjById(city_area.getParent().getId());
            province = jDAreaDao.getOneAreaId(province_area.getAreaName());
            city = jDAreaDao.getTwoAreaId(province, city_area.getAreaName());
            county = jDAreaDao.getThreeAreaId(city, area.getAreaName());
            List<String> towns = jDAreaDao.getAllFourAreaByParentId(county);
            if (towns.size() > 0)
                town = towns.get(0).toString();
            else
                town = String.valueOf(0);
        }

        Map maps = new HashMap();
        maps = this.jDPurchaseOrderDao.submitOrder(orderNum, sku, address.getTrueName(), province,
                city, county, town, address.getArea_info(), address.getMobile(),
                userInfo.getEmail(), userInfo.getCompanyName(), orderPriceSnap);

        boolean flag = ((Boolean) maps.get("success")).booleanValue();
        if (!flag) {
            purchaseOrder.setJDOrderStatus("0");
            String message = (String) maps.get("message");
            if ("重复下单".equals(message)) {
                purchaseOrder.setJDOrderStatus("1");
            }
            if (message.contains("您好，如下商品无货：商品")) {
                String skuId = message.replace("您好，如下商品无货：商品", "");
                Map goodsParams = new HashMap();
                goodsParams.put("goodsSkuId", skuId);
                String goodsHql = "obj.goodsSkuId = :goodsSkuId";
                Goods goods = (Goods) this.goodsDAO.find1(goodsHql, goodsParams);
                info = "您好，如下商品暂时缺货：" + goods.getGoods_name();
            } else {
                info = message;
            }
        } else {
            String jdOrderId = (String) maps.get("jdOrderId");
            this.jDPurchaseOrderDao.confirmOrder(jdOrderId);
            purchaseOrder.setJDOrderId(jdOrderId);
            purchaseOrder.setJDOrderStatus("1");

            info = "下单成功！您的京东订单号为：" + jdOrderId;
        }

        this.orderFormDAO.update(purchaseOrder);
        return info;
    }

    public List<GoodsCart> selectCartItemsByOrder(OrderForm purchaseOrder) {
        Map cartItemParams = new HashMap();
        cartItemParams.put("purchaseOrder", purchaseOrder);
//	    cartItemParams.put("deleteStatus", 0);
        String cartItemHql = "select obj from GoodsCart obj  where  obj.of=:purchaseOrder ";

        List cartItems = this.goodsCartDAO.query(cartItemHql, cartItemParams);
        return cartItems;
    }

    private void jointData(StringBuffer sbSku, StringBuffer sbPicture, GoodsCart cartItem) {
        Goods goods = cartItem.getGoods();
        String skuId = goods.getGoodsSkuId();
        if ((skuId == null) || ("".equals(skuId))) {
            return;
        }
        if (!"[".equals(sbSku.toString())) {
            sbSku.append(",");
            sbPicture.append(",");
        }
        String buyPrice = null;
        try {
            buyPrice = this.jDPriceDao.getProtocolPrice(skuId);
        } catch (Exception e) {
            e.printStackTrace();
            buyPrice = goods.getOriginalPrice().toString();
        }
        Integer buyCount = cartItem.getCount();
        sbSku.append("{skuId:" + skuId + ",num:" + buyCount + ",bNeedAnnex:true}");
        sbPicture.append("{price:" + buyPrice + ",skuId:" + skuId + "}");
    }


}
