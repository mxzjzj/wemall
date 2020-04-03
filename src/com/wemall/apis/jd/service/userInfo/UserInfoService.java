package com.wemall.apis.jd.service.userInfo;

import com.wemall.apis.jd.model.user.UserInfo;

public interface UserInfoService {
	public abstract UserInfo getUserInfoById(Long paramLong);

	  public abstract boolean updateUserInfo(UserInfo paramUserInfo);

}
