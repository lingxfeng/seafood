package com.eastinno.otransos.security.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.IResourceDAO;
import com.eastinno.otransos.security.service.IResourceService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ResourceServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ResourceServiceImpl implements IResourceService{
	@Resource
	private IResourceDAO resourceDao;
	
	public void setResourceDao(IResourceDAO resourceDao){
		this.resourceDao=resourceDao;
	}
	
	public Long addResource(com.eastinno.otransos.security.domain.Resource resource) {	
		this.resourceDao.save(resource);
		if (resource != null && resource.getId() != null) {
			return resource.getId();
		}
		return null;
	}
	
	public com.eastinno.otransos.security.domain.Resource getResource(Long id) {
	    com.eastinno.otransos.security.domain.Resource resource = this.resourceDao.get(id);
		return resource;
		}
	
	public boolean delResource(Long id) {	
	    com.eastinno.otransos.security.domain.Resource resource = this.getResource(id);
			if (resource != null) {
				this.resourceDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelResources(List<Serializable> resourceIds) {
		
		for (Serializable id : resourceIds) {
			delResource((Long) id);
		}
		return true;
	}
	
	public IPageList getResourceBy(IQueryObject queryObj) {	
		return this.resourceDao.findBy(queryObj);		
	}
	
	public boolean updateResource(Long id, com.eastinno.otransos.security.domain.Resource resource) {
		if (id != null) {
			resource.setId(id);
		} else {
			return false;
		}
		this.resourceDao.update(resource);
		return true;
	}	
	public com.eastinno.otransos.security.domain.Resource getResource(String resString) {
        if ((resString != null) && (!resString.equals(""))) {
            com.eastinno.otransos.security.domain.Resource r = (com.eastinno.otransos.security.domain.Resource) this.resourceDao.getBy("resStr", resString);
            return r;
        }
        return null;
    }
}
