package com.eastinno.otransos.seafood.core.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.core.dao.IShopSystemConfigDAO;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ShopSystemConfigServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopSystemConfigServiceImpl implements IShopSystemConfigService{
	@Resource
	private IShopSystemConfigDAO shopSystemConfigDao;
	
	public void setShopSystemConfigDao(IShopSystemConfigDAO shopSystemConfigDao){
		this.shopSystemConfigDao=shopSystemConfigDao;
	}
	
	public Long addShopSystemConfig(ShopSystemConfig shopSystemConfig) {
		shopSystemConfig.setTenant(ShiroUtils.getTenant());
		this.shopSystemConfigDao.save(shopSystemConfig);
		if (shopSystemConfig != null && shopSystemConfig.getId() != null) {
			return shopSystemConfig.getId();
		}
		return null;
	}
	
	public ShopSystemConfig getShopSystemConfig(Long id) {
		ShopSystemConfig shopSystemConfig = this.shopSystemConfigDao.get(id);
		return shopSystemConfig;
		}
	
	public boolean delShopSystemConfig(Long id) {	
			ShopSystemConfig shopSystemConfig = this.getShopSystemConfig(id);
			if (shopSystemConfig != null) {
				this.shopSystemConfigDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopSystemConfigs(List<Serializable> shopSystemConfigIds) {
		
		for (Serializable id : shopSystemConfigIds) {
			delShopSystemConfig((Long) id);
		}
		return true;
	}
	
	public IPageList getShopSystemConfigBy(IQueryObject queryObj) {	
		return this.shopSystemConfigDao.findBy(queryObj);		
	}
	
	public boolean updateShopSystemConfig(Long id, ShopSystemConfig shopSystemConfig) {
		if (id != null) {
			shopSystemConfig.setId(id);
		} else {
			return false;
		}
		this.shopSystemConfigDao.update(shopSystemConfig);
		return true;
	}

	@Override
	public ShopSystemConfig getSystemConfig() {
		QueryObject qo = new QueryObject();
		List<ShopSystemConfig> list = this.getShopSystemConfigBy(qo).getResult();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}	
	
}
