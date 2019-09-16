package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @Title: 微信公众帐号信息
 * @author maowei
 * @date 2014-05-21 00:53:47
 * @version V1.0
 */
@Entity
@Table(name = "Disco_WeiXin_Account")
public class Account extends TenantObject {
    private static final long serialVersionUID = 4847050388365654589L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    /** 公众帐号名称 */
    private String name;
    /** 公众帐号TOKEN */
    @Column(unique = true, nullable = false)
    private String token;
    /** 公众微信号 */
    private String number;
    /** 公众原始ID */
    private String accountid;
    /** 公众号类型 */
    private String type;// 1订阅号2服务号3企业号
    /** 公众帐号描述 */
    private String description;
    /** 公众帐号APPID */
    private String appid;
    /** 公众帐号APPSECRET */
    private String appsecret;
    /** TOKEN获取时间 */
    private Date addtoekntime = new Date();
    private String doMain;
    private String access_token;
    private Long access_token_time;
    private Integer expires_in;
    private String jsapi_ticket;//jssdk
    private Long jsapi_ticket_time;
    private String imgPath;
    private String qrcodeImg;
    @Column(unique=true,nullable=false)
    private String code;//账号编码
    private String handlerName;//自定义处理类
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public java.util.Date getAddtoekntime() {
        return addtoekntime;
    }

    public void setAddtoekntime(java.util.Date addtoekntime) {
        this.addtoekntime = addtoekntime;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getDoMain() {
        return doMain;
    }

    public void setDoMain(String doMain) {
        this.doMain = doMain;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getAccess_token_time() {
        return access_token_time;
    }

    public void setAccess_token_time(Long access_token_time) {
        this.access_token_time = access_token_time;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public Long getJsapi_ticket_time() {
		return jsapi_ticket_time;
	}

	public void setJsapi_ticket_time(Long jsapi_ticket_time) {
		this.jsapi_ticket_time = jsapi_ticket_time;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getQrcodeImg() {
		return qrcodeImg;
	}

	public void setQrcodeImg(String qrcodeImg) {
		this.qrcodeImg = qrcodeImg;
	}
	
}
