package com.eastinno.otransos.seafood.promotions.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;


/**
 * 中奖纪录
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_SweepstakesRecord")
public class SweepstakesRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//抽奖人
	private Date createTime = new Date();//抽奖时间
	private Short type=0;//抽奖方式0,消耗积分，1,消耗佣金
	private Integer cost=0;
	private Short status=0;//是否中奖，0，未中奖，1，中奖为商品，2中奖积分，
	private String productname;//中奖商品
	private Integer integal=0;//若中奖为积分，获得积分额
	//@OneToOne
	//private Coupon coupon;//中奖的优惠券
	@OneToOne(fetch=FetchType.LAZY)
	private ShopAddress address;//奖品收货地址
	private Short isDispatch=0;//是否发放，0，未，1，已
	private String imgPaths;//奖品图片路径
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public Integer getIntegal() {
		return integal;
	}
	public void setIntegal(Integer integal) {
		this.integal = integal;
	}
	public ShopAddress getAddress() {
		return address;
	}
	public void setAddress(ShopAddress address) {
		this.address = address;
	}
	public Short getIsDispatch() {
		return isDispatch;
	}
	public void setIsDispatch(Short isDispatch) {
		this.isDispatch = isDispatch;
	}
	public String getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}
	
	
	
}
