package com.eastinno.otransos.security.dao;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.security.domain.Tenant;

/**
 * ITenantDAO
 * 
 * @author ksmwly@gmail.com
 */
public interface ITenantDAO extends IJpaGenericDAO<Tenant, Long> {
	Tenant findByUrlLike(String host);
}