package com.eastinno.otransos.seafood.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;

@Component
public class EntityToolUtil {
	
	@Autowired
	private IShopOrderInfoService shopOrderInfoService;
	
	@Autowired
	private IShopOrderdetailService shopOrderdetailService;
	
	@Autowired
	private IBrandService brandService;
	
	@Autowired
	private IDeliveryRuleService deliveryRuleService;
	
	public Double getPriceByBrand(Long orderId,Long brandId){
		ShopOrderInfo shopOrderInfo=this.shopOrderInfoService.getShopOrderInfo(orderId);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.orderInfo.id", orderId, "=");
		qo.addQuery("obj.pro.brand.id", brandId, "=");
		List<ShopOrderdetail> list=this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
		double price=0L;
		for (ShopOrderdetail shopOrderdetail : list) {
			price += (shopOrderdetail.getUnit_price()*shopOrderdetail.getNum());
		}
		return price;
	}
	
	public Double getFreightPriceByBrand(Long orderId,Long brandId){
		Double freight=this.deliveryRuleService.getDeliveryByOrderAndBrandId(orderId, brandId);
		return freight;
	}
	
	public Boolean getDistanceDays(Date createTime){
		Date nowTime = new Date();
		Long intervalMilli = nowTime.getTime()-createTime.getTime();
		if((intervalMilli/(24 * 60 * 60 * 1000))>7){
			return false;
		}
		return true;
	}
}
