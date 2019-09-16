package com.eastinno.otransos.shop.promotions.action;

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
import com.eastinno.otransos.shop.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.shop.promotions.query.RushBuyRecordQuery;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRegularService;

/**
 * RushBuyRecordAction
 * @author 
 */
@Action
public class RushBuyRecordAction extends AbstractPageCmdAction {
	@Inject
    private IRushBuyRecordService recordService;
	@Inject
    private IRushBuyRegularService regularService;
	
	public IRushBuyRecordService getRecordService() {
		return recordService;
	}
	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}
	public IRushBuyRegularService getRegularService() {
		return regularService;
	}
	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}

        
    /**
     * 秒杀下单用户列表页面
     * 
     * @param form
     */
    public Page doSecKillRecordList(WebForm form) {
        RushBuyRecordQuery qo = (RushBuyRecordQuery) form.toPo(RushBuyRecordQuery.class);
        qo.addQuery("obj.regular.activityType", 0, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList pageList = this.recordService.getRushBuyRecordBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/promotions/seckill/seckillRecordList.html");
    }
    /**
     * 秒杀记录修改页
     * @param form
     */
    public Page doToSecKillRecordUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRecord entry = this.recordService.getRushBuyRecord(id);
        form.addResult("entry", entry);            
        return new Page("/bcd/promotions/seckill/seckillRecordEdit.html");
    }
    
    /**
     * 修改秒杀记录数据
     * 
     * @param form
     */
    public Page doSecKillRecordUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRecord entry = this.recordService.getRushBuyRecord(id);
        form.toPo(entry);
        if (!hasErrors()) {
            entry = this.recordService.updateSecKillRecord(entry);
            if(entry != null){
                form.addResult("msg", "修改成功");
            }
        }        
        return new Page("/bcd/promotions/seckill/seckillRecordEdit.html");
    }
    
    /**
     * 限时抢购下单用户列表页面
     * 
     * @param form
     */
    public Page doTimeLimitRecordList(WebForm form) {
        RushBuyRecordQuery qo = (RushBuyRecordQuery) form.toPo(RushBuyRecordQuery.class);
        qo.addQuery("obj.regular.activityType", 1, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList pageList = this.recordService.getRushBuyRecordBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/promotions/timelimit/timelimitRecordList.html");
    }
    /**
     * 修改限时抢购页
     * @param form
     */
    public Page doToTimeLimitRecordUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRecord entry = this.recordService.getRushBuyRecord(id);
        form.addResult("entry", entry);            
        return new Page("/bcd/promotions/timelimit/timelimitRecordEdit.html");
    }
    
    /**
     * 修改限时抢购数据
     * 
     * @param form
     */
    public Page doTimeLimitRecordUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRecord entry = this.recordService.getRushBuyRecord(id);
        form.toPo(entry);
        if (!hasErrors()) {
            entry = this.recordService.updateTimeLimitRecord(entry);
            if(entry != null){
                form.addResult("msg", "修改成功");
            }
        }        
        return new Page("/bcd/promotions/timelimit/timelimitRecordEdit.html");
    }
}