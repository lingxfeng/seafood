package com.eastinno.otransos.security.query;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.ResourceType;

public class ResourceQuery extends QueryObject {
    private String action = "";
    private String pack = "";
    private String searchKey = "";
    private ResourceType type;
    
    public void customizeQuery() {
        if (!"".equals(this.action)) {
            addQuery("obj.resStr", this.action + ":%", "like");
        }
        if (!"".equals(this.pack)) {
            addQuery("obj.resStr", this.pack + ".%", "like");
        }
        if (!"".equals(this.searchKey)) {
            addQuery("obj.resStr", "%" + this.searchKey + "%", "like");
        }
        if (this.type != null) {
            addQuery("obj.type", this.type, "=");
        }
        super.customizeQuery();
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }
}
