package com.eastinno.otransos.web;

/**
 * Web调用参数封装，把Web调用中的相关的action、form、及module三个参数封装到这个类中，可以在框架的其它应用如核心异常处理中进一步获取这些参数，从而得到异常产生的上下文件相关数据信息
 * 
 * @author lengyu
 */
public class WebInvocationParam {
    /**
     * 当前调用的action
     */
    private IWebAction action;

    /**
     * 当前调用的表单数据
     */
    private WebForm form;

    /**
     * 系统模块及配置信息
     */
    private Module module;

    /**
     * 记录url的类型
     */
    private String urlType = "java";

    private String suffix = "";

    public WebInvocationParam() {

    }

    public WebInvocationParam(IWebAction action, WebForm form, Module module) {
        this.action = action;
        this.form = form;
        this.module = module;
    }

    public IWebAction getAction() {
        return action;
    }

    public void setAction(IWebAction action) {
        this.action = action;
    }

    public WebForm getForm() {
        return form;
    }

    public void setForm(WebForm form) {
        this.form = form;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
