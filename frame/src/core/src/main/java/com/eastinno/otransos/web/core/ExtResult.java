package com.eastinno.otransos.web.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Ext框架提交结果，表示是否成功及错误信息
 * 
 * @author lengyu
 */
public class ExtResult {
    private boolean success;// 是否成功或失败
    private Object data;// 返回的数据结果集
    private Map<String, Object> errors = new HashMap<String, Object>();// 失败对应的错误信息

    public ExtResult() {
        this(true);
    }

    public ExtResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
