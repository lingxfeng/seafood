package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.GuestBook;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * GuestBookService
 */
public interface IGuestBookService {
    /**
     * 保存一个GuestBook，如果保存成功返回该对象的id，否则返回null
     * 
     * @param guestBook
     * @return 保存成功的对象的Id
     */
    Long addGuestBook(GuestBook guestBook);

    /**
     * 根据一个ID得到GuestBook
     * 
     * @param id
     * @return
     */
    GuestBook getGuestBook(Long id);

    /**
     * 删除一个GuestBook
     * 
     * @param id
     * @return
     */
    boolean delGuestBook(Long id);

    /**
     * 批量删除GuestBook
     * 
     * @param ids
     * @return
     */
    boolean batchDelGuestBooks(List<Serializable> ids);

    /**
     * 通过一个查询对象得到GuestBook
     * 
     * @param properties
     * @return
     */
    IPageList getGuestBookBy(IQueryObject qo);

    /**
     * 更新一个GuestBook
     * 
     * @param id 需要更新的GuestBook的id
     * @param dir 需要更新的GuestBook
     */
    boolean updateGuestBook(Long id, GuestBook guestBook);
}
