package com.eastinno.otransos.container.annonation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.eastinno.otransos.web.POLoadDao;

@Target({METHOD, CONSTRUCTOR, FIELD})
@Retention(RUNTIME)
/**
 * @author lengyu
 * 这个注解用于自动加载关联的List对象，适用于OneToMany关联。目前还不完善，更新对象时会覆盖掉原来的List
 */
public @interface MultiPOLoad {

    String name() default "";

    Class targetClz();

    Class pkClz() default Long.class;

    Class loadDao() default POLoadDao.class;

}
