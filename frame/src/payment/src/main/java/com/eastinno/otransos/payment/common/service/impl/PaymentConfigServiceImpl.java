package com.eastinno.otransos.payment.common.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.payment.common.dao.IPaymentConfigDAO;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * PaymentConfigServiceImpl
 */
@Service
public class PaymentConfigServiceImpl implements IPaymentConfigService {
    @Resource
    private IPaymentConfigDAO paymentConfigDao;

    public void setPaymentConfigDao(IPaymentConfigDAO paymentConfigDao) {
        this.paymentConfigDao = paymentConfigDao;
    }

    public Long addPaymentConfig(PaymentConfig paymentConfig) {
        TenantObjectUtil.setObject(paymentConfig);
        this.paymentConfigDao.save(paymentConfig);
        return paymentConfig.getId();
    }

    public PaymentConfig getPaymentConfig(Long id) {
        PaymentConfig paymentConfig = this.paymentConfigDao.get(id);
        return paymentConfig;
    }

    public boolean delPaymentConfig(Long id) {
        PaymentConfig paymentConfig = this.getPaymentConfig(id);
        if (paymentConfig != null) {
            this.paymentConfigDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelPaymentConfigs(List<Serializable> paymentConfigIds) {

        for (Serializable id : paymentConfigIds) {
            delPaymentConfig((Long) id);
        }
        return true;
    }

    public IPageList getPaymentConfigBy(IQueryObject qo) {
        TenantObjectUtil.addQuery(qo);
        return this.paymentConfigDao.findBy(qo);
    }

    public boolean updatePaymentConfig(Long id, PaymentConfig paymentConfig) {
        if (id != null) {
            paymentConfig.setId(id);
        } else {
            return false;
        }
        this.paymentConfigDao.update(paymentConfig);
        return true;
    }

	@Override
	public PaymentConfig getPaymentConfigByName(String name) {
		QueryObject qo = new QueryObject();
		qo.setLimit(-1);
		List<PaymentConfig> pList=this.paymentConfigDao.findBy(qo).getResult();
		for (PaymentConfig paymentConfig : pList) {
			if("WEIXINMPSM".equals(paymentConfig.getType().toString())){
				return paymentConfig;
			}
		}
		return null;
	}

}
