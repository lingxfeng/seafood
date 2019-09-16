package com.eastinno.otransos.seafood.promotions.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分兑换、赠送规则实体类
 * @author wb
 *
 */
@Entity(name="Disco_Shop_IntegralChangeRule")
public class IntegralChangeRule {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	//主键标示
	@Column(nullable=false)
	private String type;	//规则类型，"register_add"代表注册赠送积分数量，"cash_rate"表示购物时1积分多少元
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}		
}
