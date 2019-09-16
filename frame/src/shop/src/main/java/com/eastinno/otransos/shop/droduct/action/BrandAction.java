package com.eastinno.otransos.shop.droduct.action;

import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.shop.core.action.CheckLoginAction;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.query.BrandQuery;
import com.eastinno.otransos.shop.droduct.service.IBrandService;
import com.eastinno.otransos.shop.droduct.service.IBrandTypeService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 品牌管理
 * @author nsz
 */
@Action
public class BrandAction extends CheckLoginAction{
    @Inject
    private IBrandService service;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private IBrandTypeService brandTypeService;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String name=CommUtil.null2String(form.get("name"));
        QueryObject qo = new QueryObject();
        if(StringUtils.hasText(name)){
        	qo.addQuery("obj.name like '%"+name+"%'");
        }
        qo.setCurrentPage(CommUtil.null2Int(form.get("currentPage")));
        IPageList pl = this.service.getBrandBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        /*QueryObject qo2 = new QueryObject();
        qo2.setLimit(-1);
        List<?> bTypeList=this.brandTypeService.getBrandTypeBy(qo2).getResult();
        form.addResult("bTypeList", bTypeList);*/
        return new Page("/shopmanage/product/brand/BrandList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	setBrandType(form);
    	return new Page("/shopmanage/product/brand/BrandEdit.html");
    }
    
    /**
     * 保存数据
     * 
     * @param forms
     */
    public Page doSave(WebForm form) {
        Brand entry = (Brand)form.toPo(Brand.class);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        	entry.setImgPath(imgPath);
        }else{
        	this.addError("msg", "品牌图标不能为空！！！");
        }
        if (!hasErrors()) {
            Long id = this.service.addBrand(entry);
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
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            Brand entry = this.service.getBrand(id);
            form.addResult("entry", entry);
            setBrandType(form);
        }
        return new Page("/shopmanage/product/brand/BrandEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        Brand entry = this.service.getBrand(id);
        form.toPo(entry);
        String oldImgPath = entry.getImgPath();
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            boolean ret = service.updateBrand(id, entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
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
       if(this.service.getBrandBy(qo).getRowCount()>0){
    	   this.addError("msg", "编码已存在,清重新输入！！！");
       }
       return pageForExtForm(form);
   }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delBrand(id);
        return go("list");
    }
    
    public void setBrandTypeService(IBrandTypeService brandTypeService) {
		this.brandTypeService = brandTypeService;
	}
	public void setService(IBrandService service) {
        this.service = service;
    }
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
    private void setBrandType(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setOrderBy("sequence");
    	qo.setLimit(-1);
        List<?> bTypeList=this.brandTypeService.getBrandTypeBy(qo).getResult();
        form.addResult("bTypeList", bTypeList);
    }
}
