package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.WxSignature;
import com.eastinno.otransos.web.tools.IPageList;


public interface IWxSignatureService {
	/**
	 * 保存一个WxSignature，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addWxSignature(WxSignature domain);
	
	/**
	 * 根据一个ID得到WxSignature
	 * 
	 * @param id
	 * @return
	 */
	WxSignature getWxSignature(Long id);
	
	/**
	 * 删除一个WxSignature
	 * @param id
	 * @return
	 */
	boolean delWxSignature(Long id);
	
	/**
	 * 批量删除WxSignature
	 * @param ids
	 * @return
	 */
	boolean batchDelWxSignatures(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到WxSignature
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getWxSignatureBy(IQueryObject queryObj);
	
	/**
	  * 更新一个WxSignature
	  * @param id 需要更新的WxSignature的idO
	  * @param dir 需要更新的WxSignature
	  */
	boolean updateWxSignature(Long id,WxSignature entity);
}
