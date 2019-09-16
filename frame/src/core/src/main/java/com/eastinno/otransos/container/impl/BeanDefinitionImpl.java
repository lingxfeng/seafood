package com.eastinno.otransos.container.impl;

import org.springframework.beans.MutablePropertyValues;

import com.eastinno.otransos.container.BeanDefinition;
import com.eastinno.otransos.container.ConstructorArguments;

/**
 * BeanDefinition的默认实现
 * 
 * @author lengyu
 */
public class BeanDefinitionImpl implements BeanDefinition {
    private Class beanClass;

    private String beanName;

    private String factoryMethod;

    private ConstructorArguments constructorArguments = new ConstructorArguments();

    private MutablePropertyValues propertyValues = new MutablePropertyValues();

    private String scope = "singleton";

    private boolean lazy = false;

    private boolean abstra = false;

    private String injectType = "NONE";

    public BeanDefinitionImpl() {

    }

    public BeanDefinitionImpl(String beanName) {
        this.beanName = beanName;
    }

    public BeanDefinitionImpl(String beanName, Class beanClass, String scope) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.scope = scope;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public ConstructorArguments getConstructorArguments() {
        return constructorArguments;
    }

    public String getFactoryMethod() {
        return factoryMethod;
    }

    public String getInjectType() {
        return injectType;
    }

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public String getScope() {
        return scope;
    }

    public boolean isAbstract() {

        return abstra;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setConstructorArguments(ConstructorArguments constructorArguments) {
        this.constructorArguments = constructorArguments;
    }

    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public void setInjectType(String injectType) {
        this.injectType = injectType;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "beanName=" + beanName + ",beanClass=" + beanClass + ",scope=" + scope;
    }
}
