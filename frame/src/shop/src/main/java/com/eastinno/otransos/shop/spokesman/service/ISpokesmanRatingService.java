package com.eastinno.otransos.shop.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanRating;
/**
 * SpokesmanRatingService
 * @author ksmwly@gmail.com
 */
public interface ISpokesmanRatingService {
	/**
	 * 保存一个SpokesmanRating，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSpokesmanRating(SpokesmanRating domain);
	
	/**
	 * 根据一个ID得到SpokesmanRating
	 * 
	 * @param id
	 * @return
	 */
	SpokesmanRating getSpokesmanRating(Long id);
	
	/**
	 * 删除一个SpokesmanRating
	 * @param id
	 * @return
	 */
	boolean delSpokesmanRating(Long id);
	
	/**
	 * 批量删除SpokesmanRating
	 * @param ids
	 * @return
	 */
	boolean batchDelSpokesmanRatings(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SpokesmanRating
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSpokesmanRatingBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SpokesmanRating
	  * @param id 需要更新的SpokesmanRating的id
	  * @param dir 需要更新的SpokesmanRating
	  */
	boolean updateSpokesmanRating(Long id,SpokesmanRating entity);
	/**
	  * 判断代言人等级
	  * @param Spokesman
	  * 
	  */
	SpokesmanRating judgeRating(Spokesman spokesman);
}
