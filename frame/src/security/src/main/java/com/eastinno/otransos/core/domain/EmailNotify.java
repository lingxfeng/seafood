package com.eastinno.otransos.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.Validator;

/**
 * @intro 邮件通知列表，把发送出去的邮件系统作一个记录给保存下来
 * @version v0.1
 * @author maowei
 * @since 2010年5月9日 上午10:59:02
 */
@Entity(name = "Disco_EmailNotify")
public class EmailNotify implements Serializable {
    private static final long serialVersionUID = 8625368804742956154L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Field(name = "接收人，使用','或';'符号分隔")
    @Column(length = 1000)
    private String toUser;
    private String copyTo;
    private String secretTo;

    @Field(name = "邮件主题", validator = @Validator(name = "string", required = true, value = "blank;trim"))
    @Column(length = 200)
    private String subject;

    @Field(name = "邮件内容")
    @Lob
    private String content;

    @Field(name = "附件")
    @Column(length = 200)
    private String files;
    private Date inputTime;

    @Field(name = "类型")
    @Column(length = 16)
    private String types;
    private int times;

    @Field(name = "状态")
    private int status;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFiles() {
        return this.files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Date getInputTime() {
        return this.inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getTypes() {
        return this.types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimes() {
        return this.times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getCopyTo() {
        return this.copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public String getSecretTo() {
        return this.secretTo;
    }

    public void setSecretTo(String secretTo) {
        this.secretTo = secretTo;
    }
}
