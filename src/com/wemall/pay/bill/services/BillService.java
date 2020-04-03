package com.wemall.pay.bill.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wemall.pay.bill.config.BillConfig;

public class BillService {
    private static final String BILL_GATEWAY_NEW = "https://www.99bill.com/gateway/recvMerchantInfoAction.htm";

    public static String buildForm(BillConfig config, Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        List keys = new ArrayList(sParaTemp.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"99billsubmit\" name=\"99billsubmit\" action=\"https://www.99bill.com/gateway/recvMerchantInfoAction.htm\" method=\"" +strMethod + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name +"\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName +"\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['99billsubmit'].submit();</script>");
        return sbHtml.toString();
    }

    public static String buildForm_gd(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        List keys = new ArrayList(sParaTemp.keySet());
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<form id=\"submitForm\" name=\"submitForm\"  action=\"https://111.205.51.141/per/preEpayLogin.do?_locale=zh_CN\" method=\"" + strMethod + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\"  style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['submitForm'].submit();</script>");
        return sbHtml.toString();
    }
}




