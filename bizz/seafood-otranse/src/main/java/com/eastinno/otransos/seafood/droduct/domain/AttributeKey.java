package com.eastinno.otransos.seafood.droduct.domain;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.eastinno.otransos.container.annonation.POLoad;

import com.google.common.collect.Lists;

/**
 * 商品属性、参数、规格
 * @author nsz
 */
@Entity(name = "Disco_Shop_AttributeKey")
public class AttributeKey {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private Short type=1;//属性类型:1.属性，2.参数,3规格
	private Short colType=1;//参数类型：1.字符串类型，2.数字类型，3.日期类型，4.下拉选项,5.大字符串
	private Integer sequence=0;//排序
	@Column(length=20,nullable=false)
	private String name;
	@POLoad(name = "productTypeId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ProductType productType;//所属商品类型
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String value;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Short getColType() {
		return colType;
	}
	public void setColType(Short colType) {
		this.colType = colType;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
