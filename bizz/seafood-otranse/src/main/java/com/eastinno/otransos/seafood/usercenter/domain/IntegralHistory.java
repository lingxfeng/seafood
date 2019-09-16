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
 * 积分记录
 * @author nsz
 */
@Entity(name = "Disco_Shop_IntegralHistory")
public class IntegralHistory extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Date createDate=new Date();
	private Integer type=1;//1购物，2用户充值，3注册赠送积分，4后台管理员赠送,5抽奖使用, 6:退货返回积分，7抽奖获得
	private Long integral=0l;//积分数量
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
	public Long getIntegral() {
		return integral;
	}
	public void setIntegral(Long integral) {
		this.integral = integral;
	}
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}

	public IntegralHistory(){}
}
