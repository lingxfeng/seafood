package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.INewsDirDAO;
import com.eastinno.otransos.cms.dao.INewsDocDAO;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.cms.utils.CmsConstant;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * NewsDocServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class NewsDocServiceImpl implements INewsDocService {
    @Resource
    private INewsDocDAO newsDocDao;

    @Resource
    private INewsDirDAO newsDirDao;

    public void setNewsDocDao(INewsDocDAO newsDocDao) {
        this.newsDocDao = newsDocDao;
    }

    public Long addNewsDoc(NewsDoc newsDoc) {
        if (newsDoc.getDir() != null) {
            // TODO 未测试
            dirPlus(newsDoc.getDir());
            newsDoc.setDirPath(newsDoc.getDir().getDirPath());
        }
        if (newsDoc.getContent() != null) {
            newsDoc.setContent(newsDoc.getContent().replace("</script>", "<\\/script>"));
        }
        TenantObjectUtil.setObject(newsDoc);
        this.newsDocDao.save(newsDoc);
        if (newsDoc.getId() != null) {
            return newsDoc.getId();
        }
        return null;
    }

    public NewsDoc getNewsDoc(Long id) {
        NewsDoc newsDoc = this.newsDocDao.get(id);
        if (!TenantObjectUtil.checkObjectService(newsDoc)) {
            throw new LogicException("非法数据访问");
        }
        return newsDoc;
    }

    public boolean delNewsDoc(Long id) {
        NewsDoc newsDoc = this.getNewsDoc(id);
        if (!TenantObjectUtil.checkObjectService(newsDoc))
            throw new LogicException("非法数据访问");
        if (newsDoc != null) {
            this.newsDocDao.remove(id);
            if (newsDoc.getDir() != null) {
                // 也没测试
                dirSub(newsDoc.getDir());
            }
            return true;
        }
        return false;
    }

    public boolean batchDelNewsDocs(List<Serializable> newsDocIds) {

        for (Serializable id : newsDocIds) {
            delNewsDoc((Long) id);
        }
        return true;
    }

    public IPageList getNewsDocBy(IQueryObject queryObj) {
        TenantObjectUtil.addQuery(queryObj);
        return this.newsDocDao.findBy(queryObj);
    }

    public boolean updateNewsDoc(Long id, NewsDoc newsDoc) {
        if (id != null) {
            newsDoc.setId(id);
        } else {
            return false;
        }
        if (newsDoc.getTenant() == null) {
            TenantObjectUtil.setObject(newsDoc);
        }
        if (newsDoc.getDir() != null) {
            newsDoc.setDirPath(newsDoc.getDir().getDirPath());
        }
        if (newsDoc.getContent() != null) {
            newsDoc.setContent(newsDoc.getContent().replace("</script>", "<\\/script>"));
        }
        this.newsDocDao.update(newsDoc);
        return true;
    }

    @Override
    public boolean updateNewsDocStatus(Long id, Integer status) {
        NewsDoc newsDoc = newsDocDao.get(id);
        if (newsDoc.getTenant() == null) {
            TenantObjectUtil.setObject(newsDoc);
        }
        newsDoc.setStatus(status);
        this.newsDocDao.update(newsDoc);
        return true;
    }

    /**
     * 对栏目下的文章数量，加1 同时递归 父栏目
     * 
     * @param newsDir
     */
    private void dirPlus(NewsDir newsDir) {
        if (!TenantObjectUtil.checkObjectService(newsDir)) {
            throw new LogicException("非法数据访问");
        }
        if (newsDir != null) {
            if (newsDir.getDocNum() == null) {
                newsDir.setDocNum(1);
            } else {
                newsDir.setDocNum(newsDir.getDocNum() + 1);
            }

            if (newsDir.getParent() != null) {
                dirPlus(newsDir.getParent());
            }
        }
    }

    private void dirSub(NewsDir newsDir) {
        if (!TenantObjectUtil.checkObjectService(newsDir)) {
            throw new LogicException("非法数据访问");
        }
        if (newsDir != null) {
            if (newsDir.getDocNum() == null || newsDir.getDocNum() < 2) {
                newsDir.setDocNum(0);
            } else {
                newsDir.setDocNum(newsDir.getDocNum() - 1);
            }

            if (newsDir.getParent() != null) {
                dirSub(newsDir.getParent());
            }
        }
    }
}
