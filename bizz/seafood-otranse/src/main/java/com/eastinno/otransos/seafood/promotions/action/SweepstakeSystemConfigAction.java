package com.eastinno.otransos.seafood.promotions.action;

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
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakeSystemConfigService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;

/**
 * SweepstakeSystemConfigAction
 * @author 
 */
@Action
public class SweepstakeSystemConfigAction extends AbstractPageCmdAction {
    @Inject
    private ISweepstakeSystemConfigService service;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    /**
     * 查看、修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.service.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		SweepstakeSystemConfig sc = list.get(0);
    		form.addResult("entry", sc);
    		form.toPo(sc);
    		String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                    + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png;gif");
           if(!"".equals(imgPath)){
            	this.cusUploadFileService.addCusUploadFile(imgPath);
            	sc.setImgPaths(imgPath);
           }else{
            	this.addError("msg", "品牌图标不能为空！！！");
           }
    		if (!hasErrors()) {
                boolean ret = service.updateSweepstakeSystemConfig(sc.getId(),sc);
                if(ret){
                    form.addResult("msg", "修改成功");
                }
    	}
        }else{
        	SweepstakeSystemConfig sc = new SweepstakeSystemConfig();
        	form.toPo(sc);
        	String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                     + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
            if(!"".equals(imgPath)){
             	this.cusUploadFileService.addCusUploadFile(imgPath);
             	sc.setImgPaths(imgPath);
            }else{
             	this.addError("msg", "品牌图标不能为空！！！");
            }
        	if (!hasErrors()) {
              this.service.addSweepstakeSystemConfig(sc);
              form.addResult("entry", sc);
        	}
        }
        return new Page("/bcd/promotions/sweepstakes/sweepstakeRule.html");
    }
    
    public void setService(ISweepstakeSystemConfigService service) {
        this.service = service;
    }

	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}

	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}

	public ISweepstakeSystemConfigService getService() {
		return service;
	}
    
}