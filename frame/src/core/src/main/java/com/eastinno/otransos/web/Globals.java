package com.eastinno.otransos.web;

import java.util.Locale;

/**
 * <p>
 * Title:全局配置信息
 * </p>
 * <p>
 * Description: 配置框架的一些系统变量及值
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 1.0
 */
public abstract class Globals {

    /**
     * Disco 当前版本
     */
    public final static String VERSION = "2.1.0-SNAPSHOT";

    /**
     * Disco缺省语言
     */
    public static Locale LANGUAGE = Locale.getDefault();

    /**
     * 语言的资源文件名
     */
    public final static String LANGUAGE_PROPERTIES = "com.eastinno.otransos.web.resources";
    /**
     * disco framework缺省的配置文件所在路径
     */
    public final static String DISCO_CONFIGURE_PATH = "/WEB-INF/disco-web.xml";

    /**
     * 在程序启动的时候根据环境修改为正确的路径
     */
    public static String CONFIG_FILE_FULL_PATH = "/WEB-INF/disco-web.xml";

    /**
     * 解析配置文件的工厂
     */
    public final static String CONFIG_FACTORY_CLASS = "com.eastinno.otransos.web.config.XMLConfigFactory";

    /**
     * 默认的Form
     */
    public final static String DEFAULT_FORM_CLASS = "com.eastinno.otransos.web.WebForm";

    /**
     * 默认的Action存放位置
     */
    public final static String DEFAULT_ACTION_CLASS = "com.eastinno.otransos.web.Action";

    /**
     * 默认action存放位置
     */
    public final static String DEFAULT_ACTTION_PACKAGE = "com.eastinno.otransos.action";

    /**
     * 默认模板视图存放在/WEB-INF/views目录
     */
    public final static String DEFAULT_TEMPLATE_PATH = "/WEB-INF/views/";

    /**
     * 映射后缀名
     */
    public static String MAPPING_SUFFIX = "java";

    /**
     * 默认模板扩展名为html
     */
    public static String DEFAULT_TEMPLATE_EXT = "html";

    /**
     * 模板类型
     */
    public final static String PAGE_TEMPLATE_TYPE = "template";

    /**
     * 禁止重复提交
     */
    public final static String TRANSACTION_FORBITREP_KEY = "com.eastinno.otransos.web.FORBITREP";

    /**
     * Disco容器上下文，其他的非Disco应用可以通过这个标识从ServletContext中取得Disco容器信息
     */
    public final static String CONTAINER_CONTEXT = "Disco_Container_Context";

    /**
     * 是否通过Class环境加载资消息资源文件
     */
    public static boolean LOAD_MESSAGE_RESOURCE_FROM_PATH = false;
    /**
     * 资源文件的位置
     */
    public static String RESOURCE_FILE_PATH = "applicationResources/";

    /**
     * 指定系统中多国语言属性文件的类型，默认为properties文件，同时也支持xml属性文件
     */
    public static String PROPERTIES_TYPE = "properties";
    /**
     * 存放用户指定的语言
     */
    public static String LOCALE_SESSION = "Disco_Locale_Session";

    /**
     * 全局资源文件
     */
    public static String APPLICATION_PROPERTIES_PREFIX = "application";

    /**
     * WEB应用程序根目录，在Web环境中启动Disco后，该值会初始化为应用程序所在的物理目录路径如d:\51ts
     */
    public static String APP_BASE_DIR = "/";

    /**
     * WEB应用程序classes目录所在的物理目录路径，即是APP_BASE_DIR路径+"WEB-INF/classes"
     */
    public static String CLASS_PATH_DIR = "";

    /**
     * Disco容器配置文件的位置
     */
    public static String CONTAINER_CONFIG_LOCATION = "containerConfigLocation";

    public static String SPRING_INTEGERATION_CONTAINER = "SpringIntegerationContainer";

    /**
     * TOKEN名称
     */
    public static final String TOKEN_NAME = "com.eastinno.otransos.token";
}
