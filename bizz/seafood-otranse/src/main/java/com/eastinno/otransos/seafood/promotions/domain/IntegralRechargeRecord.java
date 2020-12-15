package com.eastinno.otransos.seafood.promotions.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

@Entity(name = "Disco_Shop_IntegralRechargeRecord")
public class IntegralRechargeRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id; // 主键
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopMember member; // 购买人
	private Short type = 0;// 购买方式0，余额， 1，微信支付
	private Short status = 0;// 购买方式0，未支付，1，已支付
	private Date createDate = new Date(); // 积分充值时间
	@Column(length = 50, nullable = false, unique = true)
	private String code;// 订单号
	@Column(length = 100)
	private String uuid;// 发送给第三封支付公司唯一标示
	@Column(length = 50, unique = true)
	private String tradeCode;// 交易流水号
	private Date tradeDate;// 支付时间
	private String tradeTime;
	private Double gross_price = 0.0;// 订单总价格
	private Long integral = 0L;// 积分数量
	@POLoad(name = "payconfigId")
	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentConfig payConfig;
	@Column(length = 10)
	private String payType;// 支付类型；在线支付：online

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

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getTypeCN() {
		String typeCN = "";
		if (this.getType() == 0) {
			typeCN = "退款余额支付";
		} else if (this.getType() == 2) {
			typeCN = "佣金余额支付";
		} else if (this.getType() == 1) {
			typeCN = "微信支付";
		} else {
			typeCN = "未知支付类型";
		}
		return typeCN;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getStatusCN() {
		String statusCN = "";
		if (this.getStatus() == 0) {
			statusCN = "充值失败";
		} else if (this.getStatus() == 1) {
			statusCN = "充值成功";
		}
		return statusCN;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Double getGross_price() {
		return gross_price;
	}

	public void setGross_price(Double gross_price) {
		this.gross_price = gross_price;
	}

	public Long getIntegral() {
		return integral;
	}

	public void setIntegral(Long integral) {
		this.integral = integral;
	}

	public PaymentConfig getPayConfig() {
		return payConfig;
	}

	public void setPayConfig(PaymentConfig payConfig) {
		this.payConfig = payConfig;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
