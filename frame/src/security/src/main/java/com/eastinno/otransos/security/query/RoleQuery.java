package com.eastinno.otransos.security.query;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;

public class RoleQuery extends QueryObject{
	private String isUserTenantR;
	
	public String getIsUserTenantR() {
		return isUserTenantR;
	}

	public void setIsUserTenantR(String isUserTenantR) {
		this.isUserTenantR = isUserTenantR;
	}

	@Override
	public void customizeQuery() {
		Tenant tenant = ShiroUtils.getTenant();
        if(this.isUserTenantR==null || "".equals(this.isUserTenantR)){
        	this.addQuery("( obj.tenant.depthPath like '" + tenant.getDepthPath() + "%' or obj.id in (" + ShiroUtils.getShiroUser().getRoleIds() + "))");
        }else{
        	this.addQuery("(obj.tenant.id=" + tenant.getId() + " or obj.id in (" + ShiroUtils.getShiroUser().getRoleIds() + "))");
        }
		super.customizeQuery();
	}
	
}
