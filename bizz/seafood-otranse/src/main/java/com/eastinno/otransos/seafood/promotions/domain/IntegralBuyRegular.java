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

@Entity(name="Disco_Shop_IntegralBuyRegular")
public class IntegralBuyRegular {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键
	private String code;	//积分活动编码
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct pro;	//活动对应的商品	
	private Long integralPrice;	//活动商品价格
	private int buyNumLimit = 1;	//购买数量限制，默认1
	private Date shelvingDate;	//上架时间
	private Date unshelvingDate;	//下架时间
	private Date createDate = new Date();	//活动创建时间
	private Boolean isRecmmend = false;	//推荐与否	
	private Long weight = 0L;	//显示权重,默认是记录的创建时间
	@Transient
	private String state;	//积分商品状态-创建后，上架后，开始抢购后，结束抢购后，下架后
	
	public Long getIntegralPrice() {
		return integralPrice;
	}
	public void setIntegralPrice(Long integralPrice) {
		this.integralPrice = integralPrice;
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
		}else if(stateEN.equals("shelving")){
			return "已经上架";
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopProduct getPro() {
		return pro;
	}
	public void setPro(ShopProduct pro) {
		this.pro = pro;
	}
	public int getBuyNumLimit() {
		return buyNumLimit;
	}
	public void setBuyNumLimit(int buyNumLimit) {
		this.buyNumLimit = buyNumLimit;
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
	public Boolean getIsRecmmend() {
		return isRecmmend;
	}
	public void setIsRecmmend(Boolean isRecmmend) {
		this.isRecmmend = isRecmmend;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
}
