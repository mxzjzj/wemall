package com.wemall.apis.jd.dao.price;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.service.IGoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
public class JDPriceHandle {

    @Resource(name = "jDPriceDao")
    private JDPrice jDPriceDao;

    @Resource(name = "goodsDAO")
    private GenericDAO<Goods> goodsDAO;

    @Autowired
    private IGoodsService goodsService;

    @ResponseBody
    @RequestMapping({"/currentprices.htm"})
    public String addupdatePrice() {
        List<String> jdskuids = goodsDAO.executeNativeNamedQuery("SELECT goods_jd_skuid FROM wemall_goods  where goods_jd_skuid is not null");
        for (String skuId : jdskuids) {
            Map<String, String> map = jDPriceDao.getAllPrice(skuId);
            String price = map.get("price");
            BigDecimal newprice = new BigDecimal(price);

            Map params = new HashMap();
            params.put("goodsSkuId", skuId);
            String condition = "select obj from Goods obj  where obj.goodsSkuId=:goodsSkuId ";
            Goods goods = (Goods) this.goodsDAO.query1(condition, params);
            if (goods != null) {
                BigDecimal oldp = goods.getGoods_current_price();
                if (newprice.compareTo(oldp) != 0) {
                    goods.setGoods_current_price(newprice);
                    System.out.println("您已经修改了商品skuid" + skuId+":的最新价格为"+newprice);
                    this.goodsService.update(goods);
                }
            }
        }
        return "successful" ;
    }
}
