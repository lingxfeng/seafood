package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ILinksManageDAO;
import com.eastinno.otransos.cms.domain.LinksManage;
import com.eastinno.otransos.cms.service.ILinksManageService;
import com.eastinno.otransos.cms.utils.StatciStr;
import com.eastinno.otransos.cms.utils.cache.MemeryCache;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * 友情链接业务层实现
 * 
 * @author nsz
 */
@Service
public class LinksManageServiceImpl implements ILinksManageService {
    @Resource
    private ILinksManageDAO linksManageDao;


    public void setLinksManageDao(ILinksManageDAO linksManageDao) {
        this.linksManageDao = linksManageDao;
    }


    public Long addLinksManage(LinksManage linksManage) {
        TenantObjectUtil.setObject(linksManage);
        this.linksManageDao.save(linksManage);
        if (linksManage != null && linksManage.getId() != null) {
            rebuiltCache();
            return linksManage.getId();
        }
        return null;
    }


    public LinksManage getLinksManage(Long id) {
        LinksManage linksManage = this.linksManageDao.get(id);
        if (!TenantObjectUtil.checkObjectService(linksManage)) {
            throw new LogicException("非法数据访问");
        }
        return linksManage;
    }


    public boolean delLinksManage(Long id) {
        LinksManage linksManage = this.getLinksManage(id);
        if (!TenantObjectUtil.checkObjectService(linksManage))
            throw new LogicException("非法数据访问");
        if (linksManage != null) {
            this.linksManageDao.remove(id);
            rebuiltCache();
            return true;
        }

        return false;
    }


    public boolean batchDelLinksManages(List<Serializable> linksManageIds) {

        for (Serializable id : linksManageIds) {
            delLinksManage((Long) id);
        }
        rebuiltCache();
        return true;
    }


    public IPageList getLinksManageBy(IQueryObject queryObj) {
        TenantObjectUtil.addQuery(queryObj);
        return this.linksManageDao.findBy(queryObj);
    }


    public boolean updateLinksManage(Long id, LinksManage linksManage) {
        if (id != null) {
            linksManage.setId(id);
        }
        else {
            return false;
        }
        if (linksManage.getTenant() == null) {
            TenantObjectUtil.setObject(linksManage);
        }
        this.linksManageDao.update(linksManage);
        rebuiltCache();

        return true;
    }


    private void rebuiltCache() {
        String tenantCode = TenantContext.getTenant().getCode();
        MemeryCache.getInstance().remove(StatciStr.DO_BEFORE.LINK_LIST + tenantCode);
    }


    @Override
    public List<Object> getShowLinksManage() {
        QueryObject qo = new QueryObject();
        qo.setOrderBy("sequence");
        qo.addQuery("obj.status", Short.parseShort("1"), "=");
        Date curDate = new Date();
        qo.addQuery("obj.startDate", curDate, "<=");
        qo.addQuery("obj.endDate", curDate, ">=");
        IPageList pl = this.getLinksManageBy(qo);
        return pl.getResult();
    }

}
