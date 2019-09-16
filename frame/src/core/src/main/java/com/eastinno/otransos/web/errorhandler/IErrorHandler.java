package com.eastinno.otransos.web.errorhandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

public interface IErrorHandler {
    Page handle(HttpServletRequest request, HttpServletResponse response, WebForm form, Throwable error)
            throws ServletException;
}
