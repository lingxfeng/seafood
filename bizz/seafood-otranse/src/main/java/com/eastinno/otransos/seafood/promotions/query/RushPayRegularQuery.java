package com.eastinno.otransos.seafood.promotions.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;

public class RushPayRegularQuery extends QueryObject{
	private Date startDate;
	private Date endDate;
	private Date shelvingDate;
	private Date unshelvingDate;
	private Long proId;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public Date getShelvingDate() {
		return shelvingDate;
	}
	public void setShelvingDate(Date shelvingDate) {
		this.shelvingDate = shelvingDate;
	}
	public Date getUnshelvingDate() {
		return unshelvingDate;
	}
	public void setUnshelvingDate(Date unshelvingDate) {
		this.unshelvingDate = unshelvingDate;
	}
	public Long getProId() {
		return proId;
	}
	public void setProId(Long proId) {
		this.proId = proId;
	}
	
	@Override
	public void customizeQuery(){
		if(this.startDate != null){
			this.addQuery("obj.startDate", this.startDate, ">=");
		}
		if(this.endDate != null){
			this.addQuery("obj.endDate", this.endDate, "<=");
		}
		if(this.shelvingDate != null){
			this.addQuery("obj.shelvingDate", this.shelvingDate, ">=");
		}
		if(this.unshelvingDate != null){
			this.addQuery("obj.unshelvingDate", this.unshelvingDate, "<=");
		}
		if(this.proId != null){
			this.addQuery("obj.pro.id", this.proId, "=");
		}
		if(this.name != null){
			this.addQuery("obj.name", "%"+this.name+"%", "like");
		}
		super.customizeQuery();
	}
}
