package com.eastinno.otransos.shop.spokesman.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.core.action.ShopBaseAction;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

/**
 * SpokesmanAction
 * @author 
 */
@Action
public class SpokesmanAction extends WxShopBaseAction {
    @Inject
    private ISpokesmanService service;
    @Inject
    private ISpokesmanRatingService spokesmanRatingService;
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String name = CommUtil.null2String(form.get("name"));
    	form.addResult("name",name);
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	 if(!"".equals(name)){
         	qo.addQuery("obj.member.nickname like '"+name+"%'");
         }
    	 qo.addQuery("obj.status",Short.parseShort("1"),"=");
        IPageList pageList = this.service.getSpokesmanBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl",pageList);
        return new Page("/bcd/spokesman/spokesman/spokesmanList.html");
    }
    
    /**
     * 跳转选择等级页面
     * 
     * @param form
     */
    public Page doChooseRating(WebForm form) {
    	String spokesid = CommUtil.null2String(form.get("spokesid"));
        if(!"".equals(spokesid)){
        	form.addResult("spokesid", spokesid);
        }
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        List<SpokesmanRating> list = this.spokesmanRatingService.getSpokesmanRatingBy(qo).getResult();
        form.addResult("pl", list);
        return new Page("/bcd/spokesman/spokesman/chooseRatingList.html");
    }
    /**
     * 更改为手动等级
     * 
     * @param form
     */
    public Page doChangeRating(WebForm form) {
    	String spokesid = CommUtil.null2String(form.get("spokesid"));
    	String ratingid = CommUtil.null2String(form.get("ratingid"));
    	Spokesman sman = this.service.getSpokesman(Long.parseLong(spokesid));
    	SpokesmanRating rating = this.spokesmanRatingService.getSpokesmanRating(Long.parseLong(ratingid));
    	sman.setSpokesmanRating(rating);
    	sman.setCustomRating(Short.parseShort("1"));
    	this.service.updateSpokesman(sman.getId(), sman);
        return go("list");
    }
    
    /**
     * 更改为自动等级
     * 
     * @param form
     */
    public Page doRecorver(WebForm form) {
    	String spokesid = CommUtil.null2String(form.get("spokesid"));
    	Spokesman sman = this.service.getSpokesman(Long.parseLong(spokesid));
    	sman.setCustomRating(Short.parseShort("0"));
    	sman.setSpokesmanRating(this.spokesmanRatingService.judgeRating(sman));
    	this.service.updateSpokesman(sman.getId(), sman);
        return go("list");
    }
    /**
     * 查看业绩累计详情
     * 
     * @param form
     */
    public Page doTeamAcount(WebForm form) {
    	String spokesid = CommUtil.null2String(form.get("spokesid"));
    	Spokesman sman = null;
    	if(!"".equals(spokesid)){
    		sman = this.service.getSpokesman(Long.parseLong(spokesid));
    	}
    	form.addResult("sman", sman);
    	QueryObject qo = new QueryObject();
    	qo.addQuery(" obj.user.id in (select t2.id from Disco_Shop_ShopMember t2 where t2.id in (select t1.member.id from Disco_Shop_Spokesman t1 where t1.dePath like '"+sman.getDePath()+"%')) and (obj.status = 1 or obj.status = 2 or obj.status = 3)");
    	qo.setPageSize(-1);
    	List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
    	form.addResult("list", list);
    	return new Page("/bcd/spokesman/spokesman/teamOrderList.html");
    }
   
    public void setService(ISpokesmanService service) {
        this.service = service;
    }

	public ISpokesmanRatingService getSpokesmanRatingService() {
		return spokesmanRatingService;
	}

	public void setSpokesmanRatingService(
			ISpokesmanRatingService spokesmanRatingService) {
		this.spokesmanRatingService = spokesmanRatingService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
    
}