package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.dao.IShopProductDAO;
import com.eastinno.otransos.seafood.droduct.dao.IShopSpecDAO;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.trade.dao.IShopOrderdetailDAO;


/**
 * ShopOrderdetailServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopOrderdetailServiceImpl implements IShopOrderdetailService{
	@Resource
	private IShopOrderdetailDAO shopOrderdetailDao;
	
	@Resource
	private IShopSpecDAO shopSpecDAO;
	
	@Resource
	private IShopProductDAO shopProductDAO;
	
	public void setShopProductDAO(IShopProductDAO shopProductDAO) {
		this.shopProductDAO = shopProductDAO;
	}

	public void setShopSpecDAO(IShopSpecDAO shopSpecDAO) {
		this.shopSpecDAO = shopSpecDAO;
	}

	public void setShopOrderdetailDao(IShopOrderdetailDAO shopOrderdetailDao){
		this.shopOrderdetailDao=shopOrderdetailDao;
	}
	
	public Long addShopOrderdetail(ShopOrderdetail shopOrderdetail) {	
		this.shopOrderdetailDao.save(shopOrderdetail);
		if (shopOrderdetail != null && shopOrderdetail.getId() != null) {
			return shopOrderdetail.getId();
		}
		return null;
	}
	
	public ShopOrderdetail getShopOrderdetail(Long id) {
		ShopOrderdetail shopOrderdetail = this.shopOrderdetailDao.get(id);
		return shopOrderdetail;
		}
	
	public boolean delShopOrderdetail(Long id) {	
			ShopOrderdetail shopOrderdetail = this.getShopOrderdetail(id);
			if (shopOrderdetail != null) {
				this.shopOrderdetailDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopOrderdetails(List<Serializable> shopOrderdetailIds) {
		
		for (Serializable id : shopOrderdetailIds) {
			delShopOrderdetail((Long) id);
		}
		return true;
	}
	
	public IPageList getShopOrderdetailBy(IQueryObject queryObj) {	
		return this.shopOrderdetailDao.findBy(queryObj);		
	}
	
	public boolean updateShopOrderdetail(Long id, ShopOrderdetail shopOrderdetail) {
		if (id != null) {
			shopOrderdetail.setId(id);
		} else {
			return false;
		}
		this.shopOrderdetailDao.update(shopOrderdetail);
		return true;
	}

	@Override
	public ShopOrderdetail getShopOrderdetailByName(String name, String value) {
		return this.shopOrderdetailDao.getBy(name, value);
	}

	@Override
	public boolean cancelOrder(List<ShopOrderdetail> orderDetails) {
		for (ShopOrderdetail shopOrderdetail : orderDetails) {
			System.out.println("=======orderdetailservice  canceloRDER  执行了=====");
			if(shopOrderdetail.getShopSpec()!=null){
				ShopSpec shopSpec = shopOrderdetail.getShopSpec();
					shopSpec.setInventory(shopSpec.getInventory()+shopOrderdetail.getNum());
					this.shopSpecDAO.update(shopSpec);
			}else{
				ShopProduct shopProduct=shopOrderdetail.getPro();
				shopProduct.setInventory(shopProduct.getInventory()+shopOrderdetail.getNum());
				this.shopProductDAO.update(shopProduct);
			}
		}
		return true;
	}
	@Override
	public List<ShopOrderdetail> getOrderDetailByMoth(Date first,Date end){
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.orderInfo.tradeDate",end,"<=");
		qo.addQuery("obj.orderInfo.tradeDate",first,">=");
		qo.addQuery("obj.orderInfo.status in (1,2,3,4,6)");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderdetail> list = this.shopOrderdetailDao.findBy(qo).getResult();
		return list;
	}
	@Override
	public List<ShopOrderdetail> getOrderDetailAll(){
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.orderInfo.status in (1,2,3,4,6)");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderdetail> list = this.shopOrderdetailDao.findBy(qo).getResult();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopOrderdetail> getOrderDetailByMothPro(Date first,Date end,Long proId){
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.orderInfo.tradeDate",end,"<=");
		qo.addQuery("obj.orderInfo.tradeDate",first,">=");
		qo.addQuery("obj.orderInfo.status in (1,2,3,4,6)");
		qo.addQuery("obj.pro.id",proId,"=");
		List<ShopOrderdetail> list = this.shopOrderdetailDao.findBy(qo).getResult();
		return list;
		
	}
	@Override
	public List<ShopOrderdetail> getOrderDetailAllPro(Long proId){
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.orderInfo.status in (1,2,3,4,6)");
		qo.addQuery("obj.pro.id",proId,"=");
		List<ShopOrderdetail> list = this.shopOrderdetailDao.findBy(qo).getResult();
		return list;
		
	}
}
