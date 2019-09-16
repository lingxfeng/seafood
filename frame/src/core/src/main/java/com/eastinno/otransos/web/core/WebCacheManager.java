package com.eastinno.otransos.web.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.container.annonation.WebCache;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.IRequestCallback;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.WebInvocationParam;

/**
 * 用于管理页面缓存信息，包括url地址对应信息等。
 * 
 * @author lengyu
 */
public class WebCacheManager {
    private final static WebCacheManager singleton = new WebCacheManager();

    public static WebCacheManager getInstance() {
        return singleton;
    }

    public final List urls = new ArrayList();

    public Page handleCache(WebInvocationParam webParam, WebCache cache) {
        Page ret = null;
        String cacheUrl = getUrl(webParam, cache);
        File f = new File(Globals.APP_BASE_DIR + cacheUrl);
        // System.out.println("执行cache:"+f.getAbsolutePath());
        Object refreshWebCache = webParam.getForm().get("refreshWebCache");
        if (!urls.contains(cacheUrl)) {
            if (f.exists() && (refreshWebCache == null) && (System.currentTimeMillis() - f.lastModified() < 1000 * cache.timeout())) {
                // System.out.println("从cache返回数据");
                ret = new Page("cache", cacheUrl, "forward");
            } else {
                try {
                    if (!f.getParentFile().exists())
                        f.getParentFile().mkdirs();
                    java.io.Writer writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
                    ActionContext.getContext().setCustomWriter(writer);
                    ActionContext.getContext().setUri(cacheUrl);
                    urls.add(cacheUrl);
                    // System.out.println("准备生成缓丰:"+urls.size());
                    ActionContext.getContext().setRequestCallback(new CacheFinishCallback(cacheUrl));
                    // Thread.sleep(5000l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ret=new Page("action",cache)
            }
        }
        return ret;
    }

    public String getUrl(WebInvocationParam webParam, WebCache cache) {
        Module module = webParam.getModule();
        WebForm form = webParam.getForm();
        String command = CommUtil.null2String(form.get("cmd"));
        if ("".equals(command))
            command = "index";
        String[] params = cache.params();
        String fileName = command;
        for (int i = 0; i < params.length; i++) {
            if (!"CMD".equals(params[i])) {
                String v = CommUtil.null2String(form.get(params[i]));
                fileName += "-" + params[i] + "-" + v;
            }
        }
        String cacheUrl = "/html/cache/" + module.getPath() + "/" + fileName + ".html";
        return cacheUrl;
    }

    public class CacheFinishCallback implements IRequestCallback {
        private String url;

        CacheFinishCallback(String url) {
            this.url = url;
        }

        public void doFinish() {
            if (url != null)
                urls.remove(url);
            System.out.println(I18n.getLocaleMessage("core.web.generate.complete.cache") + urls.size());
        }
    }
}
