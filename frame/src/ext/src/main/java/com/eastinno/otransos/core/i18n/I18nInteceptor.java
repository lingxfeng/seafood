package com.eastinno.otransos.core.i18n;

import java.lang.reflect.Method;
import java.util.Locale;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeanUtils;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.dao.impl.CustomJpaGenericDAO;
import com.eastinno.otransos.web.LocalManager;

/**
 * 国际化拦截器
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年02月11日 下午2:29:01
 * @Intro
 */
public class I18nInteceptor implements IntroductionInterceptor {

    public boolean isSaveMethod(Method method) {
        boolean ret = false;
        String methodName = method.getName();
        if ("save".equals(methodName) || "update".equals(methodName))
            ret = true;
        return ret;
    }

    public boolean isFindMethod(Method method) {
        boolean ret = false;
        String methodName = method.getName();
        if ("find".equals(methodName) || "get".equals(methodName) || "getBy".equals(methodName))
            ret = true;
        return ret;
    }

    public Object invoke(MethodInvocation mv) throws Throwable {
        Object returnObject = null;
        if (isSaveMethod(mv.getMethod())) {
            if (mv.getArguments()[0] instanceof LocaleSupport) {
                Object oldObject = mv.getArguments()[0];
                String oldClazzName = oldObject.getClass().getCanonicalName();
                Object newObj = createI18nClass(oldClazzName);
                BeanUtils.copyProperties(mv.getArguments()[0], newObj);
                mv.getArguments()[0] = newObj;
                returnObject = mv.proceed();
                if ("save".equals(mv.getMethod().getName()))
                    BeanUtils.copyProperties(newObj, oldObject);
            } else {
                returnObject = mv.proceed();
            }

        } else if (isFindMethod(mv.getMethod())) {
            Class oldClazz = ((CustomJpaGenericDAO) mv.getThis()).getClz();
            if (LocaleSupport.class.isAssignableFrom(oldClazz)) {
                String oldClazzName = oldClazz.getCanonicalName();
                Class newObj = createI18nClass(oldClazzName).getClass();
                ((CustomJpaGenericDAO) mv.getThis()).setClz(newObj);
            }
            returnObject = mv.proceed();
            ((CustomJpaGenericDAO) mv.getThis()).setClz(oldClazz);
        } else
            returnObject = mv.proceed();

        return returnObject;
    }

    private Object createI18nClass(String oldClazzName) throws ClassNotFoundException {
        Locale local = LocalManager.getCurrentLocal();
        String localName = local.getLanguage().toUpperCase();

        String realClassName = oldClazzName + localName;
        Object newObj = BeanUtils.instantiateClass(Class.forName(realClassName));
        return newObj;
    }

    public boolean implementsInterface(Class intf) {
        boolean returnboolean = IJpaGenericDAO.class.isAssignableFrom(intf);
        return returnboolean;
    }

}
