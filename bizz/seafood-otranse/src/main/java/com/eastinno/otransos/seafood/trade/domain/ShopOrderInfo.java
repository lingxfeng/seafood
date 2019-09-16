package com.eastinno.otransos.seafood.trade.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.promotions.domain.CustomCoupon;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.security.domain.TenantObject;
/**
 * 订单管理
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopOrderInfo")
public class ShopOrderInfo extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length=100)
	private String name;
	@Column(length=100)
	private String orderDesc;
	private Double gross_price=0.0;//订单总价格
	private Double product_price=0.0;//商品总价格
	private Double freight;//运费
	private Integer status=0;//订单状态:0未支付 ，1已支付待发货，2商家已发货，3用户已收货，-1已取消订单,4申请退货，5已同意退货，6:不同意退货
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;//买家
	private Date ceateDate= new Date();//下单时间
	private Date sendDate;//发货时间
	private Date receiveDate;//收货时间
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor seller;//商家
	@OneToMany(mappedBy="orderInfo",fetch=FetchType.LAZY)
	private List<ShopOrderdetail> orderdetails = new ArrayList<ShopOrderdetail>();
	
	@Column(length=50,nullable=false,unique=true)
	private String code;//订单号
	@Column(length=100)
	private String uuid;//发送给第三封支付公司唯一标示
	@Column(length=50,unique=true)
	private String tradeCode;//交易流水号
	private Date tradeDate;//支付时间
	private String tradeTime;
	@Column(length=100)
	private String unuuid;//发送给第三封支付公司唯一标示
	@Column(length=50,unique=true)
	private String untradeCode;//交易流水号
	private String untradeTime;
	@POLoad(name="payconfigId")
	@ManyToOne(fetch=FetchType.LAZY)
	private PaymentConfig payConfig;
	@Column(length=10)
	private String payType;//支付类型；在线支付：online
	//TODO 数据字典
	private Integer invoiceType;//发票类型
	@Column(length=100)
	private String invoice;//发票抬头
	private String msg_self;//用户留言
	@POLoad(name="addr_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopAddress addr;//发送地址
	@Column(length=50)
	private String expressCode;//快递单号
	@Column(length=50)
	private String expressNote;//快递备注
	@OneToOne(fetch=FetchType.LAZY)
	private LogisticsCompany logisticsCompany;//快递公司名称
	@POLoad(name="distributorId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor distributor;
	private Boolean isDisAmtEnd=false;//佣金是否已结算
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDistributor topDistributor;//所属实体店
	
	private Short isSpokesman=0;//0,不是代言订单1，是直接取走的代言订单2，商城代卖的代言订单
	private Short finishRestitution=-1;//0,未完成返现1,已结束返现，-1，未进行返现
	private Short finishSubsidy=-1;//0,未完成补贴1,已结束补贴，-1，未进行补贴
	private Integer restitutionCount=0;//返还次数
	private Integer subsidyCount=0;//补贴 次数 
	private Short isCalculate=0;//0,本次未计算1，本次已计算
	private Integer totalMonths;//本次订单返还月数
	private Float restitution=0F;//本次订单每月最高返还金额
	
	@OneToOne(fetch=FetchType.LAZY)
	private CustomCoupon myCoupon;//使用优惠券
	private Double coupon_price=0.0;//优惠券金额
	
	private String type = "normal";	//订单类型，"normal"->普通订单，"integral"->积分购买订单，"seckill"->秒杀订单，"timelimit"->限时抢购订单
	
	//配送时间
	private String delivery_time_info;
	
	private Short orderType=1;//1:pc订单 2：微信订单
	private Double balancePay;//余额支付金额
	
	@POLoad(name="ShopSinceSomeId")
	@OneToOne(fetch=FetchType.LAZY)
	private ShopSinceSome ShopSinceSome;
	
	public String getDelivery_time_info() {
		return delivery_time_info;
	}

	public void setDelivery_time_info(String delivery_time_info) {
		this.delivery_time_info = delivery_time_info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}
	
	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	
	
	public String getExpressNote() {
		return expressNote;
	}

	public void setExpressNote(String expressNote) {
		this.expressNote = expressNote;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Double getGross_price() {
		return gross_price;
	}
	public void setGross_price(Double gross_price) {
		this.gross_price = gross_price;
	}
	
	

	public LogisticsCompany getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(LogisticsCompany logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
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
	public Date getCeateDate() {
		return ceateDate;
	}
	public void setCeateDate(Date ceateDate) {
		this.ceateDate = ceateDate;
	}
	
	public ShopDistributor getSeller() {
		return seller;
	}
	
	public Boolean getIsDisAmtEnd() {
		return isDisAmtEnd;
	}

	public void setIsDisAmtEnd(Boolean isDisAmtEnd) {
		this.isDisAmtEnd = isDisAmtEnd;
	}

	public void setSeller(ShopDistributor seller) {
		this.seller = seller;
	}

	public List<ShopOrderdetail> getOrderdetails() {
		return orderdetails;
	}
	public void setOrderdetails(List<ShopOrderdetail> orderdetails) {
		this.orderdetails = orderdetails;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getMsg_self() {
		return msg_self;
	}
	public void setMsg_self(String msg_self) {
		this.msg_self = msg_self;
	}
	public ShopAddress getAddr() {
		return addr;
	}
	public void setAddr(ShopAddress addr) {
		this.addr = addr;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	public String getUnuuid() {
		return unuuid;
	}
	public void setUnuuid(String unuuid) {
		this.unuuid = unuuid;
	}
	public String getUntradeCode() {
		return untradeCode;
	}
	public void setUntradeCode(String untradeCode) {
		this.untradeCode = untradeCode;
	}
	public String getUntradeTime() {
		return untradeTime;
	}
	public void setUntradeTime(String untradeTime) {
		this.untradeTime = untradeTime;
	}
	
	public ShopDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(ShopDistributor distributor) {
		this.distributor = distributor;
	}
	

	public ShopDistributor getTopDistributor() {
		return topDistributor;
	}

	public void setTopDistributor(ShopDistributor topDistributor) {
		this.topDistributor = topDistributor;
	}
	
	public Short getIsSpokesman() {
		return isSpokesman;
	}

	public void setIsSpokesman(Short isSpokesman) {
		this.isSpokesman = isSpokesman;
	}

	public Short getFinishRestitution() {
		return finishRestitution;
	}

	public void setFinishRestitution(Short finishRestitution) {
		this.finishRestitution = finishRestitution;
	}
	
	public Short getFinishSubsidy() {
		return finishSubsidy;
	}

	public void setFinishSubsidy(Short finishSubsidy) {
		this.finishSubsidy = finishSubsidy;
	}
	
	public Integer getRestitutionCount() {
		return restitutionCount;
	}

	public void setRestitutionCount(Integer restitutionCount) {
		this.restitutionCount = restitutionCount;
	}

	public Integer getSubsidyCount() {
		return subsidyCount;
	}

	public void setSubsidyCount(Integer subsidyCount) {
		this.subsidyCount = subsidyCount;
	}
	
	public Short getIsCalculate() {
		return isCalculate;
	}

	public void setIsCalculate(Short isCalculate) {
		this.isCalculate = isCalculate;
	}
	
	public Double getProduct_price() {
		return product_price;
	}

	public void setProduct_price(Double product_price) {
		this.product_price = product_price;
	}
	
	public Integer getTotalMonths() {
		return totalMonths;
	}

	public void setTotalMonths(Integer totalMonths) {
		this.totalMonths = totalMonths;
	}

	public Float getRestitution() {
		return restitution;
	}

	public void setRestitution(Float restitution) {
		this.restitution = restitution;
	}
	
	public CustomCoupon getMyCoupon() {
		return myCoupon;
	}

	public void setMyCoupon(CustomCoupon myCoupon) {
		this.myCoupon = myCoupon;
	}
	
	public Double getCoupon_price() {
		return coupon_price;
	}

	public void setCoupon_price(Double coupon_price) {
		this.coupon_price = coupon_price;
	}

	public String getStatusCh(){
		String statusCh="";
		if(this.status==0){
			statusCh="未支付";
		}else if(this.status==1){
			statusCh = "已支付";
		}else if(this.status==2){
			statusCh = "已发货";
		}else if(this.status==3){
			statusCh = "已收货";
		}else if(this.status==4){
			statusCh = "已申请退货";
		}else if(this.status==5){
			statusCh = "退货完成";
		}else if(this.status==6){
			statusCh = "退货不成功";
		}else if(this.status==-1){
			statusCh="已取消";
		}
		return statusCh;
	}

	public Short getOrderType() {
		return orderType;
	}

	public void setOrderType(Short orderType) {
		this.orderType = orderType;
	}

	public Double getBalancePay() {
		return balancePay;
	}

	public void setBalancePay(Double balancePay) {
		this.balancePay = balancePay;
	}

	public ShopSinceSome getShopSinceSome() {
		return ShopSinceSome;
	}

	public void setShopSinceSome(ShopSinceSome shopSinceSome) {
		ShopSinceSome = shopSinceSome;
	}
	
}
