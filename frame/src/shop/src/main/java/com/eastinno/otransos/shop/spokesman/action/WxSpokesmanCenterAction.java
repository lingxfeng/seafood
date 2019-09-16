package com.eastinno.otransos.shop.spokesman.action;

import java.util.List;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.shop.spokesman.domain.Subsidy;
import com.eastinno.otransos.shop.spokesman.service.IRestitutionService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.shop.spokesman.service.ISubsidyService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

public class WxSpokesmanCenterAction extends WxShopBaseAction{
	
	@Inject
	private ISpokesmanService spokesmanService;
	@Inject
	private IShopOrderInfoService shopOrderInfoService;
	@Inject
	private IRestitutionService restitutionService;
	@Inject
	private ISubsidyService subsidyService;
	
	/**
	 * 进入我的代言信息页
	 * @param form
	 * @return
	 */
	public Page doToSpokesmanInfo(WebForm form){
		ShopMember member =  getShopMember(form);
		QueryObject qo = new QueryObject();
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		if(sman == null || sman.getStatus() == 0 ){
			return error(form,"您还不是正式的代言人！暂时无法使用此功能！");
		}else{
			form.addResult("obj", sman);
			/**
			 * 我的团队业绩
			 */
			Float team = sman.getTeamAmount();
			form.addResult("team", team);
			/**
			 * 我的代言等级
			 * 我的补贴比例
			 */
			String levelName = "未知等级";
			Float levelRating = 0F;
			SpokesmanRating sr = sman.getSpokesmanRating();
			if(sr != null){
				levelName = sr.getName();
				levelRating = sr.getLeve();
			}
			form.addResult("levelName", levelName);
			form.addResult("levelRating", levelRating);
			/**
			 * 查询我的返现金额
			 * 
			 */
			Float myrestitution = sman.getTotalRestitution();
			form.addResult("myrestitution", myrestitution);
			/**
			 * 查询我的补贴金额
			 * 
			 * 
			 */
			Float mysubsidy = sman.getTotalSubsidy();
			form.addResult("mysubsidy", mysubsidy);
			/**
			 * 查询我的返现订单
			 *
			 */
			qo = new QueryObject();
			qo.addQuery("obj.user.id",sman.getMember().getId(),"=");
			qo.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
			qo.setPageSize(-1);
			List<?> orderStatus2 = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("orderStatus2", orderStatus2==null?0:orderStatus2.size());
			/**
			 * 查询我的取走订单
			 *
			 */
			qo = new QueryObject();
			qo.addQuery("obj.user.id",sman.getMember().getId(),"=");
			qo.addQuery("obj.isSpokesman",Short.parseShort("1"),"=");
			qo.setPageSize(-1);
			List<?> orderStatus1 = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("orderStatus1", orderStatus1==null?0:orderStatus1.size());
			
			/**
			 * 查询我的补贴订单
			 * 
			 */
			qo = new QueryObject();
			String subsidyName = Subsidy.class.getName();
			qo.addQuery("exists (select 1 from "+subsidyName+" t1 where obj.id = t1.orderInfo.id and( t1.level1.id="+sman.getId()+" or t1.level2.id = "+sman.getId()+" or t1.level3.id ="+sman.getId()+"))");
			qo.addQuery("obj.isSpokesman",Short.parseShort("0"),"!=");
			qo.setPageSize(-1);
			List<?> orderSubsidy = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
			form.addResult("orderSubsidy", orderSubsidy==null?0:orderSubsidy.size());
			/**
			 * 查询我的团队成员
			 * 
			 */
			qo = new QueryObject();
			qo.addQuery("(obj.dePath like '"+sman.getDePath()+"%')");
			qo.addQuery("obj.status",Short.parseShort("1"),"=");
			qo.addQuery("obj.id",sman.getId(), "!=");
			qo.setPageSize(-1);
			List<?> teamSpokesman = this.spokesmanService.getSpokesmanBy(qo).getResult();
			form.addResult("teamSpokesman", teamSpokesman==null?0:teamSpokesman.size());
		}
		return new Page("/bcd/wxshop/spokesman/center/spokesmanIndex.html");
	}
	/**
	 * 查询返现记录
	 *
	 */
	public Page doRestitution(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.spokesman.id",sman.getId(),"=");
		qo.setPageSize(-1);
		qo.setOrderBy("time");
		qo.setOrderType("desc");
		List<?> list = this.restitutionService.getRestitutionBy(qo).getResult();
		form.addResult("restitutionRecord", list);
		return new Page ("/bcd/wxshop/spokesman/center/restitutionRecord.html");
	}
	/**
	 * 查询补贴记录
	 *
	 */
	public Page doSubsidy(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		qo.addQuery("(obj.level1.id = "+sman.getId()+" or obj.level2.id = "+sman.getId()+" or obj.level3.id = "+sman.getId()+")");
		qo.setPageSize(-1);
		qo.setOrderBy("time");
		qo.setOrderType("desc");
		List<?> list = this.subsidyService.getSubsidyBy(qo).getResult();
		form.addResult("subsidyRecord", list);
		form.addResult("loacalid", sman.getId());
		return new Page ("/bcd/wxshop/spokesman/center/subsidyRecord.html");
	}
	/**
	 * 查询返现订单
	 * @return
	 */
	public Page doSubsidyOrder(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		String subsidyName = Subsidy.class.getName();
		qo.addQuery("obj.user.id",member.getId(),"=");
		qo.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
		qo.setPageSize(-1);
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderInfo> orderRestitution = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		form.addResult("orderRestitution", orderRestitution);
		return new Page ("/bcd/wxshop/spokesman/center/restitutionOrder.html");
	}
	/**
	 * 查询补贴订单
	 * @return
	 */
	public Page doRestitutionOrder(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		String subsidyName = Subsidy.class.getName();
		qo.addQuery("exists (select 1 from "+subsidyName+" t1 where obj.id = t1.orderInfo.id and( t1.level1.id="+sman.getId()+" or t1.level2.id = "+sman.getId()+" or t1.level3.id ="+sman.getId()+"))");
		qo.addQuery("obj.isSpokesman",Short.parseShort("0"),"!=");
		qo.setPageSize(-1);
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderInfo> orderSubsidy = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		form.addResult("orderSubsidy", orderSubsidy);
		return new Page ("/bcd/wxshop/spokesman/center/subsidyOrder.html");
	}
	/**
	 * 查询我的取走订单
	 *
	 */
	public Page doRestitutionOrder2(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id",sman.getMember().getId(),"=");
		qo.addQuery("obj.isSpokesman",Short.parseShort("1"),"=");
		qo.setPageSize(-1);
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderInfo> orderSubsidy2 = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		form.addResult("orderSubsidy2", orderSubsidy2);
		return new Page ("/bcd/wxshop/spokesman/center/restitutionOrder2.html");
	}
	/**
	 * 查询我的代言团队
	 *
	 */
	public Page doMySpokesTeam(WebForm form){
		ShopMember member =  getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
		Spokesman sman = member.getMySpokesman();
		QueryObject qo = new QueryObject();
		qo.addQuery("(obj.dePath like '"+sman.getDePath()+"%')");
		qo.addQuery("obj.status",Short.parseShort("1"),"=");
		qo.addQuery("obj.id",sman.getId(), "!=");
		qo.setPageSize(-1);
		List<Spokesman> teamSpokesmans = this.spokesmanService.getSpokesmanBy(qo).getResult();
		form.addResult("teamSpokesmans", teamSpokesmans);
		return new Page ("/bcd/wxshop/spokesman/center/teammates.html");
	}
	

	public ISpokesmanService getSpokesmanService() {
		return spokesmanService;
	}
	public void setSpokesmanService(ISpokesmanService spokesmanService) {
		this.spokesmanService = spokesmanService;
	}


	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}


	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public IRestitutionService getRestitutionService() {
		return restitutionService;
	}
	public void setRestitutionService(IRestitutionService restitutionService) {
		this.restitutionService = restitutionService;
	}
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
	
}
