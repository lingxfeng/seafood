package com.eastinno.otransos.ext.platform.support.query;

import java.util.Collection;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.web.tools.IQuery;
import com.eastinno.otransos.web.tools.PageList;

public class GenericPageList extends PageList {
    protected Class cls;
    protected String queryStr;

    public GenericPageList(Class cls, IQueryObject queryObject, IJpaGenericDAO dao) {
        this(cls, queryObject.getQuery(), queryObject.getParameters(), dao);
    }

    public GenericPageList(Class cls, String queryStr, Collection paras, IJpaGenericDAO dao) {
        this.cls = cls;
        this.queryStr = queryStr;
        IQuery query = new GenericQuery(dao);
        query.setParaValues(paras);
        this.setQuery(query);
    }

    /**
     * 查询
     * 
     * @param currentPage 当前页数
     * @param pageSize 一页的查询个数
     */
    public void doList(int currentPage, int pageSize) {
        String totalSql = "select COUNT(obj) from " + cls.getName() + " obj where " + queryStr;
        super.doList(pageSize, currentPage, totalSql, queryStr);
    }

}
