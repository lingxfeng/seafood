package com.eastinno.otransos.seafood.promotions.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;

@Entity(name="Disco_Shop_RushBuyRegular")
public class RushBuyRegular {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键
	private String code;	//活动编码
	private String name;	//活动名称
	private int activityType;	//活动类型，0代表秒杀活动，1代表限时抢购活动
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct pro;	//活动对应的商品	
	private Double activityPrice;	//活动商品价格
	private Double previewPrice;	//活动前价格
	private int buyNumLimit = 1;	//购买数量限制，默认1
	private int orderNumLimit = 1;	//购买数量限制，默认1
	private Short isForNew=0;//0，不做限制1,只针对新用户（从未下过单的用户）
	private int buyNum;	//抢购数量
	private Date startDate;	//活动开始时间
	private Date endDate;	//活动结束时间
	private Date shelvingDate;	//上架时间
	private Date unshelvingDate;	//下架时间
	private Date createDate = new Date();	//活动创建时间
	private String shareLink;	//分享链接
	private int expireTime = 900;	//订单过期时间，单位秒s，默认15分钟
	@Transient
	private String state;	//活动状态-创建后，上架后，开始抢购后，结束抢购后，下架后
	private Long pcShowPosition=1L;//电脑端活动显示位置
	private Long phoneShowPosition=1L;//手机端活动显示权重
	
	
	public Long getPcShowPosition() {
		return pcShowPosition;
	}
	public void setPcShowPosition(Long pcShowPosition) {
		this.pcShowPosition = pcShowPosition;
	}
	public Long getPhoneShowPosition() {
		return phoneShowPosition;
	}
	public void setPhoneShowPosition(Long phoneShowPosition) {
		this.phoneShowPosition = phoneShowPosition;
	}
	public Double getPreviewPrice() {
		return previewPrice;
	}
	public void setPreviewPrice(Double previewPrice) {
		this.previewPrice = previewPrice;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getState() {
		Date currDate = new Date();
		if(this.pro.getInventory().compareTo(0) <= 0){
			return "empty";
		}
		if(currDate.after(this.getUnshelvingDate())){
			return "unshelving";
		}else if(currDate.after(this.getEndDate())){
			return "end";			
		}else if(currDate.after(this.getStartDate())){
			return "start";
		}else if(currDate.after(this.getShelvingDate())){
			return "shelving";
		}else if(currDate.after(this.getCreateDate())){
			return "create";
		}else{
			return "error";
		}
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateCN(){
		String stateEN = this.getState();		
		if(stateEN.equals("empty")){
			return "售罄";
		}
		if(stateEN.equals("unshelving")){
			return "已经下架";
		}else if(stateEN.equals("end")){
			return "活动结束";
		}else if(stateEN.equals("start")){
			return "活动进行中";
		}else if(stateEN.equals("shelving")){
			return "已经上架未开始";
		}else if(stateEN.equals("create")){
			return "创建未上架";
		}else if(stateEN.equals("error")){
			return "活动状态ERROR";
		}
		return "ERROR";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	public ShopProduct getPro() {
		return pro;
	}
	public void setPro(ShopProduct pro) {
		this.pro = pro;
	}
	public Double getActivityPrice() {
		return activityPrice;
	}
	public void setActivityPrice(Double activityPrice) {
		this.activityPrice = activityPrice;
	}
	public int getBuyNumLimit() {
		return buyNumLimit;
	}
	public void setBuyNumLimit(int buyNumLimit) {
		this.buyNumLimit = buyNumLimit;
	}
	
	public int getOrderNumLimit() {
		return orderNumLimit;
	}
	public void setOrderNumLimit(int orderNumLimit) {
		this.orderNumLimit = orderNumLimit;
	}
	
	public Short getIsForNew() {
		return isForNew;
	}
	public void setIsForNew(Short isForNew) {
		this.isForNew = isForNew;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getShelvingDate() {
		return shelvingDate;
	}
	public void setShelvingDate(Date shelvingDate) {
		this.shelvingDate = shelvingDate;
	}
	public Date getUnshelvingDate() {
		return unshelvingDate;
	}
	public void setUnshelvingDate(Date unshelvingDate) {
		this.unshelvingDate = unshelvingDate;
	}
	public String getShareLink() {
		return shareLink;
	}
	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}
	public int getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
	
}
