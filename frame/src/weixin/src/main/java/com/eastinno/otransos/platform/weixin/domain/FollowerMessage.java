package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;
/**
 * 粉丝消息管理
 * @author nsz
 */
@Entity
@Table(name = "Disco_WeiXin_FollowerMsg")
public class FollowerMessage extends TenantObject  {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private Boolean isFollowerMsg=true;
	private String msgType;//消息类型，text，image，voice，video,location,link
	private String content;
	private Date createDate = new Date();
	private String mediaId;
	private String msgId;
	//图片类型
	private String picUrl;
	//语音类型
	private String format;//语音格式
	//视频类型
	private String ThumbMediaId;//视频缩略图id
	//地理位置
	private String location_X;
	private String location_Y;
	private String scale;//地图缩放大小
	private String label;//地理位置信息
	//链接消息
	private String titleStr;
	private String descriptionStr;
	private String urlStr;
	@POLoad(name = "followerId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Follower follower;
	@POLoad(name = "accountId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Account account;
	@POLoad(name = "templateId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Template template;
	private String mediaUrl;
	private String ThumbMediaUrl;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getIsFollowerMsg() {
		return isFollowerMsg;
	}
	public void setIsFollowerMsg(Boolean isFollowerMsg) {
		this.isFollowerMsg = isFollowerMsg;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getThumbMediaId() {
		return ThumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
	public String getLocation_X() {
		return location_X;
	}
	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}
	public String getLocation_Y() {
		return location_Y;
	}
	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTitleStr() {
		return titleStr;
	}
	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}
	public String getDescriptionStr() {
		return descriptionStr;
	}
	public void setDescriptionStr(String descriptionStr) {
		this.descriptionStr = descriptionStr;
	}
	public String getUrlStr() {
		return urlStr;
	}
	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}
	public Follower getFollower() {
		return follower;
	}
	public void setFollower(Follower follower) {
		this.follower = follower;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getThumbMediaUrl() {
		return ThumbMediaUrl;
	}
	public void setThumbMediaUrl(String thumbMediaUrl) {
		ThumbMediaUrl = thumbMediaUrl;
	}
	
}
