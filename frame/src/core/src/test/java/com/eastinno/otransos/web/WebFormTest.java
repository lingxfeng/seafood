package com.eastinno.otransos.web;

import java.util.HashMap;
import java.util.Map;

import com.eastinno.otransos.web.WebForm;

import junit.framework.TestCase;

public class WebFormTest extends TestCase {
    public void testToPO() {
        com.eastinno.otransos.web.core.FrameworkEngine.setContainer(new MockContainer());
        WebForm form = new WebForm();
        Map map = new HashMap();
        map.put("id", 15l);
        map.put("userName", "朱峰");
        map.put("amount", "15");
        map.put("dw", "元");
        map.put("amount1", "50");
        map.put("dw1", "美元");
        map.put("poId", "2");
        form.getTextElement().putAll(map);
        O o = new O();
        form.toPo(o);
        System.out.println(o);
    }
}
