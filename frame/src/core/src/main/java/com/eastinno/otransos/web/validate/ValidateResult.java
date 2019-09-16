package com.eastinno.otransos.web.validate;

import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 验证结果，该对象存放在Errors结果集中
 * 
 * @author lengyu
 */
public class ValidateResult {

    private TargetObject targetObject;

    private Object value;

    private Map<Validator, String> customMessages = new java.util.HashMap<Validator, String>();

    public ValidateResult() {

    }

    public ValidateResult(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public void addCustomMessage(Validator validator, String message) {
        customMessages.put(validator, message);
    }

    public ValidateResult(TargetObject targetObject, Object value) {
        this.targetObject = targetObject;
        this.value = value;
    }

    private List<Validator> errorsValidators = new java.util.ArrayList<Validator>();

    public void addErrorValidator(Validator validator) {
        this.errorsValidators.add(validator);
    }

    public String msg() {
        return message();
    }

    public String message() {
        return getMessage();
    }

    public String getMessage() {
        String message = "";
        if (StringUtils.hasLength(targetObject.getKey()))// 通过资源文件加载属性
        {

        } else if (StringUtils.hasLength(targetObject.getDefaultMessage())) {
            message = targetObject.getDefaultMessage();
        } else if (errorsValidators.size() > 0) {
            for (int i = 0; i < errorsValidators.size(); i++) {
                if (!customMessages.containsKey(errorsValidators.get(i))) {
                    message += errorsValidators.get(i).getDefaultMessage() + " ";
                    if (i < errorsValidators.size() - 1)
                        message += "\n";
                }
            }
        }
        for (String msg : customMessages.values()) {
            message += msg;
        }
        String name = StringUtils.hasLength(targetObject.getDisplayName()) ? targetObject.getDisplayName()
                : targetObject.getFieldName();
        if (message != null) {
            message = message.replaceAll("\\{%1\\}", value == null ? "NULL" : value.toString());// {%0}代表验证对象的名称，{%1}代表参数值
            message = message.replaceAll("\\{1\\}", value == null ? "NULL" : value.toString());// {0}代表验证对象的名称，{1}代表参数值
        }
        if (message != null) {
            message = message.replaceAll("\\{%0\\}", name == null ? "NULL" : name);// {%0}代表验证对象的名称，{%1}代表参数值
            message = message.replaceAll("\\{0\\}", name == null ? "NULL" : name);// {0}代表验证对象的名称，{1}代表参数值
        }
        return message;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return this.message();
    }
}
