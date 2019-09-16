package com.eastinno.otransos.seafood.core.action;

import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.PageType;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopSystemConfigAction
 * @author nsz
 */
@Action
public class ShopSystemConfigAction extends AbstractPageCmdAction {
    @Inject
    private IShopSystemConfigService service;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject 
    private ISystemRegionService systemRegionService;

	/**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("toEdit");
    }

    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
    	Tenant t = ShiroUtils.getTenant();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tenant",t,"=");
    	qo.setPageSize(1);
    	List<ShopSystemConfig> list = this.service.getShopSystemConfigBy(qo).getResult();
    	ShopSystemConfig config=null;
    	if(list==null || list.size()==0){
    		config  = new ShopSystemConfig();
    		config.setTenant(t);
    		this.service.addShopSystemConfig(config);
    	}else{
    		config = list.get(0);
    	}
    	form.addResult("entry", config);
    	form.addResult("provinceList", this.systemRegionService.getRootSystemRegions().getResult());
    	return new Page("/bcd/settings/shopsystemconfig/ShopSystemConfigEdit.html");
    }
    
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopSystemConfig entry = this.service.getShopSystemConfig(id);
        form.toPo(entry);

        String logoUrl = FileUtil.uploadFile(form, "logoUrl", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(logoUrl)){
        	this.cusUploadFileService.addCusUploadFile(logoUrl);
        	entry.setLogoUrl(logoUrl);
        }
        
        String bUrl = FileUtil.uploadFile(form, "bUrl", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(bUrl)){
        	this.cusUploadFileService.addCusUploadFile(bUrl);
        	entry.setbUrl(bUrl);
        }
        
        String shareUrl = FileUtil.uploadFile(form, "shareUrl", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(shareUrl)){
        	this.cusUploadFileService.addCusUploadFile(shareUrl);
        	entry.setShareUrl(shareUrl);
        }
        
        String backgroundUrl = FileUtil.uploadFile(form, "backgroundUrl", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(backgroundUrl)){
        	this.cusUploadFileService.addCusUploadFile(backgroundUrl);
        	entry.setBackgroundUrl(backgroundUrl);
        }
        
        String introduceUrl = FileUtil.uploadFile(form, "introduceUrl", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(introduceUrl)){
        	this.cusUploadFileService.addCusUploadFile(introduceUrl);
        	entry.setIntroduceUrl(introduceUrl);
        }
        
        if (!hasErrors()) {
            boolean ret = service.updateShopSystemConfig(id, entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("toEdit");
    }
    
    /**
     * 获取地区的详细信息，返回格式JSON
     * @param form
     * @return
     */
    public Page doGetDetailRegionByArea(WebForm form){
    	String parentId = CommUtil.null2String(form.get("parentId"));
    	List childrenRegion = null;
    	if("root".equals(parentId)){
    		childrenRegion = this.systemRegionService.getRootSystemRegions().getResult();    			
    	}else if(!"".equals(parentId)){
    		childrenRegion = this.systemRegionService.getSystemRegion(Long.valueOf(parentId)).getChildren();
    	}
    	
    	form.addResult("pl", childrenRegion);
    	return new Page("/bcd/settings/shopsystemconfig/AjaxTemplate.html");
    }
        
    public void setService(IShopSystemConfigService service) {
        this.service = service;
    }
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}

    public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}
}