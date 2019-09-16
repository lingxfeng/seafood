package com.eastinno.otransos.seafood.distribu.action;

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
import com.eastinno.otransos.seafood.distribu.domain.CommissionConfig;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributorRating;
import com.eastinno.otransos.seafood.distribu.service.ICommissionConfigService;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorRatingService;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;

/**
 * ShopDistributorRatingAction
 * @author 
 */
@Action
public class ShopDistributorRatingAction extends AbstractPageCmdAction {
    @Inject
    private IShopDistributorRatingService service;
    @Inject
    private ICommissionConfigService commissionConfigService;
    /**
     * 自动等级列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	QueryObject qo = form.toPo(QueryObject.class);
        qo.addQuery("obj.type",Short.parseShort("0"),"=");
        IPageList pl = this.service.getShopDistributorRatingBy(qo);
        QueryObject qo1 = form.toPo(QueryObject.class);
        qo1.addQuery("obj.isdefault",true,"=");
        IPageList pl2 = this.commissionConfigService.getCommissionConfigBy(qo1);
        Object entry = pl2.getResult().get(0);
        form.addResult("entry", entry);
        form.addResult("pl", pl);
        return new Page("/bcd/distribution/level/distributeLevelList.html");
    }
    
    /**
     * 自定义等级列表页面
     * 
     * @param form
     */
    public Page doListCustom(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        qo.addQuery("obj.type",Short.parseShort("1"),"=");
        IPageList pl = this.service.getShopDistributorRatingBy(qo);
        QueryObject qo1 = form.toPo(QueryObject.class);
        qo1.addQuery("obj.isdefault",true,"=");
        IPageList pl2 = this.commissionConfigService.getCommissionConfigBy(qo1);
        Object entry = pl2.getResult().get(0);
        form.addResult("entry", entry);
        form.addResult("pl", pl);
        return new Page("/bcd/distribution/level/customLevelList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopDistributorRating entry = (ShopDistributorRating)form.toPo(ShopDistributorRating.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopDistributorRating(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }
    
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));//分销等级ID
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopDistributorRating entry = this.service.getShopDistributorRating(id);
            form.addResult("entry", entry);
        }
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.isdefault",true,"=");
        IPageList ip = this.commissionConfigService.getCommissionConfigBy(qo);
        if(ip.getResult()!=null){
        	Object commission = ip.getResult().get(0);
        	form.addResult("commission", commission);
        }else{
        	String msg = "默认佣金分配规则未设置！";
        	form.addResult("msg", msg);
        }
       
        return new Page("/bcd/distribution/level/distributeLevelEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopDistributorRating entry = this.service.getShopDistributorRating(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopDistributorRating(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delShopDistributorRating(id);
        return go("list");
    }
    
    public void setService(IShopDistributorRatingService service) {
        this.service = service;
    }

	public ICommissionConfigService getCommissionConfigService() {
		return commissionConfigService;
	}

	public void setCommissionConfigService(
			ICommissionConfigService commissionConfigService) {
		this.commissionConfigService = commissionConfigService;
	}
    
}