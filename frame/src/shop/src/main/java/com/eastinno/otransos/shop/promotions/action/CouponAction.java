package com.eastinno.otransos.shop.promotions.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IBrandService;
import com.eastinno.otransos.shop.droduct.service.IBrandTypeService;
import com.eastinno.otransos.shop.droduct.service.IProductTypeService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.domain.SingleDispatchCouponRecord;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.shop.promotions.service.ICouponService;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.promotions.service.ISingleDispatchCouponRecordService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.query.ShopMemberQuery;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;

/**
 * CouponAction
 * @author 
 */
@Action
public class CouponAction extends AbstractPageCmdAction {
    @Inject
    private ICouponService service;
    @Inject
    private IBrandService brandService;
    @Inject
    private IBrandTypeService brandTypeService;
    @Inject
    private IProductTypeService productTypeService;
    @Inject
    private IShopProductService shopProductService;
    @Inject
    private IShopMemberService shopMemberService;
    @Inject
    private ICustomCouponService customCouponService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private ISingleDispatchCouponRecordService singleDispatchCouponRecordService;
    
    /**
     * 有效 优惠券列表页面
     * 
     * @param form
     */
    public Page doValidList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.currentStatus",Short.parseShort("1"),"=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pl = this.service.getCouponBy(qo);
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/promotions/coupons/validList.html");
    }
    /**
     * 无效效 优惠券列表页面
     * 
     * @param form
     */
    public Page doInvalidList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.currentStatus",Short.parseShort("0"),"=");
        IPageList pl = this.service.getCouponBy(qo);
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/promotions/coupons/invalidList.html");
    }
    /**
     * 到期优惠券列表页面
     * 
     * @param form
     */
    public Page doOutTimeList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.currentStatus",Short.parseShort("2"),"=");
        IPageList pl = this.service.getCouponBy(qo);
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/promotions/coupons/outList.html");
    }
    /**
     * 保存新增
     */
    public Page doToSave(WebForm form){
    	String idStr = CommUtil.null2String(form.get("id"));
    	if(!"".equals(idStr)){
    		 Long id = Long.valueOf(Long.parseLong(idStr));
    	     Coupon entry = this.service.getCoupon(id);
    	     form.addResult("entry", entry);
    	}
    	setBrandType(form);
    	setProductTypes(form);
    	return new Page("/bcd/promotions/coupons/couponEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
         Coupon entry = (Coupon)form.toPo(Coupon.class);
         form.toPo(entry);
    	 if (!hasErrors()) {    
        	 Long id = this.service.addCoupon(entry);
        	 
         }
    	 /**
          * 更新可使用优惠券的品牌
          */
         String brandStr = CommUtil.null2String(form.get("brandli"));
         if(!"".equals(brandStr)){
         	String[] brands = brandStr.split("_");
         	List<Brand> brandList = new ArrayList<Brand>();
         	for(String brandId:brands){
         		Brand brand = this.brandService.getBrand(Long.parseLong(brandId));
         		brandList.add(brand);
         	}
         	entry.setBrandlist(brandList);
         }else{
         	entry.setBrandlist(new ArrayList<Brand>());
         }
         /**
          * 更新使用优惠券的商品
          */
         String productStr = CommUtil.null2String(form.get("productli"));
         if(!"".equals(productStr)){
         	String[] products = productStr.split("_");
         	List<ShopProduct> productList = new ArrayList<ShopProduct>();
         	for(String productId:products){
         		ShopProduct product = this.shopProductService.getShopProduct(Long.parseLong(productId));
         		productList.add(product);
         	}
         	entry.setProductlist(productList);;
         }
         String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                 + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
         if(!"".equals(imgPath)){
         	this.cusUploadFileService.addCusUploadFile(imgPath);
         	entry.setBackImgPath(imgPath);
         }
         this.service.updateCoupon(entry.getId(), entry);
    	 return go("validList");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        Coupon entry = this.service.getCoupon(id);
        form.toPo(entry);
        if (!hasErrors()) {
        	/**
             * 更新可使用优惠券的品牌
             */
            String brandStr = CommUtil.null2String(form.get("brandli"));
            if(!"".equals(brandStr)){
            	String[] brands = brandStr.split("_");
            	List<Brand> brandList = new ArrayList<Brand>();
            	for(String brandId:brands){
            		Brand brand = this.brandService.getBrand(Long.parseLong(brandId));
            		brandList.add(brand);
            	}
            	entry.setBrandlist(brandList);
            }else{
            	entry.setBrandlist(new ArrayList<Brand>());
            }
            /**
             * 更新使用优惠券的商品
             */
            String productStr = CommUtil.null2String(form.get("productli"));
            if(!"".equals(productStr)){
            	String[] products = productStr.split("_");
            	List<ShopProduct> productList = new ArrayList<ShopProduct>();
            	for(String productId:products){
            		ShopProduct product = this.shopProductService.getShopProduct(Long.parseLong(productId));
            		productList.add(product);
            	}
            	entry.setProductlist(productList);
            }
            String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                    + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
            if(!"".equals(imgPath)){
            	this.cusUploadFileService.addCusUploadFile(imgPath);
            	entry.setBackImgPath(imgPath);
            }
            boolean ret = service.updateCoupon(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("validList");
    }
    
    /**
     * 变更是否有效
     * 
     * @param form
     */
    public Page doChangeStatus(WebForm form) {
       Long id = new Long(CommUtil.null2String(form.get("id")));
       String status = CommUtil.null2String(form.get("status"));
       Coupon cp = this.service.getCoupon(id);
       cp.setCurrentStatus(Short.parseShort(status));
       this.service.updateCoupon(cp.getId(), cp);
       return go("validList");
    }
    /**
     * 单独发放跳转
     * 
     * @param form
     */
    public Page doDispatchBefore(WebForm form) {
       String cId=CommUtil.null2String(form.get("id"));
       Long id=null;
       if(StringUtils.hasText(cId)){
    	   id = Long.valueOf(cId);
       }
       ShopMemberQuery qo = form.toPo(ShopMemberQuery.class);
       String pmemberId=CommUtil.null2String(form.get("pmemberId"));
       String nickname=CommUtil.null2String(form.get("nickname"));
       String memberid=CommUtil.null2String(form.get("memberid"));
       if(StringUtils.hasText(pmemberId)){
    	   qo.addQuery("obj.pmember.id", Long.valueOf(pmemberId), "=");
       }
       if(StringUtils.hasText(nickname)){
    	   qo.addQuery("obj.nickname", nickname, "=");
       }
       if(StringUtils.hasText(memberid)){
    	   qo.addQuery("obj.id", Long.parseLong(memberid), "=");
       }
       IPageList pl = this.shopMemberService.getShopMemberListForDisplayByQO(qo);
       CommUtil.saveIPageList2WebForm(pl, form);
       form.addResult("pl", pl);
       form.addResult("id", id);
       return new Page("/bcd/promotions/coupons/chooseMemberList.html");
    }
    /**
     * 单独发放
     * 
     * @param form
     */
    public Page doDispath(WebForm form) {
       String id = CommUtil.null2String(form.get("id"));
       String memberid = CommUtil.null2String(form.get("userId"));
       if(!"".equals(memberid) && !"".equals(id)){
    	   QueryObject qo = new QueryObject();
    	   
    	   ShopMember member = this.shopMemberService.getShopMember(Long.parseLong(memberid));
    	   List<CustomCoupon> list = member.getCustomCoupon();
    	   int flag = 0;
    	   for(CustomCoupon ccpon:list){
    		   if(minuteBetween(ccpon.getCreateTime(),new Date())<60){
    			   flag = 1;
    		   }
    	   }
    	   if(flag != 1){
    		   Coupon cp = this.service.getCoupon(Long.parseLong(id));
        	   CustomCoupon cc = new CustomCoupon();
        	   cc.setCoupon(cp);
        	   cc.setShopMember(member);
        	   this.customCouponService.addCustomCoupon(cc);
        	   SingleDispatchCouponRecord sr = new SingleDispatchCouponRecord();
        	   sr.setCoupon(cp);
        	   sr.setShopMember(member);
        	   sr.setType(Short.parseShort("-1"));
        	   this.singleDispatchCouponRecordService.addSingleDispatchCouponRecord(sr);
        	   if(cp.getUseType()==0 || member.getDisType() == 0){
        		   notice(member);
        	   }
        	   form.addResult("count", 1);
               form.addResult("coupon", cp);
    	   } 
       }
       return new Page("/bcd/promotions/coupons/dispatchResult.html");
    }
	 private static int minuteBetween(Date smdate,Date bdate){    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");  
        try {
			smdate=sdf.parse(sdf.format(smdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        try {
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000);
            
       return Integer.parseInt(String.valueOf(between_days));           
	    }
    /**
     * 批量发放
     * 
     * @param form
     */
    public Page doQuantityDispath(WebForm form) {
       Long id = new Long(CommUtil.null2String(form.get("id")));
       String type = CommUtil.null2String(form.get("type"));
       Coupon cp = this.service.getCoupon(id);
       long count = 0;
       SingleDispatchCouponRecord sr = new SingleDispatchCouponRecord();
       sr.setType(Short.parseShort(type));
       sr.setCoupon(cp);
       sr.setCreateTime(new Date());
       this.singleDispatchCouponRecordService.addSingleDispatchCouponRecord(sr);
       if("1".equals(type)){//全场发放
    	   QueryObject qo = new QueryObject();
    	   qo.addQuery("obj.follower.id is not null");
    	   qo.setPageSize(-1);
    	   List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo).getResult();
    	   for(ShopMember member:list){
    		   CustomCoupon cc = new CustomCoupon();
    		   cc.setCoupon(cp);
    		   cc.setShopMember(member);
    		   this.customCouponService.addCustomCoupon(cc);
    		   if(cp.getUseType()==0 || member.getDisType() == 0){
        		   notice(member);
        	   }
    		   count += 1;
    	   }
       }else if("2".equals(type)){//会员发放
    	   QueryObject qo = new QueryObject();
    	   qo.addQuery("obj.follower is not EMPTY");
    	   qo.addQuery("obj.disType",Integer.parseInt("0"), "=");
    	   qo.setPageSize(-1);
    	   List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo).getResult();
    	   for(ShopMember member:list){
    		   CustomCoupon cc = new CustomCoupon();
    		   cc.setCoupon(cp);
    		   cc.setShopMember(member);
    		   this.customCouponService.addCustomCoupon(cc);
        	   if(cp.getUseType()==0 || member.getDisType() == 0){
        		   notice(member);
        	   }
    		   count += 1;
    	   }
       }
       form.addResult("count", count);
       form.addResult("coupon", cp);
       return new Page("/bcd/promotions/coupons/dispatchResult.html");
    }
    /**
     * 微信通知
     * @param member
     */
    private void notice(ShopMember member){
    	Follower f =member.getFollower();
    	if(member.getFollower()!=null){
    		Account a = f.getAccount();
        	String msg = "新的优惠券已发放到您的账户，快去使用吧！";
        	WeixinBaseUtils.sendMsgToFollower(a, f, msg);
    	}
    }
    private void setBrandType(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setOrderBy("sequence");
    	qo.setPageSize(-1);
        List<?> bTypeList=this.brandTypeService.getBrandTypeBy(qo).getResult();
        form.addResult("brandTypes", bTypeList);
        
        qo = new QueryObject();
        qo.setPageSize(-1);
        List<?> brands=this.brandService.getBrandBy(qo).getResult();
        form.addResult("brands", brands);
    }
    /**
     * 设置商品类型
     * @param service
     */
    private void setProductTypes(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.parent is EMPTY");
    	qo.setPageSize(-1);
    	List<?> list = this.productTypeService.getProductTypeBy(qo).getResult();
    	form.addResult("proTypes", list);
    	
    	qo = new QueryObject();
    	qo.addQuery("obj.status",Short.parseShort("1"),"=");
    	qo.setPageSize(-1);
    	List<?> products = this.shopProductService.getShopProductBy(qo).getResult();
    	form.addResult("products", products);
    }
    
    /**
     * 订单状态更改结束
     * 进行优惠券判定
     */
    public Page doTest(WebForm form){
    		Date nowtime = new Date();
    	 	QueryObject qo2 = new QueryObject();
    	    qo2.addQuery("obj.currentStatus",Short.parseShort("1"), "=");
    	    List<Coupon> listcoupon = this.service.getCouponBy(qo2).getResult();
    	    if(listcoupon!= null && listcoupon.size()!=0){
    	    	for(Coupon cp:listcoupon){
    	    		if(daysBetween(cp.getOutTime(),nowtime)>=0){
    	    			cp.setCurrentStatus(Short.parseShort("2"));
    	    			System.out.println("**************************更改一条优惠券！"+ cp.getId()+"***************************");
    	    			this.service.updateCoupon(cp.getId(), cp);
    	    		}
    	    	}
    	    }
    	    System.out.println("**************************执行优惠券判断定时任务！***************************");
			return go("validList");
    }
    public static int daysBetween(Date smdate,Date bdate)
	
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        try {
			smdate=sdf.parse(sdf.format(smdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        try {
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }
   
    public void setService(ICouponService service) {
        this.service = service;
    }
	public IBrandService getBrandService() {
		return brandService;
	}
	public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}
	public ICouponService getService() {
		return service;
	}
	public IBrandTypeService getBrandTypeService() {
		return brandTypeService;
	}
	public void setBrandTypeService(IBrandTypeService brandTypeService) {
		this.brandTypeService = brandTypeService;
	}
	public IProductTypeService getProductTypeService() {
		return productTypeService;
	}
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	public ICustomCouponService getCustomCouponService() {
		return customCouponService;
	}
	public void setCustomCouponService(ICustomCouponService customCouponService) {
		this.customCouponService = customCouponService;
	}
	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	public ISingleDispatchCouponRecordService getSingleDispatchCouponRecordService() {
		return singleDispatchCouponRecordService;
	}
	public void setSingleDispatchCouponRecordService(
			ISingleDispatchCouponRecordService singleDispatchCouponRecordService) {
		this.singleDispatchCouponRecordService = singleDispatchCouponRecordService;
	}
    
}