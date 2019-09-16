package com.eastinno.otransos.seafood.droduct.action;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.domain.BrandType;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IBrandTypeService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * BrandTypeAction
 * @author 
 */
@Action
public class BrandTypeAction extends AbstractPageCmdAction {
    @Inject
    private IBrandTypeService service;
    @Inject
    private IBrandService brandService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String name=CommUtil.null2String(form.get("name"));
    	String code=CommUtil.null2String(form.get("code"));
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        if(StringUtils.hasText(name)){
        	qo.addQuery("obj.name like '%"+name+"%'");
        }
        if(StringUtils.hasText(code)){
        	qo.addQuery("obj.code", code, "=");
        }
        IPageList pageList = this.service.getBrandTypeBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("brandTypeList", pageList.getResult());
        form.addResult("status", 2);
        return new Page("/shopmanage/product/brand/brandTypeList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        BrandType entry = (BrandType)form.toPo(BrandType.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addBrandType(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        BrandType entry = this.service.getBrandType(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateBrandType(id,entry);
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
        this.service.delBrandType(id);
        return go("list");
    }
    
    /**
     * 进入品牌分类  编译页面
     * @param form
     * @return
     */
    public Page doToBrandType(WebForm form){
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	IPageList pList=this.service.getBrandTypeBy(qo);
    	CommUtil.saveIPageList2WebForm(pList, form);
    	form.addResult("brandTypeList", pList.getResult());
    	return new Page("/shopmanage/product/brand/BrandTypeList.html");
    }
    
    /**
     * 得到品牌分类
     * @param form
     * @return
     */
    public Page doGetBrandTypeList(WebForm form){
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	IPageList pList=this.service.getBrandTypeBy(qo);
    	CommUtil.saveIPageList2WebForm(pList, form);
    	form.addResult("brandTypeList", pList.getResult());
    	return new Page("/shopmanage/product/brand/BrandTypeList.html");
    }
    
    /**
     * 进入品牌分类  编译页面
     * @param form
     * @return
     */
    public Page doToEditBrandType(WebForm form){
    	Long id=Long.valueOf(CommUtil.null2String(form.get("id")));
    	BrandType brandType=this.service.getBrandType(id);
    	form.addResult("brandType", brandType);
    	return new Page("/shopmanage/product/brand/BrandTypeEdit.html");
    }
    
    /**
     * 检查编码是否重复
     * @param form
     * @return
     */
   public Page doCheckCode(WebForm form) {
       String code=CommUtil.null2String(form.get("code"));
       QueryObject qo = new QueryObject();
       qo.addQuery("obj.code", code, "=");
       if(this.service.getBrandTypeBy(qo).getRowCount()>0){
    	   this.addError("msg", "编码已存在,清重新输入！！！");
       }
       return pageForExtForm(form);
   }
    
    /**
     * 进入品牌分类  编译页面
     * @param form
     * @return
     */
    public Page doToBrandTypeSave(WebForm form){
    	
    	return new Page("/shopmanage/product/brand/BrandTypeEdit.html");
    }
    
    public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}

	public void setService(IBrandTypeService service) {
        this.service = service;
    }
}