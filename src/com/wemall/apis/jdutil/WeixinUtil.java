package com.wemall.apis.jdutil;

import com.wemall.apis.jd.service.WxConfigureService;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by zhangjun on 2017-07-16.
 */
public class WeixinUtil {

    @Autowired
    private WxConfigureService wxConfigureService;

    public static final String APPID = "wxa04f48958fc044f3";
    public static final String APPSECRET = "84b9fa3dc09e72c2f11fa6ddd60222bb";
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";//?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static final String ACCESS_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";//appid=APPID&secret=APPSECRET&code=code&grant_type=authorization_code";
    public static final String USER_INFO="https://api.weixin.qq.com/cgi-bin/user/info";//?access_token=TOKEN&openid=OPENID&lang=zh_CN";
    public static final String WECHAT_LOGIN_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * get请求
     *
     * @param url
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String doGet(String url)  {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String result=null;
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * POST请求
     *
     * @param url
     * @param outStr
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public JSONObject doPostStr(String url, String outStr) throws ParseException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpost.setEntity(new StringEntity(outStr, "UTF-8"));
        HttpResponse response = client.execute(httpost);
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        jsonObject = JSONObject.fromObject(result);
        return jsonObject;
    }

    /**
     * 获取accessToken
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public  static AccessToken getAccessToken(){
        AccessToken token = new AccessToken();//?grant_type=client_credential&appid=APPID&secret=APPSECRET"
        String tokenurl =ACCESS_TOKEN_URL+"?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
        String result = doGet(tokenurl);
        JSONObject json=JSONObject.fromObject(result);
        if(result!=null){
            token.setToken(json .getString("access_token"));
            token.setExpiresIn(json.getInt("expires_in"));
        }
        return token;
    }

    public static String getOpenid (String code){
        String openid="";
        String url = ACCESS_OPENID_URL+"?grant_type=authorization_code&appid="+APPID+"&secret="+APPSECRET+"&code="+code;////appid=APPID&secret=APPSECRET&code=code&grant_type=authorization_code";
        String result = doGet(url);
        if(result!=null){
            return JSONObject.fromObject(result).getString("openid");
        }
        return openid;
    }

    public static String getUserInfo(String token,String openid){
        String url = USER_INFO+"?access_token="+token+"&openid="+openid+"&lang=zh_CN";//?access_token=TOKEN&openid=OPENID&lang=zh_CN";
        return doGet(url);
    }

}
