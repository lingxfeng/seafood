package com.eastinno.otransos.cms.service.impl;
import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.ITemplateFileDAO;
import com.eastinno.otransos.cms.domain.TemplateFile;
import com.eastinno.otransos.cms.service.ITemplateFileService;
import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * TemplateFileServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class TemplateFileServiceImpl implements ITemplateFileService{
	@Resource
	private ITemplateFileDAO templateFileDao;
	
	public void setTemplateFileDao(ITemplateFileDAO templateFileDao){
		this.templateFileDao=templateFileDao;
	}
	
	public Long addTemplateFile(TemplateFile templateFile) {
		templateFile.setDirPath(templateFile.getDir().getPath());
		TenantObjectUtil.setObject(templateFile);
		this.templateFileDao.save(templateFile);
		if (templateFile.getId() != null) {
			return templateFile.getId();
		}
//		if (templateFile != null && templateFile.getId() != null) {
//			return templateFile.getId();
//		}
		return null;
	}
	
	public TemplateFile getTemplateFile(Long id) {
		TemplateFile templateFile = this.templateFileDao.get(id);
		if (!TenantObjectUtil.checkObjectService(templateFile)) {
            throw new LogicException("非法数据访问");
        }
		return templateFile;
		}
	
	public boolean delTemplateFile(Long id) {	
			TemplateFile templateFile = this.getTemplateFile(id);
			if (!TenantObjectUtil.checkObjectService(templateFile)) {
	            throw new LogicException("非法数据访问");
	        }
			String path = templateFile.getPath();
			File pathFile = new File(Globals.APP_BASE_DIR+path);
//			if (templateFile != null) {
				this.templateFileDao.remove(id);
				if(pathFile.exists()){
					pathFile.delete();
				}
				return true;
//			}			
//			return false;	
	}
	
	public boolean batchDelTemplateFiles(List<Serializable> templateFileIds) {
		
		for (Serializable id : templateFileIds) {
			delTemplateFile((Long) id);
		}
		return true;
	}
	
	public IPageList getTemplateFileBy(IQueryObject queryObj) {
		TenantObjectUtil.addQuery(queryObj);
		return this.templateFileDao.findBy(queryObj);		
	}
	
	public boolean updateTemplateFile(Long id, TemplateFile templateFile) {
		if (id != null) {
			templateFile.setId(id);
		} else {
			return false;
		}
		if (templateFile.getTenant() == null) {
            TenantObjectUtil.setObject(templateFile);
        }
		this.templateFileDao.update(templateFile);
		return true;
	}	
	
}
