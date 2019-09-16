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
 * 关注欢迎语
 * 
 * @author Administrator
 */
@Entity
@Table(name = "Disco_WeiXin_Subscribe")
public class Subscribe extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@POLoad(name = "templateId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Template template;
    private String msgType;
    private Date addTime = new Date();
    @POLoad(name = "accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
    
}
