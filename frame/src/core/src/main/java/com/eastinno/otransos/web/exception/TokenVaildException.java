package com.eastinno.otransos.web.exception;

public class TokenVaildException extends NestedRuntimeException {

    private static final long serialVersionUID = -6872748093533895305L;

    public TokenVaildException(String info) {
        super("TokenVaildException: " + info);
    }

    public TokenVaildException(String info, java.lang.Throwable e) {
        super("TokenVaildException : " + info, e);
    }
}
