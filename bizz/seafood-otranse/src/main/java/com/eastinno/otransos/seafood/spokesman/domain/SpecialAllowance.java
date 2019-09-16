package com.eastinno.otransos.seafood.spokesman.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



/**
 * 特殊津贴
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_SpecialAllowance")
public class SpecialAllowance {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Float specialAllowance;//特殊津贴总额
	private Short status=0;//0,未发放，1，已发放
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getSpecialAllowance() {
		return specialAllowance;
	}
	public void setSpecialAllowance(Float specialAllowance) {
		this.specialAllowance = specialAllowance;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
}
