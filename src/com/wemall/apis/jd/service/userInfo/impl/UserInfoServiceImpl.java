package com.wemall.apis.jd.service.userInfo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wemall.apis.jd.model.user.UserInfo;
import com.wemall.apis.jd.service.userInfo.UserInfoService;
import com.wemall.core.dao.IGenericDAO;

public class UserInfoServiceImpl implements UserInfoService {

	protected Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

	@Resource(name = "userInfoDao")
	private IGenericDAO<UserInfo> userInfoDao;

	public UserInfo getUserInfoById(Long id) {
		try {
			return (UserInfo) this.userInfoDao.get(id);
		} catch (Exception e) {
			this.logger.error("UserInfoServiceImpl:" + id + ":" + e);
		}
		return null;
	}

	public boolean updateUserInfo(UserInfo userInfo) {
		try {
			this.userInfoDao.update(userInfo);
			return true;
		} catch (Exception e) {
			this.logger.error("UserInfoServiceImpl:" + userInfo.getId() + ":" + e);
		}
		return false;
	}
}
