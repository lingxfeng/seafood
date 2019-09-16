package com.eastinno.otransos.core.support;

import java.util.Collection;
import java.util.Locale;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.support.query.GenericPageList;
import com.eastinno.otransos.web.LocalManager;

public class GenericPageListForI18n extends GenericPageList {
    public GenericPageListForI18n(Class cls, String scope, Collection paras, IJpaGenericDAO dao) {
        super(cls, scope, paras, dao);
    }

    public void doList(int currentPage, int pageSize, boolean i18nEnable) {
        if (!i18nEnable) {
            super.doList(currentPage, pageSize);
        } else {
            Locale local = LocalManager.getCurrentLocal();
            String localName = local.getLanguage().toUpperCase();
            String realClassName = cls.getName() + localName;
            String totalSql = "select COUNT(o) from " + realClassName + " o where " + queryStr;
            super.doList(pageSize, currentPage, totalSql, queryStr);
        }
    }

}
