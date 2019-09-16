package com.eastinno.otransos.web.validate;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证错误或验证未通过的结果Map<br>
 * 在页面中$errors对象访问的即为该Map对象 可以使用$errors.name来访问Map中的name键值，若访问的键址不存在，则返回null
 * 
 * @author lengyu
 */
public class ErrorsMap extends HashMap {

    private static final long serialVersionUID = -6705696419399350479L;

    /**
     * 检测是否验证错误结果为空，也即是否具有未通过的验证
     * 
     * @return 若没有出现验证错误，即所有验证都通过，则返回true，否则返回false.
     */
    public boolean getEmpty() {
        return this.isEmpty();
    }

    /**
     * 获取验证错误的数量
     * 
     * @return 未难过的验证数
     */
    public int getSize() {
        return this.size();
    }

    /**
     * 得到验证Map中的所有提示信息
     * 
     * @return 验证结果提示信息
     */
    public String getMessage() {
        String ret = "";
        java.util.Iterator it = this.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry en = (Map.Entry) it.next();
            ValidateResult vr = (ValidateResult) en.getValue();
            ret += vr.getMessage();
        }
        return ret;
    }
}
