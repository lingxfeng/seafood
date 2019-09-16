package com.eastinno.otransos.seafood.spokesman.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;

/**
 * 补贴记录
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_Subsidy")
public class Subsidy {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private Spokesman level1;//一级补贴的代言人
	@ManyToOne(fetch=FetchType.LAZY)
	private Spokesman level2;//二级补贴的代言人
	@ManyToOne(fetch=FetchType.LAZY)
	private Spokesman level3;//三级补贴的代言人
	private Float restitution1=0F;//一级补贴金额
	private Float restitution2=0F;//二级补贴金额
	private Float restitution3=0F;//三级补贴金额
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo orderInfo;//补贴的订单
	private Date time = new Date();//补贴的时间
	private Short isDispatch=0;//0，未分配1，已分配
	private Float restitution;//补贴金额
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Spokesman getLevel1() {
		return level1;
	}
	public void setLevel1(Spokesman level1) {
		this.level1 = level1;
	}
	public Spokesman getLevel2() {
		return level2;
	}
	public void setLevel2(Spokesman level2) {
		this.level2 = level2;
	}
	public Spokesman getLevel3() {
		return level3;
	}
	public void setLevel3(Spokesman level3) {
		this.level3 = level3;
	}
	public Float getRestitution1() {
		return restitution1;
	}
	public void setRestitution1(Float restitution1) {
		this.restitution1 = restitution1;
	}
	public Float getRestitution2() {
		return restitution2;
	}
	public void setRestitution2(Float restitution2) {
		this.restitution2 = restitution2;
	}
	public Float getRestitution3() {
		return restitution3;
	}
	public void setRestitution3(Float restitution3) {
		this.restitution3 = restitution3;
	}
	
	public ShopOrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(ShopOrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Short getIsDispatch() {
		return isDispatch;
	}
	public void setIsDispatch(Short isDispatch) {
		this.isDispatch = isDispatch;
	}
	public Float getRestitution() {
		return restitution;
	}
	public void setRestitution(Float restitution) {
		this.restitution = restitution;
	}
	
	
}
