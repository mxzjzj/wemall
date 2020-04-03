package com.wemall.apis.jd.service;

import com.wemall.apis.jd.model.configure.JdConfigure;

public interface JdConfigureService {

	abstract void update(JdConfigure paramJdConfigure);

	abstract JdConfigure getObj();
}
