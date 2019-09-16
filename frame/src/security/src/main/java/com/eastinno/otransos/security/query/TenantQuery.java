package com.eastinno.otransos.security.query;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
/**
 * 租户查询
 * @author nsz
 *
 */
public class TenantQuery  extends QueryObject{
	@POLoad(name = "parentId")
	private Tenant parent;
	private String isTree;
	
	public String getIsTree() {
		return isTree;
	}

	public void setIsTree(String isTree) {
		this.isTree = isTree;
	}

	public Tenant getParent() {
		return parent;
	}

	public void setParent(Tenant parent) {
		this.parent = parent;
	}

	@Override
	public void customizeQuery() {
		Tenant tenant = ShiroUtils.getTenant();
		if(tenant!=null){
			this.addQuery("obj.depthPath",tenant.getDepthPath()+"%","like");
			if(!tenant.getCode().equals(ShiroUtils.ROOT)){
				this.addQuery("obj.code",tenant.getCode(),"<>");
			}
		}
		if(this.parent==null){
			if(this.isTree!=null){
				this.addQuery("obj.parent",tenant,"=");
			}
		}else{
			this.addQuery("obj.parent",this.parent,"=");
		}
		super.customizeQuery();
	}
	
}
