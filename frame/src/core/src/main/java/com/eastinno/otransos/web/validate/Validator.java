package com.eastinno.otransos.web.validate;

/**
 * 验证器
 * 
 * @author lengyu
 */
public interface Validator {
    /**
     * 执行具体的验证操作
     * 
     * @param obj 验证目标对象
     * @param value 验证值
     * @param errors 验证异常信息封装
     */
    void validate(TargetObject obj, Object value, Errors errors);// 验证

    /**
     * 验证器默认错误信息
     * 
     * @return 返回验证器默认的错误提示信息
     */
    String getDefaultMessage();
}
