package com.eastinno.otransos.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.core.DefaultRequestProcessor;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class DiscoFilter implements Filter {

	static final long serialVersionUID = 887867880L;
	private static final Logger logger = Logger.getLogger(DiscoFilter.class);

	/**
	 * 在web.xml中配置disco service时可以配置discoConfigPath属性来加载指定路径下的配置文件 <br />
	 * <param-name>discoConfigPath</param-name>
	 * <param-value>/WEB-INF/disco-web.xml</param-value> </context-param>
	 */
	public static final String DISCO_CONFIGURE_KEY = "discoConfigPath";

	public static final String DISCO_PROCESSOR_IN_CONTANIER = "Disco-Processor";
	
	public FilterConfig filterConfig = null;
	
	private static int contextPathLength = 0;

	/**
	 * Disco加载器，用来加载Disco的各种配置文件，缺省配置信息等
	 */
	private FrameworkLoader loader;

	/**
	 * Disco的核心处理器，由主控Filter即DiscoFilter来调用并进行请求处理工作
	 */
	private RequestProcessor processor;

	/**
	 * 初始化各种配置,包括解析disco-web.xml
	 * 
	 * @param servletContext
	 */
	private void initDisco(ServletContext servletContext) {
		loader = new FrameworkLoader(this.getConfigures(servletContext));
		loader.setServletContext(servletContext);// 如果是Web应用，需要设置
		// 此上下文。
		loader.initDisco();
		filterConfig.getServletContext().setAttribute(Globals.CONTAINER_CONTEXT, loader.getContainer());
		FrameworkEngine.setDiscoFilter(this);// 把Filter设置到FrameworkEngine中，以便通过FrameworkEngine来控制Filter
	}

	/**
	 * 得到disco的配置文件 若未在web.xml中设定discoConfigPath则使用默认的/WEB-INF/disco-web.xml
	 * <context-param> <param-name>discoConfigPath</param-name>
	 * <param-value> /WEB-INF/disco-web.xml</param-value> </context-param>
	 */
	private String[] getConfigures(ServletContext servletContext) {
		String paravalue = servletContext.getInitParameter(DISCO_CONFIGURE_KEY);
		String[] s = StringUtils.hasLength(paravalue) ? StringUtils.tokenizeToStringArray(paravalue, ",") : null;
		return s;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		filterConfig = config;
		// 初始化web.xml参数值
		// super.init(config);
		initDisco(config.getServletContext());
		// 初始化Servlet配置
		// logger.debug(I18n.getLocaleMessage("core.initialization.servlet.configuration"));
		String contextPath = config.getServletContext().getContextPath();
		contextPathLength = (StringUtils.isEmpty(contextPath) || "/".equals(contextPath) ? 0 : contextPath.length());
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		if(!toDisco(request)){
			chain.doFilter(request, response); 
		}else{
			// 初始化Request
	        try {
	            // 初始化上下文根，并设置框架运行模式
	            doInitRequest(request, response);
	            if (processor == null) {	            		            	
	                /**
	                 * 首先尝试从容器中加载框架处理器, 若无法从容器中加载处理器,则使用默认的处理器
	                 */
	                Object obj = loader.getContainer().getBean(DISCO_PROCESSOR_IN_CONTANIER);
	                if (obj != null && obj instanceof RequestProcessor) {
	                    // 成功从容器中加载处理器
	                    logger.debug(I18n.getLocaleMessage("core.successfully.in.containers.loaded.processor"));
	                    processor = (RequestProcessor) obj;
	                    processor.setFilter(this);
	                    processor.setWebConfig(loader.getWebConfig());
	                } else {
	                    processor = new DefaultRequestProcessor(this, loader.getWebConfig());
	                }
	            }
	            processor.process(request, response);
	        } catch (Throwable error) {
	            throw new ServletException(error);
	        }
		}
	}

	@Override
	public void destroy() {
		/**
		 * 停止后台应用
		 */
		loader.destroyApps();
	}

	protected void doInitRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put(ActionContext.HTTP_REQUEST, request);
		map.put(ActionContext.HTTP_RESPONSE, response);
		map.put(ActionContext.SERVLET_CONTEXT, filterConfig.getServletContext());
		ActionContext.setContext(new ActionContext(map));
		LocalManager.setLocale(request.getLocale());
		if (loader.getWebConfig().isDebug()) {// 调试模式每次都要初始化配置文件
			initDisco(filterConfig.getServletContext());
		}
	}
	
	/**
	 * 是否进入disco框架
	 * @param request
	 * @return
	 */
	protected boolean toDisco(HttpServletRequest request){
		//处理静态文件
		String target = request.getRequestURI().replaceAll("//", "/");
		if (contextPathLength != 0){
			target = target.substring(contextPathLength);
		}
		if(StringUtils.isEmpty(target)||"/".equals(target)||(target.indexOf('.')>=0&&target.indexOf(".java")<0)){
			//静态文件,.java请求,跳转主页
			return false;
		}else{
			//判断请求的是否是Servlet 域名/servlet/test/test
			if(target.indexOf("/servlet")==0){ 
				return false;
			}
			return true;
		}
	}

}
