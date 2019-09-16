package com.eastinno.otransos.web.exception;

import com.eastinno.otransos.core.util.I18n;

public class ValidateException extends FrameworkException {

    private static final long serialVersionUID = -8567589496529683281L;

    public ValidateException() {
        this(I18n.getLocaleMessage("core.web.Data.validation.errors"));
    }

    public ValidateException(String msg) {
        super(msg);
    }
}
