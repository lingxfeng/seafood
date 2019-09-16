package com.eastinno.otransos.web.tools.generator;

import org.apache.velocity.context.Context;

public class CrudActionTemplateProcess implements TemplateProcess {
    private String tableName;
    private String packageName = "com.disco.action";
    private String beanPackage = "com.disco.business";

    public CrudActionTemplateProcess() {

    }

    public CrudActionTemplateProcess(String tableName) {
        this.tableName = tableName;
    }

    public void process(Context context) {
        // TODO Auto-generated method stub
        context.put("tableName", this.tableName);
        context.put("package", packageName);
        context.put("beanPackage", beanPackage);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBeanPackage() {
        return beanPackage;
    }

    public void setBeanPackage(String beanPackage) {
        this.beanPackage = beanPackage;
    }

}
