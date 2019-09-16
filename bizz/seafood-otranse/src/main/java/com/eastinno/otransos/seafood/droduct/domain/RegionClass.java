package com.eastinno.otransos.seafood.droduct.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.eastinno.otransos.core.domain.SystemRegion;

/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年8月20日 下午12:03:40
*@version 1.0
*@parameter
*@since
*@return 
*/
@Entity(name="Disco_Region_Class")
public class RegionClass {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(nullable=false, unique=true)
	private Long id;	//主键标示
	private String name;//名称
	private Long code=System.currentTimeMillis();
	private String intro;//说明
	@OneToMany(mappedBy="regionClass",cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
	private List<NearRegion> nearRegion = new ArrayList<>();	//附近区域
	
	@OneToMany(mappedBy="regionClass",cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
	private List<RemoteRegion> remoteRegion = new ArrayList<>();	//偏远地区
	
	@OneToMany(mappedBy="regionClass",fetch = FetchType.LAZY)
	private List<ShopProduct> shopProduct = new ArrayList<>();	//商品
	private Boolean isDefault=false;//是否默认
	private Date createDate = new Date();	//记录创建时间
	private Date modifyDate;	//记录修改时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	
	
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public List<NearRegion> getNearRegion() {
		return nearRegion;
	}
	public void setNearRegion(List<NearRegion> nearRegion) {
		this.nearRegion = nearRegion;
	}
	public List<RemoteRegion> getRemoteRegion() {
		return remoteRegion;
	}
	public void setRemoteRegion(List<RemoteRegion> remoteRegion) {
		this.remoteRegion = remoteRegion;
	}
	public List<ShopProduct> getShopProduct() {
		return shopProduct;
	}
	public void setShopProduct(List<ShopProduct> shopProduct) {
		this.shopProduct = shopProduct;
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
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
