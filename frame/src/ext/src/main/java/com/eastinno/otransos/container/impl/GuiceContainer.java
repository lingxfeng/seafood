package com.eastinno.otransos.container.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.container.InnerContainer;
import com.eastinno.otransos.web.exception.FrameworkException;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * @author ksmwly@gmail.com
 */
public class GuiceContainer implements InnerContainer {

    private static final Logger logger = Logger.getLogger(GuiceContainer.class);

    private String[] modules = new String[] {};

    protected List<Module> moduleObjs = new ArrayList<Module>();

    private String stage = Stage.PRODUCTION.name();

    private Injector injector;

    private Container parent;

    private static boolean initialized = false;

    private Map<String, Class> beanNames = new HashMap<String, Class>();

    public void setModuleObjs(List<Module> moduleObjs) {
        this.moduleObjs = moduleObjs;
    }

    public String getContainerInfo() {

        return "Guice Framework";
    }

    public Container getParent() {

        return parent;
    }

    public void init() {

        inject();
        logger.info("GuiceContainer is setup!");

    }

    public void setParent(Container container) {

        this.parent = container;
    }

    public boolean containsBean(String name) {

        return beanNames.containsKey(name);
    }

    public Object getBean(String name) {

        return getBean(beanNames.get(name));
    }

    @SuppressWarnings("unchecked")
    public Object getBean(Class type) {

        Object instance = null;

        if (type == null) {
            return null;
        }

        try {
            instance = injector.getInstance(type);
        } catch (Throwable e) {
            // logger.error(
            // "Error occured while retrive bean from GuiceContainer!", e);
            logger.error("GuiceContainer中不存在指定的bean：" + type);
        }

        return instance;
    }

    public List getBeans(Class type) {
        return null;
        // throw new FrameworkException(I18n
        // .getLocaleMessage("ext.no.implement.function"));
    }

    public Collection getBeansName() {

        return beanNames.keySet();
    }

    void inject() {
        // beforeInject();
        List<Module> configModules = new ArrayList<Module>();
        if (!initialized) {
            for (int i = 0; i < modules.length; i++) {
                try {
                    Object instance = ClassUtils.forName(modules[i], GuiceContainer.class.getClassLoader())
                            .newInstance();
                    if (instance instanceof AbstractModule) {
                        configModules.add((AbstractModule) instance);
                    }
                } catch (Throwable e) {
                    throw new FrameworkException("Error occured while install [" + modules[i] + "] module:", e);
                }
            }
            if (moduleObjs.size() > 0) {
                configModules.addAll(moduleObjs);
            }
            injector = Guice.createInjector(Stage.valueOf(stage), configModules);
            // injector.getInstance(com.wideplay.warp.persist.PersistenceService.class).start();
            initialized = true;
            afterInject();
        }
    }

    // ------- Container Managered ------------------

    /**
     * @param modules the modules to set
     */
    public void setModules(String[] modules) {

        this.modules = modules;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(String stage) {

        this.stage = stage;
    }

    protected Injector getInjector() {
        return this.injector;
    }

    protected void beforeInject() {

    }

    protected void afterInject() {

    }

}
