package com.eastinno.otransos.core.support.query;

import java.util.Collection;
import java.util.List;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.web.tools.IQuery;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2010年08月13日 下午2:12:34
 * @Intro
 */
@SuppressWarnings({"rawtypes", "serial"})
public class GenericFieldQuery implements IQuery {
    private int max;
    private int begin;
    private IJpaGenericDAO dao;
    private Collection paraValues;

    public GenericFieldQuery(IJpaGenericDAO dao) {
        this.dao = dao;
    }

    public List getResult(String sql) {
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        return dao.query(sql, params, begin, max);
    }

    public List getResult(String sql, int begin, int max) {
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        return this.dao.query(sql, params, begin, max);
    }

    public int getRows(String sql) {
        int n = sql.toLowerCase().indexOf("order by");
        Object[] params = null;
        if (this.paraValues != null) {
            params = this.paraValues.toArray();
        }
        if (n > 0) {
            sql = sql.substring(0, n);
        }
        List ret = dao.query(sql, params, 0, 0);
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
