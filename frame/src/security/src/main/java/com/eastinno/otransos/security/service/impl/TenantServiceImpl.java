package com.eastinno.otransos.security.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.ITenantDAO;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * TenantServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class TenantServiceImpl implements ITenantService {
    @Resource
    private ITenantDAO tenantDao;

    public void setTenantDao(ITenantDAO tenantDao) {
        this.tenantDao = tenantDao;
    }

    public Long addTenant(Tenant tenant) {
    	tenant.setDePath();
        this.tenantDao.save(tenant);
        if (tenant != null && tenant.getId() != null) {
            return tenant.getId();
        }
        return null;
    }

    @Override
    public Tenant getTenantByHost(String host) {
        return this.tenantDao.findByUrlLike(host);
    }

    public Tenant getTenant(Long id) {
        Tenant tenant = this.tenantDao.get(id);
        return tenant;
    }

    public boolean delTenant(Long id) {
        Tenant tenant = this.getTenant(id);
        if (tenant != null) {
            this.tenantDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelTenants(List<Serializable> tenantIds) {

        for (Serializable id : tenantIds) {
            delTenant((Long) id);
        }
        return true;
    }

    public IPageList getTenantBy(IQueryObject queryObj) {
        return this.tenantDao.findBy(queryObj);
    }

    public boolean updateTenant(Long id, Tenant tenant) {
    	tenant.setDePath();
        if (id != null) {
            tenant.setId(id);
        } else {
            return false;
        }
        this.tenantDao.update(tenant);
        return true;
    }

}
