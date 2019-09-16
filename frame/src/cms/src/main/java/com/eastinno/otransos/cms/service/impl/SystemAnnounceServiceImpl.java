package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ISystemAnnounceDAO;
import com.eastinno.otransos.cms.domain.SystemAnnounce;
import com.eastinno.otransos.cms.service.ISystemAnnounceService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SystemAnnounceServiceImpl
 */
@Service
public class SystemAnnounceServiceImpl implements ISystemAnnounceService {
    @Resource
    private ISystemAnnounceDAO systemAnnounceDao;

    public void setSystemAnnounceDao(ISystemAnnounceDAO systemAnnounceDao) {
        this.systemAnnounceDao = systemAnnounceDao;
    }

    public Long addSystemAnnounce(SystemAnnounce systemAnnounce) {
    	 TenantObjectUtil.setObject(systemAnnounce);
        this.systemAnnounceDao.save(systemAnnounce);
        if (systemAnnounce != null && systemAnnounce.getId() != null) {
            return systemAnnounce.getId();
        }
        return null;
    }

    public SystemAnnounce getSystemAnnounce(Long id) {
        SystemAnnounce systemAnnounce = this.systemAnnounceDao.get(id);
        if (!TenantObjectUtil.checkObjectService(systemAnnounce)) {
            throw new LogicException("非法数据访问");
        }
        return systemAnnounce;
    }

    public SystemAnnounce readSystemAnnounce(Long id) {
        return this.systemAnnounceDao.get(id);
    }

    public boolean delSystemAnnounce(Long id) {
        SystemAnnounce systemAnnounce = this.getSystemAnnounce(id);
        if (!TenantObjectUtil.checkObjectService(systemAnnounce)) {
            throw new LogicException("非法数据访问");
        }
        if (systemAnnounce != null) {
            this.systemAnnounceDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelSystemAnnounces(List<Serializable> systemAnnounceIds) {

        for (Serializable id : systemAnnounceIds) {
            delSystemAnnounce((Long) id);
        }
        return true;
    }

    public IPageList getSystemAnnounceBy(IQueryObject qo) {
    	TenantObjectUtil.addQuery(qo);
        return this.systemAnnounceDao.findBy(qo);
    }

    public boolean updateSystemAnnounce(Long id, SystemAnnounce systemAnnounce) {
        if (id != null) {
            systemAnnounce.setId(id);
        } else {
            return false;
        }
        if (systemAnnounce.getTenant() == null) {
            TenantObjectUtil.setObject(systemAnnounce);
        }
        this.systemAnnounceDao.update(systemAnnounce);
        return true;
    }

}
