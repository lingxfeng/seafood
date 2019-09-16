package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ISinglePageNewsDAO;
import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.cms.query.SinglePageQuery;
import com.eastinno.otransos.cms.service.ISinglePageNewsService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SinglePageNewsServiceImpl
 */
@Service
public class SinglePageNewsServiceImpl implements ISinglePageNewsService {
    @Resource
    private ISinglePageNewsDAO singlePageNewsDAO;

    public void setSinglePageNewsDAO(ISinglePageNewsDAO singlePageNewsDAO) {
        this.singlePageNewsDAO = singlePageNewsDAO;
    }

    public Long addSinglePageNews(SinglePageNews singlePageNews) {
        singlePageNews.setStatus(0);
        if (singlePageNews.getSequence() == null) {
            // 处理顺序
            QueryObject qo = new QueryObject();
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            qo.setPageSize(1);
            List list = this.getSinglePageNewsBy(qo).getResult();
            if (list != null && list.size() > 0) {
                SinglePageNews last = (SinglePageNews) list.get(0);
                singlePageNews.setSequence(last.getSequence() == null ? 1 : last.getSequence().intValue() + 1);
            } else
                singlePageNews.setSequence(1);
        }
        TenantObjectUtil.setObject(singlePageNews);
        this.singlePageNewsDAO.save(singlePageNews);
        return singlePageNews.getId();
    }

    public SinglePageNews getSinglePageNews(Long id) {
        SinglePageNews singlePageNews = this.singlePageNewsDAO.get(id);
        if (!TenantObjectUtil.checkObjectService(singlePageNews)) {
            throw new LogicException("非法数据访问");
        }
        if (singlePageNews != null) {
            return singlePageNews;
        }
        return null;
    }

    public SinglePageNews getSinglePageNewsByCode(String code) {
        SinglePageNews singlePageNews = this.singlePageNewsDAO.getBy("code", code);
        if (!TenantObjectUtil.checkObjectService(singlePageNews)) {
            throw new LogicException("非法数据访问");
        }
        return singlePageNews;
    }

    public boolean delSinglePageNews(Long id) {
        SinglePageNews singlePageNews = this.getSinglePageNews(id);
        if (!TenantObjectUtil.checkObjectService(singlePageNews)) {
            throw new LogicException("非法数据访问");
        }
        if (singlePageNews != null) {
            this.singlePageNewsDAO.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelSinglePageNewss(List<Serializable> singlePageNewsIds) {

        for (Serializable id : singlePageNewsIds) {
            delSinglePageNews((Long) id);
        }
        return true;
    }

    public IPageList getSinglePageNewsBy(IQueryObject qo) {
        TenantObjectUtil.addQuery(qo);
        return this.singlePageNewsDAO.findBy(qo);
    }

    public boolean updateSinglePageNews(Long id, SinglePageNews singlePageNews) {
        if (id != null) {
            singlePageNews.setId(id);
        } else {
            return false;
        }
        if (singlePageNews.getTenant() == null) {
            TenantObjectUtil.setObject(singlePageNews);
        }
        singlePageNews.setUpdateDate(new Date());
        this.singlePageNewsDAO.update(singlePageNews);
        return true;
    }

    public boolean moveDir(String sourceDirId, String targetDirId) {
        if (sourceDirId.equals("")) {
            return false;
        }
        SinglePageNews sourceDir = getSinglePageNews(Long.parseLong(sourceDirId));
        if (!TenantObjectUtil.checkObjectService(sourceDir)) {
            throw new LogicException("非法数据访问");
        }
        if (targetDirId.equals("")) {
            sourceDir.setParent(null);
        } else {
            SinglePageNews targetDir = getSinglePageNews(Long.parseLong(targetDirId));
            if (!TenantObjectUtil.checkObjectService(targetDir)) {
                throw new LogicException("非法数据访问");
            }
            if (sourceDir.getTpl() == null) {
                sourceDir.setTpl(targetDir.getTpl());
            }
            sourceDir.setParent(targetDir);
        }
        this.updateSinglePageNews(sourceDir.getId(), sourceDir);
        return true;
    }

    public List<SinglePageNews> getRootsDir() {
        SinglePageQuery sqo = new SinglePageQuery();
        sqo.addQuery("obj.parent IS EMPTY", null);
        // sqo.addQuery("status", new Integer(0), ">=");
        sqo.addQuery("display", true, "=");
        sqo.setOrderBy("sequence");
        TenantObjectUtil.addQuery(sqo);
        sqo.setPageSize(-1);
        return getSinglePageNewsBy(sqo).getResult();
    }

}
