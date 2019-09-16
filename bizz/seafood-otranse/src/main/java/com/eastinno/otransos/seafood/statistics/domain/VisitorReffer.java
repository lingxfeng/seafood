package com.eastinno.otransos.seafood.statistics.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年1月24日 下午12:08:39
*@version 1.0
*@parameter
*@since
*@return 
*/
@Entity(name="Disco_Shop_VisitorReffer")
public class VisitorReffer {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键标示
	@Column(nullable=false)
	private String userIp;	//用户IP地址
	private Long userId;	//用户Id
	private Date createDate;	//访问时间
	private int countByIp;//当前ip当日访问次数
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getCountByIp() {
		return countByIp;
	}
	public void setCountByIp(int countByIp) {
		this.countByIp = countByIp;
	}
}
