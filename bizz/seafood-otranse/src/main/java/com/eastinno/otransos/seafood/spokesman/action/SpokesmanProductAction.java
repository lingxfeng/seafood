package com.eastinno.otransos.seafood.spokesman.action;

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
import com.eastinno.otransos.seafood.distribu.query.CommissionConfigQuery;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.query.ShopProductQuery;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.seafood.spokesman.query.SpokesmanProductQuery;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanProductService;

/**
 * SpokesmanProductAction
 * @author 
 */
@Action
public class SpokesmanProductAction extends AbstractPageCmdAction {
    @Inject
    private ISpokesmanProductService service;
    @Inject
    private IShopProductService  shopProductService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	SpokesmanProductQuery qo = form.toPo(SpokesmanProductQuery.class);
        IPageList pl = this.service.getSpokesmanProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/bcd/spokesman/product/spokesmanProductList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	SpokesmanProduct entry = (SpokesmanProduct)form.toPo(SpokesmanProduct.class);
         form.toPo(entry);
         if (!hasErrors()) {
         	String productid = CommUtil.null2String(form.get("id"));
         	if(!"".equals(productid)){
         		ShopProduct shopproduct = this.shopProductService.getShopProduct(Long.parseLong(productid));
         		entry.setProduct(shopproduct);
         	}
             Long id = this.service.addSpokesmanProduct(entry);
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
        String idStr = CommUtil.null2String(form.get("id"));//商品ID
        String spokesid = CommUtil.null2String(form.get("spokesid"));//佣金设置ID
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopProduct entry = this.shopProductService.getShopProduct(id);
            form.addResult("entry", entry);
        }
        if(!"".equals(spokesid)){
            Long sid = Long.valueOf(Long.parseLong(spokesid));
            SpokesmanProduct entry2 = this.service.getSpokesmanProduct(sid);
            form.addResult("entry2", entry2);
        }
        return new Page("/bcd/spokesman/product/spokesmanProductEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	 Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
         Long spokesid = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("spokesid"))));     
         SpokesmanProduct entry = this.service.getSpokesmanProduct(spokesid);
         form.toPo(entry);
         if (!hasErrors()) {
             boolean ret = service.updateSpokesmanProduct(spokesid, entry);
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
         this.service.delSpokesmanProduct(id);
         return go("list");
    }
    
    /**
     * 获取未成为代言商品的商品
     * 
     * @param form
     */
    public Page doGetProduct(WebForm form) {    	
    	QueryObject qo = form.toPo(ShopProductQuery.class);
	    String spokesmanProductName = SpokesmanProduct.class.getName();
		qo.addQuery("not exists (select 1 from "+spokesmanProductName+" t1 where t1.product is not null and t1.product.id=obj.id)");
		qo.setOrderBy("createDate");
        qo.setOrderType("desc");
		IPageList pl2 = this.shopProductService.getShopProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl2, form);
        form.addResult("pl", pl2);
        return new Page("/bcd/spokesman/product/spokesmanProductList2.html");
    }
    
    
    
    public void setService(ISpokesmanProductService service) {
        this.service = service;
    }

	public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
    
}