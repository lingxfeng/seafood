package com.eastinno.otransos.security.query;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
/**
 * 权限查询
 * @author nsz
 *
 */
public class PermissionQuery extends QueryObject{
	private Integer type;
	private String rolePer;
	
	public String getRolePer() {
		return rolePer;
	}

	public void setRolePer(String rolePer) {
		this.rolePer = rolePer;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public void customizeQuery() {
		if(this.type==null){
			this.type=1;
		}
		if(this.type!=0){
			this.addQuery("obj.type",type,"=");
		}
		Tenant tenant = ShiroUtils.getTenant();
		if (tenant != null) {
            this.addQuery("( obj.tenant.depthPath like '" + tenant.getDepthPath() + "%' or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }
		if(this.rolePer==null || "".equals(this.rolePer)){
        	this.addQuery("( obj.tenant.depthPath like '" + ShiroUtils.getTenant().getDepthPath() + "%' or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }else{
        	this.addQuery("( obj.tenant.id=" + ShiroUtils.getTenant().getId()+" or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }
		super.customizeQuery();
	}
}
