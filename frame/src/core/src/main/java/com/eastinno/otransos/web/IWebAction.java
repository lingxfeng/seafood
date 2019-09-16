package com.eastinno.otransos.web;

/**
 * <p>
 * Title:Disco Action接口
 * </p>
 * <p>
 * Description: Disco的Action接口，用户的Action组件必须实现该接口，该接口只有一个execute方法需要用户实现。
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 1.0
 */
public interface IWebAction {
    /**
     * Action执行接口，在Disco中，所有的请求都会调用该方法，执行相关的数据操作。
     * 
     * @param form 封装了本次请求的Form数据信息
     * @param module 本次调用的Module信息
     * @return 显示数据的模板或直接跳转的URL
     * @throws Exception
     */
    public Page execute(WebForm form, Module module) throws Exception;
}
