package com.eastinno.otransos.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.container.impl.WebContextContainer;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.http.util.HttpHelper;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.tools.AutoChangeLink;

public class HtmlGeneratorServiceImpl implements IHtmlGeneratorService {
    private String siteRoot;
    private String baseDir;
    private boolean local = true;
    private static final Log logger = LogFactory.getLog(HtmlGeneratorServiceImpl.class);

    public void begin() {
    }

    public void stop() {
    }

    public void process(AutoChangeLink obj) {
        if ((this.siteRoot == null) || (this.baseDir == null)) {
            setup();
        }
        if (TenantContext.getTenant() != null) {
            this.siteRoot = TenantContext.getTenant().getUrl();
        }
        if (this.siteRoot != null) {
            String url = obj.getDynamicUrl();
            url = url.replaceAll("\\\\", "\\");
            if ((url.startsWith("/")) || (url.startsWith("\\"))) {// 去左斜杠
                url = url.substring(1);
            }
            if (url.indexOf("http://") != 0) {
                url = this.siteRoot + ((this.siteRoot.endsWith("/")) || (url.charAt(0) == '/') ? "" : "/") + url;
            }
            url = url + (url.indexOf('?') < 0 ? "?" : "") + "&" + "showHtmlPage" + "=true";
            try {
                if (!obj.getDynamicUrl().equals(obj.getStaticPath())) {
                    urlSaveTo(url, this.baseDir + obj.getStaticPath());
                }
            } catch (Exception e) {
                logger.error("生存静态文件出错!" + e);
            }
        }
    }

    private void setup() {
        Container container = FrameworkEngine.getContainer();
        ServletContext servletContext = null;
        if ((container != null) && ((container instanceof WebContextContainer))) {
            servletContext = ((WebContextContainer) container).getServletContext();
        }
        if (servletContext == null) {
            logger.error("无法自动加载初始Html生成器的初始参数，生成静态html文件出错！");
        }
        if (this.siteRoot == null) {
            this.siteRoot = (this.local ? TenantContext.getTenant().getUrl() : "");
        }
        if (this.baseDir == null)
            this.baseDir = servletContext.getRealPath("/");
    }

    public void remove(AutoChangeLink obj) {
        String fileName = this.baseDir + obj.getStaticPath();
        File f = new File(fileName);
        File pf = f.getParentFile();
        if (f.exists())
            f.delete();
        if ((pf != null) && (pf.isDirectory()) && (pf.listFiles().length == 0))
            pf.delete();
    }

    public static void main(String[] args) {
        String s = "D:\\usr\\a.html\\";
        s = s.replaceAll("\\\\", "/");
        System.out.println(s);
        System.out.println(s.endsWith("/"));
    }

    public boolean urlSaveTo(String url, String localPath) throws Exception {
        boolean ret = false;
        OutputStreamWriter out = null;
        try {
            String html = HttpHelper.get(url).getContent();
            if (!StringUtils.hasText(html) || localPath.equals(Globals.APP_BASE_DIR)) {
                return ret;
            }
            File file = new File(localPath);
            if ((localPath.indexOf("index.shtml") == -1) && (localPath.endsWith("/") || localPath.endsWith("\\"))) {// 支持REST-URL风格
                file = new File(localPath + "/index.shtml");
            }
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            OutputStream o = new FileOutputStream(file);
            out = new OutputStreamWriter(o, "utf-8");
            if (!StringUtils.hasText(html)) {
                logger.debug("警告: 生成的HTML文件内容为空");
            }
            out.write(html);
            ret = true;
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return ret;
    }

    public void setSiteRoot(String siteRoot) {
        this.siteRoot = siteRoot;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public boolean isLocal() {
        return this.local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
