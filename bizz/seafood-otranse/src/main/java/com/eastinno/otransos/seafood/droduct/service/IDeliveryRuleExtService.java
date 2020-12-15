package com.eastinno.otransos.seafood.droduct.service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.DeliveryRuleExt;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 运费相关的事务类
 * 
 * @author wb
 *
 */
public interface IDeliveryRuleExtService {

	IPageList getDeliveryRuleExtBy(IQueryObject qo);

	Long addDeliveryRuleExt(DeliveryRuleExt domain);

	DeliveryRuleExt getDeliveryRuleExt(Long id);

	boolean updateDeliveryRuleExt(Long id, DeliveryRuleExt entry);
}
