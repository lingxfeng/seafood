package com.eastinno.otransos.cms.query;

import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.BaseQueryObject;

public class SinglePageQuery extends BaseQueryObject {
    @POLoad(name = "parentId")
    private SinglePageNews parent;

    private String sn = "";

    private String title = "";

    public SinglePageNews getParent() {
        return parent;
    }

    public void setParent(SinglePageNews parent) {
        this.parent = parent;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void customizeQuery() {
        // TODO Auto-generated method stub
        if (parent == null) {
            addQuery("obj.parent.id is null", null);
        } else {
            addQuery("obj.parent", parent, "=");
        }
        if (!"".equals(sn)) {
            addQuery("obj.sn", "%" + sn + "%", "like");
        }
        if (!"".equals(title)) {
            addQuery("obj.title", "%" + title + "%", "like");
        }
        if (this.getOrderBy() == null || "".equals(this.getOrderBy())) {
            this.setOrderBy("sequence");
            this.setOrderType("asc,sn asc");
        }
    }

}
