package com.eastinno.otransos.application.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.security.domain.User;

/**
 * 站内消息
 * 
 * @author maowei
 * @createDate 2014-1-15下午4:15:05
 */
@Entity(name = "Disco_ShortMessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShortMessage extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long Id;

    @Column(length = 50)
    private String title;// 消息标题

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;// 消息内容

    @POLoad(name = "senderId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;// 消息发送者

    @OneToOne(fetch = FetchType.LAZY)
    private User receive;// 消息接收者

    private Short msgType;// 消息类型
    private Date sendTime = new Date();// 发送日期

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Short getMsgType() {
        return this.msgType;
    }

    public void setMsgType(Short msgType) {
        this.msgType = msgType;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceive() {
        return receive;
    }

    public void setReceive(User receive) {
        this.receive = receive;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}