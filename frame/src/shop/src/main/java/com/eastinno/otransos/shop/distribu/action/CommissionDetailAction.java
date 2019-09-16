package com.eastinno.otransos.shop.distribu.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.distribu.domain.CommissionDetail;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.query.CommissionDetailQuery;
import com.eastinno.otransos.shop.distribu.service.ICommissionDetailService;
import com.eastinno.otransos.shop.distribu.service.ICommissionWithdrawService;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.domain.RemainderAmtHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IApplyWithdrawCashService;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.formatUtil;

/**
 * CommissionDetailAction
 * @author 
 */
@Action
public class CommissionDetailAction extends AbstractPageCmdAction {
    @Inject
    private ICommissionDetailService service;
    @Inject
    private IShopDistributorService shopDistributorService;
    @Inject
    private IRemainderAmtHistoryService remainderAmtHistoryService;
    @Inject
    private IShopMemberService shopMemberService;
    @Inject
    private ICommissionWithdrawService commissionWithdrawService;
    @Inject
    private IApplyWithdrawCashService applyWithdrawCashService;
    
    /**
     * 会员利润详情列表页面
     * @param form
     * @return
     */
    public Page doListCommissionDetail(WebForm form) {
    	String distributorId = CommUtil.null2String(form.get("id"));
    	Long id = Long.parseLong(distributorId);
    	String starttime = CommUtil.null2String(form.get("starttime"));
    	String endtime = CommUtil.null2String(form.get("endtime"));
    	
    	Long startdate=0L;
    	Long enddate=0L;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	if(!"".equals(starttime)){
    		try {
    			startdate=(sdf.parse(starttime)).getTime();
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
			qo.addQuery("obj.createTime",startdate, ">");
		}
		if(!"".equals(endtime)){
			try {
    			enddate=(sdf.parse(endtime)).getTime();
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
			qo.addQuery("obj.createTime",enddate, "<");
		}
        qo.addQuery("(obj.levelonedistri.id="+id+" or obj.leveltowdistri.id="+id+"or obj.balanceonedistri.id="+id+" or obj.balancetowdistri.id="+id+")");
        qo.setPageSize(-1);
        IPageList pl = this.service.getCommissionDetailBy(qo);
        form.addResult("pl", pl);
        form.addResult("distributorId", distributorId);
        
        //统计各类利润
        Double level1=0.0;
        Double level2=0.0;
        Double balance=0.0;
        ShopDistributor sd = this.shopDistributorService.getShopDistributor(id);
        List<CommissionDetail> list = pl.getResult();
        if(list != null && list.size() != 0){
        	for(CommissionDetail cd:list){
        		if(cd.getLevelonedistri() == sd){
        			level1 += cd.getLevelonecommission();
        		}
        		if(cd.getLeveltowdistri() == sd){
        			level2 += cd.getLeveltowcommission();
        		}
        		if(cd.getBalanceonedistri() == sd ){
        			balance += cd.getBalanceone();
        		}
        		if(cd.getBalancetowdistri() == sd ){
        			balance += cd.getBalancetow();
        		}
        		}
        }
        form.addResult("sd", sd);
        form.addResult("level1", level1);
        form.addResult("level2", level2);
        form.addResult("balance", balance);
        form.addResult("fu", formatUtil.fu);
        return new Page("/d_shop/distribu/shopdistributor/commissiondetail.html");
    }
    
    
    /**
     * 将老的佣金记录数据存到用户账户记录表中
     * @param form
     * @return
     */
    public Page doChangeCommissionDetail(WebForm form) {
    	//将老的佣金数据存到用户表中
    	QueryObject qos = new QueryObject();
    	qos.addQuery("obj.totalCommission",Double.parseDouble("0"), "!=");
    	qos.setPageSize(-1);
    	List<ShopDistributor> list1 = this.shopDistributorService.getShopDistributorBy(qos).getResult();
    	if(list1 != null && list1.size() != 0){
    		for(ShopDistributor sd:list1){
    			ShopMember member = sd.getMember();
    			member.setRemainderAmt(member.getRemainderAmt() + sd.getDisCommission());
    			member.setDisCommission(sd.getTotalCommission());
    			this.shopMemberService.updateShopMember(member.getId(),member);
    		}
    	}
    	
    	//将老的佣金提现数据存到用户表中
    	QueryObject qow = new QueryObject();
    	qow.setPageSize(-1);
    	List<CommissionWithdraw> list3 = this.commissionWithdrawService.getCommissionWithdrawBy(qow).getResult();
    	if(list3 != null && list3.size() != 0){
    		for(CommissionWithdraw cw:list3){
    			ShopDistributor dis = cw.getDistributor();
    			ApplyWithdrawCash aw = new  ApplyWithdrawCash();
    			aw.setType(Short.parseShort("1"));
    			aw.setStatus(cw.getStatus());
    			aw.setAuditTime(cw.getPayedTime());
    			aw.setCreateDate(new Date(cw.getCreateTime()));
    			aw.setBankCardNum(dis.getBankCardNum());
    			aw.setOpenAccountAddress(dis.getOpenAccountType());
    			aw.setOpenAccountName(dis.getOpenAccountName());
    			aw.setSums(cw.getCommission());
    			aw.setShopMember(cw.getUser());
    			this.applyWithdrawCashService.addApplyWithdrawCash(aw);
    		}
    	}
    	//将老的佣金记录数据存到用户账户记录表中
    	QueryObject qo = new QueryObject();
    	qo.setPageSize(-1);
    	List<CommissionDetail> list2 = this.service.getCommissionDetailBy(qo).getResult();
    	if(list2 != null && list2.size() != 0){
    		for(CommissionDetail cd:list2){
    			ShopDistributor disa = cd.getBalanceonedistri();
    			Double coma = cd.getBalanceone();
    			if(disa != null){
    				RemainderAmtHistory ra = new RemainderAmtHistory();
    				ra.setCreateDate(new Date(cd.getCreateTime()));
    				ra.setDescription("一级利润获取");
    				ra.setUser(disa.getMember());
    				ra.setAmt(coma);
    				ra.setType(5);
    				this.remainderAmtHistoryService.addRemainderAmtHistory(ra) ;
    			}
    			ShopDistributor disb = cd.getLevelonedistri();
    			Double comb = cd.getLevelonecommission();
    			if(disb != null){
    				RemainderAmtHistory ra = new RemainderAmtHistory();
    				ra.setCreateDate(new Date(cd.getCreateTime()));
    				ra.setDescription("一级佣金获取");
    				ra.setUser(disb.getMember());
    				ra.setAmt(comb);
    				ra.setType(5);
    				this.remainderAmtHistoryService.addRemainderAmtHistory(ra) ;
    			}
    			ShopDistributor disc = cd.getLeveltowdistri();
    			Double comc = cd.getLeveltowcommission();
    			if(disc != null){
    				RemainderAmtHistory ra = new RemainderAmtHistory();
    				ra.setCreateDate(new Date(cd.getCreateTime()));
    				ra.setDescription("二级佣金获取");
    				ra.setUser(disc.getMember());
    				ra.setAmt(comc);
    				ra.setType(5);
    				this.remainderAmtHistoryService.addRemainderAmtHistory(ra) ;
    			}
    			ShopDistributor disd = cd.getBalancetowdistri();
    			Double comd = cd.getBalancetow();
    			if(disd != null){
    				RemainderAmtHistory ra = new RemainderAmtHistory();
    				ra.setCreateDate(new Date(cd.getCreateTime()));
    				ra.setDescription("差额利润获取");
    				ra.setUser(disd.getMember());
    				ra.setAmt(comd);
    				ra.setType(5);
    				this.remainderAmtHistoryService.addRemainderAmtHistory(ra) ;
    			}
    			
    		}
    	}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("结果", "成功！");
		form.jsonResult(map);
		return Page.JSONPage;   	
    }
    /**
     * 将老的佣金数据存到用户表中
     * @param form
     * @return
     */
    public Page doChangeCommission(WebForm form) {
    	
    	
		return forwardPage;
    	
    }
    public void setService(ICommissionDetailService service) {
        this.service = service;
    }

	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}

	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}


	public IRemainderAmtHistoryService getRemainderAmtHistoryService() {
		return remainderAmtHistoryService;
	}


	public void setRemainderAmtHistoryService(
			IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}


	public ICommissionDetailService getService() {
		return service;
	}


	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}


	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}


	public ICommissionWithdrawService getCommissionWithdrawService() {
		return commissionWithdrawService;
	}


	public void setCommissionWithdrawService(
			ICommissionWithdrawService commissionWithdrawService) {
		this.commissionWithdrawService = commissionWithdrawService;
	}


	public IApplyWithdrawCashService getApplyWithdrawCashService() {
		return applyWithdrawCashService;
	}


	public void setApplyWithdrawCashService(
			IApplyWithdrawCashService applyWithdrawCashService) {
		this.applyWithdrawCashService = applyWithdrawCashService;
	}

	
    
}