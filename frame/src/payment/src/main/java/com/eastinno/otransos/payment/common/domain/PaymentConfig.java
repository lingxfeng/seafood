package com.eastinno.otransos.payment.common.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 支付方式配置(支付宝、银联、微信)
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年11月26日 下午3:31:06
 * @Intro
 */
@Entity(name = "Disco_Pay_PaymentConfig")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentConfig extends TenantObject implements IJsonObject, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotNull
    private String name;// 支付平台名称
    @NotNull
    private String bargainorId;// 支付宝银联商家ID，微信appId
    @NotNull
    private String bargainorKey;// 支付宝银联商户私钥，微信app密钥

    private String seller_email;//商家帐号
    private String logo;// 支付产品平台LOGO标识
    private String intro;// 介绍
    private Integer sequence = 0;// 排序
    private Integer status = 1;// 状态1正常0禁用
    private PayTypeE type = PayTypeE.ALIPAYPCWEB;// 支付类型
    
    /**
     * 非必输，支付类型不同，需要参数不同
     */
    private String partnerkey;//微信支付商家密钥
    private String perPath;//证书路径，需要证书的支付证书路径
    
    public String getPerPath() {
		return perPath;
	}

	public void setPerPath(String perPath) {
		this.perPath = perPath;
	}

	public String getPartnerkey() {
		return partnerkey;
	}

	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public String getBargainorKey() {
        return bargainorKey;
    }

    public void setBargainorKey(String bargainorKey) {
        this.bargainorKey = bargainorKey;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public PayTypeE getType() {
		return type;
	}

	public void setType(PayTypeE type) {
		this.type = type;
	}

	public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    @Override
    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tenant"});
        if (this.tenant != null) {
            map.put("tenant", CommUtil.obj2map(this.tenant, new String[] {"id", "code", "title"}));
        }
        return map;
    }
}
