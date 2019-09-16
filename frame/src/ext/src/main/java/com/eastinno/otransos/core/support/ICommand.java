package com.eastinno.otransos.core.support;

import java.util.List;

/**
 * 命令对象接口 实现vaild方法，执行对对象中属性的验证。 返回一个包含错误提示信息的列表
 * 
 * @author stefanie wu
 */
public interface ICommand {
    List<String> vaild();
}
