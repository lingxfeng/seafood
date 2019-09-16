package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface ISystemRegionService {
    public abstract SystemRegion getSystemRegion(Long paramLong);

    public abstract boolean batchRecoverSystemRegions(List<Serializable> paramList);

    public abstract boolean batchDelSystemRegions(List<Serializable> paramList);

    public abstract Long addSystemRegion(SystemRegion paramSystemRegion);

    public abstract boolean delSystemRegion(Long paramLong);

    public abstract boolean updateSystemRegion(SystemRegion paramSystemRegion);

    public abstract IPageList querySystemRegion(IQueryObject paramIQueryObject);

    public abstract boolean removeSystemRegion(Long paramLong);

    public abstract boolean batchRemoveSystemRegions(List<Serializable> paramList);

    public abstract IPageList getRootSystemRegions();

    public abstract SystemRegion getSystemRegionBySn(String paramString);

    /**
     * 根据父SN查询子级地区信息
     * 
     * @param parentId
     * @return
     */
    public abstract List<?> getGetSystemRegionByParentSn(String parentSn);
}
