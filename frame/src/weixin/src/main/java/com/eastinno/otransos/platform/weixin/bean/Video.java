package com.eastinno.otransos.platform.weixin.bean;
/**
 * 视频消息
 * nsz
 */
public class Video {
	private String MediaId;
	private String Title;
	private String Description;
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
}
