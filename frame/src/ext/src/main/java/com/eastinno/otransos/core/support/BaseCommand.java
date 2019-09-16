package com.eastinno.otransos.core.support;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.core.util.I18n;

abstract public class BaseCommand implements ICommand {

    protected List<String> errors = new ArrayList<String>();

    public void notNull(Object obj, String name) {
        if (obj == null) {
            errors.add(name + I18n.getLocaleMessage("ext.Not.empty"));
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.trim().equals("")) {
                errors.add(name + I18n.getLocaleMessage("ext.Not.empty"));
            }
        }
    }

    public void isNumber(Object obj, String name) {
        this.notNull(obj, name);
        String str = obj == null ? "" : obj.toString();
        boolean ret = str.matches("\\d*");
        if (!ret) {
            errors.add(name + I18n.getLocaleMessage("ext.Must.figure"));
        }
    }

    public void isString(Object obj, String name) {
        this.notNull(obj, name);
        String str = (String) obj;
        boolean ret = str.matches("\\w*");
        if (!ret) {
            errors.add(name + I18n.getLocaleMessage("ext.Must.text"));
        }
    }

    abstract public List<String> vaild();
}
