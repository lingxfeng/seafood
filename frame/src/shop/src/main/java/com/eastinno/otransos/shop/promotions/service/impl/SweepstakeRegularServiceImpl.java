package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeRegularService;
import com.eastinno.otransos.shop.promotions.dao.ISweepstakeRegularDAO;


/**
 * SweepstakeRegularServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SweepstakeRegularServiceImpl implements ISweepstakeRegularService{
	@Resource
	private ISweepstakeRegularDAO sweepstakeRegularDao;
	
	public void setSweepstakeRegularDao(ISweepstakeRegularDAO sweepstakeRegularDao){
		this.sweepstakeRegularDao=sweepstakeRegularDao;
	}
	
	public Long addSweepstakeRegular(SweepstakeRegular sweepstakeRegular) {	
		this.sweepstakeRegularDao.save(sweepstakeRegular);
		if (sweepstakeRegular != null && sweepstakeRegular.getId() != null) {
			return sweepstakeRegular.getId();
		}
		return null;
	}
	
	public SweepstakeRegular getSweepstakeRegular(Long id) {
		SweepstakeRegular sweepstakeRegular = this.sweepstakeRegularDao.get(id);
		return sweepstakeRegular;
		}
	
	public boolean delSweepstakeRegular(Long id) {	
			SweepstakeRegular sweepstakeRegular = this.getSweepstakeRegular(id);
			if (sweepstakeRegular != null) {
				this.sweepstakeRegularDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSweepstakeRegulars(List<Serializable> sweepstakeRegularIds) {
		
		for (Serializable id : sweepstakeRegularIds) {
			delSweepstakeRegular((Long) id);
		}
		return true;
	}
	
	public IPageList getSweepstakeRegularBy(IQueryObject queryObj) {	
		return this.sweepstakeRegularDao.findBy(queryObj);		
	}
	
	public boolean updateSweepstakeRegular(Long id, SweepstakeRegular sweepstakeRegular) {
		if (id != null) {
			sweepstakeRegular.setId(id);
		} else {
			return false;
		}
		this.sweepstakeRegularDao.update(sweepstakeRegular);
		return true;
	}
	/**
	 * 获取中奖信息
	 */
	public int checkSweepstake(int basemin){
		int finall=0;//中奖位置
		int temp=0;
		int c=0;
		int line=0;
		Random random = new Random();
		int result = random.nextInt(basemin);
		QueryObject qo = new QueryObject();
		List<SweepstakeRegular> list = this.sweepstakeRegularDao.findBy(qo).getResult();
		for(SweepstakeRegular sr:list){
			c=sr.getRating();
			temp = temp + c;
			line = basemin - temp;
			if (c != 0) {
				if (result > line && result <= (line + c)) {
					finall = sr.getPosition();
					break;
				}
			}
		}
		
		return finall;
		
	}
	/**
	 * 获取转盘角度
	 */
	public int returnangle(int position){
		int r=0;
		switch (position) {
		case 0://
			r = 0;
			break;
		case 1://
			r = randomNum(345, 375);
			break;
		case 2://
			r = randomNum(375, 405);			
			break;
		case 3://
			r = randomNum(405, 435);			
			break;
		case 4://
			r = randomNum(435, 465);			
			break;
		case 5://
			r = randomNum(465, 495);			
			break;
		case 6://
			r = randomNum(495, 525);			
			break;
		case 7://
			r = randomNum(525, 555);			
			break;
		case 8://
			r = randomNum(555, 585);
			break;
		case 9://
			r = randomNum(585, 615);
			
			break;
		case 10://
			r = randomNum(615, 645);
			break;
		case 11://
			r = randomNum(645, 675);				
			break;
		case 12:// 
			r = randomNum(675, 705);
			break;
		}
		return r;
		
	}
	public int randomNum(int min,int max){
		int Range = max - min;
		Double Rand = Math.random();
		return (int) (min + Math.round(Rand * Range));
	}
		
}
