package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来定义名称重载的属性<br>
 * 该标签用于辅助WebForm.toPo(Object)等方法，从而使得能够正确的赋值 &#064;OverrideProperty标签一般配合@InerProperty使用，也可以单独使用。 如下面的Person<br>
 * 
 * <pre>
 * public class Person
 * {
 * &#064;OverrideProperty(name="name",newName="name1")
 * private String name;
 * } 
 * 
 * Form表单属性
 * 
 * <form ...>
 * <input type=text name="name1" value="$!name"/>
 * </form>
 * 
 * Action中的使用WebForm对Person对象赋值
 * PersonAction
 * 
 * Person p=form.toPo(Person.class);
 * System.out.println(p.getName());
 * </pre>
 * 
 * @author lengyu
 */
@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
public @interface OverrideProperty {
    /**
     * 要重载(替换)的属性名
     * 
     * @return 属性名
     */
    String name();

    /**
     * 重载后的新名称
     * 
     * @return 新属性名
     */
    String newName();
}
