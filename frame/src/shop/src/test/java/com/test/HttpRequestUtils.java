package com.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtils {

    // 定义一个enum枚举类型，包括两个实例SELECT SAVE

    public static String doPost(String url, Map<String, String> params, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] param = new NameValuePair[params.size()];
        Set<String> key = params.keySet();
        int i = 0;
        for (Iterator it = key.iterator(); it.hasNext();) {
            String keyName = (String) it.next();
            param[i] = new NameValuePair(keyName, params.get(keyName));
            i++;
        }
        method.setRequestBody(param);
        method.releaseConnection();
        // 设置Http Post数据

        HttpMethodParams p = new HttpMethodParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            p.setParameter(entry.getKey(), entry.getValue());
        }
        method.setParams(p);

        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty)
                        response.append(line).append(System.getProperty("line.separator"));
                    else
                        response.append(line);
                }
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            method.releaseConnection();
        }
        return response.toString();
    }


    /**
     * 
     * @param url
     * @param params
     * @param charset
     * @param pretty
     * @param fileParams
     * @return
     */
    public static String doPost(String url, Map<String, String> params, String charset, boolean pretty,
            Map<String, File> fileParams) {
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder multiBuilder = MultipartEntityBuilder.create();
        try {
            multiBuilder.setCharset(CharsetUtils.get(charset));
        }
        catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpEntity entity = null;
        if (fileParams != null && fileParams.size() > 0) {
            for (String filekey : fileParams.keySet()) {
                File f = fileParams.get(filekey);
                multiBuilder.addBinaryBody(filekey, f);
            }

        }
        if (params != null && params.size() > 0)
            for (String key : params.keySet()) {
                String value = params.get(key);
                multiBuilder.addTextBody(key, value);
            }
        entity = multiBuilder.build();
        post.setEntity(entity);
        HttpResponse response = null;

        // post.setConfig(config)
        try {
            response = client.execute(post);
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String returnStr = null;

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                // BufferedReader reader = new BufferedReader(new
                // InputStreamReader());
                // OutputStream outstream = new OutputStreamWriter(reader);
                // multiBuilder.build().writeTo(outstream);
                returnStr = EntityUtils.toString(entity);

            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnStr;
    }


    /**
     * 
     * @param urlStr
     *            get 请求的地址
     * @return
     */
    public static String getResult(String urlStr) {
        String responseStr = "";
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(urlStr);
        try {
            client.executeMethod(get);
            responseStr = get.getResponseBodyAsString();
        }
        catch (HttpException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return responseStr;

    }


    /**
     * @param urlStr
     *            请求的地址
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    public static String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(200000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(200000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            // httpUrlConnection.setRequestProperty("Content-type",
            // "application/x-java-serialized-object");
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

}
