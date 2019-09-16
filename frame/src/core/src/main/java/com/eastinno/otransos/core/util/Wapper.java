package com.eastinno.otransos.core.util;

public class Wapper {
    public static Class toWapperClass(Class clazz) {
        if (!clazz.isPrimitive())
            return clazz;
        else {
            if (int.class.equals(clazz))
                return Integer.class;
            if (char.class.equals(clazz))
                return Character.class;
            if (boolean.class.equals(clazz))
                return Boolean.class;
            if (long.class.equals(clazz))
                return Long.class;
            if (float.class.equals(clazz))
                return Float.class;
            if (byte.class.equals(clazz))
                return Byte.class;
            if (short.class.equals(clazz))
                return Short.class;
            if (double.class.equals(clazz))
                return Double.class;
        }
        return Object.class;
    }
}
