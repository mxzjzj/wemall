package com.wemall.core.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SmsBase {
	private String url;
	private String id;
	private String pwd;

	private String host;
	private String account;
	private String password;
	private String signature;

	public SmsBase(String url, String id, String pwd) {
		this.url = url;
		this.id = id;
		this.pwd = pwd;
	}

	public SmsBase(String host, String account, String password, String signature) {
		super();
		this.host = host;
		this.account = account;
		this.password = password;
		this.signature = signature;
	}

	public String SendSms(String mobile, String content) throws UnsupportedEncodingException {
		Integer x_ac = Integer.valueOf(10);
		HttpURLConnection httpconn = null;
		String result = "-20";
		String memo = content.length() < 70 ? content.trim() : content.trim().substring(0, 70);
		StringBuilder sb = new StringBuilder();
		sb.append(this.url);
		sb.append("?id=").append(this.id);
		sb.append("&pwd=").append(this.pwd);
		sb.append("&to=").append(mobile);
		sb.append("&content=").append(URLEncoder.encode(content, "gb2312"));
		try {
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
			result = rd.readLine();
			rd.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}
		}

		return result;
	}

	public boolean sendSms(String mobile, String content)
			throws DocumentException, UnsupportedEncodingException, HttpException, IOException {
		if (StringUtils.isBlank(this.signature) || StringUtils.isBlank(this.host) || StringUtils.isBlank(this.account)
				|| StringUtils.isBlank(this.password)) {
			return false;
		}

		content = signature + content;
		StringBuffer url = new StringBuffer(this.host);
		url.append("?uid=").append(this.account);
		url.append("&pwd=").append(this.password);
		url.append("&mobiles=").append(mobile);
		url.append("&msg=").append(URLEncoder.encode(content, "UTF-8"));
		url.append("&sendTime=");

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url.toString());
		client.executeMethod(method);

		String response = method.getResponseBodyAsString().replaceAll("^[^<]", "");
		Document document = DocumentHelper.parseText(response);
		Element root = document.getRootElement();

		String status = root.elementText("Status");
		if (status.equals("0")) {
			return true;
		}
		return false;
	}
}
