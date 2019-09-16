package com.eastinno.otransos.seafood.content.action;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 链接图片
 * @author nsz
 */
@Action
public class ShopLinkImgAction extends AbstractPageCmdAction {
	@Inject
	private ILinkImgGroupService linkImgGroupService;
	@Inject
    private ILinkImgTypeService linkImgTypeService;
	@Inject
    private ICusUploadFileService cusUploadFileService;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
    	String code =CommUtil.null2String(form.get("code"));
    	return DiscoShopUtil.goPage("/shopLinkImg.java?cmd=list&code="+code);
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        String code = CommUtil.null2String(form.get("code"));
        qo.addQuery("obj.type.code",code,"=");
        IPageList pl = this.linkImgGroupService.getLinkImgGroupBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/content/shopLinkImg/shopLinkImgList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/content/shopLinkImg/shopLinkImgEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	LinkImgGroup imgGroup = form.toPo(LinkImgGroup.class);
    	if("1".equals(imgGroup.getClassy())){
    		String urlstr = "http://shop.houseofseafood.cn/shopProduct.java?cmd=toProDet&pId="+imgGroup.getAllid();
    		imgGroup.setLinkUrl(urlstr);
    	}else if("2".equals(imgGroup.getClassy())){
    		String urlstr = "http://shop.houseofseafood.cn/wxShopBase.java?cmd=toPage&tId="+imgGroup.getAllid()+"&orderBy=saleNum&isDesc=true&topage=/bcd/wxshop/product/productlist.html";
    		imgGroup.setLinkUrl(urlstr);
    	}else if("3".equals(imgGroup.getClassy())){
    		
    		String urlstr = "http://shop.houseofseafood.cn/wxShopPromotions.java?cmd=toTimeLimitDetail&pId=1&regularId="+imgGroup.getAllid();
    		imgGroup.setLinkUrl(urlstr);
    	}else if("4".equals(imgGroup.getClassy())){
    		String urlstr = "http://shop.houseofseafood.cn/wxShopPromotions.java?cmd=toSecKillDetail&pId=1&regularId="+imgGroup.getAllid();
    		imgGroup.setLinkUrl(urlstr);
    	}
    	imgGroup.setType(getImgType(form));
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        	imgGroup.setImgPath(imgPath);
        }
        if (!hasErrors()) {
            Long id = this.linkImgGroupService.addLinkImgGroup(imgGroup);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        String code =imgGroup.getType().getCode();
        return DiscoShopUtil.goPage("/shopLinkImg.java?cmd=list&code="+code);
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
            LinkImgGroup entry = this.linkImgGroupService.getLinkImgGroup(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/content/shopLinkImg/shopLinkImgEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        LinkImgGroup entry = this.linkImgGroupService.getLinkImgGroup(id);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        	entry.setImgPath(imgPath);
        }
        if (!hasErrors()) {
            boolean ret = this.linkImgGroupService.updateLinkImgGroup(entry.getId(), entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        String code =entry.getType().getCode();
        return DiscoShopUtil.goPage("/shopLinkImg.java?cmd=list&code="+code);
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        LinkImgGroup imgGroup = this.linkImgGroupService.getLinkImgGroup(id);
        LinkImgType imgType = imgGroup.getType();
        this.linkImgGroupService.delLinkImgGroup(id);
        String code =CommUtil.null2String(form.get("code"));
        if(imgType.getBannerPPTs()!=null && imgType.getBannerPPTs().size()==0){
        	this.linkImgTypeService.delPPtType(imgType.getId());
        }
        return DiscoShopUtil.goPage("/shopLinkImg.java?cmd=list&code="+code);
    }
    /**
     * 获取链接图片分类
     * @param form
     * @return
     */
    public LinkImgType getImgType(WebForm form){
    	LinkImgType imgType=null;
    	String code = CommUtil.null2String(form.get("code"));
    	if(!"".equals(code)){
    		imgType = this.linkImgTypeService.getLinkImgTypeByCode(code);
    		if(imgType==null){
    			imgType = new LinkImgType();
    			imgType.setCode(code);
    			imgType.setTitle(System.currentTimeMillis()+"");
    			imgType.setTenant(TenantContext.getTenant());
    			this.linkImgTypeService.addPPtType(imgType);
    		}
    	}
    	return imgType;
    }
	public void setLinkImgGroupService(ILinkImgGroupService linkImgGroupService) {
		this.linkImgGroupService = linkImgGroupService;
	}
	public void setLinkImgTypeService(ILinkImgTypeService linkImgTypeService) {
		this.linkImgTypeService = linkImgTypeService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
}