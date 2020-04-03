package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Transport;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface ITransportService {
    public abstract boolean save(Transport paramTransport);

    public abstract Transport getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Transport paramTransport);

    public abstract List<Transport> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




