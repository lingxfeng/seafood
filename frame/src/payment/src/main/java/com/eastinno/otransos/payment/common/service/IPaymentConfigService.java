package com.eastinno.otransos.payment.common.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * PaymentConfigService
 */
public interface IPaymentConfigService {
    /**
     * 保存一个PaymentConfig，如果保存成功返回该对象的id，否则返回null
     * 
     * @param paymentConfig
     * @return 保存成功的对象的Id
     */
    Long addPaymentConfig(PaymentConfig paymentConfig);

    /**
     * 根据一个ID得到PaymentConfig
     * 
     * @param id
     * @return
     */
    PaymentConfig getPaymentConfig(Long id);

    /**
     * 删除一个PaymentConfig
     * 
     * @param id
     * @return
     */
    boolean delPaymentConfig(Long id);

    /**
     * 批量删除PaymentConfig
     * 
     * @param ids
     * @return
     */
    boolean batchDelPaymentConfigs(List<Serializable> ids);

    /**
     * 通过一个查询对象得到PaymentConfig
     * 
     * @param properties
     * @return
     */
    IPageList getPaymentConfigBy(IQueryObject qo);

    /**
     * 更新一个PaymentConfig
     * 
     * @param id 需要更新的PaymentConfig的id
     * @param dir 需要更新的PaymentConfig
     */
    boolean updatePaymentConfig(Long id, PaymentConfig paymentConfig);
    
    /**
     * 获取一个PaymentConfig
     * 
     * @param name 名称
     */
    PaymentConfig getPaymentConfigByName(String name);
}
