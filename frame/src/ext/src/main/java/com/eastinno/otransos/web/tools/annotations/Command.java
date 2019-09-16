package com.eastinno.otransos.web.tools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注action中作请前端响应请求的具体方法
 * 
 * @author lengyu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    public String name() default ""; // Command的名称

    public String[] alias() default {};// Command的别名
}
