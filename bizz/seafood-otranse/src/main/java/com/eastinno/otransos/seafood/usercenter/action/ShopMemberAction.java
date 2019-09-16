package com.eastinno.otransos.seafood.usercenter.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.payment.tencent.weixin.util.TenpayUtil;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.core.action.ShopBaseAction;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.usercenter.domain.RemainderAmtHistory;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.query.ShopMemberQuery;
import com.eastinno.otransos.seafood.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.FileUtils;
import com.eastinno.otransos.seafood.util.formatUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopMemberAction
 * 会员-》会员列表页相关的action类
 * @author nsz
 */
@Action
public class ShopMemberAction extends ShopBaseAction {
    @Inject
    private IShopMemberService service;
    
    @Inject
    private ISystemRegionService systemRegionService;
    
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    
    @Inject
    private IShopAddressService shopAddressService;
    
    @Inject
    private IShopDiscussService shopDiscussService;
    
    @Inject
    private IShopOrderdetailService shopOrderdetailService;
    
    @Inject
    private IRemainderAmtHistoryService remainderAmtHistoryService;
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
     * 登录的处理方法
     * @param form
     * @return
     */
    public Page doLogin(WebForm form){
    	String resultPage = "";
    	String name = CommUtil.null2String(form.get("name"));
    	String password = CommUtil.null2String(form.get("password"));
    	String code = CommUtil.null2String(form.get("code"));
    	if( this.service.MemberLogin(name, password, code)){
    		resultPage = "";
    	}else{
    		resultPage = "";
    	}
    	return new Page(resultPage);
    }
    
    /**
     * 跳转到登陆页面
     * @param form
     * @return
     */
    public Page doToLogin(WebForm form){
    	return new Page("");
    }
    
    /**
     * 注册的验证方法
     * @param form
     * @return
     */
    public Page doRegister(WebForm form){
    	String resultPage = "";
    	String name = CommUtil.null2String(form.get("name"));
    	String password = CommUtil.null2String(form.get("password"));
    	String code = CommUtil.null2String(form.get("code"));
    	if( this.service.ValidateAndSendCode(name, password, code) ){
    		resultPage = "";
    	}else{
    		resultPage = "";
    	}
    	return new Page(resultPage);
    }
    
    public Page doToRegister(WebForm form){
    	return new Page("");
    }
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	ShopMemberQuery qo = form.toPo(ShopMemberQuery.class);
        IPageList pl = this.service.getShopMemberListForDisplayByQO(qo);
    	//IPageList pl = this.service.getShopMemberBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/member/shopmember/ShopMemberList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/bcd/member/shopmember/ShopMemberEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopMember entry = form.toPo(ShopMember.class);
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
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopMember entry = this.service.getShopMember(id);
            form.addResult("entry", entry);
        }
        return new Page("/bcd/member/shopmember/ShopMemberEdit.html");
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
     * 跳转界面
     * 
     * @param form
     */
    public Page doToPage(WebForm form) {
    	String toHtml=CommUtil.null2String(form.get("toHtml"));
    	if("updatePwd".equals(toHtml)){
    		return new Page("/userCenter/account_password.html");
    	}else if("updateEmail".equals(toHtml)){
    		return new Page("/userCenter/account_email.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updatePic".equals(toHtml)){
    		return new Page("/userCenter/account_pic.html");
		}else if("myOrder".equals(toHtml)){
    		return new Page("/userCenter/myOrder.html");
		}else if("myViewRecords".equals(toHtml)){
			return new Page("/userCenter/myViewRecords.html");
		}else if("addAddress".equals(toHtml)){
			return new Page("/userCenter/addAddress.html");
		}else if("standard".equals(toHtml)){
			form.addResult("num", CommUtil.null2String(form.get("num")));
			return new Page("/shop/product/standard.html");
		}
        return go("list");
    }
    
    /**
     * 跳转个人中心
     * 
     * @param form
     */
    public Page doToUserCenter(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember shopMember2=this.service.getShopMember(shopMember.getId());
    	shopMember2 = this.service.getUserInfo(shopMember2);
    	form.addResult("user", shopMember2);
    	List<?> myColls = shopMember2.getMyCollections();
    	form.addResult("myColls", myColls);
    	List<?> list=this.service.getOrderInfo(form, shopMember2);
    	form.addResult("orderList", list);
        return new Page("/userCenter/userIndex.html");
    }
    
    /**
     * 修改个人资料
     * 
     * @param form
     */
    public Page doUpateUserInfo(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	form.toPo(sMember);
    	if(sMember==null){
    		this.addError("msg", "修改个人信息失败");
    		return pageForExtForm(form);
    	}
    	this.service.updateShopMember(shopMember.getId(), sMember);
    	
        return pageForExtForm(form);
    }
    
    /**
     * 跳转到个人资料界面
     * 
     * @param form
     */
    public Page doToUserInfo(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
        ShopMember member=this.service.getShopMember(shopMember.getId());
        form.addResult("user", member);
        return new Page("/userCenter/userInfo.html");
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
     * 修改密码(个人资料修改)
     * 
     * @param form
     */
    public Page doUpdatePassword(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String oldPwd=CommUtil.null2String(form.get("oldPwd"));
    	String newPwd=CommUtil.null2String(form.get("newPwd"));
    	String confirmPwd=CommUtil.null2String(form.get("confirmPwd"));
    	this.service.updatePassword(oldPwd, newPwd, confirmPwd, sMember);
        return pageForExtForm(form);
    }
    
    /**
     * 修改email(个人资料)
     * 
     * @param form
     */
    public Page doUpdateEmail(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败，请重新登录");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String password=CommUtil.null2String(form.get("password"));
    	String email=CommUtil.null2String(form.get("email"));
    	this.service.updateEmail(password, email, sMember);
        return pageForExtForm(form);
    }
    
    /**
     * 修改手机(个人资料)
     * 
     * @param form
     */
    public Page doUpdateMobileTel(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败,请重新登录");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String mobileTel=CommUtil.null2String(form.get("mobileTel"));
    	String code=CommUtil.null2String(form.get("code"));
    	this.service.updateMobileTel(mobileTel, code , shopMember);
        return pageForExtForm(form);
    }
    
    /**
     * 修改手机获取验证码(个人资料)
     * 
     * @param form
     */
    public Page doGetCode(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "没有获取用户信息，请重新登陆");
    		return pageForExtForm(form);
    	}
    	String mobileTel=CommUtil.null2String(form.get("mobileTel"));
    	this.service.sendCode(mobileTel);
        return pageForExtForm(form);
    }
    
    /**
     * 修改头像(个人资料)
     * 
     * @param form
     */
    public Page doUpdatePic(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String imgPath = FileUtil.uploadFile(form, "imgFile", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(imgPath)) {
        	sMember.setPic(imgPath);
        }
        this.service.updateShopMember(Long.valueOf(sMember.getIdCard()), sMember);
        return new Page("/userCenter/account_pic.html");
    }
    
    /**
     * 修改个人信息
     * 
     * @param form
     */
    public Page doUpdateUserInfo(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	form.toPo(sMember);
    	String imgPath = FileUtil.uploadFile(form, "pic", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(imgPath)) {
        	sMember.setPic(imgPath);
        }
        boolean b=this.service.updateShopMember(Long.valueOf(sMember.getId()), sMember);
        if(b){
        	form.addResult("state", "0");
        }
        form.addResult("user", sMember);
        UserContext.setMember(sMember);
        return new Page("/userCenter/userInfo.html");
    }
    
    /**
     * 查看订单
     * 
     * @param form
     */
    public Page doMyOrder(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	List<?> list=this.service.getOrderInfo(form, shopMember);
    	form.addResult("orderList", list);
        return new Page("/userCenter/myOrder.html");
    }
    
    /**
     * 我的收藏
     * 
     * @param form
     */
    public Page doMyCollection(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	form.addResult("user", sMember);
        return new Page("/userCenter/myCollection.html");
    }
    
    /**
     * 查看记录
     * 
     * @param form
     */
    public Page doMyViewRecords(WebForm form) {
        return new Page("/userCenter/myViewRecords.html");
    }
    
    /**
     * 收货地址
     * 
     * @param form
     */
    public Page doMyDeliveryAddress(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.user.id", shopMember.getId(), "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	List<?> list=this.shopAddressService.getShopAddressBy(qo).getResult();
    	form.addResult("addressList", list);
        return new Page("/userCenter/myAddress.html");
    }
    
    /**
     * 删除收货地址
     * 
     * @param form
     */
    public Page doDelDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	this.shopAddressService.delShopAddress(Long.valueOf(id));
        return go("shopMember.myDeliveryAddress");
    }
    
    /**
     * 跳转编译收货地址
     * 
     * @param form
     */
    public Page doToEditDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	ShopAddress shopAddress=(ShopAddress)this.shopAddressService.getShopAddress(Long.valueOf(id));
    	form.addResult("address", shopAddress);
    	QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
    	return new Page("/userCenter/updateAddress.html");
    }
    
    /**
     * 编译收货地址
     * 
     * @param form
     */
    public Page doEditDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	ShopAddress shopAddress=(ShopAddress)this.shopAddressService.getShopAddress(Long.valueOf(id));
    	form.toPo(shopAddress);
    	this.shopAddressService.updateShopAddress(Long.valueOf(id), shopAddress);
        return go("shopMember.myDeliveryAddress");
    }
    
    /**
	 * 跳转添加收货地址
	 * @param form
	 * @return
	 */
	public Page doToSaveAddress(WebForm form){
		
		QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
		
		return new Page("/userCenter/addAddress.html");
	}
    
    /**
	 * 添加收货地址
	 * @param form
	 * @return
	 */
	public Page doSaveAddress(WebForm form){
		ShopAddress obj = form.toPo(ShopAddress.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		obj.setUser(user);
		if (!hasErrors()) {
            Long id = this.shopAddressService.addShopAddress(obj);
        }
		return go("shopMember.myDeliveryAddress");
	}
	
	/**
	 * 我的咨询
	 * @param form
	 * @return
	 */
	public Page doGetMyAdvice(WebForm form){
		String reply=CommUtil.null2String(form.get("reply"));
		ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		QueryObject  qo= new QueryObject();
		qo.addQuery("obj.user.id", shopMember.getId(), "=");
		if(reply!=""){
			qo.addQuery("obj.isHf", Boolean.valueOf(reply), "=");
		}
		List<?> list=this.shopDiscussService.getShopDiscussBy(qo).getResult();
		form.addResult("list", list);
		return new Page("/userCenter/myAdvice.html");
	}
	
	/**
	 * 我的评论
	 * @param form
	 * @return
	 */
	public Page doGetMyEvaluation(WebForm form){
		ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(shopMember==null){
			return new Page("/userCenter/login.java");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", shopMember.getId(), "=");
		qo.addQuery("obj.type", 1, "=");
		IPageList iPageList=this.shopDiscussService.getShopDiscussBy(qo);
		if(iPageList!=null){
			List<ShopDiscuss> list=iPageList.getResult();
			form.addResult("zxList", list);
		}
		
		return new Page("/userCenter/myEvaluation.html");
	}
	
	/**
	 * 退货/退款
	 * @param form
	 * @return
	 */
	public Page doOrderReturn(WebForm form){
		ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(shopMember==null){
			return new Page("/userCenter/login.java");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", shopMember.getId(), "=");
		qo.addQuery("obj.status", 0, "<");
		qo.addQuery("obj.orderInfo.status", 3, "=");
		IPageList iPageList=this.shopOrderdetailService.getShopOrderdetailBy(qo);
		if(iPageList!=null){
			List<?> list=iPageList.getResult();
			form.addResult("orderReturnList", list);
		}
		
		return new Page("/userCenter/order_return_list.html");
	}
	
	/**
	 * 检查用户是否存在
	 * @param form
	 * @return
	 */
	public Page doCheckUser(WebForm form){
		String name=CommUtil.null2String(form.get("name"));
		
		return new Page("/userCenter/order_return_list.html");
	}
	
	/**
	 * 检查用户是否存在
	 * @param form
	 * @return
	 */
	public Page doCheckUserIsOrNotExist(WebForm form){
		String name=CommUtil.null2String(form.get("name"));
		if(!StringUtils.hasText(name)){
			this.addError("msg", "用户名不能为空");
			return pageForExtForm(form);
		}
		QueryObject qo = new QueryObject();
		qo.setPageSize(1);
		qo.addQuery("obj.name", name, "=");
		List<?> list=this.service.getShopMemberBy(qo).getResult();
		if(list!=null){
			this.addError("msg", "用户名已存在");
			return pageForExtForm(form);
		}
		return pageForExtForm(form);
	}
	
	/**
     * 获取关系json
     * @param form
     * @return
     */
    public Page doGetJson(WebForm form){
    	ServletContext sc = ActionContext.getContext().getServletContext();
    	String s = sc.getRealPath("/WEB-INF/huiyuan.txt");
    	FileUtils fu = new FileUtils();
    	String str2 = fu.Read(s); 
    	form.addResult("str", str2);
    	return new Page("/d_shop/distribu/shopdistributor/test2.html");
    }
    
    public Map create(){
		Map<String, Object> mapTop = new LinkedHashMap<String,Object>();
		mapTop.put("name", "白春达");
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pmember is null");
		qo.setPageSize(-1);
		List<ShopMember> list = this.service.getShopMemberBy(qo).getResult();
		List<Map> childlist = new ArrayList<Map>();
		for(ShopMember sd:list){
			Map<String, Object> maps = new LinkedHashMap<String,Object>();
				maps.put("name", sd.getNickname());
				digui(maps,sd.getId());
				childlist.add(maps);
		}
		mapTop.put("children", childlist);
		return mapTop;
	}
	public void digui(Map maps,Long id){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pmember.id",id,"=");
		qo.setPageSize(-1);
		List<ShopMember> list = this.service.getShopMemberBy(qo).getResult();
		List<Map> childlist = new ArrayList<Map>();
		if(list != null && list.size()!=0){
			for(ShopMember sd:list){
				Map<String, Object> map = new LinkedHashMap<String,Object>();
				map.put("name", sd.getNickname());
				digui(map,sd.getId());
				childlist.add(map);
				
			}
			
			maps.put("children",childlist);
		}
	}
	
	public Page doReleaseOldUserBind(WebForm form){
		boolean result = false;
		String memberIdS = CommUtil.null2String(form.get("memberId"));
		Long memberId = 0L;
		if(!memberIdS.equals("")){
			memberId = Long.valueOf(memberIdS);
			ShopMember member = this.service.getShopMember(memberId);
			member.setOldUserStatus("-1");
			result = this.service.updateShopMember(member.getId(), member);
		}	
		
		if(result)
			form.addResult("result", "success");
		else
			form.addResult("result", "failure");
		return new Page("/bcd/member/ajax/releaseOldUser.html");
	}
	/**
	 * 可更改上列表页面
	 * @param 
	 */
	public Page doFinalmember(WebForm form){
		ShopMemberQuery qo = form.toPo(ShopMemberQuery.class);
		qo.addQuery("obj.follower.id",null,"!=");
		IPageList pl = this.service.getShopMemberBy(qo);
		CommUtil.saveIPageList2WebForm(pl, form);
		/*String sqlStr ="SELECT * from disco_shop_shopmember t where t.id not in (SELECT t1.pmember_id from disco_shop_shopmember t1 where t1.pmember_id is not null ) and t.disType = 0";
		 List<ShopMember> list = this.service.querymethod(sqlStr);*/
		//List<ShopMember> list = this.service.getShopMemberBy(qo).getResult();
	     form.addResult("pl", pl);
	     return new Page("/bcd/member/shopmember/ChangeShopMemberList.html");
		
		
	}
	/**
	 * 查询所有会员
	 * @param 
	 */
	public Page doAllmember(WebForm form){
		String memberid2 = CommUtil.null2String(form.get("memberid2"));
		ShopMember member = this.service.getShopMember(Long.parseLong(memberid2));
		ShopMemberQuery qo = form.toPo(ShopMemberQuery.class);
		qo.addQuery("obj.follower.id",null,"!=");
		qo.addQuery("obj.id",Long.parseLong(memberid2), "!=");
		qo.addQuery("obj.dePath not like '"+member.getDePath()+"%'");
        IPageList pl = this.service.getShopMemberListForDisplayByQO(qo);
    	//IPageList pl = this.service.getShopMemberBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("memberid", memberid2);
        form.addResult("pl", pl);
	     return new Page("/bcd/member/shopmember/ChooseShopMemberList.html");
		
		
	}
	/**
	 * 更改关系
	 * @param 
	 */
	public Page doChangeRelation(WebForm form){
		String memberid = CommUtil.null2String(form.get("memberid"));
		String pmemberid = CommUtil.null2String(form.get("pmemberid"));
		ShopMember sm = this.service.getShopMember(Long.parseLong(memberid));
		ShopMember psm = this.service.getShopMember(Long.parseLong(pmemberid));
		if(psm.getDisType()==0){
			sm.setDistributor(psm.getDistributor());
		}else {
			sm.setDistributor(psm.getMyDistributor());
		}
		sm.setPmember(psm);
		sm.setDePath(psm.getDePath()+"@"+sm.getCode());
		this.service.updateShopMember(sm.getId(),sm);
		
	     return go("list");
		
		
	}
	
	/**
	 * 修改会员积分页
	 * @param form
	 * @return
	 */
	public Page doToUpdateMemberIntegral(WebForm form){
		String memberId = CommUtil.null2String(form.get("memberId"));
		Long id = Long.parseLong(memberId);
		ShopMember member = this.service.getShopMember(id);
		form.addResult("member", member);
		return new Page("/bcd/promotions/integral/integralEdit.html");
	}
	
	/**
	 * 变更用户积分
	 * @param form
	 * @return
	 */
	public Page doUpdateMemberIntegral(WebForm form){
		String memberId = CommUtil.null2String(form.get("memberId"));
		String integralStr = CommUtil.null2String(form.get("integral"));
		Long id = Long.parseLong(memberId);
		ShopMember member = this.service.getShopMember(id);
		
		//表单验证
		if(member == null){
			form.addResult("msg", "数据库不存在该用户信息！用户ID="+memberId);
		}
		if(integralStr.equals("")){
			form.addResult("msg", "请输入积分变更数值!");
		}
		
		if( this.service.changeIntegralByAdmin(member, Long.parseLong(integralStr)) ){
			form.addResult("msg", "积分变更成功！");
		}
				
		form.addResult("member", member);
		return new Page("/bcd/promotions/integral/integralEdit.html");
	}
	
	/**
	 * 跳转修改用户金额页面
	 * @param form
	 * @return
	 */
	
	public Page doToUpdateMemberRAmt(WebForm form) {
		Integer id=CommUtil.null2Int(form.get("memberId"));
		ShopMember member = this.service.getShopMember((long)id);
		form.addResult("member", member);
		form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/member/shopmember/RAmtEdit.html");
    }
	
	/**
	 * 修改用户金额页面
	 * @param form
	 * @return
	 */
	
	public Page doUpdateMemberRAmt(WebForm form) {
		ShiroUser user = (ShiroUser)ShiroUtils.getShiroUser();
		if(!"root".equals(user.getName())){
			form.addResult("msg", "你没有权限修改用户金额");
			new Page("/shopmanage/error.html");
		}
		String memberId = CommUtil.null2String(form.get("memberId"));
		Double amt = Double.valueOf(CommUtil.null2String(form.get("amt")));
		String description = CommUtil.null2String(form.get("description"));
		ShopMember member = this.service.getShopMember(Long.valueOf(memberId));
		if(member==null){
			form.addResult("msg", "没有获取此用户");
			new Page("/shopmanage/error.html");
		}
		BigDecimal y_amt = new BigDecimal(Double.toString(member.getRemainderAmt()));
		if(amt>=0){
			BigDecimal amt_ = new BigDecimal(Double.toString(amt));
			member.setRemainderAmt(y_amt.add(amt_).doubleValue());
		}else{
			BigDecimal amt_ = new BigDecimal(Double.toString(-amt));
			member.setRemainderAmt(y_amt.subtract(amt_).doubleValue());
		}
		this.service.updateShopMember(member.getId(), member);
		RemainderAmtHistory rmh = new RemainderAmtHistory();
		rmh.setAmt(amt);
		rmh.setDescription(description);
		rmh.setTenant(TenantContext.getTenant());
		rmh.setType(7);
		rmh.setUser(member);
		this.remainderAmtHistoryService.addRemainderAmtHistory(rmh);
        return go("list");
    }
	
	/**
	 * 获取发送验证码
	 * @param form
	 * @return
	 */
	
	public Page doGetSendCode(WebForm form) {
    	String mobileTel=CommUtil.null2String(form.get("mobileTel"));
    	this.service.sendCode(mobileTel, "【海鲜之家】手机登录验证验证码为：");
        return pageForExtForm(form);
    }
	
	/**
	 * 初始化pc端用户登录密码
	 * @param form
	 * @return
	 */
	
	public Page doInitPwd(WebForm form) {
		String uId = CommUtil.null2String(form.get("uId"));
    	User user = (User)ShiroUtils.getUser();
    	if(!"root".equals(user.getName())){
    		this.addError("msg", "你没有权限初始化用户密码");
    	}
    	if(!hasErrors()){
    		ShopMember member = this.service.getShopMember(Long.valueOf(uId));
    		member.setPassword(MD5.encode("123456"));
    		this.service.updateShopMember(member.getId(), member);
    	}
        return pageForExtForm(form);
    }
	
    public void setShopOrderdetailService(
			IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}

	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}

	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}
    
	public void setRemainderAmtHistoryService(
			IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}

	public void setService(IShopMemberService service) {
        this.service = service;
    }
}