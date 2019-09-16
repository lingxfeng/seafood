package com.eastinno.otransos.shop.promotions.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;

public class IntegralRechargeRecordQuery extends QueryObject{
	private Date startDate;
	private Date endDate;
	private String memberName;
	private String memberId;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Override
	public void customizeQuery(){
		if(this.startDate != null){
			this.addQuery("obj.createDate", this.startDate, ">=");
		}
		if(this.endDate != null){
			this.addQuery("obj.createDate", this.endDate, "<=");
		}
		if(!"".equals(this.memberId) && this.memberId != null ){
			this.addQuery("obj.member.id", Long.parseLong(this.memberId), "=");
		}
		if(!"".equals(this.memberName) && this.memberName != null){
			this.addQuery("obj.member.follower.nickname", "%"+this.memberName+"%", "like");
		}
		super.customizeQuery();
	}
}
