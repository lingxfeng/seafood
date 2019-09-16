package com.eastinno.otransos.shop.usercenter.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.poi.util.StringUtil;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.trade.service.IShopPayMentService;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IApplyWithdrawCashService;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;

/**
 * ApplyWithdrawCashAction
 * @author 
 */
@Action
public class ApplyWithdrawCashAction extends AbstractPageCmdAction {
    @Inject
    private IApplyWithdrawCashService service;
    @Inject
    private IShopMemberService shopMemberService;
    @Inject
    private IShopPayMentService shopPayMentService;
    @Inject
    private IRemainderAmtHistoryService remainderAmtHistoryService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String nickname = CommUtil.null2String(form.get("nickname"));
    	String id = CommUtil.null2String(form.get("id"));
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        if(StringUtils.hasText(id)){
        	qo.addQuery("obj.shopMember.id", Long.valueOf(id), "=");
        }
        if(StringUtils.hasText(nickname)){
        	qo.addQuery("obj.shopMember.nickname like '%"+nickname+"%'");
        }
        IPageList iPageList = this.service.getApplyWithdrawCashBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("ApplyWithdrawCashList", iPageList.getResult());
        return new Page("/shopmanage/usercenter/shopmember/applyWithdrawingCashList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ApplyWithdrawCash entry = (ApplyWithdrawCash)form.toPo(ApplyWithdrawCash.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addApplyWithdrawCash(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ApplyWithdrawCash entry = this.service.getApplyWithdrawCash(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateApplyWithdrawCash(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delApplyWithdrawCash(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 审核用户提现
     * 
     * @param form
     */
    public Page doAuditingwithdrawCash(WebForm form) {
    	User user = ShiroUtils.getUser();
    	if(user==null){
    		this.addError("msg", "登录超时请重新登陆");
    		return pageForExtForm(form);
    	}
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        int status=CommUtil.null2Int(form.get("type"));
        ApplyWithdrawCash entry = this.service.getApplyWithdrawCash(id);
        ShopMember member=entry.getShopMember();
        if(member.getRemainderAmt()<entry.getSums()){
        	BigDecimal b = new BigDecimal(member.getRemainderAmt());
        	double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        	this.addError("msg", "此用户余额为"+f1+"，不能进行提款");
        	return pageForExtForm(form);
        }
        entry.setAuditor(ShiroUtils.getUser());
        entry.setAuditTime(new Date());
        if(status==1){
            String uuid=UUID.randomUUID().toString();
            entry.setUuid(uuid);
            // 调用接口 微信 
            if(entry.getShopMember().getFollower()!=null){
            	boolean b=this.shopPayMentService.withdrawcash(entry);
            	System.out.println("微信零钱打款："+b);
            	if(!b){
            		this.addError("msg", "微信零钱打款失败");
            		return pageForExtForm(form);
            	}else{
            		entry.setStatus((short)status);
            	}
            }
            //double sums=entry.getSums();
            BigDecimal amt = new BigDecimal(Double.toString(member.getRemainderAmt()));
	        BigDecimal sums = new BigDecimal(Double.toString(entry.getSums()));
            member.setRemainderAmt(amt.subtract(sums).doubleValue());
            this.shopMemberService.updateShopMember(member.getId(), member);
            this.remainderAmtHistoryService.addRemainderAmtHistory(member, 6, entry.getSums(), member.getNickname()+"提现，余额减少记录");
            entry.setBalance(member.getRemainderAmt());
            boolean ret = service.updateApplyWithdrawCash(id,entry);
        }else if(status==2){
        	entry.setStatus((short) status);
        	service.updateApplyWithdrawCash(id,entry);
        }
        return pageForExtForm(form);
    }
    
    public void setService(IApplyWithdrawCashService service) {
        this.service = service;
    }
    
	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public void setRemainderAmtHistoryService(
			IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}
}