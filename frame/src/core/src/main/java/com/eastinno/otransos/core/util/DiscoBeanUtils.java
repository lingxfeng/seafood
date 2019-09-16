package com.eastinno.otransos.core.util;

/**
 * @intro
 * @version v_0.1
 * @author lengyu
 * @since 2014年1月10日 下午9:31:07
 */
public class DiscoBeanUtils {

    /**
     * 判断对象是否是cglib的子类
     * 
     * @param value
     * @return
     */
    public static boolean checkLazyloadNull(Object value) {
        if (value == null)
            return true;
        if (value.getClass().toString().indexOf("$$EnhancerByCGLIB") > 0) {
            try {
                value.toString();
            } catch (NullPointerException e) {
                return true;
            }
        }
        return false;
    }
}
