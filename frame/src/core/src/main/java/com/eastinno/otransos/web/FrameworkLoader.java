package com.eastinno.otransos.web;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.BeanDefinition;
import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Bean;
import com.eastinno.otransos.container.impl.BeanDefinitionImpl;
import com.eastinno.otransos.container.impl.DefaultContainer;
import com.eastinno.otransos.container.impl.WebContextContainer;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.core.util.ResolverUtil;
import com.eastinno.otransos.web.ajax.AjaxServiceContainer;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.config.BeanConfigReader;
import com.eastinno.otransos.web.config.ConfigureResourceLoader;
import com.eastinno.otransos.web.config.DefaultWebConfig;
import com.eastinno.otransos.web.config.FileResourceLoader;
import com.eastinno.otransos.web.config.ServletContextResourceLoader;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.core.RequestScope;
import com.eastinno.otransos.web.core.SessionScope;
import com.eastinno.otransos.web.exception.FrameworkException;

/**
 * Disco加载器，用来加载Disco的各种配置文件，缺省配置信息等。 该类由ActionServlet调用并起动
 * 
 * @author lengyu
 */
public class FrameworkLoader implements Serializable {
    private static final long serialVersionUID = 1223936880827975513L;
    public final static String DefaultActionPackages = "defaultActionPackages";
    /**
     * disco-web.xml配置文件
     */
    private String[] configures;

    private WebConfig webConfig = new DefaultWebConfig();

    private Container container;

    private boolean haveInitDisco = false;// 标注是否已经初始化Disco

    private ServletContext servletContext;// 用于servletContext上下文

    private static final Logger logger = Logger.getLogger(FrameworkLoader.class);

    private ConfigureResourceLoader resourceLoader;

    public void setResourceLoader(ConfigureResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        // 赋值 将根目录赋值给全局变量Globals.APP_BASE_DIR
        if (servletContext != null) {
            Globals.APP_BASE_DIR = servletContext.getRealPath("/");
            Globals.CLASS_PATH_DIR = Globals.APP_BASE_DIR + "WEB-INF/classes/";
        }
    }

    public WebConfig getWebConfig() {
        return webConfig;
    }

    public FrameworkLoader(String[] configures) {
        this.configures = configures;
    }

    public void initDisco() {
        logger.info(I18n.getLocaleMessage("core.execute.Disco.initialization.applications"));
        if (resourceLoader == null) {
            if (servletContext != null)
                resourceLoader = new ServletContextResourceLoader(servletContext);
            else
                resourceLoader = new FileResourceLoader();
        }
        initContainer();
        FrameworkEngine.setWebConfig(webConfig);// 初始化框架工具
        FrameworkEngine.setContainer(container);// 在引擎中安装容器
        AjaxUtil.setServiceContainer(new AjaxServiceContainer(container));// 初始化Ajax容器服务
        initTemplate(); // 初始化模版
        invokeApps();// 在应用启动的时候启动一些配置好的应用
        haveInitDisco = true;
        logger.info(I18n.getLocaleMessage("core.disco.initialized"));
    }

    private String[] getConfigures() {
        List<String> cs = new ArrayList<String>();
        if (this.configures != null)
            cs.addAll(Arrays.asList(this.configures));
        else {
            if (new File(servletContext.getRealPath(Globals.DISCO_CONFIGURE_PATH)).exists()) {
                cs.add(Globals.DISCO_CONFIGURE_PATH);
            }
        }
        cs.add("classpath:/com/eastinno/otransos/web/resources/disco-web.xml");
        // 尝试加载tools配置文件
        String disco_ext_config = "/com/eastinno/otransos/ext/disco-web.xml";
        if (this.getClass().getResource(disco_ext_config) != null) {
            cs.add("classpath:" + disco_ext_config);
        }
        return cs.toArray(new String[cs.size()]);
    }

    /**
     * 初始化容器
     */
    protected void initContainer() {
        // 读取配置文件并赋值给webConfig
        webConfig.setResourceLoader(resourceLoader);
        webConfig.setConfigures(getConfigures());
        // 根据配置文件进行初始化
        // TODO 这个初始化配置文件建议后期改为Nutz那样使用JSON配置的方式
        webConfig.init();
        DefaultContainer c = (servletContext == null ? new DefaultContainer() : new WebContextContainer(servletContext));
        c.registerBeanDefinitions(webConfig.getBeanDefinitions());
        loadDefaultAction(c);// 根据包的配置参数自动加载bean信息
        ((DefaultWebConfig) webConfig).loadAlias();// 加载并处理别名
        c.registerScope("request", new RequestScope(c));
        c.registerScope("session", new SessionScope(c));
        // System.out.println(I18n.getLocaleMessage("core.started.containers.initialization"));
        c.refresh();
        this.container = c;
        // 此处用来把spring的Web应用上下文保存到指定的ServletContext属性中，此处需要进一步的修改
        /*
         * Iterator it = c.getContainers().values().iterator(); while (it.hasNext()) { Object obj = it.next(); if (obj
         * instanceof SpringContainer && servletContext != null) { ((SpringContainer)
         * obj).registerWebContext(servletContext); } }
         */
    }

    protected void loadDefaultAction(DefaultContainer container) {
        String[] packages1 = this.webConfig.getDefaultActionPackages();
        String[] packages2 = null;
        if (this.servletContext != null) {
            String dp = servletContext.getInitParameter(DefaultActionPackages);
            if (dp != null)
                packages2 = StringUtils.tokenizeToStringArray(dp, ",");
        }
        logger.info("--------------------------------------------------------------");
        loadBeanFromPackages(container, packages1);
        loadBeanFromPackages(container, packages2);
        loadBeanFromPackages(container, new String[] {Globals.DEFAULT_ACTTION_PACKAGE});
        logger.info("--------------------------------------------------------------");
        loadActionFromPackages(container, packages1);
        loadActionFromPackages(container, packages2);
        loadActionFromPackages(container, new String[] {Globals.DEFAULT_ACTTION_PACKAGE});
        logger.info("--------------------------------------------------------------");
    }

    /**
     * 在指定的包结构下加载Bean
     * 
     * @param container Disco容器
     * @param packages 包结构路径
     */
    private void loadBeanFromPackages(DefaultContainer container, String[] packages) {
        if (packages != null && packages.length > 0) {
            Map newModules = new HashMap();
            List beanDefinitions = ((DefaultWebConfig) webConfig).getBeanDefinitions();
            for (int i = 0; i < packages.length; i++) {
                logger.info(I18n.getLocaleMessage("core.in") + packages[i]
                        + I18n.getLocaleMessage("core.view.package.and.load.the.default.Bean"));
                ResolverUtil<Bean> r = new ResolverUtil<Bean>();
                r.findAnnotated(Bean.class, packages[i]);
                Iterator it = r.getClasses().iterator();
                while (it.hasNext()) {
                    Class clz = (Class) it.next();
                    if (Modifier.isAbstract(clz.getModifiers()))
                        continue;
                    Bean ac = (Bean) clz.getAnnotation(Bean.class);
                    String name = ac.name();
                    if ("".equals(name)) {
                        name = clz.getSimpleName().substring(0, 1).toLowerCase() + clz.getSimpleName().substring(1);
                        if (name.endsWith("Impl"))
                            name = name.substring(0, name.length() - "Impl".length());
                    }
                    // 如果容器中已经注册过这个类，或者已经具有同名的Bean，则不进行注册
                    if (container.getBeanDefinition(clz) == null && !this.findBeanDefinitions(beanDefinitions, name)) {
                        logger.info(I18n.getLocaleMessage("core.automatic.load.Bean") + clz.getName()
                                + I18n.getLocaleMessage("core.beanName") + name);
                        BeanDefinitionImpl definition = new BeanDefinitionImpl(name, clz, ac.scope());
                        BeanConfigReader.handleAutoInject(definition, ac.inject());// 处理自动注入
                        beanDefinitions.add(definition);
                        container.registerBeanDefinition(name, definition);
                    }
                }
            }

        }
    }

    private boolean findBeanDefinitions(List definitions, String name) {
        for (int i = 0; i < definitions.size(); i++) {
            BeanDefinition definition = (BeanDefinition) definitions.get(i);
            if (definition.getBeanName().equals(name))
                return true;
        }
        return false;
    }

    private void loadActionFromPackages(DefaultContainer container, String[] packages) {
        if (packages != null && packages.length > 0) {
            Map newModules = new HashMap();
            for (int i = 0; i < packages.length; i++) {
                logger.info(I18n.getLocaleMessage("core.in") + packages[i]
                        + I18n.getLocaleMessage("core.view.package.and.load.the.default.Action"));
                ResolverUtil<IWebAction> r = new ResolverUtil<IWebAction>();
                r.findImplementations(IWebAction.class, packages[i]);
                Iterator it = r.getClasses().iterator();
                while (it.hasNext()) {
                    Class clz = (Class) it.next();
                    if (Modifier.isAbstract(clz.getModifiers()))
                        continue;
                    String name = clz.getSimpleName().substring(0, 1).toLowerCase() + clz.getSimpleName().substring(1);
                    if (name.endsWith("Action"))
                        name = name.substring(0, name.length() - "Action".length());
                    Action ac = (Action) clz.getAnnotation(Action.class);
                    if (ac != null && !"".equals(ac.path()))
                        name = ac.path();
                    if (name.charAt(0) != '/')
                        name = "/" + name;
                    // 如果容器中已经注册过这个类，或者已经具有同path的module，则不进行注册
                    if (container.getBeanDefinition(clz) == null && this.webConfig.getModules().get(name) == null) {
                        logger.info(I18n.getLocaleMessage("core.automatic.load.Action") + " " + clz.getName()
                                + I18n.getLocaleMessage("core.path") + name);
                        Module mc = new Module();
                        mc.setAction(clz.getName());
                        mc.setPath(name);
                        if (ac != null) {
                            mc.setViews(ac.view());
                            mc.setScope(ac.scope());
                            mc.setAlias(ac.alias());
                            if (!"".equals(ac.inject()))
                                mc.setInject(ac.inject());
                            mc.setAutoToken(ac.autoToken());
                            mc.setMessageResource(ac.messageResource());
                            mc.setValidate(ac.validate());
                        }
                        if(newModules.get(name) != null){
                        	logger.error("!!!!!!!This path is already exist!The old one will be replaced!" + name);
                        }
                        newModules.put(mc.getPath(), mc);
                    }
                }
                logger.info("--------------------------------------------------------------------------------");
            }
            this.getWebConfig().getModules().putAll(newModules);
            List newBeans = BeanConfigReader.parseBeansFromModules(newModules);

            // 把Bean的注册信息添加到WebCofig中
            ((DefaultWebConfig) webConfig).getBeanDefinitions().addAll(newBeans);
            // 把bean信息注册到容器中
            container.registerBeanDefinitions(newBeans);
        }
    }

    /**
     * 初始化模板 不支持velocity的配置读入，有待改进
     * 
     * @param config
     * @throws ServletException
     */
    protected void initTemplate() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "file,class");
        p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        if (!StringUtils.hasLength(webConfig.getTemplateBasePath()))
            webConfig.setTemplateBasePath(Globals.DEFAULT_TEMPLATE_PATH);
        String realTemplatePath = webConfig.getTemplateBasePath();
        File file = new File(webConfig.getTemplateBasePath());
        if (!file.exists() && servletContext != null)
            realTemplatePath = servletContext.getRealPath(webConfig.getTemplateBasePath());
        p.setProperty("file.resource.loader.path", realTemplatePath);
        try {
            Velocity.init(p);
        } catch (Exception e) {
            logger.error(I18n.getLocaleMessage("core.initialization.template.error") + e);
            throw new com.eastinno.otransos.web.exception.FrameworkException(I18n.getLocaleMessage("core.initialization.template.error"), e);
        }
    }

    /**
     * 在应用启动的时候启动一些配置好的应用；比如后台的监控线程等;
     */
    protected void invokeApps() {
        List apps = webConfig.getInitApps();
        for (int i = 0; i < apps.size(); i++) {
            try {
                Map app = (Map) apps.get(i);
                Method init = (Method) app.get("init-method");
                if (init != null) {
                    init.invoke(app.get("classname"), new Object[] {});
                    logger.debug("app " + app.get("classname") + "has started");
                }
            } catch (Exception e) {
                throw new FrameworkException(I18n.getLocaleMessage("core.initialization.procedures.for.exception"), e);
            }
        }
    }

    // 停止启动的应用；
    public void destroyApps() {
        List apps = webConfig.getInitApps();
        for (int i = 0; i < apps.size(); i++) {
            try {
                Map app = (Map) apps.get(i);
                Method des = (Method) app.get("destroy-method");
                if (des != null) {
                    des.invoke(app.get("classname"), new Object[] {});
                }
            } catch (Exception e) {
                throw new FrameworkException(I18n.getLocaleMessage("core.initialization.end.for.exception"), e);
            }
        }
    }

    public Container getContainer() {
        return container;
    }

}
