package com.eastinno.otransos.security.query;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.User;

public class SystemLogQuery extends QueryObject {
    private String name;
    private Date vdate1;
    private Date vdate2;
    private String ip = "";
    private String action = "";
    private String pack = "";
    private String method = "";
    private Integer types;

    public void customizeQuery() {
        if (this.name != null && !"".equals(name)) {
            addQuery("obj.user.name", name, "=");
        }
        if (this.vdate1 != null) {
            addQuery("obj.vdate", this.vdate1, ">=");
        }
        if (this.vdate2 != null) {
            addQuery("obj.vdate", this.vdate2, "<");
        }
        if (!"".equals(this.ip)) {
            addQuery("obj.ip", this.ip + "5", "like");
        }
        if (!"".equals(this.pack)) {
            addQuery("obj.action", this.action + "%", "like");
        }
        if (!"".equals(this.action)) {
            addQuery("obj.action", this.action + "%", "like");
        }
        if (!"".equals(this.method)) {
            addQuery("obj.cmd", this.method, "=");
        }
        if (this.types != null) {
            addQuery("obj.types", this.types, "=");
        }
        if (!StringUtils.hasText(getOrderBy())) {
            setOrderBy("vdate");
            setOrderType("desc");
        }
        addQuery("obj.user is not null");
        super.customizeQuery();
    }



    public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void setVdate1(Date vdate1) {
        this.vdate1 = vdate1;
    }

    public void setVdate2(Date vdate2) {
        this.vdate2 = vdate2;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
