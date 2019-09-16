package com.eastinno.otransos.exeception;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.interceptor.ExceptionInterceptor;

public class AjaxInvokeExceptionHandler implements ExceptionInterceptor {
    public boolean handle(Throwable exception, Object target, Method method, Object[] args) throws Exception {
        if ((exception instanceof AjaxInvokeException)) {
            HttpServletResponse response = ActionContext.getContext().getResponse();
            HttpServletRequest request = ActionContext.getContext().getRequest();
            String contentType = "application/x-json;charset=UTF-8";
            if (request.getContentType().indexOf("multipart/form-data") == 0) {
                contentType = "text/html;charset=UTF-8";
            }
            response.setStatus(500);
            response.setContentType(contentType);
            PrintWriter out = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("err", exception.getMessage());
            out.print(AjaxUtil.getJSON(map));
            out.close();
        }
        return true;
    }

    public void blank() {
    }
}
