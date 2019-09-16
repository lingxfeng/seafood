package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * NewsDocService
 * 
 * @author ksmwly@gmail.com
 */
public interface INewsDocService {
    /**
     * 保存一个NewsDoc，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addNewsDoc(NewsDoc domain);

    /**
     * 根据一个ID得到NewsDoc
     * 
     * @param id
     * @return
     */
    NewsDoc getNewsDoc(Long id);

    /**
     * 删除一个NewsDoc
     * 
     * @param id
     * @return
     */
    boolean delNewsDoc(Long id);

    /**
     * 批量删除NewsDoc
     * 
     * @param ids
     * @return
     */
    boolean batchDelNewsDocs(List<Serializable> ids);

    /**
     * 通过一个查询对象得到NewsDoc
     * 
     * @param properties
     * @return
     */
    IPageList getNewsDocBy(IQueryObject queryObj);

    /**
     * 更新一个NewsDoc
     * 
     * @param id 需要更新的NewsDoc的id
     * @param dir 需要更新的NewsDoc
     */
    boolean updateNewsDoc(Long id, NewsDoc entity);

    /**
     * 更新一个NewsDoc状态
     * 
     * @param id 需要更新的NewsDoc的id
     * @param dir 需要更新的status
     */
    boolean updateNewsDocStatus(Long id, Integer status);
}
