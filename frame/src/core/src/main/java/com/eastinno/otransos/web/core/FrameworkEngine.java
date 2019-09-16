package com.eastinno.otransos.web.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.Action;
import com.eastinno.otransos.web.DiscoFilter;
import com.eastinno.otransos.web.FormConfig;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.IWebAction;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebConfig;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.validate.ValidatorManager;

/**
 * 框架核心引擎
 * 
 * @author lengyu
 */
public abstract class FrameworkEngine {
    private static final Logger logger = Logger.getLogger(FrameworkEngine.class);

    private static WebConfig webConfig;

    private static Container container;

    private static I18NResourceCache resourceCache;

    public static final String DEFAULT_OUTPUT_ENCODING = "UTF-8";

    private static ValidatorManager validateManager;

    private static FormHandler formHandler;
 
    private static DiscoFilter discoFilter;

    public static void setFormHandler(FormHandler formHandler) {
        FrameworkEngine.formHandler = formHandler;
    }

    public static ValidatorManager getValidateManager() {
        return validateManager;
    }

    public static void setResourceCache(I18NResourceCache resource) {
        resourceCache = resource;
    }

    public static String getResourcePath() {
        return Globals.RESOURCE_FILE_PATH;
    }

    public static WebConfig getWebConfig() {
        return webConfig;
    }

    public static void setWebConfig(WebConfig webConfig) {
        FrameworkEngine.webConfig = webConfig;
    }

    public static Container getContainer() {
        return container;
    }

    public static void setContainer(Container container) {
        FrameworkEngine.container = container;
    }

    public static DiscoFilter getDiscoFilter() {
        return discoFilter;
    }
    
    public static void setDiscoFilter(DiscoFilter discoFilter) {
        FrameworkEngine.discoFilter = discoFilter;
    }

    public static Page findPage(java.util.Map pages, String name) {
        return (Page) pages.get(name);
    }

    public static FormConfig findForm(java.util.Map forms, String name) {
        return (FormConfig) forms.get(name);
    }

    /*
     * public static Module findModule1(java.util.Map modules, String name) { return (Module) modules.get(name); }
     */

    public static java.util.ResourceBundle get(String resourceName, String locale) {
        return resourceCache.getPropertyValue(resourceName, locale);
    }

    /**
     * 根据request创建一个form对象
     * 
     * @param request
     * @param formName
     * @return 封装了用户数据的Form
     */
    public static WebForm creatWebForm(HttpServletRequest request, String formName, Module module) {
        Map<String, Object> textElement = new HashMap<String, Object>();
        Map<String, FileItem> fileElement = new HashMap<String, FileItem>();
        String contentType = request.getContentType();
        String reMethod = request.getMethod();
        if ((contentType != null) && (contentType.startsWith("multipart/form-data")) && (reMethod.equalsIgnoreCase("post"))) {
            // 二进制 multipart/form-data
            File file = new File(request.getSession().getServletContext().getRealPath("/temp"));
            if (!file.exists()) {
                file.mkdirs();
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(webConfig.getUploadSizeThreshold());
            factory.setRepository(file);
            ServletFileUpload sf = new ServletFileUpload(factory);
            //sf.setSizeMax(webConfig.getMaxUploadFileSize());
            sf.setSizeMax(50*1024*1024);//最大文件值，50M（没找到配置文件。。。先写死吧。。。）
            sf.setHeaderEncoding(request.getCharacterEncoding());
            List reqPars = null;
            try {
                reqPars = sf.parseRequest(request);
                for (int i = 0; i < reqPars.size(); i++) {
                    FileItem it = (FileItem) reqPars.get(i);
                    if (it.isFormField()) {
                        textElement.put(it.getFieldName(), it.getString(request.getCharacterEncoding()));// 文本字段需要转码
                    } else {
                        fileElement.put(it.getFieldName(), it);// 文件不需要转码
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else if ((contentType != null) && contentType.equals("text/xml")) {
            StringBuffer buffer = new StringBuffer();
            try {
                String s = request.getReader().readLine();
                while (s != null) {
                    buffer.append(s + "\n");
                    s = request.getReader().readLine();
                }
            } catch (Exception e) {
                logger.error(e);
            }
            textElement.put("xml", buffer.toString());
        } else {
            textElement = request2map(request);
        }
        // logger.debug("表单数据处理完毕！");
        WebForm wf = findForm(formName);
        wf.setValidate(module.isValidate());// 把针对模块的validate配置设置到Form中
        if (wf != null) {
            wf.setFileElement(fileElement);
            wf.setTextElement(textElement);
        }
        return wf;
    }

    /**
     * 根据formName查找(创建)一个Form对象
     * 
     * @param formName
     * @return 查找Form
     */
    public static WebForm findForm(String formName) {
        WebForm wf = null;
        String formClass = Globals.DEFAULT_FORM_CLASS;
        if (formName != null && (!formName.equals(""))) {
            FormConfig form = FrameworkEngine.findForm(webConfig.getForms(), formName);
            if (form != null)
                formClass = form.getBean();
            // logger.debug("创建配置文件中的表单！"+formClass);
            if (formClass == null || formClass.equals(""))
                formClass = Globals.DEFAULT_FORM_CLASS;
            try {
                wf = (WebForm) Class.forName(formClass).newInstance();
                wf.setProperty(FrameworkEngine.findForm(webConfig.getForms(), formName).getPropertys());
                wf.setFormConfig(form);
            } catch (Exception e) {
                logger.error("创建表单错误" + formClass + e);
                // throw "Can't ctead form of the type"+formClass;
            }
        } else {
            wf = new WebForm();
        }
        // logger.debug(wf.getClass().getName());
        return wf;
    }

    /**
     * 一个通用reuqest数据到map的转换
     * 
     * @param request
     * @return Map
     */
    public static Map request2map(HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration s = request.getParameterNames();
        // System.out.println("参数个数："+request.getParameterMap().size());
        while (s.hasMoreElements()) {
            String name = (String) s.nextElement();
            // eliminateScript((String)request.getParameter(name)));//消除参数中的特殊字符
            String[] vs = request.getParameterValues(name);
            if (vs == null || vs.length < 1)
                map.put(name, null);
            else
                map.put(name, vs.length > 1 ? vs : vs[0]);
        }
        return map;
    }

    /**
     * 根据路径返回一个IWebAction
     * 
     * @param path
     * @return action
     */
    public static IWebAction findAction(String path) {
        // 首先从容器中加载
        Object bean = container.getBean(path);
        if (bean != null && bean instanceof IWebAction) {
            return (IWebAction) bean;
        }
        IWebAction wf = null;
        String actionClass = Globals.DEFAULT_ACTION_CLASS;
        if (path != null && (!path.equals(""))) {
            Module module = webConfig.findModule(path);
            if (module != null)
                actionClass = module.getAction();
            if (actionClass == null || actionClass.equals(""))
                actionClass = Globals.DEFAULT_ACTION_CLASS;
            try {
                wf = (IWebAction) Class.forName(actionClass).newInstance();
            } catch (Exception e) {
                logger.error(I18n.getLocaleMessage("core.web.action.to.create.errors") + actionClass + e);
            }
        } else {
            if (wf == null)
                wf = new Action();
        }

        return wf;
    }

    /**
     * 查找一个IWebAction代理对象
     * 
     * @param methodName 执行的方法名称
     * @param bean 代理Bean
     * @return
     */
    public static IWebAction createProxyAction(final Module module, final Object bean) {
        String methodName = module.getMethod();
        Method method = null;
        try {
            method = bean.getClass().getMethod(methodName);
        } catch (NoSuchMethodException nme) {
            try {
                method = bean.getClass().getMethod(methodName, WebForm.class);
            } catch (NoSuchMethodException e) {
                try {
                    method = bean.getClass().getMethod(methodName, WebForm.class, Module.class);
                } catch (NoSuchMethodException e2) {
                    e.printStackTrace();
                }
            }
        }
        final Method beanMethod = method;
        if (method != null) {
            return (IWebAction) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {IWebAction.class},
                    new InvocationHandler() {
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (method.getName().equals("execute")) {
                                Object result = beanMethod.invoke(bean, args);
                                if (result != null) {
                                    if (result instanceof Page)
                                        return result;
                                    else
                                        return module.findPage(result.toString());
                                }
                                return null;
                            }
                            return method.invoke(bean, args);
                        }
                    });
        }
        return null;
    }

    /**
     * 根据Module返回一个一个IWebAction
     * 
     * @param module
     * @return 相应的Action类
     */
    public static IWebAction findAction(Module module) {
        IWebAction wf = null;
        Object bean = container.getBean(module.getPath());
        if (bean == null) {
            String actionClass = null;
            if (module != null)
                actionClass = module.getAction();
            if (actionClass == null || actionClass.equals(""))
                actionClass = Globals.DEFAULT_ACTION_CLASS;
            try {
                bean = Thread.currentThread().getContextClassLoader().loadClass(actionClass).newInstance();
                // 尝试使用代理对象加载拦截器，此处需要再修改
            } catch (Exception e) {
                logger.error(I18n.getLocaleMessage("core.web.action.to.create.errors") + actionClass + e);
            }
        }
        if (bean != null) {
            if (bean instanceof IWebAction) {
                return (IWebAction) bean;
            } else {
                return createProxyAction(module, bean);
            }
        }
        return wf;
    }

    /**
     * 根据路径返回一个Module，首先从配置文件中查找，如果配置文件中没有，则生成缺省的Module
     * 
     * @param pathName
     * @return 相应的Action类
     */
    public static Module findModule(String pathName) {
        Module m = webConfig.findModule(pathName);
        if (m != null)
            return m;
        m = new Module();
        String actionClass = getActionName(pathName);
        m.setAction(actionClass);
        m.setDefaultPage("default");
        m.setPath(pathName);
        Page p = new Page();
        p.setName("default");
        p.setType(Globals.PAGE_TEMPLATE_TYPE);
        p.setUrl(pathName + "." + Globals.DEFAULT_TEMPLATE_EXT);
        Map pages = new HashMap();
        pages.put(p.getName(), p);
        m.setPages(pages);
        return m;
    }

    /*
     * public static IRequestInterceptor findRequestInterceptor(String name) { IRequestInterceptor interceptor =
     * (IRequestInterceptor)webConfig.getInterceptors().get(name); return interceptor; } public static Iterator
     * getRequestInterceptors() { return webConfig.getInterceptors().values().iterator(); }
     */
    /**
     * 根据url路径生成默认形式的IWebAction类全名
     * 
     * @param pathName
     * @return actionName
     */
    public static String getActionName(String pathName) {
        String[] s = pathName.split("/");
        String actionName = "";
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                if (s[i] != null && (!s[i].equals("")))
                    actionName += s[i] + (i < s.length - 1 ? "_" : "");
            }
        }
        if (actionName.length() > 1)
            actionName = actionName.substring(0, 1).toUpperCase() + actionName.substring(1);// 把首字母转换成大写
        String actionClass = Globals.DEFAULT_ACTTION_PACKAGE + "." + actionName + "Action";
        return actionClass;
    }

    /**
     * 把Map中的内容转换到指定的对象中，在Disco中用于把视图层的对象转换到PO对象中
     * 
     * @param map
     * @param obj
     */
    public static void form2Obj(Map map, Object obj) {
        form2Obj(map, obj, false, false);
    }

    public static void form2Obj(Map map, Object obj, boolean ignoreBlankString, boolean validateRollback) {
        if (formHandler == null)
            formHandler = new FormHandler(container, findValidatorManager());// 先初始化formHandler
        formHandler.form2Obj(map, obj, ignoreBlankString, validateRollback);
    }

    public static void po2form(Object obj, Map map) {
        if (formHandler == null)
            formHandler = new FormHandler(container, findValidatorManager());// 先初始化formHandler
        formHandler.po2form(obj, map);
    }

    /**
     * 查找验证管理器，如果容器中没有，则自动创建一个默认的管理器
     * 
     * @return 查到系统中的验证管理器，若没有验证管理器，则会自动创建一个
     */
    public static ValidatorManager findValidatorManager() {
        if (validateManager != null)
            return validateManager;
        if (container != null)
            validateManager = (ValidatorManager) container.getBean(ValidatorManager.class);
        if (validateManager == null) {
            validateManager = new ValidatorManager();
            validateManager.reload();
        }
        return validateManager;
    }

    /**
     * 根据Module和pageName返回按指定策略寻找的Page；
     * 
     * @param module
     * @param pageName
     * @return Page 返回找到的视图页面
     */
    public static Page findPage(Module module, String pageName) {
        Page page = null;
        page = findPage(webConfig.getPages(), pageName);
        if (page != null) {
            return page;
        }
        String basePath = webConfig.getTemplateBasePath();
        if ("".equals(basePath)) {
            basePath = Globals.DEFAULT_TEMPLATE_PATH;
        }

        List allPage = new ArrayList();
        getAllPages(allPage, basePath);
        File templateDir = new File(basePath);
        if (!templateDir.exists())
            templateDir = new File(Globals.APP_BASE_DIR + basePath);
        // 按照module/pagename寻找:
        String name = module.getPath();
        name = name.substring(name.lastIndexOf("/") + 1);
        String target = module.getViews() + "/" + name + "/" + pageName;
        if (target.indexOf(".") < 0)
            target += "." + Globals.DEFAULT_TEMPLATE_EXT;
        File file = new File(templateDir, target);
        if (file.exists() && file.isFile()) {
            page = new Page();
            page.setName(pageName);
            page.setType(Globals.PAGE_TEMPLATE_TYPE);
            String url = target;
            page.setUrl(url);
            module.getPages().put(page.getName(), page);
        }
        return page;
    }

    private static void getAllPages(List allPage, String basePath) {
        String fileName = Globals.APP_BASE_DIR + basePath.substring(1);
        File file = new File(fileName);
        if (file != null && file.isDirectory()) {
            String[] pages = file.list();
            if (pages.length > 0) {
                for (int i = 0; i < pages.length; i++) {
                    allPage.add(pages[i]);
                }
            }
        }
    }

    /**
     * 得到输出Writer
     * 
     * @param response
     * @return 输出对象
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static Writer getResponseWriter(HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        Writer writer = null;
        try {
            writer = response.getWriter();
        } catch (IllegalStateException e) {
            String encoding = response.getCharacterEncoding();
            if (encoding == null) {
                encoding = DEFAULT_OUTPUT_ENCODING;
            }
            writer = new OutputStreamWriter(response.getOutputStream(), encoding);
        }
        return writer;
    }

    // 去掉<
    public static String eliminateScript(String value) {
        return value.replaceAll("<", "&lt;").replaceAll("%3c", "&lt;");
    }
}
