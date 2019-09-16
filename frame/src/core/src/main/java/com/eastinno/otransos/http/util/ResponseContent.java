package com.eastinno.otransos.http.util;

import java.io.UnsupportedEncodingException;

/**
 * 封装HttpClient返回数据
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年9月27日 下午2:59:08
 * @Intro
 */
public class ResponseContent {
    private String encoding = "UTF-8";// 编码
    private byte[] contentBytes;// 返回数据的字节
    private int statusCode;// 返回的状态码如200表示成功
    private String contentType;// 数据类型
    private String contentTypeString;//

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeString() {
        return this.contentTypeString;
    }

    public void setContentTypeString(String contenttypeString) {
        this.contentTypeString = contenttypeString;
    }

    public String getContent() throws UnsupportedEncodingException {
        return this.getContent(this.encoding);
    }

    @SuppressWarnings("hiding")
    public String getContent(String encoding) throws UnsupportedEncodingException {
        if (encoding == null) {
            return new String(contentBytes);
        }
        return new String(contentBytes, encoding);
    }

    public String getUTFContent() throws UnsupportedEncodingException {
        return this.getContent("UTF-8");
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}