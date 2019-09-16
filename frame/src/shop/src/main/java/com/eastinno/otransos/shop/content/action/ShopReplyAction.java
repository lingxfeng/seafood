package com.eastinno.otransos.shop.content.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.content.domain.ShopReply;
import com.eastinno.otransos.shop.content.service.IShopReplyService;

/**
 * ShopReplyAction
 * @author nsz
 */
@Action
public class ShopReplyAction extends AbstractPageCmdAction {
    @Inject
    private IShopReplyService service;
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
        IPageList pl = this.service.getShopReplyBy(qo);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/shopReply/shopReplyList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/product/shopReply/shopReplyEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopReply entry = (ShopReply)form.toPo(ShopReply.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopReply(entry);
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
            ShopReply entry = this.service.getShopReply(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/product/shopReply/shopReplyEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopReply entry = this.service.getShopReply(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopReply(id, entry);
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
        this.service.delShopReply(id);
        return go("list");
    }
    
    public void setService(IShopReplyService service) {
        this.service = service;
    }
}