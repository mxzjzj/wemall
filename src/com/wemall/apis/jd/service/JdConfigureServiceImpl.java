package com.wemall.apis.jd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.wemall.core.dao.IGenericDAO;
import com.wemall.apis.jd.model.configure.JdConfigure;

@Service
@Transactional
public class JdConfigureServiceImpl implements JdConfigureService {

	@Resource(name = "jdConfigureDao")
	private IGenericDAO<JdConfigure> jdConfigureDao;

	public void update(JdConfigure jdConfigure) {
		 this.jdConfigureDao.update(jdConfigure);
	}

	public JdConfigure getObj() {
		return (JdConfigure) this.jdConfigureDao.get(Long.valueOf(1L));
	}

}
