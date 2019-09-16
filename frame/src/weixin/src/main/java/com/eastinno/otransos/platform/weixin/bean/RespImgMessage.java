package com.eastinno.otransos.platform.weixin.bean;
/**
 * 图片消息
 * @author nsz
 *
 */
public class RespImgMessage extends RespMessage{
	private String MsgType = "image";
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	
}
