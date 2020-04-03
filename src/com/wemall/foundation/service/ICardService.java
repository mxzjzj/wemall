package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Card;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICardService {
    public abstract boolean save(Card paramCard);

    public abstract Card getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Card paramGoods);

    public abstract List<Card> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract Card getObjByProperty(String paramString, Object paramObject);

    public abstract int removeObjs(List<Serializable> ids);


}
