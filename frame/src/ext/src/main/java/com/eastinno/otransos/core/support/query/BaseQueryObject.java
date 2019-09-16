package com.eastinno.otransos.core.support.query;

/**
 * 基础的查询对象类，包装了page信息和order信息
 * 
 * @author stefanie wu
 */
public class BaseQueryObject extends QueryObject {
    protected boolean isDel = true;

    protected boolean all = false;

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean isDel) {
        this.isDel = isDel;
    }

    @Override
    public String getQuery() {
        if (!all) {
            if (isDel) {
                addQuery("obj.status >= 0", null);
            } else {
                addQuery("obj.status = -1", null);
            }
        }
        return super.getQuery();
    }
}
