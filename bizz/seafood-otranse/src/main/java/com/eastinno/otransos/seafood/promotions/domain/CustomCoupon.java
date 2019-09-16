package com.eastinno.otransos.seafood.promotions.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 用户优惠券
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_CustomCoupon")
public class CustomCoupon {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private Short status=0;//状态0，未使用 ，1，已使用，3,身份不同不能使用
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember shopMember;//获取用户
	private Date createTime= new Date();//获取时间
	private Date useTime;//使用时间
	private Date outTime;//过期时间
	@ManyToOne(fetch=FetchType.LAZY)
	private Coupon coupon;//优惠券
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public ShopMember getShopMember() {
		return shopMember;
	}
	public void setShopMember(ShopMember shopMember) {
		this.shopMember = shopMember;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}	
}
