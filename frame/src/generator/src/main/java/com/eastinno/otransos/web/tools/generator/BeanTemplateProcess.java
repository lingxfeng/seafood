package com.eastinno.otransos.web.tools.generator;

import java.util.List;

import org.apache.velocity.context.Context;

public class BeanTemplateProcess implements TemplateProcess {
    private final static String defaultKeyField = "id";

    private final static String defaultKeyGenerator = "com.disco.dbo.NullIdGenerator";

    private String tableName;

    private String packageName = "com.disco.business";

    private String keyField;

    private String keyGenerator;

    public BeanTemplateProcess() {

    }

    public BeanTemplateProcess(String tableName) {
        this.tableName = tableName;
    }

    public void process(Context context) {
        // TODO Auto-generated method stub
        try {
            List list = GeneratorUtil.jdbcField2Java(tableName);
            context.put("package", packageName);
            context.put("fieldList", list);
            context.put("tableKeyFiled", keyField == null ? defaultKeyField : keyField);
            context.put("tabelIdGenerator", keyGenerator == null ? defaultKeyGenerator : keyGenerator);
        } catch (Exception e) {

        }
        context.put("tableName", tableName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public void setKeyGenerator(String keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
