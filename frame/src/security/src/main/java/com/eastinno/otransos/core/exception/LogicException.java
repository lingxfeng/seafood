package com.eastinno.otransos.core.exception;

/**
 * @intro 业务逻辑异常
 * @version v0.1
 * @author maowei
 * @since 2013年5月27日 下午4:00:33
 */
public class LogicException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LogicException() {
        super("系统出现逻辑错误！");
    }

    public LogicException(String msg) {
        super(msg);
    }

    public LogicException(String msg, Throwable trw) {
        super(msg, trw);
    }
}
