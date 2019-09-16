package com.eastinno.otransos.web.errorhandler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IErrorHandlerManager {
    List findHandler(HttpServletRequest request, HttpServletResponse response, Throwable error);
}
