package com.eastinno.otransos.seafood.core.action;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.trade.domain.ApplyRefund;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IApplyRefundService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.trade.service.IShopPayMentService;
import com.eastinno.otransos.seafood.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.seafood.usercenter.service.IApplyWithdrawCashService;
import com.eastinno.otransos.seafood.usercenter.service.IMyCouponService;
import com.eastinno.otransos.seafood.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.service.IShopSinceSomeService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.formatUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

public class WxShopMemberCenterAction extends WxShopBaseAction {
	@Inject
	private IMyCouponService myCouponService;
	@Inject
	private IShopProductService shopProductService;
	@Inject
	private IShopAddressService addressService;
	@Inject
	private ISystemRegionService regionService;
	@Inject
	private IShopDistributorService distributorService;
	@Inject
	private IShopOrderInfoService shopOrderInfoService;
	@Inject
	private IPaymentConfigService paymentConfigService;
	@Inject
	private IShopPayMentService shopPayMentService;
	@Inject
	private IDeliveryRuleService deliveryRuleService;
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private IApplyRefundService applyRefundService;
	@Inject
	private IApplyWithdrawCashService applyWithdrawCashService;
	@Inject
	private IRemainderAmtHistoryService remainderAmtHistoryService;
	@Inject
	private IShopSinceSomeService shopSinceSomeService;

	public void setApplyRefundService(IApplyRefundService applyRefundService) {
		this.applyRefundService = applyRefundService;
	}

	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}

	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}

	public IShopPayMentService getShopPayMentService() {
		return shopPayMentService;
	}

	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}

	public IDeliveryRuleService getDeliveryRuleService() {
		return deliveryRuleService;
	}

	public void setDeliveryRuleService(IDeliveryRuleService deliveryRuleService) {
		this.deliveryRuleService = deliveryRuleService;
	}

	public Page doToMyInfo(WebForm form) {
		return new Page("/bcd/wxshop/member/home.html");
	}

	public void setApplyWithdrawCashService(IApplyWithdrawCashService applyWithdrawCashService) {
		this.applyWithdrawCashService = applyWithdrawCashService;
	}

	/**
	 * 我的订单 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToOrder(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member != null) {
			member = this.shopMemberService.getShopMember(member.getId());
		} else {
			return error(form, "操作超时或无法获取用户信息");
		}

		form.addResult("member", member);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.addQuery("obj.type", "integral", "<>");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		qo.setPageSize(-1);
		List<ShopOrderInfo> orderList = (List<ShopOrderInfo>) this.shopOrderInfoService.getShopOrderInfoBy(qo)
				.getResult();
		List<ShopOrderInfo> unpaidOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> paidOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> deliverOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> receiveOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> backOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> cancelOrder = new ArrayList<ShopOrderInfo>();
		List<ShopOrderInfo> returnOrder = new ArrayList<ShopOrderInfo>();// 同意退货/不同意退货
		ShopOrderInfo tempOrder;
		Integer statusCh;
		if (orderList == null) {
			orderList = new ArrayList<ShopOrderInfo>();
		}
		for (int i = 0; i < orderList.size(); ++i) {
			tempOrder = orderList.get(i);
			statusCh = tempOrder.getStatus();
			if (statusCh == 0) {
				unpaidOrder.add(tempOrder);
			} else if (statusCh == 1) {
				paidOrder.add(tempOrder);
			} else if (statusCh == 2) {
				deliverOrder.add(tempOrder);
			} else if (statusCh == 3) {
				receiveOrder.add(tempOrder);
			} else if (statusCh == 4) {
				backOrder.add(tempOrder);
			} else if (statusCh == 5) {
				returnOrder.add(tempOrder);
			} else if (statusCh == 6) {
				returnOrder.add(tempOrder);
			} else if (statusCh == -1) {
				cancelOrder.add(tempOrder);
			}
		}

		form.addResult("orderList", orderList);
		form.addResult("unpaidOrder", unpaidOrder);
		form.addResult("paidOrder", paidOrder);
		form.addResult("deliverOrder", deliverOrder);
		form.addResult("receiveOrder", receiveOrder);
		form.addResult("backOrder", backOrder);
		form.addResult("cancelOrder", cancelOrder);
		form.addResult("returnOrder", returnOrder);// 同意退货/不同意退货

		form.addResult("fu", formatUtil.fu);
		return new Page("/bcd/wxshop/member/order.html");
	}

	/**
	 * 展示我的推荐人页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToIntroducer(WebForm form) {
		ShopMember member = getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息");
		}
		ShopDistributor distributor = member.getMyDistributor();
		form.addResult("parentMember", member.getPmember());
		return new Page("/bcd/wxshop/member/introducer.html");
	}

	/**
	 * 我的优惠券 显示页0.0
	 * 
	 * @param form
	 * @return
	 */
	public Page doToMyCoupons(WebForm form) {
		QueryObject qo = new QueryObject();
		ShopMember member = this.getShopMember(form);
		form.addResult("member", member);
		qo.addQuery("obj.shopMember.id", member.getId(), "=");
		IPageList couponList = this.myCouponService.getMyCouponBy(qo);
		form.addResult("couponList", couponList);
		return new Page("/bcd/wxshop/member/coupons.html");
	}

	/**
	 * 收货地址添加请求处理
	 * 
	 * @param form
	 * @return
	 */
	public Page doAddressSave(WebForm form) {
		String url = CommUtil.null2String((String) form.get("url"));
		ShopMember member = this.getShopMember(form);
		ShopAddress entry = form.toPo(ShopAddress.class);
		entry.setUser(member);
		if (!hasErrors()) {
			Long id = this.addressService.addShopAddress(entry);
			if (id != null) {
				form.addResult("msg", "添加成功");
			}
		}
		if (!"".equals(url)) {
			form.addResult("url", url);
		}
		if (url.startsWith("wxShopTrade.java")) {
			return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList2&url=" + URLEncoder.encode(url));
		} else {
			return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList&url=" + URLEncoder.encode(url));
		}
	}

	/**
	 * 收货地址 添加的操作页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToAddressSave(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		String url = CommUtil.null2String((String) form.get("url"));
		form.addResult("url", url);
		form.addResult("addressService", this.addressService);
		form.addResult("provinceList", this.regionService.getRootSystemRegions().getResult());
		return new Page("/bcd/wxshop/member/addressEdit.html");
	}

	/**
	 * 收货地址 修改后的提交处理动作
	 * 
	 * @param form
	 * @return
	 */
	public Page doAddressEdit(WebForm form) {
		String url = CommUtil.null2String((String) form.get("url"));
		Long id = Long.valueOf(CommUtil.null2String((String) form.get("id")));
		ShopMember member = this.getShopMember(form);
		ShopAddress entry = form.toPo(ShopAddress.class);
		entry.setUser(member);

		// 处理默认地址,先将所有地址设置为false
		String isDefault = CommUtil.null2String((String) form.get("isDefault"));
		if ("1".equals(isDefault)) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.user.id", entry.getUser().getId(), "=");
			List addressList = this.getAddressService().getShopAddressBy(qo).getResult();
			ShopAddress address = null;
			int len = addressList.size();
			for (int i = 0; i < len; ++i) {
				address = (ShopAddress) addressList.get(i);
				address.setIsDefault(false);
				this.getAddressService().updateShopAddress(address.getId(), address);
			}
		}

		// 更新本条记录
		this.addressService.updateShopAddress(id, entry);
		if (!"".equals(url)) {
			form.addResult("url", url);
		}
		if (url.startsWith("wxShopTrade.java")) {
			return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList2&url=" + URLEncoder.encode(url));
		} else {
			return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList&url=" + URLEncoder.encode(url));
		}
	}

	/**
	 * 收益统计 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToProfit(WebForm form) {
		return new Page("/bcd/wxshop/member/profit.html");
	}

	/**
	 * 我的佣金 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToCommission(WebForm form) {
		return new Page("/bcd/wxshop/member/commission.html");
	}

	/**
	 * 我的浏览记录 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToVisitHistory(WebForm form) {
		String myHistory = DiscoShopUtil.getCookie("proHistory");
		if (StringUtils.hasText(myHistory)) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.id in (" + myHistory + ")");
			IPageList pl = this.shopProductService.getShopProductBy(qo);
			CommUtil.saveIPageList2WebForm(pl, form);
			form.addResult("proList", pl.getResult());
		}
		return new Page("/bcd/wxshop/member/visitHistory.html");
	}

	/**
	 * 申请成为分销商 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToApplyDistributor(WebForm form) {
		return new Page("");
	}

	/**
	 * 提交成为分销商的申请
	 * 
	 * @param form
	 * @return
	 */
	public Page doApplyDistributor(WebForm form) {

		return new Page("");
	}

	/**
	 * 我的信息
	 * 
	 * @param form
	 * @return
	 */
	public Page doToMyInfoEdit(WebForm form) {
		ShopMember member = getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息！！！");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member.id", member.getId(), "=");
		qo.setPageSize(1);
		List<?> list = this.distributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor dis = null;
		if (list != null && list.size() > 0) {
			dis = (ShopDistributor) list.get(0);
			form.addResult("dis", dis);
			return new Page("/bcd/wxshop/member/fxshyedit.html");
		} else {
			form.addResult("member", member);
			return new Page("/bcd/wxshop/member/hyzxedit.html");
		}
	}

	/**
	 * 修改个人基本信息
	 * 
	 * @param form
	 * @return
	 */
	public void doUpdate(WebForm form) {
		String type = CommUtil.null2String(form.get("memberType"));
		if ("1".equals(type)) {
			Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
			ShopMember member = this.getShopMemberService().getShopMember(id);
			form.toPo(member);
			this.getShopMemberService().updateShopMember(member.getId(), member);
			ActionContext.getContext().getSession().removeAttribute("SHOPMEMBER");

			member = this.getShopMemberService().getShopMember(id);
			Tenant t = null;
			try {
				t = TenantContext.getTenant();
			} catch (Exception e) {
				e.printStackTrace();
			}
			member.setTenant(t);
			Follower f = getFollowerBySession();
			member.setFollower(f);
			ActionContext.getContext().getSession().setAttribute("SHOPMEMBER", member);

			form.addResult("member", member);
		} else {
			Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
			ShopDistributor dis = this.distributorService.getShopDistributor(id);
			form.toPo(dis);
			this.distributorService.updateShopDistributor(dis.getId(), dis);

			// 更新userinfo表数据
			ShopMember disMember = dis.getMember();
			String trueName = CommUtil.null2String(form.get("trueName"));
			String mobileTel = CommUtil.null2String(form.get("mobileTel"));
			String idCard = CommUtil.null2String(form.get("idCard"));
			if (!trueName.equals(""))
				disMember.setTrueName(trueName);
			if (!mobileTel.equals(""))
				disMember.setMobileTel(mobileTel);
			if (!idCard.equals(""))
				disMember.setIdCard(idCard);
			this.shopMemberService.updateShopMember(disMember.getId(), disMember);

			Tenant t = null;
			disMember.setTenant(t);
			Follower f = getFollowerBySession();
			disMember.setFollower(f);
			ActionContext.getContext().getSession().setAttribute("SHOPMEMBER", disMember);
			form.addResult("dis", dis);
		}
		form.addResult("msg", "信息更新成功！");
		this.go("toMyInfoEdit");
	}

	/**
	 * 客户订单详情
	 * 
	 * @param form
	 * @return
	 */
	public Page doOrderDetail2(WebForm form) {

		// 判断身份
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		if (user != null) {
			user = this.shopMemberService.getShopMember(user.getId());
		} else {
			return error(form, "操作超时或无法获取用户信息！！请重试！");
		}

		String flag = null;
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member", user, "=");
		List<ShopDistributor> listdis = this.distributorService.getShopDistributorBy(qos).getResult();
		if (listdis != null && listdis.size() != 0) {
			ShopDistributor mydis = listdis.get(0);
			if (mydis.getStatus() == 1 && mydis.getExStatus() != 1) {
				flag = "weidian";
			} else if (mydis.getExStatus() == 1) {
				flag = "tiyandian";
			} else {
				flag = "huiyuan";
			}
		} else {
			flag = "huiyuan";
		}
		form.addResult("flag", flag);

		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));

		// 邮费计算和设置,每次进入订单支付页都会及时的计算邮费并更新订单总价
		Map<Long, Double> costMap = this.deliveryRuleService.getDeliveryCostMap(order);
		// Double freight = this.deliveryRuleService.getDeliveryCost(order);
		Double freight = this.getCostByBrandCostMap(costMap);
		Double gross_price = order.getGross_price() - order.getFreight() + freight;
		order.setFreight(freight);
		order.setGross_price(gross_price);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		form.addResult("costMap", costMap);

		form.addResult("order", order);
		// QueryObject qo = new QueryObject();
		// qo.addQuery("obj.type",PayTypeE.WEIXINMPAPI,"=");
		// qo.setPageSize(1);
		// List<PaymentConfig> configs =
		// this.paymentConfigService.getPaymentConfigBy(qo).getResult();
		// if(configs!=null && configs.size()>0){
		// PaymentConfig config = configs.get(0);
		// String reMsg = this.shopPayMentService.paySubmit(order, config, "");
		// form.addResult("jsStr", reMsg);
		// }

		BigDecimal b = new BigDecimal(user.getRemainderAmt());
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		form.addResult("rAmt", f1);
		form.addResult("fu", formatUtil.fu);
		return new Page("/bcd/wxshop/trading/orderDetails.html");
	}

	/**
	 * 依据cost map获取总邮费
	 * 
	 * @param costMap
	 * @return
	 */
	private Double getCostByBrandCostMap(Map<Long, Double> costMap) {
		Double result = 0D;
		Set keySet = costMap.keySet();
		Iterator iter = keySet.iterator();
		while (iter.hasNext()) {
			result += (Double) costMap.get((Long) iter.next());
		}
		return result;
	}

	/**
	 * 跳转老用户绑定页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToBindingOldUser(WebForm form) {
		return new Page("/bcd/wxshop/member/binding.html");
	}

	/**
	 * 老用户绑定
	 * 
	 * @param form
	 * @return
	 */
	public Page doBindingOldUser(WebForm form) {
		ShopMember shopMember = this.getShopMember(form);
		if (shopMember == null) {
			this.addError("msg", "登录超时，请重新登录！！！");
			return pageForExtForm(form);
		}
		String name = CommUtil.null2String(form.get("name"));
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.name", name, "=");
		qo.setPageSize(1);
		List<?> list = this.getShopMemberService().getShopMemberBy(qo).getResult();
		if (list == null) {
			this.addError("msg", "没有此用户");
			return pageForExtForm(form);
		}
		ShopMember oldMember = (ShopMember) list.get(0);// 老用户
		if ("1".equals(oldMember.getOldUserStatus())) {
			this.addError("msg", "此老用户已绑定");
			return pageForExtForm(form);
		}
		if (!"0".equals(oldMember.getOldUserStatus())) {
			this.addError("msg", "没有查询到此老用户信息");
			return pageForExtForm(form);
		}
		shopMember = this.getShopMemberService().getShopMember(shopMember.getId());
		oldMember.setFollower(shopMember.getFollower());
		oldMember.setPic(shopMember.getFollower().getHeadimgurl());
		oldMember.setOldUserStatus("1");
		ShopDistributor myDistributor = this.distributorService.getShopDistributorByMember(oldMember);
		oldMember.setMyDistributor(myDistributor);
		this.getShopMemberService().updateShopMember(oldMember.getId(), oldMember);// 修改老用户
		shopMember.setFollower(null);
		this.getShopMemberService().updateShopMember(shopMember.getId(), shopMember);
		HttpSession session = ActionContext.getContext().getSession();
		session.removeAttribute("DISTRIBUTOR");
		session.setAttribute("SHOPMEMBER", oldMember);
		if (myDistributor != null) {
			session.setAttribute("DISTRIBUTOR", myDistributor);
		}
		return pageForExtForm(form);
	}

	/**
	 * 设置重新绑定
	 * 
	 * @param form
	 * @return
	 */
	public Page doReBinding(WebForm form) {
		Integer id = CommUtil.null2Int(form.get("id"));
		ShopMember member = this.getShopMemberService().getShopMember((long) id);
		if (member == null) {
			return error(form, "此用户不是老用户");
		}
		if (!StringUtils.hasText(member.getOldUserStatus())) {
			return error(form, "此用户不是老用户");
		}
		if ("1".equals(member.getOldUserStatus())) {
			member.setFollower(null);
			member.setOldUserStatus("0");
			this.getShopMemberService().updateShopMember(member.getId(), member);
		}
		return go("manageBindingOldUser");
	}

	/**
	 * 设置重新绑定
	 * 
	 * @param form
	 * @return
	 */
	public Page doManageBindingOldUser(WebForm form) {
		String name = CommUtil.null2String(form.get("name"));
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.oldUserStatus", "1", "=");
		if (StringUtils.hasText(name)) {
			qo.addQuery("obj.name like '%" + name + "%'");
		}
		IPageList pList = this.getShopMemberService().getShopMemberBy(qo);
		CommUtil.saveIPageList2WebForm(pList, form);
		form.addResult("pl", pList);
		return new Page("shopmanage/usercenter/shopmember/ManageBdOldUser.html");
	}

	/**
	 * 跳转绑定用户名界面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToBindingName(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			this.addError("msg", "登陆超时请重新登陆");
			return pageForExtForm(form);
		}
		return new Page("/bcd/wxshop/member/bindingName.html");
	}

	/**
	 * pc端登录微信端绑定用户名
	 * 
	 * @param form
	 * @return
	 */
	public Page doBindingName(WebForm form) {
		ShopMember member = this.getShopMember(form);
		String name = CommUtil.null2String(form.get("name"));
		String password = CommUtil.null2String(form.get("password"));
		String password2 = CommUtil.null2String(form.get("password2"));
		if (member == null) {
			this.addError("msg", "登陆超时请重新登陆");
			return pageForExtForm(form);
		}
		if (!password2.equals(password)) {
			this.addError("msg", "确认密码与密码不一致，请重新输入");
			return pageForExtForm(form);
		}
		member = this.getShopMemberService().getShopMember(member.getId());
		ShopMember m = this.getShopMemberService().getShopMemberByName("name", name);
		if (m != null) {
			this.addError("msg", "此用户名已使用，请重新输入！！！");
			return pageForExtForm(form);
		}
		member.setName(name);
		member.setPassword(MD5.encode(password));
		this.getShopMemberService().updateShopMember(member.getId(), member);
		return pageForExtForm(form);
	}

	/**
	 * 不绑定用户名
	 * 
	 * @param form
	 * @return
	 */
	public Page doNoBindingName(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			this.addError("msg", "登陆超时请重新登陆");
			return pageForExtForm(form);
		}
		if (member.getPassword() == null) {
			member = this.getShopMemberService().getShopMember(member.getId());
			member.setPassword("");
			this.getShopMemberService().updateShopMember(member.getId(), member);
		}
		return pageForExtForm(form);
	}

	/**
	 * 取消订单
	 * 
	 * @param form
	 * @return
	 */
	public Page doToCancel(WebForm form) {
		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		if (order.getStatus() != -1) {
			order.setStatus(-1);
			this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
			List<ShopOrderdetail> list2 = order.getOrderdetails();
			for (ShopOrderdetail orderdetail : list2) {
				ShopProduct shopProduct = orderdetail.getPro();
				ShopSpec spec = orderdetail.getShopSpec();
				System.out.println("=======wxshopmembercenterAction  dotocancel执行了=====");
				if (spec != null) {
					int count = orderdetail.getNum();
					spec.setInventory(spec.getInventory() + count);
					shopProduct.setInventory(shopProduct.getInventory() + count);
					this.shopProductService.updateShopProduct(shopProduct.getId(), shopProduct);
					this.shopSpecService.updateShopSpec(spec.getId(), spec);
				} else {
					int count = orderdetail.getNum();
					shopProduct.setInventory(shopProduct.getInventory() + count);
					this.shopProductService.updateShopProduct(shopProduct.getId(), shopProduct);
				}
			}
		}
		return go("toOrder");
	}

	/**
	 * 收货地址 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToAddressList(WebForm form) {
		String url = (String) form.get("url");
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.addQuery("obj.status", Short.parseShort("1"), "=");
		qo.setPageSize(-1);
		IPageList addressList = this.addressService.getShopAddressBy(qo);
		form.addResult("addressList", addressList.getResult());
		form.addResult("url", url);
		List<ShopSinceSome> sssList = this.shopSinceSomeService.getShopSinceSomeListByMember(member);
		form.addResult("sssList", sssList);
		String state = CommUtil.null2String(form.get("state"));
		if (StringUtils.hasText(state)) {
			form.addResult("state", state);
		}
		return new Page("/bcd/wxshop/member/addressList3.html");
	}

	/**
	 * 购买时收货地址 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToAddressList2(WebForm form) {
		String url = (String) form.get("url");
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.addQuery("obj.status", Short.parseShort("1"), "=");
		qo.setPageSize(-1);
		IPageList addressList = this.addressService.getShopAddressBy(qo);
		form.addResult("addressList", addressList.getResult());
		form.addResult("url", url);
		List<ShopSinceSome> sssList = this.shopSinceSomeService.getShopSinceSomeListByMember(member);
		form.addResult("sssList", sssList);
		String state = CommUtil.null2String(form.get("state"));
		if (StringUtils.hasText(state)) {
			form.addResult("state", state);
		}
		return new Page("/bcd/wxshop/member/addressList2.html");
	}

	/**
	 * 收货地址 修改的操作页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToAddressEdit(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		form.addResult("provinceList", this.regionService.getRootSystemRegions().getResult());
		Long id = Long.valueOf(CommUtil.null2String((String) form.get("id")));
		String url = CommUtil.null2String((String) form.get("url"));
		ShopAddress entry = this.addressService.getShopAddress(id);
		entry.setUser(member);
		form.addResult("url", url);
		form.addResult("entry", entry);
		return new Page("/bcd/wxshop/member/addressEdit.html");
	}

	/**
	 * 收货地址删除
	 * 
	 * @param form
	 * @return
	 */
	public Page doToDelete(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		Long id = Long.valueOf(CommUtil.null2String((String) form.get("id")));
		String url = CommUtil.null2String((String) form.get("url"));
		ShopAddress entry = this.addressService.getShopAddress(id);
		entry.setStatus(Short.parseShort("2"));
		this.addressService.updateShopAddress(id, entry);

		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.addQuery("obj.status", Short.parseShort("1"), "=");
		qo.setPageSize(-1);
		IPageList addressList = this.addressService.getShopAddressBy(qo);
		form.addResult("addressList", addressList.getResult());
		form.addResult("url", url);
		List<ShopSinceSome> sssList = this.shopSinceSomeService.getShopSinceSomeListByMember(member);
		form.addResult("sssList", sssList);
		String state = CommUtil.null2String(form.get("state"));
		if (StringUtils.hasText(state)) {
			form.addResult("state", state);
		}
		return new Page("/bcd/wxshop/member/addressList3.html");
	}

	/**
	 * 我的收藏 显示页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToCollection(WebForm form) {
		ShopMember shopMember = getShopMember(form);
		if (shopMember == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		shopMember = getShopMemberService().getShopMember(shopMember.getId());
		List<ShopProduct> list = shopMember.getMyCollections();
		form.addResult("proList", list);
		return new Page("/bcd/wxshop/member/collection.html");
	}

	/**
	 * 收藏
	 * 
	 * @param form
	 * @return
	 */
	public Page doCollectionPro(WebForm form) {
		ShopMember member = getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		member = this.getShopMemberService().getShopMember(member.getId());
		Integer id = CommUtil.null2Int(form.get("id"));
		ShopProduct shopProduct = this.shopProductService.getShopProduct((long) id);
		boolean b = member.addCollections(shopProduct);
		if (b) {
			this.getShopMemberService().updateShopMember(member.getId(), member);
		} else {
			this.addError("msg", "已收藏此商品");
		}
		return pageForExtForm(form);
	}

	/**
	 * 跳转老用户绑定页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToOldRebind(WebForm form) {
		return new Page("/bcd/wxshop/member/rebinding.html");
	}

	/**
	 * 微信端重新绑定用户入口 by WB
	 * 
	 * @param form
	 * @return
	 */
	public Page doOldRebind(WebForm form) {
		ShopMember currMember = this.getShopMember(form);
		if (currMember == null) {
			return error(form, "此用户不是老用户");
		}

		boolean result = false;
		String memberName = CommUtil.null2String(form.get("name"));
		if (!memberName.equals("")) {
			ShopMember oldMember = this.shopMemberService.getShopMemberByName("name", memberName);
			if (!oldMember.getOldUserStatus().equals("0")) {
				form.addResult("result", "failure");
				form.addResult("error", "该用户不能被绑定！");
				return new Page("/bcd/member/ajax/releaseOldUser.html");
			}
			Follower currFollower = currMember.getFollower();
			if (oldMember != null) {

				// 注销掉当前ShopMember
				currMember.setFollower(null);
				currMember.setOldUserStatus("0");
				this.shopMemberService.updateShopMember(currMember.getId(), currMember);

				// 恢复以前的ShopMember
				oldMember.setFollower(currFollower);
				oldMember.setPic(currFollower.getHeadimgurl());
				oldMember.setOldUserStatus("1");
				this.shopMemberService.updateShopMember(oldMember.getId(), oldMember);

				// 更新SESSION信息
				HttpSession session = ActionContext.getContext().getSession();
				session.removeAttribute("DISTRIBUTOR");
				session.setAttribute("SHOPMEMBER", oldMember);
				ShopDistributor myDistributor = oldMember.getMyDistributor();
				if (myDistributor != null) {
					session.setAttribute("DISTRIBUTOR", myDistributor);
				}
				result = true;
			}
		}

		if (result)
			form.addResult("result", "success");
		else
			form.addResult("result", "failure");
		return new Page("/bcd/member/ajax/releaseOldUser.html");
	}

	/**
	 * 查看我的推广
	 * 
	 * @param form
	 * @return
	 */
	public Page doMyChildren(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "操作超时或无法获取用户信息!请刷新后重试");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pmember.id", member.getId(), "=");
		qo.setPageSize(-1);
		List<?> myMembers = this.shopMemberService.getShopMemberBy(qo).getResult();
		form.addResult("myMembersNum", myMembers == null ? "0" : myMembers.size());
		form.addResult("myMembers", myMembers);
		return new Page("/bcd/distribution/member/myMembers.html");
	}

	/**
	 * 退款/退货
	 * 
	 * @param form
	 */
	public Page doOrderRefund(WebForm form) {
		ShopMember member = this.getShopMember(form);
		member = this.shopMemberService.getShopMember(member.getId());
		if (member == null) {
			this.addError("msg", "操作超时或无法获取用户信息!请刷新后重试");
			pageForExtForm(form);
		}
		String orderId = CommUtil.null2String(form.get("orderId"));
		String reason = CommUtil.null2String(form.get("reason"));
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.valueOf(orderId));
		if (!order.getUser().getId().equals(member.getId())) {
			this.addError("msg", "你没有权限修改此订单");
			pageForExtForm(form);
		}
		if (order.getStatus() == 4 || order.getStatus() == 5 || order.getStatus() == 6) {
			this.addError("msg", "你已过申请过退款/退货");
			pageForExtForm(form);
		}
		ApplyRefund applyRefund = new ApplyRefund();
		applyRefund.setOrder(order);
		applyRefund.setShopMember(member);
		applyRefund.setReason(reason);
		this.applyRefundService.addApplyRefund(applyRefund);
		order.setStatus(4);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		// 给管理员发送消息，提醒有人退款
		try {
			ShopMember shopMember = this.shopMemberService.getShopMember(32776L);
			Follower f = shopMember.getFollower();
			Account a = f.getAccount();
			WeixinBaseUtils.sendMsgToFollower(a, f,
					"【舟山海鲜之家控股有限公司】通知！用户【" + member.getNickname() + "】于" + order.getCeateDate() + "申请退款，请尽快处理！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pageForExtForm(form);
	}

	/**
	 * 跳转微信提现
	 */
	public Page doToApplyWithdrawingCash(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "登录超时，请重新登陆");
		}
		member = this.getShopMemberService().getShopMember(member.getId());
		form.addResult("wx_member", member);
		return new Page("/bcd/distribution/member/wxApplyWithdrawingCash.html");
	}

	/**
	 * 微信提现申请
	 */
	public Page doApplyWithdrawingCash(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "登录超时，请重新登陆");
		}
		ApplyWithdrawCash applyWithdrawCash = form.toPo(ApplyWithdrawCash.class);
		member = this.getShopMemberService().getShopMember(member.getId());
		if (applyWithdrawCash.getSums() > member.getRemainderAmt() || applyWithdrawCash.getSums() < 100) {
			return error(form, "申请提取金额需要满100或申请金额不能大于账户余额");
		}
		boolean b = this.judgeApplySums(member, applyWithdrawCash.getSums());
		if (!b) {
			return error(form, "申请提取的总金额不能大于账户可提取金额");
		}
		if (!hasErrors()) {
			applyWithdrawCash.setShopMember(member);
			if (StringUtils.hasText(applyWithdrawCash.getBankCardNum())) {
				applyWithdrawCash.setType(Short.valueOf("1"));
			}

			// 给管理员发送消息，提醒有人申请提现
			try {
				ShopMember shopMember = this.shopMemberService.getShopMember(32776L);
				Follower f = shopMember.getFollower();
				Account a = f.getAccount();
				WeixinBaseUtils.sendMsgToFollower(a, f,
						"【舟山海鲜之家控股有限公司】通知！用户【" + member.getNickname() + "】于" + new Date() + "申请提现，请尽快处理！");
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.applyWithdrawCashService.addApplyWithdrawCash(applyWithdrawCash);
		} else {
			this.error(form, "填写的信息有误,请检查后重新输入");
		}
		return new Page("/bcd/distribution/member/wxApplyWithdrawingCash.html");
	}

	public boolean judgeApplySums(ShopMember shopMember, Double sums) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status", Short.valueOf("0"), "=");
		qo.addQuery("obj.shopMember.id", shopMember.getId(), "=");
		qo.setLimit(-1);
		List<ApplyWithdrawCash> list = this.applyWithdrawCashService.getApplyWithdrawCashBy(qo).getResult();
		double applySums = 0;
		if (list != null) {
			for (ApplyWithdrawCash applyWithdrawCash2 : list) {
				applySums += applyWithdrawCash2.getSums();
			}
		}
		if (applySums + sums > shopMember.getRemainderAmt()) {
			return false;
		}
		return true;
	}

	/**
	 * 跳转微信提现列表
	 */
	public Page doToApplyWithdrawingCashList(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "登录超时，请重新登陆");
		}
		member = this.getShopMemberService().getShopMember(member.getId());
		String status = CommUtil.null2String((form.get("status")));
		QueryObject qo = form.toPo(QueryObject.class);
		qo.addQuery("obj.shopMember.id", member.getId(), "=");
		if (StringUtils.hasText(status)) {
			qo.addQuery("obj.status", Short.valueOf(status), "=");
		}
		qo.setOrderBy("createDate");
		qo.setOrderType("desc");
		qo.setLimit(-1);
		List<ApplyWithdrawCash> applyWithdrawCashList = this.applyWithdrawCashService.getApplyWithdrawCashBy(qo)
				.getResult();
		form.addResult("applyWithdrawCashList", applyWithdrawCashList);
		return new Page("/bcd/distribution/member/wxApplyWithdrawingCashList.html");
	}

	/**
	 * 余额变动记录
	 * 
	 * @param form
	 * @return
	 */
	public Page doGetRemainderAmtHistory(WebForm form) {
		ShopMember member = this.getShopMember(form);
		if (member == null) {
			return error(form, "登录超时，请重新登陆");
		}
		member = this.shopMemberService.getShopMember(member.getId());
		QueryObject qo = form.toPo(QueryObject.class);
		qo.addQuery("obj.user", member, "=");
		qo.setOrderBy("createDate");
		qo.setOrderType("desc");
		qo.setLimit(-1);
		IPageList iPageList = this.remainderAmtHistoryService.getRemainderAmtHistoryBy(qo);
		form.addResult("remainderAmtHistoryList", iPageList.getResult());
		form.addResult("fu", formatUtil.fu);
		BigDecimal b = new BigDecimal(member.getRemainderAmt());
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		form.addResult("amt", f1);
		return new Page("/bcd/wxshop/member/remainderAmtHistory.html");
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public IMyCouponService getMyCouponService() {
		return myCouponService;
	}

	public void setMyCouponService(IMyCouponService myCouponService) {
		this.myCouponService = myCouponService;
	}

	public IShopAddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(IShopAddressService addressService) {
		this.addressService = addressService;
	}

	public ISystemRegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(ISystemRegionService regionService) {
		this.regionService = regionService;
	}

	public IShopDistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(IShopDistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}

	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}

	public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setRemainderAmtHistoryService(IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}

	public void setShopSinceSomeService(IShopSinceSomeService shopSinceSomeService) {
		this.shopSinceSomeService = shopSinceSomeService;
	}

}
