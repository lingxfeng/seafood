package com.eastinno.otransos.web.ajax;

import java.io.Serializable;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年01月14日 下午5:33:43
 * @Intro 通过反射机制把Entity转为JSON数据格式，默认只要是公共(public)的get方法都转换
 */
public interface IJsonObject extends Serializable {
    /**
     * 实现此方法调用CommUtil.obj2mapExcept或CommUtil.obj2map两个方法来过滤或允许数据对象
     * 
     * @return
     */
    Object toJSonObject();
}
