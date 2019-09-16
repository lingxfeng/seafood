package com.eastinno.otransos.shop.spokesman.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
/**
 * 特殊津贴发放记录
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_PaySpecial")
public class PaySpecialAllowance {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private Spokesman spokesman;//获取特殊津贴的人
	private Integer partNum;//获取的份数
	private Float SpecialAllowance;//特殊津贴金额
	private Date time=new Date();//发放时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Spokesman getSpokesman() {
		return spokesman;
	}
	public void setSpokesman(Spokesman spokesman) {
		this.spokesman = spokesman;
	}
	public Integer getPartNum() {
		return partNum;
	}
	public void setPartNum(Integer partNum) {
		this.partNum = partNum;
	}
	public Float getSpecialAllowance() {
		return SpecialAllowance;
	}
	public void setSpecialAllowance(Float specialAllowance) {
		SpecialAllowance = specialAllowance;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
