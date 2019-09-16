package com.eastinno.otransos.web.ajax;

import java.lang.reflect.Method;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.exception.FrameworkException;

/**
 * 方法不可用
 * 
 * @author lengyu
 */
public class MethodNotAvailableException extends FrameworkException {

    private static final long serialVersionUID = 3474609756582030887L;

    public MethodNotAvailableException() {
        super(I18n.getLocaleMessage("core.ajax.method.can.not.be.used"));
    }

    public MethodNotAvailableException(Method method) {
        super(I18n.getLocaleMessage("core.ajax.class") + method.getDeclaringClass()
                + I18n.getLocaleMessage("core.ajax.methed") + method
                + I18n.getLocaleMessage("core.ajax.remote.may.not.be.used.to.call"));
    }
}
