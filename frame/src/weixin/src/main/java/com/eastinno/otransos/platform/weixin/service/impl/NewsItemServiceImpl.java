package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.INewsItemDAO;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.service.INewsItemService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * NewsItemServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class NewsItemServiceImpl implements INewsItemService{
	@Resource
	private INewsItemDAO newsItemDao;
	
	public void setnewsItemDao(INewsItemDAO newsItemDao){
		this.newsItemDao=newsItemDao;
	}
	
	public Long addNewsItem(NewsItem entity) {
		this.newsItemDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public NewsItem getNewsItem(Long id) {
		NewsItem entity = this.newsItemDao.get(id);
		return entity;
		}
	
	
	public IPageList getNewsItemBy(IQueryObject queryObj) {	
		return this.newsItemDao.findBy(queryObj);	
	}
	
	public boolean updateNewsItem(Long id, NewsItem entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.newsItemDao.update(entity);
		return true;
	}

	@Override
	public boolean delNewsItem(Long id) {
		NewsItem entity = this.getNewsItem(id);
        if (entity != null) {
            this.newsItemDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelNewsItems(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
