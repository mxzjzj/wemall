package com.wemall.apis.jd.dao.goods;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.wemall.apis.jd.dao.util.JDBase;

@Repository("jdAreaRestrict")
public class JdAreaRestrict extends JDBase {
	// 3.8 商品区域购买限制查询
	private String getAreaRestrictUrl = "https://bizapi.jd.com/api/product/checkAreaLimit";

	public boolean jdAreaRestrict(String skuId, String province, String city, String county, String town) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("skuIds", skuId);
		createMap.put("province", province);
		createMap.put("city", city);
		createMap.put("county", county);
		createMap.put("town", town);

		String httpResult = this.httpClientUtil.doPost(this.getAreaRestrictUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		System.out.println(json_parent);
		boolean success = json_parent.getBoolean("success");

		return false;
	}
}
