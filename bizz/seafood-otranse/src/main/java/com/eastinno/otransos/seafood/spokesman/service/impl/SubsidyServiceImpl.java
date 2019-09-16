package com.eastinno.otransos.seafood.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanRating;
import com.eastinno.otransos.seafood.spokesman.domain.Subsidy;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanProductService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.seafood.spokesman.service.ISubsidyService;
import com.eastinno.otransos.seafood.spokesman.dao.ISubsidyDAO;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;


/**
 * SubsidyServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SubsidyServiceImpl implements ISubsidyService{
	@Resource
	private ISubsidyDAO subsidyDao;
	@Autowired
	private ISpokesmanService spokesmanService;
	@Autowired
	private ISpokesmanProductService spokesmanPoductService;
	@Autowired
	private IShopOrderInfoService shopOrderInfoService;
	@Autowired
	private ISpokesmanRatingService spokesmanRatingService;
	public ISpokesmanService getSpokesmanService() {
		return spokesmanService;
	}

	public void setSpokesmanService(ISpokesmanService spokesmanService) {
		this.spokesmanService = spokesmanService;
	}
	
	public ISpokesmanRatingService getSpokesmanRatingService() {
		return spokesmanRatingService;
	}

	public void setSpokesmanRatingService(
			ISpokesmanRatingService spokesmanRatingService) {
		this.spokesmanRatingService = spokesmanRatingService;
	}

	public void setSubsidyDao(ISubsidyDAO subsidyDao){
		this.subsidyDao=subsidyDao;
	}
	
	public Long addSubsidy(Subsidy subsidy) {	
		this.subsidyDao.save(subsidy);
		if (subsidy != null && subsidy.getId() != null) {
			return subsidy.getId();
		}
		return null;
	}
	
	public Subsidy getSubsidy(Long id) {
		Subsidy subsidy = this.subsidyDao.get(id);
		return subsidy;
		}
	
	public boolean delSubsidy(Long id) {	
			Subsidy subsidy = this.getSubsidy(id);
			if (subsidy != null) {
				this.subsidyDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSubsidys(List<Serializable> subsidyIds) {
		
		for (Serializable id : subsidyIds) {
			delSubsidy((Long) id);
		}
		return true;
	}
	
	public IPageList getSubsidyBy(IQueryObject queryObj) {	
		return this.subsidyDao.findBy(queryObj);		
	}
	
	public boolean updateSubsidy(Long id, Subsidy subsidy) {
		if (id != null) {
			subsidy.setId(id);
		} else {
			return false;
		}
		this.subsidyDao.update(subsidy);
		return true;
	}
	/**
	 * 先计算补贴
	 */
	public void calculateSubsidyFirst(ShopOrderInfo order){
		ShopMember member = order.getUser();
		Spokesman sman = member.getMySpokesman();
		Subsidy subsidy = new Subsidy();
		judgeSubsidy(null,sman,subsidy,order);
		Spokesman psman = this.spokesmanService.getRecursionSpokesman(sman);
		if(psman != null){
			judgeSubsidy(sman,psman,subsidy,order);
			Spokesman ppsman = this.spokesmanService.getRecursionSpokesman(psman);
			if(ppsman != null){
				judgeSubsidy(psman,ppsman,subsidy,order);
				Spokesman pppsman = this.spokesmanService.getRecursionSpokesman(ppsman);
				if(pppsman != null){
					judgeSubsidy(ppsman,pppsman,subsidy,order);
				}
			}	
		}
		subsidy.setOrderInfo(order);
		subsidy.setIsDispatch(Short.parseShort("0"));
		subsidy.setRestitution(subsidy.getRestitution1() + subsidy.getRestitution2() + subsidy.getRestitution3());
		if(subsidy.getLevel1() == null && subsidy.getLevel2() == null && subsidy.getLevel3() == null){
			System.out.println("==============本次订单不产生返现！级别不够");
		}else{
			addSubsidy(subsidy);
		}
		
		order.setIsCalculate(Short.parseShort("1"));//标注订单已进行补贴计算了（针对当前订单）
		order.setFinishSubsidy(Short.parseShort("0"));
		order.setSubsidyCount(order.getSubsidyCount() + 1);//补贴次数进行累加
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
	}
	/**
	 * 判断分级补贴
	 */
	public void judgeSubsidy(Spokesman spokesman,Spokesman pspokesman,Subsidy subsidy,ShopOrderInfo order){
		SpokesmanRating srating = null;
		if(spokesman != null){
			srating = spokesman.getSpokesmanRating();
		}
		SpokesmanRating psrating = null;
		if(pspokesman != null){
			psrating = pspokesman.getSpokesmanRating();
		}
		Double totalPrice = (double)order.getRestitution();
		List<ShopOrderdetail> list = order.getOrderdetails();
		int total = order.getTotalMonths();
		int num=0;
		if(list != null && list.size()!= 0){
			ShopOrderdetail orderdetail = list.get(0);
			num = orderdetail.getNum();
		}
		//声明等级和补贴比例
		Short rating = 0;
		Float leve = 0F;
		if(srating != null){
			rating = srating.getRating();
			leve = srating.getLeve();
		}
		if(psrating != null){
			Short prating = psrating.getRating();
			Float pleve = psrating.getLeve();
			switch(prating){
			case 0:{
				
			}
			break;
			case 1:{
				subsidy.setLevel1(pspokesman);
				if(order.getIsSpokesman() == 2){
					subsidy.setRestitution1((float)((pleve-leve)*totalPrice*num/100));
				}else if(order.getIsSpokesman() == 1){
					subsidy.setRestitution1((float)((pleve-leve)*totalPrice*(num/3)/100)*total);
				}
				
			}
			break;
			case 2:{
				subsidy.setLevel2(pspokesman);
				if(order.getIsSpokesman() == 2){
					subsidy.setRestitution2((float)((pleve-leve)*totalPrice*num/100));
				}else if(order.getIsSpokesman() == 1){
					subsidy.setRestitution2((float)((pleve-leve)*totalPrice*(num/3)/100)*total);
				}
			}
			break;
			case 3:{
				subsidy.setLevel3(pspokesman);
				if(order.getIsSpokesman() == 2){
					subsidy.setRestitution3((float)((pleve-leve)*totalPrice*num/100));
				}else if(order.getIsSpokesman() == 1){
					subsidy.setRestitution3((float)((pleve-leve)*totalPrice*(num/3)/100)*total);
				}
			}
			break;
				
			}
		}
	}
	/**
	 * 分配补贴
	 */
	public void disPatchSubsidy(ShopOrderInfo order){
		QueryObject qosub = new QueryObject();
		qosub.addQuery("obj.orderInfo.id",order.getId(),"=");
		qosub.addQuery("obj.isDispatch", Short.parseShort("0"),"=");
		List<Subsidy> listsub = this.subsidyDao.findBy(qosub).getResult();
		if(listsub != null && listsub.size() != 0){
			Subsidy subsidy = listsub.get(0);
			//分配补贴
			Spokesman sman = subsidy.getLevel1();
			if(sman != null){
				sman.setAvailableSubsidy(sman.getAvailableSubsidy() + subsidy.getRestitution1());
				sman.setTotalSubsidy(sman.getTotalSubsidy() + subsidy.getRestitution1());
				this.spokesmanService.updateSpokesman(sman.getId(), sman);
			}
			
			Spokesman psman = subsidy.getLevel2();
			if(psman != null){
				psman.setAvailableSubsidy(psman.getAvailableSubsidy() + subsidy.getRestitution2());
				psman.setTotalSubsidy(psman.getTotalSubsidy() + subsidy.getRestitution2());
				this.spokesmanService.updateSpokesman(psman.getId(), psman);
			}
			
			Spokesman ppsman = subsidy.getLevel3();
			if(ppsman != null){
				ppsman.setAvailableSubsidy(ppsman.getAvailableSubsidy() + subsidy.getRestitution3());
				ppsman.setTotalSubsidy(ppsman.getTotalSubsidy() + subsidy.getRestitution3());
				this.spokesmanService.updateSpokesman(ppsman.getId(), ppsman);
			}
			//更改记录状态
			subsidy.setIsDispatch(Short.parseShort("1"));
			updateSubsidy(subsidy.getId(),subsidy);
			//更改订单中的相关信息
			if(order.getIsSpokesman() == 1){
				order.setFinishSubsidy(Short.parseShort("1"));
			}else{
				
			}
			List<ShopOrderdetail> list = order.getOrderdetails();
			int total = 0;
			if(list != null && list.size()!= 0){
				ShopOrderdetail orderdetail = list.get(0);
				ShopProduct sp = orderdetail.getPro();
				QueryObject qo = new QueryObject();
				qo.addQuery("obj.product.id",sp.getId(),"=");
				 List<SpokesmanProduct> listpro = this.spokesmanPoductService.getSpokesmanProductBy(qo).getResult();
				 if(listpro != null && listpro.size()!= 0){
					 SpokesmanProduct spokesProduct = listpro.get(0);
					 total = spokesProduct.getTotalMonths();
				 }
			}
			
			if((order.getSubsidyCount() + 1) > total){
				order.setFinishSubsidy(Short.parseShort("1"));
			}else{
				order.setFinishSubsidy(Short.parseShort("0"));
			}
			
		}
		order.setIsCalculate(Short.parseShort("0"));//将订单改为未计算状态
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
	}

	public ISpokesmanProductService getSpokesmanPoductService() {
		return spokesmanPoductService;
	}

	public void setSpokesmanPoductService(
			ISpokesmanProductService spokesmanPoductService) {
		this.spokesmanPoductService = spokesmanPoductService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	
	
}
