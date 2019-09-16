package com.eastinno.otransos.seafood.usercenter.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;

@Entity(name = "Disco_Shop_SinceSome")
public class ShopSinceSome {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Date createDate =new Date();//创建日期
	private String trueName;//收货人姓名
	private String telephone;//收货人电话
	@POLoad(name = "area_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private SystemRegion area;//县
	private String area_info;//详情
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//所属用户
	@POLoad(name="shopDistributorId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor shopDistributor;//经销商自提点
	private Boolean isDefault=false;//是否为默认地址
	private Short status=1;//1有效2无效
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
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public SystemRegion getArea() {
		return area;
	}
	public void setArea(SystemRegion area) {
		this.area = area;
	}
	public String getArea_info() {
		return area_info;
	}
	public void setArea_info(String area_info) {
		this.area_info = area_info;
	}
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public ShopDistributor getShopDistributor() {
		return shopDistributor;
	}
	public void setShopDistributor(ShopDistributor shopDistributor) {
		this.shopDistributor = shopDistributor;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
}
