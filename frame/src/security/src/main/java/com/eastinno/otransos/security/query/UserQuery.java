package com.eastinno.otransos.security.query;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;

public class UserQuery extends QueryObject {
	private String name;
	private String password;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void customizeQuery() {
		Tenant tenant = ShiroUtils.getTenant();
		if (tenant != null) {
			this.addQuery("obj.code", tenant.getCode(), "<>");
			this.addQuery("obj.tenant.depthPath like '" + tenant.getDepthPath()
					+ "%'");
		}
		this.addQuery("obj.isTenantAdmin",Boolean.FALSE,"=");
		this.addQuery("obj.type",4,"<>");
		super.customizeQuery();
	}
}
