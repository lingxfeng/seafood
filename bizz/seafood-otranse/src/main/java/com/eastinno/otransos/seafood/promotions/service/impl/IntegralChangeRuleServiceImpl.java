package com.eastinno.otransos.seafood.promotions.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.promotions.domain.IntegralChangeRule;
import com.eastinno.otransos.seafood.promotions.service.IIntegralChangeRuleService;
import com.eastinno.otransos.seafood.promotions.dao.IIntegralChangeRuleDAO;


/**
 * IntegralChangeRuleServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class IntegralChangeRuleServiceImpl implements IIntegralChangeRuleService{
	@Resource
	private IIntegralChangeRuleDAO integralChangeRuleDao;
	
	public void setIntegralChangeRuleDao(IIntegralChangeRuleDAO integralChangeRuleDao){
		this.integralChangeRuleDao=integralChangeRuleDao;
	}
	
	public Long addIntegralChangeRule(IntegralChangeRule integralChangeRule) {	
		this.integralChangeRuleDao.save(integralChangeRule);
		if (integralChangeRule != null && integralChangeRule.getId() != null) {
			return integralChangeRule.getId();
		}
		return null;
	}
	
	public IntegralChangeRule getIntegralChangeRule(Long id) {
		IntegralChangeRule integralChangeRule = this.integralChangeRuleDao.get(id);
		return integralChangeRule;
		}
	
	public boolean delIntegralChangeRule(Long id) {	
			IntegralChangeRule integralChangeRule = this.getIntegralChangeRule(id);
			if (integralChangeRule != null) {
				this.integralChangeRuleDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelIntegralChangeRules(List<Serializable> integralChangeRuleIds) {
		
		for (Serializable id : integralChangeRuleIds) {
			delIntegralChangeRule((Long) id);
		}
		return true;
	}
	
	public IPageList getIntegralChangeRuleBy(IQueryObject queryObj) {	
		return this.integralChangeRuleDao.findBy(queryObj);		
	}
	
	public boolean updateIntegralChangeRule(Long id, IntegralChangeRule integralChangeRule) {
		if (id != null) {
			integralChangeRule.setId(id);
		} else {
			return false;
		}
		this.integralChangeRuleDao.update(integralChangeRule);
		return true;
	}

	@Override
	public boolean setIntegralRuleByRegister(Long integral) {
		return this.setIntegralRule("register_add", "single", integral);
	}

	@Override
	public Long getIntegralRuleByRegister() {
		Long result = 0L;
		try{
			result = Long.parseLong(this.getIntegralRuleValue("register_add", "single"));
		}catch(Exception e){
			result = 0L;
			System.out.println("注册增积分获取失败！");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean setIntegralCashRate(Long rate) {
		return this.setIntegralRule("cash_rate", "single", rate);
	}

	@Override
	public Long getIntegralCashRate() {
		Long result = 0L;
		try{
			result = Long.parseLong(this.getIntegralRuleValue("cash_rate", "single"));
		}catch(Exception e){
			result = 0L;
			System.out.println("积分现金比率获取失败！");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 设置积分
	 * @param type
	 * @param ruleKey
	 * @param integral
	 * @return
	 */
	private boolean setIntegralRule(String type, String ruleKey, Long integral){
		QueryObject qo = new QueryObject();
		qo.addQuery("type", type, "=");
		qo.addQuery("ruleKey", ruleKey, "=");
		List list = this.getIntegralChangeRuleBy(qo).getResult();
		IntegralChangeRule rule = null;
		if(list==null || list.isEmpty()){
			rule = new IntegralChangeRule();
			rule.setCreateDate(new Date());
			rule.setModifyDate(new Date());
			rule.setType(type);
			rule.setRuleKey(ruleKey);
			rule.setRuleValue(integral+"");
			rule = this.getIntegralChangeRule(this.addIntegralChangeRule(rule));
		}else{
			rule = (IntegralChangeRule) list.get(0);
			rule.setModifyDate(new Date());
			rule.setRuleValue(integral+"");
		}
		return this.updateIntegralChangeRule(rule.getId(), rule);
	}
	
	/**
	 * 设置积分
	 * @param type
	 * @param ruleKey
	 * @param integral
	 * @return
	 */
	private String getIntegralRuleValue(String type, String ruleKey){
		QueryObject qo = new QueryObject();
		qo.addQuery("type", type, "=");
		qo.addQuery("ruleKey", ruleKey, "=");
		List list = this.getIntegralChangeRuleBy(qo).getResult();
		IntegralChangeRule rule = null;
		if(list==null || list.isEmpty()){
			rule = new IntegralChangeRule();
			rule.setCreateDate(new Date());
			rule.setModifyDate(new Date());
			rule.setType(type);
			rule.setRuleKey(ruleKey);
			rule.setRuleValue("1");
			rule = this.getIntegralChangeRule(this.addIntegralChangeRule(rule));
		}else{
			rule = (IntegralChangeRule) list.get(0);
		}
		return rule.getRuleValue();
	} 
}
