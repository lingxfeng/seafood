package com.eastinno.otransos.seafood.droduct.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/**
 * 商品规格
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopSpec")
public class ShopSpec {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length=100)
	private String name;//规格名称
	private Double amt=0.0;//价格
	private Double store_price=0.0;// 店铺价格（微店价格）
	private Double tydAmt=0.0;	//体验店价格
	private Double costAmt=0.0;//成本价
	private Integer inventory=0;//库存
	private String code;//货号
	private Boolean isDefault=false;//是否为默认规格
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct product;
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
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public ShopProduct getProduct() {
		return product;
	}
	public void setProduct(ShopProduct product) {
		this.product = product;
	}
	public Double getStore_price() {
		return store_price;
	}
	public void setStore_price(Double store_price) {
		this.store_price = store_price;
	}
	public Double getTydAmt() {
		return tydAmt;
	}
	public void setTydAmt(Double tydAmt) {
		this.tydAmt = tydAmt;
	}
	public Double getCostAmt() {
		return costAmt;
	}
	public void setCostAmt(Double costAmt) {
		this.costAmt = costAmt;
	}
	
}
