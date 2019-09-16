package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * TenantService
 * 
 * @author ksmwly@gmail.com
 */
public interface ITenantService {
    /**
     * 保存一个Tenant，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addTenant(Tenant domain);

    /**
     * 根据一个ID得到Tenant
     * 
     * @param id
     * @return
     */
    Tenant getTenant(Long id);

    /**
     * 根据一个一级域名获取对应的Tenant对象
     * 
     * @param host 一级域名
     * @return
     */
    Tenant getTenantByHost(String host);

    /**
     * 删除一个Tenant
     * 
     * @param id
     * @return
     */
    boolean delTenant(Long id);

    /**
     * 批量删除Tenant
     * 
     * @param ids
     * @return
     */
    boolean batchDelTenants(List<Serializable> ids);

    /**
     * 通过一个查询对象得到Tenant
     * 
     * @param properties
     * @return
     */
    IPageList getTenantBy(IQueryObject queryObj);

    /**
     * 更新一个Tenant
     * 
     * @param id 需要更新的Tenant的id
     * @param dir 需要更新的Tenant
     */
    boolean updateTenant(Long id, Tenant entity);
}
