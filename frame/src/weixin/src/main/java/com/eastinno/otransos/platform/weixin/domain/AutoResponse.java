package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Column;
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
 * 关键字自动回复
 * 
 * @author maowei
 */
@Entity
@Table(name = "Disco_WeiXin_AutoResponse")
public class AutoResponse extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@Column(nullable=false)
    private String keyWord;// 关键字
    @POLoad(name = "templateId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Template template;// 对应的模板
    private String msgType = "1";// 消息类别1文本消息 2普通图文消息3链接型图文消息
    @POLoad(name = "accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;// 所属账户
    private Date inputDate = new Date();

    @Column(name = "keyword")
    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    
    public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
}
