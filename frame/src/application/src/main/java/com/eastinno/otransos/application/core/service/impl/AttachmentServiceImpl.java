package com.eastinno.otransos.application.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.application.core.dao.IAttachmentDAO;
import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.application.core.service.IAttachmentService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * AttachmentServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class AttachmentServiceImpl implements IAttachmentService {
    @Resource
    private IAttachmentDAO attachmentDao;

    public void setAttachmentDao(IAttachmentDAO attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    public Long addAttachment(Attachment attachment) {
        this.attachmentDao.save(attachment);
        if (attachment != null && attachment.getId() != null) {
            return attachment.getId();
        }
        return null;
    }

    public Attachment getAttachment(Long id) {
        Attachment attachment = this.attachmentDao.get(id);
        return attachment;
    }

    public boolean delAttachment(Long id) {
        Attachment attachment = this.getAttachment(id);
        if (attachment != null) {
            this.attachmentDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelAttachments(List<Serializable> attachmentIds) {

        for (Serializable id : attachmentIds) {
            delAttachment((Long) id);
        }
        return true;
    }

    public IPageList getAttachmentBy(IQueryObject queryObj) {
        return this.attachmentDao.findBy(queryObj);
    }

    public boolean updateAttachment(Long id, Attachment attachment) {
        if (id != null) {
            attachment.setId(id);
        } else {
            return false;
        }
        this.attachmentDao.update(attachment);
        return true;
    }

    @Override
    public Long addAttachments(List<Attachment> domain) {
        for (Attachment a : domain) {
            addAttachment(a);
        }
        return null;
    }

}
