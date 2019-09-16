package com.eastinno.otransos.shop.distribu.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.core.domain.ShopSystemConfig;
import com.eastinno.otransos.shop.core.service.IShopSystemConfigService;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.query.ShopMemberQuery;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.FileUtils;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 分销会员
 * @author nsz
 */
@Action
public class ShopDistributorAction extends AbstractPageCmdAction {
    @Inject
    private IShopDistributorService service;
    @Inject
    private IShopSystemConfigService shopSystemConfigService;
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    @Inject
    private IShopMemberService shopMemberService;
    @Inject
    private ICustomCouponService customCouponService;
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String statusStr = CommUtil.null2String(form.get("status"));
        String disType = CommUtil.null2String(form.get("disType"));
        if("".equals(statusStr)){
        	statusStr = "1";
        }
        if("0".equals(statusStr)){
        	qo.addQuery("obj.exStatus not in (0,2)");
        	qo.addQuery("obj.status in (0,2)");
        }else{
        	qo.addQuery("obj.status",Integer.parseInt(statusStr),"=");
        }
        if(StringUtils.hasText(disType)){
        	qo.addQuery("obj.disType", Integer.valueOf(disType), "=");
        }
        String myShopName=CommUtil.null2String(form.get("myShopName"));
        if(StringUtils.hasText(myShopName)){
        	qo.addQuery("obj.myShopName like '%"+myShopName+"%'");
        }
        String memberid=CommUtil.null2String(form.get("memberid"));
        if(StringUtils.hasText(memberid)){
        	qo.addQuery("obj.member.id",Long.parseLong(memberid),"=");
        }
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList pl = this.service.getShopDistributorBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        if("1".equals(statusStr)){
        	return new Page("/d_shop/distribu/shopdistributor/list.html");
        }else if("0".equals(statusStr)){
        	return new Page("/d_shop/distribu/shopdistributor/applylist.html");
        }else{
        	return new Page("/d_shop/distribu/shopdistributor/applynolist.html");
        }
    }
    /**
     * 审批分销会员
     * @param form
     * @return
     */
    public Page doChangeApplyStatus(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	String status = CommUtil.null2String(form.get("status"));
    	Integer statusInt = Integer.parseInt(status);
    	ShopDistributor obj = this.service.getShopDistributor(Long.parseLong(id));
    	obj.setStatus(Integer.parseInt(status));
    	obj.setDisType(1);
    	if(status.equals("2")){
    		this.service.updateShopDistributor(obj.getId(), obj);
    	}else{
	    	this.service.updateShopDistributor(obj.getId(), obj);
	    	//审批通知
	    	Follower f = obj.getMember().getFollower();
	    	if(f!=null){
	    		Account a = f.getAccount();
		    	String msg = "恭喜您的申请通过";
		    	if(statusInt!=1){
		    		msg="对不起你的申请没有通过";
		    	}
		    	WeixinBaseUtils.sendMsgToFollower(a, f, msg);
	    	}
	    	//审批
	    	this.service.updateChangeWxShop(obj);
	    	this.customCouponService.disableCustomCoupon(obj.getMember());//判断优惠券失效
    	}
    	/**
    	 * 跳转
    	 */
    	QueryObject qo3 = new QueryObject();
    	//qo3.addQuery("obj.status",Integer.parseInt("0"),"=");
    	qo3.addQuery("obj.exStatus", Integer.valueOf(1), "!=");
    	qo3.addQuery("obj.status in (0,2)");
    	qo3.setOrderBy("createDate");
    	qo3.setOrderType("desc");
    	IPageList pl = this.service.getShopDistributorBy(qo3);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/d_shop/distribu/shopdistributor/applylist.html");
    }
    /**
     * 审批体验店
     * @param service
     */
    public Page doChangeDisbutorType(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	String exStatus = CommUtil.null2String(form.get("exStatus"));
    	Integer statusInt = Integer.parseInt(exStatus);//实体店申请状态 1：通过 2：拒绝
    	ShopDistributor obj = this.service.getShopDistributor(Long.parseLong(id));
    	obj.setExStatus(Integer.parseInt(exStatus));
    	/**
    	 * 审批通知
    	 */
    	Follower f = obj.getMember().getFollower();
    	Account a = f.getAccount();
    	String msg = "恭喜您的申请通过";
    	if(statusInt!=1){
    		msg="对不起你的申请没有通过";
    	}
    	WeixinBaseUtils.sendMsgToFollower(a, f, msg);
    	/**
    	 * 审批通知结束
    	 */
    	if("1".equals(exStatus)){
    		this.service.updateChangeEntityShop(obj);
    		this.customCouponService.disableCustomCoupon(obj.getMember());//判断优惠券失效
    	}else{
    		boolean b=this.service.updateShopDistributor(obj.getId(), obj);
    	}
    	return go("tydapply");
    }
    
    /**
     * 进入体验店申请列表
     * @param service
     */
    public Page doDeleteDis(WebForm form){
    	int id=CommUtil.null2Int(form.get("id"));
    	try {
    		boolean b=this.service.delShopDistributor((long)id);
        	if(!b){
        		this.addError("msg", "删除失败");
        	}
		} catch (Exception e) {
			this.addError("msg", "删除失败");
		}
        return pageForExtForm(form);
    }
    
    
    
    /**
     * 进入体验店申请列表
     * @param service
     */
    public Page doTydapply(WebForm form){
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.disType", 2, "=");
    	//qo.addQuery("obj.exStatus in (0,1,2)");
    	 String myShopName=CommUtil.null2String(form.get("myShopName"));
         if(StringUtils.hasText(myShopName)){
         	qo.addQuery("obj.myShopName like '%"+myShopName+"%'");
         }
    	qo.setOrderBy("openAccountApplyDate");
    	qo.setOrderType("desc");
    	IPageList pl = this.service.getShopDistributorBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("list", pl.getResult());
        return new Page("/d_shop/distribu/shopdistributor/exapplylist.html");
    }
    /**
     * 查看我的下级
     * @param form
     * @return
     */
    public Page doChilrenList(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	ShopDistributor obj = this.service.getShopDistributor(Long.parseLong(id));
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.dePath", obj.getDePath()+"%","like");
    	IPageList pl = this.service.getShopDistributorBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/d_shop/distribu/shopdistributor/childrenlist.html");
    }
    /**
     * 进入变更上级
     * @param form
     * @return
     */
    public Page doToChangeParent(WebForm form){
    	Long id = new Long(CommUtil.null2String(form.get("id")));
    	ShopDistributor distributor = service.getShopDistributor(id);
		form.addResult("obj", distributor);
		return new Page("/bcd/distribution/member/changeLeader.html");
    }
    /**
     * 修改分销商设置
     * @param form
     * @return
     */
    public Page doUpdateDisSet(WebForm form){
    	Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopSystemConfig entry = this.shopSystemConfigService.getShopSystemConfig(id);
        form.toPo(entry);
        this.shopSystemConfigService.updateShopSystemConfig(entry.getId(), entry);
        form.addResult("obj", entry);
    	return new Page("/d_shop/distribu/shopdistributor/baseset.html");
    }
    /**
     * 进入分销商设置编辑页面
     * @param form
     * @return
     */
    public Page doToEditDIsSet(WebForm form){
    	Tenant t = ShiroUtils.getTenant();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tenant",t,"=");
    	qo.setPageSize(1);
    	List<ShopSystemConfig> list = this.shopSystemConfigService.getShopSystemConfigBy(qo).getResult();
    	ShopSystemConfig config=null;
    	if(list==null || list.size()==0){
    		config  = new ShopSystemConfig();
    		config.setTenant(t);
    		this.shopSystemConfigService.addShopSystemConfig(config);
    	}else{
    		config = list.get(0);
    	}
    	form.addResult("obj", config);
    	return new Page("/d_shop/distribu/shopdistributor/baseset.html");
    }
    /**
     * 获取关系json
     * @param form
     * @return
     * @throws IOException 
     */
    public Page doGetJson(WebForm form) throws IOException{
    	ServletContext sc = ActionContext.getContext().getServletContext();
    	String s = sc.getRealPath("/WEB-INF/weidian.txt");
    	FileUtils fu = new FileUtils();
    	String str2 = fu.Read(s); 
    	form.addResult("str", str2);
    	return new Page("/d_shop/distribu/shopdistributor/test.html");
    	
    }
    /**
     * 手动更新数据
     * @param service
     * @throws IOException 
     */
    public Page doUpdateFile(WebForm form) throws IOException{
    	Map map = new HashMap();
    	ServletContext sc = ActionContext.getContext().getServletContext();
    	String s = sc.getRealPath("/WEB-INF/weidian.txt");
     	Map<String, Object> maps = new LinkedHashMap<String,Object>();
     	maps = this.service.create();
     	String str = JSONObject.toJSONString(maps);
     	FileUtils fu = new FileUtils();
     	fu.InputAndCover(s, str);
     	map.put("flag", "更新成功");
     	form.jsonResult(map);
     	return Page.JSONPage;
    }
    
    /**
	 * 分销订单
	 * @param form
	 * @return
	 */
    public Page doOrderList(WebForm form){
    	QueryObject qo = form.toPo(QueryObject.class);
    	//Tenant t = ShiroUtils.getTenant();
    	Tenant t =TenantContext.getTenant();
    	qo.addQuery("obj.tenant",t,"=");
    	qo.addQuery("obj.distributor is NOT EMPTY");
    	IPageList pl = this.shopOrderInfoService.getShopOrderInfoBy(qo);
    	CommUtil.saveIPageList2WebForm(pl, form);
    	return new Page("/d_shop/distribu/disorder/distributeOrderList.html");
    }
    
    /**
     * 进入更改体验店隶属列表页面
     * @param form
     * @return
     */
    public Page doListEntityShop(WebForm form){
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	String disid = CommUtil.null2String(form.get("id"));
    	qo.addQuery("obj.exStatus",Integer.parseInt("1"),"=");
    	IPageList pl = this.service.getShopDistributorBy(qo);
    	 CommUtil.saveIPageList2WebForm(pl, form);
    	form.addResult("pl",pl);
    	form.addResult("disid",disid);
    	return new Page("/d_shop/distribu/shopdistributor/entityShop.html");
    }
    /**
     * 手动更改体验店
     * @param form
     * @return
     */
    public Page doChangeEntityShop(WebForm form){
    	String entityid = CommUtil.null2String(form.get("id"));
    	String disid = CommUtil.null2String(form.get("disid"));
    	ShopDistributor entityshop = null;
    	ShopDistributor distributor = null;
    	ShopMember member = null;
    	if(!"".equals(entityid)){
    		entityshop = this.service.getShopDistributor(Long.parseLong(entityid));
    	}
    	if(!"".equals(disid)){
    		distributor = this.service.getShopDistributor(Long.parseLong(disid));
    		member = distributor.getMember();
    	}
    	
    	distributor.setJoinTime(new Date());
    	distributor.setTopDistributor(entityshop);
    	
    	/**
	     * 批量跟新结束
	    */
    	return go("list");
    
    }
    
    /**
     * 进入有佣金的会员列表页面
     * @param form
     * @return
     */
    public Page doListCommission(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("(obj.status="+1+" or obj.exStatus="+1+")");
        qo.addQuery("obj.totalCommission",Double.parseDouble("0"),"!=");
        IPageList pl = this.service.getShopDistributorBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("fu", formatUtil.fu);
        return new Page("/d_shop/distribu/shopdistributor/choselist.html");
    }
    /**
     * 进入选择上级会员列表页面
     * @param form
     * @return
     */
    public Page doListUp(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.follower.id",null,"!=");
        IPageList pl = this.shopMemberService.getShopMemberBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/d_shop/distribu/shopdistributor/choselist.html");
    }
    
    public void setService(IShopDistributorService service) {
        this.service = service;
    }
	public void setShopSystemConfigService(
			IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
    
    
   
    /**
     * 更改推荐关系
     * @param form
     * @return
     */
    public Page doChangeRalation(WebForm form) {
    	String id = CommUtil.null2String(form.get("memberid"));
    	String pid = CommUtil.null2String(form.get("pmemberid"));
    	if(!"".equals(id) && !"".equals(pid)){
    		ShopMember member = this.shopMemberService.getShopMember(Long.parseLong(id));
    		ShopMember pmember = this.shopMemberService.getShopMember(Long.parseLong(pid));
    		this.service.changeRalation(member,pmember);
    		
    	}
    	ShopMemberQuery qo = form.toPo(ShopMemberQuery.class);
		qo.addQuery("obj.follower.id",null,"!=");
		IPageList pl = this.shopMemberService.getShopMemberBy(qo);
		CommUtil.saveIPageList2WebForm(pl, form);
	     form.addResult("pl", pl);
	     return new Page("/bcd/member/shopmember/ChangeShopMemberList.html");
    }
	public ICustomCouponService getCustomCouponService() {
		return customCouponService;
	}
	public void setCustomCouponService(ICustomCouponService customCouponService) {
		this.customCouponService = customCouponService;
	}
	public IShopDistributorService getService() {
		return service;
	}
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}
    
}
