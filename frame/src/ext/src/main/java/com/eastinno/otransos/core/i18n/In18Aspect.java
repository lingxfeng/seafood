package com.eastinno.otransos.core.i18n;

import java.util.Locale;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.BeanUtils;

import com.eastinno.otransos.web.LocalManager;

public class In18Aspect {
    public void onSave(JoinPoint thisJoinPoint) {
        Object obj = thisJoinPoint.getArgs()[0];
        if (obj instanceof LocaleSupport) {
            Locale local = LocalManager.getCurrentLocal();
            String localName = local.toString().toUpperCase();
            try {
                String realClassName = obj.getClass().getCanonicalName() + localName;
                if (obj.getClass().getCanonicalName().indexOf(localName) < 0) {
                    realClassName = realClassName + localName;
                }
                Object newObj = BeanUtils.instantiateClass(Class.forName(realClassName));
                BeanUtils.copyProperties(obj, newObj);

                thisJoinPoint.getArgs()[0] = newObj;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println(obj);
    }

}
