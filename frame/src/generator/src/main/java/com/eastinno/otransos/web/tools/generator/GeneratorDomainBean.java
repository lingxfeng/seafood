package com.eastinno.otransos.web.tools.generator;

import com.eastinno.otransos.core.util.JdkVersion;

public class GeneratorDomainBean extends AbstractGenerator {
    private String beanDir = "/src/main";

    private String domainBeanPackage = "com.disco.business";

    private String beanTemplateFile = "/java/bean.java";
    private String beanJdk5TemplateFile = "/java/bean-jdk5.java";

    protected void initGenerator() {
        // TODO Auto-generated method stub
        if (JdkVersion.getJavaVersion() >= JdkVersion.JDK_1_5)
            tg.setTemplateName(beanJdk5TemplateFile);
        else
            tg.setTemplateName(beanTemplateFile);
        tg.setTargetDir(GeneratorUtil.getRealTemplaeDir(beanDir));
        tg.setTargetName("/" + domainBeanPackage.replaceAll("\\.", "/") + "/" + tableName + ".java");
        BeanTemplateProcess process = new BeanTemplateProcess(tableName);
        process.setPackageName(domainBeanPackage);
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
                        this.domainBeanPackage = value;
                }
            }
        }
    }

    public void setBeanDir(String beanDir) {
        this.beanDir = beanDir;
    }

    public void setBeanTemplateFile(String beanTemplateFile) {
        this.beanTemplateFile = beanTemplateFile;
    }

    public void setDomainBeanPackage(String domainBeanPackage) {
        this.domainBeanPackage = domainBeanPackage;
    }

    public void setBeanJdk5TemplateFile(String beanJdk5TemplateFile) {
        this.beanJdk5TemplateFile = beanJdk5TemplateFile;
    }

}
