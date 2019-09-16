package com.eastinno.otransos.core.util;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.core.FrameworkEngine;

/**
 * @author lengyu
 * @since 2007.3
 */
public class I18n {
    private static Map<Locale, ResourceBundle> bundles = new java.util.HashMap<Locale, ResourceBundle>();
    private static I18n singleton = new I18n();
    private static final Logger logger = Logger.getLogger(I18n.class);

    /**
     * 更改成某一个语言的支持
     * 
     * @param language
     */
    public static void changeLocale(String language) {
        Locale locale = new Locale(language);
        if (ActionContext.getContext() != null)
            ActionContext.getContext().getSession().setAttribute(Globals.LOCALE_SESSION, locale);
    }

    /**
     * 先从模块名.properties里得到值 如果得不到,则到application_local.properties里取
     * 
     * @param property
     * @return
     */
    public static String get(String property) {
        return getPropertyValue(property, null);

    }

    /**
     * 从一个指定的模块中加载消息资源
     * 
     * @param property
     * @param moduleName
     * @return
     */
    public static String get(String property, String moduleName) {
        return getPropertyValue(property, moduleName);
    }

    /**
     * 支持可变参数的形式调用
     * 
     * @param property 属性名称
     * @param firstParas 第一个参数
     * @param otherParas 第二个以外的参数
     * @return 返回对属性中的占位符进行替换后的多国语言信息
     */
    public static String get(String property, String firstParas, String... otherParas) {
        int other = 0;
        if (otherParas != null)
            other = otherParas.length;
        String[] args = new String[1 + other];
        args[0] = firstParas;
        for (int i = 1; i < args.length; i++)
            args[i] = otherParas[i - 1];
        return getParameterMessage(property, args);
    }

    /**
     * 支持可变参数的形式调用
     * 
     * @param property
     * @param paras
     * @return
     */
    public static String getParameterMessage(String property, String... paras) {
        String value = getPropertyValue(property, null);
        if (paras != null)
            for (int i = 0; i < paras.length; i++)
                value = value.replaceAll("\\{" + i + "\\}", paras[i]);
        return value;
    }

    public static I18n getInstance() {
        return singleton;
    }

    /**
     * 获得客户端的Locale 如果客户没有选择指定Locale，则返回浏览器中的首选Locale，如果已经指定了某一种语言，则返回该种语言所对应的Locale
     * 
     * @return 返回客户端的Locale，如果没有则返回服务器的默认Locale
     */
    public static Locale getLocale() {
        try {
            Locale locale = (Locale) com.eastinno.otransos.web.ActionContext.getContext().getRequest().getSession()
                    .getAttribute(Globals.LOCALE_SESSION);
            if (locale == null)
                locale = com.eastinno.otransos.web.ActionContext.getContext().getRequest().getLocale();
            return locale;
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }

    /**
     * 根据当前机器的locale得到语言信息
     * 
     * @param key
     * @return
     */
    public static String getLocaleMessage(String key) {
        return getMessage(key, Globals.LANGUAGE);
    }

    /**
     * 根据客户端信息得到Disco框架系统中的多国语言信息，用于多国语言提示输出
     * 
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        Locale locale = I18n.getLocale();
        return getMessage(key, locale);
    }

    /**
     * 根据指定的Locale得到属性key的信息。
     * 
     * @param key
     * @param locale
     * @return
     */
    public static String getMessage(String key, Locale locale) {
        String ret = "";
        ResourceBundle bundle = bundles.get(locale);
        try {
            if (bundle == null) {
                bundle = ResourceBundle.getBundle("message", locale);
                if (bundle != null) {
                    bundles.put(locale, bundle);
                }
            }
            ret = bundle.getString(key);
        } catch (MissingResourceException mre) {
            ret = key;
        }
        return ret;
    }

    /**
     * 根据Locale构造出其语言的字符前缀
     * 
     * @param locale
     * @return 返回类似en或zh_CN这些指定Locale的语言缩写
     */
    public static String getLocalePrefix(Locale locale) {
        if (locale == null)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(locale.getLanguage());
        if (!"".equals(locale.getCountry()))
            sb.append("_").append(locale.getCountry());
        if (StringUtils.hasText(locale.getVariant()))
            sb.append("_").append(locale.getVariant());
        return sb.toString();
    }

    /**
     * 从某一个模块(页面)资源文件中加载指定的属性资源信息
     * 
     * @param property
     * @param bundleName
     * @return
     */
    private static String getPropertyValue(String property, String bundleName) {
        String ret = "";
        String bundle = bundleName;
        try {
            if (bundle == null && ActionContext.getContext() != null
                    && ActionContext.getContext().getWebInvocationParam() != null) {
                Module module = ActionContext.getContext().getWebInvocationParam().getModule();
                if (module != null) {
                    bundle = module.getPath().replaceAll("/", "");
                    if (StringUtils.hasLength(module.getMessageResource()))
                        bundle = module.getMessageResource() + "/" + bundle;
                }
            }
            ResourceBundle rb = null;
            try {
                rb = FrameworkEngine.get(bundle, null);// 查询module.properties
            } catch (java.util.MissingResourceException e1) {
            }
            if (rb == null) {
                rb = FrameworkEngine.get(null, null);
                ret = rb.getString(property);
            }
            try {
                if (rb != null)
                    ret = rb.getString(property);

            } catch (MissingResourceException mre) {
                rb = FrameworkEngine.get(null, null);
                ret = rb.getString(property);
            }
        } catch (MissingResourceException mre) {
            logger.warn(I18n
                    .getLocaleMessage("No.international.configuration.information.the.use.of.direct.bonding.properties")
                    + property + I18n.getLocaleMessage("substitute"));
            ret = property;
        } catch (Exception e) {
            logger.error(e);
        }
        if (!StringUtils.hasText(ret))
            ret = property;

        return ret;
    }

}
