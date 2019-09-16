package com.eastinno.otransos.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryService;
import com.eastinno.otransos.core.util.TagUtil;

@Service
public class LogicUtil {
    @Resource
    private ISystemDictionaryService systemDictionaryService;

    public void setSystemDictionaryService(ISystemDictionaryService systemDictionaryService) {
        this.systemDictionaryService = systemDictionaryService;
    }

    public String showTitle(String sn, Object value) {
        SystemDictionary dict = this.systemDictionaryService.getBySn(sn);
        if (dict == null)
            return "";
        for (SystemDictionaryDetail d : dict.getChildren()) {
            if (d.getTvalue().equals(value)) {
                return d.getTitle();
            }
        }
        return "";
    }

    public String showCheckBox(Object value) {
        String ret = "";
        if (((value instanceof Boolean)) && (((Boolean) value).booleanValue())) {
            ret = "checked";
        }
        return ret;
    }

    public String showCheckBox(String obj, Object value) {
        String ret = "";
        if ((obj != null) && (obj.equals(value))) {
            ret = "checked";
        }
        return ret;
    }

    public List<SystemDictionaryDetail> getSystemDictionaryDetailbySn(String sn) {
        SystemDictionary systemDictionary = this.systemDictionaryService.getBySn(sn);
        List set = null;
        if (systemDictionary != null) {
            set = systemDictionary.getChildren();
        }
        return set;
    }

    public String selectBox(String sn, Object value) {
        String ret = null;
        SystemDictionary dic = this.systemDictionaryService.getBySn(sn);
        if (dic != null) {
            List list = dic.getChildren();
            if (list != null) {
                String[][] s = new String[list.size() + 1][2];
                s[0][0] = "";
                s[0][1] = "--请选择--";
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext();) {
                    SystemDictionaryDetail obj = (SystemDictionaryDetail) it.next();
                    s[(i + 1)][0] = obj.getTitle();
                    s[(i + 1)][1] = obj.getTvalue();
                    i++;
                }
                ret = TagUtil.options(s, value != null ? value.toString() : null);
            }
        }
        return ret;
    }

    public String radioBox(String sn, Object value) {
        return radioBox(sn, sn, value);
    }

    public String radioBox(String fieldName, String sn, Object value) {
        String ret = "";
        SystemDictionary dic = this.systemDictionaryService.getBySn(sn);
        if (dic != null) {
            List list = dic.getChildren();
            if (list != null) {
                for (Iterator it = list.iterator(); it.hasNext();) {
                    SystemDictionaryDetail obj = (SystemDictionaryDetail) it.next();
                    ret = ret + obj.getTitle() + "<input type='radio' name='" + fieldName + "' value='" + obj.getTvalue() + "'";
                    if (value != null)
                        ret = ret + (obj.getTvalue().equals(value.toString()) ? "checked" : "");
                    ret = ret + ">";
                }
            }
        }

        return ret;
    }

    public String checkBox(String sn, Object value) {
        return checkBox(sn, sn, value);
    }

    public String checkBox(String fieldName, String sn, Object value) {
        String ret = "";
        SystemDictionary dic = this.systemDictionaryService.getBySn(sn);
        List s = Arrays.asList(value.toString().split(","));
        if (dic != null) {
            List list = dic.getChildren();
            if (list != null) {
                for (Iterator it = list.iterator(); it.hasNext();) {
                    SystemDictionaryDetail obj = (SystemDictionaryDetail) it.next();
                    ret = ret + obj.getTitle() + "<input type=checkbox name='" + fieldName + "' value='" + obj.getTvalue() + "'"
                            + (s.contains(obj.getTvalue()) ? " checked" : "") + ">";
                }
            }
        }

        return ret;
    }
}
