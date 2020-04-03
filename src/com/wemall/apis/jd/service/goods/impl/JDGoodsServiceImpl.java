package com.wemall.apis.jd.service.goods.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wemall.apis.jd.dao.goods.JDGoodsInfo;
import com.wemall.apis.jd.dao.goods.JDGoodsPool;
import com.wemall.apis.jd.dao.inventory.JDInventory;
import com.wemall.apis.jd.dao.price.JDPrice;
import com.wemall.apis.jd.helper.JdConfigureHelper;
import com.wemall.apis.jd.model.user.UserInfo;
import com.wemall.apis.jd.response.CheckSkuResponse;
import com.wemall.apis.jd.response.CheckStockResponse;
import com.wemall.apis.jd.service.goods.JDGoodsService;
import com.wemall.apis.jdutil.BigDecimalUtil;
import com.wemall.apis.jdutil.HttpClientUtil;
import com.wemall.core.base.GenericDAO;
import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.domain.Accessory;
import com.wemall.foundation.domain.Album;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.GoodsBrand;
import com.wemall.foundation.domain.GoodsClass;
import com.wemall.foundation.domain.Store;
import com.wemall.foundation.domain.Transport;

@Service("jDGoodsService")
@Transactional
public class JDGoodsServiceImpl implements JDGoodsService {

	private String articleInventoryUrl = "https://bizapi.jd.com/api/stock/getStockById";

	static HttpClientUtil httpClientUtil = null;

	static String charset = "utf-8";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected Logger logger = Logger.getLogger(JDGoodsServiceImpl.class);

	@Resource(name = "storeDAO")
	private IGenericDAO<Store> storeDAO;

	@Resource(name = "goodsDAO")
	private GenericDAO<Goods> goodsDAO;

	@Resource(name = "goodsClassDAO")
	private IGenericDAO<GoodsClass> goodsClassDAO;

	@Resource(name = "accessoryDAO")
	private GenericDAO<Accessory> accessoryDAO;

	@Resource(name = "albumDAO")
	private IGenericDAO<Album> albumDAO;

	@Resource(name = "goodsBrandDAO")
	private IGenericDAO<GoodsBrand> goodsBrandDAO;

	@Resource(name = "transportDAO")
	private IGenericDAO<Transport> transportDAO;

	@Resource(name = "jDGoodsInfoDao")
	private JDGoodsInfo jDGoodsInfoDao;

	@Resource(name = "jDPriceDao")
	private JDPrice jDPriceDao;

	@Resource(name = "jDGoodsPoolDao")
	private JDGoodsPool jDGoodsPoolDao;

	@Resource(name = "jDInventoryDao")
	private JDInventory jDInventoryDao;
	
	@Resource(name="userInfoDao")
	private IGenericDAO<UserInfo> userInfoDao;
	
	@Autowired
	private JdConfigureHelper jdConfigureHelper;

	

	private Store store;
	private String pictureUrl = "http://img13.360buyimg.com/n0/";
	private String area = "12_984_3381";
	private Album album;
	private Integer goodsStatusDown = -1;
	private Integer goodsStatusUp = 0;

	public JDGoodsServiceImpl() {
		httpClientUtil = new HttpClientUtil();
	}

	@Override
	public void batchAddOrUpdateGoods(String cid) {

		this.store = ((Store) this.storeDAO.get(Long.valueOf(30L)));
		this.album = ((Album) this.albumDAO.get(Long.valueOf(2L)));
		// this.systemModule = ((SystemModule)
		// this.systemModuleDao.get(Long.valueOf(2L)));

		if (cid == null) {
			return;
		}
		List skuIds = this.jDGoodsPoolDao.getPoolGoodsSkuId(cid);
		if ((skuIds == null) || (skuIds.size() == 0))
			return;
		List lists = new ArrayList();

		int page = skuIds.size() / 80;
		if (skuIds.size() % 80 != 0) {
			page++;
		}

		for (int i = 0; i < page; i++) {
			if (skuIds.size() > 80) {
				lists = skuIds.subList(0, 80);
				List skus = this.jDGoodsInfoDao.getCheckSkuId(lists);
				List goodsSkus = this.jDInventoryDao.JDArticleInventoryByList(skus, this.area);
				downGoods(skus, goodsSkus);
				for (int j = 0; j < goodsSkus.size(); j++) {
					String skuId = (String) skus.get(j);
					try {
						System.out.println(skuId);
						addOrUpdateGoods(skuId);
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(this.sdf.format(new Date()) + " where skus.size>80 skuId=" + skuId + " error:" + e);
					}
				}

				for (int j = 0; j < 80; j++)
					skuIds.remove(0);
			} else {
				lists = skuIds.subList(0, skuIds.size());
				List skus = this.jDGoodsInfoDao.getCheckSkuId(lists);
				List goodsSkus = this.jDInventoryDao.JDArticleInventoryByList(skus, this.area);
				downGoods(skus, goodsSkus);
				for (int j = 0; j < goodsSkus.size(); j++) {
					String skuId = (String) skus.get(j);
					try {
						System.out.println(skuId);
						addOrUpdateGoods(skuId);
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(this.sdf.format(new Date()) + " where skus.size<80 skuId=" + skuId + " error:" + e);
					}
				}
			}
		}
	}

	private void downGoods(List<String> sku, List<String> goodsSkuId) {
		for (int i = 0; i < sku.size(); i++) {
			String jurisdiction = (String) sku.get(i);
			boolean flag = false;
			for (int j = 0; j < goodsSkuId.size(); j++) {
				String invent = (String) goodsSkuId.get(j);
				if (jurisdiction.equals(invent)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				Map params = new HashMap();
				params.put("goodsSkuId", jurisdiction);
				String condition = "select obj from Goods obj  where obj.goodsSkuId=:goodsSkuId ";

				Goods goods = (Goods) this.goodsDAO.query1(condition, params);
				if (goods != null) {
					goods.setGoods_status(this.goodsStatusDown);
					goods.setGoods_inventory(Integer.valueOf(0));
					this.goodsDAO.update(goods);
				}
			}
		}
	}

	@Override
	public void addOrUpdateGoods(String skuId) {
		Map params = new HashMap();
		params.put("goodsSkuId", skuId);
		String condition = "select obj from Goods obj  where obj.goodsSkuId=:goodsSkuId ";

		Goods goods = (Goods) this.goodsDAO.query1(condition, params);

		int Inventory = 1000;

		Map maps = this.jDGoodsInfoDao.getGoodsParticularInfo(skuId);
		if (goods == null) {
			if (maps.size() != 0)
				;
		} else if (maps.size() == 0) {
			goods.setGoods_status(this.goodsStatusDown);
			this.goodsDAO.update(goods);
			return;
		}

		String price = null;
		String originalPrice = null;
		Map map = this.jDPriceDao.getAllPrice(skuId);
		if (map != null) {
			price = (String) map.get("jdPrice");
			originalPrice = (String) map.get("price");//客户购买价格
		} else {
			return;
		}

		BigDecimal newprice = new BigDecimal(price);// 京东价格
		BigDecimal jdprice = new BigDecimal(price);
		BigDecimal bei = new BigDecimal("1.15");
		BigDecimal neworiginalPrice = new BigDecimal(originalPrice);// 京东协议价
		BigDecimal signPrice = new BigDecimal(100);
		BigDecimal grossmargin = BigDecimalUtil.mul(signPrice, BigDecimalUtil.div2(BigDecimalUtil.sub(newprice, neworiginalPrice), newprice, 2), 0);
		BigDecimal isPrice = new BigDecimal(5);
		BigDecimal truePrice = new BigDecimal(0.05D);

		/*if (grossmargin.compareTo(isPrice) == -1) {
			newprice = BigDecimalUtil.add(neworiginalPrice, BigDecimalUtil.mul(truePrice, newprice), 2);
			grossmargin = new BigDecimal(5);
		}*/
		newprice = BigDecimalUtil.add(newprice, BigDecimalUtil.mul(truePrice, newprice), 2);
		BigDecimal newmarketPrice = newprice.multiply(bei).setScale(0, 4);

		if (goods != null) {
			if (goods.getGoods_price().compareTo(newprice) != 0) {
				goods.setGoods_name(((String) maps.get("goodsName")).replace("【京东超市】", "").replace("京东", ""));
				goods.setGoods_status(this.goodsStatusUp);
				goods.setGoods_weight(new BigDecimal(Double.parseDouble((String) maps.get("goodsWeight"))));
				goods.setGoods_inventory(Integer.valueOf(Inventory));
				goods.setGoods_details((String) maps.get("introduction"));

				Accessory mainImage = goods.getGoods_main_photo();
				if (mainImage == null) {
					mainImage = new Accessory();
					mainImage.setAlbum(this.album);
					this.accessoryDAO.save(mainImage);
				}
				mainImage.setPath(this.pictureUrl);
				mainImage.setName((String) maps.get("imagePath"));
				goods.setGoods_main_photo(mainImage);
				this.goodsDAO.update(goods);
			} else {
				goods.setGoods_status(this.goodsStatusUp);
				goods.setGoods_name(((String) maps.get("goodsName")).replace("【京东超市】", "").replace("京东", ""));
				goods.setGoods_weight(new BigDecimal(Double.parseDouble((String) maps.get("goodsWeight"))));

				goods.setGoods_current_price(newprice);
				goods.setJdPrice(jdprice);
				goods.setGoods_price(newmarketPrice);
				goods.setOriginalPrice(neworiginalPrice);

				goods.setGoods_inventory(Integer.valueOf(Inventory));
				goods.setGoods_details((String) maps.get("introduction"));

				Accessory mainImage = goods.getGoods_main_photo();
				if (mainImage == null) {
					mainImage = new Accessory();
					mainImage.setAlbum(this.album);
					this.accessoryDAO.save(mainImage);
				}
				mainImage.setPath(this.pictureUrl);
				mainImage.setName((String) maps.get("imagePath"));
				goods.setGoods_main_photo(mainImage);
				this.goodsDAO.update(goods);
			}
		} else {
			Goods newGoods = new Goods();
			newGoods.setGoods_name(((String) maps.get("goodsName")).replace("【京东超市】", "").replace("京东", ""));
			newGoods.setGoodsSkuId((String) maps.get("skuId"));

			newGoods.setGoods_current_price(newprice);
			newGoods.setJdPrice(jdprice);
			newGoods.setGoods_price(newmarketPrice);
			newGoods.setOriginalPrice(neworiginalPrice);
			newGoods.setGoods_property("[]");

			newGoods.setGoods_inventory(Integer.valueOf(Inventory));
			newGoods.setGoods_weight(new BigDecimal(Double.parseDouble((String) maps.get("goodsWeight"))));
			newGoods.setGoods_volume(new BigDecimal(0));
			// newGoods.setFreeShippingFlag(Boolean.valueOf(false));
			newGoods.setMail_trans_fee(new BigDecimal(0));
			newGoods.setExpress_trans_fee(new BigDecimal(0));
			newGoods.setEms_trans_fee(new BigDecimal(0));

			newGoods.setGoods_details((String) maps.get("introduction"));
			// newGoods.setBuyLimitCount(Integer.valueOf(0));
			newGoods.setGoods_click(Integer.valueOf(0));
			newGoods.setGoods_salenum(Integer.valueOf(0));
			newGoods.setGoods_recommend(Boolean.valueOf(false));
			// GoodsMold goodsMold = (GoodsMold)
			// this.goodsMoldDao.get(Long.valueOf(1L));
			// newGoods.setGoodsMold(goodsMold);

			newGoods.setGoods_status(this.goodsStatusUp);

			Accessory mainImage = new Accessory();
			mainImage.setPath(this.pictureUrl);
			mainImage.setName((String) maps.get("imagePath"));
			mainImage.setAlbum(this.album);
			this.accessoryDAO.save(mainImage);
			newGoods.setGoods_main_photo(mainImage);

			String categorys = (String) maps.get("category");
			String cid = categorys.split(";")[2];
			Map find1Classify = new HashMap();
			find1Classify.put("classificationJDCid", cid);
			String hql = "obj.classificationJDCid=:classificationJDCid";
			GoodsClass classification = (GoodsClass) this.goodsClassDAO.find1(hql, find1Classify);
			newGoods.setGc(classification);
			newGoods.setGoods_store(this.store);

			Transport shippingTemplate = setShippingTemplateFreight(skuId);
			newGoods.setTransport(shippingTemplate);
			this.goodsDAO.save(newGoods);
		}
	}

	private Transport setShippingTemplateFreight(String skuId) {
		UserInfo userInfo = (UserInfo) this.userInfoDao.get(Long.valueOf(1L));
		String freight = this.jDGoodsInfoDao.getFreight(skuId, "1", userInfo.getProvinceVal(), userInfo.getCityVal(), userInfo.getCountyVal(), userInfo.getTownVal());
		Transport shippingTemplate = (Transport) this.transportDAO.get(Long.valueOf(17L));

		if ((freight != null) && (!"6".equals(freight)) && (!"0".equals(freight))) {
			String expressConfig = "[{\"addFee\":2.0,\"cityId\":\"-1\",\"initPricing\":1,\"cityName\":\"全国\",\"addPricing\":1,\"initFee\":" + freight + "}, {\"addFee\":0.0,\"cityId\":\"21\",\"initPricing\":1,\"cityName\":\"无锡\",\"addPricing\":1,\"initFee\":" + freight + "}]";
			Map shippingTemParams = new HashMap();
			shippingTemParams.put("expressConfig", expressConfig);
			shippingTemParams.put("templateName", "京东");
			String stHql = "obj.expressConfig=:expressConfig and obj.templateName=:templateName";

			shippingTemplate = (Transport) this.transportDAO.find1(stHql, shippingTemParams);
			if (shippingTemplate == null) {
				shippingTemplate = new Transport();
				shippingTemplate.setTrans_name("京东");
				shippingTemplate.setTrans_mail(Boolean.valueOf(false));
				shippingTemplate.setTrans_ems(Boolean.valueOf(false));
				shippingTemplate.setTrans_ems_info(expressConfig);
				shippingTemplate.setTrans_time(24);
			}
		}
		return shippingTemplate;
	}

	@Override
	public String getSkuid(Long paramLong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateInventory(Long paramLong, int paramInt) {
		// TODO Auto-generated method stub

	}

	@Override
	public CheckSkuResponse getCheckSkuResponse(List<String> ids) {
		CheckSkuResponse checkSkuResponse = jDGoodsInfoDao.getCheckSkuResponse(ids);
		if(checkSkuResponse!=null &&StringUtils.equals("2007", checkSkuResponse.getResultCode())){
			try {
				jdConfigureHelper.update();
			} catch (Exception e) {
			}
			return checkSkuResponse = jDGoodsInfoDao.getCheckSkuResponse(ids);
		}
		return checkSkuResponse;
	}

	@Override
	public CheckStockResponse getStockByIdResponse(List<String> ids) {
		CheckStockResponse response = jDGoodsInfoDao.getStockById(ids);
		if(response!=null &&StringUtils.equals("2007", response.getResultCode())){
			try {
				jdConfigureHelper.update();
			} catch (Exception e) {
			}
			return response = jDGoodsInfoDao.getStockById(ids);
		}
		return response;
	}

	@Override
	public void batchAddOrUpdateGoods_rest(String cid) {
		this.store = ((Store) this.storeDAO.get(Long.valueOf(30L)));
		this.album = ((Album) this.albumDAO.get(Long.valueOf(2L)));
		// this.systemModule = ((SystemModule)
		// this.systemModuleDao.get(Long.valueOf(2L)));

		if (cid == null) {
			return;
		}
		List skuIds = this.jDGoodsPoolDao.getPoolGoodsSkuId(cid);
		if ((skuIds == null) || (skuIds.size() == 0))
			return;
		List lists = new ArrayList();

		int page = skuIds.size() / 80;
		if (skuIds.size() % 80 != 0) {
			page++;
		}

		lists = skuIds.subList(0, 5);
		List skus = this.jDGoodsInfoDao.getCheckSkuId(lists);
		List goodsSkus = this.jDInventoryDao.JDArticleInventoryByList(skus, this.area);
		downGoods(skus, goodsSkus);
		for (int j = 0; j < goodsSkus.size(); j++) {
			String skuId = (String) skus.get(j);
			try {
				System.out.println(skuId);
				addOrUpdateGoods(skuId);
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error(this.sdf.format(new Date()) + " where skus.size>80 skuId=" + skuId + " error:" + e);
			}
		}

		for (int j = 0; j < 80; j++)
			skuIds.remove(0);

	}

}
