package com.eastinno.otransos.platform.weixin.bean;

public class RespVideoMessage extends RespMessage{
	private String MsgType = "video";
	private Video Video;
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public Video getVideo() {
		return Video;
	}
	public void setVideo(Video video) {
		Video = video;
	}
	
}
