package com.eastinno.otransos.seafood.droduct.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 运费规则实体类
 * @author wb
 *
 */
@Entity(name="Disco_Shop_DeliveryRule")
public class DeliveryRule {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键标示
	@Column(nullable=false)
	private Long productId;	//商品id，如果productdId=0，则表示该记录为默认记录
	@Column(nullable=false)
	private int type;	//规则类型，1表示单一商品包邮总价起点记录，2表示设置订单总价包邮的起点，3表示偏远地区运费设置记录，4表示非偏远地区运费设置记录，5表示附近地区运费设置记录，
	@Column(nullable=false)
	private String ruleKey;	//对应商品对应规则的记录个数，"single"代表单值，"multi"代表多值
	private String ruleValue;	//对应商品对应规则的记录键名对应的值
	private Date createDate;	//记录创建时间
	private Date modifyDate;	//记录修改时间
	
	public String getRuleKey() {
		return ruleKey;
	}
	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}
	public String getRuleValue() {
		return ruleValue;
	}
	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}		
}
