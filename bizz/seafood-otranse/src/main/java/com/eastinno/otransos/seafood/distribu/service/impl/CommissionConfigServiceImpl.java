package com.eastinno.otransos.seafood.distribu.service.impl;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.distribu.domain.CommissionConfig;
import com.eastinno.otransos.seafood.distribu.service.ICommissionConfigService;
import com.eastinno.otransos.seafood.distribu.dao.ICommissionConfigDAO;
import com.eastinno.otransos.seafood.droduct.dao.IShopProductDAO;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;


/**
 * CommissionConfigServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CommissionConfigServiceImpl implements ICommissionConfigService{
	@Resource
	private ICommissionConfigDAO commissionConfigDao;
	@Resource
	private IShopProductDAO shopProductDao;
	public void setCommissionConfigDao(ICommissionConfigDAO commissionConfigDao){
		this.commissionConfigDao=commissionConfigDao;
	}
	
	public Long addCommissionConfig(CommissionConfig commissionConfig) {	
		this.commissionConfigDao.save(commissionConfig);
		if (commissionConfig != null && commissionConfig.getId() != null) {
			return commissionConfig.getId();
		}
		return null;
	}
	
	public CommissionConfig getCommissionConfig(Long id) {
		CommissionConfig commissionConfig = this.commissionConfigDao.get(id);
		return commissionConfig;
		}
	
	public boolean delCommissionConfig(Long id) {	
			CommissionConfig commissionConfig = this.getCommissionConfig(id);
			if (commissionConfig != null) {
				this.commissionConfigDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCommissionConfigs(List<Serializable> commissionConfigIds) {
		
		for (Serializable id : commissionConfigIds) {
			delCommissionConfig((Long) id);
		}
		return true;
	}
	
	public IPageList getCommissionConfigBy(IQueryObject queryObj) {	
		return this.commissionConfigDao.findBy(queryObj);		
	}
	
	public boolean updateCommissionConfig(Long id, CommissionConfig commissionConfig) {
		if (id != null) {
			commissionConfig.setId(id);
		} else {
			return false;
		}
		this.commissionConfigDao.update(commissionConfig);
		return true;
	}
	public Double getCommissionConfigByProductId(ShopProduct product,int type){
		CommissionConfig cc = null;
		Double leve1=0.0,leve2=0.0;
		if(product!=null){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.product", product, "=");
			IPageList pl = this.commissionConfigDao.findBy(qo);
			if(pl.getResult()!=null){
				cc = (CommissionConfig) pl.getResult().get(0);
				
			}else{
				QueryObject qo2 = new QueryObject();
				qo.addQuery("obj.isdefault", true, "=");
				IPageList pl2 = this.commissionConfigDao.findBy(qo2);
				if(pl2.getResult()!=null){
				cc = (CommissionConfig) pl2.getResult().get(0);
				}
			}
			Float config1 = cc.getLeve1();
			Float config2 = cc.getLeve2();
			Short commitype = cc.getType();
			Float totalcomission = cc.getTotalcomission();
			
			if(commitype==0){
				BigDecimal bd = new BigDecimal(totalcomission * (config1/100));
				leve1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				BigDecimal bd2 = new BigDecimal(totalcomission * (config2/100));
				leve2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}else{
				//leve1 = (double)config1;
				//leve2 = (double)config2;
				leve1 = Double.parseDouble(String.valueOf(config1));
				leve2 = Double.parseDouble(String.valueOf(config2));
			}
		}
		if(type == 1){
			return leve1;
		}else{
			return leve2;
		}
		
	}
	@Override
	public List<ShopProduct> querymethod(String sql) {
		//ShopProduct product = new ShopProduct();
		List<ShopProduct> result = (List<ShopProduct>) this.shopProductDao.executeNativeQuery(sql, null, 0, 0,ShopProduct.class);
		return result;
	}	
	
}
