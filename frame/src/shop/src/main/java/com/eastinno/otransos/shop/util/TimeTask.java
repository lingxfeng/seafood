package com.eastinno.otransos.shop.util;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
import com.eastinno.otransos.shop.promotions.service.ICouponService;
import com.eastinno.otransos.shop.spokesman.domain.PaySpecialAllowance;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.service.IPaySpecialAllowanceService;
import com.eastinno.otransos.shop.spokesman.service.IRestitutionService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.shop.spokesman.service.ISubsidyService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
@Component("TimeTask")  
public class TimeTask {
	@Autowired
	private IShopOrderInfoService service;
	@Autowired
	private IRestitutionService restitutionService;
	@Autowired
	private ISubsidyService subsidyService;
	@Autowired
	private ISpokesmanService spokesmanService;
	@Autowired
	private IPaySpecialAllowanceService paySpecialAllowanceService;
	@Autowired
	private ICouponService couponService;
	@Autowired
	private IShopProductService shopProductservice;
	@Autowired
	private IShopSpecService shopSpecservice;
	@Autowired
	private IShopDistributorService shopDistributorService;
	@Autowired
	private IShopMemberService shopMemberService;
	@Scheduled(cron = "0 0 1 * * ?")  
	public void job1() throws IOException {
		Date nowtime = new Date();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status",Integer.parseInt("0"),"=");
        List<ShopOrderInfo> list = this.service.getShopOrderInfoBy(qo).getResult();
        if(list != null && list.size() !=0){
        	for(ShopOrderInfo order:list){
	        	if(daysBetween(order.getCeateDate(),nowtime)>=3){
	        		System.out.println("**************************找到一条！"+ order.getId()+"***************************");
	        		order.setStatus(-1);
	        		this.service.updateShopOrderInfo(order.getId(), order);
	        		List<ShopOrderdetail> list2 = order.getOrderdetails();
	        		for(ShopOrderdetail orderdetail:list2){
	        			ShopProduct shopProduct = orderdetail.getPro();
	        			ShopSpec spec = orderdetail.getShopSpec();
	        			if(spec != null){
	        				int count = orderdetail.getNum();
		        			spec.setInventory(spec.getInventory()+count);
		        			shopProduct.setInventory(shopProduct.getInventory()+count);
		        			this.shopProductservice.updateShopProduct(shopProduct.getId(), shopProduct);
		        			this.shopSpecservice.updateShopSpec(spec.getId(), spec);
	        			}else{
	        				int count = orderdetail.getNum();
		        			shopProduct.setInventory(shopProduct.getInventory()+count);
		        			this.shopProductservice.updateShopProduct(shopProduct.getId(), shopProduct);
		        		}
	        		}
	        	}
	        		
	        }
	   } 
        System.out.println("**************************执行取消订单定时任务！***************************");
        
        Date nowtime2 = new Date();
        QueryObject qoc = new QueryObject();
        qoc.addQuery("obj.status",Integer.parseInt("2"),"=");
        List<ShopOrderInfo> listc = this.service.getShopOrderInfoBy(qoc).getResult();
        if(listc != null && listc.size() !=0){
        	for(ShopOrderInfo order:listc){
	        	if(daysBetween(order.getCeateDate(),nowtime)>=10){
	        		System.out.println("**************************找到一自动确认收货条！"+ order.getId()+"***************************");
	        		order.setStatus(3);
	        		this.service.updateShopOrderInfo(order.getId(), order);
	        	}
	        		
	        }
	   } 
        System.out.println("**************************执行自动收货订单定时任务！***************************");
	        
        /**
         * 订单状态更改结束
         * 进行优惠券判定
         */
        QueryObject qo2 = new QueryObject();
        qo2.addQuery("obj.currentStatus",Short.parseShort("1"), "=");
        List<Coupon> listcoupon = this.couponService.getCouponBy(qo2).getResult();
        if(listcoupon!= null && listcoupon.size()!=0){
        	for(Coupon cp:listcoupon){
        		if(daysBetween(cp.getOutTime(),nowtime)>=0){
        			cp.setCurrentStatus(Short.parseShort("2"));
        			System.out.println("**************************更改一条优惠券！"+ cp.getId()+"***************************");
        			this.couponService.updateCoupon(cp.getId(), cp);
        		}
        	}
        }
        System.out.println("**************************执行优惠券判断定时任务！***************************");
        
        
        /**
         * 更新微店关系图数据到文件
         */
        String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
        s = s.substring(0,s.length()-9) + "/weidian.txt";        
    	System.out.println(s);
    	Map<String, Object> maps = new LinkedHashMap<String,Object>();
    	maps = create();
    	String str = JSONObject.toJSONString(maps);
    	FileUtils fu = new FileUtils();
    	fu.InputAndCover(s, str);
    	
    	
    	/**
         * 更新会员关系图数据到文件
         */
    	String s2=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    	s2 = s2.substring(0,s2.length()-9) + "/huiyuan.txt";     
    	System.out.println(s2);
    	Map<String, Object> maps2 = new LinkedHashMap<String,Object>();
    	maps2 = create2();
    	String str2 = JSONObject.toJSONString(maps2);
    	FileUtils fu2 = new FileUtils();
    	fu2.InputAndCover(s2, str2);
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
	/**
	 * 创建微店json的map
	 * @return
	 */
	  public Map create(){
  		Map<String, Object> mapTop = new LinkedHashMap<String,Object>();
  		mapTop.put("name", "白春达");
  		QueryObject qo = new QueryObject();
  		qo.addQuery("obj.parent is null");
  		qo.addQuery("(obj.status ="+ 1 +"or obj.exStatus = "+ 1 + ")");
  		qo.setPageSize(-1);
  		List<ShopDistributor> list = this.shopDistributorService.getShopDistributorBy(qo).getResult();
  		List<Map> childlist = new ArrayList<Map>();
  		if(list != null && list.size() != 0){
  			for(ShopDistributor sd:list){
  				Map<String, Object> maps = new LinkedHashMap<String,Object>();
  				if(sd.getDisType() == 2){
  					String strings = sd.getMyShopName() + "(体验店)";
  					maps.put("name",strings );
  				}else{
  					maps.put("name", sd.getMyShopName());
  				}
  					
  					digui(maps,sd.getId());
  					childlist.add(maps);
  			}
  		}
  		
  		mapTop.put("children", childlist);
  		return mapTop;
  	}
	  /**
	   * 递归获取微店json
	   * @param maps
	   * @param id
	   */
  	public void digui(Map maps,Long id){
  		QueryObject qo = new QueryObject();
  		qo.addQuery("obj.parent.id",id,"=");
  		qo.setPageSize(-1);
  		List<ShopDistributor> list = this.shopDistributorService.getShopDistributorBy(qo).getResult();
  		List<Map> childlist = new ArrayList<Map>();
  		if(list != null && list.size()!=0){
  			for(ShopDistributor sd:list){
  				Map<String, Object> map = new LinkedHashMap<String,Object>();
  				if(sd.getDisType() == 2){
  					String strings = sd.getMyShopName() + "(体验店)";
  					map.put("name",strings );
  				}else{
  					map.put("name", sd.getMyShopName());
  				}
  				
  				digui(map,sd.getId());
  				childlist.add(map);
  				
  			}
  			
  			maps.put("children",childlist);
  		}
  	}
  	
  	/**
	 * 创建会员json的map
	 * @return
	 */
     public Map create2(){
 		Map<String, Object> mapTop = new LinkedHashMap<String,Object>();
 		mapTop.put("name", "白春达");
 		QueryObject qo = new QueryObject();
 		qo.addQuery("obj.pmember is null");
 		qo.setPageSize(-1);
 		List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo).getResult();
 		List<Map> childlist = new ArrayList<Map>();
 		for(ShopMember sd:list){
 			Map<String, Object> maps = new LinkedHashMap<String,Object>();
 				maps.put("name", sd.getNickname());
 				digui2(maps,sd.getId());
 				childlist.add(maps);
 		}
 		mapTop.put("children", childlist);
 		return mapTop;
 	}
     /**
	   * 递归获取会员json
	   * @param maps
	   * @param id
	   */
 	public void digui2(Map maps,Long id){
 		QueryObject qo = new QueryObject();
 		qo.addQuery("obj.pmember.id",id,"=");
 		qo.setPageSize(-1);
 		List<ShopMember> list = this.shopMemberService.getShopMemberBy(qo).getResult();
 		List<Map> childlist = new ArrayList<Map>();
 		if(list != null && list.size()!=0){
 			for(ShopMember sd:list){
 				Map<String, Object> map = new LinkedHashMap<String,Object>();
 				map.put("name", sd.getNickname());
 				digui2(map,sd.getId());
 				childlist.add(map);
 				
 			}
 			
 			maps.put("children",childlist);
 		}
 	}
	/*@Scheduled(cron = "0 0 * * * ?")  
	public void job2() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
		qo.addQuery("obj.finishRestitution",Short.parseShort("0"), "=");
		qo.addQuery("obj.status = 1 or obj.status = 2 or obj.status = 3");
		qo.setPageSize(-1);
		List<ShopOrderInfo> list = this.service.getShopOrderInfoBy(qo).getResult();
		if(list != null && list.size() != 0){
			for(ShopOrderInfo order:list){
				this.restitutionService.calcuteRestitution(order);
				System.out.println("============返现一次");
			}
		}
		
		//补贴发放操作
		QueryObject qo2 = new QueryObject();
		qo2.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
		qo2.addQuery("obj.finishSubsidy",Short.parseShort("0"), "=");
		qo.addQuery("obj.status = 1 or obj.status = 2 or obj.status = 3");
		qo2.setPageSize(-1);
		List<ShopOrderInfo> list2 = this.service.getShopOrderInfoBy(qo2).getResult();
		if(list != null && list.size() != 0){
			for(ShopOrderInfo order:list2){
				if(order.getIsCalculate() == 1){
					this.subsidyService.disPatchSubsidy(order);
				}else{
					this.subsidyService.calculateSubsidyFirst(order);
					this.subsidyService.disPatchSubsidy(order);
				}
				System.out.println("============补贴一次");
				
			}
		}
		//特殊津贴发放
		int total = this.paySpecialAllowanceService.calculateSpecialAllowance();//总份数 
		Float totalAllowance = 0F;
		QueryObject qo3 = new QueryObject();
		qo3.addQuery("obj.spokesmanRating.rating",Short.parseShort("3"), "=");
		qo3.setPageSize(-1);
		List<Spokesman> list3 = this.spokesmanService.getSpokesmanBy(qo3).getResult();
		if(list3 != null && list3.size() != 0){
			for(Spokesman sman:list3){
				int num = 0; 
				num = this.paySpecialAllowanceService.calculateSpecialAllowancepart(sman);
				if(num != 0){
					PaySpecialAllowance pa = new PaySpecialAllowance();
					pa.setPartNum(num);
					pa.setSpokesman(sman);
					pa.setSpecialAllowance(totalAllowance/total * num);
					this.paySpecialAllowanceService.addPaySpecialAllowance(pa);
				}
			}
		}
	}
*/
	public IShopOrderInfoService getService() {
		return service;
	}

	public void setService(IShopOrderInfoService service) {
		this.service = service;
	}

	public IRestitutionService getRestitutionService() {
		return restitutionService;
	}

	public void setRestitutionService(IRestitutionService restitutionService) {
		this.restitutionService = restitutionService;
	}

	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}

	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}

	public ISpokesmanService getSpokesmanService() {
		return spokesmanService;
	}

	public void setSpokesmanService(ISpokesmanService spokesmanService) {
		this.spokesmanService = spokesmanService;
	}

	public IPaySpecialAllowanceService getPaySpecialAllowanceService() {
		return paySpecialAllowanceService;
	}

	public void setPaySpecialAllowanceService(
			IPaySpecialAllowanceService paySpecialAllowanceService) {
		this.paySpecialAllowanceService = paySpecialAllowanceService;
	}

	public ICouponService getCouponService() {
		return couponService;
	}

	public void setCouponService(ICouponService couponService) {
		this.couponService = couponService;
	}

	public IShopProductService getShopProductservice() {
		return shopProductservice;
	}

	public void setShopProductservice(IShopProductService shopProductservice) {
		this.shopProductservice = shopProductservice;
	}

	public IShopSpecService getShopSpecservice() {
		return shopSpecservice;
	}

	public void setShopSpecservice(IShopSpecService shopSpecservice) {
		this.shopSpecservice = shopSpecservice;
	}

	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}

	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}

	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	
	
}



