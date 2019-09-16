package com.eastinno.otransos.shop.trade.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.shop.content.domain.ShopDiscuss;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

/**
 * 订单明细
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopOrderdetail")
public class ShopOrderdetail extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Double unit_price;//购买时单价
	@POLoad(name="product_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct pro;//购买的商品
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopSpec shopSpec;//购买的规格
	private Integer num;//购买数量
	private Double gross_price;//子订单总价格
	private Integer status=0;//自订单状态，-1已取消退货，-2用户申请退货，-3.退货中，-4不同意退货，-5退换货完成
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//买家
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor seller;//商家
	private Date ceateDate= new Date();//下单时间
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopOrderInfo orderInfo;
	@Column(length=50,nullable=false)
	private String code;//订单号
	@Column(length=50,unique=true)
	private String tradeCode;//交易流水号
	@ManyToOne(fetch=FetchType.LAZY)
	private PaymentConfig payConfig;
	private Date tradeDate;//交易时间
	private Boolean isPointBuy=false;//是否用积分购买
	private Integer type=1;//1普通订单，2其他订单
	
	@OneToOne(fetch=FetchType.LAZY)
	private ShopDiscuss shopDiscuss;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public ShopProduct getPro() {
		return pro;
	}
	public void setPro(ShopProduct pro) {
		this.pro = pro;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getGross_price() {
		return gross_price;
	}
	public void setGross_price(Double gross_price) {
		this.gross_price = gross_price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}
	
	public ShopDistributor getSeller() {
		return seller;
	}
	public void setSeller(ShopDistributor seller) {
		this.seller = seller;
	}
	public Date getCeateDate() {
		return ceateDate;
	}
	public void setCeateDate(Date ceateDate) {
		this.ceateDate = ceateDate;
	}
	public ShopOrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(ShopOrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public PaymentConfig getPayConfig() {
		return payConfig;
	}
	public void setPayConfig(PaymentConfig payConfig) {
		this.payConfig = payConfig;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public Boolean getIsPointBuy() {
		return isPointBuy;
	}
	public void setIsPointBuy(Boolean isPointBuy) {
		this.isPointBuy = isPointBuy;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public ShopSpec getShopSpec() {
		return shopSpec;
	}
	public void setShopSpec(ShopSpec shopSpec) {
		this.shopSpec = shopSpec;
	}
	public ShopDiscuss getShopDiscuss() {
		return shopDiscuss;
	}
	public void setShopDiscuss(ShopDiscuss shopDiscuss) {
		this.shopDiscuss = shopDiscuss;
	}
	
}
