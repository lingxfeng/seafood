package com.eastinno.otransos.web.validate;

import java.util.Map;

/**
 * 验证错误结果<br>
 * 在Disco中，当一次请求过程中，若出现了验证错误，则会把该错误自动封装到ErrorsMap，并传送到视图层的模板对象中。 视图层可以直接通过$errors标签访问该对象中的ErrorsMap对象
 * 
 * @author lengyu,stef_wu
 */
public class Errors {
    /**
     * 存放结果的Map<String,ValidateResult>
     */
    private ErrorsMap errors = new ErrorsMap();

    /**
     * 验证主体对象,比如Domain
     */
    private Object object;

    public Errors() {

    }

    public Errors(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * 获取验证错误的数量
     * 
     * @return 未难过的验证数
     */
    public int getErrorCount() {
        return errors.size();
    }

    /**
     * 检测是否验证错误结果为空，也即是否具有未通过的验证
     * 
     * @return 若没有出现验证错误，即所有验证都通过，则返回true，否则返回false.
     */
    public boolean getEmpty() {
        return errors == null || errors.size() < 1;
    }

    /**
     * 检测此次请求中是否包含验证错误，或者具有未通过的验证
     * 
     * @return 若有未通过的验证则返回true，否则返回false
     */

    public boolean hasError() {
        return !getEmpty();
    }

    /**
     * 查找并返回指定属性(字段)的验证错误结果，是getMessage(name)的快速访问方式
     * 
     * @param fieldName 属性(字段)名称
     * @return 若针对该属性(字段)包含错误信息，则返回该提示信息，否则返回null
     */
    public String msg(String fieldName) {
        return message(fieldName);
    }

    /**
     * 与msg方法一样，查找并返回指定属性(字段)的验证错误结果，是getMessage(name)的快速访问方式
     * 
     * @param fieldName 属性(字段)名称
     * @return 若针对该属性(字段)包含错误信息，则返回该提示信息，否则返回null
     */
    public String message(String fieldName) {
        return getMessage(fieldName);
    }

    /**
     * 查找并返回指定属性(字段)的验证错误结果
     * 
     * @param fieldName 属性(字段)名称
     * @return 若针对该属性(字段)包含错误信息，则返回该提示信息，否则返回null
     */
    public String getMessage(String fieldName) {
        ValidateResult obj = (ValidateResult) errors.get(fieldName);
        if (obj != null)
            return obj.getMessage();
        else
            return null;
    }

    public ValidateResult getErrorObject(String name) {
        return (ValidateResult) errors.get(name);
    }

    public void addError(ValidateResult validatorResult) {
        errors.put(validatorResult.getTargetObject().getFieldName(), validatorResult);
    }

    public String getMsg() {
        return getMessage();
    }

    public String getMessage() {
        return toString();
    }

    public String toString() {
        String ret = "";
        java.util.Iterator it = errors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry en = (Map.Entry) it.next();
            ValidateResult vr = (ValidateResult) en.getValue();
            ret += vr.getMessage();
        }
        return ret;
    }

    public Map getErrors() {
        return errors;
    }

}
