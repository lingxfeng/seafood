package com.eastinno.otransos.seafood.trade.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;
@Action
public class ShopPayConfigAction extends AbstractPageCmdAction{
	@Inject
    private IPaymentConfigService paymentConfigService;
	@Inject
    private ICusUploadFileService cusUploadFileService;

	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}

	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}

	@Override
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
        IPageList pl = this.paymentConfigService.getPaymentConfigBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/d_shop/shopmanage/trade/paymentconfig/payconfiglist.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/d_shop/shopmanage/trade/paymentconfig/payconfigedit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        PaymentConfig entry = (PaymentConfig)form.toPo(PaymentConfig.class);
        form.toPo(entry);
        String logo = FileUtil.uploadFile(form, "logo", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(logo)){
        	this.cusUploadFileService.addCusUploadFile(logo);
        	entry.setLogo(logo);
        }
        if (!hasErrors()) {
            Long id = this.paymentConfigService.addPaymentConfig(entry);
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
            PaymentConfig entry = this.paymentConfigService.getPaymentConfig(id);
            form.addResult("entry", entry);
        }
        return new Page("/d_shop/shopmanage/trade/paymentconfig/payconfigedit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        PaymentConfig entry = this.paymentConfigService.getPaymentConfig(id);
        form.toPo(entry);
        String logo = FileUtil.uploadFile(form, "logo", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(logo)){
        	this.cusUploadFileService.addCusUploadFile(logo);
        	entry.setLogo(logo);
        }
        if (!hasErrors()) {
            boolean ret = this.paymentConfigService.updatePaymentConfig(id, entry);
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
        this.paymentConfigService.delPaymentConfig(id);
        return go("list");
    }

	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}

	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
}
