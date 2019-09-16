package com.eastinno.otransos.web.core;

import javax.servlet.http.HttpSession;

import com.eastinno.otransos.container.BeanDefinition;
import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.container.Scope;
import com.eastinno.otransos.container.impl.BeanCreatorUtil;
import com.eastinno.otransos.web.ActionContext;

public class SessionScope implements Scope {
    private Container container;

    public SessionScope(Container container) {
        this.container = container;
    }

    public Object getBean(String name, BeanDefinition beanDefinition) {
        Object ret = null;
        HttpSession session = ActionContext.getContext().getSession();
        ret = session.getAttribute(name);
        if (ret == null) {
            ret = BeanCreatorUtil.initBean(beanDefinition, container);
            session.setAttribute(name, ret);
        }
        return ret;
    }
}
