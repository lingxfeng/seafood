package com.eastinno.otransos.platform.weixin.bean;
/**
 * 语音消息
 * @author nsz
 *
 */
public class RespVoiceMessage extends RespMessage{
	private String MsgType = "voice";
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
}
