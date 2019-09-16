package com.eastinno.otransos.web.ajax;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.exception.FrameworkException;

public class ServiceNotAvailableException extends FrameworkException {

    private static final long serialVersionUID = 9110533433680407704L;

    public ServiceNotAvailableException() {
        super(I18n.getLocaleMessage("core.ajax.designated.service.is.not.available.may.have.limited.name"));
    }

    public ServiceNotAvailableException(String name) {
        super(I18n.getLocaleMessage("core.ajax.named") + name
                + I18n.getLocaleMessage("core.ajax.may.not.use.the.service.the.names.may.have.limited"));
    }
}
