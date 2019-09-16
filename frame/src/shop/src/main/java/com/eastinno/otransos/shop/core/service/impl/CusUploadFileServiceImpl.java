package com.eastinno.otransos.shop.core.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.core.dao.ICusUploadFileDAO;
import com.eastinno.otransos.shop.core.domain.CusUploadFile;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * CusUploadFileServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CusUploadFileServiceImpl implements ICusUploadFileService{
	@Resource
	private ICusUploadFileDAO cusUploadFileDao;
	
	public void setCusUploadFileDao(ICusUploadFileDAO cusUploadFileDao){
		this.cusUploadFileDao=cusUploadFileDao;
	}
	
	public Long addCusUploadFile(CusUploadFile cusUploadFile) {	
		this.cusUploadFileDao.save(cusUploadFile);
		if (cusUploadFile != null && cusUploadFile.getId() != null) {
			return cusUploadFile.getId();
		}
		return null;
	}
	
	public CusUploadFile getCusUploadFile(Long id) {
		CusUploadFile cusUploadFile = this.cusUploadFileDao.get(id);
		return cusUploadFile;
		}
	
	public boolean delCusUploadFile(Long id) {	
			CusUploadFile cusUploadFile = this.getCusUploadFile(id);
			if (cusUploadFile != null) {
				this.cusUploadFileDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCusUploadFiles(List<Serializable> cusUploadFileIds) {
		
		for (Serializable id : cusUploadFileIds) {
			delCusUploadFile((Long) id);
		}
		return true;
	}
	
	public IPageList getCusUploadFileBy(IQueryObject queryObj) {	
		return this.cusUploadFileDao.findBy(queryObj);		
	}
	
	public boolean updateCusUploadFile(Long id, CusUploadFile cusUploadFile) {
		if (id != null) {
			cusUploadFile.setId(id);
		} else {
			return false;
		}
		this.cusUploadFileDao.update(cusUploadFile);
		return true;
	}

	@Override
	public Long addCusUploadFile(String filePath) {
		if(!"".equals(filePath)){
			CusUploadFile cusUploadFile = new CusUploadFile();
			cusUploadFile.setImgPath(filePath);
			this.cusUploadFileDao.save(cusUploadFile);
			if (cusUploadFile != null && cusUploadFile.getId() != null) {
				return cusUploadFile.getId();
			}
		}
		return null;
	}

	@Override
	public boolean delCusUploadFile(String filePath) {
		if(!"".equals(filePath)){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.imgPath",filePath,"=");
			List<CusUploadFile> objs = this.cusUploadFileDao.findBy(qo).getResult();
			if(objs!=null){
				this.cusUploadFileDao.delete(objs.get(0));
			}
			DiscoShopUtil.deleteFile(filePath);
			return true;
		}
		return false;
	}	
	
}
