package com.wemall.core.query.support;

import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.query.PageObject;
import java.util.Map;

public abstract interface IQueryObject {
    public abstract String getQuery();

    public abstract Map getParameters();

    public abstract PageObject getPageObj();

    public abstract IQueryObject addQuery(String paramString, Map paramMap);

    public abstract IQueryObject addQuery(String paramString1, SysMap paramSysMap, String paramString2);

    public abstract IQueryObject addQuery(String paramString1, Object paramObject, String paramString2, String paramString3);
}