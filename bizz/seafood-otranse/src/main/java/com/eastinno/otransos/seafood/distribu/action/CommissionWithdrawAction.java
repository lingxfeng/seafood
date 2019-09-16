package com.eastinno.otransos.seafood.distribu.action;

import java.util.Date;
import java.util.List;

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
import com.eastinno.otransos.seafood.core.action.WxShopBaseAction;
import com.eastinno.otransos.seafood.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.ICommissionWithdrawService;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.trade.service.IShopPayMentService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.formatUtil;

/**
 * CommissionWithdrawAction
 * @author 
 */
@Action
public class CommissionWithdrawAction extends WxShopBaseAction {
    @Inject
    private ICommissionWithdrawService service;    
	@Inject
    private IShopDistributorService shopDistributorService;
    @Inject
    private IShopPayMentService shopPayMentService;
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getCommissionWithdrawBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        CommissionWithdraw entry = (CommissionWithdraw)form.toPo(CommissionWithdraw.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addCommissionWithdraw(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
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
     * 用户申请提现(后台管理员)
     * 
     * @param form
     */
    public Page doApplyList(WebForm form) {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status",Short.parseShort("0"),"=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pl = this.service.getCommissionWithdrawBy(qo);
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/distribution/member/listApply.html");
    }
    /**
     * 确认支付
     * 
     * @param form
     */
    public Page doConfirmApply(WebForm form) {
        String applyid = CommUtil.null2String(form.get("id"));
        
        CommissionWithdraw cw = this.service.getCommissionWithdraw(Long.parseLong(applyid));
        if(cw.getStatus()==1){
        	System.out.println("=====================报告队长！发现重复提交！时间："+new Date() +"======================");
        }else{
        	ShopDistributor distributor = cw.getDistributor();
            Double oldcommission = distributor.getDisCommission();
            Double newcommission = oldcommission - cw.getCommission();
            distributor.setDisCommission(newcommission);
            this.shopDistributorService.updateShopDistributor(distributor.getId(), distributor);
            cw.setStatus(Short.parseShort("1"));
            cw.setPayedTime(new Date());
            cw.setRemaincommission(newcommission);
            System.out.println("=====================支付操作时间："+new Date() +"======================");
            this.service.updateCommissionWithdraw(cw.getId(), cw);
            this.shopPayMentService.withdrawcash(cw);
        }
        
        return DiscoShopUtil.goPage(DiscoShopUtil.getDomain()+"/commissionWithdraw.java?cmd=payedList");
    }
    /**
     * 已支付列表
     * 
     * @param form
     */
    public Page doPayedList(WebForm form) {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status",Short.parseShort("1"),"=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pl = this.service.getCommissionWithdrawBy(qo);
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/distribution/member/listPayment.html");
    }
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delCommissionWithdraw(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(ICommissionWithdrawService service) {
        this.service = service;
    }
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	public IShopPayMentService getShopPayMentService() {
		return shopPayMentService;
	}

	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}

	public ICommissionWithdrawService getService() {
		return service;
	}   
}