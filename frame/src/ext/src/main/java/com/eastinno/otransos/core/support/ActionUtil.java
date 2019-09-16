package com.eastinno.otransos.core.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.IWebAction;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.FrameworkEngine;

/**
 * @intro Action工具类
 * @verion 1.0
 * @author lengyu
 * @since 2007年5月15日 下午01:38:22
 */
public class ActionUtil {
    private static final Logger logger = Logger.getLogger(ActionUtil.class);

    public static WebForm getWebForm() {
        return getForm();
    }

    /**
     * 获取HttpServletRequest
     * 
     * @return
     */
    public static HttpServletRequest getReq() {
        return ActionContext.getContext().getRequest();
    }

    /**
     * 获取javaweb中HttpServletResponse
     * 
     * @return
     */
    public static HttpServletResponse getResp() {
        return ActionContext.getContext().getResponse();
    }

    /**
     * 获取url类型
     * 
     * @return
     */
    public static String getUrlType() {
        return ActionContext.getContext().getWebInvocationParam().getUrlType();
    }

    /**
     * 获取处理请求的action
     * 
     * @return
     */
    public static IWebAction getAction() {
        return ActionContext.getContext().getWebInvocationParam().getAction();
    }

    /**
     * 获取封装后的请求表单
     * 
     * @return
     */
    public static WebForm getForm() {
        return ActionContext.getContext().getWebInvocationParam().getForm();
    }

    public static Module getModule() {
        return ActionContext.getContext().getWebInvocationParam().getModule();
    }

    /**
     * 将“mulitId”字段的值封装成List
     * 
     * @param form
     * @return
     */
    public static List<Serializable> processIds(WebForm form) {
        return processIds(form, "");
    }

    /**
     * 将idsName中的值根据“，”封装成List
     * 
     * @param form
     * @param idsName 要封装的字段
     * @return
     */
    public static List<Serializable> processIds(WebForm form, String idsName) {
        if (logger.isDebugEnabled()) {
            logger.debug("processIds(WebForm) - start");
        }
        String key;
        if (idsName.equals("")) {
            key = "mulitId";
        } else {
            key = idsName;
        }
        String mulitId = CommUtil.null2String(form.get(key));
        if (mulitId.endsWith(","))
            mulitId = mulitId.substring(0, mulitId.length() - 1);
        String[] idsStr = mulitId.split(",");
        if (idsStr.length > 0) {
            List<Serializable> ids = new ArrayList<Serializable>(idsStr.length);
            for (String id : idsStr) {
                if (!"".equals(id))
                    ids.add(Long.parseLong(id));
            }
            return ids;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("processIds(WebForm) - end");
        }
        return null;
    }

    /**
     * 删除图片
     * 
     * @param path String类型的图片路径
     */
    public static void removeImage(String path) {
        if (path != null) {
            File bigFile = new File(Globals.APP_BASE_DIR + path);
            File smallFile = new File(Globals.APP_BASE_DIR + smallPath(path));
            if (bigFile.exists()) {
                bigFile.delete();
            }
            if (smallFile.exists()) {
                smallFile.delete();
            }
        }
    }

    /**
     * 将文件名添加后缀：_small
     * 
     * @param path
     * @return
     */
    private static String smallPath(String path) {
        int dotIndex = path.lastIndexOf('.');
        String preffix = path.substring(0, dotIndex);
        String suffix = path.substring(dotIndex);
        String name = preffix + "_small" + suffix;
        return name;
    }

    /**
     * 删除文件
     * 
     * @param path
     */
    public static void removeFile(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }

    }

    /**
     * 将value封装成下拉选项一个option
     * 
     * @param value 要封装的值
     * @return
     */
    public String generateOption(String value) {
        return "<option value=" + value + ">" + value + "</option>";
    }

    /**
     * 下载文件
     * 
     * @param f File对象：要下载的文件
     * @return
     */
    public static Page download(File f) {
        try {
            InputStream in = new FileInputStream(f);
            if (in != null) {
                HttpServletResponse resp = ActionContext.getContext().getResponse();
                resp.setContentType("APPLICATION/OCTET-STREAM");
                resp.setContentLength(in.available());
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + new String(f.getName().getBytes(), "iso8859-1") + "\"");
                byte[] buff = new byte[1000];
                OutputStream out = resp.getOutputStream();
                int c;
                while ((c = in.read(buff, 0, 1000)) > 0) {
                    out.write(buff, 0, c);
                    out.flush();
                }
                out.close();
                in.close();
            }
        } catch (Exception e) {
            logger.error("下载错误:" + e.getMessage());
        }
        return Page.nullPage;
    }

    /**
     * 转换列表数据
     * 
     * @param form 一个WebForm对象
     * @param clz -个Class对象
     * @return
     */
    public static List<?> parseMulitItems(WebForm form, Class<?> clz) {
        return parseMulitItems(form, clz, "");
    }

    /**
     * 把列表数据转换成表格
     * 
     * @param form
     * @param clz
     * @param prefix
     * @return
     */
    public static List<?> parseMulitItems(WebForm form, Class<?> clz, String prefix) {
        Field[] fs = clz.getDeclaredFields();
        String[] fields = new String[fs.length];
        for (int i = 0; i < fs.length; i++)
            fields[i] = fs[i].getName();
        return parseMulitItems(form, clz, fields, prefix);
    }

    /**
     * 用来把表单中的表格数据转换成对象数组
     * 
     * @param form form
     * @param clz 类名
     * @param fields 需要转换的字段
     * @param prefix 前缀
     * @return
     */
    public static List<Object> parseMulitItems(WebForm form, Class<?> clz, String[] fields, String prefix) {
        Map<String, String[]> datas = new HashMap<String, String[]>();
        for (int i = 0; i < fields.length; i++) {
            String[] objs = CommUtil.getStringArray(form.get(prefix + fields[i]));
            datas.put(fields[i], objs);
        }
        List<Object> list = new ArrayList<Object>();
        String[] objs = datas.get(fields[0]);
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                Map<String, String> map = new HashMap<String, String>();
                for (int j = 0; j < fields.length; j++) {
                    String[] obj = datas.get(fields[j]);
                    if (obj != null)
                        map.put(fields[j], obj[i]);
                }
                try {
                    Object obj = clz.newInstance();
                    FrameworkEngine.form2Obj(map, obj, false, true);
                    list.add(obj);
                } catch (Exception e) {
                    logger.error("数据转换出错" + e);
                }
            }
        }
        return list;
    }

    /**
     * 异步方式用来把表单中的表格数据转换成对象数组
     * 
     * @param form
     * @param clz
     * @param prefix
     * @return
     */
    public static List<Object> parseAjaxUploadMulitItems(WebForm form, Class<?> clz, String prefix) {
        Field[] fs = clz.getDeclaredFields();
        String[] fields = new String[fs.length];
        for (int i = 0; i < fs.length; ++i)
            fields[i] = fs[i].getName();
        return parseAjaxUploadMulitItems(form, clz, fields, prefix);
    }

    /**
     * 异步方式用来把表单中的表格数据转换成对象数组
     * 
     * @param form
     * @param clz
     * @param fields
     * @param prefix
     * @return
     */
    public static List<Object> parseAjaxUploadMulitItems(WebForm form, Class<?> clz, String[] fields, String prefix) {
        Map<String, String[]> datas = new HashMap<String, String[]>();
        for (int i = 0; i < fields.length; ++i) {
            Object obj = form.get(prefix + fields[i]);
            if (obj != null) {
                String[] objs = (String[]) null;
                if (obj instanceof String)
                    objs = obj.toString().split(",");
                else if (obj.getClass().isArray())
                    objs = (String[]) obj;
                datas.put(fields[i], objs);
            }
        }
        List<Object> list = new ArrayList<Object>();
        String[] objs = datas.get(fields[0]);
        if (objs != null) {
            for (int i = 0; i < objs.length; ++i) {
                Map<String, String> map = new HashMap<String, String>();
                for (int j = 0; j < fields.length; ++j) {
                    String[] obj = datas.get(fields[j]);
                    if ((obj != null) && (obj.length > i))
                        map.put(fields[j], obj[i]);
                }
                try {
                    Object obj = clz.newInstance();
                    FrameworkEngine.form2Obj(map, obj, false, true);
                    list.add(obj);
                } catch (Exception e) {
                    logger.error("数据转换出错");
                }
            }
        }
        return list;
    }
}
