package com.eastinno.otransos.seafood.droduct.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.seafood.promotions.domain.Coupon;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;
import com.eastinno.otransos.web.tools.AutoChangeLink;
import com.google.common.collect.Lists;

/**
 * 商品
 * 
 * @author nsz
 */
@Entity(name = "Disco_Shop_Product")
public class ShopProduct extends TenantObject implements AutoChangeLink, IJsonObject {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 100, nullable = false)
	private String name;
	@Column(length = 50, nullable = false)
	private String code = System.currentTimeMillis() + "";// 商品货号
	private Double amt = 0.0;// 单价(零售价)
	private Double store_price = 0.0;// 店铺价格（微店价格）
	private Double tydAmt = 0.0; // 体验店价格
	private Double costAmt = 0.0;// 成本价
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String intro;// 商品简介
	private Date createDate = new Date();// 录入日期

	@POLoad(name = "productTypeId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductType productType;

	private Boolean isVirtual = false;// 是否虚拟商品
	private Boolean isCod = false;// 是否支持货到付款
	private Boolean isRecommend = false;// 是否推荐
	private Boolean isTop = false;// 是否置顶
	private Short status = 0;// 商品状态；0，未发布，1.已发布，2.违规下架
	private String imgPaths;
	private String pcImgPath;// PC首页展示图
	private String videoPath;// 视频地址

	@POLoad(name = "brandId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Brand brand;// 品牌
	private Integer inventory = 0;// 商品库存
	@Column(length = 200)
	private String pack_details;// 商品清单
	@Column(length = 200)
	private String goods_service;// 售后服务
	@Column(length = 100)
	private String seo_keywords;
	@Column(length = 200)
	private String seo_description;
	private Integer saleNum = 0;// 卖出数量
	private Integer cridNum = 0;// 评论数量
	private Integer star5crid = 0;// 5星级评论
	private Integer star4crid = 0;
	private Integer star3crid = 0;
	private Integer star2crid = 0;
	private Integer star1crid = 0;
	private Integer collectNum = 0;// 收藏数量
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<AttributeValue> attrValues = new ArrayList<AttributeValue>();
	private Integer sequence = 1;// 排序
	private Boolean isPointBuy = false;// 是否可用积分购买
	private Long needPoints = 0l;// 需要的积分
	private Integer sendPoints = 0;// 买商品赠送积分
	private Boolean isMoreSpec = false;// 是否多规格商品
	@OneToMany(mappedBy = "product", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private List<ShopSpec> shopSpecs = Lists.newArrayList();
	private Integer goodsLimitbNumber = 0;// 每人限购
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String goods_details;// 商品详情
	@ManyToMany(mappedBy = "productlist", fetch = FetchType.LAZY)
	private List<Coupon> coupons = new ArrayList<Coupon>();

	@POLoad(name = "regionClassId")
	@ManyToOne(fetch = FetchType.LAZY)
	private RegionClass regionClass;

	@POLoad(name = "deliveryRuleExtId")
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryRuleExt deliveryRuleExt;

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	public String getGoods_details() {
		return goods_details;
	}

	public void setGoods_details(String goods_details) {
		this.goods_details = goods_details;
	}

	public Double getTydAmt() {
		return tydAmt;
	}

	public void setTydAmt(Double tydAmt) {
		this.tydAmt = tydAmt;
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

	public Double getCostAmt() {
		return costAmt;
	}

	public void setCostAmt(Double costAmt) {
		this.costAmt = costAmt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getAmt() {
		return amt;
	}

	public Integer getSendPoints() {
		return sendPoints;
	}

	public void setSendPoints(Integer sendPoints) {
		this.sendPoints = sendPoints;
	}

	public Double getStore_price() {
		return store_price;
	}

	public void setStore_price(Double store_price) {
		this.store_price = store_price;
	}

	public Integer getGoodsLimitbNumber() {
		return goodsLimitbNumber;
	}

	public void setGoodsLimitbNumber(Integer goodsLimitbNumber) {
		this.goodsLimitbNumber = goodsLimitbNumber;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Boolean getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Boolean getIsCod() {
		return isCod;
	}

	public void setIsCod(Boolean isCod) {
		this.isCod = isCod;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getImgPaths() {
		return imgPaths;
	}

	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}

	public String getPcImgPath() {
		return pcImgPath;
	}

	public void setPcImgPath(String pcImgPath) {
		this.pcImgPath = pcImgPath;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Boolean getIsMoreSpec() {
		return isMoreSpec;
	}

	public void setIsMoreSpec(Boolean isMoreSpec) {
		this.isMoreSpec = isMoreSpec;
	}

	public List<ShopSpec> getShopSpecs() {
		return shopSpecs;
	}

	public void setShopSpecs(List<ShopSpec> shopSpecs) {
		this.shopSpecs = shopSpecs;
	}

	public String getPack_details() {
		return pack_details;
	}

	public void setPack_details(String pack_details) {
		this.pack_details = pack_details;
	}

	public String getGoods_service() {
		return goods_service;
	}

	public void setGoods_service(String goods_service) {
		this.goods_service = goods_service;
	}

	public String getSeo_keywords() {
		return seo_keywords;
	}

	public void setSeo_keywords(String seo_keywords) {
		this.seo_keywords = seo_keywords;
	}

	public String getSeo_description() {
		return seo_description;
	}

	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public List<AttributeValue> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<AttributeValue> attrValues) {
		this.attrValues = attrValues;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	public Integer getCridNum() {
		return cridNum;
	}

	public void setCridNum(Integer cridNum) {
		this.cridNum = cridNum;
	}

	public Integer getStar5crid() {
		return star5crid;
	}

	public void setStar5crid(Integer star5crid) {
		this.star5crid = star5crid;
	}

	public Integer getStar4crid() {
		return star4crid;
	}

	public void setStar4crid(Integer star4crid) {
		this.star4crid = star4crid;
	}

	public Integer getStar3crid() {
		return star3crid;
	}

	public void setStar3crid(Integer star3crid) {
		this.star3crid = star3crid;
	}

	public Integer getStar2crid() {
		return star2crid;
	}

	public void setStar2crid(Integer star2crid) {
		this.star2crid = star2crid;
	}

	public Integer getStar1crid() {
		return star1crid;
	}

	public void setStar1crid(Integer star1crid) {
		this.star1crid = star1crid;
	}

	public Long getNeedPoints() {
		return needPoints;
	}

	public void setNeedPoints(Long needPoints) {
		this.needPoints = needPoints;
	}

	public Boolean getIsPointBuy() {
		return isPointBuy;
	}

	public void setIsPointBuy(Boolean isPointBuy) {
		this.isPointBuy = isPointBuy;
	}

	public RegionClass getRegionClass() {
		return regionClass;
	}

	public void setRegionClass(RegionClass regionClass) {
		this.regionClass = regionClass;
	}

	public DeliveryRuleExt getDeliveryRuleExt() {
		return deliveryRuleExt;
	}

	public void setDeliveryRuleExt(DeliveryRuleExt deliveryRuleExt) {
		this.deliveryRuleExt = deliveryRuleExt;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getImgIndex() {
		if (StringUtils.hasText(this.imgPaths)) {
			return this.imgPaths.split("_")[0];
		}
		return "";
	}

	public Map<String, String> getAttrMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<AttributeValue> values = this.getAttrValues();
		for (AttributeValue av : values) {
			AttributeKey a = av.getAttributeKey();
			if (a.getType() == 1) {
				String[] vals = a.getValue().split(",");
				int val = Integer.parseInt(av.getValue());
				if (val <= (vals.length)) {
					map.put(a.getName(), vals[val - 1]);
				}
			}
		}
		return map;
	}

	public Map<String, String> getCanshu() {
		Map<String, String> map = new HashMap<String, String>();
		List<AttributeValue> values = this.getAttrValues();
		for (AttributeValue av : values) {
			AttributeKey a = av.getAttributeKey();
			if (a.getType() == 2) {
				String val = av.getValue();
				if (!"".equals(val)) {
					map.put(a.getName(), val);
				}
			}
		}
		return map;
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

	@Override
	public String getDynamicUrl() {
		return "/goShop.java?cmd=productDetail&id=" + this.id;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", this.id);
		map.put("name", this.name);
		if (this.imgPaths != null && !"".equals(this.imgPaths)) {
			map.put("imgPath", this.imgPaths.split("_")[0]);
		}
		map.put("amt", this.amt);
		Map<String, Object> proMap = new HashMap<String, Object>();
		proMap.put("id", this.productType.getId());
		proMap.put("name", this.productType.getName());
		map.put("productType", proMap);
		return map;
	}

	public Set<AttributeKey> getAttrKs(String typestr) {
		Short type = Short.parseShort(typestr);
		Set<AttributeKey> set = new HashSet<AttributeKey>();
		for (AttributeValue av : this.attrValues) {
			AttributeKey ak = av.getAttributeKey();
			if (ak.getType() == type) {
				set.add(ak);
			}
		}
		return set;
	}

	public AttributeValue getAttrVs(AttributeKey ak) {
		for (AttributeValue av : this.attrValues) {
			if (av.getAttributeKey().getId().equals(ak.getId())) {
				return av;
			}
		}
		return null;
	}
}
