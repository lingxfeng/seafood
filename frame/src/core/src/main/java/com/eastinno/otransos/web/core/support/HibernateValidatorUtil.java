package com.eastinno.otransos.web.core.support;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.eastinno.otransos.web.validate.ValidateType;
import com.eastinno.otransos.web.validate.ValidatorManager;

public class HibernateValidatorUtil {
    // 通过ValidatorFactory得到了一个Validator的实例. Validator是线程安全的,并且可以重复使用
    public static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 对一个给定实体对象的单个属性进行校验. 其中属性名称需要符合JavaBean规范中定义的属性名称.
     * 
     * @param obj
     * @param fieldName
     * @param validateManager
     */
    public static void validateProperty(Object obj, String fieldName, ValidatorManager validateManager) {
        try {
            Set<ConstraintViolation<Object>> cvs = HibernateValidatorUtil.validator.validateProperty(obj, fieldName);
            if (cvs.size() > 0) {
                for (ConstraintViolation<Object> cv : cvs) {
                    validateManager.addCustomError(cv.getPropertyPath().toString(), cv.getMessage(), ValidateType.Field);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 校验如果把一个特定的值赋给一个类的某一个属性的话,是否会违反此类中定义的约束条件.
     * 
     * @param obj
     * @param fieldName
     * @param validateManager
     */
    public static void validateValue(Class clz, String fieldName, Object fieldValue, ValidatorManager validateManager) {
        Set<ConstraintViolation<Class<?>>> cvs = HibernateValidatorUtil.validator.validateValue(clz, fieldName, fieldValue);
        if (cvs.size() > 0) {
            for (ConstraintViolation<Class<?>> cv : cvs) {
                validateManager.addCustomError(cv.getPropertyPath().toString(), cv.getMessage(), ValidateType.Field);
            }
        }
    }

    // @Null 被注释的元素必须为 null
    // @NotNull 被注释的元素必须不为 null
    // @NotBlank//不能为空，检查时会将空格忽略
    // @AssertTrue 被注释的元素必须为 true
    // @AssertFalse 被注释的元素必须为 false
    // @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
    // @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
    // @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
    // @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
    // @Size(max, min) 被注释的元素的大小必须在指定的范围内
    // @Digits(integer=2,fraction=20)//检查是否是一种数字的整数、分数,小数位数的数字。
    // @Past 被注释的元素必须是一个过去的日期
    // @Future 被注释的元素必须是一个将来的日期
    // @Pattern(value) 被注释的元素必须符合指定的正则表达式
    // @URL(protocol=,host,port)//检查是否是一个有效的URL，如果提供了protocol，host等，则该URL还需满足提供的条件
    // @Valid 该注解只要用于字段为一个包含其他对象的集合或map或数组的字段，或该字段直接为一个其他对象的引用，这样在检查当前对象的同时也会检查该字段所引用的对象

    // Hibernate Validator 附加的 constraint
    // @CreditCardNumber//对信用卡号进行一个大致的验证
    // @Email 被注释的元素必须是电子邮箱地址
    // @Length 被注释的字符串的大小必须在指定的范围内
    // @NotEmpty 被注释的字符串的必须非空
    // @Range 被注释的元素必须在合适的范围内
}
