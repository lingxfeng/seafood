package com.eastinno.otransos.web.core;

import javax.servlet.http.HttpServletRequest;

import com.eastinno.otransos.container.BeanDefinition;
import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.container.Scope;
import com.eastinno.otransos.container.impl.BeanCreatorUtil;
import com.eastinno.otransos.web.ActionContext;

public class RequestScope implements Scope {
    private Container container;

    public RequestScope(Container container) {
        this.container = container;
    }

    public Object getBean(String name, BeanDefinition beanDefinition) {
        Object ret = null;
        HttpServletRequest request = ActionContext.getContext().getRequest();
        // Container cotainer=ActionContext.getContext().get
        ret = request.getAttribute(name);
        if (ret == null) {
            ret = BeanCreatorUtil.initBean(beanDefinition, container);
            request.setAttribute(name, ret);
        }
        return ret;
    }
}
