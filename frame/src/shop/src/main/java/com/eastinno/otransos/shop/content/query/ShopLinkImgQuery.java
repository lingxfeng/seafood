package com.eastinno.otransos.shop.content.query;

import com.eastinno.otransos.core.support.query.QueryObject;

public class ShopLinkImgQuery extends QueryObject{
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Override
    public void customizeQuery() {
		if(this.code!=null){
			this.addQuery("obj.code",this.code,"=");
		}
        super.customizeQuery();
    }
}
