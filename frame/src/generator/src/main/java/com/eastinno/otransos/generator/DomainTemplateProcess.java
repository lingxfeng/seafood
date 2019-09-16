package com.eastinno.otransos.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.context.Context;

import com.eastinno.otransos.web.tools.generator.TemplateProcess;

public class DomainTemplateProcess implements TemplateProcess {

    private String attributes;

    private String domainName;

    public DomainTemplateProcess(String domainName, String attributes) {
        this.setDomainName(domainName);
        this.setAttributes(attributes);
    }

    public void process(Context context) throws Exception {
        String[] parms = this.getAttributes().split("#");
        List<Map> fields = new ArrayList<Map>();
        for (int i = 0; i < parms.length; i++) {
            String[] field = parms[i].split(":");
            String Name = field[0].substring(0, 1).toUpperCase() + field[0].substring(1);
            String Type = field[2].substring(0, 1).toUpperCase() + field[2].substring(1);
            if ("Email".equals(Type)) {
                Type = "String";
            }
            Map map = new HashMap();
            map.put("name", field[0]);
            map.put("Name", Name);
            map.put("alias", field[1]);
            map.put("type", field[2].toLowerCase());
            map.put("Type", Type);
            map.put("length", field[3]);
            if (field.length >= 5)
                map.put("validatorName", field[4]);
            fields.add(map);
        }
        Object svuid = Math.random() < 0.1 ? "-" : "" + Math.round(Math.pow(10, 17) * Math.random()) + "L";
        context.put("svuid", svuid);
        context.put("nowTime", new java.util.Date().toString());
        context.put("domainName", this.getDomainName());
        context.put("fields", fields);
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

}
