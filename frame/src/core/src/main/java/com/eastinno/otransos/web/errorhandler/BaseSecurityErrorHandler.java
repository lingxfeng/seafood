package com.eastinno.otransos.web.errorhandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

public abstract class BaseSecurityErrorHandler implements IErrorHandler {

    public final Page handle(HttpServletRequest request, HttpServletResponse response, WebForm form, Throwable error) {
        // TODO Auto-generated method stub
        return handleSecurityException(request, response, form, (SecurityException) error);
    }

    protected abstract Page handleSecurityException(HttpServletRequest request, HttpServletResponse response,
            WebForm form, SecurityException error);

}
