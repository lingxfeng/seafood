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
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

@Entity(name="Disco_Shop_RushBuyRecord")
public class RushBuyRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember member;	//抢购人	
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo order;	//抢购订单
	@ManyToOne(fetch=FetchType.LAZY)
	private RushBuyRegular regular;	//抢购活动
	private Date createDate = new Date();	//获取抢购资格的时间
	private String ipAddress;	//获取抢购资格的访问IP地址
	private String recordState;	//用来说明该记录的状态信息，比如正常、异常
	@Transient
	private boolean outExpire = false;	//订单是否过期
	
	public boolean isOutExpire() {		
		long second = (new Date().getTime()-this.getCreateDate().getTime())/1000;
		if(second < this.getRegular().getExpireTime()){
			return false;
		}else{
			return true;	
		}		
	}
	public void setOutExpire(boolean outExpire) {
		this.outExpire = outExpire;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getMember() {
		return member;
	}
	public void setMember(ShopMember member) {
		this.member = member;
	}
	public ShopOrderInfo getOrder() {
		return order;
	}
	public void setOrder(ShopOrderInfo order) {
		this.order = order;
	}
	public RushBuyRegular getRegular() {
		return regular;
	}
	public void setRegular(RushBuyRegular regular) {
		this.regular = regular;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
}
