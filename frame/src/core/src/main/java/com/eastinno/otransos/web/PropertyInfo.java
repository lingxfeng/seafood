package com.eastinno.otransos.web;

import org.springframework.beans.MutablePropertyValues;

public abstract class PropertyInfo {
    private MutablePropertyValues propertyValues = new MutablePropertyValues();

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
