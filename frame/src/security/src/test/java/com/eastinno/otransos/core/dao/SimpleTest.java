package com.eastinno.otransos.core.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SimpleTest extends TestCase {
    public void testGenic() {
        List list = new ArrayList();
        Type clz = list.getClass();
        System.out.println(clz);
        if ((clz instanceof ParameterizedType))
            System.out.println(((ParameterizedType) clz).getActualTypeArguments()[0]);
    }
}
