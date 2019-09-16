package com.eastinno.otransos.container;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.eastinno.otransos.core.service.IDynamicStaticPairService;

@ContextConfiguration(locations = {"classpath:application.xml"})
public class SpringContainerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private IDynamicStaticPairService dynamicStaticPairService;

    @Test
    public void testLoad() {
        System.out.println(dynamicStaticPairService);
    }
}
