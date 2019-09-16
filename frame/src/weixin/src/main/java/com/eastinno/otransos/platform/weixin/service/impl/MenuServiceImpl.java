package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IMenuDAO;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.platform.weixin.service.IMenuService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * MenuServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class MenuServiceImpl implements IMenuService{
	@Resource
	private IMenuDAO menuDao;
	
	public void setmenuDao(IMenuDAO menuDao){
		this.menuDao=menuDao;
	}
	
	public Long addMenu(Menu entity) {
		this.menuDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public Menu getMenu(Long id) {
		Menu entity = this.menuDao.get(id);
		return entity;
		}
	
	
	public IPageList getMenuBy(IQueryObject queryObj) {	
		return this.menuDao.findBy(queryObj);	
	}
	
	public boolean updateMenu(Long id, Menu entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.menuDao.update(entity);
		return true;
	}

	@Override
	public boolean delMenu(Long id) {
		Menu entity = this.getMenu(id);
        if (entity != null) {
            this.menuDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelMenus(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
