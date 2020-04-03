package com.wemall.apis.jd.service;

import com.wemall.apis.jd.model.configure.WxConfigure;
import com.wemall.core.dao.IGenericDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by zhangjun on 2017-07-16.
 */
@Service
@Transactional
public class WxConfigureServiceImpl implements WxConfigureService {

    @Resource(name = "wxConfigureDao")
    private IGenericDAO<WxConfigure> wxConfigureDao;

    @Override
    public void update(WxConfigure paramWxConfigure) {
        this.wxConfigureDao.update(paramWxConfigure);
    }

    @Override
    public WxConfigure getObj() {
        return this.wxConfigureDao.get(1);
    }
}
