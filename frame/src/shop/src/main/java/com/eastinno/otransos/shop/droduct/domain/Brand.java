package com.eastinno.otransos.shop.droduct.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * 品牌管理
 * 
 * @author nsz
 */
@Entity(name = "Disco_Shop_Brand")
public class Brand implements AutoChangeLink {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;// 品牌名称
	@POLoad(name="brandTypeId")
	@ManyToOne(fetch=FetchType.LAZY)
	private BrandType brandType;
	@Column(nullable = false)
	private Character indexC;// 首字母

	@Column(nullable = false, unique = true, length = 50)
	private String code=System.currentTimeMillis()+"";// 编码

	@Column(nullable = false, length = 100)
	private String imgPath;// 默认图片

	private Boolean isRecommend;// 是否推荐
	private Integer sequence = 1;// 排序

	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	private List<ProductType> productTypes = new ArrayList<ProductType>();
	
	@ManyToMany(mappedBy = "brandlist",fetch = FetchType.LAZY)
	private List<Coupon> coupons = new ArrayList<Coupon>();
	private Date createDate = new Date();

	private String description;// 栏目描述
	
	private String afterSaleService;//售后服务

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

	public Character getIndexC() {
		return indexC;
	}

	public void setIndexC(Character indexC) {
		this.indexC = indexC;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductType> productTypes) {
		this.productTypes = productTypes;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public BrandType getBrandType() {
		return brandType;
	}

	public void setBrandType(BrandType brandType) {
		this.brandType = brandType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String getStaticUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStaticPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAfterSaleService() {
		return afterSaleService;
	}

	public void setAfterSaleService(String afterSaleService) {
		this.afterSaleService = afterSaleService;
	}

	@Override
	public String getDynamicUrl() {
		return "/goShop.java?cmd=toBrand&id=" + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if(obj==null){
			return false;
		}
		if(!(obj instanceof Brand)){
			return false;
		}
		Brand brandObj = (Brand) obj;
		if(brandObj.getCode().equals(code) && brandObj.getId().equals(id)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.id.hashCode()+this.code.hashCode();
	}
}
