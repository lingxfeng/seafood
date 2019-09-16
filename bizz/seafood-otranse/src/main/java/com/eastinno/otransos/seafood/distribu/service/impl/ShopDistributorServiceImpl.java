package com.eastinno.otransos.seafood.distribu.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.application.util.QRCodeUtil;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.dao.IShopDistributorDAO;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.usercenter.dao.IShopMemberDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ShopDistributorServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopDistributorServiceImpl implements IShopDistributorService{
	@Resource
	private IShopDistributorDAO shopDistributorDao;	
	@Resource
	private IShopMemberDAO shopMemberDAO;
	@Autowired
	private IShopMemberService shopMemberService;
	public void setShopMemberDAO(IShopMemberDAO shopMemberDAO) {
		this.shopMemberDAO = shopMemberDAO;
	}

	public void setShopDistributorDao(IShopDistributorDAO shopDistributorDao){
		this.shopDistributorDao=shopDistributorDao;
	}
	
	public Long addShopDistributor(ShopDistributor shopDistributor) {	
		this.shopDistributorDao.save(shopDistributor);
		if (shopDistributor != null && shopDistributor.getId() != null) {
			return shopDistributor.getId();
		}
		return null;
	}
	
	public ShopDistributor getShopDistributor(Long id) {
		ShopDistributor shopDistributor = this.shopDistributorDao.get(id);
		return shopDistributor;
		}
	
	public boolean delShopDistributor(Long id) {	
			ShopDistributor shopDistributor = this.getShopDistributor(id);
			if (shopDistributor != null) {
				this.shopDistributorDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopDistributors(List<Serializable> shopDistributorIds) {
		
		for (Serializable id : shopDistributorIds) {
			delShopDistributor((Long) id);
		}
		return true;
	}
	
	public IPageList getShopDistributorBy(IQueryObject queryObj) {	
		return this.shopDistributorDao.findBy(queryObj);		
	}
	
	public boolean updateShopDistributor(Long id, ShopDistributor shopDistributor) {
		if (id != null) {
			shopDistributor.setId(id);
		} else {
			return false;
		}
		this.shopDistributorDao.update(shopDistributor);
		return true;
	}
	/**
	 * 申请成为分销商
	 */
	@Override
	public void applyShopDistributor(Account a, ShopDistributor butor, ShopMember member) {
		butor.setMember(member);
		String url=WeixinBaseUtils.getDomain()+"/distributionCore.java?accountId="+a.getId()+"&pmemberId="+member.getId();
		butor.setUrl(url);
		this.shopDistributorDao.save(butor);
		if(butor.getId()!=null){
			String imgName = System.currentTimeMillis()+"";
			String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();
	        path=path.replace("file:", ""); //去掉file:  
	        path=path.replace("classes/", ""); //去掉class\
	        path=path.replace("/WEB-INF/", "");
			String imgPath = UploadFileConstant.FILE_UPLOAD_PATH + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE+"/";
			try {
				QRCodeUtil.encode(url, null, path+imgPath, false,imgName);
				butor.setqRcodeImg(imgPath+imgName+".jpg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public ShopDistributor getShopDistributorByMember(ShopMember member) {
		return this.shopDistributorDao.getBy("member", member);
	}
	/**
	 * 审批实体店
	 */
	public boolean updateChangeEntityShop(ShopDistributor obj){
		ShopMember member = obj.getMember();		
		if(member.getDisType() == 0){
			ShopDistributor distributor = member.getDistributor();
			if(distributor!=null){
				String olddePath = distributor.getDePath();
				String newdePath = "@"+member.getCode();
				String disSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"') where  exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+olddePath+"%'";
				String topSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+obj.getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%'and t1.id <> "+obj.getId()+")";
				String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+obj.getId()+" where m.dePath like '"+member.getDePath()+"%' and m.distributor_id="+distributor.getId();
				String parentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+obj.getId()+" where  exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id = " + distributor.getId();
				this.piupdate(disSql);
				this.piupdate(topSql);
				this.piupdate(memberSql);
				this.piupdate(parentSql);
			}else{
				String newdePath = "@"+member.getCode();
				String depathSql = "update Disco_Shop_Distributor t1 set t1.dePath=concat('"+newdePath+"',t1.dePath) where  exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%'and t1.id <> "+obj.getId()+")";
				String disSql = "update Disco_Shop_Distributor t1 set t1.parent_id ="+obj.getId()+" where  exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.parent_id is null)";
				String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+obj.getId()+" where m.dePath like '"+member.getDePath()+"%' and m.distributor_id is null and m.disType = 0";
				String topSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+obj.getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.id <> "+obj.getId()+")";
				this.piupdate(depathSql);
				this.piupdate(disSql);
				this.piupdate(memberSql);
				this.piupdate(topSql);
			}
		}else{
			ShopDistributor distributor = obj.getParent();
			if(distributor!=null){
				String olddePath = obj.getDePath();
				String newdePath = "@"+member.getCode();
				String disSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"') where  exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+olddePath+"%'";
				String topSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+obj.getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%'and t1.id <> "+obj.getId()+")";
				this.piupdate(disSql);
				this.piupdate(topSql);
			}else{
				String topSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+obj.getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.id <> "+obj.getId()+")";
				this.piupdate(topSql);
			}
		}
		member.setDisType(2);
		member.setDistributor(null);
		obj.setJoinTime(new Date());
		obj.setExStatus(1);
		obj.setDisType(2);
		obj.setParent(null);
		obj.setTopDistributor(null);
		obj.setDePath("@"+member.getCode());
		this.updateShopDistributor(obj.getId(), obj);
		return true;
	}
	/**
	 * 审批微店
	 * @param obj
	 */
	public void updateChangeWxShop(ShopDistributor obj){
		//查询当前微店所关联的会员
		ShopMember member = obj.getMember();
		if(member.getDisType()==0){
			//查询被推荐人已经成为微店
	    	if(member.getDistributor() == null){
	    		String sql1 = "update Disco_Shop_Distributor t1 set t1.parent_id= '"+obj.getId()+"'where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.parent_id is null and t1.id != '"+obj.getId()+"')";
	    		this.piupdate(sql1);
	    	}else{
	    		String sql1 = "update Disco_Shop_Distributor t1 set t1.parent_id= '"+obj.getId()+"'where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.parent_id = '"+member.getDistributor().getId()+"')";
	    		this.piupdate(sql1);
	    	}
	    	
	    	/**
	    	 * 批量更新子distributor的depath
	    	 */
	    	String olddelPath=null;
	    	if(member.getDistributor()!=null){
	    		olddelPath=member.getDistributor().getDePath();
	    		String newdepath=member.getDistributor().getDePath()+"@"+member.getCode();
	    		obj.setDePath(newdepath);
	        	String sql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddelPath+"','"+newdepath+"') where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+olddelPath+"%'";
	        	this.piupdate(sql);
	    	}else{
	    		String newdepath= "@"+member.getCode();
	    		obj.setDePath(newdepath);
	    		String sql = "update `Disco_Shop_Distributor` `t1` set `t1`.`dePath`=concat('"+newdepath+"',`t1`.`dePath`) where exists (select 1 from `Disco_Shop_ShopMember` `m` where `m`.`id`=`t1`.`member_id` and `m`.`dePath` like '"+member.getDePath()+"%' and `m`.`dePath` <> '"+member.getDePath()+"')";
	        	this.piupdate(sql);
	    	}
	    	obj.setParent(member.getDistributor());
	    	obj.setJoinTime(new Date());
	    	if(obj.getMember().getDistributor() != null){
	    		if(obj.getMember().getDistributor().getExStatus() == 1){
	    			obj.setTopDistributor(obj.getMember().getDistributor());
	    		}else{
	    			obj.setTopDistributor(obj.getMember().getDistributor().getTopDistributor());
	    		}
	    	}else{
	    		obj.setTopDistributor(null);
	    	}
	    	this.updateShopDistributor(obj.getId(), obj);
	    	/**
	    	 * 批量跟新结束
	    	 */

	    	if(member.getDistributor() != null){
	    		//我所属微店
	    		//String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+obj.getId()+" where m.dePath like '"+member.getDePath()+"%' and (m.distributor_id="+member.getDistributor().getId()+" or m.distributor_id is null)";
	    		String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+obj.getId()+" where m.dePath like '"+member.getDePath()+"%' and m.disType = 0 and m.distributor_id="+member.getDistributor().getId()+" and m.id <> "+member.getId();
	    		this.piupdate(memberSql);
	    	}else{
	    		//我没有所属微店
	    		String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+obj.getId()+" where m.dePath like '"+member.getDePath()+"%' and  m.distributor_id is null and m.disType = 0 and m.id <> "+member.getId();
	        	this.piupdate(memberSql);
	    	}
	    	
	    	//更新member表数据
	    	member.setDisType(1);
	    	member.setDistributor(null);
	    	member.setMyDistributor(obj);
	    	this.shopMemberService.updateShopMember(member.getId(),member);
		}	
	}
	/**
	 * 推荐关系更改
	 */
	
	public void changeRalation(ShopMember member,ShopMember pmember){
		ShopDistributor dis = member.getMyDistributor();
		ShopDistributor pdis = pmember.getMyDistributor();		
		/**
		 * 批量更改我的下级(更改shopmember表)
		 * 
		 */
		String olddePath = member.getDePath();
		String newdePath = pmember.getDePath()+"@"+member.getCode();
		//更改depath字段
		String memberDepathSql = "update Disco_Shop_ShopMember t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"') where t1.dePath like '"+olddePath+"%'";
		//更改distributor字段
		String memberDistributorSql = "";
		if(member.getDisType()==0 && pmember.getDisType()==0 && pmember.getDistributor() != null && member.getDistributor() != null){//我是会员，我有所属微店，更改的上级是会员，更改的上级有所属微店
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id='"+pmember.getDistributor().getId()+"' where m.dePath like '"+member.getDePath()+"%' and m.distributor_id ="+member.getDistributor().getId();
		}else if(member.getDisType()==0 && pmember.getDisType()==0 && pmember.getDistributor() != null && member.getDistributor() == null){//我是会员，我没有所属微店，更改的上级是会员，更改的上级有所属微店
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id='"+pmember.getDistributor().getId()+"' where m.dePath like '"+member.getDePath()+"%' and m.distributor_id is null and m.disType=0";
		}else if(member.getDisType()==0 && pmember.getDisType()==0 && pmember.getDistributor() == null && member.getDistributor() == null){//我是会员，我没有所属微店，更改的上级是会员，更改的上级没有所属微店
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id = null where m.dePath like '"+member.getDePath()+"%' and m.distributor_id is null and m.disType=0";
		}else if(member.getDisType()==0 && pmember.getDisType()==0 && pmember.getDistributor() == null && member.getDistributor() != null){//我是会员，我有所属微店，更改的上级是会员，更改的上级没有所属微店
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id = null where m.dePath like '"+member.getDePath()+"%' and m.distributor_id ="+member.getDistributor().getId();				
		}else if(member.getDisType()==0 && pmember.getDisType()!=0 && member.getDistributor() == null){//我是会员，我没有所属微店，更改的上级不是会员
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id ='"+pdis.getId()+"'where m.dePath like '"+member.getDePath()+"%' and m.distributor_id is null and m.disType=0";
		}else if(member.getDisType()==0 && pmember.getDisType()!=0 && member.getDistributor() != null){//我是会员，我有所属微店，更改的上级不是会员
			memberDistributorSql = "update Disco_Shop_ShopMember m set m.distributor_id ='"+pdis.getId()+"'where m.dePath like '"+member.getDePath()+"%' and m.distributor_id ="+member.getDistributor().getId();
		}
		this.piupdate(memberDepathSql);
		if(!memberDistributorSql.equals("")){
			this.piupdate(memberDistributorSql);
		}
		
		
		/**
		 * 批量更改我的下级(更改distributor表)
		 * 
		 */
		String distributorTopSql="";
		String distributorDepathSql="";
		String distributorParentSql ="";
		if(member.getDisType()==1){//我是微店
			String oldDisDepath = member.getMyDistributor().getDePath();
			if(pmember.getDisType() == 0 && pmember.getDistributor()==null){//上级是会员，且没有上级微店
				String newDisDePath = "@"+member.getCode();
				distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where t1.dePath like '"+oldDisDepath+"%'";
				distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where t1.dePath like '"+oldDisDepath+"%'";
			}else if(pmember.getDisType() == 0 && pmember.getDistributor()!=null){//上级是会员，且有上级微店
				String newDisDePath = pmember.getDistributor().getDePath()+"@"+member.getCode();
				distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where t1.dePath like '"+oldDisDepath+"%'";
				if(pmember.getDistributor().getTopDistributor()==null){//上级微店没有隶属体验店
					if(pmember.getDistributor().getDisType()==2){//上级身份为体验店
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id='"+pmember.getDistributor().getId()+"' where t1.dePath like '"+oldDisDepath+"%'";
					}else{
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where t1.dePath like '"+oldDisDepath+"%'";
					}
				}else{
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ='"+pmember.getDistributor().getTopDistributor().getId()+"' where t1.dePath like '"+oldDisDepath+"%'";
				}
			}else if(pmember.getDisType() == 2){
				String newDisDePath = pmember.getCode()+"@"+member.getCode();
				distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where t1.dePath like '"+oldDisDepath+"%'";
				distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+pmember.getMyDistributor().getId()+" where t1.dePath like '"+oldDisDepath+"%'";

			}else if(pmember.getDisType() == 1){					
				String newDisDePath = pmember.getMyDistributor().getDePath()+"@"+member.getCode();
				distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where t1.dePath like '"+oldDisDepath+"%'";

				if(pmember.getMyDistributor().getTopDistributor()==null){
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where t1.dePath like '"+oldDisDepath+"%'";
				}else{
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ='"+pmember.getMyDistributor().getTopDistributor().getId()+"' where t1.dePath like '"+oldDisDepath+"%'";
				}
			}
		}else if(member.getDisType() == 0){//我是会员
			if(member.getDistributor()==null){//我没有所属微店
				if(pmember.getDisType() == 0 && pmember.getDistributor()==null){//上级是会员，且没有上级微店
					
				}else if(pmember.getDisType() == 0 && pmember.getDistributor()!=null){//上级是会员，且有上级微店
					String newDisDePath = pmember.getDistributor().getDePath();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=concat('"+newdePath+"',t1.dePath)  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id is null";
					if(pmember.getDistributor().getTopDistributor()==null){//上级微店没有隶属体验店
						
					}else{
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ="+pmember.getDistributor().getTopDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					}
				}else if(pmember.getDisType() == 2){
					String newDisDePath = pmember.getCode();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=concat('"+newdePath+"',t1.dePath)  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id="+pmember.getMyDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getMyDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id is null";
				}else if(pmember.getDisType() == 1){					
					String newDisDePath = pmember.getMyDistributor().getDePath();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=concat('"+newdePath+"',t1.dePath)  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getMyDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id is null";
					if(pmember.getMyDistributor().getTopDistributor()==null){
						
					}else{
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ='"+pmember.getMyDistributor().getTopDistributor().getId()+"' where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.topDistributor_id is null and t1.disType <>2";
					}
				}
			}else{//我有所属微店
				String oldDisDepath=member.getDistributor().getDePath();
				if(pmember.getDisType() == 0 && pmember.getDistributor()==null){//上级是会员，且没有上级微店
					String newDisDePath = "";
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = null where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id ="+member.getDistributor().getId();
				}else if(pmember.getDisType() == 0 && pmember.getDistributor()!=null){//上级是会员，且有上级微店
					String newDisDePath = pmember.getDistributor().getDePath();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id = "+ member.getDistributor().getId();
					if(pmember.getDistributor().getTopDistributor()==null){//上级微店没有隶属体验店
						if(pmember.getDistributor().getDisType()==2){//上级身份为体验店
							distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id='"+pmember.getDistributor().getId()+"' where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
						}else{
							distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
						}
					}else{
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ='"+pmember.getDistributor().getTopDistributor().getId()+"' where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					}
				}else if(pmember.getDisType() == 2){
					String newDisDePath = pmember.getCode();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id='"+pmember.getMyDistributor().getId()+"' where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getMyDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id = "+ member.getDistributor().getId();
				}else if(pmember.getDisType() == 1){					
					String newDisDePath = pmember.getMyDistributor().getDePath();
					distributorDepathSql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddePath+"','"+newdePath+"')  where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					distributorParentSql = "update Disco_Shop_Distributor t1 set t1.parent_id = "+pmember.getMyDistributor().getId()+" where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.parent_id = "+ member.getDistributor().getId();
					if(pmember.getMyDistributor().getTopDistributor()==null){
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id = null where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					}else{
						distributorTopSql = "update Disco_Shop_Distributor t1 set t1.topDistributor_id ='"+pmember.getMyDistributor().getTopDistributor().getId()+"' where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+oldDisDepath+"%'";
					}
				}
			}
		}
		if(!distributorDepathSql.equals("")){
			this.piupdate(distributorDepathSql);
		}
		if(!distributorTopSql.equals("")){
			this.piupdate(distributorTopSql);
		}
		if(!distributorParentSql.equals("")){
			this.piupdate(distributorParentSql);
		}
		/**
		 * 更改当前会员信息
		 */
		member.setPmember(pmember);
		member.setDePath(pmember.getDePath()+"@"+member.getCode());
		if(member.getDisType()==0){
			if(pdis!=null){
				member.setDistributor(pdis);
			}else{
				member.setDistributor(pmember.getDistributor());
			}
		}else if(member.getDisType()==1){
			if(pdis!=null){
				dis.setParent(pdis);
				dis.setDePath(pdis.getDePath()+"@"+member.getCode());
				if(pdis.getDisType()==2){
					dis.setTopDistributor(pdis);
				}else{
					dis.setTopDistributor(pdis.getTopDistributor());
				}
			}else{
				member.setDistributor(pmember.getDistributor());
			}
		}
	}
	
	
    /**
     * 生成map，准备转json
     * @return
     */
    public Map create(){
		Map<String, Object> mapTop = new LinkedHashMap<String,Object>();
		mapTop.put("name", "白春达");
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent is null");
		qo.addQuery("(obj.status ="+ 1 +"or obj.exStatus = "+ 1 + ")");
		qo.setPageSize(-1);
		List<ShopDistributor> list = this.getShopDistributorBy(qo).getResult();
		List<Map> childlist = new ArrayList<Map>();
		if(list != null && list.size() != 0){
			for(ShopDistributor sd:list){
				Map<String, Object> maps = new LinkedHashMap<String,Object>();
				if(sd.getDisType() == 2){
					String strings = sd.getMyShopName() + "(体验店)";
					maps.put("name",strings );
				}else{
					maps.put("name", sd.getMyShopName());
				}
					
					digui(maps,sd.getId());
					childlist.add(maps);
			}
		}
		
		mapTop.put("children", childlist);
		return mapTop;
	}
    //递归获取关系串
	private void digui(Map maps,Long id){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent.id",id,"=");
		qo.setPageSize(-1);
		List<ShopDistributor> list = this.getShopDistributorBy(qo).getResult();
		List<Map> childlist = new ArrayList<Map>();
		if(list != null && list.size()!=0){
			for(ShopDistributor sd:list){
				Map<String, Object> map = new LinkedHashMap<String,Object>();
				if(sd.getDisType() == 2){
					String strings = sd.getMyShopName() + "(体验店)";
					map.put("name",strings );
				}else{
					map.put("name", sd.getMyShopName());
				}
				
				digui(map,sd.getId());
				childlist.add(map);
				
			}
			maps.put("children",childlist);
		}
	}
	
	@Override
	public void mUpdteDistributor(ShopDistributor distributor,ShopDistributor entityshop) {
		String disstr = distributor.getDePath();//当前depath
		ShopMember member = distributor.getMember();//当前微店的会员
		String olddelPath=member.getDistributor().getDePath();//当前depath的前半部分
    	//String newdepath=member.getDistributor().getDePath()+"@"+member.getCode();//depath前半部分要替换成这个
    	String newdepath="@"+entityshop.getMember().getCode();//depath前半部分要替换成这个
    	distributor.setDePath(newdepath);
    	distributor.setParent(member.getDistributor());
    	
    	QueryObject qo = new QueryObject();
		qo.addQuery("obj.dePath",olddelPath+"%","like");
		qo.addQuery("obj.member.dePath",member.getDePath()+"%","like");
		List<ShopDistributor> list = this.getShopDistributorBy(qo).getResult();
		if(list!=null && list.size()>0){
			for(ShopDistributor dis:list){
				String keepstr = disstr.substring(olddelPath.length(), disstr.length());//保留下的部分
				dis.setDePath(newdepath + keepstr);//拼凑起的depath
			}
		}
	}
	@Override
	public String getMapLevel1Date(){
		Map<String,Integer> map = new HashMap<>();
		StringBuffer str = new StringBuffer("");
		//统计加盟店数量
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.disType",2,"=");
		qo.addQuery("obj.exStatus",1,"=");
		qo.setPageSize(-1);
		List<ShopDistributor> list1 = this.getShopDistributorBy(qo).getResult();
		List<String> containList = new ArrayList<>();
		if(list1 != null && list1.size() != 0){
			for(ShopDistributor dis:list1){
				String[] strarray = dis.getArea().getPath().split("@");
				String snStr = strarray[0];
				if(containList.contains(snStr)){
					map.put(this.getProvince(dis.getArea()), map.get(this.getProvince(dis.getArea()))+1);
				}else{
					containList.add(snStr);
					map.put(this.getProvince(dis.getArea()),1);
				}
			}
		}
		str = str.append("[");
		for (Map.Entry<String, Integer> entry : map.entrySet()) { 
			str=str.append("{name:").append("\"").append(entry.getKey()).append("\"").append(",value:").append(entry.getValue()).append("},");
		} 
		str = str.deleteCharAt(str.length() - 1);
		str = str.append("]");
		System.out.println(str.toString());
		return str.toString();
	}
	@Override
	public String getMapLevel2Date(){
		Map<String,Integer> map = new HashMap<>();
		StringBuffer str = new StringBuffer("");
		//统计加盟店数量
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",1,"=");
		qo.addQuery("obj.exStatus",1,"!=");
		qo.setPageSize(-1);
		List<ShopDistributor> list1 = this.getShopDistributorBy(qo).getResult();
		List<String> containList = new ArrayList<>();
		if(list1 != null && list1.size() != 0){
			for(ShopDistributor dis:list1){
				String[] strarray = dis.getArea().getPath().split("@");
				String snStr = strarray[0];
				if(containList.contains(snStr)){
					map.put(this.getProvince(dis.getArea()), map.get(this.getProvince(dis.getArea()))+1);
				}else{
					containList.add(snStr);
					map.put(this.getProvince(dis.getArea()),1);
				}
			}
		}
		str = str.append("[");
		for (Map.Entry<String, Integer> entry : map.entrySet()) { 
			str=str.append("{name:").append("\"").append(entry.getKey()).append("\"").append(",value:").append(entry.getValue()).append("},");
		} 
		str = str.deleteCharAt(str.length() - 1);
		str = str.append("]");
		System.out.println(str.toString());
		return str.toString();
	}
	public String getProvince(SystemRegion area){
		String provincename="";
		if(area.getParent() != null){
			if(area.getParent().getParent() != null){
				provincename = area.getParent().getParent().getTitle();
			}else{
				provincename = area.getParent().getTitle();
			}
		}else{
			provincename = area.getTitle();
		}
		return provincename.substring(0,provincename.length() - 1);
	}
	@Override
	public void piupdate(String sql) {
		//this.shopDistributorDao.batchUpdate(jpql, null);
		int result = this.shopDistributorDao.executeNativeSQL(sql);
		System.out.println(result);
	}

	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public IShopDistributorDAO getShopDistributorDao() {
		return shopDistributorDao;
	}

	public IShopMemberDAO getShopMemberDAO() {
		return shopMemberDAO;
	}	
	
	
}
