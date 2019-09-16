package com.eastinno.otransos.core.support.query;

/**
 * 包装分页信息
 * 
 * @author stefanie wu
 */
public class PageObject {
    private Integer currentPage = 0;

    private Integer pageSize = -1;

    public Integer getCurrentPage() {
        if (currentPage == null) {
            currentPage = -1;
        }
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if (currentPage == null) {
            currentPage = -1;
        }
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
