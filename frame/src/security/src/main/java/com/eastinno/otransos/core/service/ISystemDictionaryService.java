package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface ISystemDictionaryService
{
  public abstract Long addSystemDictionary(SystemDictionary paramSystemDictionary);

  public abstract SystemDictionary getSystemDictionary(Long paramLong);

  public abstract boolean delSystemDictionary(Long paramLong);

  public abstract boolean batchDelSystemDictionarys(List<Serializable> paramList);

  public abstract IPageList getSystemDictionaryBy(IQueryObject paramIQueryObject);

  public abstract boolean updateSystemDictionary(SystemDictionary paramSystemDictionary);

  public abstract SystemDictionary getBySn(String paramString);

  public abstract void addSystemDictionarValue(String paramString, SystemDictionaryDetail paramSystemDictionaryDetail);
}

