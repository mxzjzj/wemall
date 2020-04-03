package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.IntegralGoodsCart;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IIntegralGoodsCartService {
    public abstract boolean save(IntegralGoodsCart paramIntegralGoodsCart);

    public abstract IntegralGoodsCart getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(IntegralGoodsCart paramIntegralGoodsCart);

    public abstract List<IntegralGoodsCart> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




