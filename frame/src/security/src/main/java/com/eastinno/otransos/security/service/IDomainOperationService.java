package com.eastinno.otransos.security.service;

import java.io.Serializable;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.DomainOperationLog;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface IDomainOperationService {
    public abstract void record(DomainOperationLog paramDomainOperationLog);

    public abstract void record(Object paramObject, Integer paramInteger);

    public abstract void record(Object paramObject, Integer paramInteger, User paramUser);

    public abstract DomainOperationLog getDomainOperationLog(Long paramLong);

    public abstract <T> T getObject(Class<T> paramClass, Serializable paramSerializable, Integer paramInteger);

    public abstract <T> T getObject(Class<T> paramClass, Serializable paramSerializable);

    public abstract void revert(Class paramClass, Serializable paramSerializable, Integer paramInteger);

    public abstract IPageList getDomainOperationLogBy(IQueryObject paramIQueryObject);
}
