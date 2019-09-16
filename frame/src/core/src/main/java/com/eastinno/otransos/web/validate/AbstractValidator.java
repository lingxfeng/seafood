package com.eastinno.otransos.web.validate;

/**
 * Disco中验证器的基类，实现了Validaotr接口。<br>
 * 提供了addError供子类调用，从而使验证结果的添加非常容易。 用户自定义的验证器类均可直接继承该类实现。
 * 
 * @author lengyu
 */
public abstract class AbstractValidator implements Validator {
    /**
     * 往验证结果信中添加验证错误
     * 
     * @param obj 验证的目标对象
     * @param value 要验证的值
     * @param errors 验证错误提示结果集
     */
    public void addError(TargetObject obj, Object value, Errors errors) {
        addError(obj, value, errors, null);
    }

    /**
     * 往验证结果集中添加一条自定义的验证错误信息，针对类似StringRequired中的min_msg等特殊的验证提示信息使用。
     * 
     * @param obj 验证目录对象
     * @param value 验证的值
     * @param errors 验证错误结果集
     * @param customMessage 自定义的验证出错提示信息
     */
    public void addError(TargetObject obj, Object value, Errors errors, String customMessage) {
        ValidateResult result = errors.getErrorObject(obj.getFieldName());
        if (result == null) {
            result = new ValidateResult(obj, value);
            errors.addError(result);
        }
        result.addErrorValidator(this);
        if (customMessage != null)
            result.addCustomMessage(this, customMessage);
    }
}
