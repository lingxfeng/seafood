package com.eastinno.otransos.web.errorhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseErrorHandlerManager implements IErrorHandlerManager {
    private Map error2HandlerMap;

    public Map getError2HandlerMap() {
        if (this.error2HandlerMap == null) {
            this.error2HandlerMap = new HashMap();
        }
        return error2HandlerMap;
    }

    public BaseErrorHandlerManager() {
        init();
    }

    abstract protected void init();

    public void setError2HandlerMap(Map error2HandlerMap) {
        this.error2HandlerMap = error2HandlerMap;
    }

    private IErrorHandler defaultHandler;

    public IErrorHandler getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(IErrorHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public List findHandler(HttpServletRequest request, HttpServletResponse response, Throwable error) {
        // TODO Auto-generated method stub
        List ret = new ArrayList();
        if (this.getError2HandlerMap() != null && this.getError2HandlerMap().size() > 0) {
            for (Iterator it = this.getError2HandlerMap().keySet().iterator(); it.hasNext();) {
                String exClazz = (String) it.next();
                if (exClazz.equalsIgnoreCase(error.getClass().getName())) {
                    ret.add(this.getError2HandlerMap().get(exClazz));
                } else if (exClazz.equalsIgnoreCase(error.getClass().getName()
                        .substring(error.getClass().getName().lastIndexOf(".")))) {
                    ret.add(this.getError2HandlerMap().get(exClazz));
                }
            }
        } else if (this.getDefaultHandler() != null) {
            ret.add(this.getDefaultHandler());
        }
        return ret;
    }

    protected void registerHandler(String exClazz, IErrorHandler handler) throws Exception {
        if (exClazz.equals("java.lang.Exception")) {
            this.setDefaultHandler(handler);
        } else {
            this.getError2HandlerMap().put(exClazz, handler);
        }
    }

}
