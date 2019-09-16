package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 平台管理业务层接口
 * 
 * @author nsz
 */
public interface ISystemConfigService {
    /**
     * 保存一个SystemConfig，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addSystemConfig(SystemConfig domain);

    /**
     * 根据一个ID得到SystemConfig
     * 
     * @param id
     * @return
     */
    SystemConfig getSystemConfig(Long id);

    /**
     * 删除一个SystemConfig
     * 
     * @param id
     * @return
     */
    boolean delSystemConfig(Long id);

    /**
     * 获取配置
     * 
     * @param key
     * @return
     */
    /**
     * 获取某配置信息
     * 
     * @param keys
     * @return
     */
    Map<String, String> getConfig(String... keys);
    /*
     */
    Object getConfigOne(String key);
    /**
     * 批量删除SystemConfig
     * 
     * @param ids
     * @return
     */
    boolean batchDelSystemConfigs(List<Serializable> ids);

    /**
     * 通过一个查询对象得到SystemConfig
     * 
     * @param properties
     * @return
     */
    IPageList getSystemConfigBy(IQueryObject queryObj);

    SystemConfig getSystemConfig();

    /**
     * 更新一个SystemConfig
     * 
     * @param id 需要更新的SystemConfig的id
     * @param dir 需要更新的SystemConfig
     */
    boolean updateSystemConfig(Long id, SystemConfig entity);

    /**
     * 设置配置信息
     * 
     * @param map
     * @return
     */
    boolean setConfig(Map<String, String> map);

    /**
     * 删除某参数配置信息
     * 
     * @param keys
     * @return
     */
    boolean delConfig(String... keys);
}
