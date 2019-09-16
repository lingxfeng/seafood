package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IWxSignatureDAO;
import com.eastinno.otransos.platform.weixin.domain.WxSignature;
import com.eastinno.otransos.platform.weixin.service.IWxSignatureService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * WxSignatureServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class WxSignatureServiceImpl implements IWxSignatureService{
	@Resource
	private IWxSignatureDAO wxSignatureDao;
	
	public void setWxSignatureDao(IWxSignatureDAO wxSignatureDao){
		this.wxSignatureDao=wxSignatureDao;
	}
	
	public Long addWxSignature(WxSignature entity) {
		this.wxSignatureDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public WxSignature getWxSignature(Long id) {
		WxSignature entity = this.wxSignatureDao.get(id);
		return entity;
		}
	
	
	public IPageList getWxSignatureBy(IQueryObject queryObj) {	
		return this.wxSignatureDao.findBy(queryObj);	
	}
	
	public boolean updateWxSignature(Long id, WxSignature entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.wxSignatureDao.update(entity);
		return true;
	}

	@Override
	public boolean delWxSignature(Long id) {
		WxSignature entity = this.getWxSignature(id);
        if (entity != null) {
            this.wxSignatureDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelWxSignatures(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
