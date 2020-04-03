package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.CardItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ICardItemService {
    public abstract boolean save(CardItem paramCard);

    public abstract CardItem getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(CardItem paramGoods);

    public abstract List<CardItem> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract CardItem getObjByProperty(String paramString, Object paramObject);

    public abstract long getCountByCard(long cardId);

    public abstract String getNumberByPrefixMax(String prefix);

    public abstract int removeObjsByCardNoOwner(List<Serializable> cardIds);
}
