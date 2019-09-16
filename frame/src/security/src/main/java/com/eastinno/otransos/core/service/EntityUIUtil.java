package com.eastinno.otransos.core.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.web.ajax.JSonConvertUtil;

public class EntityUIUtil {
    private static Map<String, Map> caches = new HashMap();

    private static java.lang.reflect.Field findField(Class clz, String name) {
        try {
            java.lang.reflect.Field field = clz.getDeclaredField(name);
            if ((field == null) && (clz.getSuperclass() != Object.class))
                return findField(clz.getSuperclass(), name);
            return field;
        } catch (NoSuchFieldException nf) {
            if (clz.getSuperclass() != Object.class) {
                return findField(clz.getSuperclass(), name);
            }
        }
        return null;
    }

    public static Map doLoadColumnField(Class clz) {
        List fields = new ArrayList();
        Map cm = new HashMap();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(clz);
        for (int i = 0; i < targetPds.length; i++) {
            PropertyDescriptor pd = targetPds[i];
            String name = pd.getName();
            if (!name.equals("class")) {
                fields.add(name);
                Map map = new HashMap();
                map.put("name", name);
                java.lang.reflect.Field field = findField(clz, name);
                if (field != null) {
                    Class type = field.getType();
                    String typeName = "";
                    Field a = (Field) field.getAnnotation(Field.class);
                    if ((a != null) && (StringUtils.hasLength(a.name()))) {
                        map.put("header", a.name());
                    }

                    if (JSonConvertUtil.isSimpleType(type)) {
                        typeName = type.getSimpleName().toLowerCase();
                    } else if (Map.class.isAssignableFrom(type))
                        typeName = "map";
                    else if ((Collection.class.isAssignableFrom(type)) || (type.isArray()))
                        typeName = "array";
                    else {
                        typeName = "object";
                    }
                    map.put("type", typeName);
                }
                cm.put(name, map);
            }
        }
        Map map = new HashMap();
        map.put("fields", fields);
        map.put("columnMap", cm);
        caches.put(clz.getName(), map);
        return map;
    }
}
