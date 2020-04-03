package com.wemall.apis.jd.dao.order;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.wemall.apis.jd.dao.util.JDBase;

@Repository("jDPurchaseOrderDao")
public class JDPurchaseOrder extends JDBase {

	private String submitOrderUrl = "https://bizapi.jd.com/api/order/submitOrder";

	private String confirmOrderUrl = "https://bizapi.jd.com/api/order/confirmOrder";

	private String cancelUrl = "https://bizapi.jd.com/api/order/cancel";

	private String selectJdOrderIdByThirdOrderUrl = "https://bizapi.jd.com/api/order/cancel";

	private String selectJdOrderUrl = "https://bizapi.jd.com/api/order/selectJdOrder";

	private String orderTrackUrl = "https://bizapi.jd.com/api/order/orderTrack";

	public Map<String, Object> submitOrder(String thirdOrder, String sku, String name, String province, String city, String county, String town, String address, String mobile, String email, String companyName, String orderPriceSnap) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("thirdOrder", thirdOrder);
		createMap.put("sku", sku);

		createMap.put("name", name);
		createMap.put("province", province);
		createMap.put("city", city);
		createMap.put("county", county);
		createMap.put("town", town);
		createMap.put("address", address);

		createMap.put("mobile", mobile);
		createMap.put("email", email);

		createMap.put("invoiceState", "2");
		createMap.put("invoiceType", "2");
		createMap.put("selectedInvoiceTitle", "5");
		createMap.put("companyName", companyName);

		createMap.put("invoiceContent", "1");
		createMap.put("paymentType", "4");
		createMap.put("isUseBalance", "1");
		createMap.put("submitState", "0");

		createMap.put("doOrderPriceMode", "1");
		createMap.put("orderPriceSnap", orderPriceSnap);

		Map maps = new HashMap();

		String httpResult = this.httpClientUtil.doPost(this.submitOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);

		boolean success = json_parent.getBoolean("success");
		maps.put("success", Boolean.valueOf(success));
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			String resultCode = json_parent.getString("resultCode");
			maps.put("message", resultMessage);
			logger.error("jDpurchaseOrder:" + resultMessage);
			logger.error(json_parent);
			return maps;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));

		String jdOrderId = jsonResult.getString("jdOrderId");
		String freight = jsonResult.getString("freight");
		maps.put("jdOrderId", jdOrderId);
		maps.put("freight", freight);

		return maps;
	}

	public boolean cancelNoSureOrder(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.cancelUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("result");
		if (!success) {
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + json_parent.getString("resultMessage"));
			return false;
		}
		return success;
	}

	public String peggingJdOrder(String thirdOrder) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("thirdOrder", thirdOrder);
		String httpResult = this.httpClientUtil.doPost(this.selectJdOrderIdByThirdOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			logger.error("jDpurchaseOrder:" + thirdOrder + ":" + json_parent.getString("resultMessage"));
			return null;
		}
		return json_parent.getString("result");
	}

	public boolean confirmOrder(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.confirmOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return false;
		}
		boolean result = json_parent.getBoolean("result");
		return result;
	}

	public String selectJdOrder(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.selectJdOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return "0";
		}
		JSONObject result = json_parent.getJSONObject("result");
		JSONArray cOrder = result.getJSONArray("cOrder");
		JSONObject jsonObj = cOrder.getJSONObject(0);
		String state = jsonObj.getString("state");
		return state;
	}

	public String selectJdOrderByOne(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.selectJdOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return "0";
		}
		JSONObject result = json_parent.getJSONObject("result");
		String state = result.getString("state");
		return state;
	}

	public HashMap<String, String> selectJdOrderChild(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.selectJdOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		HashMap maps = new HashMap();
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return maps;
		}
		JSONObject result = JSONObject.fromObject(json_parent.get("result"));
		JSONArray cOrderes = JSONArray.fromObject(result.getJSONArray("cOrder"));
		for (int i = 0; i < cOrderes.size(); i++) {
			JSONObject cOrder = cOrderes.getJSONObject(i);
			String jdOrderIdChild = cOrder.getString("jdOrderId");
			JSONArray skues = cOrder.getJSONArray("sku");
			for (int j = 0; j < skues.size(); j++) {
				JSONObject sku = skues.getJSONObject(j);
				String skuId = sku.getString("skuId");
				maps.put(skuId, jdOrderIdChild);
			}
		}
		return maps;
	}

	public HashMap<String, String> selectJdOrderChildByOne(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.selectJdOrderUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		HashMap maps = new HashMap();
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return maps;
		}
		JSONObject result = JSONObject.fromObject(json_parent.get("result"));
		String jdOrderIdChild = result.getString("jdOrderId");
		JSONArray skues = result.getJSONArray("sku");
		for (int i = 0; i < skues.size(); i++) {
			JSONObject sku = skues.getJSONObject(i);
			String skuId = sku.getString("skuId");
			maps.put(skuId, jdOrderIdChild);
		}
		return maps;
	}

	public String selectOrderTrack(String jdOrderId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("jdOrderId", jdOrderId);
		String httpResult = this.httpClientUtil.doPost(this.orderTrackUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String message = json_parent.getString("resultMessage");
			logger.error("jDpurchaseOrder:" + jdOrderId + ":" + message);
			return null;
		}
		JSONObject result = json_parent.getJSONObject("result");

		JSONArray orderTrank = result.getJSONArray("orderTrack");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orderTrank.size(); i++) {
			JSONObject jsonObj = orderTrank.getJSONObject(i);
			String msgTime = jsonObj.getString("msgTime");
			String content = jsonObj.getString("content");
			sb.append(msgTime);
			sb.append(":");
			sb.append(content);
			sb.append("<br/>");
		}
		return sb.toString();
	}

}
