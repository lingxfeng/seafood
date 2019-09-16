package com.eastinno.otransos.shop.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.spokesman.domain.PaySpecialAllowance;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.service.IPaySpecialAllowanceService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.shop.spokesman.dao.IPaySpecialAllowanceDAO;


/**
 * PaySpecialAllowanceServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class PaySpecialAllowanceServiceImpl implements IPaySpecialAllowanceService{
	@Resource
	private IPaySpecialAllowanceDAO paySpecialAllowanceDao;
	@Autowired
	private ISpokesmanService spokesmanService;
	public void setPaySpecialAllowanceDao(IPaySpecialAllowanceDAO paySpecialAllowanceDao){
		this.paySpecialAllowanceDao=paySpecialAllowanceDao;
	}
	
	public Long addPaySpecialAllowance(PaySpecialAllowance paySpecialAllowance) {	
		this.paySpecialAllowanceDao.save(paySpecialAllowance);
		if (paySpecialAllowance != null && paySpecialAllowance.getId() != null) {
			return paySpecialAllowance.getId();
		}
		return null;
	}
	
	public PaySpecialAllowance getPaySpecialAllowance(Long id) {
		PaySpecialAllowance paySpecialAllowance = this.paySpecialAllowanceDao.get(id);
		return paySpecialAllowance;
		}
	
	public boolean delPaySpecialAllowance(Long id) {	
			PaySpecialAllowance paySpecialAllowance = this.getPaySpecialAllowance(id);
			if (paySpecialAllowance != null) {
				this.paySpecialAllowanceDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelPaySpecialAllowances(List<Serializable> paySpecialAllowanceIds) {
		
		for (Serializable id : paySpecialAllowanceIds) {
			delPaySpecialAllowance((Long) id);
		}
		return true;
	}
	
	public IPageList getPaySpecialAllowanceBy(IQueryObject queryObj) {	
		return this.paySpecialAllowanceDao.findBy(queryObj);		
	}
	
	public boolean updatePaySpecialAllowance(Long id, PaySpecialAllowance paySpecialAllowance) {
		if (id != null) {
			paySpecialAllowance.setId(id);
		} else {
			return false;
		}
		this.paySpecialAllowanceDao.update(paySpecialAllowance);
		return true;
	}
	/**
	 * 计算总份数
	 */
	public int calculateSpecialAllowance(){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.spokesmanRating.rating",Short.parseShort("3"), "=");
		qo.addQuery("obj.pspokesman",null, "!=");
		qo.setPageSize(-1);
		List<?> list = this.spokesmanService.getSpokesmanBy(qo).getResult();
		int num = 0;
		if(list != null){
			num = list.size();
		}
		return num;
	}
	/**
	  * 计算及每个代言人的份数
	  * @param 
	  * @param 
	  */
	public int calculateSpecialAllowancepart(Spokesman sman){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pspokesman.id",sman.getId(), "=");
		qo.addQuery("obj.spokesmanRating.rating",Short.parseShort("3"), "=");
		qo.setPageSize(-1);
		List<?> list = this.spokesmanService.getSpokesmanBy(qo).getResult();
		int num = 0;
		if(list != null){
			num = list.size();
		}
		return num;
		
	}
}
