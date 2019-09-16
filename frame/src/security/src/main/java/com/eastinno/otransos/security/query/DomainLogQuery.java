package com.eastinno.otransos.security.query;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.User;

public class DomainLogQuery extends QueryObject {
    private User user;
    private String ip = "";
    private String entity = "";
    private String id = "";
    private Date vdate1;
    private Date vdate2;
    private Integer ver;

    public void customizeQuery() {
        if (!"".equals(this.ip)) {
            addQuery("obj.ip", this.ip, "=");
        }
        if (!"".equals(this.entity)) {
            addQuery("obj.entityType", this.entity + "%", "like");
        }
        if (!"".equals(this.id)) {
            addQuery("obj.entityId", this.id, "=");
        }
        if (this.user != null) {
            addQuery("obj.user", this.user, "=");
        }
        if (this.vdate1 != null) {
            addQuery("obj.vdate", this.vdate1, ">=");
        }
        if (this.vdate2 != null) {
            addQuery("obj.vdate", this.vdate2, "<");
        }
        if (this.ver != null) {
            addQuery("obj.ver", this.ver, "=");
        }
        if (!StringUtils.hasLength(getOrderBy())) {
            setOrderBy("id");
            setOrderType("DESC");
        }
        super.customizeQuery();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setVdate2(Date vdate2) {
        this.vdate2 = vdate2;
    }

    public void setVdate1(Date vdate1) {
        this.vdate1 = vdate1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public String getIp() {
        return this.ip;
    }

    public String getEntity() {
        return this.entity;
    }

    public String getId() {
        return this.id;
    }

    public Date getVdate1() {
        return this.vdate1;
    }

    public Date getVdate2() {
        return this.vdate2;
    }

    public Integer getVer() {
        return this.ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }
}
