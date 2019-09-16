package com.eastinno.otransos.seafood.droduct.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;

/**
 * 商品的属性、参数值
 * 
 * @author nsz
 */
@Entity(name = "Disco_Shop_AttributeValue")
public class AttributeValue {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@POLoad(name = "productId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopProduct product;// 所属商品
	@POLoad(name = "attributeKeyId")
	@ManyToOne(fetch = FetchType.LAZY)
	private AttributeKey attributeKey;// 所属属性
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String value;
	private Integer type = 1;//

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShopProduct getProduct() {
		return product;
	}

	public void setProduct(ShopProduct product) {
		this.product = product;
	}

	public AttributeKey getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(AttributeKey attributeKey) {
		this.attributeKey = attributeKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
