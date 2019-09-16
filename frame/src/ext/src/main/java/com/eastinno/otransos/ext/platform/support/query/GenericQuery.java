package com.eastinno.otransos.ext.platform.support.query;

import java.util.Collection;
import java.util.List;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.web.tools.IQuery;

public class GenericQuery implements IQuery {
    private int max;
    private int begin;
    private IJpaGenericDAO dao;
    private Collection paraValues;

    public GenericQuery(IJpaGenericDAO dao) {
        this.dao = dao;
    }

    public List getResult(String queryStr) {
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        return dao.find(queryStr, params, begin, max);
    }

    public List getResult(String queryStr, int begin, int max) {
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        return this.dao.find(queryStr, params, begin, max);
    }

    public int getRows(String queryStr) {
        int n = queryStr.toLowerCase().indexOf("order by");
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        if (n > 0) {
            queryStr = queryStr.substring(0, n);
        }
        List ret = dao.query(queryStr, params, 0, 0);
        if (ret != null && ret.size() > 0) {
            return ((Long) ret.get(0)).intValue();
        } else {
            return 0;
        }
    }

    public void setFirstResult(int begin) {
        this.begin = begin;
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public void setParaValues(Collection params) {
        this.paraValues = params;
    }

}
