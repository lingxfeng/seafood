package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.eastinno.otransos.cms.dao.IGuestBookDAO;
import com.eastinno.otransos.cms.domain.GuestBook;
import com.eastinno.otransos.cms.service.IGuestBookService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * GuestBookServiceImpl
 */
@Service
public class GuestBookServiceImpl implements IGuestBookService {
    @Resource
    private IGuestBookDAO guestBookDao;

    public void setGuestBookDao(IGuestBookDAO guestBookDao) {
        this.guestBookDao = guestBookDao;
    }

    public Long addGuestBook(GuestBook guestBook) {
        guestBook.setContent(HtmlUtils.htmlEscape(guestBook.getContent()));
        TenantObjectUtil.setObject(guestBook);
        this.guestBookDao.save(guestBook);
//        if (guestBook != null && guestBook.getId() != null) {
//            return guestBook.getId();
//        }
        return null;
    }

    public GuestBook getGuestBook(Long id) {
        GuestBook guestBook = this.guestBookDao.get(id);
        return guestBook;
    }

    public boolean delGuestBook(Long id) {
        GuestBook guestBook = this.getGuestBook(id);
        if (!TenantObjectUtil.checkObjectService(guestBook))
            throw new LogicException("非法数据访问");
        if (guestBook != null) {
            this.guestBookDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelGuestBooks(List<Serializable> guestBookIds) {

        for (Serializable id : guestBookIds) {
            delGuestBook((Long) id);
        }
        return true;
    }

    public IPageList getGuestBookBy(IQueryObject qo) {
    	TenantObjectUtil.addQuery(qo);
        return this.guestBookDao.findBy(qo);
    }

    public boolean updateGuestBook(Long id, GuestBook guestBook) {
        guestBook.setContent(HtmlUtils.htmlUnescape(guestBook.getContent()));
        guestBook.setContent(HtmlUtils.htmlEscape(guestBook.getContent()));
        if (id != null) {
            guestBook.setId(id);
        } else {
            return false;
        }
        if (guestBook.getTenant() == null) {
            TenantObjectUtil.setObject(guestBook);
        }
        guestBook.setStatus(1);
        this.guestBookDao.update(guestBook);
        return true;
    }

}
