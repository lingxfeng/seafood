package com.eastinno.otransos.shop.content.action;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 栏目分类
 * @author nsz
 */
@Action
public class ShopNewsDirAction extends AbstractPageCmdAction {
	@Inject
    private INewsDirService newsDirService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private INewsDocService newsDocService;
	/**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
    	String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return DiscoShopUtil.goPage("/shopNewsDir.java?cmd=list&type="+type);
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        qo.addQuery("obj.tCode",type,"=");
        qo.setOrderBy("sequence");
        String isJson = CommUtil.null2String(form.get("json"));
        if(!"".equals(isJson)){
        	qo.setOrderType("desc");
        	List<?> list = this.newsDirService.getNewsDirBy(qo).getResult();
        	form.jsonResult((list==null?new ArrayList<Object>():list));
        	return Page.JSPage;
        }
        IPageList pl = this.newsDirService.getNewsDirBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/content/shopNewsDir/shopNewsDirList"+type+".html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
    	return new Page("/shopmanage/content/shopNewsDir/shopNewsDirEdit"+type+".html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        NewsDir entry = form.toPo(NewsDir.class);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setBannerImg(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        entry.settCode(type);
        if (!hasErrors()) {
            Long id = this.newsDirService.addNewsDir(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return DiscoShopUtil.goPage("/shopNewsDir.java?cmd=list&type="+type);
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
            NewsDir entry = this.newsDirService.getNewsDir(id);
            form.addResult("entry", entry);
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return new Page("/shopmanage/content/shopNewsDir/shopNewsDirEdit"+type+".html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        NewsDir entry = this.newsDirService.getNewsDir(id);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setBannerImg(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        entry.settCode(type);
        if (!hasErrors()) {
            boolean ret = this.newsDirService.updateNewsDir(entry.getId(), entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return DiscoShopUtil.goPage("/shopNewsDir.java?cmd=list&type="+type);
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.dir.id",id,"=");
        List<NewsDoc> list = this.newsDocService.getNewsDocBy(qo).getResult();
        if(list!=null && list.size()>0){
        	for(NewsDoc nd:list){
        		this.newsDocService.delNewsDoc(nd.getId());
        	}
        }
        this.newsDirService.delNewsDir(id);
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return DiscoShopUtil.goPage("/shopNewsDir.java?cmd=list&type="+type);
    }
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	public void setNewsDirService(INewsDirService newsDirService) {
		this.newsDirService = newsDirService;
	}
	public void setNewsDocService(INewsDocService newsDocService) {
		this.newsDocService = newsDocService;
	}
    
}