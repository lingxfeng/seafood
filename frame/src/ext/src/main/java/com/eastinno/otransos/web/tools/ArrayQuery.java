package com.eastinno.otransos.web.tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.eastinno.otransos.web.tools.IQuery;

/**
 * 简单的数组对象查询，当前还不完善
 * 
 * @author lengyu
 */
public class ArrayQuery implements IQuery {
    private Arrays array = null;

    public ArrayQuery() {

    }

    public ArrayQuery(Arrays array) {
        this.array = array;
    }

    public void setArray(Arrays array) {
        this.array = array;
    }

    public int getRows(String conditing) {
        // TODO Auto-generated method stub
        // 随便写的代码
        return (array != null ? 1 : 0);
    }

    public List getResult(String conditing) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setFirstResult(int begin) {
        // TODO Auto-generated method stub

    }

    public void setMaxResults(int max) {
        // TODO Auto-generated method stub

    }

    public List getResult(String conditing, int begin, int max) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setParaValues(Collection paraValues) {
        // TODO Auto-generated method stub

    }

}
