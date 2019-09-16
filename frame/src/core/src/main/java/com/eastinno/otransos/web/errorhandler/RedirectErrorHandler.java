package com.eastinno.otransos.web.errorhandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

public class RedirectErrorHandler implements IErrorHandler {

    private String errorPage;

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public Page handle(HttpServletRequest request, HttpServletResponse response, WebForm form, Throwable error) {
        // TODO Auto-generated method stub
        if (getErrorPage() != null) {
            try {
                response.sendRedirect(getErrorPage());
            } catch (Exception e) {

            }
        } else {
            return new Page("error", "/error");
        }
        return null;
    }

}
