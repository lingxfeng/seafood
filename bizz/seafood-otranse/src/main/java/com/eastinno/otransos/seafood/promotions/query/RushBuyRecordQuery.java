package com.eastinno.otransos.seafood.promotions.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;

public class RushBuyRecordQuery extends QueryObject{
	private Date startDate;
	private Date endDate;
	private String regularName;
	private String regularCode;
	private String proName;
	private String proId;
	private Long regularId;
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

	public String getRegularName() {
		return regularName;
	}

	public void setRegularName(String regularName) {
		this.regularName = regularName;
	}

	public String getRegularCode() {
		return regularCode;
	}

	public void setRegularCode(String regularCode) {
		this.regularCode = regularCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}
	public Long getRegularId() {
		return regularId;
	}

	public void setRegularId(Long regularId) {
		this.regularId = regularId;
	}

	@Override
	public void customizeQuery(){
		if(this.startDate != null){
			this.addQuery("obj.createDate", this.startDate, ">=");
		}
		if(this.endDate != null){
			this.addQuery("obj.createDate", this.endDate, "<=");
		}
		if(this.proId != null){
			this.addQuery("obj.regular.pro.id", this.proId, "=");
		}
		if(this.proName != null){
			this.addQuery("obj.regular.pro.name", "%"+this.proName+"%", "like");
		}
		if(this.regularCode != null){
			this.addQuery("obj.regular.code", "%"+this.regularCode+"%", "like");
		}
		if(this.regularName != null){
			this.addQuery("obj.regular.name", "%"+this.regularName+"%", "like");
		}
		if(this.regularId != null){
			this.addQuery("obj.regular.id",this.regularId, "=");
		}
		super.customizeQuery();
	}
}
