package com.eastinno.otransos.shop.droduct.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.shop.droduct.dao.IDeliveryRuleDAO;
import com.eastinno.otransos.shop.droduct.dao.IRemoteRegionDAO;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.DeliveryRule;
import com.eastinno.otransos.shop.droduct.domain.RemoteRegion;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IRemoteRegionService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

@Service
public class DeliveryRuleServiceImpl implements IDeliveryRuleService{
	@Resource
	private IDeliveryRuleDAO deliveryRuleDAO;
	@Autowired
	private IRemoteRegionService remoteService;
	@Autowired
	private IShopOrderInfoService orderService;
	@Autowired
	private IShopOrderdetailService shopOrderdetailService;
	
	public IDeliveryRuleDAO getDeliveryRuleDAO() {
		return deliveryRuleDAO;
	}

	public void setDeliveryRuleDAO(IDeliveryRuleDAO deliveryRuleDAO) {
		this.deliveryRuleDAO = deliveryRuleDAO;
	}

	/**
	 * 依据主键id获取偏远地区记录
	 * @param id
	 * @return
	 */
	public DeliveryRule getDeliveryRule(Long id){
		return this.deliveryRuleDAO.get(id);
	}
	
	/**
	 * 添加一条偏远地区记录
	 * @param deliveryRule
	 * @return
	 */
	public DeliveryRule addDeliveryRule(DeliveryRule deliveryRule){
		return this.deliveryRuleDAO.save(deliveryRule);		
	}
	
	/**
	 * 删除一条偏远地区记录
	 * @param id
	 * @return
	 */
	public boolean deldeliveryRule(Long id){
		try{
			this.deliveryRuleDAO.delete(id);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean delDeliveryRule(Long id) {		
		try{
			this.deliveryRuleDAO.delete(id);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean updateDeliveryRule(Long id, DeliveryRule deliveryRule) {
		try{
			this.deliveryRuleDAO.update(deliveryRule);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean setNormalPostCost(ShopProduct shopProduct, Double cost) {
		return this.setCost(shopProduct.getId(), 4, "single", cost);	
	}
	
	@Override
	public boolean setDefaultNormalPostCost(Double cost) {
		return this.setCost(0L, 4, "single", cost);
	}

	@Override
	public boolean setRemoteRegionPostCost(ShopProduct shopProduct, Double cost) {
		return this.setCost(shopProduct.getId(), 3, "single", cost);
	}

	@Override
	public boolean setDefaultRemoteRegionPostCost(Double cost) {
		return this.setCost(0L, 3, "single", cost);
	}
	
	@Override
	public boolean setNormalFreepostStart(ShopProduct shopProduct, Double cost) {
		return this.setCost(shopProduct.getId(), 1, "single", cost);
	}
	
	@Override
	public boolean setDefaultNormalFreepostStart(Double cost) {
		return this.setCost(0L, 1, "single", cost);
	}
	
	@Override
	public boolean setOrderFreepostStart(Double cost) {
		return this.setCost(0L, 2, "single", cost);
	}
	
	private boolean setCost(Long productId, int type, String key, Double cost){
		//判断当前表中是否存在记录
		DeliveryRule currRule = this.getSingleDeliveryRule(productId, type);
		if(currRule == null){
			DeliveryRule tempRule = new DeliveryRule();
			tempRule.setType(type);
			tempRule.setCreateDate(new Date());
			tempRule.setModifyDate(new Date());
			tempRule.setProductId(productId);
			tempRule.setRuleValue(cost.toString());
			tempRule.setRuleKey(key);
			currRule = this.addDeliveryRule(tempRule);
		}else{
			currRule.setModifyDate(new Date());
			currRule.setRuleValue(cost.toString());
			return this.updateDeliveryRule(currRule.getId(), currRule);
		}
		return true;
	}
			
	@Override
	public DeliveryRule getNormalPostCost(ShopProduct shopProduct) {
		return this.getSingleDeliveryRule(shopProduct.getId(), 4);
	}

	@Override
	public DeliveryRule getDefaultNormalPostCost() {
		DeliveryRule tempRule = this.getSingleDeliveryRule(0L, 4);
		if(tempRule == null){
			this.setCost(0L, 4, "single", 15D);
			tempRule = this.getSingleDeliveryRule(0L, 4);
		}
		return tempRule;
	}

	@Override
	public DeliveryRule getRemoteRegionPostCost(ShopProduct shopProduct) {
		return this.getSingleDeliveryRule(shopProduct.getId(), 3);
	}

	@Override
	public DeliveryRule getDefaultRemoteRegionPostCost() {
		DeliveryRule tempRule = this.getSingleDeliveryRule(0L, 3);
		if(tempRule == null){
			this.setCost(0L, 3, "single", 15D);
			tempRule =  this.getSingleDeliveryRule(0L, 3);
		}
		return tempRule;
	}

	@Override
	public DeliveryRule getOrderFreepostStart() {
		DeliveryRule tempRule = this.getSingleDeliveryRule(0L, 2);
		if(tempRule == null){
			this.setCost(0L, 2, "single", 100D);
			tempRule = this.getSingleDeliveryRule(0L, 2); 
		}
		return tempRule;
	}

	@Override
	public DeliveryRule getNormalFreepostStart(ShopProduct shopProduct) {
		return this.getSingleDeliveryRule(shopProduct.getId(), 1);
	}

	@Override
	public DeliveryRule getDefaultNormalFreepostStart() {
		DeliveryRule tempRule = this.getSingleDeliveryRule(0L, 1);
		if(tempRule == null){
			this.setCost(0L, 1, "single", 100D);
			tempRule = this.getSingleDeliveryRule(0L, 1); 
		}
		return tempRule;
	}
	
	/**
	 * 依据shopproduct id获取商品对应的包邮规则，没有则返回null
	 * @param product
	 * @return
	 */
	private DeliveryRule getSingleDeliveryRule(Long productId, int type){
		QueryObject qo = new QueryObject();		
		qo.addQuery("obj.type", type, "=");
		qo.addQuery("obj.ruleKey", "single", "=");
		qo.addQuery("obj.productId", productId, "=");
		List<DeliveryRule> listResult = this.deliveryRuleDAO.findBy(qo).getResult();
		if(listResult!=null && listResult.size()==1){
			return listResult.get(0);
		}
		return null;
	}
	
	/**
	 * 依据订单信息获取快递费用
	 * @param orderInfo
	 * @return
	 */
	public Double getDeliveryCost(ShopOrderInfo orderInfo){
		Double result = 0D;
		
		//是否包邮判定
		/*DeliveryRule tempRule = this.getOrderFreepostStart();
		if(orderInfo.getGross_price().compareTo(Double.parseDouble(tempRule.getRuleValue())) > 0){
			return result;
		}*/
		
		//依据brand对detail分组
		Map<Long, List<ShopOrderdetail>> detailBrand = this.getOrderDetailsByBrand(orderInfo);
		Set keys = detailBrand.keySet();
		Iterator iter = keys.iterator();
		
		//是否是偏远地区
		if(this.remoteService.isRemoteRegion(orderInfo.getAddr().getArea())){
			while(iter.hasNext()){
				result += this.getRemoteDeliveryCostByOrderDetails(detailBrand.get(iter.next()));	
			}			
		}else{
			while(iter.hasNext()){
				result += this.getNoneRemoteByOrderDetails(detailBrand.get(iter.next()));
			}
		}
		return result;
	}
	
	/**
	 * 依据订单信息获取快递费用
	 * @param orderInfo
	 * @return
	 */
	public Map<Long, Double> getDeliveryCostMap(ShopOrderInfo orderInfo){
		Map<Long, Double> costMap = new HashMap();
		
		//是否包邮判定
		/*DeliveryRule tempRule = this.getOrderFreepostStart();
		if(orderInfo.getGross_price().compareTo(Double.parseDouble(tempRule.getRuleValue())) > 0){
			return result;
		}*/
		
		//依据brand对detail分组
		Map<Long, List<ShopOrderdetail>> detailBrand = this.getOrderDetailsByBrand(orderInfo);
		Set keys = detailBrand.keySet();
		Iterator iter = keys.iterator();
		
		//是否是偏远地区
		if(this.remoteService.isRemoteRegion(orderInfo.getAddr().getArea())){
			while(iter.hasNext()){
				Long brandId = (Long)iter.next();
				costMap.put(brandId, this.getRemoteDeliveryCostByOrderDetails(detailBrand.get(brandId)));	
			}			
		}else{
			while(iter.hasNext()){
				Long brandId = (Long)iter.next();
				costMap.put(brandId, this.getNoneRemoteByOrderDetails(detailBrand.get(brandId)));
			}
		}
		
		return costMap;
	}
	
	/**
	 * 依据品牌对商品订单对应的内容进行分类
	 * @param order
	 * @return
	 */
	private Map<Long, List<ShopOrderdetail>> getOrderDetailsByBrand(ShopOrderInfo order){
		Map<Long, List<ShopOrderdetail>> result = new HashMap();
		List<ShopOrderdetail> details = order.getOrderdetails();
		int detailNum = details.size();
		for(int i=0; i<detailNum; ++i){
			Long detailId = 0L;
			Brand brand = details.get(i).getPro().getBrand();
			if(brand != null){
				detailId = brand.getId();
			}
			if(result.get(detailId) == null){
				result.put(detailId, new ArrayList());
			}
			result.get(detailId).add(details.get(i));
		}
		return result;
	}
	
	/**
	 * 依据订单详情，计算偏远地区的运费
	 * @param details
	 * @return
	 */
	private Double getRemoteDeliveryCostByOrderDetails(List<ShopOrderdetail> details){
		Double cost = 0D;
		List<DeliveryRule> ruleList = new ArrayList();
		//依据detail，获取规则数组
		int orderNum = details.size();
		if(details!=null){
			for(int i=0; i<orderNum; ++i){
				DeliveryRule tempRule = this.getRemoteRegionPostCost(details.get(i).getPro());
				if(tempRule != null){
					ruleList.add(tempRule);
				}
			}
		}
		//依据规则数组，计算价格
		if(ruleList.size() > 0){			
			for(int i=0; i<ruleList.size(); ++i){
				Double tempCost = Double.parseDouble(ruleList.get(i).getRuleValue());
				if(tempCost.compareTo(cost) > 0){
					cost = tempCost;
				}
			}
		}else{
			DeliveryRule tempRule = this.getDefaultRemoteRegionPostCost();
			cost = Double.valueOf(tempRule.getRuleValue());
		}
		return cost;
	}
	
	/**
	 * 依据商品详细信息获取非偏远地区的运费
	 * @param details
	 * @return
	 */
	private Double getNoneRemoteByOrderDetails(List<ShopOrderdetail> details){
		Double cost = 0D;
		int detailNum = details.size();
		//依据details获取不包邮商品中的运费最大值
		if(details != null){
			Double countAmt = 0D;
			
			//将shopOrderdetail按商品分类，并存入map中
			Map<Long, List<ShopOrderdetail>> orderMaps = new HashMap<Long, List<ShopOrderdetail>>();			
			for(int i=0; i<detailNum; ++i){
				ShopOrderdetail tempDetail = details.get(i);
				if(!orderMaps.containsKey(tempDetail.getPro().getId())){
					orderMaps.put(tempDetail.getPro().getId(), new ArrayList<ShopOrderdetail>());
				}
				orderMaps.get(tempDetail.getPro().getId()).add(tempDetail);
			}
			
			//依据订单详情map计算各个商品对应的订单
			Set mapSet = orderMaps.keySet();
			Iterator iter = mapSet.iterator();
			while(iter.hasNext()){
				List<ShopOrderdetail> detailList = orderMaps.get(iter.next());
				//当前商品有对应的下单信息
				if(detailList.size() == 0){
					continue;
				}
				
				for(int i=0; i<detailList.size(); ++i){
					if(detailList.get(i).getShopSpec() != null){
						countAmt += detailList.get(i).getShopSpec().getAmt()*detailList.get(i).getNum();	
					}else{
						countAmt += detailList.get(i).getPro().getAmt()*detailList.get(i).getNum();	
					}					
				}
				//获取设置的对应于该商品的规则
				DeliveryRule tempRule = this.getNormalFreepostStart(detailList.get(0).getPro());
				if(tempRule == null){
					tempRule = this.getDefaultNormalFreepostStart();
				}
				Double totalAmt = 0D;
				for(int i=0; i<detailList.size(); ++i){
					if(detailList.get(i).getShopSpec() != null){
						totalAmt += detailList.get(i).getShopSpec().getAmt()*detailList.get(i).getNum();	
					}else{
						totalAmt += detailList.get(i).getPro().getAmt()*detailList.get(i).getNum();	
					}
				}
				//如果该商品的总价小于包邮的总价显示，则将该邮费设置的邮费值计入考量范围，在所有不包邮的商品中取邮费最大值
				if(totalAmt.compareTo(Double.parseDouble(tempRule.getRuleValue())) < 0){
					DeliveryRule tempCostRule = this.getNormalPostCost(detailList.get(0).getPro());
					if(tempCostRule == null){
						tempCostRule = this.getDefaultNormalPostCost();
					}
					Double tempCost = Double.parseDouble(tempCostRule.getRuleValue());
					if(cost.compareTo(tempCost) < 0){
						cost = tempCost;
					}
				}
			
			}
			
			//品牌满足设定的数额，邮费设置为0
			DeliveryRule brandPost = this.getOrderFreepostStart();
			if(countAmt.compareTo(Double.parseDouble(brandPost.getRuleValue())) >= 0){
				cost = 0D;
			}
		}
		return cost;
	}

	@Override
	public Double getDeliveryByOrderAndBrandId(Long orderId, Long brandId) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.orderInfo.id", orderId, "=");
		qo.addQuery("obj.pro.brand.id", brandId, "=");
		List<ShopOrderdetail> shopOrderdetailsList=this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
		double result=0;
		//是否是偏远地区
		ShopOrderInfo shopOrderInfo=this.orderService.getShopOrderInfo(orderId);
		if(this.remoteService.isRemoteRegion(shopOrderInfo.getAddr().getArea())){
			result +=this.getRemoteDeliveryCostByOrderDetails(shopOrderdetailsList);	
		}else{
			result += this.getNoneRemoteByOrderDetails(shopOrderdetailsList);
		}
		return result;
	}
}
