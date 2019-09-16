package com.eastinno.otransos.shop.distribu.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;

/**
 * 收货地址查询
 * @author dll
 *
 */
public class CommissionConfigQuery extends QueryObject{
	private String name;	//会员账户名
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	@Override
    public void customizeQuery() {
    	this.addQuery("obj.isdefault", false, "=");
		if(this.name != null){
			this.addQuery("obj.product.name", "%"+this.name+"%", "like");
		}		
        super.customizeQuery();
    }
}
