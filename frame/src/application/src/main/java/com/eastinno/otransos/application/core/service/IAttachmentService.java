package com.eastinno.otransos.application.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * AttachmentService
 * 
 * @author ksmwly@gmail.com
 */
public interface IAttachmentService {
    /**
     * 保存一个Attachment，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addAttachment(Attachment domain);

    Long addAttachments(List<Attachment> domain);

    /**
     * 根据一个ID得到Attachment
     * 
     * @param id
     * @return
     */
    Attachment getAttachment(Long id);

    /**
     * 删除一个Attachment
     * 
     * @param id
     * @return
     */
    boolean delAttachment(Long id);

    /**
     * 批量删除Attachment
     * 
     * @param ids
     * @return
     */
    boolean batchDelAttachments(List<Serializable> ids);

    /**
     * 通过一个查询对象得到Attachment
     * 
     * @param properties
     * @return
     */
    IPageList getAttachmentBy(IQueryObject queryObj);

    /**
     * 更新一个Attachment
     * 
     * @param id 需要更新的Attachment的id
     * @param dir 需要更新的Attachment
     */
    boolean updateAttachment(Long id, Attachment entity);
}
