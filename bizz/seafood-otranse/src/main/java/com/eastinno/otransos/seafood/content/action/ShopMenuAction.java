package com.eastinno.otransos.seafood.content.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 电商菜单
 * @author nsz
 */
@Action
public class ShopMenuAction extends AbstractPageCmdAction {
    @Inject
    private ISystemMenuService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.type",Integer.parseInt("2"),"=");
        String parentId = CommUtil.null2String(form.get("parentId"));
        if(!"".equals(parentId)){
        	qo.addQuery("obj.parent.id",Long.parseLong(parentId),"=");
        }else{
        	qo.addQuery("obj.parent is EMPTY");
        }
        
        qo.setOrderBy("sequence");
        IPageList pageList = this.service.getSystemMenuBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        SystemMenu entry = (SystemMenu)form.toPo(SystemMenu.class);
        entry.setTenant(ShiroUtils.getTenant());
        entry.setType(2);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addSystemMenu(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemMenu entry = this.service.getSystemMenu(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSystemMenu(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delSystemMenu(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    public Page doGetTree(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.type",Integer.parseInt("2"),"=");
        if (!"".equals(id)) {
            SystemMenu parent = service.getSystemMenu(Long.parseLong(id));
            qo.addQuery("obj.parent", parent, "=");
        } else {
            qo.addQuery("obj.parent is EMPTY", null);
        }
        qo.setPageSize(-1);
        qo.setOrderBy("sequence");
        IPageList pageList = this.service.getSystemMenuBy(qo);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
            	SystemMenu systemMenu = (SystemMenu) pageList.getResult().get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", systemMenu.getId() + "");
                map.put("text", systemMenu.getTitle());
                map.put("leaf", systemMenu.getChildren().size() < 1);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无菜单");
            map.put("id", "0");
            map.put("leaf", true);
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

	public void setService(ISystemMenuService service) {
		this.service = service;
	}
    
}