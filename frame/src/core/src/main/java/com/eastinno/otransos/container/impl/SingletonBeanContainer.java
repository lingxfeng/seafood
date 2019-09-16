package com.eastinno.otransos.container.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.Assert;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.exception.FrameworkException;

public class SingletonBeanContainer {
    @SuppressWarnings("unchecked")
    private Map beans = new TreeMap();

    public boolean containsSingletonBean(String name) {
        return beans.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getBeansByType(Class<T> type) {
        java.util.Iterator it = beans.values().iterator();
        List<T> ret = null;
        while (it.hasNext()) {
            Object obj = it.next();
            if (type.isAssignableFrom(obj.getClass())) {
                if (ret == null)
                    ret = new ArrayList<T>();
                ret.add((T) obj);
            }
        }
        return ret;
    }

    public <T> T getSingletionBean(Class<T> type) {
        T ret = null;
        List<T> list = getBeansByType(type);
        if (list != null && list.size() > 0) {
            if (list.size() > 1)
                throw new FrameworkException(I18n.getLocaleMessage("core.container.has.many.bean.error"));
            ret = list.get(0);
        }
        return ret;
    }

    public Object getSingletionBean(String name) {
        return beans.get(name);
    }

    public void register(String name, Object bean) {
        Assert.hasText(name);
        Assert.notNull(bean);
        synchronized (beans) {
            if (!containsSingletonBean(name)) // throw new
                beans.put(name, bean);
        }
    }

    public void removeAll() {
        beans.clear();
    }

    public <T> void removeBean(Class<T> type) {
        Object bean = getSingletionBean(type);
        beans.values().remove(bean);
    }

    public void removeBean(String name) {
        beans.remove(name);
    }
}
