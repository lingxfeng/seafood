package com.eastinno.otransos.cms.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.security.domain.User;

/**
 * <p>
 * Title: 在线留言
 * </p>
 * <p>
 * Copyright: Copyright (c)2006
 * </p>
 * <p>
 * Company: Disco@gmail.com
 * </p>
 * 
 * @author lengyu
 * @since 2013-3-13 上午8:58:41
 */
@Entity(name = "Disco_SaaS_CMS_GuestBook")
public class GuestBook extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 50)
    private String guestName;// 留言人名称

    @Column(length = 200)
    private String email;

    private Date inputDate = new Date();

    private SystemDictionaryDetail types;// 留言类别

    @Column(length = 200)
    private String title;

    @Column(length = 2000)
    private String content;

    private Integer status = 0;// 状态 1公开 0待审核 -1删除

    @Column(length = 2000)
    @Field(name = "回复内容", validator = @Validator(name = "string", value = "max:2000"))
    private String reply;

    private Date replyDate;// 回复日期

    private String ip;

    @ManyToOne
    private User inputUser;// 留言者
    @ManyToOne
    private User replyUser;// 回复者

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"opUser", "user"});
        if (this.inputUser != null) {
            map.put("inputUser", CommUtil.obj2map(this.inputUser, new String[] {"id", "name", "trueName"}));
        }
        if (this.replyUser != null) {
            map.put("replyUser", CommUtil.obj2map(this.replyUser, new String[] {"id", "name", "trueName"}));
        }
        if (this.types != null) {
            map.put("types", CommUtil.obj2map(this.types, new String[] {"id", "title", "tvalue"}));
        }
        return map;
    }

    public String getGuestName() {
        return this.guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public SystemDictionaryDetail getTypes() {
        return types;
    }

    public void setTypes(SystemDictionaryDetail types) {
        this.types = types;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getInputUser() {
        return inputUser;
    }

    public void setInputUser(User inputUser) {
        this.inputUser = inputUser;
    }

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }
    public static void main(String[] args) {
    	int count = 0;
		for(int i = 0;i<=100000000;i++){
			if(i%2==1 && i%3==0 && i%4 == 1 && i%5 == 4 && i%6 == 3 && i%7 == 0 && i%8 == 1 && i%9 == 0){
				count = count +1;
				System.out.println(i);
			}
		}
		System.out.println(count);
		System.out.println("算完了");
	}
}
