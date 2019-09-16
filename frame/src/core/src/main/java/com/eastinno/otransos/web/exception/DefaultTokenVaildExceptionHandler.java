package com.eastinno.otransos.web.exception;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.WebInvocationParam;
import com.eastinno.otransos.web.interceptor.ExceptionInterceptor;

public class DefaultTokenVaildExceptionHandler implements ExceptionInterceptor {

    private String msg = I18n
            .getLocaleMessage("core.web.Requests.are.being.processed.or.have.been.dealt.with.please.do.not.repeat.the");

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean handle(Throwable e, Object target, Method method, Object[] args) {
        if (TokenVaildException.class.isAssignableFrom(e.getClass())) {
            if (args.length == 1) {
                WebInvocationParam param = (WebInvocationParam) args[0];
                param.getForm().addResult("msg", msg);
                showInfo();
            }
            return true;
        }
        return false;
    }

    public void showInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        sb.append("<a href='javascript:history.go(-1);'>");
        sb.append(I18n.getLocaleMessage("core.web.return"));
        sb.append("</a>");
        try {
            ActionContext.getContext().getResponse().setContentType("text/html");
            ActionContext.getContext().getResponse().setCharacterEncoding("UTF-8");
            Writer w = ActionContext.getContext().getResponse().getWriter();
            w.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ActionContext.getContext().getResponse().getWriter().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
