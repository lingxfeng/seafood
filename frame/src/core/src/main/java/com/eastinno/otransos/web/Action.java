package com.eastinno.otransos.web;

/**
 * <p>
 * Title: 简单的IWebAction默认实现示例
 * </p>
 * <p>
 * Description: 实现IWebAction接口,IWebAction的默认实现
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
public class Action implements IWebAction {
    /**
     * 默认的Action直接查询指定的default页面执行
     */
    public Page execute(WebForm form, Module module) throws Exception {
        Page page = null;
        if (module != null) {
            page = module.findPage(module.getDefaultPage());
        }
        return page;
    }
}
