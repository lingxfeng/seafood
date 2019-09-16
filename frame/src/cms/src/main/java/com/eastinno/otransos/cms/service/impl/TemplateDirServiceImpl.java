package com.eastinno.otransos.cms.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ITemplateDirDAO;
import com.eastinno.otransos.cms.domain.TemplateDir;
import com.eastinno.otransos.cms.service.ITemplateDirService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * TemplateDirServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class TemplateDirServiceImpl implements ITemplateDirService{
	@Resource
	private ITemplateDirDAO templateDirDao;
	
	public void setTemplateDirDao(ITemplateDirDAO templateDirDao){
		this.templateDirDao=templateDirDao ;
	}
	
	public Long addTemplateDir(TemplateDir templateDir) {
		if(templateDir.getParent()!=null){
			templateDir.setPath(templateDir.getParent().getPath()+templateDir.getSn()+"@");
		}else{
			templateDir.setPath(templateDir.getSn()+"@");
		}
		TenantObjectUtil.setObject(templateDir);
		this.templateDirDao.save(templateDir);
//		if (templateDir != null && templateDir.getId() != null) {
//			return templateDir.getId();
//		}
		if (templateDir.getId() != null) {
			return templateDir.getId();
		}
		return null;
	}
	
	public TemplateDir getTemplateDir(Long id) {
		TemplateDir templateDir = this.templateDirDao.get(id);
		if (!TenantObjectUtil.checkObjectService(templateDir)) {
            throw new LogicException("非法数据访问");
        }
		return templateDir;
		}
	
	public boolean delTemplateDir(Long id) {	
			TemplateDir templateDir = this.getTemplateDir(id);
			if (!TenantObjectUtil.checkObjectService(templateDir)) {
	            throw new LogicException("非法数据访问");
	        }
			if (templateDir != null) {
				this.templateDirDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelTemplateDirs(List<Serializable> templateDirIds) {
		
		for (Serializable id : templateDirIds) {
			delTemplateDir((Long) id);
		}
		return true;
	}
	
	public IPageList getTemplateDirBy(IQueryObject queryObj) {
		TenantObjectUtil.addQuery(queryObj);
		return this.templateDirDao.findBy(queryObj);		
	}
	
	public boolean updateTemplateDir(Long id, TemplateDir templateDir) {
		if (id != null) {
			templateDir.setId(id);
		} else {
			return false;
		}
		 if (templateDir.getTenant() == null) {
	            TenantObjectUtil.setObject(templateDir);
	        }
		this.templateDirDao.update(templateDir);
		return true;
	}	
	
}
