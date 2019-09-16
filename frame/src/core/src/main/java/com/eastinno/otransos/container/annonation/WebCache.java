package com.eastinno.otransos.container.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标识Disco框架的Cache，缓存采用生成html静态页面的形式，可以配置基本的缓存策略。
 * 
 * @author lengyu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebCache {

    public long timeout() default 300l;

    public String[] params() default {"CMD"};

    public boolean security() default false;
}
