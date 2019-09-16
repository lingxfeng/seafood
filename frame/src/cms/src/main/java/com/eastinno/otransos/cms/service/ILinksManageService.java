package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.LinksManage;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * 友情链接业务层接口
 * @author nsz
 */
public interface ILinksManageService {
	/**
	 * 保存一个LinksManage，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addLinksManage(LinksManage domain);
	
	/**
	 * 根据一个ID得到LinksManage
	 * 
	 * @param id
	 * @return
	 */
	LinksManage getLinksManage(Long id);
	
	/**
	 * 删除一个LinksManage
	 * @param id
	 * @return
	 */
	boolean delLinksManage(Long id);
	
	/**
	 * 批量删除LinksManage
	 * @param ids
	 * @return
	 */
	boolean batchDelLinksManages(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到LinksManage
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getLinksManageBy(IQueryObject queryObj);
	
	/**
	  * 更新一个LinksManage
	  * @param id 需要更新的LinksManage的id
	  * @param dir 需要更新的LinksManage
	  */
	boolean updateLinksManage(Long id,LinksManage entity);
	/**
	 * 查询所有官网可用的
	 * @return
	 */
	List<Object> getShowLinksManage();
}
