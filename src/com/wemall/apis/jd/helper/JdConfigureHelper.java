package com.wemall.apis.jd.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wemall.apis.jd.dao.util.JDBase;
import com.wemall.apis.jd.model.configure.JdConfigure;
import com.wemall.apis.jd.service.JdConfigureService;
import com.wemall.apis.jdutil.HttpClientUtil;

@Component
public class JdConfigureHelper {

	@Autowired
	private JdConfigureService jdconfigureService;
	private String url = "https://bizapi.jd.com/oauth2/refreshToken?";

	private HttpClientUtil httpClientUtil = null;

	private String charset = "utf-8";

	public JdConfigureHelper() {
		this.httpClientUtil = new HttpClientUtil();
	}

	public void update() {
		Map createMap = new HashMap();
		createMap.put("refresh_token", "Zi0VkZKd3yVj62ZNFbIbTqULjzRESdfWiajItfRP");
		createMap.put("client_id", "06ckaHwMWShimcbsc4Bf");
		createMap.put("client_secret", "7vVBIS0km8dCd1jynpJU");

		String httpOrgCreateTestRtn = this.httpClientUtil.doPost(this.url, createMap, this.charset);

		System.out.println(httpOrgCreateTestRtn);

		JSONObject json = JSONObject.fromObject(httpOrgCreateTestRtn);

		JSONObject json2 = JSONObject.fromObject(json.get("result"));

		JSONArray jsonArray = JSONArray.fromObject(json2);

		Iterator it = jsonArray.iterator();

		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();

			System.out.println(ob.getString("access_token"));

			JdConfigure jdconfigure = new JdConfigure();
			jdconfigure.setId(1L);
			jdconfigure.setClientid("06ckaHwMWShimcbsc4Bf");
			jdconfigure.setClientSecret("7vVBIS0km8dCd1jynpJU");
			jdconfigure.setRefreshToken(ob.getString("refresh_token"));
			jdconfigure.setAppToken(ob.getString("access_token"));

			this.jdconfigureService.update(jdconfigure);
		}

		JDBase.setAccessToken();
		System.out.println("jDBase:" + JDBase.accessToken);
	}
}
