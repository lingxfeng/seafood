package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ILinkImgGroupService
 * 
 * @author ksmwly@gmail.com
 */
public interface ILinkImgGroupService {
    /**
     * 保存一个LinkImgGroup，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addLinkImgGroup(LinkImgGroup domain);

    /**
     * 根据一个ID得到LinkImgGroup
     * 
     * @param id
     * @return
     */
    LinkImgGroup getLinkImgGroup(Long id);

    /**
     * 删除一个LinkImgGroup
     * 
     * @param id
     * @return
     */
    boolean delLinkImgGroup(Long id);

    /**
     * 批量删除LinkImgGroup
     * 
     * @param ids
     * @return
     */
    boolean batchDelLinkImgGroups(List<Serializable> ids);

    /**
     * 通过一个查询对象得到LinkImgGroup
     * 
     * @param properties
     * @return
     */
    IPageList getLinkImgGroupBy(IQueryObject queryObj);

    /**
     * 更新一个LinkImgGroup
     * 
     * @param id 需要更新的LinkImgGroup的id
     * @param dir 需要更新的LinkImgGroup
     */
    boolean updateLinkImgGroup(Long id, LinkImgGroup entity);

    List<LinkImgGroup> getLinkImgGroupByType(LinkImgType sdd);

    /**
     * 根据指定排序号查询指定CODE图片组下的单张图片
     * 
     * @param code 图片组所属分类CODE
     * @param sequence 指定排序号
     * @return
     */
    LinkImgGroup getLinkImgGroupBy(String code, Integer sequence);
}
