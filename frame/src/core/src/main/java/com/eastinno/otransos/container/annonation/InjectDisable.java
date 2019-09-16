package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来定义不可注入的属性 Disco框架的IOC支持自动按类型、自动按名称或自动选择等几种方式来注入对象所依赖的属性
 * 在有的时候，我们要使用自动注入的方式注入对象依赖，而对象中一些属性不需要注入。此时即可通过使用@InjectDisable标签来设置不需要注入的属性。
 * Disco框架在处理自动注入的过程中，若发现@InjectDisable标签，将会忽略对这个属性的注入。 例如下面的示例中，在自动注入的情况下，service将会被自动注入，而parent这一个属性将不会被注入。
 * 
 * <pre>
 * public class PersonAction implements IWebAction {
 *     private PersonService service;
 *     &#064;InjectDisable
 *     private Person parent;
 * 
 * }
 * </pre>
 * 
 * 该标签可以用于方法、属性字段，及构造子上。
 * 
 * @author lengyu
 */
@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
public @interface InjectDisable {

}
