package com.eastinno.otransos.seafood.core.action;

import java.util.HashMap;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.seafood.core.domain.CusUploadFile;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 文件上传控制
 * @author nsz
 */
@Action
public class CusUploadFileAction extends AbstractPageCmdAction {
    @Inject
    private ICusUploadFileService service;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
    	String imgPath = FileUtil.uploadFile(form, "imgFile", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
    	Long id = this.service.addCusUploadFile(imgPath);
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("url", imgPath);
		map.put("id", id);
		form.jsonResult(map);
    	return Page.JSPage;
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pl = this.service.getCusUploadFileBy(qo);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/cusUploadFile/cusUploadFileList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/product/cusUploadFile/cusUploadFileEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        CusUploadFile entry = (CusUploadFile)form.toPo(CusUploadFile.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addCusUploadFile(entry);
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
            CusUploadFile entry = this.service.getCusUploadFile(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/product/cusUploadFile/cusUploadFileEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        CusUploadFile entry = this.service.getCusUploadFile(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateCusUploadFile(id, entry);
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
        this.service.delCusUploadFile(id);
        return go("list");
    }
    
    public void setService(ICusUploadFileService service) {
        this.service = service;
    }
}