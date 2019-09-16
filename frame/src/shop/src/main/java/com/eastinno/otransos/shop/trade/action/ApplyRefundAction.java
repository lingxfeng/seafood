package com.eastinno.otransos.shop.trade.action;

import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.distribu.domain.CommissionDetail;
import com.eastinno.otransos.shop.distribu.service.ICommissionDetailService;
import com.eastinno.otransos.shop.trade.domain.ApplyRefund;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.service.IApplyRefundService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.shopMsgUtil;

/**
 * ApplyRefundAction
 * @author 
 */
@Action
public class ApplyRefundAction extends AbstractPageCmdAction {
    @Inject
    private IApplyRefundService service;
    
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    
    @Inject
    private ICommissionDetailService commissionDetailService;
    
    @Inject
    private IShopMemberService shopMemberService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getApplyRefundBy(qo);
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
        ApplyRefund entry = (ApplyRefund)form.toPo(ApplyRefund.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addApplyRefund(entry);
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
        ApplyRefund entry = this.service.getApplyRefund(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateApplyRefund(id,entry);
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
        this.service.delApplyRefund(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 退款/退货
     * 
     * @param form
     */
    public Page doRefund(WebForm form) {
    	String type=CommUtil.null2String(form.get("type"));//type 1：同意 2：拒绝
    	Long orderId=Long.valueOf(CommUtil.null2Int(form.get("orderId")));
    	String freight=(CommUtil.null2String((form.get("freight"))));//运费
    	Double f=freight==""?null:Double.valueOf(freight);
    	//查询退款订单
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.order.id", orderId , "=");
    	qo.setPageSize(1);
    	List<ApplyRefund> orderList=this.service.getApplyRefundBy(qo).getResult();
    	if(orderList==null){
    		this.addError("msg", "没有获取订单退货申请");
    		return pageForExtForm(form);
    	}
    	ApplyRefund applyRefund = (ApplyRefund)orderList.get(0);
    	if(applyRefund.getStatus()!=0){
    		this.addError("msg", "订单已经审核");
    		return pageForExtForm(form);
    	}
    	ShopOrderInfo order=applyRefund.getOrder();
    	if(f!=null){
    		if((double)f<0){
    			this.addError("msg", "运费的价格必须大于0");
    			return pageForExtForm(form);
    		}
    		if(order.getGross_price()<f){
    			this.addError("msg", "订单的价格应大于运费的价格，请重新输入");
    			return pageForExtForm(form);
    		}
    		applyRefund.setFreight(f);
    	}
    	//查询佣金分配
    	CommissionDetail  commissionDetail = this.commissionDetailService.getCommissionDetail("order", order);
//    	if(order.getUser().getDisType()!=2 &&(commissionDetail==null || commissionDetail.getStatus()==2)){
//    		this.addError("msg", "没有查询到此订单或此订单已退款");
//    		return pageForExtForm(form);
//    	}
    	if(commissionDetail!=null){
    		if(commissionDetail.getStatus()==2){
    			this.addError("msg", "没有查询到此订单或此订单已退款");
    			return pageForExtForm(form);
    		}
    	}
    	if("1".equals(type)){
    		//退款 0:会员 1:微店 2：店
    		if(order.getUser().getDisType()==0){
    			//退回积分
    			this.shopMemberService.returnIntegrall(order,"退款退货扣除积分");
    			//退回佣金
    			if(commissionDetail!=null){
    				this.commissionDetailService.returnCommissionDetail(order.getUser(),commissionDetail);
    			}
    			
    		}else if(order.getUser().getDisType()==1){
    			//返回佣金
    			if(commissionDetail!=null){
    				this.commissionDetailService.returnCommissionDetail(order.getUser(),commissionDetail);
    			}
    		}
    		order.setStatus(5);
    		this.shopMemberService.returnmoney(applyRefund,order,f);//返钱
    		shopMsgUtil.SendRefundInfo(order.getUser(), order,"1");
    	}else if("2".equals(type)){
    		order.setStatus(6);
    		shopMsgUtil.SendRefundInfo(order.getUser(), order,"2");
    	}else{
    		this.addError("msg", "请重新登陆");
    		return pageForExtForm(form);
    	}
    	applyRefund.setUser(ShiroUtils.getUser());
    	applyRefund.setStatus(Short.valueOf(type));
    	applyRefund.setAuditTime(new Date());
    	this.service.updateApplyRefund(applyRefund.getId(), applyRefund);//修改退货退款状态
    	this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);//修改订单状态
        return pageForExtForm(form) ;
    }
    
    /**
	 * 跳转错误页面
	 * @param form
	 * @return
	 */
	public Page error(WebForm form,String msg){
		form.addResult("msg", msg);
		return new Page("/userCenter/error.html");
	}
	
	/**
     * 退货列表
     * 
     * @param form
     */
    public Page doApplyRefundList(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	String nickname=CommUtil.null2String(form.get("nickname"));
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	if(StringUtils.hasText(id)){
    		qo.addQuery("obj.shopMember.id", Long.valueOf(id), "=");
    	}
    	if(StringUtils.hasText(nickname)){
    		qo.addQuery("obj.shopMember.nickname like '%"+nickname+"%'");
    	}
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList iPageList=this.service.getApplyRefundBy(qo);
        CommUtil.saveIPageList2WebForm(iPageList, form);
        form.addResult("applyRefundList", iPageList.getResult());
        return new Page("/shopmanage/trade/ShopOrderInfo/applyRefundList.html");
    }
    
    public void setService(IApplyRefundService service) {
        this.service = service;
    }

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public void setCommissionDetailService(
			ICommissionDetailService commissionDetailService) {
		this.commissionDetailService = commissionDetailService;
	}
}