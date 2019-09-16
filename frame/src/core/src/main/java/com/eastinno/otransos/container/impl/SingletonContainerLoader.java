package com.eastinno.otransos.container.impl;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.web.config.BeanConfigReader;

/**
 * Create {@link Container} instance from singleton .
 * 
 * @author ksmwly@gmail.com
 */
public class SingletonContainerLoader {

    /** Container instances （singleton） */
    private static Map<String, Container> containers = new HashMap<String, Container>();

    /**
     * Obtain an instance of {@link Container} through the special configFile .
     * <p>
     * If the container is not initialized , then initial it .
     * 
     * @param configFile bean's configuration file
     * @return an instance of {@link Container}
     */
    public static Container getInstance(String configFile) {

        Container container = null;

        synchronized (SingletonContainerLoader.class) {

            container = containers.get(configFile);
            if (container == null) {
                // TODO 初始化容器的逻辑需要重新设计

                // 此处目的是获取Disco框架容器，如果有了初始化框架的Loader，便可以从Loader中获取容器

                // DefaultContainer container =
                // ContainerLoader.getInstance(configFile);
                Document doc = null;
                try {
                    doc = new SAXReader().read(SingletonContainerLoader.class.getResourceAsStream(configFile));
                } catch (DocumentException e) {
                    throw new IllegalArgumentException(configFile + "is not a valid config file !");
                }

                // 此处逻辑也很怪，可以考虑在DefaultContainer构造函数中把配置文件传进去，直接构造出一个可用的Container
                DefaultContainer defaultContainer = new DefaultContainer();
                defaultContainer.registerBeanDefinitions(BeanConfigReader.parseBeansFromDocument(doc));
                defaultContainer.refresh();

                container = defaultContainer;

                containers.put(configFile, container);
            }
        }

        return container;

    }

}
