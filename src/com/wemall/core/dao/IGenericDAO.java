package com.wemall.core.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGenericDAO<T> {

    public abstract T get(Serializable paramSerializable);

    public abstract void save(T paramT);

    public abstract void remove(Serializable paramSerializable);

    public abstract void update(T paramT);

    public abstract int update(String paramString, Map<String, Object> paramMap);

    public abstract T query1(String paramString, Map<String, Object> paramMap);

    public abstract T find1(String paramString, Map<String, Object> paramMap);

    public abstract T getBy(String paramString, Object paramObject);

    public abstract List executeNamedQuery(String paramString, Object[] paramArrayOfObject,
        int paramInt1, int paramInt2);

    public abstract List<T> find(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract List<T> find(String paramString, Map<String, Object> paramMap);

    public abstract List query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract List<T> query(String paramString, Map<String, Object> paramMap);

    public abstract int batchUpdate(String paramString, Object[] paramArrayOfObject);

    public abstract List executeNativeNamedQuery(String paramString);

    public abstract List executeNativeQuery(String paramString, Object[] paramArrayOfObject,
        int paramInt1, int paramInt2);

    public abstract int executeNativeSQL(String paramString);

    public abstract void flush();

    public abstract Object queryObj(String paramString, Map<String, Object> paramMap);

    public abstract List<Object> queryObjs(String paramString, Map<String, Object> paramMap);
}
