package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.StorePoint;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IStorePointService {
    public abstract boolean save(StorePoint paramStorePoint);

    public abstract StorePoint getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(StorePoint paramStorePoint);

    public abstract List<StorePoint> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




