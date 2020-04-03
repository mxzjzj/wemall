package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.UserConfig;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IUserConfigService {
    public abstract boolean save(UserConfig paramUserConfig);

    public abstract UserConfig getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(UserConfig paramUserConfig);

    public abstract List<UserConfig> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract UserConfig getUserConfig();
}




