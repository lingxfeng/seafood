package com.eastinno.otransos.seafood.content.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.domain.ShopReply;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.content.service.IShopReplyService;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopDiscussAction
 * @author nsz
 */
@Action
public class ShopDiscussAction extends AbstractPageCmdAction {
    @Inject
    private IShopDiscussService service;
    
    @Inject
    private IShopReplyService shopReplyService;
    
    @Inject
    private IShopProductService shopProductService;
    
    @Inject
    private IShopMemberService shopMemberService;
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
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList pl = this.service.getShopDiscussBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/shopDiscuss/shopDiscussList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/product/shopDiscuss/shopDiscussEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopDiscuss entry = (ShopDiscuss)form.toPo(ShopDiscuss.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopDiscuss(entry);
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
            ShopDiscuss entry = this.service.getShopDiscuss(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/product/shopDiscuss/shopDiscussEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopDiscuss entry = this.service.getShopDiscuss(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopDiscuss(id, entry);
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
        this.service.delShopDiscuss(id);
        return go("list");
    }
    
    /**
     * 查询所有咨询或者评论
     * @param form
     * @return
     */
    public Page doZxOrPlList(WebForm form){
    	int type=CommUtil.null2Int(form.get("type"));
    	QueryObject qoObject = new QueryObject();
    	qoObject.addQuery("obj.type", type, "=");
    	qoObject.setOrderBy("createDate");
    	qoObject.setOrderType("desc");
    	IPageList pageList=this.service.getShopDiscussBy(qoObject);
    	CommUtil.saveIPageList2WebForm(pageList, form);
    	form.addResult("zxOrPlList", pageList.getResult());
    	return new Page("/shopmanage/content/ShopDisw/self_evaluate_list.html");
    }
    
    /**
     * 跳转到评论界面
     * @param form
     * @return
     */
    public Page doToEvaluatePage(WebForm form){
    	return new Page("/shopmanage/content/ShopDisw/evaluate_reply.html");
    }
    
    /**
     * 保存回复
     * @param form
     * @return
     */
    public Page doSaveEvaluate(WebForm form){
    	ShopMember user = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MANAGEMEMBER");
    	if(user==null){
    		this.addError("msg", "此用户没有登录");
    		return pageForExtForm(form);
    	}
    	String id=CommUtil.null2String(form.get("id"));
    	String replay=CommUtil.null2String(form.get("replay"));
    	ShopDiscuss shopDiscuss=this.service.getShopDiscuss(Long.valueOf(id));
    	ShopReply shopReply=form.toPo(ShopReply.class);
    	shopReply.setType(1);
    	shopReply.setUser(user);
    	shopReply.setDiscuss(shopDiscuss);
    	this.shopReplyService.addShopReply(shopReply);
    	return pageForExtForm(form);
    }
    
    /**
     * 跳转评论管理页面
     * @param form
     * @return
     */
    public Page doGetDiscuss(WebForm form){
    	ShopDiscuss shopDiscuss=this.service.getShopDiscuss(Long.valueOf(CommUtil.null2String(form.get("id"))));
    	form.addResult("shopDiscuss", shopDiscuss);
    	return new Page("/shopmanage/content/ShopDisw/discussManage.html");
    }
    
    /**
     * 跳转评论管理页面
     * @param form
     * @return
     */
    public Page doSearchDiscuss(WebForm form){
    	String name=CommUtil.null2String(form.get("name"));
    	String name_="";
    	Tenant tenant=ShiroUtils.getTenant();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tenant", tenant, "=");
    	if (StringUtils.hasText(name)) {
    		try {
				name_ =new String( name.getBytes("ISO-8859-1") , "UTF-8");
				qo.addQuery("obj.pro.name like '%"+name_+"%'");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.service.getShopDiscussBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("zxOrPlList", iPageList.getResult());
    	return new Page("/shopmanage/content/ShopDisw/self_evaluate_list.html");
    }
    /**
     * 修改评论状态
     * @param form
     * @return
     */
    public Page doChangeDiscussStatus(WebForm form){
    	String id=CommUtil.null2String(form.get("id"));
    	String isShow=CommUtil.null2String(form.get("isShow"));
    	ShopDiscuss shopDiscuss=this.service.getShopDiscuss(Long.valueOf(id));
    	if("1".equals(isShow)){
    		shopDiscuss.setIsShow(true);
    	}else if("0".equals(isShow)){
    		shopDiscuss.setIsShow(false);
    	}
    	boolean b=this.service.updateShopDiscuss(Long.valueOf(id), shopDiscuss);
    	if(!b){
    		this.addError("msg", "修改失败");
    	}
    	return pageForExtForm(form);
    }
    /**
     * 修改评论状态
     * @param form
     * @return
     */
    public Page doChangeDiscussShow(WebForm form){
    	String id=CommUtil.null2String(form.get("id"));
    	String isShow=CommUtil.null2String(form.get("isShow"));
    	ShopDiscuss shopDiscuss=this.service.getShopDiscuss(Long.valueOf(id));
    	if("1".equals(isShow)){
    		shopDiscuss.setIsShow(true);
    	}else if("0".equals(isShow)){
    		shopDiscuss.setIsShow(false);
    	}
    	boolean b=this.service.updateShopDiscuss(Long.valueOf(id), shopDiscuss);
    	if(!b){
    		this.addError("msg", "修改失败");
    	}
    	return go("list");
    }
    
    /**
     * 加载更多评论
     * @param form
     * @return
     */
    public Page doAddMoreDiscuss(WebForm form){
    	Integer curPage = CommUtil.null2Int(form.get("curPage"));
    	Integer pId=CommUtil.null2Int(form.get("pId"));
    	Tenant tenant = TenantContext.getTenant();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tenant", tenant, "=");
    	qo.addQuery("obj.pro.id", (long)pId, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	qo.setCurrentPage(curPage);
    	qo.setPageSize(5);
    	IPageList pl=this.service.getShopDiscussBy(qo);
    	if(pl.getPages()<curPage){
    		return new Page("/bcd/wxshop/product/discusstmore.html");
    	}
    	CommUtil.saveIPageList2WebForm(pl, form);
    	form.addResult("proDiscussList", pl.getResult());
    	return new Page("/bcd/wxshop/product/discusstmore.html");
    }
    
    /**
     * 测试
     * 
     * @param form
     */
    public Page doSaveDiscuss(WebForm form) {
    	Long proId=Long.valueOf(CommUtil.null2Int(form.get("proId")));
    	Long userId=Long.valueOf(CommUtil.null2Int(form.get("userId")));
        ShopDiscuss entry = (ShopDiscuss)form.toPo(ShopDiscuss.class);
        form.toPo(entry);
        ShopProduct shopProduct=this.shopProductService.getShopProduct(proId);
        ShopMember member = this.shopMemberService.getShopMember(userId);
        if (!hasErrors()) {
        	entry.setUser(member);
        	entry.setPro(shopProduct);
            Long id = this.service.addShopDiscuss(entry);
        }
        return pageForExtForm(form);
    }
    
    /**
     * 跳转登录界面
     * 
     * @param form
     */
    public Page doToLogin(WebForm form) {
        return new Page("/shopmanage/login.html");
    }
    
    public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public void setShopReplyService(IShopReplyService shopReplyService) {
		this.shopReplyService = shopReplyService;
	}
	public void setService(IShopDiscussService service) {
        this.service = service;
    }
}