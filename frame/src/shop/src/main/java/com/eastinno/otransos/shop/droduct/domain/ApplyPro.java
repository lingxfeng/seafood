package com.eastinno.otransos.shop.droduct.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;

/**
 * 申请商品
 * @author nsz
 */
@Entity(name = "Disco_Shop_ApplyPro")
public class ApplyPro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String proPic;//申请商品图片
	private String eqPic;//企业资质
	private String mobile;//手机号码
	@POLoad(name = "distributorId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor distributor;
	private Date createDate = new Date(); //申请时间
	private Integer status = 0; //0：审核未通过 1：审核通过
	private Date auditTime;//审核时间
	@ManyToOne(fetch=FetchType.LAZY)
	private User auditUser;//审核人
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProPic() {
		return proPic;
	}
	public void setProPic(String proPic) {
		this.proPic = proPic;
	}
	public String getEqPic() {
		return eqPic;
	}
	public void setEqPic(String eqPic) {
		this.eqPic = eqPic;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public ShopDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(ShopDistributor distributor) {
		this.distributor = distributor;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public User getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}
}
