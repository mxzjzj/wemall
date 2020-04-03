package com.wemall.apis.jd.helper;

import com.wemall.apis.jd.model.configure.WxConfigure;
import com.wemall.apis.jd.service.WxConfigureService;
import com.wemall.apis.jdutil.AccessToken;
import com.wemall.apis.jdutil.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangjun on 2017-07-16.
 */
@Component
public class WxConfigureHelper {

    @Autowired
    private WxConfigureService wxConfigureService;


    public void update() {
        AccessToken accessToken=WeixinUtil.getAccessToken();
        WxConfigure configure=new WxConfigure();
        configure.setId(1);
        configure.setToken(accessToken.getToken());
        this.wxConfigureService.update(configure);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+" weixin token"+accessToken.getToken());

    }
}
