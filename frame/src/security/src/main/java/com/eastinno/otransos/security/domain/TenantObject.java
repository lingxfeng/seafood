package com.eastinno.otransos.security.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

@MappedSuperclass
public abstract class TenantObject implements IJsonObject, ITenantObject, Serializable {

    @POLoad(name = "tenantId")
    @ManyToOne(fetch = FetchType.LAZY)
    protected Tenant tenant;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"tenant"});
        return map;
    }

}
