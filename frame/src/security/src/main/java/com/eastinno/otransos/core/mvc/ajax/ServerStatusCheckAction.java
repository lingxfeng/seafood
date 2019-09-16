package com.eastinno.otransos.core.mvc.ajax;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebConfig;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class ServerStatusCheckAction extends AbstractPageCmdAction {
    public Page doInit(WebForm form, Module module) {
        WebConfig config = FrameworkEngine.getWebConfig();
        Iterator it = config.getModules().entrySet().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Map.Entry en = (Map.Entry) it.next();
            Module m = (Module) en.getValue();
            list.add(m);
        }
        Collections.sort(list, new Comparator<Module>() {
            public int compare(Module o1, Module o2) {
                return o1.getPath().compareTo(o2.getPath());
            }
        });
        String realPath = ActionContext.getContext().getRequest().getSession().getServletContext().getRealPath("/");
        realPath = realPath.substring(0, realPath.length() - 1);
        form.addResult("viewRoot", config.getTemplateBasePath());
        form.addResult("list", list);
        form.addResult("realPath", realPath);
        for (Iterator localIterator1 = list.iterator(); localIterator1.hasNext();) {
            Object obj = localIterator1.next();
            String viewPath = ((Module) obj).getViews();
            String path = ((Module) obj).getPath();
            String str1 = realPath + config.getTemplateBasePath() + "/" + viewPath + path;
        }

        return new Page("serverStatusCheck", "classpath:com/eastinno/otransos/core/views/serverStatusCheck.html");
    }

    private void createFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            if (!new File(file.getParent()).exists()) {
                createFile(file.getParent());
            }
            file.mkdir();
            System.out.println("创建了一个文件夹：" + fileName);
        }
    }
}
