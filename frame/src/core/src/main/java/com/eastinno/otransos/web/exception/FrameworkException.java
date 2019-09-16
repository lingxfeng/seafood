package com.eastinno.otransos.web.exception;

/**
 * 自定义框架错误
 * 
 * @author lengyu
 */
public class FrameworkException extends NestedRuntimeException {

    private static final long serialVersionUID = 1839620751050558396L;

    public FrameworkException(String info) {
        super("frameworkException: " + info);
    }

    public FrameworkException(String info, java.lang.Throwable e) {
        super("frameworkException : " + info, e);
    }
}
