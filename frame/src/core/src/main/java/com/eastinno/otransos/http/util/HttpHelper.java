package com.eastinno.otransos.http.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;

/**
 * HTTP工具类，封装HttpClient4.3.x来对外提供简化的HTTP请求
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年9月27日 下午2:59:28
 * @Intro
 */
public class HttpHelper {
    private static Integer socketTimeout = 5000;// 定义Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间,此处设置为3秒。
    private static Integer connectTimeout = 6000;// 定义通过网络与服务器建立连接的超时时间(6秒)
    private static Integer connectionRequestTimeout = 1000;

    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     * 
     * @param url 完整的URL地址
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     */
    public static ResponseContent get(String url) {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        ResponseContent response = null;
        try {
            response = hw.getResponse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     * 
     * @param url 完整的URL地址
     * @param headers 设置请求头 头名称必须符合org.apache.http.HttpHeaders规范
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     */
    public static ResponseContent get(String url, Map<String, String> headers) {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        ResponseContent response = null;
        try {
            response = hw.getResponse(url, headers);
        } catch (Exception e) {
            System.out.println(url);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     * 
     * @param url 完整的URL地址
     * @param urlEncoding 编码，可以为null
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     */
    public static ResponseContent get(String url, String urlEncoding) {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        ResponseContent response = null;
        try {
            response = hw.getResponse(url, urlEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 将参数拼装在url中，进行post请求。
     * 
     * @param url
     * @return
     */
    public static ResponseContent post(String url) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            setParams(url, hw);
            ret = hw.postNV(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static void setParams(String url, HttpClientWrapper hw) {
        String[] paramStr = url.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
            return;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
            return;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            hw.addNV(keyValue[0], keyValue[1]);
        }
    }

    /**
     * 上传文件（包括图片）
     * 
     * @param url 请求URL
     * @param paramsMap 参数和值
     * @return
     */
    public static ResponseContent post(String url, Map<String, Object> paramsMap) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            setParams(url, hw);
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                    FileBody fileBody = new FileBody((File) value);
                    hw.getContentBodies().add(fileBody);
                } else if (value instanceof byte[]) {
                    byte[] byteVlue = (byte[]) value;
                    ByteArrayBody byteArrayBody = new ByteArrayBody(byteVlue, key);
                    hw.getContentBodies().add(byteArrayBody);
                } else {
                    if (value != null && !"".equals(value)) {
                        hw.addNV(key, String.valueOf(value));
                    } else {
                        hw.addNV(key, "");
                    }
                }
            }
            ret = hw.postEntity(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 使用post方式，发布对象转成的json给Rest服务。
     * 
     * @param url
     * @param jsonBody
     * @return
     */
    public static ResponseContent postJsonEntity(String url, String jsonBody) {
        return post(url, jsonBody, "application/json");
    }

    /**
     * 使用post方式，发布对象转成的xml给Rest服务
     * 
     * @param url URL地址
     * @param xmlBody xml文本字符串
     * @return ResponseContent 如果发生异常则返回空，否则返回ResponseContent对象
     */
    public static ResponseContent postXmlEntity(String url, String xmlBody) {
        return post(url, xmlBody, "application/xml");
    }

    private static ResponseContent post(String url, String body, String contentType) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            hw.addNV("body", body);
            ret = hw.postNV(url, contentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void main(String[] args) {
        testGet();
        // testUploadFile();
    }

    // test
    public static void testGet() {
        String url = "http://www.sina.com";
        ResponseContent responseContent = get(url);
        try {
            System.out.println(responseContent.getEncoding());
            System.out.println(responseContent.getContentBytes());
            System.out.println(responseContent.getStatusCode());
            System.out.println(responseContent.getContentType());
            System.out.println(responseContent.getContentTypeString());
            System.out.println(responseContent.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // test
    public static void testUploadFile() {
        try {
            String url = "http://localhost:8280/jfly/action/admin/user/upload.do";
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("userName", "jj");
            paramsMap.put("password", "jj");
            paramsMap.put("filePath", new File("C:\\Users\\yangjian1004\\Pictures\\default (1).jpeg"));
            ResponseContent ret = post(url, paramsMap);
            System.out.println(ret.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}