package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.dao.IBaseUIConfigDAO;
import com.eastinno.otransos.core.domain.BaseUIConfig;
import com.eastinno.otransos.core.service.IBaseUIConfigService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class BaseUIConfigServiceImpl implements IBaseUIConfigService {
    @Resource
    private IBaseUIConfigDAO baseUIConfigDao;

    public void setBaseUIConfigDao(IBaseUIConfigDAO baseUIConfigDao) {
        this.baseUIConfigDao = baseUIConfigDao;
    }

    public Long addBaseUIConfig(BaseUIConfig baseUIConfig) {
        this.baseUIConfigDao.save(baseUIConfig);
        if ((baseUIConfig != null) && (baseUIConfig.getId() != null)) {
            return baseUIConfig.getId();
        }
        return null;
    }

    public BaseUIConfig getBaseUIConfig(Long id) {
        BaseUIConfig baseUIConfig = (BaseUIConfig) this.baseUIConfigDao.get(id);
        return baseUIConfig;
    }

    public boolean delBaseUIConfig(Long id) {
        BaseUIConfig baseUIConfig = getBaseUIConfig(id);
        if (baseUIConfig != null) {
            this.baseUIConfigDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelBaseUIConfigs(List<Serializable> baseUIConfigIds) {
        for (Serializable id : baseUIConfigIds) {
            delBaseUIConfig((Long) id);
        }
        return true;
    }

    public IPageList getBaseUIConfigBy(IQueryObject queryObject) {
        return QueryUtil.query(queryObject, BaseUIConfig.class, this.baseUIConfigDao);
    }

    public boolean updateBaseUIConfig(Long id, BaseUIConfig baseUIConfig) {
        if (id != null)
            baseUIConfig.setId(id);
        else {
            return false;
        }
        this.baseUIConfigDao.update(baseUIConfig);
        return true;
    }
}
