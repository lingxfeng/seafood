package com.eastinno.otransos.web.tools.generator;

public class GeneratorWebAction extends AbstractGenerator {
    private String beanDir = "/src/main";
    private String actionPackage = "com.disco.action";
    private String actionTemplateFile = "/java/crudAction.java";
    private String beanPackage;

    protected void initGenerator() {
        // TODO Auto-generated method stub
        tg.setTemplateName(actionTemplateFile);
        tg.setTargetDir(GeneratorUtil.getRealTemplaeDir(beanDir));
        tg.setTargetName("/" + actionPackage.replaceAll("\\.", "/") + "/" + tableName + "Action.java");
        CrudActionTemplateProcess process = new CrudActionTemplateProcess(tableName);
        process.setPackageName(this.actionPackage);
        if (beanPackage != null)
            process.setBeanPackage(this.beanPackage);
        tg.setProcess(process);
    }

    protected void parseArgs() {
        if (this.args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                int endPrefix = args[i].indexOf('=');
                if (endPrefix > 0) {
                    String prefix = GeneratorUtil.getArgKey(args[i]);
                    String value = GeneratorUtil.getArgValue(args[i]);
                    if ("package".equalsIgnoreCase(prefix) && value != null)
                        this.actionPackage = value;
                    else if ("beanPackage".equalsIgnoreCase(prefix) && value != null)
                        this.beanPackage = value;
                }
            }
        }
    }

    public void setActionPackage(String actionPackage) {
        this.actionPackage = actionPackage;
    }

    public void setActionTemplateFile(String actionTemplateFile) {
        this.actionTemplateFile = actionTemplateFile;
    }

    public void setBeanDir(String beanDir) {
        this.beanDir = beanDir;
    }
}
