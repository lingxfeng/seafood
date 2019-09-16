package com.eastinno.otransos.platform.weixin.bean;

public class RespMusicMessage extends RespMessage {
    private String MsgType = "music";
    private Music Music;
    
    public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getMsgType() {
        return MsgType;
    }

}
