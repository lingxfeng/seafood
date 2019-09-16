package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.dao.INewsDirDAO;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.cms.utils.StatciStr;
import com.eastinno.otransos.cms.utils.cache.MemeryCache;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * 栏目管理业务层实现
 * 
 * @author nsz
 */
@Service
public class NewsDirServiceImpl implements INewsDirService {
    @Resource
    private INewsDirDAO newsDirDao;


    public void setNewsDirDao(INewsDirDAO newsDirDao) {
        this.newsDirDao = newsDirDao;
    }


    public Long addNewsDir(NewsDir newsDir) {
        if (!StringUtils.hasText(newsDir.getCode())) {
            newsDir.setCode(CommUtil.getRandomVal(6));
        }
        newsDir.setCode(TenantContext.getTenant().getCode() + "_" + newsDir.getCode());

        if (newsDir.getParent() != null) {
            newsDir.setDirPath(newsDir.getParent().getDirPath() + newsDir.getCode() + "@");
        }
        else {
            newsDir.setDirPath(newsDir.getCode() + "@");
        }
        TenantObjectUtil.setObject(newsDir);
        this.newsDirDao.save(newsDir);
        if (newsDir.getId() != null) {
            rebuiltCache();
            return newsDir.getId();
        }
        return null;
    }


    public List<NewsDir> getRootsDir() {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.parent IS EMPTY", null);
        qo.addQuery("status", new Integer(0), ">=");
        qo.addQuery("display", new Integer(1), "=");
        qo.setOrderBy("sequence");
        TenantObjectUtil.addQuery(qo);
        qo.setPageSize(-1);
        return getNewsDirBy(qo).getResult();
    }


    public NewsDir getNewsDir(Long id) {
        NewsDir newsDir = this.newsDirDao.get(id);
        if (!TenantObjectUtil.checkObjectService(newsDir)) {
            throw new LogicException("非法数据访问");
        }
        return newsDir;
    }


    public boolean delNewsDir(Long id) {
        NewsDir newsDir = this.getNewsDir(id);
        if (!TenantObjectUtil.checkObjectService(newsDir))
            throw new LogicException("非法数据访问");
        if (newsDir != null) {
            this.newsDirDao.remove(id);
            rebuiltCache();
            return true;
        }
        return false;
    }


    public boolean batchDelNewsDirs(List<Serializable> newsDirIds) {
        for (Serializable id : newsDirIds) {
            delNewsDir((Long) id);
        }
        rebuiltCache();
        return true;
    }


    public IPageList getNewsDirBy(IQueryObject queryObj) {
        TenantObjectUtil.addQuery(queryObj);
        return this.newsDirDao.findBy(queryObj);
    }


    public boolean updateNewsDir(Long id, NewsDir newsDir) {
        if (id == null)
            return false;
        newsDir.setId(id);
        if (newsDir.getTenant() == null) {
            TenantObjectUtil.setObject(newsDir);
        }
        rebuiltCache();
        this.newsDirDao.update(newsDir);
        return true;
    }


    /**
     * 此处修改
     */
    public NewsDir getNewsDirByCode(String dirCode) {
        NewsDir dir = newsDirDao.getBy("code", dirCode);
        if (!TenantObjectUtil.checkObjectService(dir)) {
            throw new LogicException("非法数据访问");
        }
        return dir;
    }


    private void rebuiltCache() {
        String tenantCode = TenantContext.getTenant().getCode();
        MemeryCache.getInstance().remove(StatciStr.DO_BEFORE.DIR_LIST + tenantCode);
    }


    @Override
    public List<NewsDir> getShowNewsDir() {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status", 1, "=");
        qo.addQuery("obj.parent is EMPTY", null);
        qo.setOrderBy("sequence");
        qo.setPageSize(-1);
        TenantObjectUtil.addQuery(qo);
        return this.getNewsDirBy(qo).getResult();
    }

}