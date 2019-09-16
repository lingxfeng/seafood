package com.eastinno.otransos.shop.droduct.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

/**
 * 特殊商品针对会员维护是否首次登陆
 * @author Administrator
 *
 */
@Entity(name = "Disco_Shop_SpecialProType")
public class SpecialProType {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember member;
	@ManyToOne(fetch=FetchType.LAZY)
	private ProductType pType;
	private Date createDate = new Date();//创建时间
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getMember() {
		return member;
	}
	public void setMember(ShopMember member) {
		this.member = member;
	}
	public ProductType getpType() {
		return pType;
	}
	public void setpType(ProductType pType) {
		this.pType = pType;
	}
}

