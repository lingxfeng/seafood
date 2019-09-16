/*
 * EsayJF.com Inc.  
 * 
 * Copyright (c) 2006-2008 All Rights Reserved.
 */
package com.eastinno.otransos;

import com.eastinno.otransos.container.impl.DefaultContainer;

import junit.framework.TestCase;

/**
 * Base TestCase for Containers
 * 
 * @author ksmwly@gmail.com
 */
public class BaseTest extends TestCase {

    protected DefaultContainer container;

    private static final String Disco_FILE = "/conf/disco-web.xml";

    private static final String BLANK_SPAR = "    ";

    protected void setUp() throws Exception {
    }

    protected void registeCustomDefinitions(DefaultContainer container) {

    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {

        /*
         * super.tearDown(); container = null;
         */
    }

    /**
     * List all beans in Container , include beans in InnerContainer .
     */
    protected void showAllBeans() {

        /*
         * out.println("-------- All beans in Container ----------"); int index = 0;
         * out.println("Beans in Disco Container"); Collection beanNames = container.getBeansName(); for (Iterator
         * iter = beanNames.iterator(); iter.hasNext();) { index++; String name = (String) iter.next();
         * out.println(index + " : " + name); Object bean = container.getBean(name); if (bean instanceof InnerContainer)
         * { InnerContainer innerContainer = (InnerContainer) bean; Collection innerBeanNames =
         * innerContainer.getBeansName(); int innerIndex = 0; for (Iterator innerIter = innerBeanNames.iterator();
         * innerIter.hasNext();) { innerIndex++; out.println(BLANK_SPAR + innerIndex + " : " + (String)
         * innerIter.next()); } } } out.println("------------------------------------------");
         */
    }

    public void testNothing() {

    }
}
