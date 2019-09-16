package com.eastinno.otransos.web.tools.generator;

public abstract class AbstractGenerator implements Generator {

    protected String templateDir = "/webapps/WEB-INF/discotools";

    protected String tableName;

    protected String[] args;

    protected TemplateGenerator tg = new TemplateGenerator();

    public void generator(boolean append) throws RuntimeException {
        // TODO Auto-generated method stub
        this.parseArgs();// 第一步，首先解析参数
        this.initGenerator();// 第二步，设置特殊性参数
        tg.setTemplateDir(GeneratorUtil.getRealTemplaeDir(this.templateDir));// 设置模板路径
        tg.generator(false);// 执行生成
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTargetDir(String targetDir) {
        tg.setTargetDir(targetDir);
    }

    public void setTargetName(String targetName) {
        tg.setTargetName(targetName);
    }

    public void setTemplateDir(String templateDir) {
        tg.setTemplateDir(templateDir);
    }

    public void setTemplateName(String templateName) {

        tg.setTargetName(templateName);
    }

    public void setArgs(String[] args) {
        this.args = args.clone();
    }

    // 设置相关属性
    protected abstract void initGenerator();

    // 解析参数
    protected abstract void parseArgs();
}
