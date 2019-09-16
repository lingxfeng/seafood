package com.eastinno.otransos.core.mvc.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.Personality;
import com.eastinno.otransos.core.service.EntityUIUtil;
import com.eastinno.otransos.core.service.IPersonalityService;
import com.eastinno.otransos.core.service.IScriptLoader;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * @intro 远程动态加载脚本
 * @version v_0.1
 * @author lengyu ksmwly@gmail.com
 * @since 2010-1-12 下午4:03:39
 */
@Action
public class ExtAppAction extends AbstractPageCmdAction {

    @Inject
    private IScriptLoader scriptLoader;

    @Inject
    private IPersonalityService personalityService;

    public void setPersonalityService(IPersonalityService personalityService) {
        this.personalityService = personalityService;
    }

    public Object doBefore(WebForm form, Module module) {
        form.addResult("personality", getPersonality(form));
        form.addResult("CURRENT_USER", UserContext.getUser());
        return super.doBefore(form, module);
    }

    public void setScriptLoader(IScriptLoader scriptLoader) {
        this.scriptLoader = scriptLoader;
    }

    public Page doIndex(WebForm form) {
        String userAgent = ActionContext.getContext().getRequest().getHeader("user-agent");
        form.addResult("ff", Boolean.valueOf((userAgent.indexOf("Firefox") > 0) || (userAgent.indexOf("Firefox") > 0)));
        String script = CommUtil.null2String(form.get("script"));
        if ("".equals(script)) {
            form.addResult("script", form.get("appClass") + ".js");
        }
        String otherScripts = CommUtil.null2String(form.get("otherScripts"));
        if (!"".equals(otherScripts)) {
            List<String> list = new ArrayList<String>();
            String[] s = otherScripts.split(";");
            for (String o : s) {
                list.add(o);
            }
            form.getDiscoResult().put("otherScriptList", list);
        }

        return page("index");
    }

    public Page doLoadScript(WebForm form) {
        String[] scripts = CommUtil.getStringArray(form.get("script"));
        if ((scripts == null) || (scripts.length == 0)) {
            scripts = CommUtil.getStringArray(form.get("appClass"));
            if (scripts != null) {
                for (int i = 0; i < scripts.length; i++) {
                    scripts[i] = (scripts[i] + ".js");
                }
            }
        }
        StringBuffer scriptSb = new StringBuffer();
        String scriptNames = "";
        String str = null;
        if (scripts != null) {
            scriptNames = StringUtils.join(scripts, ",");
            for (String script : scripts) {
                str = this.scriptLoader.loadApp(script);
                // scriptSb.append(s);
            }
        }
        Page p = new Page(scriptNames, scriptNames, "string");
        p.setContent(scriptSb.toString());
        p.setContentType("js");
        try {
            HttpServletResponse resp = ActionContext.getContext().getResponse();
            resp.setContentType("text/html;charset=gbk");
            resp.getWriter().write(str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p.nullPage;
    }

    public Page doLoadColumnField(WebForm form) {
        String entityName = CommUtil.null2String(form.get("entityClzName"));
        Map map = new HashMap();
        try {
            Class clz = Class.forName(entityName);
            if (clz != null)
                map = EntityUIUtil.doLoadColumnField(clz);
        } catch (Exception localException) {
        }
        form.jsonResult(map);
        return Page.JSONPage;
    }

    public Page doLoadScript2(WebForm form) {
        String script = CommUtil.null2String(form.get("script"));
        if ("".equals(script)) {
            script = form.get("appClass") + ".js";
        }
        String s = this.scriptLoader.loadApp(script);
        form.addResult("msg", s);
        return new Page("classpath:com/eastinno/otransos/core/views/blank.html");
    }

    public Page doCore(WebForm form) {
        return new Page("/manage/core.js");
    }

    public Page doCommonService(WebForm form) {
        return page("commonService.js");
    }

    private Personality getPersonality(WebForm form) {
        User user = UserContext.getUser();
        Personality p = null;
        if (user != null) {
            Personality obj = this.personalityService.getPersonality(user);
            if (obj != null) {
                p = new Personality();
                CommUtil.shallowCopy(obj, p);
                p.setUser(null);
            }
        }
        if (p == null) {
            p = new Personality();
            p.setPortals("id:currentUser,col:0,row:0@@id:announce,col:0,row:1@@id:workItem,col:1,row:0@@id:news,col:1,row:1@@id:links,col:2,row:0@@id:plan,col:2,row:1");
        }
        return p;
    }
}