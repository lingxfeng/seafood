package com.eastinno.otransos.seafood.spokesman.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;

/**
 * 代言人商品
 * @author dll
 *
 */

@Entity(name = "Disco_Shop_spokesmanProduct")
public class SpokesmanProduct {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@OneToOne(fetch=FetchType.LAZY)
	private ShopProduct product;//关联的商品
	private Integer totalMonths;//返还月数
	private Float restitution=0F;//每月最高返还金额
	private Float value=0F;//现金价值
	private Float  restitutionPrice=0F;//代言价格
	private Date createTime=new Date();//创建时间
	private Date modifiedTime;//修改时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopProduct getProduct() {
		return product;
	}
	public void setProduct(ShopProduct product) {
		this.product = product;
	}
	public Integer getTotalMonths() {
		return totalMonths;
	}
	public void setTotalMonths(Integer totalMonths) {
		this.totalMonths = totalMonths;
	}
	public Float getRestitution() {
		return restitution;
	}
	public void setRestitution(Float restitution) {
		this.restitution = restitution;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public Float getRestitutionPrice() {
		return restitutionPrice;
	}
	public void setRestitutionPrice(Float restitutionPrice) {
		this.restitutionPrice = restitutionPrice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
}
