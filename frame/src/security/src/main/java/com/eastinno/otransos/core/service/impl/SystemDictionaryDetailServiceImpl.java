package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.dao.ISystemDictionaryDetailDAO;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryDetailService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class SystemDictionaryDetailServiceImpl implements ISystemDictionaryDetailService {
    @Resource
    private ISystemDictionaryDetailDAO systemDictionaryDetailDao;

    public void setSystemDictionaryDetailDao(ISystemDictionaryDetailDAO systemDictionaryDetailDao) {
        this.systemDictionaryDetailDao = systemDictionaryDetailDao;
    }

    public Long addSystemDictionaryDetail(SystemDictionaryDetail systemDictionaryDetail) {
        if (systemDictionaryDetail.getSequence() == null) {
            QueryObject qo = new QueryObject();
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            qo.setPageSize(Integer.valueOf(1));
            qo.addQuery("obj.parent", systemDictionaryDetail.getParent(), "=");
            List list = getSystemDictionaryDetailBy(qo).getResult();
            if ((list != null) && (list.size() > 0))
                systemDictionaryDetail.setSequence(Integer.valueOf(((SystemDictionaryDetail) list.get(0)).getSequence().intValue() + 1));
            else
                systemDictionaryDetail.setSequence(Integer.valueOf(1));
        }
        this.systemDictionaryDetailDao.save(systemDictionaryDetail);
        if ((systemDictionaryDetail != null) && (systemDictionaryDetail.getId() != null)) {
            return systemDictionaryDetail.getId();
        }
        return null;
    }

    public SystemDictionaryDetail getSystemDictionaryDetail(Long id) {
        SystemDictionaryDetail systemDictionaryDetail = (SystemDictionaryDetail) this.systemDictionaryDetailDao.get(id);
        if (systemDictionaryDetail != null) {
            return systemDictionaryDetail;
        }
        return null;
    }

    public boolean delSystemDictionaryDetail(Long id) {
        if (id != null) {
            this.systemDictionaryDetailDao.remove(id);
            SystemDictionaryDetail systemDictionaryDetail = getSystemDictionaryDetail(id);
            if (systemDictionaryDetail == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean batchDelSystemDictionaryDetails(List<Serializable> systemDictionaryDetailIds) {
        for (Serializable id : systemDictionaryDetailIds) {
            delSystemDictionaryDetail((Long) id);
        }
        return true;
    }

    public IPageList getSystemDictionaryDetailBy(IQueryObject properties) {
        if (properties == null) {
            properties = new QueryObject();
        }
        return QueryUtil.query(properties, SystemDictionaryDetail.class, this.systemDictionaryDetailDao);
    }

    public boolean updateSystemDictionaryDetail(SystemDictionaryDetail systemDictionaryDetail) {
        if (systemDictionaryDetail != null) {
            this.systemDictionaryDetailDao.update(systemDictionaryDetail);
        }
        return true;
    }

    public List<SystemDictionaryDetail> getDetailsByDictionarySn(String sn) {
        QueryObject qo = new QueryObject();
        qo.addQuery("parent.sn", sn, "=");
        qo.setOrderBy("sequence");
        return getSystemDictionaryDetailBy(qo).getResult();
    }
}
