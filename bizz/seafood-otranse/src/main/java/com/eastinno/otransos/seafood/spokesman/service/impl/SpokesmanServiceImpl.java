package com.eastinno.otransos.seafood.spokesman.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.seafood.spokesman.dao.ISpokesmanDAO;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;


/**
 * SpokesmanServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SpokesmanServiceImpl implements ISpokesmanService{
	@Resource
	private ISpokesmanDAO spokesmanDao;
	@Autowired
	private ISpokesmanRatingService spokesmanRatingService;
	public void setSpokesmanDao(ISpokesmanDAO spokesmanDao){
		this.spokesmanDao=spokesmanDao;
	}
	
	public Long addSpokesman(Spokesman spokesman) {	
		this.spokesmanDao.save(spokesman);
		if (spokesman != null && spokesman.getId() != null) {
			return spokesman.getId();
		}
		return null;
	}
	
	public Spokesman getSpokesman(Long id) {
		Spokesman spokesman = this.spokesmanDao.get(id);
		return spokesman;
		}
	
	public boolean delSpokesman(Long id) {	
			Spokesman spokesman = this.getSpokesman(id);
			if (spokesman != null) {
				this.spokesmanDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSpokesmans(List<Serializable> spokesmanIds) {
		
		for (Serializable id : spokesmanIds) {
			delSpokesman((Long) id);
		}
		return true;
	}
	
	public IPageList getSpokesmanBy(IQueryObject queryObj) {	
		return this.spokesmanDao.findBy(queryObj);		
	}
	
	public boolean updateSpokesman(Long id, Spokesman spokesman) {
		if (id != null) {
			spokesman.setId(id);
		} else {
			return false;
		}
		this.spokesmanDao.update(spokesman);
		return true;
	}	
	public Map getSubsiSpokesman(Spokesman sman) {
		Map map = new HashMap<>();
		//第一级极差人
		Spokesman psman = getRecursionSpokesman(sman);
		if(psman != null){
			map.put(psman.getSpokesmanRating().getLeve(),psman);
			//获取第二级极差人
			Spokesman ppsman = getRecursionSpokesman(psman);
			if(ppsman != null){
				map.put(ppsman.getSpokesmanRating().getLeve(),ppsman);
				//获取第三 级极差人
				Spokesman pppsman = getRecursionSpokesman(ppsman);
				if(pppsman != null){
					map.put(pppsman.getSpokesmanRating().getLeve(),pppsman);
				}
			}
		}
		return map;
	}
	/**
	 * 递归获取极差上级
	 * @param sman
	 * @return
	 */
	public Spokesman getRecursionSpokesman(Spokesman sman) {
		Spokesman finalman = null;
		Short rating = sman.getSpokesmanRating().getRating();
		if(rating != 3){
			Spokesman psman = sman.getPspokesman();
			if(psman != null){
				Short prating = psman.getSpokesmanRating().getRating();
				if(prating > rating){
					finalman = psman;
				}else{
					finalman = getRecursionSpokesman(psman);
				}
			}
		}
			
		return finalman;
		
	}
	/**
	 * 计算团队业绩累计 
	 */
	public void calculateTeamAccount(ShopOrderInfo order){
		ShopMember member = order.getUser();
		Spokesman sman = member.getMySpokesman();
		Double totalprice = order.getProduct_price();
		String depath = sman.getDePath();
		String idstr ="0";
		String a[] =depath.split("@");
		for(int i=1; i<a.length; i++){
			 idstr = idstr + ","+ a[i];
		 }
		//低效方法
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.id in ("+idstr+")");
		qo.addQuery("obj.status",Short.parseShort("1"),"=");
		qo.setPageSize(-1);
		List<Spokesman> list = this.spokesmanDao.findBy(qo).getResult();
		if(list != null && list.size() != 0){
			for(Spokesman man:list){
				man.setTeamAmount((float)(man.getTeamAmount() + totalprice));//累计业绩
				man.setSpokesmanRating(this.spokesmanRatingService.judgeRating(man));
				this.spokesmanDao.update(man);
			}
		}
		//优化方法
		//String SpokesmanSql = "update Disco_Shop_Spokesman t set t.teamAmount= ((select t1.teamAmount from Disco_Shop_Spokesman where t1.id = t.id) + "+totalprice+" )where t.dePath like '%@"+sman.getId()+"' and t.status = 1";
		//String SpokesmanSql = "update Disco_Shop_Spokesman t set t.teamAmount= (t.teamAmount + "+totalprice+" )where t.dePath like '%@"+sman.getId()+"' and t.status = 1";
		//int result = this.spokesmanDao.executeNativeSQL(SpokesmanSql);
		//System.out.println(result);
 		
	}
	/**
	 * 
	 * @param code
	 * @param sequence
	 * @return
	 */
	@Override
    public List<ShopOrderInfo> getTeamOrderInfo(Long spokesmanid) {
		Spokesman sman = this.spokesmanDao.findOne(spokesmanid);
        String jpql = "select * from Disco_Shop_ShopOrderInfo t where t.user_id in(select t2.id from Disco_Shop_ShopMember t2 where t2.id = (select t1.member_id from Disco_Shop_Spokesman t1 where t1.dePath like '"+sman.getDePath()+"%')) and (t.status = 1 or t.status = 2 or t.status = 3)";
        List<ShopOrderInfo>list1 = (List<ShopOrderInfo>) this.spokesmanDao.queryBySql(jpql);
        //System.out.println(((ShopOrderInfo)list1.get(0)).getName());
//        List<ShopOrderInfo> list = null;
//        for(Object order:list1){
//        	ShopOrderInfo order1=null;
//        	order1=(ShopOrderInfo) order;
//        	list.add(order1);
//        }

        return list1;
    }
}
