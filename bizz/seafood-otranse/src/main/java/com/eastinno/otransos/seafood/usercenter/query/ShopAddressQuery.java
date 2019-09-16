package com.eastinno.otransos.seafood.usercenter.query;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;

/**
 * 收货地址查询
 * @author Administrator
 *
 */
public class ShopAddressQuery extends QueryObject{
	private String userName;	//会员账户名
	private String trueName;	//真实姓名
	private Long userId;	//会员帐号
	
	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
		
	@Override
    public void customizeQuery() {
		if(this.userName != null){
			this.addQuery("(obj.user.name like '%"+this.userName+"%' or obj.user.nickname like '%"+this.userName+"%' or obj.user.trueName like '%"+this.userName+"%' )");
		}
		if(this.trueName!=null){
			this.addQuery("(obj.user.trueName like '%"+this.trueName+"%')");
		}
		if(this.userId != null){			
			this.addQuery("obj.id", this.userId, "=");
		}
        super.customizeQuery();
    }
}
