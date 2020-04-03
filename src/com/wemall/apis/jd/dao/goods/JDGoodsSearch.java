package com.wemall.apis.jd.dao.goods;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.wemall.apis.jd.dao.util.JDBase;

@Repository("jDGoodsSearchDao")
public class JDGoodsSearch extends JDBase {
	//3.12 商品搜索接口
	private String searchUrl = "https://bizapi.jd.com/api/search/search";

	public void searchByCatId(String catId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("catId", catId);
		createMap.put("pageIndex", "1");
		createMap.put("pageSize", "40");

		String httpResult = this.httpClientUtil.doPost(this.searchUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));

		String pageCount = jsonResult.getString("pageCount");

		JSONObject brandAggregate = jsonResult.getJSONObject("brandAggregate");

		JSONObject categoryAggregate = jsonResult.getJSONObject("categoryAggregate");

		JSONArray hitResult = jsonResult.getJSONArray("hitResult");

		System.out.println(pageCount);
		System.out.println(brandAggregate);
		System.out.println(categoryAggregate);
		System.out.println(hitResult);
	}
}