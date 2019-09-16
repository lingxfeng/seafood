package com.eastinno.otransos.seafood.promotions.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.google.common.collect.Lists;

/**
 * 优惠券
 * @author dll
 */
@Entity(name = "Disco_Shop_Merchant")
public class Coupon{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private Short currentStatus=1;//状态0，未生效 ，1，已生效,2已过期
	private Integer time;//有效时间（天）
//	private Short getType=1;//获得规则0，注册就送，1，商家发放，2，购买某品牌送3，购买某商品送
//	@ManyToMany(fetch = FetchType.LAZY)
//	private List<Brand> getBrandlist = new ArrayList<Brand>();//购买满足条件可获取优惠券的品牌
//	@ManyToMany(fetch = FetchType.LAZY)
//	private List<ShopProduct> getProductlist = Lists.newArrayList();//购买满足条件可获取优惠券的商品	
	private Short type=0;//使用类型：0，全场可用，1，限制品牌 2,限制商品
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Brand> brandlist = new ArrayList<Brand>();//购买满足条件可使用优惠券的品牌
	@ManyToMany(fetch = FetchType.LAZY)
	private List<ShopProduct> productlist = Lists.newArrayList();//购买满足条件可使用优惠券的商品    
    private Double usecondition=0.0;//使用条件金额，（满多少可使用）
    private Double value=0.0;//优惠券抵现金额    
    private Short useType=0;//使用人身份；0，无限制。1，只会员。
//    private Double getCondition=0.0;//获取条件金额，（满多少可获得）
	private Date beginTime;//开始时间
	private Date outTime;//过期时间
	private String backImgPath;//背景图片
	private Date createTime=new Date();//创建时间
	private Date clockingTime;//定时发放时间
	private Short clockingStatus=0;//0，未进行定时发放，1，已进行定时发放
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Short getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(Short currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
//	public Short getGetType() {
//		return getType;
//	}
//	public void setGetType(Short getType) {
//		this.getType = getType;
//	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	
	public Double getUsecondition() {
		return usecondition;
	}
	public void setUsecondition(Double usecondition) {
		this.usecondition = usecondition;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Short getUseType() {
		return useType;
	}
	public void setUseType(Short useType) {
		this.useType = useType;
	}
//	public List<Brand> getGetBrandlist() {
//		return getBrandlist;
//	}
//	public void setGetBrandlist(List<Brand> getBrandlist) {
//		this.getBrandlist = getBrandlist;
//	}
//	public Double getGetCondition() {
//		return getCondition;
//	}
//	public void setGetCondition(Double getCondition) {
//		this.getCondition = getCondition;
//	}
	public List<Brand> getBrandlist() {
		return brandlist;
	}
	public void setBrandlist(List<Brand> brandlist) {
		this.brandlist = brandlist;
	}
//	public List<ShopProduct> getGetProductlist() {
//		return getProductlist;
//	}
//	public void setGetProductlist(List<ShopProduct> getProductlist) {
//		this.getProductlist = getProductlist;
//	}
	public List<ShopProduct> getProductlist() {
		return productlist;
	}
	public void setProductlist(List<ShopProduct> productlist) {
		this.productlist = productlist;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public String getBackImgPath() {
		return backImgPath;
	}
	public void setBackImgPath(String backImgPath) {
		this.backImgPath = backImgPath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getClockingTime() {
		return clockingTime;
	}
	public void setClockingTime(Date clockingTime) {
		this.clockingTime = clockingTime;
	}
	public Short getClockingStatus() {
		return clockingStatus;
	}
	public void setClockingStatus(Short clockingStatus) {
		this.clockingStatus = clockingStatus;
	}
	
    
    
}
