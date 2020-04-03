package com.wemall.manage.quartz;

import com.wemall.apis.jd.helper.WxConfigureHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangjun on 2017-07-16.
 */
public class GetWxTokenQuartz {

    @Autowired
    private WxConfigureHelper wxConfigureHelper;

    public void execute() throws Exception {
        this.wxConfigureHelper.update();
    }
}
