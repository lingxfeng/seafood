package com.eastinno.otransos.web.tools;

/**
 * 主键id存在的错误
 * 
 * @author lengyu
 */
public class IdExistException extends Exception {
    private final static long serialVersionUID = 9888l;

    public IdExistException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public IdExistException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public String getMessage() {
        // TODO Auto-generated method stub
        return "The ID value has exist!can't save to database;";
    }
}
