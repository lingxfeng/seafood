package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * PPtTypeService
 * 
 * @author ksmwly@gmail.com
 */
public interface ILinkImgTypeService {

    /**
     * 根据图片分类编号获取对应的图片分类对象
     * 
     * @param imgTypeCode
     * @return
     */
    LinkImgType getLinkImgTypeByCode(String imgTypeCode);

    /**
     * 保存一个PPtType，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addPPtType(LinkImgType domain);

    /**
     * 根据一个ID得到PPtType
     * 
     * @param id
     * @return
     */
    LinkImgType getPPtType(Long id);

    /**
     * 删除一个PPtType
     * 
     * @param id
     * @return
     */
    boolean delPPtType(Long id);

    /**
     * 批量删除PPtType
     * 
     * @param ids
     * @return
     */
    boolean batchDelPPtTypes(List<Serializable> ids);

    /**
     * 通过一个查询对象得到PPtType
     * 
     * @param properties
     * @return
     */
    IPageList getPPtTypeBy(IQueryObject queryObj);

    /**
     * 更新一个PPtType
     * 
     * @param id 需要更新的PPtType的id
     * @param dir 需要更新的PPtType
     */
    boolean updatePPtType(Long id, LinkImgType entity);
}
