package com.eastinno.otransos.core.support;

import java.util.Collection;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.support.query.GenericPageList;

public class GenericPageListForObject extends GenericPageList {
    public GenericPageListForObject(Class cls, String scope, Collection paras, IJpaGenericDAO dao) {
        super(cls, scope, paras, dao);
    }

    public void doList(int currentPage, int pageSize) {
        String subScope = "";
        if (queryStr.indexOf("where") > 0) {
            subScope = queryStr.substring(queryStr.indexOf("where") + 5);
        }
        super.doList(pageSize, currentPage, queryStr, subScope);
    }
}
