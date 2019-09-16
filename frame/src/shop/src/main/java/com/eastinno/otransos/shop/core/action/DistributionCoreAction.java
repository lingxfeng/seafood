package com.eastinno.otransos.shop.core.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.eastinno.otransos.application.util.QRCodeUtil;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.ICommissionWithdrawService;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 分销模块控制
 * @author nsz
 */
public class DistributionCoreAction extends WxShopBaseAction{
	@Inject
    private IShopDistributorService shopDistributorService;	
	@Inject
	private IShopOrderInfoService shopOrderInfoService;	
	@Inject
	private ISystemRegionService systemRegionService;
	@Inject
	private ICommissionWithdrawService commissionWithdrawService;
	
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	
	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	
	public ICommissionWithdrawService getCommissionWithdrawService() {
		return commissionWithdrawService;
	}

	public void setCommissionWithdrawService(
			ICommissionWithdrawService commissionWithdrawService) {
		this.commissionWithdrawService = commissionWithdrawService;
	}
	
	/**
	 * 进入申请页面
	 * @param form
	 * @return
	 */
	public Page doToApply(WebForm form){
		ShopMember member = getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		
		//错误检查，并跳转
		Page page = this.checkApply(form, member);
		if(page != null){
			return page;
		}
		/**
		 * 获取分销商结束
		 */
		QueryObject qoArea = new QueryObject();
		qoArea.addQuery("obj.lev", 1, "=");
		qoArea.setLimit(-1);
		List<?> list=this.systemRegionService.querySystemRegion(qoArea).getResult();
		form.addResult("rList", list);
		return new Page("/bcd/wxshop/distributor/addfx.html");
	}
		
	/**
	 * 申请实体店页面
	 * @param form
	 * @return
	 */
	public Page doApplyEntityShop(WebForm form){
		String sn=CommUtil.null2String(form.get("area_id"));
		String openAccountAddress=CommUtil.null2String(form.get("openAccountAddress"));
		ShopDistributor shopDistributor=this.shopDistributorService.getShopDistributorByMember(this.getShopMember(form));
		if(shopDistributor==null){
			shopDistributor = form.toPo(ShopDistributor.class);
			this.shopDistributorService.applyShopDistributor(this.getAccount(form), shopDistributor, this.getShopMember(form));
		}
		shopDistributor = this.shopDistributorService.getShopDistributorByMember(this.getShopMember(form));
		
		//错误检查，并跳转
		Page page = this.checkApply(form, this.getShopMember(form));
		if(page != null){
			return page;
		}
		
		QueryObject qoo = new QueryObject();
		qoo.addQuery("obj.sn", sn, "=");
		List<?> list=this.systemRegionService.querySystemRegion(qoo).getResult();
		if(list!=null){
			SystemRegion systemRegion=(SystemRegion)list.get(0);
			shopDistributor.setArea(systemRegion);
		}
		/*if(shopDistributor.getArea()!=null){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.id", shopDistributor.getId(), "<>");
			qo.addQuery("obj.area", shopDistributor.getArea(), "=");
			List<?> list2=this.shopDistributorService.getShopDistributorBy(qo).getResult();
			if(list2!=null){
				return error(form,"此地区已经申请了分销商");
			}
		}*/
		shopDistributor.setOpenAccountApplyDate(new Date());
		shopDistributor.setDisType(2);
		shopDistributor.setExStatus(0);
		shopDistributor.setOpenAccountAddress(openAccountAddress);
		this.shopDistributorService.updateShopDistributor(shopDistributor.getId(), shopDistributor);
		form.addResult("shopDistributor", shopDistributor);
		return error(form, "您的开店申请已经提交，系统正在处理...");
	}
	
	/**
	 * 判断
	 * @param form
	 * @param member
	 * @return
	 */
	private Page checkApply(WebForm form, ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		qo.setPageSize(1);
		List<ShopDistributor> lists = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			
		ShopDistributor distributor = null;
		if(lists!=null && lists.size()>0){
			distributor = lists.get(0);
			if(distributor.getExStatus() == 0){
				return error(form, "您的开店申请已经提交，系统正在处理...");	
			}else if(distributor.getExStatus()==1 && distributor.getDisType()==2){
				form.addResult("tStatus", "1");
				return new Page("/bcd/distribution/member/distributorIndex.html");
			}else if(distributor.getExStatus() == 2){
				return error(form, "抱歉，您的申请已经被拒绝，如有疑问请联系管理员！");
			}			
		}
		
		return null;
	}

	/**
	 * 根据二维码进入到个人店铺前置
	 */
	@Override
	public Page doInit(WebForm form, Module module) {
		Account account = getAccount(form);
		if(account == null){
			return error(form,"操作超时或无法获取公众号信息!请刷新后重试");
		}
		String pmemberId = CommUtil.null2String(form.get("pmemberId"));
		String toProId = CommUtil.null2String(form.get("toProId"));
		String url = WeixinBaseUtils.getDomain()+"/distributionCore.java?cmd=distributShop&accountId="+account.getId()+"&pmemberId="+pmemberId;
		if(!"".equals(toProId)){
			url+="&toProId="+toProId;
		}
		return WeixinBaseUtils.reSendWx(url, account);
	}
	/**
	 * 根据二维码进入到个人店铺后置
	 */
	public Page doDistributShop(WebForm form){
		String pmemberId = CommUtil.null2String(form.get("pmemberId"));
		if(!"".equals(pmemberId)){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id",Long.parseLong(pmemberId),"=");
			qo.setPageSize(1);
			List<?> list = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			if(list!=null && list.size()>0){
				ShopDistributor distributor = (ShopDistributor) list.get(0);
				HttpSession session = ActionContext.getContext().getSession();
				session.setAttribute("DISTRIBUTOR",list.get(0));
				form.addResult("distributor", distributor);
				String toProId = CommUtil.null2String(form.get("toProId"));
				if(!"".equals(toProId)){
					form.addResult("toProId", toProId);
				}
			}
		}
		
		return new Page("/bcd/wxshop/product/index.html");
	}
	
	/**
	 * 申请成为分销商
	 * @param form
	 * @return
	 */
	public Page doApplyDistributor(WebForm form){
		ShopMember member = getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		
		//错误检查，并跳转
		Page page = this.checkDistributorApply(form, member);
		if(page != null){
			return page;
		}
		
			
		Account a = getAccount(form);	
		ShopDistributor butor=this.shopDistributorService.getShopDistributorByMember(member);
		if(butor==null){			
			butor = form.toPo(ShopDistributor.class);
			if(member.getPmember()!=null){
				butor.setParent(null);
			}
			this.shopDistributorService.applyShopDistributor(a, butor, member);
		}
		butor.setDisType(1);
		butor.setStatus(0);
		
		//地区信息
		String sn=CommUtil.null2String(form.get("area_id"));
		QueryObject qoo = new QueryObject();
		qoo.addQuery("obj.sn", sn, "=");
		List<?> list=this.systemRegionService.querySystemRegion(qoo).getResult();
		if(list!=null){
			SystemRegion systemRegion=(SystemRegion)list.get(0);
			butor.setArea(systemRegion);
		}
		
		this.shopDistributorService.updateShopDistributor(butor.getId(), butor);
		form.addResult("disStatus", "0");
		return error(form, "您的开店申请已经提交，系统正在处理...");
	}
	
	/**
	 * 判断微店申请的信息
	 * @param form
	 * @param member
	 * @return
	 */
	private Page checkDistributorApply(WebForm form, ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		qo.setPageSize(1);
		List<ShopDistributor> lists = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			
		ShopDistributor distributor = null;
		if(lists!=null && lists.size()>0){
			distributor = lists.get(0);
			/*if(distributor.getStatus() == 2){
				return error(form, "抱歉，您的申请已经被拒绝，如有疑问请联系管理员！");
			}else */
			if(distributor.getStatus()==0 || distributor.getStatus()==-1){
				return error(form, "您的开店申请已经提交，系统正在处理...");
			}else if(distributor.getStatus() == 1){
				return error(form, "您已经升级成为微店，不需再提交申请！");
			}
		}
		
		return null;
	}
	
	/**
	 * 判断体验店申请的信息
	 * @param form
	 * @param member
	 * @return
	 */
	private Page checkEntityApply(WebForm form, ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		qo.setPageSize(1);
		List<ShopDistributor> lists = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			
		ShopDistributor distributor = null;
		if(lists!=null && lists.size()>0){
			distributor = lists.get(0);			
			if(distributor.getDisType().toString().equals("2") && (distributor.getExStatus()==-1 || distributor.getExStatus()==0)){
				return error(form, "您的开店申请已经提交，系统正在处理...");
			}else if(distributor.getExStatus()==1){
				return error(form, "您已经升级成为实体店，不需再提交申请！");
			}
		}
		
		return null;
	}
	
	/**
	 * 进入我的分销商信息页
	 * @param form
	 * @return
	 */
	public Page doToDistributorInfo(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		qo.setPageSize(-1);
		List<?> list =this.shopDistributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor distributor = null;
		if(list!=null && list.size()>0){
			distributor = (ShopDistributor) list.get(0);
			/*if(distributor.getExStatus()<0){
				if(distributor.getStatus()!=1){
					form.addResult("disStatus", distributor.getStatus());
					return new Page("/bcd/wxshop/distributor/addfx.html");
				}
			}*/
			boolean isAccess = false;
			if(distributor.getDisType().toString().equals("1") && distributor.getStatus().toString().equals("1")){
				isAccess = true;
			}else if(distributor.getDisType().toString().equals("2")){
				if(distributor.getMember().getDisType()==0){
					if(distributor.getExStatus()==1){
						isAccess = true;
					}
				}else{
					isAccess = true;
				}
			}
			if(!isAccess){
				return error(form, "亲，您的开店申请还在审核中，请耐心等待！");
			}
			
			form.addResult("obj", distributor);
			/**
			 * 我的分销团队
			 */
			qo = new QueryObject();
			qo.addQuery("(obj.parent.id="+distributor.getId()+" or obj.parent.parent.id="+distributor.getId()+")");
			qo.setPageSize(-1);
			List<?> mychildrenDises = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			form.addResult("myChildrenNum", mychildrenDises==null?"0":mychildrenDises.size());
			/**
			 * 我的分销团队结束
			 * 查询我的粉丝
			 */
			qo=new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.addQuery("obj.disType",Integer.parseInt("0"),"=");
			qo.setPageSize(-1);
			List<?> myMembers = this.getShopMemberService().getShopMemberBy(qo).getResult();
			form.addResult("mymemberNum", myMembers==null?0:myMembers.size());
			/**
			 * 查询我的粉丝结束
			 * 查询我的订单
			 */
			qo = new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.addQuery("obj.user.id",member.getId(),"!=");
			qo.setPageSize(-1);
			List<?> myOrderlist = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("myOrderNums", myOrderlist==null?"0":myOrderlist.size());
			/**
			 * 查询我的订单结束
			 * 查询代理商订单
			 * 
			 */
			qo = new QueryObject();
			qo.addQuery("(obj.distributor.parent.id="+distributor.getId()+" or obj.distributor.parent.parent.id="+distributor.getId()+")");
			qo.setPageSize(-1);
			List<?> mydailiorders = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("myAgentOrderNum", mydailiorders==null?"0":mydailiorders.size());
			/**
			 * 查询代理订单结束
			 * 查询待处理订单
			 */
			qo = new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.addQuery("obj.status",Integer.parseInt("0"),"=");
			qo.setPageSize(-1);
			List<?> orderStatus0s = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("myOrderNums0", orderStatus0s==null?0:orderStatus0s.size());
			
			/**
			 * 查询待处理订单结束
			 * 查询已发货订单
			 */
			qo = new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.addQuery("obj.status",Integer.parseInt("2"),"=");
			qo.setPageSize(-1);
			List<?> orderStatus1s = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("myOrderNums2", orderStatus1s==null?0:orderStatus1s.size());
			/**
			 * 查询已发货订单结束
			 * 查询已完成订单
			 */
			qo = new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.addQuery("obj.status",Integer.parseInt("3"),"=");
			qo.setPageSize(-1);
			List<?> orderStatus2s = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("myOrderNums3", orderStatus2s==null?0:orderStatus2s.size());
			/**
			 * 查询已完成订单订单结束
			 * 查询我的提现申请
			 */
			qo = new QueryObject();
			qo.addQuery("obj.distributor.id",distributor.getId(),"=");
			qo.setPageSize(-1);
			List<?> commissionapply = this.commissionWithdrawService.getCommissionWithdrawBy(qo).getResult();
			form.addResult("myApply", commissionapply==null?0:commissionapply.size());
			/**
			 * 查询体验店下所有的会员
			 */
			if(distributor.getExStatus() ==1){
				qo = new QueryObject();
				qo.addQuery("(obj.distributor.topDistributor.id = "+distributor.getId()+"or obj.distributor.id = "+distributor.getId()+")");
				qo.addQuery("obj.disType",Integer.parseInt("0"),"=");
				qo.setPageSize(-1);
				List<?> allMember = this.getShopMemberService().getShopMemberBy(qo).getResult();
				form.addResult("myAllMember", allMember==null?0:allMember.size());
				/**
				 * 查询体验店下所有的微店
				 */
				qo = new QueryObject();
				qo.addQuery("obj.topDistributor.id",distributor.getId(),"=");
				qo.addQuery("obj.id",distributor.getId(),"!=");
				qo.setPageSize(-1);
				List<?> topDistri = this.shopDistributorService.getShopDistributorBy(qo).getResult();
				form.addResult("myWeidian", topDistri==null?0:topDistri.size());
				/**
				 * 查询体验店下所有的订单
				 */
				qo = new QueryObject(); 
				qo.addQuery("(obj.topDistributor.id="+distributor.getId()+" or obj.distributor.id ="+distributor.getId()+")");
				qo.addQuery("obj.user.id",member.getId(),"!=");
				qo.setPageSize(-1);
				List<?> topDistriOrder = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
				form.addResult("myAllOrder", topDistriOrder==null?0:topDistriOrder.size());
			}
		}
		return new Page("/bcd/distribution/member/distributorIndex.html");
	}
	/**
	 * 查询我的分销团队
	 * @param form
	 * @return
	 */
	public Page doMyChilren(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		//查询一级微店
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		List<?> list =this.shopDistributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor distributor = null;
		if(list!=null && list.size()>0){
			distributor = (ShopDistributor) list.get(0);
			qo = form.toPo(QueryObject.class);
			qo.addQuery("obj.parent.id",distributor.getId(),"=");
			qo.setOrderBy("createDate");
			qo.setOrderType("desc");
			qo.setPageSize(-1);
			IPageList pl1 = this.shopDistributorService.getShopDistributorBy(qo);
			form.addResult("pl1", pl1);
		}
		ShopDistributor distributor2 = null;
		if(list!=null && list.size()>0){
			distributor2 = (ShopDistributor) list.get(0);
			qo = form.toPo(QueryObject.class);
			qo.addQuery("obj.parent.parent.id",distributor2.getId(),"=");
			qo.setOrderBy("createDate");
			qo.setOrderType("desc");
			qo.setPageSize(-1);
			IPageList pl2 = this.shopDistributorService.getShopDistributorBy(qo);
			form.addResult("pl2", pl2);
		}
		return new Page("/bcd/distribution/member/dismember.html");
	}
	/**
	 * 查询我的粉丝
	 * @param form
	 * @return
	 */
	public Page doMyFollower(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		//查询一级粉丝会员
		
		ShopDistributor distributor = member.getMyDistributor();
		QueryObject qo=new QueryObject();
		qo.addQuery("obj.distributor.id",distributor.getId(),"=");
		qo.addQuery("obj.disType",Integer.parseInt("0"),"=");
		qo.setOrderBy("registerTime");
		qo.setOrderType("desc");
		qo.setPageSize(-1);
		IPageList pl1 = this.getShopMemberService().getShopMemberBy(qo);
		form.addResult("pl1", pl1);
		return new Page("/bcd/distribution/member/member.html");
	}
	/**
	 * 查询我的订单
	 * @param form
	 * @return
	 */
	public Page doMyOrder(WebForm form){
		String type = CommUtil.null2String(form.get("type"));
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		List<?> list =this.shopDistributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor distributor = null;
		if(list!=null && list.size()>0){
			distributor = (ShopDistributor) list.get(0);
			qo=new QueryObject();
			qo.addQuery("obj.user.id",member.getId(),"!=");
			if("".equals(type)){
				//全部订单
				qo.addQuery("obj.distributor",distributor,"=");
				qo.setOrderBy("status,ceateDate");
				qo.setOrderType("desc");
				qo.setPageSize(-1);
				IPageList pl =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl);
			}else if(type.equals("0")){
				//未支付订单
				qo.addQuery("obj.distributor",distributor,"=");
				qo.addQuery("obj.status",Integer.parseInt("0"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				qo.setPageSize(-1);
				IPageList pl0 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl0);
			}else if(type.equals("1")){
				//已支付未发货
				qo.addQuery("obj.distributor",distributor,"=");
				qo.addQuery("obj.status",Integer.parseInt("1"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				qo.setPageSize(-1);
				IPageList pl1 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl1);
			}else if(type.equals("2")){
				//已发货
				qo.addQuery("obj.distributor",distributor,"=");
				qo.addQuery("obj.status",Integer.parseInt("2"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				qo.setPageSize(-1);
				IPageList pl2 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl2);
			}else if(type.equals("3")){
				//已完成
				qo.addQuery("obj.distributor",distributor,"=");
				qo.addQuery("obj.status",Integer.parseInt("3"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				qo.setPageSize(-1);
				IPageList pl3 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl3);
			}
		
		}
		return new Page("/bcd/distribution/member/myOrder.html");
	}
	
	/**
	 * 查询下级微店订单
	 * @param form
	 * @return
	 */
	public Page doMyDisOrder(WebForm form){
		String type = CommUtil.null2String(form.get("type"));
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		List<?> list =this.shopDistributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor distributor = null;
		if(list!=null && list.size()>0){
			distributor = (ShopDistributor) list.get(0);
			qo=new QueryObject();
			qo.setPageSize(-1);
			qo.addQuery("(obj.distributor.parent.id="+distributor.getId()+" or obj.distributor.parent.parent.id="+distributor.getId()+")");
			if("".equals(type)){
				//全部订单
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				IPageList pl =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl);
			}else if(type.equals("0")){
				//未支付订单
				qo.addQuery("obj.status",Integer.parseInt("0"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				IPageList pl0 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl0);
			}else if(type.equals("1")){
				//已支付未发货
				qo.addQuery("obj.status",Integer.parseInt("1"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				IPageList pl1 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl1);
			}else if(type.equals("2")){
				//已发货
				qo.addQuery("obj.status",Integer.parseInt("2"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				IPageList pl2 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl2);
			}else if(type.equals("3")){
				//已完成
				qo.addQuery("obj.status",Integer.parseInt("3"),"=");
				qo.setOrderBy("ceateDate");
				qo.setOrderType("desc");
				IPageList pl3 =this.shopOrderInfoService.getShopOrderInfoBy(qo);
				form.addResult("pl", pl3);
			}
		
		}
		return new Page("/bcd/distribution/member/disOrder.html");
	}
	
	/**
	 * 我为体验店时，我的微店
	 * @param form
	 * @return
	 */
	public Page doMyWeidian(WebForm form){
		String type = CommUtil.null2String(form.get("type"));
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member",member,"=");
		List<?> list =this.shopDistributorService.getShopDistributorBy(qo).getResult();
		ShopDistributor distributor = null;
		if(list!=null && list.size()>0){
			distributor = (ShopDistributor) list.get(0);
			qo = new QueryObject();
			qo.addQuery("obj.topDistributor.id",distributor.getId(),"=");
			qo.addQuery("obj.id",distributor.getId(),"!=");
			qo.setOrderBy("createDate");
			qo.setOrderType("desc");
			qo.setPageSize(-1);
			List<?> topDistri = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			form.addResult("topDistri", topDistri);
		}
		return new Page("/bcd/distribution/member/distri_entityshop.html");
	}
	/**
	 * 我为体验店时，我所有的会员
	 * @param form
	 * @return
	 */
	public Page doMyAllMember(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		ShopDistributor distributor = member.getMyDistributor();
		QueryObject qo = new QueryObject();
		qo.addQuery("(obj.distributor.topDistributor.id = "+distributor.getId()+"or obj.distributor.id = "+distributor.getId()+")");
		qo.addQuery("obj.disType",Integer.parseInt("0"),"=");
		qo.setOrderBy("registerTime");
		qo.setOrderType("desc");
		qo.setPageSize(-1);
		List<?> allMember = this.getShopMemberService().getShopMemberBy(qo).getResult();
		form.addResult("allMember", allMember);
		
		return new Page("/bcd/distribution/member/myAllMembers.html");
	}
	/**
	 * 我为体验店时，我所有的订单
	 * @param form
	 * @return
	 */
	public Page doMyAllOrder(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		ShopDistributor distributor = member.getMyDistributor();
		QueryObject qo = new QueryObject();
		qo = new QueryObject();
		qo.addQuery("(obj.topDistributor.id="+distributor.getId()+"or obj.distributor.id ="+distributor.getId()+")");
		qo.addQuery("obj.user.id",member.getId(),"!=");
		qo.setPageSize(-1);
		List<?> topDistriOrder = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		form.addResult("topDistriOrder", topDistriOrder);
		
		return new Page("/bcd/distribution/member/myAllOrders.html");
	}

	
	/**
	 * 我的店铺二维码
	 * @param form
	 * @return
	 */
	public Page doErweima(WebForm form){
		String id=CommUtil.null2String(form.get("id"));
		ShopDistributor distributor = this.shopDistributorService.getShopDistributor(Long.parseLong(id));
		form.addResult("distributor", distributor);
		return new Page("/bcd/distribution/member/erweima.html");
	}
	
	/**
	 * 跳转申请实体店页面
	 * @param form
	 * @return
	 */
	public Page doToApplayEntityShop(WebForm form){
		Integer id=CommUtil.null2Int(form.get("id"));
		ShopDistributor  shopDistributor=this.shopDistributorService.getShopDistributor((long)id);
		if(shopDistributor==null){
			return error(form,"你还不是分销商");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.lev", 1, "=");
		qo.setLimit(-1);
		List<?> list=this.systemRegionService.querySystemRegion(qo).getResult();
		form.addResult("shopDistributor", shopDistributor);
		form.addResult("rList", list);
		if(shopDistributor.getExStatus()<0){
			return new Page("/bcd/wxshop/distributor/addstd.html");
		}else{
			return new Page("/bcd/wxshop/distributor/addstd.html");
		}
	}
	
	/**
	 * 跳转申请实体店页面
	 * @param form
	 * @return
	 */
	public Page doJudgeNext(WebForm form){
		Map<String , String> map = new HashMap<String , String>();
		String sn=CommUtil.null2String(form.get("sn"));
		QueryObject qo = new QueryObject();
		qo.setPageSize(1);
		qo.addQuery("obj.parent.sn", sn, "=");
		List<?> list=this.systemRegionService.querySystemRegion(qo).getResult();
		if(list!=null){
			map.put("status", "1");
		}else{
			map.put("status", "0");
		}
		form.jsonResult(map);
		return Page.JSONPage;
	}
	public Page doInitRecode(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		ShopDistributor dis = member.getMyDistributor();
		this.shopMemberService.confirmweidian(dis);
		Account account = null;
		account = member.getFollower().getAccount();
		account.setTenant(account.getTenant());
		QueryObject qo = new QueryObject();
		qo.addQuery("(obj.qRcodeImg is null)");
		qo.setPageSize(-1);
		List<ShopDistributor> distributors = this.shopDistributorService.getShopDistributorBy(qo).getResult();
		if(distributors!=null){
			int i=1;
			for(ShopDistributor sd:distributors){
				String url=WeixinBaseUtils.getDomain()+"/distributionCore.java?accountId="+account.getId()+"&pmemberId="+sd.getMember().getId();
				sd.setUrl(url);
				String imgName = System.currentTimeMillis()+""+i;
				String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();
		        path=path.replace("file:", ""); //去掉file:  
		        path=path.replace("classes/", ""); //去掉class\
		        path=path.replace("/WEB-INF/", "");
				String imgPath = UploadFileConstant.FILE_UPLOAD_PATH + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE+"/";
				try {
					QRCodeUtil.encode(url, null, path+imgPath, false,imgName);
					sd.setqRcodeImg(imgPath+imgName+".jpg");
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.shopDistributorService.updateShopDistributor(sd.getId(), sd);
				i++;
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("num", distributors==null?0:distributors.size());
		form.jsonResult(map);
		return DiscoShopUtil.goPage("/wxShopBase.java?cmd=init");
	}
	/*
	 * 手动生成二维码页面（慎用！！！）
	 */
	public Page doAddRecode(WebForm form){
		QueryObject qo = new QueryObject();
		qo.addQuery("(obj.qRcodeImg is null)");
		qo.setPageSize(-1);
		List<ShopDistributor> distributors = this.shopDistributorService.getShopDistributorBy(qo).getResult();
		if(distributors!=null){
			int i=1;
			for(ShopDistributor sd:distributors){
				String url=WeixinBaseUtils.getDomain()+"/distributionCore.java?accountId="+1+"&pmemberId="+sd.getMember().getId();
				sd.setUrl(url);
				String imgName = System.currentTimeMillis()+""+i;
				String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();
		        path=path.replace("file:", ""); //去掉file:  
		        path=path.replace("classes/", ""); //去掉class\
		        path=path.replace("/WEB-INF/", "");
				String imgPath = UploadFileConstant.FILE_UPLOAD_PATH + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE+"/";
				try {
					QRCodeUtil.encode(url, null, path+imgPath, false,imgName);
					sd.setqRcodeImg(imgPath+imgName+".jpg");
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.shopDistributorService.updateShopDistributor(sd.getId(), sd);
				i++;
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("num", distributors==null?0:distributors.size());
		form.jsonResult(map);
		return Page.JSONPage;
	}
	
	
    /**
     * 用户申请提现跳转
     * 
     * @param form
     */
    public Page doBeforeWithdrawApply(WebForm form) {
        String disid = CommUtil.null2String(form.get("disid"));
        ShopDistributor distri = this.shopDistributorService.getShopDistributor(Long.parseLong(disid));
        form.addResult("distri",distri);
        return new Page("/bcd/distribution/member/inputcommission.html");
    }
    /**
     * 用户申请提现
     * 
     * @param form
     */
    public Page doWithdrawApply(WebForm form) {
        String commission = CommUtil.null2String(form.get("commission"));
        String disid = CommUtil.null2String(form.get("disid"));
        String openAccountName = CommUtil.null2String(form.get("openAccountName"));
        String bankCardNum = CommUtil.null2String(form.get("bankCardNum"));
        String openAccountType = CommUtil.null2String(form.get("openAccountType"));
        
        ShopDistributor distri = this.shopDistributorService.getShopDistributor(Long.parseLong(disid));
        distri.setBankCardNum(bankCardNum);
        distri.setOpenAccountName(openAccountName);
        distri.setOpenAccountType(openAccountType);
        this.shopDistributorService.updateShopDistributor(distri.getId(),distri);
        ShopMember user = distri.getMember();
        CommissionWithdraw cw = new CommissionWithdraw();
        Double withrrawCommission = 0.0;
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status", Short.parseShort("0"), "=");
        qo.addQuery("obj.user.id",user.getId(),"=");
        List<CommissionWithdraw> list = this.commissionWithdrawService.getCommissionWithdrawBy(qo).getResult();
        if(list != null && list.size() != 0){
        	for(CommissionWithdraw commw:list){
        		withrrawCommission += commw.getCommission();
        	}
        }
        if(distri.getDisCommission()>(Double.valueOf(commission)+ withrrawCommission)){
			cw.setCommission(Double.valueOf(commission));
		}else{
			return error(form,"申请提取的佣金大于余额佣金,或者您已经提过申请，不能再次申请");
		}
        cw.setDistributor(distri);
        cw.setUser(user);
        System.out.println("=====================提现申请时间："+new Date() +"======================");
        this.commissionWithdrawService.addCommissionWithdraw(cw);
        return DiscoShopUtil.goPage(DiscoShopUtil.getDomain()+"/commissionWithdraw.java?cmd=myList");
    }
    /**
     * 用户申请提现(用户端)记录
     * 
     * @param form
     */
    public Page doMyList(WebForm form) {
        ShopMember member = getShopMember(form);
        ShopDistributor distri = member.getMyDistributor();
        QueryObject qo = new QueryObject();
        if(member != null){
        	qo.addQuery("obj.user.id",member.getId(),"=");
        }
        if(distri != null){
        	qo.addQuery("obj.distributor.id",distri.getId(),"=");
        }
        qo.setPageSize(-1);
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pl = this.commissionWithdrawService.getCommissionWithdrawBy(qo);
        form.addResult("pl", pl);
        return new Page("/bcd/distribution/member/withdrawRecord.html");
    }
	
}
