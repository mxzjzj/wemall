package com.wemall.apis.jd.service;

import com.wemall.apis.jd.model.configure.WxConfigure;

/**
 * Created by zhangjun on 2017-07-16.
 */
public interface WxConfigureService {
    abstract void update(WxConfigure paramWxConfigure);

    abstract WxConfigure getObj();
}
