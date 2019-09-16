package com.eastinno.otransos.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ILinkImgTypeDAO;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * PPtTypeServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class LinkImgTypeServiceImpl implements ILinkImgTypeService {
    @Resource
    private ILinkImgTypeDAO linkImgTypeDAO;

    @Override
    public LinkImgType getLinkImgTypeByCode(String imgTypeCode) {
        return this.linkImgTypeDAO.getBy("code", imgTypeCode);

    }

    public ILinkImgTypeDAO getLinkImgTypeDAO() {
        return linkImgTypeDAO;
    }

    public void setLinkImgTypeDAO(ILinkImgTypeDAO linkImgTypeDAO) {
        this.linkImgTypeDAO = linkImgTypeDAO;
    }

    public Long addPPtType(LinkImgType pPtType) {
    	TenantObjectUtil.setObject(pPtType);
        this.linkImgTypeDAO.save(pPtType);
        if (pPtType != null && pPtType.getId() != null) {
            return pPtType.getId();
        }
        return null;
    }

    public LinkImgType getPPtType(Long id) {
        LinkImgType pPtType = this.linkImgTypeDAO.get(id);
        if (!TenantObjectUtil.checkObjectService(pPtType)) {
            throw new LogicException("非法数据访问");
        }
        return pPtType;
    }

    public boolean delPPtType(Long id) {
        LinkImgType pPtType = this.getPPtType(id);
        if (!TenantObjectUtil.checkObjectService(pPtType))
            throw new LogicException("非法数据访问");
        if (pPtType != null) {
            this.linkImgTypeDAO.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelPPtTypes(List<Serializable> pPtTypeIds) {

        for (Serializable id : pPtTypeIds) {
            delPPtType((Long) id);
        }
        return true;
    }

    public IPageList getPPtTypeBy(IQueryObject queryObj) {
    	TenantObjectUtil.addQuery(queryObj);
        return this.linkImgTypeDAO.findBy(queryObj);
    }

    public boolean updatePPtType(Long id, LinkImgType pPtType) {
        if (id != null) {
            pPtType.setId(id);
        } else {
            return false;
        }
        if (pPtType.getTenant() == null) {
            TenantObjectUtil.setObject(pPtType);
        }
        this.linkImgTypeDAO.update(pPtType);
        return true;
    }

}
