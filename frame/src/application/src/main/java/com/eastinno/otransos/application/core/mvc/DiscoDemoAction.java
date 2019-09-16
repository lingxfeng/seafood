package com.eastinno.otransos.application.core.mvc;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Action
public class DiscoDemoAction extends AbstractPageCmdAction {
    // 这个注解是指禁止注入，出了使用Spring的注入方式Disco框架也有注入注解@Inject
    @InjectDisable
    private String abc;

    @Override
    public Object doAfter(WebForm form, Module module) {
        form.addResult("after", "我是在你所调用的方法之后调用");
        return super.doAfter(form, module);
    }

    @Override
    public Object doBefore(WebForm form, Module module) {
        form.addResult("before", "我是在你所调用的方法之前调用");
        return super.doBefore(form, module);
    }

    public void doShow(WebForm form) {
        System.out.println("不需要显式的返回视图页面，那么此时对应的视图为/discoDemo/show.html");
    }

    public Page doShow() {
        System.out.println("不需要显式的返回视图页面，那么此时对应的视图为/discoDemo/show.html");
        return Page.nullPage;
    }
}
