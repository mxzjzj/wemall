package com.wemall.pay.tenpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.csii.payment.client.core.MerchantSignTool;
import com.csii.payment.client.entity.SignParameterObject;
import com.wemall.pay.tenpay.util.MD5Util;
import com.wemall.pay.tenpay.util.TenpayUtil;

public class RequestHandler {
    private String gateUrl;
    private String key;
    private SortedMap parameters;
    private String debugInfo;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
        this.key = "";
        this.parameters = new TreeMap();
        this.debugInfo = "";
    }

    public void init() {
    }

    public String getGateUrl() {
        return this.gateUrl;
    }

    public void setGateUrl(String gateUrl) {
        this.gateUrl = gateUrl;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParameter(String parameter) {
        String s = (String)this.parameters.get(parameter);
        return s == null ? "" : s;
    }

    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (parameterValue != null) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    public SortedMap getAllParameters() {
        return this.parameters;
    }

    public String getDebugInfo() {
        return this.debugInfo;
    }

    public String getRequestURL()  throws UnsupportedEncodingException {
        createSign();

        StringBuffer sb = new StringBuffer();
        String enc = TenpayUtil.getCharacterEncoding(this.request,
                     this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
        }

        String reqPars = sb.substring(0, sb.lastIndexOf("&"));

        return getGateUrl() + "?" + reqPars;
    }

    //zhangjun
    public String getRequestURL_GD()  throws UnsupportedEncodingException {
        createSign_gd();

        StringBuffer sb = new StringBuffer();
        String enc = TenpayUtil.getCharacterEncoding(this.request,
                this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append(k + "=" + v + "&");//URLEncoder.encode(v, enc)
        }

        String reqPars = sb.substring(0, sb.lastIndexOf("&"));

        return getGateUrl() + "?" + reqPars;
    }

    protected void createSign_gd() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ((v == null) || ("".equals(v)) || ("sign".equals(k)) ||
                    ("key".equals(k))) continue;
            sb.append(k + "=" + v + "~|~");
        }

        String enc = TenpayUtil.getCharacterEncoding(this.request,this.response);
//        String plain = MD5Util.MD5Encode(sb.toString(), enc);
        String plain=sb.substring(0,sb.lastIndexOf("~|~"));

        SignParameterObject signParam=new SignParameterObject();
        signParam.setMerchantId("370310000004");//商户号
        signParam.setPlain(plain);//明文
        signParam.setCharset("GBK");//明文使用的字符集
        signParam.setType(0);//0-普通报文
        signParam.setAlgorithm("MD5withRSA");//签名算法
        String sign= null;
        try {
            sign = MerchantSignTool.sign(signParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.parameters.clear();
        setParameter("Plain",plain);
        setParameter("Signature", sign);
        setParameter("TransName","IPER");
        setDebugInfo(sb.toString() + " => sign:" + sign);
    }

    public void doSend() throws UnsupportedEncodingException, IOException {
        this.response.sendRedirect(getRequestURL());
    }

    protected void createSign() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ((v == null) || ("".equals(v)) || ("sign".equals(k)) ||
                    ("key".equals(k))) continue;
            sb.append(k + "=" + v + "&");
        }

        sb.append("key=" + getKey());

        String enc = TenpayUtil.getCharacterEncoding(this.request,
                     this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

        setParameter("sign", sign);

        setDebugInfo(sb.toString() + " => sign:" + sign);
    }



    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    protected HttpServletResponse getHttpServletResponse() {
        return this.response;
    }
}




