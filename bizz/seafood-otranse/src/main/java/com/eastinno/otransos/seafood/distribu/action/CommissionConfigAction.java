package com.eastinno.otransos.seafood.distribu.action;

import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.distribu.domain.CommissionConfig;
import com.eastinno.otransos.seafood.distribu.query.CommissionConfigQuery;
import com.eastinno.otransos.seafood.distribu.service.ICommissionConfigService;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.query.ShopProductQuery;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.util.formatUtil;

/**
 * CommissionConfigAction
 * @author 
 */
@Action
public class CommissionConfigAction extends AbstractPageCmdAction {
    @Inject
    private ICommissionConfigService service;
    @Inject
    private IShopProductService  shopProductService;
    public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setShopProductService(
			IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	
	@Override
	public Object doBefore(WebForm form, Module module) {
		form.addResult("fu",formatUtil.fu);
		return super.doBefore(form, module);
	}

	/**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {    	
    	CommissionConfigQuery cc = form.toPo(CommissionConfigQuery.class);    	
        IPageList pl = this.service.getCommissionConfigBy(cc);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/bcd/distribution/commission/list.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        CommissionConfig entry = (CommissionConfig)form.toPo(CommissionConfig.class);
        form.toPo(entry);
        if (!hasErrors()) {
        	String productid = CommUtil.null2String(form.get("id"));
        	if(!"".equals(productid)){
        		ShopProduct shopproduct = this.shopProductService.getShopProduct(Long.parseLong(productid));
        		entry.setProduct(shopproduct);
        	}else{
        		entry.setIsdefault(true);
        	}
            Long id = this.service.addCommissionConfig(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
            
	          //修改关联商品
        }
        return go("list");
    }
    
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));//商品ID
        String configid = CommUtil.null2String(form.get("configid"));//佣金设置ID
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopProduct entry = this.shopProductService.getShopProduct(id);
            form.addResult("entry", entry);
        }
        if(!"".equals(configid)){
            Long cid = Long.valueOf(Long.parseLong(configid));
            CommissionConfig entry2 = this.service.getCommissionConfig(cid);
            form.addResult("entry2", entry2);
        }
        return new Page("/bcd/distribution/commission/addCustom.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        Long commissionid = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("commissionid"))));     
        CommissionConfig entry = this.service.getCommissionConfig(commissionid);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateCommissionConfig(commissionid,entry);
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
        this.service.delCommissionConfig(id);
        return go("list");
    }
    
    /**
     * 获取未设置佣金的商品
     * 
     * @param form
     */
    public Page doCommissionProduct2(WebForm form) {
    	//获取已设置自定义的商品id
    	String productid="0";
    	QueryObject ccq = form.toPo(QueryObject.class);
    	ccq.addQuery("obj.isdefault", false, "=");    	
    	ccq.setPageSize(-1);
    	IPageList pl = this.service.getCommissionConfigBy(ccq);
    	List<CommissionConfig> cc=pl.getResult();
    	if(cc!=null){
    		for(CommissionConfig commissionConfig:cc){
        		productid=productid+","+commissionConfig.getProduct().getId();
        	}
    	}
    	QueryObject qo2 = form.toPo(QueryObject.class);
    	qo2.addQuery("obj.id not in ("+productid+")");
    	String name = CommUtil.null2String(form.get("name"));
    	if(!name.equals("")){
    		qo2.addQuery("obj.name", "%"+name+"%", "like");
    	}
        IPageList pl2 = this.shopProductService.getShopProductBy(qo2);
        CommUtil.saveIPageList2WebForm(pl2, form);
        form.addResult("pl", pl2);
        return new Page("/bcd/distribution/commission/addCustomlist.html");
    }
    /**
     * 获取未设置佣金的商品
     * 
     * @param form
     */
    public Page doCommissionProduct(WebForm form) {    	
//	    String sqlStr = "SELECT * FROM disco_shop_product t where t.id not in(SELECT t1.product_id from disco_shop_commission t1 where t1.product_id is not null)";
//	    List<ShopProduct> list = this.service.querymethod(sqlStr);
    	QueryObject qo = form.toPo(ShopProductQuery.class);
	    String commissionConfigName = CommissionConfig.class.getName();
		qo.addQuery("not exists (select 1 from "+commissionConfigName+" t1 where t1.product is not null and t1.product.id=obj.id)");
		qo.setOrderBy("createDate");
        qo.setOrderType("desc");
		IPageList pl2 = this.shopProductService.getShopProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl2, form);
        form.addResult("pl", pl2);
        return new Page("/bcd/distribution/commission/addCustomlist.html");
    }
    
    /**
     * 增加默认佣金设置
     * 
     * @param form
     */
    public Page doDefaultConfig(WebForm form) {
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.isdefault",true, "=");
    	IPageList pl = this.service.getCommissionConfigBy(qo); 
    	if(pl.getResult()!=null){
    		Object entry = pl.getResult().get(0);
    		form.addResult("entry", entry);
    	}
    	
    	return new Page("/bcd/distribution/commission/addBase.html");
    }
    
    public void setService(ICommissionConfigService service) {
        this.service = service;
    }
}