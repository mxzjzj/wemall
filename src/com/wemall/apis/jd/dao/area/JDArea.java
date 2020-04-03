package com.wemall.apis.jd.dao.area;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import com.wemall.apis.jd.dao.util.JDBase;

@Repository("jDAreaDao")
public class JDArea extends JDBase {

	//4.1 获取一级地址
	private String oneAreaUrl = "https://bizapi.jd.com/api/area/getProvince";
	//4.2 获取二级地址
	private String twoAreaUrl = "https://bizapi.jd.com/api/area/getCity";
	//4.3 获取三级地址
	private String threeAreaUrl = "https://bizapi.jd.com/api/area/getCounty";
	//4.4 获取四级地址
	private String fourAreaUrl = "https://bizapi.jd.com/api/area/getTown";

	public String getOneAreaId(String address) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);

		String httpResult = this.httpClientUtil.doPost(this.oneAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		return jsonResult.getString(address);
	}

	public List<String> getAllOneArea() {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);

		String httpResult = this.httpClientUtil.doPost(this.oneAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		String strs = jsonResult.toString();
		strs = strs.replace("{", "");
		strs = strs.replace("}", "");
		strs = strs.replace("\"", "");
		String[] areas = strs.split(",");
		List addresses = new LinkedList();
		for (int i = 0; i < areas.length; i++) {
			String[] str = areas[i].split(":");
			addresses.add(str[0] + ":" + str[1]);
		}
		return addresses;
	}

	public String getTwoAreaId(String id, String address) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.twoAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		return jsonResult.getString(address);
	}

	public List<String> getAllTwoAreaByParentId(String id) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.twoAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		String strs = jsonResult.toString();
		strs = strs.replace("{", "");
		strs = strs.replace("}", "");
		strs = strs.replace("\"", "");
		String[] areas = strs.split(",");
		List addresses = new LinkedList();
		for (int i = 0; i < areas.length; i++) {
			String[] str = areas[i].split(":");
			addresses.add(str[0] + ":" + str[1]);
		}
		return addresses;
	}

	public String getThreeAreaId(String id, String address) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.threeAreaUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		return jsonResult.getString(address);
	}

	public List<String> getAllThreeAreaByParentId(String id) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.threeAreaUrl, createMap, this.charset);
		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		String strs = jsonResult.toString();
		strs = strs.replace("{", "");
		strs = strs.replace("}", "");
		strs = strs.replace("\"", "");
		String[] areas = strs.split(",");
		List addresses = new LinkedList();
		for (int i = 0; i < areas.length; i++) {
			String[] str = areas[i].split(":");
			addresses.add(str[0] + ":" + str[1]);
		}
		return addresses;
	}

	public String getFourAreaId(String id, String address) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.fourAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		return jsonResult.getString(address);
	}

	public List<String> getAllFourAreaByParentId(String id) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("id", id);

		String httpResult = this.httpClientUtil.doPost(this.fourAreaUrl, createMap, this.charset);

		JSONObject json_parent = JSONObject.fromObject(httpResult);
		boolean success = json_parent.getBoolean("success");
		if (!success) {
			String resultMessage = json_parent.getString("resultMessage");
			logger.error("JDAreaError:" + resultMessage);
			return null;
		}
		JSONObject jsonResult = JSONObject.fromObject(json_parent.get("result"));
		String strs = jsonResult.toString();
		strs = strs.replace("{", "");
		strs = strs.replace("}", "");
		strs = strs.replace("\"", "");
		if ((strs == null) || ("".equals(strs))) {
			return null;
		}
		String[] areas = strs.split(",");
		List addresses = new LinkedList();
		for (int i = 0; i < areas.length; i++) {
			String[] str = areas[i].split(":");
			addresses.add(str[0] + ":" + str[1]);
		}
		return addresses;
	}

}
