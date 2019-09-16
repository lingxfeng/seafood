package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.droduct.dao.IShopSpecDAO;


/**
 * ShopSpecServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopSpecServiceImpl implements IShopSpecService{
	@Resource
	private IShopSpecDAO shopSpecDao;
	
	public void setShopSpecDao(IShopSpecDAO shopSpecDao){
		this.shopSpecDao=shopSpecDao;
	}
	
	public Long addShopSpec(ShopSpec shopSpec) {	
		this.shopSpecDao.save(shopSpec);
		if (shopSpec != null && shopSpec.getId() != null) {
			return shopSpec.getId();
		}
		return null;
	}
	
	public ShopSpec getShopSpec(Long id) {
		ShopSpec shopSpec = this.shopSpecDao.get(id);
		return shopSpec;
		}
	
	public boolean delShopSpec(Long id) {	
			ShopSpec shopSpec = this.getShopSpec(id);
			if (shopSpec != null) {
				this.shopSpecDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopSpecs(List<Serializable> shopSpecIds) {
		
		for (Serializable id : shopSpecIds) {
			delShopSpec((Long) id);
		}
		return true;
	}
	
	public IPageList getShopSpecBy(IQueryObject queryObj) {	
		return this.shopSpecDao.findBy(queryObj);		
	}
	
	public boolean updateShopSpec(Long id, ShopSpec shopSpec) {
		if (id != null) {
			shopSpec.setId(id);
		} else {
			return false;
		}
		this.shopSpecDao.update(shopSpec);
		return true;
	}	
	
	public Map<String, List<String>> getSpecGroupByProduct(ShopProduct product){
		Map<String, List<String>> result = new HashMap();
		
		//依据商品规格，获取商品的属性集合
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.product.id", product.getId(), "=");
		IPageList pageList = this.shopSpecDao.findBy(qo);		
		List<ShopSpec> shopSpecList = pageList.getResult();
		if(shopSpecList == null){
			return result;
		}
		int shopSpecLen = shopSpecList.size();
		String valuesStr = ""; 
		for(int i=0; i<shopSpecLen; ++i){
			String[] tempValuesStr = shopSpecList.get(i).getName().split(",");
			for(int j=0; j<tempValuesStr.length; ++j){
				String tempStr = ","+valuesStr+",";
				if(!tempStr.contains(","+tempValuesStr[j]+",")){					
					valuesStr = valuesStr+tempValuesStr[j]+",";
				}
			}			
		}
		valuesStr = valuesStr.substring(0, valuesStr.length()-1);
		String[] valuesArray = valuesStr.split(",");
		
		//依据商品类型，获取商品的规格分类集合
		List<AttributeKey> attrKeys = product.getProductType().getAllAttrs("3");
		int keyLen = attrKeys.size();
		//初始化result结果集
		for(int i=0; i<keyLen; ++i){
			result.put(attrKeys.get(i).getName(), new ArrayList());
		}
		List temp;
		for(int i=0; i<valuesArray.length; ++i){
			for(int j=0; j<keyLen; ++j){
				String tempStr = ","+attrKeys.get(j).getValue()+",";
				if(tempStr.contains(","+valuesArray[i]+",")){
					temp = result.get(attrKeys.get(j).getName());
					temp.add(valuesArray[i]);
					break;
				}
			}
		}
		
		return result;
	}

	@Override
	public String getSpecJsonByProduct(ShopProduct product) {
		String result = "";		
		//依据商品规格，获取商品的属性集合
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.product.id", product.getId(), "=");
		IPageList pageList = this.shopSpecDao.findBy(qo);
		//将查询的结果封装到json格式字符串中
		List<ShopSpec> shopSpecList = pageList.getResult();
		if(shopSpecList == null){
			result = "[]";
			return result;
		}
		int listSize = shopSpecList.size();
		List<Map<String, String>> tempList = new ArrayList();
		ShopSpec tempSpec = null;
		Map<String, String> tempMap;
		for(int i=0; i<listSize; ++i){
			tempSpec = shopSpecList.get(i);
			tempMap = new HashMap();
			tempMap.put("id", tempSpec.getId().toString());
			tempMap.put("name", tempSpec.getName().toString());
			tempMap.put("amt", tempSpec.getAmt().toString());
			tempMap.put("costAmt", tempSpec.getCostAmt().toString());
			tempMap.put("store_price", tempSpec.getStore_price().toString());
			tempMap.put("tydAmt", tempSpec.getTydAmt().toString());
			tempMap.put("inventory", tempSpec.getInventory().toString());
			tempList.add(tempMap);
		}
		result = JSONObject.toJSONString(tempList);
		return result;
	}	
}
