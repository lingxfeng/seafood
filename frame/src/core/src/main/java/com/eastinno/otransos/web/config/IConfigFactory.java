package com.eastinno.otransos.web.config;

import java.util.Map;

import org.dom4j.Document;

/**
 * <p>
 * Title:配置信息处理接口
 * </p>
 * <p>
 * Description:配置工厂接口定义,目的在于支持多种配置方式。
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

public interface IConfigFactory {

    public final static String DEBUG = "com.eastinno.otransos.isDebug";

    public final static String MaxUploadFileSize = "com.eastinno.otransos.maxUploadFileSize";

    public final static String UploadSizeThreshold = "com.eastinno.otransos.uploadSizeThreshold";

    public final static String MaxDirectJumpToActionTimes = "com.eastinno.otransos.maxDirectJumpToActionTimes";

    /**
     * 设置Action扫描的路径，根据你的情况调整
     */
    public final static String DefaultActionPackage = "com.eastinno.otransos.defaultActionPackages";

    public final static String MessageResourceLoader = "com.eastinno.otransos.messageResourceLoader";
    public final static String Language = "com.eastinno.otransos.language";
    public final static String PropertiesType = "com.eastinno.otransos.propertiesType";
    public final static String PermissionVerify = "com.eastinno.otransos.permissionVerify";

    /**
     * 初始化表单信息
     * 
     * @param forms
     */
    public void initForm(Map forms);

    /**
     * 初始化Module信息
     * 
     * @param module
     */
    public void initModule(Map module);

    /**
     * 初始化Page信息
     * 
     * @param page
     */
    public void initPage(Map page);

    /**
     * 初始化其它配置信息，存放到HashMap中
     * 
     * @return 其它配置信息
     */
    public Map initOtherCfg();

    public Document getDoc();
}
