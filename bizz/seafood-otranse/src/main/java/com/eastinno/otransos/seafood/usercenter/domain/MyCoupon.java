package com.eastinno.otransos.seafood.usercenter.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.seafood.droduct.domain.Couponlist;
@Entity(name = "Disco_Shop_MyCoupon")
public class MyCoupon {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember shopMember;
	@ManyToOne(fetch=FetchType.LAZY)
	private Couponlist coupon;//公共优惠券
	private Integer status=2;//0已使用2未过期
	private Date createDate=new Date();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getShopMember() {
		return shopMember;
	}
	public void setShopMember(ShopMember shopMember) {
		this.shopMember = shopMember;
	}
	public Couponlist getCoupon() {
		return coupon;
	}
	public void setCoupon(Couponlist coupon) {
		this.coupon = coupon;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
