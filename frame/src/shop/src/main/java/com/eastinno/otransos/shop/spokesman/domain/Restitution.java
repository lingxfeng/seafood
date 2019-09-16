package com.eastinno.otransos.shop.spokesman.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;

/**
 * 返还记录
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_Restitution")
public class Restitution {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private Spokesman spokesman;//返现的代言人
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo order;//返现的订单
	private Date time = new Date();//返现的时间
	private Float restitution;//返现金额
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Spokesman getSpokesman() {
		return spokesman;
	}
	public void setSpokesman(Spokesman spokesman) {
		this.spokesman = spokesman;
	}
	
	public ShopOrderInfo getOrder() {
		return order;
	}
	public void setOrder(ShopOrderInfo order) {
		this.order = order;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Float getRestitution() {
		return restitution;
	}
	public void setRestitution(Float restitution) {
		this.restitution = restitution;
	}
	
	
}
