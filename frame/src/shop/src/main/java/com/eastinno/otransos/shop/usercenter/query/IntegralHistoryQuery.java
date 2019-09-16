package com.eastinno.otransos.shop.usercenter.query;

import java.util.Date;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.User;

/**
 * 积分记录查询
 * @author Administrator
 *
 */
public class IntegralHistoryQuery extends QueryObject{
	private Long userId;	//会员ID
	private String userName;	//会员账户名
	private String userCode;	//用户账号
	private String description;	//备注信息
	private Date createDateStart;
	private Date createDateEnd;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
		
	@Override
    public void customizeQuery() {		
		if(this.userCode != null){
			this.addQuery("obj.user.code", "%"+this.userCode+"%", "like");
		}
		if(this.userName != null && !"".equals(this.userName)){
			this.addQuery("(obj.user.name like '%"+this.userName+"%' or obj.user.nickname like '%"+this.userName+"%' or obj.user.trueName like '%"+this.userName+"%' )");
		}
		if(this.description != null){
			this.addQuery("obj.description", "%"+this.description+"%", "like");
		}
		if(this.createDateStart != null){
			this.addQuery("obj.createDate", this.createDateStart, ">=");
		}
		if(this.createDateEnd != null){
			this.addQuery("obj.createDate", this.createDateEnd, "<=");
		}
		if(this.userId != null && !"".equals(this.userId)){
			this.addQuery("obj.user.id", this.userId, "=");
		}
        super.customizeQuery();
    }
}
