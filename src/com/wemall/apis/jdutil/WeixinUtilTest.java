package com.wemall.apis.jdutil;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeixinUtilTest {

    @Test
    public void gettoken(){
        System.out.println(WeixinUtil.getAccessToken().getToken());
    }

}