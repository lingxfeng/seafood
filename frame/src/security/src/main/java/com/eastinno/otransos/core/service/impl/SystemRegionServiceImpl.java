package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.dao.ISystemRegionDAO;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.service.String2SpellUtil;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class SystemRegionServiceImpl implements ISystemRegionService {
    @Resource
    private ISystemRegionDAO dirDao;

    public void setDirDao(ISystemRegionDAO dirDao) {
        this.dirDao = dirDao;
    }

    public Long addSystemRegion(SystemRegion dir) {
        if (UserContext.getUser() != null)
            dir.setInputUser(UserContext.getUser().getName());
        parseSystemRegion(dir);
        this.dirDao.save(dir);
        return dir.getId();
    }

    public boolean batchDelSystemRegions(List<Serializable> dirIds) {
        for (Serializable id : dirIds) {
            delSystemRegion((Long) id);
        }
        return true;
    }

    public boolean batchRecoverSystemRegions(List<Serializable> ids) {
        if ((ids != null) && (ids.size() > 0)) {
            for (Serializable id : ids) {
                recoverSystemRegion((Long) id);
            }
        }
        return true;
    }

    public boolean recoverSystemRegion(Long id) {
        SystemRegion dir = getSystemRegion(id);
        if (dir == null) {
            return false;
        }

        List<SystemRegion> children = dir.getChildren();
        if (children != null) {
            for (SystemRegion childDir : children) {
                recoverSystemRegion(childDir.getId());
            }
        }
        dir.setStatus(Integer.valueOf(0));
        return true;
    }

    public boolean batchRemoveSystemRegions(List<Serializable> dirIds) {
        for (Serializable id : dirIds) {
            removeSystemRegion((Long) id);
        }
        return true;
    }

    public boolean delSystemRegion(Long id) {
        SystemRegion dir = getSystemRegion(id);
        if (dir == null) {
            return false;
        }

        List<SystemRegion> children = dir.getChildren();
        if (children != null) {
            for (SystemRegion childDir : children) {
                delSystemRegion(childDir.getId());
            }
        }
        dir.setStatus(Integer.valueOf(-1));
        return true;
    }

    public SystemRegion getSystemRegion(Long id) {
        SystemRegion dir = (SystemRegion) this.dirDao.get(id);
        return dir;
    }

    public IPageList querySystemRegion(IQueryObject conditions) {
        if (conditions == null) {
            conditions = new QueryObject();
        }
        return QueryUtil.query(conditions, SystemRegion.class, this.dirDao);
    }

    public boolean removeSystemRegion(Long id) {
        SystemRegion dir = getSystemRegion(id);
        if (dir == null) {
            return false;
        }

        List<SystemRegion> children = dir.getChildren();
        if (children != null) {
            for (SystemRegion childDir : children) {
                removeSystemRegion(childDir.getId());
            }
        }
        this.dirDao.remove(id);
        return true;
    }

    private void parseSystemRegion(SystemRegion dir) {
        dir.setLev(Integer.valueOf(dir.getParent() == null ? 1 : dir.getParent().getLev().intValue() + 1));
        dir.setPath((dir.getParent() != null ? dir.getParent().getPath() : "") + dir.getSn() + "@");
        dir.setSpell(String2SpellUtil.getBeginCharacter(dir.getTitle()));
    }

    public boolean updateSystemRegion(SystemRegion dir) {
        if (dir != null) {
            parseSystemRegion(dir);
            this.dirDao.update(dir);
            for (SystemRegion d : dir.getChildren()) {
                updateSystemRegion(d);
            }
        }
        return true;
    }

    public SystemRegion getSystemRegion(String sn, String value) {
        SystemRegion region = (SystemRegion) this.dirDao.getBy(sn, value);
        return region;
    }

    public SystemRegion getRegion(String title, String value) {
        SystemRegion region = (SystemRegion) this.dirDao.getBy(title, value);
        return region;
    }

    public IPageList getRootSystemRegions() {
        QueryObject queryObject = new QueryObject();
        queryObject.addQuery("obj.parent is EMPTY", null);
        queryObject.setPageSize(-1);
        return querySystemRegion(queryObject);
    }

    public SystemRegion getSystemRegionBySn(String sn) {
        return (SystemRegion) this.dirDao.getBy("sn", sn);
    }

    @Override
    public List<?> getGetSystemRegionByParentSn(String parentId) {
        String regionSql = "select id,title,sn from Disco_RegionInfo where parent_sn = '" + parentId + "'";
        List<?> list = this.dirDao.query(regionSql);
        return list;
    }
}
