package com.eastinno.otransos.core.support.query;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.support.query.GenericPageList;
import com.eastinno.otransos.web.tools.IPageList;

public class QueryUtil {
    /**
     * 数据查询工具，可以通过queryObject查询封装来进行数据，数据查询涉及到查询条件，参数值，还涉及到分页信息等。若页码不符合查询范围，由自动设置为第一页，若pageSize为-1，则查询所有数据
     * 
     * @param queryObject
     * @param entityType
     * @param dao
     * @return 分页查询结果
     */
    public static IPageList query(IQueryObject queryObject, Class entityType, IJpaGenericDAO dao) {
        PageObject pageObj = queryObject.getPageObj();
        int currentPage;
        int pageSize;
        currentPage = pageObj.getCurrentPage();
        pageSize = pageObj.getPageSize();
        GenericPageList pageList = new GenericPageList(entityType, queryObject, dao);
        pageList.doList(currentPage, pageSize);// 查询第几页，每页多少条
        return pageList;
    }
}
