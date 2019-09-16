package com.eastinno.otransos.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面处理器 用于处理页面合成等工作。 在RequestProcessor执行完IWebAction.execute(WebForm,Module)并返回一个不为null的page时执行 只用于合成页面策略
 * 传入的Page类型只为template
 * 
 * @author lengyu
 */
public interface IPageVender extends Order {

    /**
     * 负责页面视图的合成
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param param WebInvocationParam 对象
     * @return boolean，如果boolean为true，则合成完成，如果为false，交由下一个vender合成，在vender链的最后是disco的默认的合成器
     */
    boolean venderPage(HttpServletRequest request, HttpServletResponse response, Page page, WebInvocationParam param);

    /**
     * 指定是否支持当前扩展名
     * 
     * @param suffixs　以逗号分隔开的扩展名
     * @return
     */
    boolean supports(String suffix);
}
