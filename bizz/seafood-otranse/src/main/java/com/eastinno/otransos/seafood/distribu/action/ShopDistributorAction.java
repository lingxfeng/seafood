package com.eastinno.otransos.seafood.distribu.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.promotions.service.ICustomCouponService;
import com.eastinno.otransos.seafood.trade.domain.OrderDetailShow;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.query.ShopMemberQuery;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.util.FileUtils;
import com.eastinno.otransos.seafood.util.formatUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
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
    @Inject
    private ISystemRegionService systemRegionService;
    
    public Page doToUpdate(WebForm form){
    	String idStr=CommUtil.null2String(form.get("id"));
    	Long id = null;
    	if(idStr.equals("")){
    		form.addResult("msg", "没有传入微店ID，请求有误！请重新进入本页！");
    	}else{
    		id = Long.valueOf(idStr);
    		ShopDistributor distributor = this.service.getShopDistributor(id);
    		form.addResult("entry", distributor);
    	}
    	
    	/*
    	 * 查询地区信息
    	 */
    	QueryObject qoArea = new QueryObject();
		qoArea.addQuery("obj.lev", 1, "=");
		qoArea.setLimit(-1);
		List<?> list=this.systemRegionService.querySystemRegion(qoArea).getResult();
		form.addResult("rList", list);
		
    	return new Page("/d_shop/distribu/shopdistributor/shopdistributorsave.html");
    }
    
    public Page doUpdate(WebForm form){
    	String idStr=CommUtil.null2String(form.get("id"));
    	Long id = null;
    	if(idStr.equals("")){
    		form.addResult("msg", "没有传入微店ID，请求有误！更新失败！");
    	}else{
    		id = Long.valueOf(idStr);    		
    		ShopDistributor distributor = this.service.getShopDistributor(id);
    		form.toPo(distributor);
    		String areaStr = CommUtil.null2String(form.get("area_id"));
    		SystemRegion region = this.systemRegionService.getSystemRegionBySn(areaStr);
    		distributor.setArea(region);
    		this.service.updateShopDistributor(id, distributor);    		
    		distributor = this.service.getShopDistributor(id);
    		form.addResult("entry", distributor);
    		form.addResult("msg", "更新成功！");
    	}
    	
    	/*
    	 * 查询地区信息
    	 */
    	QueryObject qoArea = new QueryObject();
		qoArea.addQuery("obj.lev", 1, "=");
		qoArea.setLimit(-1);
		List<?> list=this.systemRegionService.querySystemRegion(qoArea).getResult();
		form.addResult("rList", list);
    	
    	return new Page("/d_shop/distribu/shopdistributor/shopdistributorsave.html");
    }
    
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
        String membername=CommUtil.null2String(form.get("membername"));
        if(StringUtils.hasText(membername)){
        	qo.addQuery("obj.member.nickname like '%"+membername+"%'");
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
    
  //统计分销商数量
    public Page doCalculateDistri(WebForm form){
		int levelone = 0;
		int leveltow = 0;
		int levelthree = 0;
		//统计加盟店数量
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.disType",2,"=");
		qo.addQuery("obj.exStatus",1,"=");
		qo.setPageSize(-1);
		List<ShopDistributor> list1 = this.service.getShopDistributorBy(qo).getResult();
		levelone = list1.size();
		
		
		//统计分销商总数量
		int total = 0;
		QueryObject qo2 = new QueryObject();
		qo2.addQuery("obj.status",1,"=");
		qo2.addQuery("obj.exStatus",1,"!=");
		qo2.setPageSize(-1);
		List<ShopDistributor> list2 = this.service.getShopDistributorBy(qo2).getResult();
		total = list2.size();
		
		//统计三级分销商数量
		QueryObject qo3 = new QueryObject();
		qo3.addQuery("obj.exStatus",1,"!=");
		qo3.addQuery("obj.status",1,"=");
		qo3.addQuery("obj.parent is not null");
		qo3.addQuery("obj.parent.exStatus",1,"!=");
		qo3.addQuery("obj.parent.status",1,"=");
		qo3.setPageSize(-1);
		List<ShopDistributor> list3 = this.service.getShopDistributorBy(qo3).getResult();
		levelthree = list3.size();
		//统计二级分销商数量
		leveltow = total - levelthree;
		
		//统计消费过的会员数量
		QueryObject qo4 = new QueryObject();
		qo4.addQuery("obj.consumptionAmount",Double.parseDouble("0"),"!=");
		qo4.setPageSize(-1);
		List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo4).getResult();
		int totalmemebr = 0;
		if(list != null){
			totalmemebr = list.size();
		}
		
		
		form.addResult("one", levelone);
		form.addResult("tow", leveltow);
		form.addResult("three", levelthree);
		form.addResult("member", totalmemebr);
		return new Page("/bcd/count/count.html");
    	}

    /**
     * 导出加盟店
     * @param form
     * @return
     */
	public Page doExportEntity(WebForm form){
		Map map = new HashMap<>();
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("加盟店明细");
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式  
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号"); 
        cell.setCellStyle(style);
        
        cell = row.createCell( (short) 1);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("所属省份");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("店铺地址");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("用户昵称");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("申请时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("批准时间");  
        cell.setCellStyle(style);
        QueryObject qo = new QueryObject();
		qo.addQuery("obj.disType",2,"=");
		qo.addQuery("obj.exStatus",1,"=");
		qo.setOrderBy("joinTime");
		qo.setOrderType("desc");
		qo.setPageSize(-1);
		List<ShopDistributor> list1 = this.service.getShopDistributorBy(qo).getResult();
		if(list1 != null && list1.size() != 0){
			int i=0;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(ShopDistributor dis:list1){
				String province = this.getProvince(dis.getArea());
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i+1);
				row.createCell(1).setCellValue(dis.getMyShopName());
				row.createCell(2).setCellValue(province);
				row.createCell(3).setCellValue(dis.getOpenAccountAddress());
				row.createCell(4).setCellValue(dis.getMember().getNickname());
				row.createCell(5).setCellValue(formatter.format(dis.getCreateDate()));
				row.createCell(6).setCellValue(dis.getJoinTime() == null?"null":formatter.format(dis.getJoinTime()));
				i = i + 1;
			}
		}
		try {
			String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        s = s.substring(0,s.length()-17) + "/static/EntityShop.xls";
	        
			File f = new File(s);
		    if (f.exists()){
		    	f.delete();
		    }
		    f.createNewFile();	
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            map.put("info", "导出成功!");
            System.out.println("导出成功!");
        } catch (FileNotFoundException e) {
        	map.put("info", "导出失败!");
            System.out.println("导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
        	map.put("info", "导出失败!");
        	System.out.println("导出失败!");
            e.printStackTrace();
        }
		
		//ServletContext sc = ActionContext.getContext().getServletContext();
    	//String s = sc.getRealPath("/WEB-INF/static/OrderDetail.xls");
		String s = "static/EntityShop.xls";
    	map.put("str", s);
    	form.jsonResult(map);
		return Page.JSONPage;
    }
	
	/**
     * 导出分销商
     * @param form
     * @return
     */
	public Page doExportVirtual(WebForm form){
		Map map = new HashMap<>();
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("分销商明细");
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式  
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号"); 
        cell.setCellStyle(style);
        
        cell = row.createCell( (short) 1);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("所属省份");  
        cell.setCellStyle(style); 
        
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("用户昵称");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("申请时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("批准时间");  
        cell.setCellStyle(style);
        QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",1,"=");
		qo.addQuery("obj.exStatus",1,"!=");
		qo.setOrderBy("joinTime");
		qo.setOrderType("desc");
		qo.setPageSize(-1);
		List<ShopDistributor> list1 = this.service.getShopDistributorBy(qo).getResult();
		if(list1 != null && list1.size() != 0){
			int i=0;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(ShopDistributor dis:list1){
				String province = this.getProvince(dis.getArea());
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i+1);
				row.createCell(1).setCellValue(dis.getMyShopName());
				row.createCell(2).setCellValue(province);
				row.createCell(3).setCellValue(dis.getMember().getNickname());
				row.createCell(4).setCellValue(formatter.format(dis.getCreateDate()));
				row.createCell(5).setCellValue(dis.getJoinTime()== null?"null":formatter.format(dis.getJoinTime()));
				i = i + 1;
			}
		}
		try {
			String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        s = s.substring(0,s.length()-17) + "/static/virtualShop.xls";
	        
			File f = new File(s);
		    if (f.exists()){
		    	f.delete();
		    }
		    f.createNewFile();	
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            map.put("info", "导出成功!");
            System.out.println("导出成功!");
        } catch (FileNotFoundException e) {
        	map.put("info", "导出失败!");
            System.out.println("导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
        	map.put("info", "导出失败!");
        	System.out.println("导出失败!");
            e.printStackTrace();
        }
		
		//ServletContext sc = ActionContext.getContext().getServletContext();
    	//String s = sc.getRealPath("/WEB-INF/static/OrderDetail.xls");
		String s = "static/virtualShop.xls";
    	map.put("str", s);
    	form.jsonResult(map);
		return Page.JSONPage;
    }
	
	
	/**
     * 导出分消费用户
     * @param form
     * @return
     */
	public Page doExportShopMember(WebForm form){
		Map map = new HashMap<>();
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("消费用户明细");
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式  
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号"); 
        cell.setCellStyle(style);
        
        cell = row.createCell( (short) 1);  
        cell.setCellValue("会员名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("消费类型");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("收货人");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("地址");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("订单总价");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("下单时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("支付时间");  
        cell.setCellStyle(style);
        QueryObject qo = new QueryObject();
		qo.addQuery("obj.consumptionAmount",Double.parseDouble("0"),"!=");
		qo.setPageSize(-1);
		List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo).getResult();
		if(list != null && list.size() != 0){
			int i=0;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(ShopMember member:list){
				
				QueryObject qo2 = new QueryObject();
				qo2.setLimit(-1);
				qo2.addQuery("obj.user.id",member.getId(),"=");
				qo2.addQuery("obj.status in (1,2,3,4,6)");
				List<ShopOrderInfo> listorder = this.shopOrderInfoService.getShopOrderInfoBy(qo2).getResult();
				if(listorder != null && listorder.size() != 0){
					for(ShopOrderInfo order:listorder){
						row = sheet.createRow(i + 1);
						row.createCell(0).setCellValue(i+1);
						row.createCell(1).setCellValue(member.getNickname());
						if(order.getShopSinceSome() != null){
							row.createCell(2).setCellValue("上门自提");
							row.createCell(3).setCellValue(order.getShopSinceSome().getTrueName());
							row.createCell(4).setCellValue(order.getShopSinceSome().getShopDistributor().getArea().getFullTitle()+order.getShopSinceSome().getShopDistributor().getOpenAccountAddress());
							row.createCell(5).setCellValue(order.getShopSinceSome().getTelephone());
						}else{
							row.createCell(2).setCellValue("快递发货");
							row.createCell(3).setCellValue(order.getAddr().getTrueName());
							row.createCell(4).setCellValue(order.getAddr().getArea().getFullTitle()+order.getAddr().getArea_info());
							row.createCell(5).setCellValue(order.getAddr().getTelephone());
						}
						row.createCell(6).setCellValue(order.getGross_price());
						row.createCell(7).setCellValue(order.getCeateDate()== null?"null":formatter.format(order.getCeateDate()));
						row.createCell(8).setCellValue(order.getTradeDate()== null?"null":formatter.format(order.getTradeDate()));
						i = i + 1;
					}
				}
			}
		}
		try {
			String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        s = s.substring(0,s.length()-17) + "/static/shopMember.xls";
			File f = new File(s);
		    if (f.exists()){
		    	f.delete();
		    }
		    f.createNewFile();	
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            map.put("info", "导出成功!");
            System.out.println("导出成功!");
        } catch (FileNotFoundException e) {
        	map.put("info", "导出失败!");
            System.out.println("导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
        	map.put("info", "导出失败!");
        	System.out.println("导出失败!");
            e.printStackTrace();
        }
		
		//ServletContext sc = ActionContext.getContext().getServletContext();
    	//String s = sc.getRealPath("/WEB-INF/static/OrderDetail.xls");
		String s = "static/shopMember.xls";
    	map.put("str", s);
    	form.jsonResult(map);
		return Page.JSONPage;
    }
	
	
	public String getProvince(SystemRegion area){
		String provincename="";
		if(area.getParent() != null){
			if(area.getParent().getParent() != null){
				provincename = area.getParent().getParent().getTitle();
			}else{
				provincename = area.getParent().getTitle();
			}
		}else{
			provincename = area.getTitle();
		}
		return provincename.substring(0,provincename.length() - 1);
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

	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}
    
}
