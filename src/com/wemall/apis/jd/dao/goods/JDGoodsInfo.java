package com.wemall.apis.jd.dao.goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wemall.apis.jd.dao.area.JDArea;
import com.wemall.apis.jd.dao.util.JDBase;
import com.wemall.apis.jd.response.CheckSkuResponse;
import com.wemall.apis.jd.response.CheckStockResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository("jDGoodsInfoDao")
public class JDGoodsInfo extends JDBase {

	// 3.4 获取商品详细信息接口
	private String poolNumUrl = "https://bizapi.jd.com/api/product/getDetail";

	// 3.5 获取商品上下架状态接口
	private String skuStateUrl = "https://bizapi.jd.com/api/product/skuState";

	// 3.11 运费查询接口
	private String getFreightUrl = "https://bizapi.jd.com/api/order/getFreight";

	// 3.13 商品可售验证接口
	private String check = "https://bizapi.jd.com/api/product/check";
	
	// 6.3 批量获取库存接口(建议商品列表页使用)
	private String getStockByIdUrl ="https://bizapi.jd.com/api/stock/getStockById";

	private JDArea jDAreaDao = new JDArea();

	public CheckSkuResponse getCheckSkuResponse(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		String skuIds = StringUtils.join(ids, ",");
		Map<String, String> createMap = new HashMap<String, String>();
		createMap.put("token", accessToken);
		createMap.put("skuIds", skuIds);
		String httpResult = this.httpClientUtil.doPost(this.check, createMap,
				this.charset);
		try {
			CheckSkuResponse response =  com.alibaba.fastjson.JSONObject.parseObject(httpResult,
					CheckSkuResponse.class);
			return response;
		} catch (Exception e) {
		}
		return null;
	}
	
	public CheckStockResponse getStockById(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		String skuIds = StringUtils.join(ids, ",");
		Map<String, String> createMap = new HashMap<String, String>();
		createMap.put("token", accessToken);
		createMap.put("sku", skuIds);
		createMap.put("area", "12_984_40035");

		String httpResult = this.httpClientUtil.doPost(this.getStockByIdUrl, createMap,
				this.charset);
		try {
			httpResult=httpResult.toString().replace("\\","");
			httpResult=httpResult.replace("\"[{","[{");
			httpResult=httpResult.replace("]\"}","]}");
			CheckStockResponse response =  com.alibaba.fastjson.JSONObject.parseObject(httpResult,
					CheckStockResponse.class);
			return response;
		} catch (Exception e) {
			System.out.println("wb==="+e.getMessage());
		}
		return null;
	}

	public List<String> getCheckSkuId(List<String> lists) {
		String skuIds = lists.toString();
		skuIds = skuIds.substring(1, skuIds.length());
		skuIds = skuIds.substring(0, skuIds.length() - 1);
		Map<String, String> createMap = new HashMap<String, String>();
		createMap.put("token", accessToken);
		createMap.put("skuIds", skuIds.replace(" ", ""));
		String httpResult = this.httpClientUtil.doPost(this.check, createMap,
				this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		if (json_parent == null) {
			return new ArrayList<String>();
		}
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			System.out.println(resultMessage);
			System.out.println("error" + lists.size());
			logger.error("JDGoodsInfoError:" + resultMessage);
			return null;
		}
		JSONArray jsonArr = json_parent.getJSONArray("result");
		List skus = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			String saleState = jsonObj.getString("saleState");
			if (!"0".equals(saleState)) {
				String skuId = jsonObj.getString("skuId");
				skus.add(skuId);
			}
		}
		return skus;
	}

	public List<String> getGoodsCids(String skuId) {
		List<String> cids = new ArrayList<String>();
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("sku", skuId);

		JSONObject jsonResult = returnJDObj(this.poolNumUrl, createMap);
		if (jsonResult == null) {
			return cids;
		}
		String category = jsonResult.getString("category");
		return Arrays.asList(category.split(";"));
	}

	public Map<String, String> getGoodsParticularInfo(String skuId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("sku", skuId);

		JSONObject jsonResult = returnJDObj(this.poolNumUrl, createMap);
		if (jsonResult == null) {
			return new HashMap();
		}
		String state = jsonResult.getString("state");

		if (state == "0") {
			return new HashMap();
		}
		Map maps = new HashMap();
		maps.put("skuId", skuId);
		maps.put("state", state);
		maps.put("saleUnit", jsonResult.getString("saleUnit"));
		maps.put("goodsWeight", jsonResult.getString("weight"));
		maps.put("productArea", jsonResult.getString("productArea"));
		try {
			maps.put("wareQD", jsonResult.getString("wareQD"));
		} catch (Exception e) {
			maps.put("wareQD", "");
		}
		maps.put("imagePath", jsonResult.getString("imagePath"));
		try {
			maps.put("param", jsonResult.getString("param"));
		} catch (Exception e) {
			maps.put("param", "");
		}
		maps.put("introduction", jsonResult.getString("introduction"));
		maps.put("brandName", jsonResult.getString("brandName"));
		maps.put("upc", jsonResult.getString("upc"));
		maps.put("category", jsonResult.getString("category"));
		maps.put("goodsName", jsonResult.getString("name"));

		return maps;
	}

	public String getFreight(String skuId, String number, String province,
			String city, String county, String town) {
		String sku = "[{skuId:" + skuId + ",num:" + number + "}]";
		if (province.indexOf("省") != -1) {
			province = province.substring(0, province.indexOf("省"));
		}
		String provinceId = this.jDAreaDao.getOneAreaId(province);
		String cityId = this.jDAreaDao.getTwoAreaId(provinceId, city);
		String countyId = this.jDAreaDao.getThreeAreaId(cityId, county);
		String townId = "0";
		if (town != null) {
			townId = this.jDAreaDao.getFourAreaId(countyId, town);
		}
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("sku", sku);
		createMap.put("province", provinceId);
		createMap.put("city", cityId);
		createMap.put("county", countyId);
		createMap.put("town", townId);
		createMap.put("paymentType", "4");

		String httpResult = this.httpClientUtil.doPost(this.getFreightUrl,
				createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDGoodsInfoError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject
				.fromObject(json_parent.get("result"));
		String freight = jsonResult.getString("freight");
		return freight;
	}

	public Map<String, String> getSkuState(List skuIds) {
		if (skuIds.size() > 100)
			throw new RuntimeException("最多支持一次查询100个");
		String strs = skuIds.toString().substring(1,
				skuIds.toString().length() - 1);
		strs = strs.replace(" ", "");
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("sku", strs);

		return returnMapIsState(createMap);
	}

	public Map<String, String> getSkuState(String skuId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("sku", skuId);

		return returnMapIsState(createMap);
	}

	private Map<String, String> returnMapIsState(Map<String, String> createMap) {
		JSONArray jsonResult = returnJDArray(this.skuStateUrl, createMap);
		if (jsonResult == null) {
			return null;
		}
		Map skuState = new HashMap();
		for (int i = 0; i < jsonResult.size(); i++) {
			JSONObject jsonObj = jsonResult.getJSONObject(i);
			String sku = jsonObj.getString("sku");
			String state = jsonObj.getString("state");
			skuState.put(sku, state);
		}
		return skuState;
	}
}
