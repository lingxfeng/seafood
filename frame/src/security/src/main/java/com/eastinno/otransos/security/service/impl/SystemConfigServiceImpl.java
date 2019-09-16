package com.eastinno.otransos.security.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.dao.ISystemConfigDAO;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemConfigService;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 平台管理业务层实现
 * 
 * @author nsz
 */
@Service
public class SystemConfigServiceImpl implements ISystemConfigService {
    @Resource
    private ISystemConfigDAO systemConfigDao;

    public void setSystemConfigDao(ISystemConfigDAO systemConfigDao) {
        this.systemConfigDao = systemConfigDao;
    }

    public Long addSystemConfig(SystemConfig systemConfig) {
        TenantObjectUtil.setObject(systemConfig);
        this.systemConfigDao.save(systemConfig);
        if (systemConfig != null && systemConfig.getId() != null) {
            return systemConfig.getId();
        }
        return null;
    }

    public SystemConfig getSystemConfig(Long id) {
        SystemConfig systemConfig = this.systemConfigDao.get(id);
        if (!TenantObjectUtil.checkObjectService(systemConfig)) {
            throw new LogicException("非法数据访问");
        }
        return systemConfig;
    }

    public boolean delSystemConfig(Long id) {
        SystemConfig systemConfig = this.getSystemConfig(id);
        if (!TenantObjectUtil.checkObjectService(systemConfig)) {
            throw new LogicException("非法数据访问");
        }
        if (systemConfig != null) {
            this.systemConfigDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelSystemConfigs(List<Serializable> systemConfigIds) {
        for (Serializable id : systemConfigIds) {
            delSystemConfig((Long) id);
        }
        return true;
    }

    public IPageList getSystemConfigBy(IQueryObject queryObj) {
        TenantObjectUtil.addQuery(queryObj);
        return this.systemConfigDao.findBy(queryObj);
    }

    @Override
    public Map<String, String> getConfig(String... keys) {
        Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<SystemConfig> cons = this.systemConfigDao.findBy(qo).getResult();
        Map<String, String> mapReturn = new HashMap<String, String>();
        if (cons != null && cons.size() > 0) {
            SystemConfig config = cons.get(0);
            Map<String, String> map = (Map<String, String>) config.toJSonObject();
            for (String str : keys) {
                mapReturn.put(str, map.get(str));
            }
        }
        return mapReturn;
    }

    public boolean updateSystemConfig(Long id, SystemConfig systemConfig) {
        if (id != null) {
            systemConfig.setId(id);
        } else {
            return false;
        }
        if (systemConfig.getTenant() == null) {
            TenantObjectUtil.setObject(systemConfig);
        }
        this.systemConfigDao.update(systemConfig);
        return true;
    }

    @Override
    public SystemConfig getSystemConfig() {
        QueryObject qo = new QueryObject();
        TenantObjectUtil.addQuery(qo);
        List<SystemConfig> cons = this.systemConfigDao.findBy(qo).getResult();
        if (cons != null && cons.size() > 0) {
            return cons.get(0);
        }
        return null;
    }

    @Override
    public boolean setConfig(Map<String, String> map) {
        Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<SystemConfig> cons = this.systemConfigDao.findBy(qo).getResult();
        if (cons != null && cons.size() > 0) {
            SystemConfig config = cons.get(0);
            Map<String, Object> mapOld = config.toJsonMap();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                mapOld.put(key, map.get(key));
            }
            config.setJsonStr(JSONObject.toJSONString(mapOld));
        } else {
            SystemConfig config = new SystemConfig();
            config.setJsonStr(JSONObject.toJSONString(map));
            this.systemConfigDao.save(config);
        }
        return true;
    }

    @Override
    public boolean delConfig(String... keys) {
        Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<SystemConfig> cons = this.systemConfigDao.findBy(qo).getResult();
        if (cons != null && cons.size() > 0) {
            SystemConfig config = cons.get(0);
            Map<String, String> map = (Map<String, String>) config.toJSonObject();
            for (String str : keys) {
                map.remove(str);
            }
            config.setJsonStr(JSONObject.toJSONString(map));
        }
        return true;
    }

	@Override
	public Object getConfigOne(String key) {
		Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<SystemConfig> cons = this.systemConfigDao.findBy(qo).getResult();
        String strreturn=null;
        if (cons != null && cons.size() > 0) {
            SystemConfig config = cons.get(0);
            Map<String, String> map = (Map<String, String>) config.toJSonObject();
            strreturn = map.get(key);
        }
        return strreturn;
	}

}
