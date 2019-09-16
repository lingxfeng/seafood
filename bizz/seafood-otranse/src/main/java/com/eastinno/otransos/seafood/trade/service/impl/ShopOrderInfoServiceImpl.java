
package com.eastinno.otransos.seafood.trade.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.domain.CommissionDetail;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.ICommissionConfigService;
import com.eastinno.otransos.seafood.distribu.service.ICommissionDetailService;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.seafood.trade.dao.IShopOrderInfoDAO;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.seafood.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopOrderInfoServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class ShopOrderInfoServiceImpl implements IShopOrderInfoService {
	@Resource
	private IShopOrderInfoDAO shopOrderInfoDao;
	@Autowired
	private IShopDistributorService shopDistributorService;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
	private IShopMemberService shopMemberService;
	@Autowired
	private ICommissionDetailService commissionDetailService;
	@Autowired
	private ICommissionConfigService commissionConfigService;
	@Autowired
	private ISpokesmanService spokesmanService;
	@Autowired
	private ISpokesmanRatingService spokesmanRatingService;
	@Autowired
	private IRemainderAmtHistoryService remainderAmtHistoryService;

	public void setShopOrderInfoDao(IShopOrderInfoDAO shopOrderInfoDao) {
		this.shopOrderInfoDao = shopOrderInfoDao;
	}

	public Long addShopOrderInfo(ShopOrderInfo shopOrderInfo) {
		this.shopOrderInfoDao.save(shopOrderInfo);
		if (shopOrderInfo != null && shopOrderInfo.getId() != null) {
			return shopOrderInfo.getId();
		}
		return null;
	}

	public ShopOrderInfo getShopOrderInfo(Long id) {
		ShopOrderInfo shopOrderInfo = this.shopOrderInfoDao.get(id);
		return shopOrderInfo;
	}

	public ICommissionDetailService getCommissionDetailService() {
		return commissionDetailService;
	}

	public void setCommissionDetailService(ICommissionDetailService commissionDetailService) {
		this.commissionDetailService = commissionDetailService;
	}

	public ISpokesmanService getSpokesmanService() {
		return spokesmanService;
	}

	public void setSpokesmanService(ISpokesmanService spokesmanService) {
		this.spokesmanService = spokesmanService;
	}

	public ISpokesmanRatingService getSpokesmanRatingService() {
		return spokesmanRatingService;
	}

	public void setSpokesmanRatingService(ISpokesmanRatingService spokesmanRatingService) {
		this.spokesmanRatingService = spokesmanRatingService;
	}

	public boolean delShopOrderInfo(Long id) {
		ShopOrderInfo shopOrderInfo = this.getShopOrderInfo(id);
		if (shopOrderInfo != null) {
			this.shopOrderInfoDao.remove(id);
			return true;
		}
		return false;
	}

	public boolean batchDelShopOrderInfos(List<Serializable> shopOrderInfoIds) {

		for (Serializable id : shopOrderInfoIds) {
			delShopOrderInfo((Long) id);
		}
		return true;
	}

	public IPageList getShopOrderInfoBy(IQueryObject queryObj) {
		return this.shopOrderInfoDao.findBy(queryObj);
	}

	public boolean updateShopOrderInfo(Long id, ShopOrderInfo shopOrderInfo) {
		if (id != null) {
			shopOrderInfo.setId(id);
		} else {
			return false;
		}
		this.shopOrderInfoDao.update(shopOrderInfo);
		return true;
	}

	@Override
	public List<ShopOrderInfo> getOrderByStatus(int status, int num) {
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user", user, "=");
		qo.addQuery("obj.status", Integer.parseInt(status + ""), "=");
		qo.setPageSize(num);
		List<ShopOrderInfo> list = this.getShopOrderInfoBy(qo).getResult();
		return list;
	}

	// demo
	public String topay() {
		PayParamsObj param = new PayParamsObj();
		param.setIp("120.123.1.3");// 客户端ip，不可为空
		param.setOrderDesc("该订单是购买手机的电单");// 可以为空
		param.setOrderId("23245343");// 订单号，唯一，不可为空
		param.setOrderName("。。。购买");// 不可为空
		param.setTotal_fee("0.01");// 订单金额，元不可为空
		param.setUserCode("weixinopenId");// 退款和批量使用，如果是支付可以为空
		PaymentConfig config = new PaymentConfig();// 可以根据前台传的id获取,IPaymentConfigService.get...();
		param.setPayConfig(config);
		String pagestr = PaymentUtil.paysubmitStr(param);
		return pagestr;
	}

	/**
	 * 计算佣金（海鲜之家）
	 */
	public void disTributorAmt(ShopOrderInfo order) {
		if (!order.getIsDisAmtEnd() && order.getType().equals("normal")) {
			order.setIsDisAmtEnd(true);// 确认已分配完成
			ShopDistributor distributora = order.getDistributor();// 当前分销商
			ShopDistributor topDistributor = null;// 所属实体店
			ShopMember user = order.getUser();// 消费者

			ShopDistributor mydis = user.getMyDistributor();

			ShopDistributor distributorb = null;// 上级分销商
			ShopMember membera = null;
			ShopMember memberb = null;
			ShopMember memberc = null;

			Double disCommissiona = 0.0;// 本级分销佣金
			Double disCommissionb = 0.0;// 上级分销佣金
			Double topDisCommission = 0.0;// 实体店利润

			if (distributora != null) {
				membera = distributora.getMember();
				// 累计分销金额(直接分销金额)
				BigDecimal orderprice = new BigDecimal(order.getProduct_price());
				BigDecimal beforeCountdis = new BigDecimal(membera.getConsumptionDistri());
				double countdis = orderprice.add(beforeCountdis).doubleValue();
				membera.setConsumptionDistri(countdis);

				if (distributora.getParent() != distributora) {
					distributorb = distributora.getParent();// 上级分销商
				}
				if (distributorb != null) {
					memberb = distributorb.getMember();
				}
				topDistributor = distributora.getTopDistributor();// 获取所属实体店
				if (topDistributor != null) {
					memberc = topDistributor.getMember();
				}

				List<ShopOrderdetail> orderdetails = order.getOrderdetails();
				for (ShopOrderdetail orderdetail : orderdetails) {
					ShopProduct pro = orderdetail.getPro();
					ShopSpec spec = orderdetail.getShopSpec();
					int num = orderdetail.getNum();

					BigDecimal s_amt = null;
					BigDecimal s_TydAmt = null;
					BigDecimal s_Store_price = null;
					BigDecimal p_amt = null;
					BigDecimal p_TydAmt = null;
					BigDecimal p_Store_price = null;

					if (spec != null) {
						s_amt = new BigDecimal(spec.getAmt());
						s_TydAmt = new BigDecimal(spec.getTydAmt());
						s_Store_price = new BigDecimal(spec.getStore_price());
					} else {
						p_amt = new BigDecimal(pro.getAmt());
						p_TydAmt = new BigDecimal(pro.getTydAmt());
						p_Store_price = new BigDecimal(pro.getStore_price());
					}

					if (mydis != null) {// 我不是会员
						if (distributora.getExStatus() != 1 && distributora.getStatus() == 1) {// 我是微店
							if (topDistributor != null) {
								if (spec != null) {
									topDisCommission += (s_Store_price.subtract(s_TydAmt).doubleValue()) * num; // 加盟店返现
								} else {
									topDisCommission += (p_Store_price.subtract(p_TydAmt).doubleValue()) * num;
								}
							}
							disCommissiona += (commissionConfigService
									.getCommissionConfigByProductId(orderdetail.getPro(), 1)) * num;// 一级佣金

							if (distributorb != null) {
								if (distributorb.getExStatus() != 1) {
									disCommissionb += (commissionConfigService
											.getCommissionConfigByProductId(orderdetail.getPro(), 2)) * num;// 二级佣金
								}
							}

						}
					} else {// 我是会员
						if (distributora.getExStatus() == 1) {// 直属于加盟店
							if (spec != null) {
								topDisCommission += (s_amt.subtract(s_TydAmt).doubleValue()) * num;// 如果是体验店用零售价减体验店价格
							} else {
								topDisCommission += (p_amt.subtract(p_TydAmt).doubleValue()) * num;// 如果是体验店用零售价减体验店价格
							}

						} else if (distributora.getExStatus() != 1 && distributora.getStatus() == 1) {// 有上级微店
							disCommissiona += (commissionConfigService
									.getCommissionConfigByProductId(orderdetail.getPro(), 1)) * num;//
							if (topDistributor != null) {
								if (spec != null) {
									topDisCommission += (s_Store_price.subtract(s_TydAmt).doubleValue()) * num;
								} else {
									topDisCommission += (p_Store_price.subtract(p_TydAmt).doubleValue()) * num;
								}

							}
						}
						if (distributorb != null) {
							if (distributorb.getExStatus() != 1) {
								disCommissionb += (commissionConfigService
										.getCommissionConfigByProductId(orderdetail.getPro(), 2)) * num;
							}
						}
					}
				}

				/**
				 * 2019.8.21去掉原来用佣金减去优惠券的逻辑
				 */
				// if (disCommissiona != 0) {
				// BigDecimal Coupon_price = new
				// BigDecimal(order.getCoupon_price());
				// BigDecimal disCommissiona2 = new BigDecimal(disCommissiona);
				// disCommissiona =
				// disCommissiona2.subtract(Coupon_price).doubleValue();
				// }

				if (disCommissiona != 0 || disCommissionb != 0 || topDisCommission != 0) {
					// 增加佣金记录
					CommissionDetail cd = new CommissionDetail();
					cd.setOrder(order);
					cd.setUser(user);
					if (disCommissiona != 0) {
						BigDecimal b = new BigDecimal(disCommissiona);
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						cd.setBalanceonedistri(distributora);
						cd.setBalanceone(f1);
						this.remainderAmtHistoryService.addRemainderAmtHistory(membera, 5, f1, "一级佣金获取");

					}
					if (disCommissionb != 0) {
						BigDecimal b = new BigDecimal(disCommissionb);
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						cd.setLevelonedistri(distributorb);
						cd.setLevelonecommission(f1);
						this.remainderAmtHistoryService.addRemainderAmtHistory(memberb, 5, f1, "二级佣金获取");
					}

					if (topDisCommission != 0) {
						BigDecimal b = new BigDecimal(topDisCommission);
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						cd.setBalancetowdistri(topDistributor);
						cd.setBalancetow(f1);
						if (memberc != null) {
							cd.setBalancetowdistri(topDistributor);
							cd.setBalancetow(f1);
							this.remainderAmtHistoryService.addRemainderAmtHistory(memberc, 5, f1, "加盟店利润获取");
						} else {
							cd.setBalancetowdistri(distributora);
							cd.setBalancetow(f1);
							this.remainderAmtHistoryService.addRemainderAmtHistory(membera, 5, f1, "加盟店利润获取");
						}

					}
					this.commissionDetailService.addCommissionDetail(cd);
				}

				// 转为精确数字，计算用户表中的分销佣金和余额
				BigDecimal disCommissiona_ = new BigDecimal(disCommissiona);
				BigDecimal disCommissionb_ = new BigDecimal(disCommissionb);
				BigDecimal topDisCommission_ = new BigDecimal(topDisCommission);

				// 一级利润的佣金及余额
				BigDecimal m_disCommission = new BigDecimal(membera.getDisCommission());
				BigDecimal m_remainderAmt = new BigDecimal(membera.getRemainderAmt());
				if (disCommissiona != 0) {
					membera.setDisCommission(disCommissiona_.add(m_disCommission).doubleValue());
					membera.setRemainderAmt(disCommissiona_.add(m_remainderAmt).doubleValue());
					this.shopMemberService.updateShopMember(membera.getId(), membera);
				}

				if (disCommissionb != 0 && distributorb != null) {
					// 一级佣金的佣金及余额
					BigDecimal mb_disCommission = new BigDecimal(memberb.getDisCommission());
					BigDecimal mb_remainderAmt = new BigDecimal(memberb.getRemainderAmt());
					memberb.setDisCommission(disCommissionb_.add(mb_disCommission).doubleValue());
					memberb.setRemainderAmt(disCommissionb_.add(mb_remainderAmt).doubleValue());
					this.shopMemberService.updateShopMember(memberb.getId(), memberb);
				}
				if (topDisCommission != 0) {
					// 二级利润的佣金及余额
					if (topDistributor != null && topDistributor != distributora) {
						BigDecimal md_remainderAmt = new BigDecimal(memberc.getRemainderAmt());
						BigDecimal t_disCommission = new BigDecimal(memberc.getDisCommission());
						memberc.setDisCommission(topDisCommission_.add(t_disCommission).doubleValue());
						memberc.setRemainderAmt(topDisCommission_.add(md_remainderAmt).doubleValue());
						this.shopMemberService.updateShopMember(memberc.getId(), memberc);
					} else {
						BigDecimal md_remainderAmt = new BigDecimal(membera.getRemainderAmt());
						BigDecimal t_disCommission = new BigDecimal(membera.getDisCommission());
						membera.setDisCommission(topDisCommission_.add(t_disCommission).doubleValue());
						membera.setRemainderAmt(topDisCommission_.add(md_remainderAmt).doubleValue());
						this.shopMemberService.updateShopMember(membera.getId(), membera);
					}

				}
			}

		}
	}

	/**
	 * 计算佣金(白春达)
	 */
	/*
	 * @Override public void disTributorAmt(ShopOrderInfo order) {
	 * 
	 * if(!order.getIsDisAmtEnd()){ order.setIsDisAmtEnd(true); ShopDistributor
	 * distributora = order.getDistributor();//当前分销商 ShopDistributor
	 * topDistributor = null;//所属实体店 ShopMember user = order.getUser(); //判断身份
	 * ShopDistributor mydis = null; QueryObject qos = new QueryObject();
	 * qos.addQuery("obj.member",user,"="); List<ShopDistributor> listdis =
	 * this.shopDistributorService.getShopDistributorBy(qos).getResult(); String
	 * flag=null; if(listdis!=null && listdis.size()!=0){ mydis =
	 * listdis.get(0); if(mydis.getStatus()==1 && mydis.getExStatus()!=1){ flag
	 * = "weidian"; }else if(mydis.getExStatus()==1){ flag = "tiyandian"; }else{
	 * flag = "huiyuan"; } }else{ flag = "huiyuan"; } ShopDistributor
	 * distributorb = null; ShopDistributor distributorc = null; ShopMember
	 * membera = null; ShopMember memberb = null; ShopMember memberc = null;
	 * ShopMember memberd = null;
	 * 
	 * if(distributora!=null){ membera = distributora.getMember();
	 * if(distributora.getParent() != distributora){ distributorb =
	 * distributora.getParent();//上级分销商 } if(distributorb != null){ memberb =
	 * distributorb.getMember(); if(distributorb.getParent() != distributorb){
	 * distributorc=distributorb.getParent();//上上级分销商 } } if(distributorc !=
	 * null){ memberc = distributorc.getMember(); } topDistributor =
	 * distributora.getTopDistributor();//获取所属实体店 if(topDistributor != null){
	 * memberd = topDistributor.getMember(); } Double
	 * disCommissiona=0.0;//本级分销佣金 Double disCommissionb=0.0;//上级分销佣金 Double
	 * disCommissionc=0.0;//上上级分销佣金 Double topDisCommission=0.0;//实体店利润
	 * 
	 * 
	 * 
	 * List<ShopOrderdetail> orderdetails = order.getOrderdetails();
	 * for(ShopOrderdetail orderdetail:orderdetails){ ShopProduct pro =
	 * orderdetail.getPro(); ShopSpec spec = orderdetail.getShopSpec(); int num
	 * = orderdetail.getNum();
	 * 
	 * // BigDecimal s_amt = null; BigDecimal s_TydAmt = null; BigDecimal
	 * s_Store_price = null; if(spec!=null){ s_amt = new
	 * BigDecimal(spec.getAmt()); s_TydAmt = new BigDecimal(spec.getTydAmt());
	 * s_Store_price = new BigDecimal(spec.getStore_price()); } // BigDecimal
	 * p_amt = new BigDecimal(pro.getAmt()); BigDecimal p_TydAmt = new
	 * BigDecimal(pro.getTydAmt()); BigDecimal p_Store_price = new
	 * BigDecimal(pro.getStore_price());
	 * 
	 * 
	 * if(mydis != null){ if(!mydis.equals(topDistributor) &&
	 * !mydis.equals(distributora)){
	 * if(distributora.getExStatus()==1){//累加本级分销佣金 if(spec!=null){
	 * disCommissiona +=
	 * (s_amt.subtract(s_TydAmt).doubleValue())*num;//如果是体验店用零售价减体验店价格 }else{
	 * disCommissiona +=
	 * (p_amt.subtract(p_TydAmt).doubleValue())*num;//如果是体验店用零售价减体验店价格 }
	 * 
	 * }else if(distributora.getExStatus()!=1 && distributora.getStatus()==1){
	 * if(spec!=null){ disCommissiona +=
	 * (s_amt.subtract(s_Store_price).doubleValue())*num;//如果是微店，用零售价-微点价格
	 * if(topDistributor != null){ topDisCommission +=
	 * (s_Store_price.subtract(s_TydAmt).doubleValue())*num; }
	 * 
	 * }else{ disCommissiona
	 * +=(p_amt.subtract(p_Store_price).doubleValue())*num;//如果是微店，用零售价-微点价格
	 * if(topDistributor != null){ topDisCommission +=
	 * (p_Store_price.subtract(p_TydAmt).doubleValue())*num; } } }
	 * if(distributorb!=null ){ if(distributorb.getExStatus() != 1){
	 * disCommissionb+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 1))*num; }
	 * 
	 * } if(distributorc!=null){ if(distributorc.getExStatus() != 1){
	 * disCommissionc+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 2))*num; } } }else if(mydis.equals(distributora)){
	 * if(distributora.getExStatus()!=1 && distributora.getStatus()==1){
	 * if(topDistributor != null){ if(spec!=null){ topDisCommission +=
	 * (s_Store_price.subtract(s_TydAmt).doubleValue())*num; }else{
	 * topDisCommission += (p_Store_price.subtract(p_TydAmt).doubleValue())*num;
	 * } } if(distributorb!=null){ if(distributorb.getExStatus() != 1){
	 * disCommissionb+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 1))*num; } } if(distributorc!=null){
	 * if(distributorc.getExStatus() != 1){ disCommissionc+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 2))*num; } } } } }else{
	 * if(distributora.getExStatus()==1){//累加本级分销佣金 if(spec!=null){
	 * disCommissiona +=
	 * (s_amt.subtract(s_TydAmt).doubleValue())*num;//如果是体验店用零售价减体验店价格 }else{
	 * disCommissiona +=
	 * (p_amt.subtract(p_TydAmt).doubleValue())*num;//如果是体验店用零售价减体验店价格 }
	 * 
	 * }else if(distributora.getExStatus()!=1 && distributora.getStatus()==1){
	 * if(spec!=null){ disCommissiona +=
	 * (s_amt.subtract(s_Store_price).doubleValue())*num;//如果是微店，用零售价-微点价格
	 * if(topDistributor != null){ topDisCommission +=
	 * (s_Store_price.subtract(s_TydAmt).doubleValue())*num; } }else{
	 * disCommissiona
	 * +=(p_amt.subtract(p_Store_price).doubleValue())*num;//如果是微店，用零售价-微点价格
	 * if(topDistributor != null){ topDisCommission +=
	 * (p_Store_price.subtract(p_TydAmt).doubleValue())*num; }
	 * 
	 * } } if(distributorb != null){ if(distributorb.getExStatus() != 1){
	 * disCommissionb+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 1))*num; } } if(distributorc!=null){
	 * if(distributorc.getExStatus() != 1){ disCommissionc+=
	 * (commissionConfigService.getCommissionConfigByProductId(orderdetail.
	 * getPro(), 2))*num; } } }
	 * 
	 * } if(disCommissiona != 0){ BigDecimal Coupon_price = new
	 * BigDecimal(order.getCoupon_price()); BigDecimal disCommissiona2 = new
	 * BigDecimal(disCommissiona); disCommissiona =
	 * disCommissiona2.subtract(Coupon_price).doubleValue(); } CommissionDetail
	 * cd = new CommissionDetail(); //cd.setIntegral(null); cd.setOrder(order);
	 * cd.setUser(user); if(disCommissiona != 0){ BigDecimal b = new
	 * BigDecimal(disCommissiona); double f1 = b.setScale(2,
	 * BigDecimal.ROUND_HALF_UP).doubleValue();
	 * cd.setBalanceonedistri(distributora); cd.setBalanceone(f1);
	 * this.remainderAmtHistoryService.addRemainderAmtHistory(membera,5,f1,
	 * "一级利润获取") ;
	 * 
	 * } if(disCommissionb != 0){ BigDecimal b = new BigDecimal(disCommissionb);
	 * double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	 * cd.setLevelonedistri(distributorb); cd.setLevelonecommission(f1);
	 * this.remainderAmtHistoryService.addRemainderAmtHistory(memberb,5,f1,
	 * "一级佣金获取") ; }
	 * 
	 * if(disCommissionc != 0){ BigDecimal b = new BigDecimal(disCommissionc);
	 * double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	 * cd.setLeveltowdistri(distributorc); cd.setLeveltowcommission(f1);
	 * this.remainderAmtHistoryService.addRemainderAmtHistory(memberc,5,f1,
	 * "二级佣金获取") ; }
	 * 
	 * if(topDisCommission != 0){ BigDecimal b = new
	 * BigDecimal(topDisCommission); double f1 = b.setScale(2,
	 * BigDecimal.ROUND_HALF_UP).doubleValue();
	 * cd.setBalancetowdistri(topDistributor); cd.setBalancetow(f1);
	 * this.remainderAmtHistoryService.addRemainderAmtHistory(memberd,5,f1,
	 * "差额利润获取") ; } this.commissionDetailService.addCommissionDetail(cd);
	 * 
	 * //转为精确数字 BigDecimal disCommissiona_ = new BigDecimal(disCommissiona);
	 * BigDecimal disCommissionb_ = new BigDecimal(disCommissionb); BigDecimal
	 * disCommissionc_ = new BigDecimal(disCommissionc); BigDecimal
	 * topDisCommission_ = new BigDecimal(topDisCommission);
	 * 
	 * 
	 * //一级利润的佣金及余额 BigDecimal m_disCommission = new
	 * BigDecimal(membera.getDisCommission()); BigDecimal m_remainderAmt = new
	 * BigDecimal(membera.getRemainderAmt());
	 * membera.setDisCommission(disCommissiona_.add(m_disCommission).doubleValue
	 * ());
	 * membera.setRemainderAmt(disCommissiona_.add(m_remainderAmt).doubleValue()
	 * ); // distributora.setDisAmount(order.getGross_price());//设置分销总金额
	 * this.shopMemberService.updateShopMember(membera.getId(), membera);
	 * if(distributorb!=null){ //一级佣金的佣金及余额 BigDecimal mb_disCommission = new
	 * BigDecimal(memberb.getDisCommission()); BigDecimal mb_remainderAmt = new
	 * BigDecimal(memberb.getRemainderAmt());
	 * memberb.setDisCommission(disCommissionb_.add(mb_disCommission).
	 * doubleValue());
	 * memberb.setRemainderAmt(disCommissionb_.add(mb_remainderAmt).doubleValue(
	 * )); this.shopMemberService.updateShopMember(memberb.getId(), memberb); }
	 * if(distributorc!=null){ //二级佣金的佣金及余额 BigDecimal mc_remainderAmt = new
	 * BigDecimal(memberc.getRemainderAmt()); BigDecimal dc_disCommission = new
	 * BigDecimal(memberc.getDisCommission());
	 * memberc.setDisCommission(disCommissionc_.add(dc_disCommission).
	 * doubleValue());
	 * memberc.setRemainderAmt(disCommissionc_.add(mc_remainderAmt).doubleValue(
	 * )); this.shopMemberService.updateShopMember(memberc.getId(), memberc); }
	 * if(topDistributor != null && topDistributor!=distributora){ //二级利润的佣金及余额
	 * BigDecimal md_remainderAmt = new BigDecimal(memberd.getRemainderAmt());
	 * BigDecimal t_disCommission = new BigDecimal(memberd.getDisCommission());
	 * memberd.setDisCommission(topDisCommission_.add(t_disCommission).
	 * doubleValue());
	 * memberd.setRemainderAmt(topDisCommission_.add(md_remainderAmt).
	 * doubleValue()); this.shopMemberService.updateShopMember(memberd.getId(),
	 * memberd); } } } }
	 */

	/**
	 * 计算团队累计业绩
	 */
	@Override
	public void spokesmanTeam(ShopOrderInfo order) {
		Double totalprice = order.getGross_price();
		ShopMember user = order.getUser();
		Spokesman man = user.getMySpokesman();
		String depath = man.getDePath();
		String[] str = depath.split("@");
		for (int i = 0; i <= str.length; i++) {
			Spokesman sman = this.spokesmanService.getSpokesman(Long.parseLong(str[i]));
			if (sman != null) {
				sman.setTeamAmount((float) (sman.getTeamAmount() + totalprice));
				sman.setSpokesmanRating(this.spokesmanRatingService.judgeRating(sman));
				this.spokesmanService.updateSpokesman(sman.getId(), sman);
			}
		}
	}

	/**
	 * 支付完成修改库存,及用户信息
	 */
	@Override
	public void disPaySuccess(ShopOrderInfo order) {
		if (order.getStatus() == 0) {
			order.setStatus(1);
			System.out.println("=======订单：" + order.getCode() + "更改了状态");
		}
		this.updateShopOrderInfo(order.getId(), order);

		ShopMember member = order.getUser();
		Follower f = member.getFollower();
		if (f != null) {
			Account a = f.getAccount();
			ShopSinceSome ss = order.getShopSinceSome();
			if (ss != null) {
				ShopDistributor dis = ss.getShopDistributor();
				if (dis != null) {
					String address = "【地址";
					SystemRegion region = ss.getArea();
					if (region != null) {
						SystemRegion pregion = region.getParent();
						if (pregion != null) {
							SystemRegion ppregion = pregion.getParent();
							if (ppregion != null) {
								address = address + ":" + ppregion.getTitle() + "-" + pregion.getTitle() + "-"
										+ region.getTitle() + "-" + dis.getOpenAccountAddress() + "】";
							} else {
								address = address + ":" + pregion.getTitle() + "-" + region.getTitle() + "-"
										+ dis.getOpenAccountAddress() + "】";
							}
						} else {
							address = address + ":" + region.getTitle() + "-" + dis.getOpenAccountAddress() + "】";
						}
					} else {
						address = address + ":" + dis.getOpenAccountAddress() + "】";
					}
					String tel = "【店家联系电话：" + dis.getMobile() + "】";
					WeixinBaseUtils.sendMsgToFollower(a, f,
							"【舟山海鲜之家控股有限公司】尊敬的客户" + member.getNickname() + "您好！您于" + order.getCeateDate() + "所下订单:"
									+ order.getCode() + "已成功了，" + address + tel + "，如需服务，请致电400-9261088");
				} else {
					WeixinBaseUtils.sendMsgToFollower(a, f, "【舟山海鲜之家控股有限公司】尊敬的客户" + member.getNickname() + "您好！您于"
							+ order.getCeateDate() + "所下订单:" + order.getCode() + "已成功，如需服务，请致电400-9261088");
				}
			} else {
				WeixinBaseUtils.sendMsgToFollower(a, f, "【舟山海鲜之家控股有限公司】尊敬的客户" + member.getNickname() + "您好！您于"
						+ order.getCeateDate() + "所下订单:" + order.getCode() + "已成功，我们将及时为您发货，如需服务，请致电400-9261088");
			}
		}
		System.out.println("=======订单：" + order.getCode() + "不是未支付状态");
	}

	@Override
	public Map<Long, List<ShopOrderdetail>> getOrderDetailsByBrand(ShopOrderInfo order) {
		Map<Long, List<ShopOrderdetail>> result = new HashMap();
		List<ShopOrderdetail> details = order.getOrderdetails();
		for (int i = 0; i < details.size(); ++i) {
			Long detailId = 0L;
			Brand brand = details.get(i).getPro().getBrand();
			if (brand != null) {
				detailId = brand.getId();
			}
			if (result.get(detailId) == null) {
				result.put(detailId, new ArrayList());
			}
			result.get(detailId).add(details.get(i));
		}
		return result;
	}

	@Override
	public void queryOrderCount(WebForm form, ShopMember shopMember) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 0");
		Integer nonPaymentCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 0
																					// 未支付订单
		form.addResult("nonPaymentCount", nonPaymentCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 1");
		Integer paymentNodeliveryCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 1
																							// 已付款代发货
		form.addResult("paymentNodeliveryCount", paymentNodeliveryCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 2");
		Integer paymentCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 2
																				// 代收货
		form.addResult("paymentCount", paymentCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 3");
		Integer completeCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 3
																				// 已完成
		form.addResult("completeCount", completeCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = -1");
		Integer qxCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // -1
																			// 取消订单
		form.addResult("qxCount", qxCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 4");
		Integer appReturnCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 4
																					// 申请退货
		form.addResult("appReturnCount", appReturnCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 5");
		Integer agreeReturnCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 5
																					// 同意退货
		form.addResult("agreeReturnCount", agreeReturnCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status = 6");
		Integer disAgreeReturnCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 5
																						// 不同意退货
		form.addResult("disAgreeReturnCount", disAgreeReturnCount);
		qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		Integer allOrderCount = this.shopOrderInfoDao.findBy(qo).getRowCount(); // 所有订单
		form.addResult("allOrderCount", allOrderCount);
	}

	@Override
	public List<ShopOrderInfo> queryMyOrder(ShopMember shopMember) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user", shopMember, "=");
		qo.addQuery("obj.status", 0, "=");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		return this.shopOrderInfoDao.findBy(qo).getResult();
	}

	@Override
	public ShopOrderInfo getShopOrderByName(String name, String code) {
		return this.shopOrderInfoDao.getBy(name, code);
	}

	@Override
	public Double getShopOrderAmt(ShopOrderInfo order) {
		List<ShopOrderdetail> orderDetailList = order.getOrderdetails();
		Double amt = null;
		for (ShopOrderdetail shopOrderdetail : orderDetailList) {
			amt = (shopOrderdetail.getPro().getAmt()) * (shopOrderdetail.getNum());
		}
		return amt;
	}

	public IRemainderAmtHistoryService getRemainderAmtHistoryService() {
		return remainderAmtHistoryService;
	}

	public void setRemainderAmtHistoryService(IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}

}
