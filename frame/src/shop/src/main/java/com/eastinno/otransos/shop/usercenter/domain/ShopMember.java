package com.eastinno.otransos.shop.usercenter.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.droduct.domain.Couponlist;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;

/**
 * 会员管理
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopMember")
public class ShopMember extends User{
	private Short userStatus=0;//用户状态：0.已注册未验证，1.已注册并验证成功，2.已禁用，3.已申请商户入驻，4.已成为商户
	@OneToOne(fetch=FetchType.LAZY)
	private Follower follower;	
	private Long availableIntegral=0L; // 可用积分
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String dePath;
	private Long totalIntegral=0L; //总积分
	@OneToOne(fetch=FetchType.LAZY)
	private SystemRegion area;	//用户所在地区

	@Transient
	private Integer userRating=3; // 1:金牌用户  2：银牌用户 3：铜牌用户
	@Transient
	private String userRatingName = "";
	@Transient
	private Integer securityLevel; //安全等级 1:低 2：中 3：高
	@ManyToMany(fetch=FetchType.LAZY)
	private List<ShopProduct> myCollections = new ArrayList<ShopProduct>();
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<Couponlist> couponlists = new ArrayList<Couponlist>();
	private Double remainderAmt=0.0;//余额
	private Double disCommission=0.0;//累计佣金
	@Transient
	private String menusStr;
	@POLoad(name = "pmemberId")
	@OneToOne(fetch=FetchType.LAZY)
	private ShopMember pmember;//通过谁推荐注册的、
	private Double consumptionAmount=0.0; //消费总金额
	private Integer disType=0;
	@OneToOne(fetch=FetchType.LAZY)
	private ShopDistributor distributor;//所属微店
	@OneToOne(fetch=FetchType.LAZY,mappedBy="member")
	private ShopDistributor myDistributor;//我的微店
	
	private String oldUserStatus; //0：未绑定 1：已绑定
	
	@OneToOne(fetch=FetchType.LAZY,mappedBy="member")
	private Spokesman mySpokesman;//我的代言人功能
	@OneToMany(fetch=FetchType.LAZY,mappedBy="shopMember")
	private List<CustomCoupon> customCoupon = new ArrayList<CustomCoupon>();//我的代言
	
	public String getOldUserStatus() {
		return oldUserStatus;
	}

	public void setOldUserStatus(String oldUserStatus) {
		this.oldUserStatus = oldUserStatus;
	}

	public SystemRegion getArea() {
		return area;
	}

	public void setArea(SystemRegion area) {
		this.area = area;
	}
	
	public ShopMember getPmember() {
		return pmember;
	}

	public void setPmember(ShopMember pmember) {
		this.pmember = pmember;
	}

	public Long getAvailableIntegral() {
		return availableIntegral;
	}

	public void setAvailableIntegral(Long availableIntegral) {
		this.availableIntegral = availableIntegral;
	}

	public String getUserRatingName() {
		return userRatingName;
	}

	public void setUserRatingName(String userRatingName) {
		this.userRatingName = userRatingName;
	}
	
	public Long getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(Long totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public Integer getUserRating() {
		return userRating;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

	public Short getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Short userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(Integer securityLevel) {
		this.securityLevel = securityLevel;
	}

	public List<ShopProduct> getMyCollections() {
		return myCollections;
	}

	public void setMyCollections(List<ShopProduct> myCollections) {
		this.myCollections = myCollections;
	}

	public Follower getFollower() {
		return follower;
	}

	public void setFollower(Follower follower) {
		this.follower = follower;
	}
	
	public Integer getDisType() {
		return disType;
	}

	public void setDisType(Integer disType) {
		this.disType = disType;
	}

	public ShopDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(ShopDistributor distributor) {
		this.distributor = distributor;
	}

	public ShopDistributor getMyDistributor() {
		return myDistributor;
	}

	public void setMyDistributor(ShopDistributor myDistributor) {
		this.myDistributor = myDistributor;
	}

	public List<Couponlist> getCouponlists() {
		return couponlists;
	}

	public void setCouponlists(List<Couponlist> couponlists) {
		this.couponlists = couponlists;
	}

	public String getMenusStr() {
		return menusStr;
	}

	public void setMenusStr(String menusStr) {
		this.menusStr = menusStr;
	}

	public Double getRemainderAmt() {
		return remainderAmt;
	}

	public void setRemainderAmt(Double remainderAmt) {
		this.remainderAmt = remainderAmt;
	}
	
	
	public Double getDisCommission() {
		return disCommission;
	}

	public void setDisCommission(Double disCommission) {
		this.disCommission = disCommission;
	}

	public Double getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(Double consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}
	
	public String getDePath() {
		return dePath;
	}

	public void setDePath(String dePath) {
		this.dePath = dePath;
	}
	
	public Spokesman getMySpokesman() {
		return mySpokesman;
	}

	public void setMySpokesman(Spokesman mySpokesman) {
		this.mySpokesman = mySpokesman;
	}
	
	public List<CustomCoupon> getCustomCoupon() {
		return customCoupon;
	}

	public void setCustomCoupon(List<CustomCoupon> customCoupon) {
		this.customCoupon = customCoupon;
	}

	public boolean addCollections(ShopProduct shopProduct) {
        boolean iscontain = true;
        for (ShopProduct sProduct : this.myCollections) {
            if (sProduct.getId().equals(shopProduct.getId())) {
                iscontain = false;
            }
        }
        if (iscontain) {
            this.myCollections.add(shopProduct);
        }
        return iscontain;
    }
}
