package com.eastinno.otransos.seafood.content.action;

import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.seafood.content.domain.ShopFloor;
import com.eastinno.otransos.seafood.content.service.IShopFloorService;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 楼层管理
 * @author nsz
 */
@Action
public class ShopFloorAction extends AbstractPageCmdAction {
    @Inject
    private IShopFloorService service;
    @Inject
    private IProductTypeService productTypeService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
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
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pl = this.service.getShopFloorBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/content/shopFloor/shopFloorList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.productTypeService.getProductTypeBy(qo).getResult();
    	form.addResult("pTypes", list);
    	
    	return new Page("/shopmanage/content/shopFloor/shopFloorEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopFloor entry = (ShopFloor)form.toPo(ShopFloor.class);
        form.toPo(entry);
        String logoImg = FileUtil.uploadFile(form, "logoImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(logoImg)){
        	this.cusUploadFileService.addCusUploadFile(logoImg);
        	entry.setLogoImg(logoImg);
        }
        String advImg = FileUtil.uploadFile(form, "advImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(advImg)){
        	this.cusUploadFileService.addCusUploadFile(advImg);
        	entry.setAdvImg(advImg);
        }
        if (!hasErrors()) {
            Long id = this.service.addShopFloor(entry);
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
            ShopFloor entry = this.service.getShopFloor(id);
            form.addResult("entry", entry);
        }
        QueryObject qo = new QueryObject();
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.productTypeService.getProductTypeBy(qo).getResult();
    	form.addResult("pTypes", list);
        return new Page("/shopmanage/content/shopFloor/shopFloorEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopFloor entry = this.service.getShopFloor(id);
        form.toPo(entry);
        String logoImg = FileUtil.uploadFile(form, "logoImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(logoImg)){
        	this.cusUploadFileService.addCusUploadFile(logoImg);
        	entry.setLogoImg(logoImg);
        }
        String advImg = FileUtil.uploadFile(form, "advImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(advImg)){
        	this.cusUploadFileService.addCusUploadFile(advImg);
        	entry.setAdvImg(advImg);
        }
        if (!hasErrors()) {
            boolean ret = service.updateShopFloor(id, entry);
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
        this.service.delShopFloor(id);
        return go("list");
    }
    
    public void setService(IShopFloorService service) {
        this.service = service;
    }
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	
    
}