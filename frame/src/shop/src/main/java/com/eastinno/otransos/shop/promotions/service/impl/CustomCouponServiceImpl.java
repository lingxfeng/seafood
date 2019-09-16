package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.promotions.dao.ICustomCouponDAO;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;


/**
 * CustomCouponServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CustomCouponServiceImpl implements ICustomCouponService{
	@Resource
	private ICustomCouponDAO customCouponDao;
	
	public void setCustomCouponDao(ICustomCouponDAO customCouponDao){
		this.customCouponDao=customCouponDao;
	}
	
	public Long addCustomCoupon(CustomCoupon customCoupon) {	
		this.customCouponDao.save(customCoupon);
		if (customCoupon != null && customCoupon.getId() != null) {
			return customCoupon.getId();
		}
		return null;
	}
	
	public CustomCoupon getCustomCoupon(Long id) {
		CustomCoupon customCoupon = this.customCouponDao.get(id);
		return customCoupon;
		}
	
	public boolean delCustomCoupon(Long id) {	
			CustomCoupon customCoupon = this.getCustomCoupon(id);
			if (customCoupon != null) {
				this.customCouponDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCustomCoupons(List<Serializable> customCouponIds) {
		
		for (Serializable id : customCouponIds) {
			delCustomCoupon((Long) id);
		}
		return true;
	}
	
	public IPageList getCustomCouponBy(IQueryObject queryObj) {	
		return this.customCouponDao.findBy(queryObj);		
	}
	
	public boolean updateCustomCoupon(Long id, CustomCoupon customCoupon) {
		if (id != null) {
			customCoupon.setId(id);
		} else {
			return false;
		}
		this.customCouponDao.update(customCoupon);
		return true;
	}
	/**
	  * 判断失效优惠券
	  * @param id 用户会员id 
	  */
	public void disableCustomCoupon(ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",Short.parseShort("0"), "=");
		qo.addQuery("obj.shopMember.id",member.getId(),"=");
		qo.addQuery("obj.coupon.useType",Short.parseShort("1"), "=");
		qo.setPageSize(-1);
		List<CustomCoupon> list = this.customCouponDao.findBy(qo).getResult();
		if(list!=null){
			for(CustomCoupon cc:list){
				cc.setStatus(Short.parseShort("3"));
				this.customCouponDao.update(cc);
			}
		}
	}
	/**
	  * 判断是否有可用优惠券
	  * @param id 用户会员id 
	  */
	public List<CustomCoupon> judgeCustomCoupon(ShopMember member,ShopOrderInfo order){
		List<CustomCoupon> validlist = new ArrayList<CustomCoupon>();
		QueryObject qo = new QueryObject();
    	qo.addQuery("obj.shopMember.id",member.getId(), "=");
    	qo.addQuery("obj.status",Short.parseShort("0"), "=");
    	qo.addQuery("obj.coupon.currentStatus",Short.parseShort("1"),"=");
    	if(member.getDisType()!= 0){
    		qo.addQuery("obj.coupon.useType",Short.parseShort("0"), "=");
    	}
    	qo.setPageSize(-1);
    	List<CustomCoupon> cclist = this.customCouponDao.findBy(qo).getResult();
    	if(cclist!=null && cclist.size()!=0){
    		for(CustomCoupon cc:cclist){
        		if(order.getProduct_price()>=cc.getCoupon().getUsecondition()){
        			if(cc.getCoupon().getType() == 0){
            			validlist.add(cc);
        			}else if(cc.getCoupon().getType() == 1){
        				List<Brand> brandlist = cc.getCoupon().getBrandlist();
    					List<ShopOrderdetail> list = order.getOrderdetails();
    					/**
    					 * 只要没有，就不可用的方法
    					 */
//    					List<Brand> branlist = new ArrayList<Brand>();
//    					for(ShopOrderdetail orderdetail:list){
//    						Brand bd = orderdetail.getPro().getBrand();
//    						if(!branlist.contains(bd)){
//    							branlist.add(bd);
//    						}
//    					}
//    					if(brandlist.containsAll(branlist)){
//    						validlist.add(cc);
//    					}
    					/**
    					 * 只要有，就可用的方法
    					 */
    					if(brandlist!=null){
    						for(ShopOrderdetail orderdetail:list){
        			    		ShopProduct product = orderdetail.getPro();
        			    		Brand proBrand = product.getBrand();
        			    		if(brandlist.indexOf(proBrand) != -1){
        			    			if(validlist!=null){
        			    				if(validlist.indexOf(proBrand) == -1){
        			    					validlist.add(cc);
        			    				}
        			    			}else{
        			    				validlist.add(cc);
        			    			}
        			    			
        			    		}
        			    	}
    					}	
        			}else if(cc.getCoupon().getType() == 2){
        				List<ShopProduct> prolist = cc.getCoupon().getProductlist();
    					List<ShopOrderdetail> list = order.getOrderdetails();
    					/**
    					 * 只要没有，就不可用的方法
    					 * 
    					 */
    					List<ShopProduct> productlist = new ArrayList<ShopProduct>();
//    					for(ShopOrderdetail orderdetail:list){
//    						productlist.add(orderdetail.getPro());
//    					}
//    					if(prolist.containsAll(productlist)){
//    						validlist.add(cc);
//    					}
    					/**
    					 * 只要有，就可用的方法
    					 */	
    					if(prolist != null){
    						for(ShopOrderdetail orderdetail:list){
        			    		ShopProduct pro = orderdetail.getPro();
        			    		if(prolist.indexOf(pro)!= -1){
        			    			if(validlist != null){
        			    				if(validlist.indexOf(pro) == -1){
        			    					validlist.add(cc);
        			    				}
        			    			}else{
        			    				validlist.add(cc);
        			    			}
        			    			
        			    		}
        			    	}
    					}	
        			}
        		}
    		}
    	
    	}
    	return validlist;	
    }
	
}
