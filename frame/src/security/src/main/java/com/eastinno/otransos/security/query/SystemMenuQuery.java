package com.eastinno.otransos.security.query;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
/**
 * 菜单查询
 * @author Administrator
 *
 */
public class SystemMenuQuery extends QueryObject{
	private Integer type;
	@POLoad(name = "parentId")
	private SystemMenu parent;
	private String isPerm;
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIsPerm() {
		return isPerm;
	}
	public void setIsPerm(String isPerm) {
		this.isPerm = isPerm;
	}
	public SystemMenu getParent() {
		return parent;
	}
	public void setParent(SystemMenu parent) {
		this.parent = parent;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public void customizeQuery() {
		this.setOrderBy("sequence");
		if(type==null){
			type=1;
		}
		this.addQuery("obj.type",type,"=");
		if(this.parent==null){
	        if (this.id!=null && !"".equals(this.id)) {
	        	this.addQuery("obj.parent.id", Long.parseLong(id), "=");
	        } else {
	        	this.addQuery("obj.parent is EMPTY");
	        }
		}else{
			this.addQuery("obj.parent",parent,"=");
		}
		if(this.isPerm==null || "".equals(this.isPerm)){
        	this.addQuery("( obj.tenant.depthPath like '" + ShiroUtils.getTenant().getDepthPath() + "%' or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }else{
        	this.addQuery("( obj.tenant.id=" + ShiroUtils.getTenant().getId()+" or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }
		super.customizeQuery();
	}
}
