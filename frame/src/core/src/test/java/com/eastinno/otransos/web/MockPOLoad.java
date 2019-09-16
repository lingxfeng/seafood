package com.eastinno.otransos.web;

import java.io.Serializable;

import com.eastinno.otransos.web.POLoadDao;

public class MockPOLoad implements POLoadDao {
    public Object get(Class clz, Serializable id) {
        if (clz == PO.class && id.equals(2l)) {
            PO po = new PO();
            po.setId(2l);
            po.setTitle("标题");
            return po;
        }
        return null;
    }
}
