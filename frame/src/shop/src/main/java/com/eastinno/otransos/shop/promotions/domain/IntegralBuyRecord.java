package com.eastinno.otransos.shop.promotions.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

@Entity(name="Disco_Shop_IntegralBuyRecord")
public class IntegralBuyRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember member;	//购买人
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo order;	//购买订单
	@ManyToOne(fetch=FetchType.LAZY)
	private IntegralBuyRegular regular;	//积分活动商品
	private Date createDate = new Date();	//积分购物时间
	private String ipAddress;	//积分购物的访问IP地址
	private String recordState;	//用来说明该记录的状态信息，比如正常、异常
	
	public IntegralBuyRegular getRegular() {
		return regular;
	}
	public void setRegular(IntegralBuyRegular regular) {
		this.regular = regular;
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
