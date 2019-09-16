package com.eastinno.otransos.shop.promotions.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * 用户优惠券
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_SingleDispatchRecord")
public class SingleDispatchCouponRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private Short type=0;//1,发放给所有人，2发放给会员，-1，单独发放
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember shopMember;//获取用户
	private Date createTime= new Date();//获取时间
	@ManyToOne(fetch=FetchType.LAZY)
	private Coupon coupon;//优惠券
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
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
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}	
}
