package com.eastinno.otransos.seafood.usercenter.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

/**
 * 申请提取现金记录
 * @author cl
 *
 */
@Entity(name="Disco_Shop_ApplyWithdrawCash")
public class ApplyWithdrawCash {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember shopMember;//申请人
	private String uuid;
	@ManyToOne(fetch=FetchType.LAZY)
	private User auditor;//退款审核人
	private Date auditTime;//审核时间
	private Double sums;//提款金额
	private Short status=0;//审核状态 1：同意 2：拒绝
	private String comment; //备注信息
	private Date createDate = new Date();//申请提取现金时间
	
	private Short type=0;//0：默认提款到微信零钱 1：提款到网银
	private String openAccountName; //开户人名称
	private String bankCardNum;//银行卡号
	private String openAccountAddress;//开户人详细地址
	
	private Double balance;//每次提取后余额
	
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getOpenAccountName() {
		return openAccountName;
	}
	public void setOpenAccountName(String openAccountName) {
		this.openAccountName = openAccountName;
	}
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	public String getOpenAccountAddress() {
		return openAccountAddress;
	}
	public void setOpenAccountAddress(String openAccountAddress) {
		this.openAccountAddress = openAccountAddress;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getShopMember() {
		return shopMember;
	}
	public void setShopMember(ShopMember shopMember) {
		this.shopMember = shopMember;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public User getAuditor() {
		return auditor;
	}
	public void setAuditor(User auditor) {
		this.auditor = auditor;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Double getSums() {
		return sums;
	}
	public void setSums(Double sums) {
		this.sums = sums;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
