package com.eastinno.otransos.web.tools.generator;

import java.io.File;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.eastinno.otransos.core.util.I18n;

public class VelocityFactory {
    private static VelocityEngine ve;

    private VelocityFactory() {
    }

    private static VelocityEngine getVEngine(String templateDir) {
        if (ve == null)
            doInit(templateDir);

        ve.setProperty("file.resource.loader.path", templateDir);
        return ve;
    }

    private static void doInit(String templateDir) {
        Properties p = new Properties();
        ve = new VelocityEngine();
        File f = new File(templateDir);
        System.out.println(f.getAbsolutePath());
        if (!f.exists())
            System.out.println(I18n.getLocaleMessage("generator.Template.directory") + templateDir
                    + I18n.getLocaleMessage("generator.Does.not.exist"));
        p.setProperty("file.resource.loader.path", templateDir);
        try {
            ve.init(p);
        } catch (Exception e) {
            System.out.println(I18n.getLocaleMessage("generator.Initialization.error") + e);
        }
    }

    public static VelocityEngine getVelocityEngine(String templateDir) {

        Properties p = new Properties();
        VelocityEngine ve = new VelocityEngine();
        p.setProperty("file.resource.loader.path", templateDir);
        try {
            ve.init(p);
        } catch (Exception e) {
            System.out.println(I18n.getLocaleMessage("generator.Initialization.error") + e);
        }
        return ve;
    }

    public static Template getTemplate(String templateDir, String fileName) throws Exception {
        return getTemplate(templateDir, fileName, "UTF-8");
    }

    public static Template getTemplate(String templateDir, String fileName, String encoding) throws Exception {
        Template template = null;
        try {
            template = getVEngine(templateDir).getTemplate(fileName, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return template;
    }
}