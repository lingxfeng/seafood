package com.eastinno.otransos.web.tools;

import java.util.Collection;
import java.util.List;

import com.eastinno.otransos.web.tools.IQuery;

/**
 * 通用的数据库查询器，根据数据库操作对象DAO执行数据库分页查询操作
 * 
 * @author lengyu
 */
public class DbQuery implements IQuery {

    private IDAO dao;

    private int begin;

    private int max;

    private Collection paraValues;

    private Class cls;

    public DbQuery(IDAO dao, Class cls) {
        this.dao = dao;
        this.cls = cls;
    }

    public int getRows(String conditing) {
        int n = conditing.toLowerCase().indexOf("order by");
        String totalSql = conditing;
        if (n > 0)
            totalSql = conditing.substring(0, n);
        int total = ((Number) dao.uniqueResult(totalSql, paraValues)).intValue();
        return total;
    }

    public List getResult(String conditing) {
        return dao.query(cls, conditing, paraValues, begin, max);
    }

    public void setFirstResult(int begin) {
        this.begin = begin;
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public List getResult(String conditing, int begin, int max) {
        return dao.query(cls, conditing, paraValues, begin, max);
    }

    public void setParaValues(Collection paraValues) {
        this.paraValues = paraValues;
    }
}
