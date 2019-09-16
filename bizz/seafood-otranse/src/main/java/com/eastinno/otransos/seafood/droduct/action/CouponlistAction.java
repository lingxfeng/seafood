package com.eastinno.otransos.seafood.droduct.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.Couponlist;
import com.eastinno.otransos.seafood.droduct.service.ICouponlistService;

/**
 * CouponlistAction
 * @author 
 */
@Action
public class CouponlistAction extends AbstractPageCmdAction {
    @Inject
    private ICouponlistService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getCouponlistBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
		AjaxUtil.convertEntityToJson(pageList);
        form.addResult("pl", pageList);
        return new Page("/meiyan/trade/Couponlist/CouponlistList.html");
    }
    
    public Page doAdd(WebForm form){
    	 return new Page("/meiyan/trade/Couponlist/CouponlistEdit.html");
    }
    /**
     * 根据优惠券状态查询
     * @param form
     * @return
     */
    
    public Page doViewList(WebForm form){
    	String couponState = CommUtil.null2String(form.get("couponState"));
    	QueryObject qo = new QueryObject();
    	if(!"".equals(couponState)){
    		qo.addQuery("obj.couponState",couponState,"=");
    	}
        IPageList pl = this.service.getCouponlistBy(qo);
        form.addResult("pl", pl);
        return new Page("/meiyan/trade/Couponlist/CouponlistList.html");
    }
    
    
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            Couponlist entry = this.service.getCouponlist(id);
            form.addResult("entry", entry);
        }
        return new Page("/meiyan/trade/Couponlist/CouponlistEdit.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     * @throws ParseException 
     */
    public Page doSave(WebForm form) throws ParseException {
        Couponlist entry = (Couponlist)form.toPo(Couponlist.class);
        Date d=new Date();   
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");  
		String endTime = entry.getEndTime();
		long end = df.parse(endTime).getTime();
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addCouponlist(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return new Page("/meiyan/trade/Couponlist/CouponlistEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     * @throws ParseException 
     */
    public Page doUpdate(WebForm form) throws ParseException {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        Couponlist entry = this.service.getCouponlist(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateCouponlist(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delCouponlist(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return go("list");
    }
    
    public void setService(ICouponlistService service) {
        this.service = service;
    }
}