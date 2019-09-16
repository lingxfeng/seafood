package com.eastinno.otransos.platform.weixin.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.ITimedTaskDAO;
import com.eastinno.otransos.platform.weixin.domain.TimedTask;
import com.eastinno.otransos.platform.weixin.service.ITimedTaskService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * TimedTaskServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class TimedTaskServiceImpl implements ITimedTaskService{
	@Resource
	private ITimedTaskDAO timedTaskDao;
	
	public void setTimedTaskDao(ITimedTaskDAO timedTaskDao){
		this.timedTaskDao=timedTaskDao;
	}
	
	public Long addTimedTask(TimedTask timedTask) {	
		this.timedTaskDao.save(timedTask);
		if (timedTask != null && timedTask.getId() != null) {
			return timedTask.getId();
		}
		return null;
	}
	
	public TimedTask getTimedTask(Long id) {
		TimedTask timedTask = this.timedTaskDao.get(id);
		return timedTask;
		}
	
	public boolean delTimedTask(Long id) {	
			TimedTask timedTask = this.getTimedTask(id);
			if (timedTask != null) {
				this.timedTaskDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelTimedTasks(List<Serializable> timedTaskIds) {
		
		for (Serializable id : timedTaskIds) {
			delTimedTask((Long) id);
		}
		return true;
	}
	
	public IPageList getTimedTaskBy(IQueryObject queryObj) {	
		return this.timedTaskDao.findBy(queryObj);		
	}
	
	public boolean updateTimedTask(Long id, TimedTask timedTask) {
		if (id != null) {
			timedTask.setId(id);
		} else {
			return false;
		}
		this.timedTaskDao.update(timedTask);
		return true;
	}	
	
}
