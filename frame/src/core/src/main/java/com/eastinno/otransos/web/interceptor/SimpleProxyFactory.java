package com.eastinno.otransos.web.interceptor;

/**
 * 简单的代码封装
 * 
 * @author lengyu
 */
public class SimpleProxyFactory {
    private Object target;
    private Class[] interfaces;
    private Interceptor[] advices;

    public Object createObject() throws Exception {
        return java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces,
                new SimpleInvocationHandler(target, advices));
    }

    public Class getObjectType() {
        return target.getClass();
    }

    public boolean isSingleton() {
        return false;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setAdvices(Interceptor[] advices) {
        this.advices = advices;
    }

    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }
}
