package com.eastinno.otransos.web.tools;

import java.util.Collection;
import java.util.List;

import com.eastinno.otransos.web.tools.IQuery;

/**
 * 分页列表查询算法实现，当前功能还不全
 * 
 * @author lengyu
 */
public class ListQuery implements IQuery {
    private int begin = 0;

    private int max = 0;

    private List list = null;

    public ListQuery() {

    }

    public ListQuery(List l) {
        if (l != null) {
            this.list = l;
            this.max = l.size();
        }
    }

    public void initList(List l) {
        this.list = l;
        this.max = l.size();
    }

    public int getRows(String conditing) {

        return (list == null ? 0 : list.size());
    }

    public List getResult(String conditing) {
        return list.subList(begin, begin + max > list.size() ? list.size() : begin + max);
    }

    public void setFirstResult(int begin) {
        this.begin = list.size() < begin ? list.size() : begin;
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public List getResult(String conditing, int begin, int max) {

        return list;
    }

    public void setParaValues(Collection paraValues) {

    }
}
