package com.eastinno.otransos.core.util;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class XmlElementUtil {
    public static Element findElement(String name, Element el) {
        Element ret = null;
        if (el != null) {
            List e = el.elements(name);
            for (int i = 0; i < e.size(); i++) {
                Element n = (Element) e.get(i);
                if (n.getName().equals(name)) {
                    ret = n;
                    break;
                }
            }
        }
        return ret;
    }

    public static List findElements(String name, Element el) {
        List list = new ArrayList();
        if (el != null) {
            List e = el.elements(name);
            for (int i = 0; i < e.size(); i++) {
                Element n = (Element) e.get(i);
                if (n.getName().equals(name))
                    list.add(n);
            }
        }
        return list;
    }
}
