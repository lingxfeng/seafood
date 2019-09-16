package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 简单的代理方法调用处理器
 * 
 * @author lengyu
 */
public class SimpleInvocationHandler implements InvocationHandler {
    private Object target;
    private BeforeInterceptor[] beforeAdvices;
    private AfterInterceptor[] afterAdvices;
    private AroundInterceptor[] aroundAdvices;

    public SimpleInvocationHandler(Object target, Interceptor[] advices) {
        this.target = target;
        initAdvices(advices);
    }

    private void initAdvices(Interceptor[] advices) {
        if (advices != null) {
            java.util.List beforeList = new java.util.ArrayList();
            java.util.List afterList = new java.util.ArrayList();
            java.util.List aroundList = new java.util.ArrayList();
            for (int i = 0; i < advices.length; i++) {
                if (advices[i] instanceof BeforeInterceptor) {
                    beforeList.add(advices[i]);
                } else if (advices[i] instanceof AfterInterceptor) {
                    afterList.add(advices[i]);
                } else if (advices[i] instanceof AroundInterceptor) {
                    aroundList.add(advices[i]);
                }
            }
            if (beforeList.size() > 0) {
                beforeAdvices = new BeforeInterceptor[beforeList.size()];
                for (int i = 0; i < beforeList.size(); i++)
                    beforeAdvices[i] = (BeforeInterceptor) beforeList.get(i);
            }
            if (afterList.size() > 0) {
                afterAdvices = new AfterInterceptor[afterList.size()];
                for (int i = 0; i < afterList.size(); i++)
                    afterAdvices[i] = (AfterInterceptor) afterList.get(i);
            }
            if (aroundList.size() > 0) {
                aroundAdvices = new AroundInterceptor[aroundList.size()];
                for (int i = 0; i < aroundList.size(); i++)
                    aroundAdvices[i] = (AroundInterceptor) aroundList.get(i);
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理方法前通知
        Object ret = null;
        boolean haveAround = false;
        if (beforeAdvices != null) {
            for (int i = 0; i < beforeAdvices.length; i++) {
                BeforeInterceptor advice = beforeAdvices[i];
                advice.before(target, method, args);
            }
        }
        if (aroundAdvices != null) {
            for (int i = 0; i < aroundAdvices.length; i++) {
                haveAround = true;
                AroundInterceptor advice = aroundAdvices[i];
                ret = advice.invoke(new SimpleMethodInvocation(target, method, args));
            }
        }
        if (!haveAround)
            ret = method.invoke(target, args);
        if (afterAdvices != null) {
            for (int i = 0; i < afterAdvices.length; i++) {
                afterAdvices[i].after(ret, target, method, args);
            }
        }
        return ret;
    }
}
