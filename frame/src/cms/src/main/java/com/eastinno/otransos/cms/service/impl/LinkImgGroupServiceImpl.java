package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ILinkImgGroupDAO;
import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.query.LinkImgGroupQuery;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * LinkImgGroupServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class LinkImgGroupServiceImpl implements ILinkImgGroupService {
    @Resource
    private ILinkImgGroupDAO linkImgGroupDAO;

    public void setLinkImgGroupDAO(ILinkImgGroupDAO linkImgGroupDAO) {
        this.linkImgGroupDAO = linkImgGroupDAO;
    }

    public Long addLinkImgGroup(LinkImgGroup linkImgGroup) {
        TenantObjectUtil.setObject(linkImgGroup);
        this.linkImgGroupDAO.save(linkImgGroup);
        if (linkImgGroup != null && linkImgGroup.getId() != null) {
            return linkImgGroup.getId();
        }
        return null;
    }

    public LinkImgGroup getLinkImgGroup(Long id) {
        LinkImgGroup linkImgGroup = this.linkImgGroupDAO.get(id);
        if (!TenantObjectUtil.checkObjectService(linkImgGroup)) {
            throw new LogicException("非法数据访问");
        }
        return linkImgGroup;
    }

    public boolean delLinkImgGroup(Long id) {
        LinkImgGroup linkImgGroup = this.getLinkImgGroup(id);
        if (!TenantObjectUtil.checkObjectService(linkImgGroup))
            throw new LogicException("非法数据访问");
        if (linkImgGroup != null) {
            this.linkImgGroupDAO.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelLinkImgGroups(List<Serializable> linkImgGroupIds) {
        for (Serializable id : linkImgGroupIds) {
            delLinkImgGroup((Long) id);
        }
        return true;
    }

    public IPageList getLinkImgGroupBy(IQueryObject queryObj) {
        TenantObjectUtil.addQuery(queryObj);
        return this.linkImgGroupDAO.findBy(queryObj);
    }

    public boolean updateLinkImgGroup(Long id, LinkImgGroup linkImgGroup) {
        if (id != null) {
            linkImgGroup.setId(id);
        } else {
            return false;
        }
        if (linkImgGroup.getTenant() == null) {
            TenantObjectUtil.setObject(linkImgGroup);
        }
        this.linkImgGroupDAO.update(linkImgGroup);
        return true;
    }

    @Override
    public List<LinkImgGroup> getLinkImgGroupByType(LinkImgType systemDictionaryDetail) {
        LinkImgGroupQuery qo = new LinkImgGroupQuery();
        qo.setType(systemDictionaryDetail);
        TenantObjectUtil.addQuery(qo);
        IPageList pl = this.linkImgGroupDAO.findBy(qo);
        return pl.getResult();
    }

    @Override
    public LinkImgGroup getLinkImgGroupBy(String code, Integer sequence) {
        String jpql = "from " + LinkImgGroup.class.getName() + " obj where obj.type.code=? and obj.sequence=?";
        LinkImgGroup imgGroup = (LinkImgGroup) this.linkImgGroupDAO.getSingleResult(jpql, new Object[] {code, sequence});
        return imgGroup;
    }
}
