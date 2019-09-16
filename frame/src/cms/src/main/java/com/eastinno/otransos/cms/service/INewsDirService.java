package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 栏目管理业务层接口
 * 
 * @author nsz
 */
public interface INewsDirService {
    /**
     * 根据栏目编号获取对应的栏目对象
     * 
     * @param dirCode
     * @return
     */
    NewsDir getNewsDirByCode(String dirCode);

    /**
     * 保存一个NewsDir，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addNewsDir(NewsDir domain);

    /**
     * 根据一个ID得到NewsDir
     * 
     * @param id
     * @return
     */
    NewsDir getNewsDir(Long id);

    /**
     * 得到所有的根栏目（一级栏目）
     * 
     * @return 所有的根栏目对象列表
     */
    List<NewsDir> getRootsDir();

    /**
     * 删除一个NewsDir
     * 
     * @param id
     * @return
     */
    boolean delNewsDir(Long id);

    /**
     * 批量删除NewsDir
     * 
     * @param ids
     * @return
     */
    boolean batchDelNewsDirs(List<Serializable> ids);

    /**
     * 通过一个查询对象得到NewsDir
     * 
     * @param properties
     * @return
     */
    IPageList getNewsDirBy(IQueryObject queryObj);

    /**
     * 更新一个NewsDir
     * 
     * @param id 需要更新的NewsDir的id
     * @param dir 需要更新的NewsDir
     */
    boolean updateNewsDir(Long id, NewsDir entity);

    /**
     * 获取一级导航栏目
     * 
     * @param
     * @return
     */
    List<NewsDir> getShowNewsDir();

}
