package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.Personality;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface IPersonalityService
{
  public abstract Long addPersonality(Personality paramPersonality);

  public abstract Personality getPersonality(Long paramLong);

  public abstract boolean delPersonality(Long paramLong);

  public abstract boolean batchDelPersonalitys(List<Serializable> paramList);

  public abstract IPageList getPersonalityBy(IQueryObject paramIQueryObject);

  public abstract boolean updatePersonality(Long paramLong, Personality paramPersonality);

  public abstract Personality getPersonality(User paramUser);

  public abstract Personality getDefaultPersonality();
}

