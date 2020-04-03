package com.wemall.apis.jd.dao.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.wemall.apis.jd.dao.util.JDBase;

/**
 * 
 * @author zhangjun 
 * 3.1 获取商品池编号接口 
 * 3.2 获取池内商品编号接口
 */

@Repository("jDGoodsPoolDao")
public class JDGoodsPool extends JDBase {
	private String poolNumUrl = "https://bizapi.jd.com/api/product/getPageNum";

	private String poolSkuUrl = "https://bizapi.jd.com/api/product/getSkuByPage";

	public String getPoolNum(String poolName) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);

		JSONArray jsonResult = returnJDArray(this.poolNumUrl, createMap);
		if (jsonResult == null) {
			return null;
		}
		for (int i = 0; i < jsonResult.size(); i++) {
			JSONObject jsonObj = jsonResult.getJSONObject(i);
			if (jsonObj.get("name").equals(poolName)) {
				return jsonObj.getString("page_num");
			}
		}
		return null;
	}
	
	public List<String> getPoolNum() {
		List<String> pagenums=new ArrayList<String>();
		Map createMap = new HashMap();
		createMap.put("token", accessToken);

		JSONArray jsonResult = returnJDArray(this.poolNumUrl, createMap);
		if (jsonResult == null) {
			return null;
		}
		for (int i = 0; i < jsonResult.size(); i++) {
			JSONObject jsonObj = jsonResult.getJSONObject(i);
			pagenums.add(jsonObj.getString("page_num"));
		}
		return pagenums;
	}
	
	
	public String getPoolGoodsSkuIdByCategory(String pageNum) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("pageNum", pageNum);
		createMap.put("pageNo", "1");

		JSONObject jsonResult = returnJDObj(this.poolSkuUrl, createMap);
		if (jsonResult == null) {
			return null;
		}

		int pageCount = jsonResult.getInt("pageCount");
		JSONArray jsonSkuIds = JSONArray.fromObject(jsonResult.get("skuIds"));
		String skuid=jsonSkuIds.getString(0);
		return skuid;
	}

	public List<String> getPoolGoodsSkuId(String pageNum) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("pageNum", pageNum);
		createMap.put("pageNo", "1");

		JSONObject jsonResult = returnJDObj(this.poolSkuUrl, createMap);
		if (jsonResult == null) {
			return null;
		}

		int pageCount = jsonResult.getInt("pageCount");

		JSONArray jsonSkuIds = JSONArray.fromObject(jsonResult.get("skuIds"));

		for (int i = 2; i <= pageCount; i++) {
			createMap.put("pageNo", Integer.toString(i));
			JSONArray list = getSkuIds(createMap);
			jsonSkuIds.addAll(list);
		}
		List lists = new LinkedList();
		for (int i = 0; i < jsonSkuIds.size(); i++) {
			lists.add(jsonSkuIds.getString(i));
		}
		return lists;
	}

	private JSONArray getSkuIds(Map<String, String> createMap) {
		JSONObject jsonResult = returnJDObj(this.poolSkuUrl, createMap);
		if (jsonResult == null) {
			return null;
		}
		JSONArray jsonSkuIds = JSONArray.fromObject(jsonResult.get("skuIds"));
		return jsonSkuIds;
	}

}
