package com.eastinno.otransos.security.service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface ISystemLogService {
    public abstract void addSystemLog(SystemLog paramSystemLog);

    public abstract void updateSystemLog(SystemLog paramSystemLog);

    public abstract void delSystemLog(Long paramLong);

    public abstract IPageList getSystemLogBy(IQueryObject paramIQueryObject);

    public abstract void batchDelSystemLog(IQueryObject paramIQueryObject);

    public abstract void exportSystemLog(IQueryObject paramIQueryObject) throws Exception;
}
