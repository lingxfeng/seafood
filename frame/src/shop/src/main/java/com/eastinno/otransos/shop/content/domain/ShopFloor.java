package com.eastinno.otransos.shop.content.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
/**
 * 首页楼层管理
 * @author Administrator
 *
 */
@Entity(name = "Disco_Shop_ShopFloor")
public class ShopFloor {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@POLoad(name = "productTypeId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ProductType productType;
	@Column(length=20)
	private String floorColor;//楼层颜色
	@Column(length=50)
	private String name;//楼层标题
	@Column(length=100)
	private String logoImg;//楼层导航图标
	private Integer sequence=1;//排序
	private Boolean isTopPro;//是否显示置顶商品
	@Column(length=100)
	private String advImg;
	@Column(length=100)
	private String advUrl;
	private Short type=0;//0,样式一，1，样式二
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFloorColor() {
		return floorColor;
	}
	public void setFloorColor(String floorColor) {
		this.floorColor = floorColor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogoImg() {
		return logoImg;
	}
	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public Boolean getIsTopPro() {
		return isTopPro;
	}
	public void setIsTopPro(Boolean isTopPro) {
		this.isTopPro = isTopPro;
	}
	public String getAdvImg() {
		return advImg;
	}
	public void setAdvImg(String advImg) {
		this.advImg = advImg;
	}
	public String getAdvUrl() {
		return advUrl;
	}
	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	
}
