package com.eastinno.otransos.web;

import java.util.Map;

/**
 * <p>
 * Title: 用户请求url缺省映射处理器
 * </p>
 * <p>
 * Description: 用户请求url缺省映射处理器
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
public interface IPathMappingRuler {
    public String CLASSIC_PATTERN = "classic";

    /**
     * 得到模块的名称
     * 
     * @return 返回该请求的模块名称
     */
    public String getModuleName();

    /**
     * 得到模块的缺省参数
     * 
     * @return 返回该请求的模块参数
     */
    public Map getParams();

    /**
     * 返回该请求的模块命令
     * 
     * @return 该请求的模块命令
     */
    public String getCommand();

    public String getUrlPattern();

    /**
     * 该请求的URL后缀名
     * 
     * @return
     */
    public String getSuffix();
}
