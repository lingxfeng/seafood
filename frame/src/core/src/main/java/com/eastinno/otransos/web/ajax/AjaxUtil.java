package com.eastinno.otransos.web.ajax;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.DiscoBeanUtils;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 这个工具类的一些方法正确执行需要先安装一个AjaxServiceContainer
 * 
 * @author lengyu
 */
public class AjaxUtil {
    private static AjaxServiceContainer serviceContainer;
    public static int JSON_OBJECT_MAX_DEPTH = 5;

    public static boolean isRemoteMethod(String serviceName, Method method) {
        if (!serviceContainer.isAllowMethod(serviceName, method.getName()) || !Modifier.isPublic(method.getModifiers()))
            return false;
        return true;
    }

    public static String getJSON(Object value) {
        return getJSON(value, JSON_OBJECT_MAX_DEPTH);
    }

    public static String getJSON(Object value, int maxDepth) {
        HandleSimpleJSON handler = new HandleSimpleJSON(maxDepth);
        return handler.getJSON(0, value);
    }

    public static void convertEntityToJson(IPageList pageList) {
        if (pageList != null && pageList.getResult() != null) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                Object o = pageList.getResult().get(i);
                if (o instanceof IJsonObject) {
                    pageList.getResult().set(i, ((IJsonObject) o).toJSonObject());
                }
            }
        }
    }

    public static void convertEntityToJson(List list) {
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                if (o instanceof IJsonObject) {
                    list.set(i, ((IJsonObject) o).toJSonObject());
                }
            }
        }
    }

    public static List getAjaxStub(String serviceName) {
        Object bean = serviceContainer.getService(serviceName);
        List ret = new ArrayList();
        Method[] ms = bean.getClass().getMethods();
        for (int i = 0; i < ms.length; i++) {
            String methodName = ms[i].getName();
            if (ms[i].getDeclaringClass() != Object.class && serviceContainer.isAllowMethod(serviceName, methodName)) {
                Map map = new HashMap();
                map.put("name", methodName);
                List para = new ArrayList();
                Class[] paras = serviceContainer.getSignatures(serviceName, methodName);
                if (paras == null)
                    paras = ms[i].getParameterTypes();
                if (paras != null && paras.length > 0) {
                    for (int j = 0; j < paras.length; j++) {
                        if (paras[j] != null) {
                            Map p = new HashMap();
                            p.put("name", "p" + j);
                            p.put("type", paras[j].getName());
                            para.add(p);
                        }
                    }
                }
                map.put("para", para);
                ret.add(map);
            }
        }
        return ret;
    }

    public static AjaxServiceContainer getServiceContainer() {
        return serviceContainer;
    }

    public static void setServiceContainer(AjaxServiceContainer serviceContainer) {
        AjaxUtil.serviceContainer = serviceContainer;
    }

    static class HandleJSON {
        private Map vars = new HashMap();
        private Stack stack = new Stack();
        private Stack declars = new Stack();
        private int maxDepth = 6;
        private int d = 0;

        HandleJSON() {
        }

        HandleJSON(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        public String generatorJSON(Object value, int depth) {
            if (value != null && value instanceof IJsonObject)
                value = ((IJsonObject) value).toJSonObject();
            StringBuilder ret = new StringBuilder("");
            StringBuilder varName = new StringBuilder("v" + (d++));
            if (value == null || depth > maxDepth) {
                ret.append("null");
            } else if (JSonConvertUtil.isSimpleType(value.getClass())) {// 简单类型
                if (value instanceof Date) {
                    return "new Date(" + CommUtil.formatDate("yyyy,M-1,d,H,m,s", value) + ")";
                } else if (value instanceof String || value.getClass().isEnum()) {
                    StringBuilder sv = new StringBuilder(value.toString());
                    if (sv.indexOf("\"") < 0) {
                        return new StringBuilder("\"").append(CommUtil.convert2json(sv.toString())).append("\"").toString();
                    }
                    sv = new StringBuilder(sv.toString().replaceAll("\"", "&quot;"));
                    return new StringBuilder("\"").append(CommUtil.convert2json(sv.toString())).append("\".replace(/&quot;/g, '\"')").toString();
                } else
                    return "" + value;
            } else {
                vars.put(value, varName.toString());
                if (value instanceof Map) {
                    ret = new StringBuilder(handlerMap(varName.toString(), value, depth));
                } else if (value instanceof Collection) {
                    ret = new StringBuilder(handlerCollection(varName.toString(), value, depth));
                } else if (value.getClass().isArray()) {
                    ret = new StringBuilder(handlerArray(varName.toString(), value, depth));
                } else {
                    ret = new StringBuilder(handlerObject(varName.toString(), value, depth + 1));
                }

            }
            return ret.toString();
        }

        public String handlerCollection(String varName, Object value, int depth) {
            StringBuilder ret = new StringBuilder("");
            declars.push(new StringBuilder("var ").append(varName).append("=[];").toString());
            Iterator it = ((Collection) value).iterator();
            int i = 0;
            while (it.hasNext()) {
                Object v = it.next();
                if (v == null) {
                    ret.append("null");
                    // ret += "null";
                } else {
                    ret.append(varName).append("[").append(i).append("]=");
                    // ret += varName + "[" + i + "]=";
                    String o = (String) vars.get(v);
                    if (o != null)
                        ret.append(o);
                    // ret += o;
                    else {
                        String p = generatorJSON(v, depth);
                        ret.append(p);
                        // ret += p;
                    }
                }
                ret.append(";");
                // ret += ";";
                i++;
            }
            if (ret.toString().endsWith(";;"))
                ret = new StringBuilder(ret.substring(0, ret.length() - 1));
            stack.push(ret.toString());
            ret = new StringBuilder(varName);
            return ret.toString();
        }

        public String handlerArray(String varName, Object value, int depth) {
            StringBuilder ret = new StringBuilder("");
            declars.push(new StringBuilder("var ").append(varName).append("=[];").toString());
            int max = Array.getLength(value);
            for (int i = 0; i < max; i++) {
                Object v = Array.get(value, i);
                if (v == null) {
                    ret.append("null");
                    // ret += "null";
                } else {
                    String o = (String) vars.get(v);
                    ret.append(varName).append("[").append(i).append("]=");
                    if (o != null)
                        ret.append(o).append(";");
                    // ret += o + ";";
                    else {
                        ret.append(generatorJSON(v, depth));
                    }
                }
                ret.append(";");
            }
            if (ret.toString().endsWith(";;"))
                ret = new StringBuilder(ret.substring(0, ret.length() - 1));
            stack.push(ret.toString());
            // ret = new StringBuilder(varName);
            return varName;
        }

        public String handlerMap(String varName, Object value, int depth) {
            StringBuilder ret = new StringBuilder("");
            declars.push(new StringBuilder("var ").append(varName).append("={};").toString());
            Map map = (Map) value;
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object propertyName = (String) it.next();
                ret.append(varName).append("['").append(propertyName).append("']").append("=");
                // ret += varName + "." + propertyName + "=";
                Object v = map.get(propertyName);
                if (v == null) {
                    ret.append("null");
                    // ret += "null";
                } else {
                    String o = (String) vars.get(v);
                    if (o != null)
                        ret.append(o);

                    else {
                        ret.append(generatorJSON(v, depth));
                    }
                }
                ret.append(";");
            }
            if (ret.toString().endsWith(";;"))
                ret = new StringBuilder(ret.substring(0, ret.length() - 1));
            stack.push(ret.toString());
            // ret = new StringBuilder(varName);
            return varName;
        }

        public String handlerObject(String varName, Object value, int depth) {
            StringBuilder ret = new StringBuilder("");
            declars.push(new StringBuilder("var ").append(varName).append("={};").toString());
            BeanWrapper wrapper = new BeanWrapperImpl(value);
            PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                String propertyName = pds[i].getName();
                if (serviceContainer != null && !serviceContainer.isAllowProperty(value.getClass(), propertyName)) {
                    continue;
                }
                if (!"class".equals(propertyName) && wrapper.isReadableProperty(propertyName)) {
                    try {
                        Object v = wrapper.getPropertyValue(propertyName);
                        ret.append(varName).append("['").append(propertyName).append("']").append("=");
                        if (v == null) {
                            ret.append("null");
                        } else {
                            String o = (String) vars.get(v);
                            if (o != null)
                                ret.append(o);
                            else {
                                ret.append(generatorJSON(v, depth));
                            }
                        }
                        ret.append(";");
                    } catch (Exception e) {
                        e.printStackTrace();// errors
                        ret.append("null;");
                    }
                }
            }
            if (ret.toString().endsWith(";;"))
                ret = new StringBuilder(ret.substring(0, ret.length() - 1));
            stack.push(ret.toString());
            // ret = new StringBuilder(varName);
            return varName;
        }

    }

    static class HandleSimpleJSON {
        private Set stack = new HashSet();
        private int maxDepth = 5;

        private HandleSimpleJSON(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        public String getJSON(int depth, Object value) {
            StringBuilder ret = new StringBuilder("");
            if (value != null && value instanceof IJsonObject)
                value = ((IJsonObject) value).toJSonObject();
            if (value == null) {
                ret.append("null");
            } else if (value instanceof JSON) {
                String v = ((JSON) value).toJSONString();
                ret.append(v);
            } else if (JSonConvertUtil.isSimpleType(value.getClass())) {
                String b = "";
                if (value instanceof Date) {
                    b = ((Date) value).getTime() + "";
                    // b = "new Date(" + CommUtil.formatDate("yyyy,M-1,d,H,m,s", value) + ")";
                } else if (value instanceof String || value.getClass().isEnum()) {
                    StringBuilder sv = new StringBuilder(value.toString());
                    if (sv.indexOf("\"") < 0) {
                        b = new StringBuilder("\"").append(CommUtil.convert2json(sv.toString())).append("\"").toString();
                    } else {
                        sv = new StringBuilder(sv.toString().replaceAll("\"", "&quot;"));
                        b = new StringBuilder("\"").append(CommUtil.convert2json(sv.toString())).append("\".replace(/&quot;/g, '\"')").toString();
                    }
                } else
                    b = "" + value;
                ret.append(b);
            } else if (value instanceof Map) {
                ret.append("{");
                Map map = (Map) value;
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    Object propertyName = (String) it.next();
                    ret.append("\"").append(propertyName).append("\"").append(":");
                    Object v = map.get(propertyName);
                    if (DiscoBeanUtils.checkLazyloadNull(v)) {
                        ret.append("null");
                    } else if (depth < maxDepth && !stack.contains(v)) {// 通过stack来避双向关联无限伸缩//
                        stack.add(v);
                        ret.append(getJSON(depth + 1, v));
                        stack.remove(v);
                    } else
                        ret.append("undefined");
                    if (it.hasNext())
                        ret.append(",");
                }
                ret.append("}");
            } else if (value instanceof Collection) {
                ret.append("[");
                Iterator it = ((Collection) value).iterator();
                while (it.hasNext()) {
                    Object v = it.next();
                    ret.append(getJSON(depth, v));
                    if (it.hasNext())
                        ret.append(",");
                }
                ret.append("]");
            } else if (value.getClass().isArray()) {
                int max = Array.getLength(value);
                ret.append("[");
                for (int i = 0; i < max; i++) {
                    ret.append(getJSON(depth, Array.get(value, i)));
                    if (i < max - 1)
                        ret.append(",");
                }
                ret.append("]");
            } else {
                ret.append("{");
                BeanWrapper wrapper = new BeanWrapperImpl(value);
                PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
                for (int i = 0; i < pds.length; i++) {
                    String propertyName = pds[i].getName();
                    if (!"class".equals(propertyName) && wrapper.isReadableProperty(propertyName)
                            && (serviceContainer == null || serviceContainer.isAllowProperty(value.getClass(), propertyName))) {
                        ret.append("\"").append(propertyName).append("\"").append(":");
                        Object v = wrapper.getPropertyValue(propertyName);
                        if (DiscoBeanUtils.checkLazyloadNull(v)) {
                            ret.append("null");
                        } else if (depth < maxDepth && !stack.contains(v)) {// 通过stack来避双向关联无限伸缩//
                            stack.add(v);
                            ret.append(getJSON(depth + 1, v));
                            stack.remove(v);
                        } else
                            ret.append("undefined");
                        if (i < pds.length - 1)
                            ret.append(",");
                    }
                }
                ret.append("}");
            }
            // stack.clear();
            return ret.toString();
        }
    }

    public static void main(String[] args) {
        String s = "[11:123}]";
        try {
            JSONObject.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
