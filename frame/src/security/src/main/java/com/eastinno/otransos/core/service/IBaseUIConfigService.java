package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.BaseUIConfig;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface IBaseUIConfigService
{
  public abstract Long addBaseUIConfig(BaseUIConfig paramBaseUIConfig);

  public abstract BaseUIConfig getBaseUIConfig(Long paramLong);

  public abstract boolean delBaseUIConfig(Long paramLong);

  public abstract boolean batchDelBaseUIConfigs(List<Serializable> paramList);

  public abstract IPageList getBaseUIConfigBy(IQueryObject paramIQueryObject);

  public abstract boolean updateBaseUIConfig(Long paramLong, BaseUIConfig paramBaseUIConfig);
}

