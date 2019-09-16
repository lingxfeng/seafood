package com.eastinno.otransos.shop.core.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.content.domain.ShopDiscuss;
import com.eastinno.otransos.shop.content.service.IShopDiscussService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopMemberAction
 * @author nsz
 */
@Action
public class ShopMemberCenterAction extends ShopBaseAction {
    @Inject
    private IShopMemberService service;
    @Inject
    private IShopDiscussService shopDiscussService;
    
    
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
        IPageList pl = this.service.getShopMemberBy(qo);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/shopMember/shopMemberList.html");
    }

    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopMember entry = (ShopMember)form.toPo(ShopMember.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopMember(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }

    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopMember entry = this.service.getShopMember(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopMember(id, entry);
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
        this.service.delShopMember(id);
        return go("list");
    }
    
    /**
     * 用户注册
     * 
     * @param form
     */
    public Page doRegister(WebForm form) {
    	ShopMember entry = (ShopMember)form.toPo(ShopMember.class);
        String code=CommUtil.null2String(form.get("code"));
        boolean b=this.service.MemberRegisterVerification(entry,code);
        if(!b){
        	return pageForExtForm(form);
        }
        if(entry!=null){
        	entry.setPassword(MD5.encode(entry.getPassword()));
        	UserContext.setMember(entry);
        	entry.setTenant(TenantContext.getTenant());
        	this.service.addShopMember(entry);
        }
        return pageForExtForm(form);
    }
    
    /**
     * 用户登录
     * 
     * @param form
     */
    public Page doLogin(WebForm form) {
        String name=CommUtil.null2String(form.get("name"));
        String password=CommUtil.null2String(form.get("password"));
        String code=CommUtil.null2String(form.get("code"));
        boolean b=this.service.MemberLogin(name, password,code);
        return pageForExtForm(form);
    }
    
    /**
     * 跳转登录界面
     * 
     * @param form
     */
    public Page doToLogin(WebForm form) {
        return new Page("/userCenter/login.html");
    }
    
    /**
     * 跳转注册界面
     * 
     * @param form
     */
    public Page doToRegister(WebForm form) {
        return new Page("/userCenter/register.html");
    }
    
    /**
     * 跳转界面
     * 
     * @param form
     */
    public Page doToPage(WebForm form) {
    	String toHtml=CommUtil.null2String(form.get("toHtml"));
    	if("findPwd".equals(toHtml)){
    		return new Page("/userCenter/findPwd.html");
    	}else if("findPwd2".equals(toHtml)){
    		return new Page("/userCenter/findPwd2.html");
		}else if("findPwd3".equals(toHtml)){
    		return new Page("/userCenter/findPwd3.html");
		}
        return go("list");
    }
    
    /**
     * 邮箱找回密码第一步
     * @param form
     */
    public Page doEmailGetPwd(WebForm form) {
    	String name=CommUtil.null2String(form.get("name"));
    	String email=CommUtil.null2String(form.get("email"));
    	String code=CommUtil.null2String(form.get("code"));
    	this.service.getPwd(name, email, code);
        return pageForExtForm(form);
    }
    
    /**
     * 手机找回密码第一步
     * @param form
     */
    public Page doMobileTelGetPwd(WebForm form) {
    	String name=CommUtil.null2String(form.get("name"));
    	String mobileTel=CommUtil.null2String(form.get("mobileTel"));
    	String code=CommUtil.null2String(form.get("code"));
    	this.service.getPwd(name, mobileTel, code);
        return pageForExtForm(form);
    }
    
    /**
     * 找回密码第二步修改密码
     * @param form
     */
    public Page doGetPwd2(WebForm form) {
    	String name=CommUtil.null2String(form.get("name"));
        return go("list");
    }
    
    /**
     * 得到验证码
     * @param form
     */
    public Page doGetCheckCode(WebForm form) {
//    	String name=CommUtil.null2String(form.get("name"));
//    	String yzfs=CommUtil.null2String(form.get("yzfs"));
//    	String code=CommUtil.null2String(form.get("code"));
//    	this.service.ValidateAndSendCode(name, yzfs,code);
        return pageForExtForm(form);
    }
    
    /**
     *  找回密码第二步判断验证码
     * @param form
     */
    public Page doJudgeCode(WebForm form) {
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("shopMember");
    	String code=CommUtil.null2String(form.get("code"));
    	this.service.judgeCode(shopMember, code);
        return pageForExtForm(form);
    }
    /**
	 * 跳转到登录页面2，异步
	 * @param form
	 * @return
	 */
	public Page doToLogin2(WebForm form){
		return new Page("/userCenter/login2.html");
	}
    /**
	 * 登录2,异步登录
	 * @param form
	 * @return
	 */
	public Page doLogin2(WebForm form){
		Map<String,String> map = new HashMap<String,String>();
		String randomCode = CommUtil.null2String(form.get("code"));
		String randomCodeS = ActionContext.getContext().getSession().getAttribute("rand") + "";
		if(!randomCode.equals(randomCodeS)){
			map.put("success", "0");
			map.put("msg", "验证码错误！");
		}else{
			String name=CommUtil.null2String(form.get("username"));
	        String password=CommUtil.null2String(form.get("password"));
	        boolean b=this.service.MemberLogin(name, password,randomCode);
	        if(!b){
	        	map.put("success", "0");
	        	map.put("msg", "用户名或密码错误！");
	        }else{
	        	map.put("success", "1");
	        }
		}
		form.jsonResult(map);
		return Page.JSPage;
	}
    
	
	/**
     * 修改密码
     * @param form
     */
    public Page doUpdatePwd(WebForm form) {
    	String password=CommUtil.null2String(form.get("password"));
    	String password2=CommUtil.null2String(form.get("password2"));
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("shopMember");
    	this.service.updatePwd(password, password2, shopMember);
        return pageForExtForm(form);
    }


    /**
     * 退出
     * @param form
     */
    public Page doSignOut(WebForm form) {
    	UserContext.logoutMember();
		DiscoShopUtil.setCookieByVal("SESSION_LOGIN_NAME", null);
		DiscoShopUtil.setCookieByVal("SESSION_LOGIN_VALUE", null);
        return go("goShop.init");
    }
    /**
     * 进入资讯页面
     * @param form
     * @return
     */
    public Page doTozixun(WebForm form){
    	User user = DiscoShopUtil.getUser();
    	if(user==null){
    		return go("toLogin");
    	}
    	return new Page("/shop/toaddzixun.html");
    }
    /**
     * 添加资讯
     * @param form
     * @return
     */
    public Page doSaveZixun(WebForm form){
    	ShopMember member = (ShopMember) DiscoShopUtil.getUser();
    	ShopDiscuss sd = form.toPo(ShopDiscuss.class);
    	sd.setUser(member);
    	sd.setType(2);
    	this.shopDiscussService.addShopDiscuss(sd);
    	form.addResult("goodsAdviceList", this.getGoShopService().getGoodsAdvice(sd.getPro().getId(),0));
    	this.getAdviceCount(form, sd.getPro().getId());
    	return new Page("/shop/addzixunsuccess.html");
    }
    
    /**
	 * 获取我要咨询类型的条数
	 * @param form
	 * @return
	 */
	public void getAdviceCount(WebForm form,Long id){
		int qbzxCount=this.getGoShopService().getAdviceCount(id, 0);
		int cpzxCount=this.getGoShopService().getAdviceCount(id, 1);
		int kcjpsCount=this.getGoShopService().getAdviceCount(id, 2);
		int zfjfpCount=this.getGoShopService().getAdviceCount(id, 3);
		int shzxCount=this.getGoShopService().getAdviceCount(id, 4);
		int cxhdCount=this.getGoShopService().getAdviceCount(id, 5);
		form.addResult("qbzxCount", qbzxCount);
		form.addResult("cpzxCount", cpzxCount);
		form.addResult("kcjpsCount", kcjpsCount);
		form.addResult("zfjfpCount", zfjfpCount);
		form.addResult("shzxCount", shzxCount);
		form.addResult("cxhdCount", cxhdCount);
	}
    
	public void setService(IShopMemberService service) {
        this.service = service;
    }
	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}
    
}