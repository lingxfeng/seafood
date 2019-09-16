package com.eastinno.otransos.shop.droduct.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;
import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * 商品分类
 * @author nsz
 */
@Entity(name = "Disco_Shop_ProductType")
public class ProductType extends TenantObject implements IJsonObject, AutoChangeLink {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;// 分类名称

	@Column(unique = true, length = 20, nullable = false)
	private String code;
	private Integer sequence = 1;// 排序

	@POLoad(name = "parentId")
	@ManyToOne(fetch = FetchType.EAGER)
	private ProductType parent; // 父栏目

	@OrderBy("sequence")
	@OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private List<ProductType> chilren = new ArrayList<ProductType>();// 子栏目

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy("sequence")
	private List<Brand> brands = new ArrayList<Brand>();// 品牌
	private Boolean isChilrenBrand = false;// 品牌是否关联到下级

	@OneToMany(mappedBy = "productType", fetch = FetchType.LAZY)
	private List<AttributeKey> attributeKeys = new ArrayList<AttributeKey>();// 该商品分类所拥有的属性
	private Boolean isChilrenAtt = false;// 属性是否关联到下级
	
	private Boolean isShow = false;// 是否显示
	private Boolean isRecommend = false;// 是否推荐
	@Column(length = 100)
	private String imgPath;
	@Column(length = 50)
	private String seoKey;
	private String seoDescripts;
	private Date cleateDate = new Date();
	private Integer level = 1;
	@Column(length = 100)
	private String advImg;
	@Column(length = 100)
	private String advUrl;
	@Column(length = 10)
	private String curColor;
	private Boolean isPrivate=true;
	private String dePath;
	
	private Boolean isSpecialProType = false;//是否特殊商品分类
	private String password;//特殊分类密码
	
	public Boolean getIsSpecialProType() {
		return isSpecialProType;
	}

	public void setIsSpecialProType(Boolean isSpecialProType) {
		this.isSpecialProType = isSpecialProType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public ProductType getParent() {
		return parent;
	}
	
	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setParent(ProductType parent) {
		this.parent = parent;
	}

	public List<ProductType> getChilren() {
		return chilren;
	}

	public void setChilren(List<ProductType> chilren) {
		this.chilren = chilren;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public Boolean getIsChilrenBrand() {
		return isChilrenBrand;
	}

	public void setIsChilrenBrand(Boolean isChilrenBrand) {
		this.isChilrenBrand = isChilrenBrand;
	}

	public List<AttributeKey> getAttributeKeys() {
		return attributeKeys;
	}

	public void setAttributeKeys(List<AttributeKey> attributeKeys) {
		this.attributeKeys = attributeKeys;
	}

	public Boolean getIsChilrenAtt() {
		return isChilrenAtt;
	}

	public void setIsChilrenAtt(Boolean isChilrenAtt) {
		this.isChilrenAtt = isChilrenAtt;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public String getSeoKey() {
		return seoKey;
	}

	public void setSeoKey(String seoKey) {
		this.seoKey = seoKey;
	}

	public String getSeoDescripts() {
		return seoDescripts;
	}

	public void setSeoDescripts(String seoDescripts) {
		this.seoDescripts = seoDescripts;
	}

	public Date getCleateDate() {
		return cleateDate;
	}

	public void setCleateDate(Date cleateDate) {
		this.cleateDate = cleateDate;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public String getCurColor() {
		return curColor;
	}

	public void setCurColor(String curColor) {
		this.curColor = curColor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDePath() {
		return dePath;
	}

	public void setDePath(String dePath) {
		this.dePath = dePath;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.parent != null) {
			map.put("parent", CommUtil.obj2map(this.parent, new String[] {
					"id", "name" }));
		}
		map.put("id", this.id);
		map.put("name", this.name);
		map.put("isRecommend", this.isRecommend);
		map.put("sequence", this.sequence);
		map.put("isShow", this.isShow);
		map.put("level", this.level);
		map.put("chilrenSize", this.chilren.size());
		return map;
	}

	/**
	 * 获取一级分类
	 * 
	 * @return
	 */
	public ProductType getTopPType() {
		ProductType pType = this;
		while (pType.parent != null) {
			pType = pType.parent;
		}
		return pType;
	}

	/**
	 * 获取所有的分类
	 * 
	 * @return
	 */
/*	public Set<Brand> getAllBrands() {
		Set<Brand> set = new HashSet<Brand>();
		set.addAll(this.brands);
		if(this.parent!=null && this.parent.isChilrenBrand){
			set.addAll(this.parent.getAllBrands());
		}
		return set;
	}*/
	public List<Brand> getAllBrands() {
		List<Brand> set = new ArrayList<Brand>();
		set.addAll(this.brands);
		if(this.parent!=null && this.parent.isChilrenBrand){
			set.addAll(this.parent.getAllBrands());
		}
		return set;
	}

	/**
	 * 获取所有的属性
	 * 
	 * @return
	 */
	public List<AttributeKey> getAllAttrs(String typeStr) {
		Short type = Short.parseShort(typeStr);
		List<AttributeKey> list = new ArrayList<AttributeKey>();
		for (AttributeKey a : this.attributeKeys) {
			if (type == a.getType()) {
				list.add(a);
			}
		}
		if(this.parent!=null && this.parent.isChilrenAtt){
			list.addAll(this.parent.getAllAttrs(typeStr));
		}
		return list;
	}

	/**
	 * 获取最子级推荐分类
	 * 
	 * @return
	 */
	public List<ProductType> getPTypeIsRecommend() {
		List<ProductType> list = new ArrayList<ProductType>();
		for (ProductType pType : this.getChilren()) {
			if (pType.getLevel() != 3) {
				for (ProductType p : pType.getChilren()) {
					if (p.isRecommend) {
						list.add(p);
					}
				}
			} else {
				if (pType.isRecommend) {
					list.add(pType);
				}
			}
		}
		return list;
	}

	@Override
	public String getStaticUrl() {
		return null;
	}

	@Override
	public String getStaticPath() {
		return null;
	}

	@Override
	public String getDynamicUrl() {
		return "/goShop.java?cmd=queryPro&pid=" + this.id;
	}
}
