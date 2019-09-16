package com.eastinno.otransos.core.support;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Disco平台Junit测试基类
 * <p>
 * 带事务的单元测试,默认是事务回滚,如需事务不回滚则在测试的方法上加@Rollback(false)
 * </p>
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2010年11月27日 下午3:58:14
 * @Intro
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public abstract class DiscoFrameworkAbstractTest extends AbstractTransactionalJUnit4SpringContextTests {
    // @Test
    // public void addTest() {
    // System.out.println(newsDocService + "**********************");
    // System.out.println(newsDirService + "**********************");
    // }
}
