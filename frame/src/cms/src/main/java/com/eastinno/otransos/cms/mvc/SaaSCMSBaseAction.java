package com.eastinno.otransos.cms.mvc;

import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年12月4日 下午12:44:41
 * @Intro SaaS版多租户CMS基础Action
 */
public class SaaSCMSBaseAction extends AbstractPageCmdAction {
    @Override
    public Object doBefore(WebForm form, Module module) {
        Tenant t = TenantContext.getTenant();
        if (t == null) {
            form.addResult("msg", "无法根据你的域名授权对应的租户信息");
            return new Page("blank", "classpath:com/eastinno/otransos/core/views/blank.html");
        }
        return super.doBefore(form, module);
    }
}
