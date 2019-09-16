package com.eastinno.otransos.platform.weixin.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * 消息模板(普通文本消息、图文消息等)
 * 
 * @author maowei
 */
@Entity
@Table(name = "Disco_WeiXin_Template")
public class Template extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String title;// 消息模板的标题名称
    private String content;// 普通文本消息内容
    private Date inputDate = new Date();
    @POLoad(name = "accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;// 消息模板所对应的微信公众号
    private String type = "1";// 消息类别1文本消息 2普通图文消息3链接型图文消息4图片消息，5语音消息6.视频消息
    @OneToMany(mappedBy="tpl",cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<NewsItem> newsItemList = new ArrayList<NewsItem>();// 图文消息详情
    private String mediaPath;
    private String mediaId;
    private Long meidaCreateAt;
    private String thumbMediaPath;
    private String thumbMediaId;
    private Long thumbMediaCreateAt;
	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<NewsItem> getNewsItemList() {
		return newsItemList;
	}

	public void setNewsItemList(List<NewsItem> newsItemList) {
		this.newsItemList = newsItemList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getMeidaCreateAt() {
		return meidaCreateAt;
	}

	public void setMeidaCreateAt(Long meidaCreateAt) {
		this.meidaCreateAt = meidaCreateAt;
	}

	public String getThumbMediaPath() {
		return thumbMediaPath;
	}

	public void setThumbMediaPath(String thumbMediaPath) {
		this.thumbMediaPath = thumbMediaPath;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public Long getThumbMediaCreateAt() {
		return thumbMediaCreateAt;
	}

	public void setThumbMediaCreateAt(Long thumbMediaCreateAt) {
		this.thumbMediaCreateAt = thumbMediaCreateAt;
	}
	
}
