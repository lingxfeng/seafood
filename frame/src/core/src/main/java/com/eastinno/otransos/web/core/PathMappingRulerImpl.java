package com.eastinno.otransos.web.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import com.eastinno.otransos.web.IPathMappingRuler;



/**
 * <p>
 * Title: 简单的IPathMappingRuler默认实现示例
 * </p>
 * <p>
 * Description: 实现IPathMappingRuler接口，用于处理用户请求路径
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 0.7
 */
public class PathMappingRulerImpl implements IPathMappingRuler {

    private String moduleName = "";

    private Map params = null;

    private String URIpath;// 包括应用名称，不带参数的全路径.其作用用来处理绝对路径及相对路径情况。

    private String servletPath;// 不带参数的路径，通过其进行Ruler转换。

    private String command;// 解析后的命令

    private String urlPattern = "modern";

    private String suffix = "";

    public PathMappingRulerImpl(HttpServletRequest request,Map<String,Object> modules) {
        String path = request.getPathInfo();// 这个得到的是应用的实际路径，也即?号后面的数据
        String uriPath = request.getRequestURI();// URI是访问网址后面的全路径，也即/开始后面的部分。但不包括?后面的数据
        while (uriPath != null && uriPath.indexOf("//") > -1)
            uriPath = uriPath.substring(1);// 过滤掉第一个"/"符号
        if (path == null)
            path = request.getServletPath();// 这里主要防止pathInfo为null的情况，当用户在web.xml没有作/*的配置时，将会出现
        String contextPath = request.getContextPath();
        String realUrilPath = uriPath.substring(contextPath.length());// contextPath为servlet路径如/、""、/hello等
        int l = realUrilPath.indexOf(path);
        if (l <= 0) {
            l = uriPath.lastIndexOf(".");
            if (l > 0) {
                suffix = uriPath.substring(l + 1);
                urlPattern = CLASSIC_PATTERN;
            }
        } else {
            suffix = realUrilPath.substring(1, l);
        }
        this.URIpath = uriPath;
        this.servletPath = path;
        // if(servletPath.endsWith(".java"))urlPattern=CLASSIC_PATTERN;
        doPathParse(request,modules);
    }


	public String getModuleName() {
        return moduleName;
    }

    public Map getParams() {
        return params;
    }

    private void doPathParse(HttpServletRequest request ,Map<String,Object> modules) {
        String path = this.servletPath;
        if (path == null || "".equals(path))
            return;
        while (path.length()>1&&path.charAt(0) == '/')
            path = path.substring(1);// 过滤掉第一个"/"符号
        if (CLASSIC_PATTERN.equals(urlPattern)) {
            moduleName = "/" + cancelEJFTag(path);
            return;
        }
//        String[] s = path.split("/");
//        if (s != null) {
//            if (s.length > 0)// 处理模板名称
//            {
//                moduleName = "/" + cancelEJFTag(s[0]);
//            }
//            if (s.length > 1)// 处理第二个参数
//            {
//                s[1] = cancelEJFTag(s[1]);
//                if (!isParas(s[1]))// 称判断是否是属于参数,不是则直接赋值为命令
//                    command = s[1];
//                else {
//                    if (params == null)
//                        params = new HashMap();
//                    String key = s[1].substring(0, s[1].indexOf("="));
//                    String value = s[1].substring(s[1].indexOf("=") + 1);
//                    params.put(key, value);
//                }
//            }
//            if (s.length > 2)// 处理第三个参数
//            {
//                if (params == null)
//                    params = new HashMap();
//                for (int i = 2; i < s.length; i++) {
//                    s[i] = cancelEJFTag(s[i]);
//                    String v = s[i];
//                    String key;
//                    String value;
//                    if (isParas(v)) {
//                        key = s[i].substring(0, s[i].indexOf("="));
//                        value = s[i].substring(s[i].indexOf("=") + 1);
//                    } else {
//                        key = getDefaultParamName(i - 2);
//                        value = s[i];
//                    }
//                    params.put(key, value);
//                }
//            }
//        }
        moduleName = this.getActionPath(moduleName,path,modules);
        if(StringUtils.isBlank(moduleName)){
        	moduleName = "/" + (path.split("/").length>0?path.split("/")[0]:"");
			return;
		}
        path=("/"+path).substring(moduleName.length());
        if (path == null || "".equals(path))
            return;
        while (path.length()>1&&path.charAt(0) == '/'){
            path = path.substring(1);
        }
		String[] amp = path.split("/");
		if(amp.length>=1){	//解析方法
			if(params == null){
				params = new HashMap<String,String>();
			}
			String cmd = StringUtils.isNotBlank(amp[0])?amp[0]:null;
			if(amp.length>=2){
				for(int i=1;i<amp.length;i++){
					String p = amp[i];
					params.put("_pa_in"+(i-1)+"d_ex_", p);
				}
			}
			if(StringUtils.isNotBlank(cmd)){
				command = cmd;
				params.put("cmd", cmd);
			}
		}
        /*
         * if (path.lastIndexOf(".java") > 0) { moduleName = path.substring(0, path.lastIndexOf(".java")); } else { path
         * = this.URIpath; if (path.indexOf("/ejf/") == 0) path = path.substring(4); int n = path.lastIndexOf('/'); if
         * (n > 0) { para = path.substring(n + 1); moduleName = path.substring(0, n); if (para.lastIndexOf('.') > 0)
         * para = para.substring(0, para.lastIndexOf('.')); } } if (para != null && (!para.equals(""))) { this.hasPara =
         * true; //this.paras = para.split("_"); }
         */
    }

    /**
     * 取消参数中结尾的.java
     * 
     * @param s
     * @return
     */
    protected String cancelEJFTag(String s) {
        if (s.toLowerCase().endsWith("." + suffix)) {
            return s.substring(0, s.length() - suffix.length() - 1);
        } else
            return s;
    }

   /* protected String getDefaultParamName(int i) {
        String ret = "id";
        // 下面添加处理默认第二、第三个参数的名称
        if (i == 1)
            ret = "page";
        if (i == 2)
            ret = "other";
        return ret;
    }*/

    protected boolean isParas(String s) {
        return s.indexOf("=") > 0;// 包含=号的即为参数
    }

    public String getCommand() {
        return command;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getURIpath() {
        return URIpath;
    }

    public void setURIpath(String ipath) {
        URIpath = ipath;
    }

    public final void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public final void setCommand(String command) {
        this.command = command;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getSuffix() {
        return suffix;
    }
    private String getActionPath(String moduleName,String path,Map<String,Object> modules){
    	if (path == null || "".equals(path))
            return moduleName;
    	String lastModuleName = moduleName;
    	while (path.length()>1&&path.charAt(0) == '/')
            path = path.substring(1);// 过滤掉第一个"/"符号
    	String[] acs = path.split("/");
    	if(acs.length>1){
    		moduleName = getActionPath(moduleName+"/"+acs[0],path.substring(acs[0].length()),modules);
    	}
    	if(modules.get(lastModuleName)!=null&&modules.get(moduleName)==null){
    		path = null;
    		return lastModuleName;
    	}
    	return moduleName;
    }
}
