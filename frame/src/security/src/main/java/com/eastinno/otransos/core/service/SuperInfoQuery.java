package com.eastinno.otransos.core.service;

import java.util.List;

import com.eastinno.otransos.core.support.query.QueryObject;

public abstract class SuperInfoQuery
{
  protected Integer number;
  protected boolean elite;
  protected String orderBy;
  protected String orderByType;
  protected int page;
  protected int pageSize;
  protected List list;

  protected void parseProperty(String properties)
  {
    String[] p = properties.split(";");
    for (int i = 0; i < p.length; i++)
      if ((p[i] != null) && (!"".equals(p[i]))) {
        String pro = p[i].trim();
        String[] pvalue = new String[2];
        if (pro.indexOf(":") > 0)
          pvalue = pro.split(":");
        else
          pvalue[0] = pro;
        pvalue[0] = pvalue[0].trim();
        if (pvalue[1] != null)
          pvalue[1] = pvalue[1].trim();
        if ("number".equalsIgnoreCase(pvalue[0]))
          number(Integer.parseInt(pvalue[1]));
        else if ("elite".equalsIgnoreCase(pvalue[0]))
          elite();
        else if (("orderBy".equalsIgnoreCase(pvalue[0])) || 
          ("order".equalsIgnoreCase(pvalue[0])))
          orderBy(pvalue[1]);
        else if (("orderByType".equalsIgnoreCase(pvalue[0])) || 
          ("orderType".equalsIgnoreCase(pvalue[0])))
          this.orderByType = pvalue[1];
        else if ("page".equalsIgnoreCase(pvalue[0]))
          page(Integer.parseInt(pvalue[1]));
        else if ("pageSize".equalsIgnoreCase(pvalue[0]))
          pageSize(Integer.parseInt(pvalue[1]));
        else if ("desc".equalsIgnoreCase(pvalue[0]))
          desc();
        else if ("asc".equalsIgnoreCase(pvalue[0]))
          asc();
      }
  }

  protected String getPropertyValue(String properties, String name)
  {
    String ret = null;
    String[] p = properties.split(";");
    for (int i = 0; i < p.length; i++) {
      if ((p[i] != null) && (!"".equals(p[i]))) {
        String pro = p[i].trim();
        String[] pvalue = new String[2];
        if (pro.indexOf(":") > 0) {
          pvalue = pro.split(":");
        }
        else
          pvalue[0] = pro;
        pvalue[0] = pvalue[0].trim();
        if (pvalue[1] != null)
          pvalue[1] = pvalue[1].trim();
        if (name.equalsIgnoreCase(pvalue[0])) {
          ret = pvalue[1];
        }
      }
    }
    return ret;
  }

  protected boolean hasProperty(String properties, String name) {
    boolean ret = false;
    String[] p = properties.split(";");
    for (int i = 0; i < p.length; i++) {
      if ((p[i] != null) && (!"".equals(p[i]))) {
        String pro = p[i].trim();
        String[] pvalue = new String[2];
        if (pro.indexOf(":") > 0) {
          pvalue = pro.split(":");
        }
        else
          pvalue[0] = pro;
        pvalue[0] = pvalue[0].trim();
        if (pvalue[1] != null)
          pvalue[1] = pvalue[1].trim();
        if (name.equalsIgnoreCase(pvalue[0])) {
          ret = true;
        }
      }
    }
    return ret;
  }

  public SuperInfoQuery number(int number)
  {
    this.number = Integer.valueOf(number);
    return this;
  }

  public SuperInfoQuery num(int num) {
    return number(num);
  }
  public SuperInfoQuery elite() {
    this.elite = true;
    return this;
  }

  public SuperInfoQuery orderBy(String orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public SuperInfoQuery asc() {
    this.orderByType = "asc";
    return this;
  }

  public SuperInfoQuery desc() {
    this.orderByType = "desc";
    return this;
  }

  public SuperInfoQuery page(int page) {
    this.page = page;
    return this;
  }

  public SuperInfoQuery pageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  protected void parseQueryObject(QueryObject queryObject) {
    if (this.elite)
      queryObject.addQuery("elite", Boolean.valueOf(true), "=");
    if ((this.orderBy != null) && (!"".equals(this.orderBy))) {
      queryObject.setOrderBy(this.orderBy);
      queryObject.setOrderType(this.orderByType);
    }
    if (this.number != null)
      queryObject.setPageSize(this.number);
    if (this.page < 1)
      queryObject.setCurrentPage(Integer.valueOf(1));
    else
      queryObject.setCurrentPage(Integer.valueOf(this.page));
  }

  public List getList() {
    if (this.list == null) {
      try {
        this.list = loadList();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.list;
  }

  protected abstract List loadList();
}

