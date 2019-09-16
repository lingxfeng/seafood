package com.eastinno.otransos.seafood.usercenter.action;

import java.net.URLEncoder;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.seafood.usercenter.service.IShopSinceSomeService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopSinceSomeAction
 * @author 
 */
@Action
public class ShopSinceSomeAction extends AbstractPageCmdAction {
    @Inject
    private IShopSinceSomeService service;
    
    @Inject
    private ISystemRegionService systemRegionService;
    
    @Inject
    private IShopDistributorService shopDistributorService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getShopSinceSomeBy(qo);
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
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
    	if(member==null){
    		return this.goPage(form, "登录超时请重新登录");
    	}
        ShopSinceSome entry = (ShopSinceSome)form.toPo(ShopSinceSome.class);
        entry.setUser(member);
        Long id = this.service.addShopSinceSome(entry);
        if (id != null) {
            form.addResult("msg", "添加成功");
        }
//        if (!hasErrors()) {
//        	
//        }
        String url =CommUtil.null2String((String)form.get("url"));
        form.addResult("url",url);
        return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList&state=2&url="+URLEncoder.encode(url));
    }
    
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	String url =CommUtil.null2String((String)form.get("url"));
        form.addResult("url",url);
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopSinceSome entry = this.service.getShopSinceSome(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopSinceSome(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList&state=2&url="+URLEncoder.encode(url));
    }
    /**
     * 数据删除
     * 
     * @param form
     */
    public Page doToDelete(WebForm form) {
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
		if(member == null){
			return this.goPage(form, "登录超时请重新登录");
		}
    	String url =CommUtil.null2String((String)form.get("url"));
        form.addResult("url",url);
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopSinceSome entry = this.service.getShopSinceSome(id);
        entry.setStatus(Short.parseShort("2"));
        this.service.updateShopSinceSome(id, entry);
        
        return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toAddressList&state=2&url="+URLEncoder.encode(url));
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doToUpdate(WebForm form) {
    	String url =CommUtil.null2String((String)form.get("url"));
        form.addResult("url",url);
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopSinceSome entry = this.service.getShopSinceSome(id);
        form.addResult("sss", entry);
        this.getRegion(form);
    	this.getShopSinceSomeByArea(form);
        return new Page("/bcd/wxshop/member/address_zt.html");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delShopSinceSome(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 设为默认
     * 
     * @param form
     */
    public Page doSetDefault(WebForm form) {
    	Long id = (long)CommUtil.null2Int(form.get("id"));
    	ShopMember member = (ShopMember) this.getShopMemberBySession();
    	if(member==null){
    		this.addError("msg", "登录超时");
    		return pageForExtForm(form);
    	}
    	this.service.setDefault(id, member);
        return pageForExtForm(form);
    }
    
    /**
     * 自提页面
     * @param form
     * @return
     */
    public Page doGetShopSinceSome(WebForm form){
    	this.getRegion(form);
    	return new Page("/bcd/wxshop/member/address_zt.html");
    }
    
    //获取地区
    public void getRegion(WebForm form){
    	form.addResult("provinceList", this.systemRegionService.getRootSystemRegions().getResult());
    }
    
    
    /**
     * 根据县获取商户
     * @param form
     * @param area_id
     */
    public Page getShopSinceSomeByArea(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	SystemRegion region = this.systemRegionService.getSystemRegion(Long.parseLong(id));
    	String path ="";
    	if(region != null){
    		path = region.getPath();
    	}
    	QueryObject qo = new QueryObject();
    	qo = new QueryObject();
    	if(StringUtils.hasText(path)){
    		qo.addQuery("obj.area.path like '" +path+"%'");
    		qo.setLimit(-1);
    	}else{
    		qo.setLimit(10);
    	}
    	qo.addQuery("obj.exStatus",1,"=");
    	List<ShopDistributor> sdList = this.shopDistributorService.getShopDistributorBy(qo).getResult();
    	form.addResult("sdList", sdList);
    	return new Page("/bcd/wxshop/member/sincesomelist.html");
    }
    
    public Object getShopMemberBySession(){
    	ShopMember mebmer = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
    	return mebmer;
    }
    
    public Page goPage(WebForm form,String msg){
    	form.addResult("msg", msg);
    	return new Page("/bcd/wxshop/error.html");
    }
    
    public void setService(IShopSinceSomeService service) {
        this.service = service;
    }

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	
    
}