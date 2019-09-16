package com.eastinno.otransos.shop.usercenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
/**
 * 收货地址
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopAddress")
public class ShopAddress {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length=20,nullable=false)
	private String trueName;//收货人姓名
	private String telephone;//收货人电话
	@POLoad(name = "area_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private SystemRegion area;//县
	@Column(length=200)
	private String area_info;//详情
	@Column(length=6)
	private String zip;//邮编
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//所属用户
	private Boolean isDefault=false;//是否为默认地址
	private Date createDate =new Date();//
	
	public String getProvinceName() {
		String result = "";
		try{
			result = this.getArea().getParent().getParent().getTitle();
		}catch(Exception e){
			result = "";
			return result;
		}
		return result;
	}

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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
