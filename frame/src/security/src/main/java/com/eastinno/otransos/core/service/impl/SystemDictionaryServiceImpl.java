package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.dao.ISystemDictionaryDAO;
import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
    @Resource
    private ISystemDictionaryDAO systemDictionaryDao;

    public void setSystemDictionaryDao(ISystemDictionaryDAO systemDictionaryDao) {
        this.systemDictionaryDao = systemDictionaryDao;
    }

    public Long addSystemDictionary(SystemDictionary systemDictionary) {
        this.systemDictionaryDao.save(systemDictionary);
        if ((systemDictionary != null) && (systemDictionary.getId() != null)) {
            return systemDictionary.getId();
        }
        return null;
    }

    public SystemDictionary getSystemDictionary(Long id) {
        SystemDictionary systemDictionary = (SystemDictionary) this.systemDictionaryDao.get(id);
        if (systemDictionary != null) {
            return systemDictionary;
        }
        return null;
    }

    public boolean delSystemDictionary(Long id) {
        this.systemDictionaryDao.remove(id);
        SystemDictionary systemDictionary = getSystemDictionary(id);
        if (systemDictionary == null) {
            return true;
        }
        return false;
    }

    public boolean batchDelSystemDictionarys(List<Serializable> systemDictionaryIds) {
        for (Serializable id : systemDictionaryIds) {
            delSystemDictionary((Long) id);
        }
        return true;
    }

    public IPageList getSystemDictionaryBy(IQueryObject properties) {
        if (properties == null) {
            properties = new QueryObject();
        }
        return QueryUtil.query(properties, SystemDictionary.class, this.systemDictionaryDao);
    }

    public boolean updateSystemDictionary(SystemDictionary systemDictionary) {
        if (systemDictionary != null) {
            this.systemDictionaryDao.update(systemDictionary);
        }
        return true;
    }

    public SystemDictionary getBySn(String sn) {
        if ((sn != null) && (!"".equals(sn))) {
            return (SystemDictionary) this.systemDictionaryDao.getBy("sn", sn);
        }
        return null;
    }

    public void addSystemDictionarValue(String sn, SystemDictionaryDetail detail) {
        SystemDictionary dictionary = getBySn(sn);
        if ((dictionary != null) && (!"".equals(detail.getTitle()))) {
            if (detail.getTvalue() == null)
                detail.setTvalue(detail.getTitle());
            dictionary.addChild(detail);
        }
    }
}
