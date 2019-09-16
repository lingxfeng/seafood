package com.eastinno.otransos.generator;

public class CodeGenner {

    public static void main(String[] args) {
        Generator generator = Generator.getInstance();
        try {
            generator.setTemplateDir("../templates");
            generator.setPri("../");
            generator.doGenerator(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
