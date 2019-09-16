package com.eastinno.otransos.container.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标签业务层的Bean对象
 * 
 * @author lengyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    public String name() default "";// 默认的
    public String inject() default "AutoJnjectByType";// 自动注入类型
    public String[] disInject() default {};// 禁止注入的数据
    public String[] autoInject() default {};// 自动注入的数据
    public String alias() default "";// bean的别名
    public String scope() default "singleton";// Bean的范围　
}
