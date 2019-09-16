package com.eastinno.otransos.shop.distribu.query;

import java.util.Date;

import com.eastinno.otransos.core.support.query.QueryObject;

/**
 * 收货地址查询
 * @author Administrator
 *
 */
public class CommissionDetailQuery extends QueryObject{
	private Date starttime;	//开始时间
	private Date endtime;	//结束时间
	
	
		
	public Date getStarttime() {
		return starttime;
	}



	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}



	public Date getEndtime() {
		return endtime;
	}



	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}



	@Override
    public void customizeQuery() {
		if(this.starttime != null){
			this.addQuery("obj.createtime",this.starttime, ">");
		}
		if(this.starttime != null){
			this.addQuery("obj.endtime",this.endtime, "<");
		}
        super.customizeQuery();
    }
}
