package com.eastinno.otransos.seafood.promotions.action;

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
import com.eastinno.otransos.seafood.promotions.domain.SingleDispatchCouponRecord;
import com.eastinno.otransos.seafood.promotions.query.RushBuyRecordQuery;
import com.eastinno.otransos.seafood.promotions.service.ISingleDispatchCouponRecordService;

/**
 * SingleDispatchCouponRecordAction
 * @author 
 */
@Action
public class SingleDispatchCouponRecordAction extends AbstractPageCmdAction {
    @Inject
    private ISingleDispatchCouponRecordService service;
    /**
     * 单独发放列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.type",Short.parseShort("-1"),"=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pageList = this.service.getSingleDispatchCouponRecordBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
        return new Page("/bcd/promotions/coupons/recordList.html");
    }
    /**
     * 批量发放列表页面
     * 
     * @param form
     */
    public Page doPList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.type",Short.parseShort("-1"),"!=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pageList = this.service.getSingleDispatchCouponRecordBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
        return new Page("/bcd/promotions/coupons/pRecordList.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        SingleDispatchCouponRecord entry = (SingleDispatchCouponRecord)form.toPo(SingleDispatchCouponRecord.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addSingleDispatchCouponRecord(entry);
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
        SingleDispatchCouponRecord entry = this.service.getSingleDispatchCouponRecord(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSingleDispatchCouponRecord(id,entry);
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
        this.service.delSingleDispatchCouponRecord(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(ISingleDispatchCouponRecordService service) {
        this.service = service;
    }
}