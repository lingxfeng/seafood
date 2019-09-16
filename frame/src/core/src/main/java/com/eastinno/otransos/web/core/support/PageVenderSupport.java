package com.eastinno.otransos.web.core.support;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.velocity.context.Context;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.HtmlUtil;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.core.util.TagUtil;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.core.ICustomGlobalsUtilBean;
import com.eastinno.otransos.web.tools.widget.Html;

/**
 * PageVender的支持工具类， 在其中主要是一些工具方法，比如将Session对象转化成Map对象等。
 * 
 * @author lengyu
 */
public class PageVenderSupport {

    /**
     * 将session对象中的值转成Map对象
     * 
     * @param session HttpSession对象
     * @return session对象值的构成的Map
     */
    public static Map<String, Object> session2map(HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            map.put(key, session.getAttribute(key));
        }
        return map;
    }

    /**
     * 将请求对象中的参数转成Map对象
     * 
     * @param request HttpServletRequest对象
     * @return request对象中请求参数的构成的Map
     */
    public static Map<String, Object> request2map(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            map.put(key, request.getAttribute(key));
        }
        return map;
    }

    public static Map<String, Object> request2map(ServletContext context) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (context == null)
            return map;
        Enumeration<String> e = context.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            map.put(key, context.getAttribute(key));
        }
        return map;
    }

    /**
     * 创建工具上下文 把所有要用到的全局工具加载到内存
     * 
     * @param context Velocity上下文对象
     * @param globalUtils 需要传入的Map，作为一个Cache使用
     */
    public static void createUtilContext(Context context, Map<String, Object> globalUtils) {
        if (globalUtils == null) {// 加载全局的Util
            globalUtils = new HashMap<String, Object>();
            List<ICustomGlobalsUtilBean> cus = FrameworkEngine.getContainer().getBeans(ICustomGlobalsUtilBean.class);
            if (cus != null && cus.size() > 0) {
                for (ICustomGlobalsUtilBean cu : cus) {
                    String names = cu.getName();
                    if (names == null || "".equals(names)) {
                        globalUtils.put(cu.getClass().getName(), cu);
                        continue;
                    } else {
                        if (names.charAt(names.length() - 1) == ',') {
                            names = names.substring(0, names.lastIndexOf(","));
                        }
                        String[] ns = names.split(",");
                        if (ns.length > 0) {
                            for (String n : ns) {
                                globalUtils.put(n, cu);
                            }
                        }
                    }
                }
            }
            globalUtils.put("lang", I18n.getInstance());
            globalUtils.put("html", Html.getInstance());
            globalUtils.put("HtmlUtil", HtmlUtil.getInstance());
            globalUtils.put("CommUtil", CommUtil.getInstance());
            globalUtils.put("TagUtil", TagUtil.getInstance());
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {// 视图层通过$!ROLE.hasRole("admin")校验是否具有指定角色
                globalUtils.put("ROLE", subject);
            }
        }
        Iterator<Entry<String, Object>> it = globalUtils.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> en = (Map.Entry<String, Object>) it.next();
            context.put((String) en.getKey(), en.getValue());
        }
    }
}
