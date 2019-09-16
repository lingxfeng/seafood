package com.eastinno.otransos.seafood.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.seafood.spokesman.dao.ISpokesmanRatingDAO;


/**
 * SpokesmanRatingServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SpokesmanRatingServiceImpl implements ISpokesmanRatingService{
	@Resource
	private ISpokesmanRatingDAO spokesmanRatingDao;
	
	public void setSpokesmanRatingDao(ISpokesmanRatingDAO spokesmanRatingDao){
		this.spokesmanRatingDao=spokesmanRatingDao;
	}
	
	public Long addSpokesmanRating(SpokesmanRating spokesmanRating) {	
		this.spokesmanRatingDao.save(spokesmanRating);
		if (spokesmanRating != null && spokesmanRating.getId() != null) {
			return spokesmanRating.getId();
		}
		return null;
	}
	
	public SpokesmanRating getSpokesmanRating(Long id) {
		SpokesmanRating spokesmanRating = this.spokesmanRatingDao.get(id);
		return spokesmanRating;
		}
	
	public boolean delSpokesmanRating(Long id) {	
			SpokesmanRating spokesmanRating = this.getSpokesmanRating(id);
			if (spokesmanRating != null) {
				this.spokesmanRatingDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSpokesmanRatings(List<Serializable> spokesmanRatingIds) {
		
		for (Serializable id : spokesmanRatingIds) {
			delSpokesmanRating((Long) id);
		}
		return true;
	}
	
	public IPageList getSpokesmanRatingBy(IQueryObject queryObj) {	
		return this.spokesmanRatingDao.findBy(queryObj);		
	}
	
	public boolean updateSpokesmanRating(Long id, SpokesmanRating spokesmanRating) {
		if (id != null) {
			spokesmanRating.setId(id);
		} else {
			return false;
		}
		this.spokesmanRatingDao.update(spokesmanRating);
		return true;
	}
	/**
	 * 判断代言人等级
	 */
	public SpokesmanRating judgeRating(Spokesman spokesman){
		SpokesmanRating spokesmanRating = null;
		if(spokesman.getCustomRating() ==1){
			spokesmanRating = spokesman.getSpokesmanRating();
		}else{
			QueryObject qo = new QueryObject();
			qo.addQuery("conditionf", spokesman.getTeamAmount(), "<=");
			qo.setOrderBy("conditionf");
			qo.setOrderType("desc");
			List<SpokesmanRating> list = this.spokesmanRatingDao.findBy(qo).getResult();
			if(list!=null && list.size()!=0){
				spokesmanRating =list.get(0);
			}
		}
		return spokesmanRating;
	}
}
