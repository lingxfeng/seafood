package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.ITemplateDAO;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.service.ITemplateService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * TemplateServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class TemplateServiceImpl implements ITemplateService{
	@Resource
	private ITemplateDAO templateDao;
	
	public void settemplateDao(ITemplateDAO templateDao){
		this.templateDao=templateDao;
	}
	
	public Long addTemplate(Template entity) {
		this.templateDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public Template getTemplate(Long id) {
		Template entity = this.templateDao.get(id);
		return entity;
		}
	
	
	public IPageList getTemplateBy(IQueryObject queryObj) {	
		return this.templateDao.findBy(queryObj);	
	}
	
	public boolean updateTemplate(Long id, Template entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.templateDao.update(entity);
		return true;
	}

	@Override
	public boolean delTemplate(Long id) {
		Template entity = this.getTemplate(id);
        if (entity != null) {
            this.templateDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelTemplates(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
