package com.eastinno.otransos.shop.trade.domain;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

import com.google.common.collect.Lists;

/**
 * 配送方式
 * @author Administrator
 *
 */
@Entity(name = "Disco_Shop_Delivery")
public class Delivery implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@Column(length = 30)
	private String name;//配送方式名称
	private Integer type;//配送类型 1：商家配送 2： 用户自提
	private Integer completeType;//完善信息  0：否 1：是
	private Integer sequence = 1;// 排序
	@OrderBy("sequence")
	@OneToMany(mappedBy="delivery",cascade={CascadeType.REMOVE},fetch=FetchType.LAZY)
	private List<DeliveryDetail> deliveryDetails = Lists.newArrayList();
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCompleteType() {
		return completeType;
	}

	public void setCompleteType(Integer completeType) {
		this.completeType = completeType;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {""});
		return map;
	}
	
}
