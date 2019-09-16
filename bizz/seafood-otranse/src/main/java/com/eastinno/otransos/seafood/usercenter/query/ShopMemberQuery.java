package com.eastinno.otransos.seafood.usercenter.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.dbo.util.StringUtils;

/**
 * 会员查询
 * @author Administrator
 *
 */
public class ShopMemberQuery extends QueryObject{
	private String name;	//会员账户名	
	private String nickname;
	private Long pmemberId;
	private Long uId;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setPmemberId(Long pmemberId) {
		this.pmemberId = pmemberId;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setuId(Long uId) {
		this.uId = uId;
	}

	@Override
    public void customizeQuery() {
		if(StringUtils.hasText(this.name)){
			this.addQuery("obj.name like '%"+name+"%'");
		}
		if(StringUtils.hasText(this.nickname)){
			this.addQuery("obj.nickname like '%"+this.nickname+"%'");
		}
		if(this.uId!=null){
			this.addQuery("obj.id", uId, "=");
		}
		if(this.pmemberId!=null){
			this.addQuery("obj.pmember.id", pmemberId, "=");
		}
		this.setOrderBy("registerTime");
		this.setOrderType("desc");
        super.customizeQuery();
    }
}
