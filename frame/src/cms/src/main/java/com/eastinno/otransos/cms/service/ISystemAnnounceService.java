package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.SystemAnnounce;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SystemAnnounceService
 * 
 * @author EasyJWeb 1.0-m2 $Id: SystemAnnounceService.java,v 0.0.1 Feb 20, 2009 3:44:56 PM EasyJWeb 1.0-m2 Exp $
 */
public interface ISystemAnnounceService {
    /**
     * 保存一个SystemAnnounce，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addSystemAnnounce(SystemAnnounce instance);

    /**
     * 根据一个ID得到SystemAnnounce
     * 
     * @param id
     * @return
     */
    SystemAnnounce getSystemAnnounce(Long id);

    SystemAnnounce readSystemAnnounce(Long id);

    /**
     * 删除一个SystemAnnounce
     * 
     * @param id
     * @return
     */
    boolean delSystemAnnounce(Long id);

    /**
     * 批量删除SystemAnnounce
     * 
     * @param ids
     * @return
     */
    boolean batchDelSystemAnnounces(List<Serializable> ids);

    /**
     * 通过一个查询对象得到SystemAnnounce
     * 
     * @param properties
     * @return
     */
    IPageList getSystemAnnounceBy(IQueryObject queryObject);

    /**
     * 更新一个SystemAnnounce
     * 
     * @param id 需要更新的SystemAnnounce的id
     * @param dir 需要更新的SystemAnnounce
     */
    boolean updateSystemAnnounce(Long id, SystemAnnounce instance);
}
