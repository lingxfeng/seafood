package com.eastinno.otransos.security.service;

import com.eastinno.otransos.security.domain.Tenant;

/**
 * @intro CloudEDU SaaS平台对象接口
 * @version v0.1
 * @author maowei
 * @since 2014年5月19日 下午4:46:49
 */
public interface ITenantObject {
    Tenant getTenant();

    void setTenant(Tenant tenant);
}