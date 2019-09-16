package com.eastinno.otransos.shop.spokesman.query;

import com.eastinno.otransos.core.support.query.QueryObject;

/**
 * 
 * @author dll
 *
 */
public class RestitutionQuery extends QueryObject {
	private String name;	//商品名称
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	@Override
    public void customizeQuery() {
		if(this.name != null){
			this.addQuery("obj.product.name", "%"+this.name+"%", "like");
		}		
        super.customizeQuery();
    }
}
