package com.eastinno.otransos.web.tools.generator;

public class GeneratorWebEditPage extends AbstractGenerator {
    private String pageDir = "/webapps/WEB-INF/disco";
    private String editPageTemplateFile = "/page/editPage.html";

    protected void initGenerator() {
        // TODO Auto-generated method stub
        tg.setTemplateName(this.editPageTemplateFile);
        tg.setTargetDir(GeneratorUtil.getRealTemplaeDir(this.pageDir));
        tg.setTemplateName(this.editPageTemplateFile);
        tg.setTargetName("/" + this.tableName + "Edit.html");
        tg.setProcess(new PageTemplateProcess(this.tableName));
    }

    protected void parseArgs() {
        // TODO Auto-generated method stub

    }
}
