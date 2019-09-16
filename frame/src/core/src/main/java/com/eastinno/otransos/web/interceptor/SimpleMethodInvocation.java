package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.Method;

/**
 * 简单的方法调用
 * 
 * @author lengyu
 */
public class SimpleMethodInvocation implements MethodInvocation {

    private Object target;

    private Method method;

    private Object[] args;

    public SimpleMethodInvocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args.clone();
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return args.clone();
    }

    public Object getTarget() {

        return target;
    }

    public Object proceed() throws Throwable {
        return method.invoke(target, args);
    }
}
