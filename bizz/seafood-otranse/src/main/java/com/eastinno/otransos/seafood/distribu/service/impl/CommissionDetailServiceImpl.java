package com.eastinno.otransos.seafood.distribu.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.distribu.domain.CommissionDetail;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.ICommissionDetailService;
import com.eastinno.otransos.seafood.distribu.dao.ICommissionDetailDAO;
import com.eastinno.otransos.seafood.distribu.dao.IShopDistributorDAO;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.dao.IRemainderAmtHistoryDAO;
import com.eastinno.otransos.seafood.usercenter.dao.IShopMemberDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;


/**
 * CommissionDetailServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CommissionDetailServiceImpl implements ICommissionDetailService{
	@Resource
	private ICommissionDetailDAO commissionDetailDao;
	
	@Resource
	private IShopMemberService shopMemberService;
	
	@Resource
	private IShopDistributorDAO shopDistributorDAO;
	
	@Resource
	private IRemainderAmtHistoryService remainderAmtHistoryService;
	
	public void setCommissionDetailDao(ICommissionDetailDAO commissionDetailDao){
		this.commissionDetailDao=commissionDetailDao;
	}

	public void setShopDistributorDAO(IShopDistributorDAO shopDistributorDAO) {
		this.shopDistributorDAO = shopDistributorDAO;
	}
	
	public void setRemainderAmtHistoryService(
			IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}

	public Long addCommissionDetail(CommissionDetail commissionDetail) {	
		this.commissionDetailDao.save(commissionDetail);
		if (commissionDetail != null && commissionDetail.getId() != null) {
			return commissionDetail.getId();
		}
		return null;
	}
	
	public CommissionDetail getCommissionDetail(Long id) {
		CommissionDetail commissionDetail = this.commissionDetailDao.get(id);
		return commissionDetail;
		}
	
	public boolean delCommissionDetail(Long id) {	
			CommissionDetail commissionDetail = this.getCommissionDetail(id);
			if (commissionDetail != null) {
				this.commissionDetailDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCommissionDetails(List<Serializable> commissionDetailIds) {
		
		for (Serializable id : commissionDetailIds) {
			delCommissionDetail((Long) id);
		}
		return true;
	}
	
	public IPageList getCommissionDetailBy(IQueryObject queryObj) {	
		return this.commissionDetailDao.findBy(queryObj);		
	}
	
	public boolean updateCommissionDetail(Long id, CommissionDetail commissionDetail) {
		if (id != null) {
			commissionDetail.setId(id);
		} else {
			return false;
		}
		this.commissionDetailDao.update(commissionDetail);
		return true;
	}

	@Override
	public boolean returnCommissionDetail(ShopMember member,CommissionDetail commissionDetail) {
		
		if(member.getDisType()==0){
			ShopDistributor balanceonedistri=commissionDetail.getBalanceonedistri();//一级利润--店铺
			if(balanceonedistri!=null){
				//一级利润用户 扣除余额
				ShopMember balanceoneMember=balanceonedistri.getMember();
				this.shopMemberService.updateShopMemberAmt(balanceoneMember, commissionDetail.getBalanceone());
				this.remainderAmtHistoryService.addRemainderAmtHistory(balanceoneMember, 4, -commissionDetail.getBalanceone(), member.getNickname()+"退款退货，扣除差额利润");
			}
		}
		ShopDistributor levelonedistri=commissionDetail.getLevelonedistri();//一级佣金--店铺
		if(levelonedistri!=null){
			//一级佣金用户  扣除金额
			ShopMember leveloneMember=levelonedistri.getMember();
			this.shopMemberService.updateShopMemberAmt(leveloneMember, commissionDetail.getLevelonecommission());
			this.remainderAmtHistoryService.addRemainderAmtHistory(leveloneMember, 4, -commissionDetail.getLevelonecommission(), member.getNickname()+"退款退货，扣除一级佣金");
			
			ShopDistributor leveltowdistri=commissionDetail.getLeveltowdistri();
			if(leveltowdistri!=null){
				//二级佣金用户 扣除金额
				ShopMember leveltowMember=leveltowdistri.getMember();
				this.shopMemberService.updateShopMemberAmt(leveltowMember, commissionDetail.getLeveltowcommission());
				this.remainderAmtHistoryService.addRemainderAmtHistory(leveltowMember, 4, -commissionDetail.getLeveltowcommission(), member.getNickname()+"退款退货，扣除二级佣金");
			}
		}
		ShopDistributor balancetowdistri=commissionDetail.getBalancetowdistri();//二级利润--店铺
		if(balancetowdistri!=null){
			//二级利润 扣除金额
			ShopMember  balancetowMember=balancetowdistri.getMember();
			this.shopMemberService.updateShopMemberAmt(balancetowMember, commissionDetail.getBalancetow());
			this.remainderAmtHistoryService.addRemainderAmtHistory(balancetowMember, 4, -commissionDetail.getBalancetow(), member.getNickname()+"退款退货，扣除差额利润");
		}
		commissionDetail.setStatus(Short.valueOf("2"));
		this.commissionDetailDao.update(commissionDetail);
		return true;
	}

	@Override
	public CommissionDetail getCommissionDetail(String name,ShopOrderInfo shopOrderInfo) {
		CommissionDetail commissionDetail = this.commissionDetailDao.getBy(name, shopOrderInfo);
		return commissionDetail;
	}	
	
}
