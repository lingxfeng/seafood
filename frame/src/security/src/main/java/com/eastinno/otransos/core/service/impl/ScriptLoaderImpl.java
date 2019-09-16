package com.eastinno.otransos.core.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.service.IScriptLoader;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class ScriptLoaderImpl implements IScriptLoader {
    private static Logger logger = LoggerFactory.getLogger(ScriptLoaderImpl.class);

    private String clientId;// 客户端ID
    private String serial;// 系列号
    private List<String> servers = new ArrayList<String>();
    private String defaultServer = "http://script.dgdy.cc";// 默认远程服务器
    private boolean offline = false;// 是否离线版
    private boolean debug = false;// 是否调试模式 调试模式下每次加载都更新缓存，反之则优先读取缓存
    private boolean compress = true;// 是否压缩
    private boolean encrypt = false;// 是否加密
    private boolean expired = false;// 软件许可是否已到期
    private String encoding = "UTF-8";// 远程加载时数据字符集
    private Map<String, Object> caches = new HashMap<String, Object>();
    private String error = "Ext.Msg.alert('友情提醒','您所使用的应用服务许可配置有误,<br />请联系管理员.');";

    // private String error = "Ext.Msg.alert('友情提醒','当前应用非法加载或应用APP配置有误,<br />请联系管理员;";

    public String loadApp(String appName) {
        if (this.expired)
            return error;
        if (this.debug) {
            caches.clear();// 在debug模式下,每次都更新缓存
        }
        this.getEncoding();
        // 缓存中有则直接从缓存中加载
        if (caches.containsKey(appName)) {
            byte[] bs = (byte[]) caches.get(appName);
            if (bs != null) {
                try {
                    return new String(bs, encoding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            String s = loadFromLocal(appName);// 优先尝试从本地加载
            if (s == null) {
                s = this.loadFromClassPath(appName);// 从JAR包中加载
            }
            if (s == null && !this.offline)
                s = loadFromRemote(appName);// 从远程加载
            if (s != null) {
                // 加密
                if (this.encrypt) {
                    s = this.doEncrypt(s);
                }
                // GZIP压缩 debug模式下则不压缩
                if (compress && !debug) {
                    try {
                        Writer out = new StringWriter();
                        Reader in = new StringReader(s);
                        JavaScriptCompressor jsc = new JavaScriptCompressor(in, null);
                        jsc.compress(out, -1, true, false, false, false);
                        in.close();
                        out.close();
                        s = out.toString();
                    } catch (Exception e) {
                        logger.error("javascript compress error:", e);
                    }
                }
                try {
                    caches.put(appName, s.getBytes(this.encoding));// 加入缓存
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }
        }
        return error;
    }

    public boolean isOffline() {
        return this.offline;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    /**
     * 从classPath路径下加载指定文件
     * 
     * @param appName
     * @return
     */
    private String loadFromClassPath(String appName) {
        appName = appName.replaceAll("\\.\\.", "");
        String script = "/com/eastinno/otransos/views/extApp/" + appName;
        script = script.replaceAll("//", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream in1 = this.getClass().getResourceAsStream(script);// classLoader.getResourceAsStream(srcript);
            if (in1 == null)
                in1 = classLoader.getResourceAsStream(script);
            if (in1 == null)
                return null;
            Writer out = new StringWriter();
            InputStreamReader in = new InputStreamReader(in1, encoding);
            int c = in.read();
            while (c != -1) {
                out.write(c);
                c = in.read();
            }
            in.close();
            in1.close();
            out.flush();
            out.close();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadFromLocal(String appName) {
        // Bug，禁止使用..来往上级目录跳转
        // extApp.java?cmd=loadScript&script=../../web.xml
        appName = appName.replaceAll("\\.\\.", "");
        String path = Globals.APP_BASE_DIR + "/WEB-INF/views/extApp/" + appName;
        Tenant t = ShiroUtils.getTenant();
        // path = (t != null) ? path.replace("{0}", t.getCode() + "/") : path.replace("{0}", "");
        File file = new File(path);
        if (file.exists()) {
            try {
                Writer out = new StringWriter();
                InputStreamReader in = new InputStreamReader(new FileInputStream(path), encoding);
                int c = in.read();
                while (c != -1) {
                    out.write(c);
                    c = in.read();
                }
                in.close();
                out.flush();
                out.close();
                return out.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 从远程服务器上加载
     * 
     * @param appName
     * @return
     */
    private String loadFromRemote(String appName) {
        if (this.servers.isEmpty()) {
            this.servers.add(defaultServer);
        }
        for (int i = 0; i < this.servers.size(); i++) {
            String s = this.loadFromRemoteBySimple(appName, this.servers.get(i));
            // 如果从当前远程servers中加载到脚本文件则直接返回
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    /**
     * 不通过验证授权许可的方式直接加载远程脚本
     * 
     * @param appName 应用脚本的名称
     * @param server 远程服务器
     * @return
     */
    private String loadFromRemoteBySimple(String appName, String server) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(server + "/extApp/" + appName).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-javascript");
            conn.connect();
            if (!(200 == conn.getResponseCode())) {
                return null;
            }
            Writer out = new StringWriter();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(), encoding);
            int c = in.read();
            while (c != -1) {
                out.write(c);
                c = in.read();
            }
            in.close();
            out.flush();
            out.close();
            conn.disconnect();// 断开连接
            String result = out.toString();// 返回内容
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过验证授权许可的方式加载远程脚本 必须有授权许可才能加载
     * 
     * @param appName
     * @param server
     * @return
     */
    private String loadFromRemoteByAuth(String appName, String server) {
        try {
            String queryString = "clientId=" + this.clientId + "&serial=" + this.serial + "&script=" + appName;
            HttpURLConnection conn = (HttpURLConnection) new URL(server + "/extApp.java?cmd=loadScript").openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            conn.getOutputStream().write(queryString.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            StringBuffer outbuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                outbuffer.append(line);
            }
            reader.close();
            conn.disconnect();
            String result = outbuffer.toString();// 返回内容
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密指定参数字符串
     * 
     * @param s
     * @return
     */
    private String doEncrypt(String s) {
        return s;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public String getDefaultServer() {
        return defaultServer;
    }

    public void setDefaultServer(String defaultServer) {
        this.defaultServer = defaultServer;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean isExpired() {
        return expired;
    }

    /**
     * 设置编码
     * 
     * @return
     */
    public String getEncoding() {
        HttpServletRequest req = ActionContext.getContext().getRequest();
        if (req != null) {
            String str = req.getCharacterEncoding();
            if (StringUtils.hasText(str)) {
                this.setEncoding(str);
            }
        }
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setCaches(Map<String, Object> caches) {
        this.caches = caches;
    }

    public Map<String, Object> getCaches() {
        return caches;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
