package com.eastinno.otransos.seafood.trade.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

/**
 * 申请退款
 * @author Administrator
 *
 */
@Entity(name="Disco_Shop_ApplyRefund")
public class ApplyRefund {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Date createDate = new Date();//申请退款时间
	private String reason; //退款原因
	private Short status=0;//1：同意 2：拒绝
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember shopMember;
	@OneToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo order;
	private Date auditTime;
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;//退款人
	private Double freight;//退款自定义运费
	private Double sums;//退款金额
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String remarks;//备注
	
	
	public Double getSums() {
		return sums;
	}
	public void setSums(Double sums) {
		this.sums = sums;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public ShopMember getShopMember() {
		return shopMember;
	}
	public void setShopMember(ShopMember shopMember) {
		this.shopMember = shopMember;
	}
	public ShopOrderInfo getOrder() {
		return order;
	}
	public void setOrder(ShopOrderInfo order) {
		this.order = order;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
