package com.eastinno.otransos.shop.trade.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 物流公司信息
 * @author Administrator
 *
 */
@Entity(name = "Disco_Shop_LogisticsCompany")
public class LogisticsCompany {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private String name;//物流公司名称
	private String code;//快递100识别编码
	private Short status=1;//是否使用  0：否 1：是
	private Integer sequence = 1;// 排序
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
}
