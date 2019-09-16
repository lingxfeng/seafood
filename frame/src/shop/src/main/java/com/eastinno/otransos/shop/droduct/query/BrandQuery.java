package com.eastinno.otransos.shop.droduct.query;

import com.eastinno.otransos.core.support.query.QueryObject;
/**
 * 品牌查询
 * @author nsz
 *
 */
public class BrandQuery extends QueryObject{
	private String name;
	private String typeName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Override
    public void customizeQuery() {
		if(this.name!=null){
			this.addQuery("obj.name","%"+this.name+"%","like");
		}
		if(this.typeName!=null){
			this.addQuery("obj.typeName","%"+this.typeName+"%","like");
		}
        super.customizeQuery();
    }
}
