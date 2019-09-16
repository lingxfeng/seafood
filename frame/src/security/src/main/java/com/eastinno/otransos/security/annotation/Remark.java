package com.eastinno.otransos.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 使用此注解可以给类或方法添加一个别名,方便记忆一般用于监控日志或反射扫描类时使用
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年10月15日 下午2:23:28
 * @Intro
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Remark {
    public abstract String value();
}
