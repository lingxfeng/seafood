package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface ISystemDictionaryDetailService
{
  public abstract Long addSystemDictionaryDetail(SystemDictionaryDetail paramSystemDictionaryDetail);

  public abstract SystemDictionaryDetail getSystemDictionaryDetail(Long paramLong);

  public abstract boolean delSystemDictionaryDetail(Long paramLong);

  public abstract boolean batchDelSystemDictionaryDetails(List<Serializable> paramList);

  public abstract IPageList getSystemDictionaryDetailBy(IQueryObject paramIQueryObject);

  public abstract boolean updateSystemDictionaryDetail(SystemDictionaryDetail paramSystemDictionaryDetail);

  public abstract List<SystemDictionaryDetail> getDetailsByDictionarySn(String paramString);
}

