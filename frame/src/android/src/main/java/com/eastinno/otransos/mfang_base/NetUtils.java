package com.eastinno.otransos.mfang_base;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NetUtils {
    private final static int DEFAULT_TIMEOUT = 20000;
    // 上次请求的地址
    public static String lastURL = null;
    public static RequestParams lastParams = null;

    /**
     * post方式请求
     * 
     * @param url 地址
     * @param params 键值对
     * @return
     */
    public static void post(String url, List<BasicNameValuePair> content, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams(content);
        post(url, params, handler);
    }

    // 使用async——http做get请求
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.get(url, params, responseHandler);
    }

    // 使用async——http做get请求
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(url, params, responseHandler);
    }

    public static void postWithSession(Context context, String url, RequestParams params, String sessionValue,
            AsyncHttpResponseHandler responseHandler) {
        lastURL = url;
        lastParams = params;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("Cookie", "JSESSIONID=" + sessionValue);
        client.post(url, params, responseHandler);
    }

}
