package com.eastinno.otransos.payment.union.app.util;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
/**
 * 
 * @author Administrator
 *
 */
public class HttpUtil {

	public static String encoding="utf-8";

    private static final HttpConnectionManager connectionManager;
    
    private static final HttpClient client;

    static {

    	HttpConnectionManagerParams params = loadHttpConfFromFile();
    	
    	connectionManager = new MultiThreadedHttpConnectionManager();

        connectionManager.setParams(params);

        client = new HttpClient(connectionManager);
    }
    
    private static HttpConnectionManagerParams loadHttpConfFromFile(){
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(15000);
        params.setSoTimeout(30000);
        params.setStaleCheckingEnabled(true);
        params.setTcpNoDelay(true);
        params.setDefaultMaxConnectionsPerHost(100);
        params.setMaxTotalConnections(1000);
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		return params;
    }
    
	public static String post(String url, String encoding, String content) {
		try {
			byte[] resp = post(url, content.getBytes(encoding));
			if (null == resp)
				return null;
			return new String(resp, encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
    
    
	public static String post(String url, String content) {
    	return post(url, encoding, content);
    }


    public static byte[] post(String url, byte[] content) {
		try {
			byte[] ret = post(url, new ByteArrayRequestEntity(content));
			return ret;
		} catch (Exception e) {
			return null;
		}
    }

    public static byte[] post(String url, RequestEntity requestEntity) throws Exception {

        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Connection", "Keep-Alive");
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        method.setRequestEntity(requestEntity);
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
        
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            return method.getResponseBody();

        } catch (SocketTimeoutException e) {
        	return null;
        } catch (Exception e) {
        	return null;
        } finally {
            method.releaseConnection();
        }
    }
}
