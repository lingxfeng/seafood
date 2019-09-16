package com.eastinno.otransos.platform.weixin.bean;

public class RespTextMessage extends RespMessage {

    private String MsgType = "text";
    // 文本消息
    private String Content;

    public RespTextMessage() {
    }

    public RespTextMessage(String content) {
        Content = content;
    }

    public String getMsgType() {
        return MsgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
