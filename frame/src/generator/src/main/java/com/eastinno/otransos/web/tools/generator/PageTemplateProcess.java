package com.eastinno.otransos.web.tools.generator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.context.Context;

public class PageTemplateProcess implements TemplateProcess {
    private String tableName;

    PageTemplateProcess() {

    }

    PageTemplateProcess(String tableName) {
        this.tableName = tableName;
    }

    public void process(Context context) throws Exception {
        Connection conn = null;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableName);
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        List list = new ArrayList(count);
        for (int i = 1; i <= count; i++) {
            Map map = new HashMap();
            map.put("name", meta.getColumnName(i));
            map.put("type", meta.getColumnTypeName(i));
            map.put("lable", meta.getColumnLabel(i));
            map.put("size", new Integer(meta.getColumnDisplaySize(i)));
            list.add(map);
        }
        rs.close();
        stmt.close();
        context.put("fieldList", list);
        System.out.println("域模型的值：" + context.get("domain"));
        context.put("tableName", tableName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
