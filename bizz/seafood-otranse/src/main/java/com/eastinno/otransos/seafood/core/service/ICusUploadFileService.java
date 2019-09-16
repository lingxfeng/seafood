package com.eastinno.otransos.seafood.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.core.domain.CusUploadFile;
/**
 * CusUploadFileService
 * @author ksmwly@gmail.com
 */
public interface ICusUploadFileService {
	/**
	 * 保存一个CusUploadFile，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCusUploadFile(CusUploadFile domain);
	Long addCusUploadFile(String filePath);
	/**
	 * 根据一个ID得到CusUploadFile
	 * 
	 * @param id
	 * @return
	 */
	CusUploadFile getCusUploadFile(Long id);
	
	/**
	 * 删除一个CusUploadFile
	 * @param id
	 * @return
	 */
	boolean delCusUploadFile(Long id);
	boolean delCusUploadFile(String filePath);
	
	/**
	 * 批量删除CusUploadFile
	 * @param ids
	 * @return
	 */
	boolean batchDelCusUploadFiles(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到CusUploadFile
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCusUploadFileBy(IQueryObject queryObj);
	
	/**
	  * 更新一个CusUploadFile
	  * @param id 需要更新的CusUploadFile的id
	  * @param dir 需要更新的CusUploadFile
	  */
	boolean updateCusUploadFile(Long id,CusUploadFile entity);
}
