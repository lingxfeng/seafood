package com.eastinno.otransos.cms.query;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.ActionContext;

public class NewsDirQuery extends QueryObject {
	private String code;

	@Override
	public void customizeQuery() {
		if (code != null) {
			this.addQuery("obj.code", code, "=");
		}
		super.customizeQuery();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
