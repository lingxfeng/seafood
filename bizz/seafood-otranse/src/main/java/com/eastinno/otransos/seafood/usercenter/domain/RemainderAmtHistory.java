package com.eastinno.otransos.seafood.usercenter.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.security.domain.TenantObject;

/**
 * 余额记录
 * @author nsz
 */
@Entity(name = "Disco_Shop_RemainderAmtHistory")
public class RemainderAmtHistory extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Date createDate=new Date();
	private Integer type=1;//1：购物，2：充值，3：退款.4.退款佣金，5，获取佣金  6:提现 7:管理员操作
	private Double amt=0.0;//交易金额
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//所属用户
	private String description;	//备注信息
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}
	
}
