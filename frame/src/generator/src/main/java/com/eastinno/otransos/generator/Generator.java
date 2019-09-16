package com.eastinno.otransos.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Generator {
    private static Generator instance;
    private final static Object keyForDiscoServer = new Object();

    public static Generator getInstance() {
        if (Generator.instance == null) {
            synchronized (Generator.keyForDiscoServer) {
                if (Generator.instance == null) {
                    Generator.instance = new Generator();
                }
            }
        }
        return Generator.instance;
    }

    private Generator() {
    }

    private String domainName;

    private boolean debug = false;

    private Class domainClz = null;

    private String templateDir;

    private String rootDomain;

    private String targetDir;

    private String pri = "../";

    private void init(String[] args) {
        String domainName = args[0];
        if (args != null) {
            java.io.File f = new java.io.File(domainName);
            if (f.exists()) {
                try {
                    Runtime.getRuntime().exec("cmd /c compile.bat " + f.getAbsolutePath());
                } catch (Exception e) {
                    System.out.println("编译:" + f.getAbsolutePath() + "出现错误！");
                }
                this.waitFor(domainName);
            }
        }
        if (debug) {
            this.setPri("");
        }
        try {
            Class clz = Class.forName(domainName);
            this.domainClz = clz;
            this.rootDomain = this.domainClz.getName().substring(0,
                    this.domainClz.getPackage().getName().lastIndexOf('.'));
            this.setTargetDir(this.getPri() + "src/main/java/" + this.rootDomain.replace('.', '/') + "/");
            this.setDomainName(clz.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            this.setTargetDir(this.getPri() + "src/main/java/disco/demo/domain/");
            this.setDomainName(domainName.substring(0, 1).toUpperCase() + domainName.substring(1));
            // 生成DOMAIN
            // this.genDomain(args);
            this.rootDomain = "disco.demo";
        }

    }

    public void doGenerator(String[] args) throws Exception {

        this.init(args);// 初始化参数，在需要的时候还会根据参数生成Domain

        // File f = new File(this.getTargetDir() + "domain/"
        // + this.getDomainName() + ".java");
        // String targetFile = f.getAbsolutePath().replace("bin\\..\\src",
        // "src");
        //
        // File idao = new File(this.getTargetDir() + "dao/I"
        // + this.getDomainName() + "DAO.java");
        // File imvc = new File(this.getTargetDir() + "mvc/"
        // +this.getDomainName() + "Action.java");
        // File is = new File(this.getTargetDir() + "service/I"
        // + this.getDomainName() + "Service.java");
        //
        // File isi = new File(this.getTargetDir() + "service/impl/"
        // + this.getDomainName() + "ServiceImpl.java");
        //

        // Runtime.getRuntime().exec("cmd /c compile.bat " + targetFile);
        // 参数１为ＤＯＭＡＩＮ名 ２为包名
        String arg1 = this.rootDomain + ".domain." + this.getDomainName();
        String arg2 = this.rootDomain;
        String[] arg = {arg1, arg2};
        this.waitFor(arg1);
        // System.out.println(arg);
        AllGenerator gen = new AllGenerator(arg);
        if (this.templateDir != null)
            gen.templateDir = this.templateDir;
        gen.setPri(this.pri);
        gen.gener();

    }

    /**
     * 基于Disco+Jpa+Spring的Crud应用生成 useage generator ActionName domainType argumentvalue 如generator Person domain
     * com.eastinno.otransos.demo.domain.Person
     * 
     * @param args
     */
    public static void main(String[] args) {
        Generator generator = Generator.getInstance();
        try {
            generator.setTemplateDir("../templates");
            generator.setTargetDir("../");
            generator.doGenerator(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runWlrServer(String domainName) {
        // Thread.sleep(1000);
        // System.out.println(idao.getAbsolutePath());
        // Runtime.getRuntime().exec(
        // "cmd /c compile.bat " + idao.getAbsolutePath());
        // this.waitFor(this.process(idao.getAbsolutePath()));
        // Runtime.getRuntime().exec(
        // "cmd /c compile.bat " + is.getAbsolutePath());
        // this.waitFor(this.process(is.getAbsolutePath()));
        // Runtime.getRuntime().exec(
        // "cmd /c compile.bat " + isi.getAbsolutePath());
        // this.waitFor(this.process(isi.getAbsolutePath()));
        // Runtime.getRuntime().exec(
        // "cmd /c compile.bat " + imvc.getAbsolutePath());
        //
        // System.out.println("编译完成");
        //
        // File mvc=new File(this.getPri() + "src/main/java/mvc-app.xml");
        // File dao = new File(this.getPri() + "src/main/java/dao.xml");
        // File service = new File(this.getPri() + "src/main/java/service.xml");
        // File application=new File(this.getPri() +
        // "src/main/java/application.xml");
        // File jpabase=new File(this.getPri() + "src/main/java/jpa-base.xml");
        // File log4j=new File(this.getPri() +
        // "src/main/java/log4j.properties");
        // File persistence=new File(this.getPri() +
        // "src/main/java/persistence.xml");
        //
        // String cls=this.getPri() + "src/main/webapp/WEB-INF/classes/";
        // String webinf=this.getPri() + "src/main/webapp/WEB-INF/";
        //
        // System.out.println("开始配置文件");
        // try{
        // this.copyFile(application, new File(cls+"application.xml"));
        // this.copyFile(jpabase, new File(cls+"jpa-base.xml"));
        // this.copyFile(log4j, new File(cls+"log4j.properties"));
        // this.copyFile(persistence, new File(cls+"persistence.xml"));
        // this.copyFile(dao, new File(cls+"dao.xml"));
        // this.copyFile(service, new File(cls+"service.xml"));
        // this.copyFile(mvc, new File(webinf+"mvc-app.xml"));
        // }
        // catch(Exception e)
        // {
        // System.out.println("拷贝配置文件出错!");
        // e.printStackTrace();
        // }
        /*
         * File targetDaoFile = new File( "../src/main/webapp/WEB-INF/classes/dao.xml"); File targetServiceFile = new
         * File( "../src/main/webapp/WEB-INF/classes/service.xml"); if (dao.exists() && service.exists()) {
         * generator.copyFile(dao, targetDaoFile); generator.copyFile(service, targetServiceFile); generator.copyFile(
         * new File("../src/main/java/mvc-app.xml"), new File( "../src/main/webapp/WEB-INF/mvc-app.xml")); }
         */
        System.out.println("配置文件添加完成，准备启动服务器");
        try {
            Thread.sleep(3000);
            Runtime.getRuntime().exec("cmd /c start db.bat");
            Runtime.getRuntime().exec("cmd /c start web.bat");

            Thread.sleep(13000);
            System.out.println("正在打开默认浏览器");

            Runtime.getRuntime().exec("cmd /c start explorer http://127.0.0.1:82/ejf/" + domainName);
            System.out.println("完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitFor(String className) {
        boolean haveClass = false;
        int t = 0;
        while (!haveClass && t < 5) {
            try {
                Class.forName(className);
                haveClass = true;
            } catch (java.lang.ClassNotFoundException e) {
                try {
                    Thread.sleep(1000);
                    t++;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // \. \\
    private String process(String s) {
        String s1 = this.rootDomain.replace('.', File.separator.charAt(0));
        String daoClass = s.substring(s.indexOf(s1));
        return daoClass.substring(0, daoClass.length() - 5).replaceAll("\\\\", ".");
    }

    private void copyFile(File source, File target) throws Exception {
        FileInputStream in = new FileInputStream(source);
        FileOutputStream out = new FileOutputStream(target);
        byte[] a = new byte[2000];
        int len = in.read(a);
        while (len != -1) {
            out.write(a, 0, len);
            len = in.read(a);
        }
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }
}
