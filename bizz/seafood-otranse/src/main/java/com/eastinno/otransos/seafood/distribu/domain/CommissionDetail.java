package com.eastinno.otransos.seafood.distribu.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 佣金设置
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_commissiondetail")
public class CommissionDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//购买用户
	
	private Double integral;//积分
	@OneToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo order;//订单
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor levelonedistri;//一级佣金获得者
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor leveltowdistri;//二级佣金获得者
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor balanceonedistri;//一级利润获得者
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor balancetowdistri;//二级利润获得者
	private Double levelonecommission;//一级佣金
	private Double leveltowcommission;//二级佣金
	private Double balanceone;//一级利润
	private Double balancetow;//二级利润
	private Long createTime = (new Date()).getTime();//创建时间
	private Date modifiedTime;//修改时间
	private Short status=1;//1:正常分配 2：退回分配
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
	public Double getIntegral() {
		return integral;
	}
	public void setIntegral(Double integral) {
		this.integral = integral;
	}
	
	public ShopOrderInfo getOrder() {
		return order;
	}
	public void setOrder(ShopOrderInfo order) {
		this.order = order;
	}
	public ShopDistributor getLevelonedistri() {
		return levelonedistri;
	}
	public void setLevelonedistri(ShopDistributor levelonedistri) {
		this.levelonedistri = levelonedistri;
	}
	public ShopDistributor getLeveltowdistri() {
		return leveltowdistri;
	}
	public void setLeveltowdistri(ShopDistributor leveltowdistri) {
		this.leveltowdistri = leveltowdistri;
	}
	public ShopDistributor getBalanceonedistri() {
		return balanceonedistri;
	}
	public void setBalanceonedistri(ShopDistributor balanceonedistri) {
		this.balanceonedistri = balanceonedistri;
	}
	public ShopDistributor getBalancetowdistri() {
		return balancetowdistri;
	}
	public void setBalancetowdistri(ShopDistributor balancetowdistri) {
		this.balancetowdistri = balancetowdistri;
	}
	public Double getLevelonecommission() {
		return levelonecommission;
	}
	public void setLevelonecommission(Double levelonecommission) {
		this.levelonecommission = levelonecommission;
	}
	public Double getLeveltowcommission() {
		return leveltowcommission;
	}
	public void setLeveltowcommission(Double leveltowcommission) {
		this.leveltowcommission = leveltowcommission;
	}
	public Double getBalanceone() {
		return balanceone;
	}
	public void setBalanceone(Double balanceone) {
		this.balanceone = balanceone;
	}
	public Double getBalancetow() {
		return balancetow;
	}
	public void setBalancetow(Double balancetow) {
		this.balancetow = balancetow;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
}
