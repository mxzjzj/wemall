package com.wemall.foundation.service;

import com.wemall.foundation.CardType;

import java.util.List;
import java.util.Map;

public interface ICardTypeService {

    public abstract CardType getObjById(Long paramLong);

    public abstract List<CardType> getObjs();

    public abstract List<CardType> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}
