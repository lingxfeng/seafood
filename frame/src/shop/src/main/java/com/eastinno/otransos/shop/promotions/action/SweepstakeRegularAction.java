package com.eastinno.otransos.shop.promotions.action;

import java.util.List;

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
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.droduct.domain.AttributeKey;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeRegularService;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeSystemConfigService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;

/**
 * SweepstakeRegularAction
 * @author 
 */
@Action
public class SweepstakeRegularAction extends WxShopBaseAction {
    @Inject
    private ISweepstakeRegularService service;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private ISweepstakeSystemConfigService sweepstakeSystemConfigService;
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setPageSize(12);
        IPageList pageList = this.service.getSweepstakeRegularBy(qo);
        form.addResult("pl",pageList);
        int minbase=0;
    	QueryObject qo2 = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo2).getResult();
    	if(list != null && list.size() != 0){
    		SweepstakeSystemConfig sc = list.get(0);
    		minbase = sc.getMinbase();
    	}
    	form.addResult("minbase", minbase);
        return new Page("/bcd/promotions/sweepstakes/sweepstakeList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
        int minbase=0;
    	SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);
    		minbase = sc.getMinbase();
    	}
    	form.addResult("minbase", minbase);
    	return new Page("/bcd/promotions/sweepstakes/sweepstakeEdit.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	
         QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
         IPageList pageList = this.service.getSweepstakeRegularBy(qo);
         if(pageList.getRowCount()<=11){
        	 SweepstakeRegular entry = (SweepstakeRegular)form.toPo(SweepstakeRegular.class);
             form.toPo(entry);
             String imgPath = FileUtil.uploadFile(form, "imgPaths", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                     + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
             if(!"".equals(imgPath)){
             	entry.setImgPaths(imgPath);
             	this.cusUploadFileService.addCusUploadFile(imgPath);
             }
        	 if (!hasErrors()) {    
            	 Long id = this.service.addSweepstakeRegular(entry);
             }
        	 return go("list");
         }else{
        	 return  error(form,"最大支持12条数据");
         }
        
         
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
            SweepstakeRegular entry = this.service.getSweepstakeRegular(id);
            form.addResult("entry", entry);
        }
        int minbase=0;
    	SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);
    		minbase = sc.getMinbase();
    	}
    	form.addResult("minbase", minbase);
        //查询优惠券
//        QueryObject qo = new QueryObject();
//        qo.
        return new Page("/bcd/promotions/sweepstakes/sweepstakeEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SweepstakeRegular entry = this.service.getSweepstakeRegular(id);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPaths", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png;gif");
        if(!"".equals(imgPath)){
        	entry.setImgPaths(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            boolean ret = service.updateSweepstakeRegular(id, entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    
    public void setService(ISweepstakeRegularService service) {
        this.service = service;
    }
	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	public ISweepstakeSystemConfigService getSweepstakeSystemConfigService() {
		return sweepstakeSystemConfigService;
	}
	public void setSweepstakeSystemConfigService(
			ISweepstakeSystemConfigService sweepstakeSystemConfigService) {
		this.sweepstakeSystemConfigService = sweepstakeSystemConfigService;
	}
    
}