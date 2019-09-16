package com.eastinno.otransos.shop.core.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.security.domain.TenantObject;
/**
 * 系统整体站点配置
 * @author Administrator
 */
@Entity(name = "Disco_Shop_ShopSystemConfig")
public class ShopSystemConfig extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private String name;//店铺名称
	private String siteName;//站点名称
	private String copyrights;//版权信息
	private String logoImg;//logo
	private String topImg;//顶部图片
	private String proDefaultImg;//商品默认图片
	private Integer reseiveDates;//默认收货时间
	@Column(length = 30)
	private String subtitle;//副标题
	private String shopUrl;//店铺介绍url
	private String logoUrl;//店铺logo地址
	private String bUrl;//底部logo地址
	private String shareUrl;//分享图片地址
	private String backgroundUrl;//分销商背景图
	private String introduceUrl;//商家介绍图
	private String sld;//二级域名
	@POLoad(name = "systemRegionId")
	@ManyToOne(fetch=FetchType.LAZY)
	private SystemRegion systemRegion;//地区
	
	private String address;//联系地址
	@Column(length=6)
	@Pattern(regexp = "\\d{6}$")
	private String zip;//邮编
	private String linkman;//联系人
	@Column(length = 11)
    @Pattern(regexp = "(\\+\\d+)?1[34578]\\d{9}$")
	private String mobileTel;//联系电话
	private String cName;//客服姓名
	private String tel;
	@Email()
    @Column(length = 100)
	@Pattern(regexp = "(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+$")
    private String email;
	private String priceTag;//价格标签
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String descripton;//描述
	private String tradingPwd;//交易密码
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String storeList;//门店列表
	private Integer status=1;//店铺状态 0：关闭 1：开启
	private String closeReason;//关闭原因
	private Integer type=1;// 公众号服务 1：认证服务号   2：订阅号
	@Column(unique = true)
    private String weixinOpenId;// 微信
	private Integer autType;//授权类型   1：自定义 2： 微网站
	private String app_id;//微网站App_ID
	private String app_domain_name;//微网站域名
	
	private Integer customerStatus=0;//是否开启 0：关闭 1：开启
	private Integer customerType=1;//客服类型 1：微客服 2：快商通（微网站） 3：快微通
	private String customerShow;//展示页面
	private Double disCondition=0.0;//申请成为分销商条件
	private Integer disCheckType=1;//分销商审核方式
	private Double freight=0.0;//运费
	
	public Double getDisCondition() {
		return disCondition;
	}
	public void setDisCondition(Double disCondition) {
		this.disCondition = disCondition;
	}
	public Integer getDisCheckType() {
		return disCheckType;
	}
	public void setDisCheckType(Integer disCheckType) {
		this.disCheckType = disCheckType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getbUrl() {
		return bUrl;
	}
	public void setbUrl(String bUrl) {
		this.bUrl = bUrl;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getIntroduceUrl() {
		return introduceUrl;
	}
	public void setIntroduceUrl(String introduceUrl) {
		this.introduceUrl = introduceUrl;
	}
	public String getSld() {
		return sld;
	}
	public void setSld(String sld) {
		this.sld = sld;
	}
	public SystemRegion getSystemRegion() {
		return systemRegion;
	}
	public void setSystemRegion(SystemRegion systemRegion) {
		this.systemRegion = systemRegion;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	public Integer getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(Integer customerStatus) {
		this.customerStatus = customerStatus;
	}
	public Integer getCustomerType() {
		return customerType;
	}
	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}
	public String getCustomerShow() {
		return customerShow;
	}
	public void setCustomerShow(String customerShow) {
		this.customerShow = customerShow;
	}
	public String getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPriceTag() {
		return priceTag;
	}
	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}
	public String getDescripton() {
		return descripton;
	}
	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}
	public String getTradingPwd() {
		return tradingPwd;
	}
	public void setTradingPwd(String tradingPwd) {
		this.tradingPwd = tradingPwd;
	}
	public String getStoreList() {
		return storeList;
	}
	public void setStoreList(String storeList) {
		this.storeList = storeList;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCloseReason() {
		return closeReason;
	}
	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getWeixinOpenId() {
		return weixinOpenId;
	}
	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}
	public Integer getAutType() {
		return autType;
	}
	public void setAutType(Integer autType) {
		this.autType = autType;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_domain_name() {
		return app_domain_name;
	}
	public void setApp_domain_name(String app_domain_name) {
		this.app_domain_name = app_domain_name;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getLogoImg() {
		return logoImg;
	}
	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}
	public String getProDefaultImg() {
		return proDefaultImg;
	}
	public void setProDefaultImg(String proDefaultImg) {
		this.proDefaultImg = proDefaultImg;
	}
	public Integer getReseiveDates() {
		return reseiveDates;
	}
	public void setReseiveDates(Integer reseiveDates) {
		this.reseiveDates = reseiveDates;
	}
	public String getCopyrights() {
		return copyrights;
	}
	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}
	public String getTopImg() {
		return topImg;
	}
	public void setTopImg(String topImg) {
		this.topImg = topImg;
	}
	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}
}
