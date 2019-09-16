package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SinglePageNewsService
 */
public interface ISinglePageNewsService {
    /**
     * 保存一个SinglePageNews，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addSinglePageNews(SinglePageNews instance);

    /**
     * 根据一个ID得到SinglePageNews
     * 
     * @param id
     * @return
     */
    SinglePageNews getSinglePageNews(Long id);

    /**
     * 根据一个ID得到SinglePageNews
     * 
     * @param id
     * @return
     */
    SinglePageNews getSinglePageNewsByCode(String code);

    /**
     * 删除一个SinglePageNews
     * 
     * @param id
     * @return
     */
    boolean delSinglePageNews(Long id);

    /**
     * 批量删除SinglePageNews
     * 
     * @param ids
     * @return
     */
    boolean batchDelSinglePageNewss(List<Serializable> ids);

    /**
     * 通过一个查询对象得到SinglePageNews
     * 
     * @param properties
     * @return
     */
    IPageList getSinglePageNewsBy(IQueryObject queryObject);

    /**
     * 更新一个SinglePageNews
     * 
     * @param id 需要更新的SinglePageNews的id
     * @param dir 需要更新的SinglePageNews
     */
    boolean updateSinglePageNews(Long id, SinglePageNews instance);

    /**
     * 将一个文件夹及其下所有文件夹和文章移动到目标文件夹下
     * 
     * @param targetDirId 目标文件夹的id
     * @param sourceDirId 要移动的文件夹的id
     * @return 移动成功返回true，失败返回false
     */
    boolean moveDir(String sourceDirId, String targetDirId);

    /**
     * 得到所有的根文件夹
     * 
     * @return 所有的根文件夹对象列表
     */
    List<SinglePageNews> getRootsDir();
}
