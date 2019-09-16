package com.eastinno.otransos.web;

/**
 * <p>
 * Title:初始化应用程序
 * </p>
 * <p>
 * Description: 初始化应用程序。用于第一次运行discoFilter时执行。
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

public interface IAppInit {
    /**
     * 在Disco启动时，自定义的需要执行的程序
     * 
     * @param discoFilter 具体的discoFilter对象
     */
    public void init(DiscoFilter discoFilter);
}
