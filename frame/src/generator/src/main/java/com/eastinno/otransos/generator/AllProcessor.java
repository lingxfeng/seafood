package com.eastinno.otransos.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.velocity.context.Context;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.web.tools.generator.TemplateProcess;

public class AllProcessor implements TemplateProcess {
    private String beanClass;

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    protected <T> void loadAllField(List<Field> list, Class<T> clz) {
        for (Field field : clz.getDeclaredFields()) {
            list.add(field);
        }
        if (!clz.getSuperclass().getName().equals(Object.class.getName())) {
            loadAllField(list, clz.getSuperclass());
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public void process(Context context) throws Exception {
        Class bean = Class.forName(getBeanClass());
        if (bean.isAnnotationPresent(Embedded.class)) {
            return;
        }

        if (bean != null && bean.isAnnotationPresent(Entity.class)) {//
            String domainName = bean.getSimpleName();
            FormPO af = (FormPO) bean.getAnnotation(FormPO.class);
            context.put("domainLabel", (af != null && af.label() != null) ? af.label() : domainName);
            context.put("domainName", domainName);
            context.put("nowTime", new java.util.Date().toLocaleString());
            String domain = domainName.substring(0, 1).toLowerCase() + domainName.substring(1);
            context.put("domain", domain);
            List<Map> fields = new ArrayList<Map>();
            List<Map> complexFields = new ArrayList<Map>();
            List<Field> fls = new java.util.ArrayList<Field>();
            loadAllField(fls, bean);
            for (Field field : fls) {
                /*
                 * if (field.getName().equals("status")) { continue; }
                 */
                if (resovleGenerAnno(field)) {
                    continue;
                }
                if (isSimpleType(field.getType())) {
                    fields.add(parseField(field));
                    resovleIdAnno(context, fields, field);
                } else if (isComplexFields(field.getType())) {
                    complexFields.add(parseField(field));
                    resovleEmbed(fields, complexFields, field);
                }

            }
            context.put("fields", fields);
            context.put("complexFields", complexFields == null || complexFields.isEmpty() ? null : complexFields);
            context.put("packageName", packageName);
            String locath = packageName.substring(packageName.lastIndexOf(".") + 1);
            context.put("locath", locath);
        }
    }

    @SuppressWarnings("unchecked")
    private Map parseField(Field field) {
        Map map = new HashMap();
        map.put("name", field.getName());
        map.put("lable", field.getName());
        if (isSimpleType(field.getType())) {
            if (field.getType().equals(java.lang.Boolean.class)) {
                map.put("type", "boolean");
            } else if (field.getType().equals(java.util.Date.class)) {
                map.put("type", "date");
            } else if (Number.class.isAssignableFrom(field.getType())) {
                map.put("type", "number");
            }
        } else if (isComplexFields(field.getType())) {
            String name = field.getType().getSimpleName();
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            map.put("type", name);
        }
        map.put("present", true);
        // add text area for fckeditor
        map.put("editor", false);
        com.eastinno.otransos.container.annonation.Field af = field.getAnnotation(com.eastinno.otransos.container.annonation.Field.class);
        if (af != null) {
            if (af.present()) {
                map.put("present", true);
            } else {
                map.put("present", false);
            }
            if (af.editor()) {
                map.put("editor", true);
            }
            if (StringUtils.hasLength(af.name()))
                map.put("lable", af.name());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void resovleEmbed(List<Map> fields, List<Map> complexFields, Field field) {
        if (field.isAnnotationPresent(Embedded.class)) {
            for (Field f : field.getType().getDeclaredFields()) {
                if ((field.getName().equals("status")) || field.getName().equals("serialVersionUID")) {
                    continue;
                }
                if (resovleGenerAnno(field)) {
                    continue;
                }
                if (isSimpleType(f.getType())) {
                    fields.add(parseField(f));
                }
            }
            complexFields.remove(complexFields.size() - 1);
        }
    }

    private boolean resovleGenerAnno(Field field) {
        if (field.isAnnotationPresent(com.eastinno.otransos.container.annonation.Field.class)) {
            com.eastinno.otransos.container.annonation.Field f = field.getAnnotation(com.eastinno.otransos.container.annonation.Field.class);
            boolean gen = f.gener();
            return !gen;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void resovleIdAnno(Context context, List<Map> fields, Field field) {
        if (field.isAnnotationPresent(Id.class)) {
            fields.remove(fields.size() - 1);
            String fieldName = field.getName();
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            context.put("id", fieldName);
            context.put("idName", field.getName());
            context.put("idType", field.getType().getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isSimpleType(Class clazz) {
        if (BeanUtils.isSimpleProperty(clazz) || Number.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean isComplexFields(Class clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        return true;
    }

}
