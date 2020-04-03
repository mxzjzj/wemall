package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Group;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGroupService {
    public abstract boolean save(Group paramGroup);

    public abstract Group getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Group paramGroup);

    public abstract List<Group> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




