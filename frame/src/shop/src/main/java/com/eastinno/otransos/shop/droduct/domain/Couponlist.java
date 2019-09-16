package com.eastinno.otransos.shop.droduct.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 优惠卷
 * @author nsz
 *
 */
@Entity(name = "Disco_Shop_Couponlist")
public class Couponlist {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 100)
	private Double money=0.0;// 金额
	@Column(length = 10)
	private String beginTime;
	@Column(length = 10)
	private String endTime;
	private Integer validState = 0;// 是否开启
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public Integer getValidState() {
		return validState;
	}

	public void setValidState(Integer validState) {
		this.validState = validState;
	}

	public int getSyDays() throws Exception {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String endTime = this.getEndTime();
		long end = df.parse(endTime).getTime();
		int days = (int) ((end - d.getTime()) / 1000 / 60 / 60 / 24);
		if(days>=0){
			return days;
		}else{
			return -1;
		}
		
	}
}
