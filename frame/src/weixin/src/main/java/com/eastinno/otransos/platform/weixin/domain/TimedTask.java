package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

@Entity
@Table(name = "Disco_WeiXin_TimedTask")
public class TimedTask implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	
	private String title; //标题
	
	private String date;
	
	private String minute;
	
	private String hour;
	
	private String state;// 1:所有粉丝 2：群 3:24小时有操作
	
	private Date crearteTime = new Date(); //创建时间
	
	private String msgType;// 1:文本消息 2:图文消息 3:音乐消息 4:图片消息 5:语音消息 6:视频消息
	
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	@Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;// 描述简介
	
	@POLoad(name = "accountId")
	@ManyToOne
	private Account account;
	
	@POLoad(name = "templateId")
	@ManyToOne
    private Template template;// 消息模板
	
	@POLoad(name = "followerGroupId")
    @ManyToOne(fetch = FetchType.LAZY)
	private FollowerGroup followerGroup;
	
	public FollowerGroup getFollowerGroup() {
		return followerGroup;
	}
	public void setFollowerGroup(FollowerGroup followerGroup) {
		this.followerGroup = followerGroup;
	}
	public String getDescription() {
		return description;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCrearteTime() {
		return crearteTime;
	}

	public void setCrearteTime(Date crearteTime) {
		this.crearteTime = crearteTime;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {""});
		return map;
	}
	
	
}
