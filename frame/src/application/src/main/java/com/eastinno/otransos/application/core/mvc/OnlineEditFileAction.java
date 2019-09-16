package com.eastinno.otransos.application.core.mvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * @intro 在线编辑文本文件源代码
 * @version v0.1
 * @author maowei
 * @since 2014年5月9日 下午12:56:36
 */
@Action
public class OnlineEditFileAction extends AbstractPageCmdAction {
    @InjectDisable
    private String disablePath = "classes;lib;";// 禁访目录
    @InjectDisable
    private static String parentPath = "";// 默认读取的根目录
    @InjectDisable
    private static String disableFile = ".svn;lib;classes;.swf;.gif;.jpg;.png;.db;";// 过滤的文件后缀

    @Deprecated
    @InjectDisable
    private static String filterFile = ".txt;.html;.htm;.xhtml;;.shtml;.bat;.xml;.properties;";// 允许访问的文件后缀(暂时未实现)

    /**
     * 得到文件目录结构树
     * 
     * @param form
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Page doGetContents(WebForm form) {
        String pack = CommUtil.null2String(form.get("id"));
        List<Map<String, Object>> nodes = new java.util.ArrayList<Map<String, Object>>();
        if (!"".equals(pack)) {
            parentPath = pack;
        } else {
            parentPath = Globals.APP_BASE_DIR;
        }
        File filePath = new File(parentPath.toString());
        File[] files = filePath.listFiles();
        int ii = files != null ? files.length : 0;
        for (int i = 0; i < ii; i++) {
            if (disablePath != null) {
                int n = 0;
                String wc = CommUtil.null2String(disablePath);
                String[] str = (wc + disableFile).toLowerCase().split(";");
                for (int m = 0; m < str.length; m++) {
                    // 如果文件名与受保护文件不一致并且文件名后缀与受保护的文件名后缀不一样
                    boolean f1 = !files[i].getName().toLowerCase().equals(str[m]);
                    boolean f2 = !files[i].getName().toLowerCase().endsWith(str[m]);
                    if (f1 && f2) {
                        // 如果没有找到，则说明这个文件不属于受保存对象之一
                        ++n;
                    }
                    // 如果在受保护对象列表中找不到这个文件
                    if (n == str.length) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", files[i].getPath());
                        map.put("text", files[i].getName());
                        map.put("qtip", files[i].getName());
                        map.put("icon", "img/item/tage.gif");
                        if (files[i].isDirectory()) {
                            map.put("leaf", false);
                        } else {
                            map.put("leaf", true);
                        }
                        nodes.add(map);
                    }
                }
            }
        }
        // 进行排序（把文件夹排在前面）
        Collections.sort(nodes, new Comparator<Map>() {
            public int compare(Map arg0, Map arg1) {
                return arg0.get("leaf").toString().compareTo(arg1.get("leaf").toString());
            }
        });
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    /**
     * 保存源代码
     * 
     * @param form
     * @return
     * @throws IOException
     */
    public Page doSaveSource(WebForm form) throws IOException {
        String filePath = CommUtil.null2String(form.get("filePath"));
        String fileSource = CommUtil.null2String(form.get("fileSource"));
        File file = new File(filePath);
        if (file.exists()) {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "utf-8");
            out.write(fileSource);
            out.flush();
            out.close();
            fos.close();
            fos.flush();
        }
        return pageForExtForm(form);
    }

    /**
     * 得到指定文件的文件源代码
     * 
     * @param form
     * @return
     * @throws IOException
     */
    public Page doGetFileDetails(WebForm form) throws IOException {
        String pack = CommUtil.null2String(form.get("pack"));
        StringBuffer sb = new StringBuffer();
        File content = new File(pack);
        if (!"".equals(pack) && content.exists()) {
            BufferedReader brd = new BufferedReader(new InputStreamReader(new FileInputStream(content), "utf-8"));
            while (brd.ready()) {
                sb.append(brd.readLine()).append("\n");
            }
            brd.close();
        }
        systemFile sf = new systemFile();
        sf.setFilePath(pack);
        sf.setFileSource(sb.toString());
        form.jsonResult(sf);
        return pageForExtForm(form);
    }

    /**
     * @author <a href="mailto:ksmwly@163.com">LengYu</a>
     * @Creation date: 2009-9-25 下午10:36:43 文件源代码
     */
    private class systemFile {
        private String filePath = "";// 文件物理路径
        private String fileSource = "";// 文件源代码

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileSource() {
            return fileSource;
        }

        public void setFileSource(String fileSource) {
            this.fileSource = fileSource;
        }
    }

}
