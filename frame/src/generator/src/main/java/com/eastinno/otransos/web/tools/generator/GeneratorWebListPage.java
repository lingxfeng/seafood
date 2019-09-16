package com.eastinno.otransos.web.tools.generator;

import java.io.*;

public class GeneratorWebListPage extends AbstractGenerator {
    private String pageDir = "/webapps/WEB-INF/disco";
    private String listPageTemplateFile = "/page/listPage.html";

    protected void initGenerator() {
        // TODO Auto-generated method stub
        tg.setTemplateName(listPageTemplateFile);
        tg.setTargetDir(GeneratorUtil.getRealTemplaeDir(pageDir));
        tg.setTargetName(File.separator + tableName + "List.html");
        tg.setProcess(new PageTemplateProcess(tableName));
    }

    protected void parseArgs() {
        // TODO Auto-generated method stub

    }
}
