package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 命名标签，用来指定一个目标的名称 该标签可以用于任何其它需要标识名称的地方
 * 
 * <pre>
 * &#064;Name("myValidator")
 * public class MyValidator implements Validator
 * {
 * ...
 * }
 * </pre>
 * 
 * @author lengyu
 */
@Target({METHOD, CONSTRUCTOR, FIELD, TYPE})
@Retention(RUNTIME)
public @interface Name {
    /**
     * 指定某一个目标的名称，这个目标可以是一个对象，也可以是一个类等，根据实际应用的环境来定
     * 
     * @return 目标的标识名称
     */
    public String value();
}
