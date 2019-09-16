package com.eastinno.otransos.web.core;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.interceptor.IRequestInterceptor;

/**
 * 用户入侵检测载器
 * 
 * @author lengyu
 */
public class UserConnectInterceptor implements IRequestInterceptor {
    public Object doIntercept() throws Exception {
        // System.out.println(I18n.getLocaleMessage("core.web.starting.application"));
        if (ActionContext.getContext().getRequest() != null) {
            if (!UserConnectManage.checkLoginValidate(ActionContext.getContext().getRequest().getRemoteAddr(), "guest")) {
                // System.out.println(I18n.getLocaleMessage("core.web.refresh.your.pages.too.soon.please.wait"));
                throw new Exception(I18n.getLocaleMessage("core.web.refresh.your.pages.too.soon.please.wait")
                        + UserConnectManage.getWaitInterval() / 1000
                        + I18n.getLocaleMessage("core.web.seconds.later.refresh.pages"));
            }
        }
        return IRequestInterceptor.SUCCESS;
    }
}
