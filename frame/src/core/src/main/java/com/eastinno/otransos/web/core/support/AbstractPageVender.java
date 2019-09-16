package com.eastinno.otransos.web.core.support;

import com.eastinno.otransos.web.IPageVender;
import com.eastinno.otransos.web.Order;

public abstract class AbstractPageVender implements IPageVender, Order {
    private Integer order = 0;
    private String suffix = "";

    public boolean supports(String suffix) {
        return this.suffix.indexOf("," + suffix + ",") >= 0;
    }

    public void setSuffix(String suffix) {
        this.suffix = "," + suffix + ",";

    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return this.order;
    }

}
