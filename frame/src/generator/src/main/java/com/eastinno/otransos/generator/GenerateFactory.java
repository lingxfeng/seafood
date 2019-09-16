package com.eastinno.otransos.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.eastinno.otransos.core.util.I18n;

public class GenerateFactory {
    public static void main(String[] args) throws Exception {
        generSimple();
    }

    @SuppressWarnings("unused")
    private static void generSimple() throws IOException, FileNotFoundException {
        String path = "package.properties";
        Properties properties = new Properties();
        properties.load(GenerateFactory.class.getResourceAsStream(path));
        System.out.println(properties);

        List<String[]> argsList = new ArrayList<String[]>();
        for (Object element : properties.keySet()) {
            String domainName = (String) element;
            String packageName = properties.getProperty(domainName);
            System.out.println(I18n.getLocaleMessage("generator.package.name") + packageName);
            System.out.println("Domain：" + domainName);
            String[] pair = new String[] {domainName, packageName};
            argsList.add(pair);
        }
        for (String[] arg : argsList) {
            // System.out.println(arg);
            // 参数１为ＤＯＭＡＩＮ名 ２为包名
            // AllGenerator gen = new AllGenerator(arg);
            // gen.gener();
        }
    }

}
