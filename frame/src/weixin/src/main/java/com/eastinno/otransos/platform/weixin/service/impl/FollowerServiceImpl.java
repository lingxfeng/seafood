package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.dao.IFollowerDAO;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * FollowerServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class FollowerServiceImpl implements IFollowerService{
	@Resource
	private IFollowerDAO followerDao;
	
	public void setFollowerDao(IFollowerDAO followerDao){
		this.followerDao=followerDao;
	}
	
	public Long addFollower(Follower follower) {
		this.followerDao.save(follower);
		if (follower != null && follower.getId() != null) {
			return follower.getId();
		}
		return null;
	}
	
	public Follower getFollower(Long id) {
		Follower follower = this.followerDao.get(id);
		return follower;
		}
	
	
	public IPageList getFollowerBy(IQueryObject queryObj) {	
		return this.followerDao.findBy(queryObj);	
	}
	
	public boolean updateFollower(Long id, Follower follower) {
		if (id != null) {
			follower.setId(id);
		} else {
			return false;
		}
		this.followerDao.update(follower);
		return true;
	}

	@Override
	public boolean delFollower(Long id) {
		Follower Follower = this.getFollower(id);
        if (Follower != null) {
            this.followerDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelFollowers(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateWxUser(Account account, String next_openid) {
		Map<String, Object> map = RequestWxUtils.getWxUser(account, null);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        List<String> openIds = (List<String>) data.get("openid");
        if (openIds != null && openIds.size() > 0) {
            List<String> openIdsDb = this.followerDao.getFollowerIdsByIds(account.getId());
            for(String openId:openIdsDb){
            	openIds.remove(openId);
            }
            int i = 0;
            for (String openId : openIds) {
                if (i < 400) {
                	Follower follower = new Follower();
                    follower.setTenant(account.getTenant());
                    follower.setAccount(account);
                    follower.setCode(new Date().getTime() + "");
                    follower.setName(new Date().getTime() + "");
                    follower.setWeixinOpenId(openId);
                    follower.setRegisterTime(new Date());
                    follower.setStatus(new Integer(1));
                    RequestWxUtils.getUserInfo(follower, account);
                    System.out.println(follower.getNickname());
                    this.followerDao.save(follower);
                    i++;
                }
            }
            Object next_openidObj = map.get("next_openid");
            if (next_openidObj != null) {
                // this.updateWxUser(account,next_openidObj.toString());
            }
        }
	}

	@Override
	public Follower getFollowerByCode(Account account,String code) {
		Follower follower = null;
    	if(!"".equals(code)){
        	Map<String,Object> userInfo =null;
        	for(int i=0;i<5;i++){
        		userInfo = RequestWxUtils.getUserInfoByCode(account,code);
        		if(userInfo!=null && userInfo.get("openid")!=null ){
        			break;
        		}
        	}
        	String weixinOpenId = userInfo.get("openid")+"";
        	if(!"".equals(weixinOpenId) && !"null".equals(weixinOpenId)){
        		QueryObject qo = new QueryObject();
                qo.addQuery("obj.weixinOpenId", weixinOpenId, "=");
                qo.addQuery("obj.tenant",account.getTenant(),"=");
                List<?> list = this.followerDao.findBy(qo).getResult();
                if (list != null && list.size() > 0) {
                	follower = (Follower) list.get(0);
                	follower.setAccount(account);
                }else{
                	follower = new Follower();
        			follower.setTenant(account.getTenant());
        			follower.setAccount(account);
        			follower.setCode(new Date().getTime() + "");
        			follower.setName(follower.getCode());
        			follower.setWeixinOpenId(weixinOpenId);
        			follower.setRegisterTime(new Date());
        			follower.setStatus(new Integer(-1));
        			String access_token = userInfo.get("access_token")+"";
        			if(!"".equals(access_token) && !"null".equals(access_token)){
        				RequestWxUtils.getUserInfo(follower, account, access_token, null);
        			}
        			this.followerDao.save(follower);
        			
                }
                ActionContext.getContext().getSession().setAttribute("wx_follwer", follower);
        	}
    	}
        return follower;
	}

	@Override
	public Follower getFollowerBySession() {
		Follower follower = null;
    	Object followerIdObj = ActionContext.getContext().getSession().getAttribute("wx_follwer");
    	if(followerIdObj!=null){
    		follower = (Follower) followerIdObj;
    	}
    	return follower;
	}	
	
}
