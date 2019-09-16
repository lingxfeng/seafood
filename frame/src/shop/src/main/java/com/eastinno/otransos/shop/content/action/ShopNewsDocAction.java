package com.eastinno.otransos.shop.content.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 文章管理
 * @author nsz
 */
@Action
public class ShopNewsDocAction extends AbstractPageCmdAction {
	@Inject
    private INewsDirService newsDirService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private INewsDocService newsDocService;
    
    @Override
	public Object doBefore(WebForm form, Module module) {
    	String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tCode",type,"=");
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.newsDirService.getNewsDirBy(qo).getResult();
    	form.addResult("dirs", list);
		return super.doBefore(form, module);
	}
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
        return DiscoShopUtil.goPage("/shopNewsDoc.java?cmd=list&type="+type);
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        IPageList pl = this.newsDocService.getNewsDocBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return new Page("/shopmanage/content/shopNewsDoc/shopNewsDocList"+type+".html");
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
    	return new Page("/shopmanage/content/shopNewsDoc/shopNewsDocEdit"+type+".html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        NewsDoc entry = form.toPo(NewsDoc.class);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            Long id = this.newsDocService.addNewsDoc(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return DiscoShopUtil.goPage("/shopNewsDoc.java?cmd=list&type="+type);
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
            NewsDoc entry = this.newsDocService.getNewsDoc(id);
            form.addResult("entry", entry);
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return new Page("/shopmanage/content/shopNewsDoc/shopNewsDocEdit"+type+".html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        NewsDoc entry = this.newsDocService.getNewsDoc(id);
        form.toPo(entry);
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	entry.setImgPath(imgPath);
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        }
        if (!hasErrors()) {
            boolean ret = this.newsDocService.updateNewsDoc(entry.getId(), entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return DiscoShopUtil.goPage("/shopNewsDoc.java?cmd=list&type="+type);
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.newsDocService.delNewsDoc(id);
        String type = CommUtil.null2String(form.get("type"));
        if("".equals(type)){
        	type="1";
        }
        return DiscoShopUtil.goPage("/shopNewsDoc.java?cmd=list&type="+type);
    }
    /**
     * 获取文章详情
     * @param form
     * @return
     */
    public Page doDocDetails(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	NewsDoc doc = this.newsDocService.getNewsDoc(Long.parseLong(id));
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("details", doc.getContent());
    	form.jsonResult(map);
    	return Page.JSPage;
    }
    
	public void setNewsDirService(INewsDirService newsDirService) {
		this.newsDirService = newsDirService;
	}
	public void setNewsDocService(INewsDocService newsDocService) {
		this.newsDocService = newsDocService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	
}