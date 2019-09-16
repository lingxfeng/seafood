package com.eastinno.otransos.shop.spokesman.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributorRating;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;

/**
 * SpokesmanRatingAction
 * @author 
 */
@Action
public class SpokesmanRatingAction extends AbstractPageCmdAction {
    @Inject
    private ISpokesmanRatingService service;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	QueryObject qo = form.toPo(QueryObject.class);
        IPageList pl = this.service.getSpokesmanRatingBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/bcd/spokesman/level/spokesmanLevelList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	SpokesmanRating entry = (SpokesmanRating)form.toPo(SpokesmanRating.class);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            Long id = this.service.addSpokesmanRating(entry);
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
            SpokesmanRating entry = this.service.getSpokesmanRating(id);
            form.addResult("entry", entry);
        }
        return new Page("/bcd/spokesman/level/spokesmanLevelEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
    	SpokesmanRating entry = this.service.getSpokesmanRating(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSpokesmanRating(id,entry);
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
        this.service.delSpokesmanRating(id);
        return go("list");
    }
    
    public void setService(ISpokesmanRatingService service) {
        this.service = service;
    }

	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}

	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
    
}