package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 设置内部属属性，嵌套属性 该标签主要用于辅助WebForm.toPo方法来给复杂的属性对象赋值，主要用于内嵌属性。 地址信息的Address
 * 
 * <pre>
 * public class Address{
 * private String province;
 * private String city;
 * }
 * 
 * 下面是Employee类的定义： 
 * 
 * public class Employee
 * {
 * private String name;
 * &#064;InnerProperty
 * private Address address1;
 * &#064;InnerProperty(overrides={&#064;OverrideProperty(name="province",newName="province2"),&#064;OverrideProperty(name="city",newName="city2")});
 * private Address address2;
 * }
 * 
 * 下面是编辑该对象的form表单
 * 
 * <form ..>
 * <input type=text name="name" value="$!name"/>
 * <input type=text name="province" value="$!address1.province"/>
 * <input type=text name="city" value="$!address1.city"/>
 * <input type=text name="province2" value="$!address2.province"/>
 * <input type=text name="city2" value="$!address2.city"/>
 * ..
 * </form> 
 * 
 * EmployeeAction中读取表单中内容到对象中的代码如下
 * ：
 * Employee e=form.toPo(Employee.class);
 * System.out.println(e.getAddress2().getCity());
 * </pre>
 * 
 * @author lengyu
 */
@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
public @interface InnerProperty {
    /**
     * 用来指定嵌套类中需要替换的属性
     * 
     * @return 指定内嵌属性中包括的属性替换信息
     */
    OverrideProperty[] overrides() default {};// 指定需要重载的属性名称
}
