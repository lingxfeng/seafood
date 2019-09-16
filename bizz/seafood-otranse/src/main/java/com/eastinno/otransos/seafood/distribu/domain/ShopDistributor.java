package com.eastinno.otransos.seafood.distribu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 分销商
 * @author nsz
 *
 */
@Entity(name = "Disco_Shop_Distributor")
public class ShopDistributor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4216697433884194453L;
	@Id
	@TableGenerator(name="initId",initialValue=10000)
    @GeneratedValue(strategy = GenerationType.TABLE, generator="initId")
	private Long id;
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String dePath;//深度路径
	private Integer disType=1;//分销商类型1,普通分销商，2体验店
	@POLoad(name = "parentId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor parent;//父级分销商
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor topDistributor;//所属体验店
	@OneToOne(fetch=FetchType.LAZY)
	private ShopMember member;//关联的会员
	private Integer status=0;//分销商状态
	private Date createDate = new Date();
	private Date joinTime;
	private Double disAmount=0.0;//分销金额
	private Double disCommission=0.0;//佣金余额
	private Double totalCommission=0.0;//佣金累计
	private Double checkCash=0.0;//已提现金额
	private Integer childNum=0;//下级分销商人数
	@Column(length=30)
	private String distributorName;//分销商名称
	@Column(length=30)
	private String myShopName;//店铺名称
	@Column(length=20)
	private String mobile;//分销商手机号
	private String intro;//简介
	@OneToOne(fetch=FetchType.LAZY)
	private ShopDistributorRating shopDistributorRating;//自定义分销商等级
	private Date effectiveTime;//自定义等级有效时间
	@Column(length=100)
	private String qRcodeImg;
	@Column(length=50)
	private String bankCardNum;//银行卡号
	private Integer exStatus=-1;//体验店状态
	private String url;
	private Integer level=1;//层级
	
	@POLoad(name = "area_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private SystemRegion area;//县
	
	private String openAccountAddress;//开店详细地址地址
	private String openAccountName; 
	private String openAccountType;//确认是什么银行 
	private Date openAccountApplyDate;//店铺申请时间
	
	public Date getOpenAccountApplyDate() {
		return openAccountApplyDate;
	}
	public void setOpenAccountApplyDate(Date openAccountApplyDate) {
		this.openAccountApplyDate = openAccountApplyDate;
	}
	public SystemRegion getArea() {
		return area;
	}
	public void setArea(SystemRegion area) {
		this.area = area;
	}
	public String getOpenAccountAddress() {
		return openAccountAddress;
	}
	public void setOpenAccountAddress(String openAccountAddress) {
		this.openAccountAddress = openAccountAddress;
	}
	public String getOpenAccountName() {
		return openAccountName;
	}
	public void setOpenAccountName(String openAccountName) {
		this.openAccountName = openAccountName;
	}
	public ShopDistributorRating getShopDistributorRating() {
		return shopDistributorRating;
	}
	public void setShopDistributorRating(ShopDistributorRating shopDistributorRating) {
		this.shopDistributorRating = shopDistributorRating;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getqRcodeImg() {
		return qRcodeImg;
	}
	public void setqRcodeImg(String qRcodeImg) {
		this.qRcodeImg = qRcodeImg;
	}
	
	public Integer getExStatus() {
		return exStatus;
	}
	public void setExStatus(Integer exStatus) {
		this.exStatus = exStatus;
	}
	public Date getEffectiveTime() {
		return effectiveTime;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getCheckCash() {
		return checkCash;
	}
	public void setCheckCash(Double checkCash) {
		this.checkCash = checkCash;
	}
	public String getDePath() {
		return dePath;
	}
	public void setDePath(String dePath) {
		this.dePath = dePath;
	}
	public Integer getDisType() {
		return disType;
	}
	public void setDisType(Integer disType) {
		this.disType = disType;
	}
	public ShopDistributor getParent() {
		return parent;
	}
	public void setParent(ShopDistributor parent) {
		this.parent = parent;
	}
	public ShopMember getMember() {
		return member;
	}
	
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	public void setMember(ShopMember member) {
		this.member = member;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
	public Double getDisAmount() {
		return disAmount;
	}
	public void setDisAmount(Double disAmount) {
		this.disAmount = disAmount;
	}
	public Double getDisCommission() {
		return disCommission;
	}
	public void setDisCommission(Double disCommission) {
		this.disCommission = disCommission;
	}
	
	public Double getTotalCommission() {
		return totalCommission;
	}
	public void setTotalCommission(Double totalCommission) {
		this.totalCommission = totalCommission;
	}
	public Integer getChildNum() {
		return childNum;
	}
	public void setChildNum(Integer childNum) {
		this.childNum = childNum;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getMyShopName() {
		return myShopName;
	}
	public void setMyShopName(String myShopName) {
		this.myShopName = myShopName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public ShopDistributor getTopDistributor() {
		return topDistributor;
	}
	public void setTopDistributor(ShopDistributor topDistributor) {
		this.topDistributor = topDistributor;
	}
	
	public String getOpenAccountType() {
		return openAccountType;
	}
	public void setOpenAccountType(String openAccountType) {
		this.openAccountType = openAccountType;
	}
	public Double getKtqYe(Double disCommission ,Double checkCash){
		Double ye = this.disCommission - this.checkCash;
		BigDecimal b = new BigDecimal(ye);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
}
