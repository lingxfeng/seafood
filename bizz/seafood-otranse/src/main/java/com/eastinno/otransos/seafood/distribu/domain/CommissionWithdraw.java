package com.eastinno.otransos.seafood.distribu.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 佣金提现
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_commissionwithdraw")
public class CommissionWithdraw {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//提现用户
	private Double commission;//金额
	private Double remaincommission;//提现后余额
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor distributor;//所属微店
	private Short status=0;//0,未支付1，已支付
	private Long createTime = (new Date()).getTime();//申请时间
	private Date payedTime;//支付时间
	private String uuid=System.currentTimeMillis()+"";//体现惟一表示
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	
	public Double getRemaincommission() {
		return remaincommission;
	}
	public void setRemaincommission(Double remaincommission) {
		this.remaincommission = remaincommission;
	}
	public ShopDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(ShopDistributor distributor) {
		this.distributor = distributor;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Date getPayedTime() {
		return payedTime;
	}
	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
