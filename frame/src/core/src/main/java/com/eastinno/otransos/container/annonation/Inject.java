package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标识需要注入的属性 在Disco框架的IOC中，除了可以使用配置文件的方式来进行依赖注入以外，还可以使用注解标签的形式来进行注入。 &#064;Inject标签正是这个用来标识依赖注入的标签
 * &#064;Inject标签可以用在属性、方法或构造子上，用来指定要注入的对象
 * 
 * <pre>
 * public class PersonAction implements IWebAction
 * {
 * &#064;Inject
 * private PersonService service;
 * }
 * or 
 * public class PersonAction implements IWebAction
 * {
 * &#064;Inject(name="personService")
 * private PersonService service;
 * }
 * </pre>
 * 
 * @author lengyu
 */
@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
public @interface Inject {
    /**
     * 默认情况下为根据类型自动注入
     */
    final String autoInjectByType = "AutoJnjectByType";

    /**
     * 指定要注入的bean的名称
     * 
     * @return 返回注入的bean名称
     */
    String name() default autoInjectByType;// name用来指定要注入的bean的名称
}
