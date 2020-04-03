package com.wemall.pay.bill.util;

import com.csii.payment.client.core.MerchantSignTool;
import com.csii.payment.client.entity.SignParameterObject;
import com.wemall.pay.tenpay.util.TenpayUtil;
import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component
public class BillCore {

    private Map parameters=new LinkedHashMap();



    public static String appendParam(String returnStr, String paramId, String paramValue) {
        if (!returnStr.equals("")) {
            if (!paramValue.equals("")) {
                returnStr = returnStr + "&" + paramId + "=" + paramValue;
            }
        } else if (!paramValue.equals("")) {
            returnStr = paramId + "=" + paramValue;
        }

        return returnStr;
    }

    public void clearParameter(){
        this.parameters.clear();
    }

    public  void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (parameterValue != null) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    public Map<String,String> createSign_gd() {
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
        return this.parameters;
    }


}




