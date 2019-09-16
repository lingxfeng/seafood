package com.eastinno.otransos.shop.usercenter.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMemberRating;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberRatingService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopMemberRatingAction
 * 会员-》等级管理的action类
 * @author nsz
 */
@Action
public class ShopMemberRatingAction extends AbstractPageCmdAction {
    @Inject
    private IShopMemberRatingService service;
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
        qo.setOrderBy("sequence");
        qo.setOrderType("asc");
        IPageList pl = this.service.getShopMemberRatingBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/bcd/member/shopmemberrating/shopMemberRatingList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/bcd/member/shopmemberrating/shopMemberRatingEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopMemberRating entry = (ShopMemberRating)form.toPo(ShopMemberRating.class);
        if(entry.getName().equals("")){
        	form.addResult("msg", "等级名称不允许为空！");
        }
        if(entry.getIntegral() < 0){
        	form.addResult("msg", "所需积分不允许为负值！");
        }
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            Long id = this.service.addShopMemberRating(entry);
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
            ShopMemberRating entry = this.service.getShopMemberRating(id);
            form.addResult("entry", entry);
        }
        return new Page("/bcd/member/shopmemberrating/shopMemberRatingEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopMemberRating entry = this.service.getShopMemberRating(id);        
        if(entry.getName().equals("")){
        	form.addResult("msg", "等级名称不允许为空！");
        	return go("toEdit");
        }
        if(entry.getIntegral() < 0){
        	form.addResult("msg", "所需积分不允许为负值！");
        	return go("toEdit");
        }        
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            boolean ret = service.updateShopMemberRating(id, entry);
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
        this.service.delShopMemberRating(id);
        return go("list");
    }
    
    public void setService(IShopMemberRatingService service) {
        this.service = service;
    }
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
    
}