package com.eastinno.otransos.seafood.droduct.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 运费规则实体类-增加根据不同数量设置不同运费策略
 * 
 * @author dongliangliang
 *
 */
@Entity(name = "Disco_Shop_DeliveryRule_Ext")
public class DeliveryRuleExt {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id; // 主键标示

	@Column(nullable = false)
	private String name; // 运费规则名称

	private int level1Num;
	private int level1NearValue;
	private int level1CommonValue;
	private int level1FarValue;

	private int level2Num;
	private int level2NearValue;
	private int level2CommonValue;
	private int level2FarValue;

	private int level3Num;
	private int level3NearValue;
	private int level3CommonValue;
	private int level3FarValue;

	private int level4Num;
	private int level4NearValue;
	private int level4CommonValue;
	private int level4FarValue;

	private int level5Num;
	private int level5NearValue;
	private int level5CommonValue;
	private int level5FarValue;

	private Date createDate; // 记录创建时间
	private Date modifyDate; // 记录修改时间

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

	public int getLevel1Num() {
		return level1Num;
	}

	public void setLevel1Num(int level1Num) {
		this.level1Num = level1Num;
	}

	public int getLevel1NearValue() {
		return level1NearValue;
	}

	public void setLevel1NearValue(int level1NearValue) {
		this.level1NearValue = level1NearValue;
	}

	public int getLevel1CommonValue() {
		return level1CommonValue;
	}

	public void setLevel1CommonValue(int level1CommonValue) {
		this.level1CommonValue = level1CommonValue;
	}

	public int getLevel1FarValue() {
		return level1FarValue;
	}

	public void setLevel1FarValue(int level1FarValue) {
		this.level1FarValue = level1FarValue;
	}

	public int getLevel2Num() {
		return level2Num;
	}

	public void setLevel2Num(int level2Num) {
		this.level2Num = level2Num;
	}

	public int getLevel2NearValue() {
		return level2NearValue;
	}

	public void setLevel2NearValue(int level2NearValue) {
		this.level2NearValue = level2NearValue;
	}

	public int getLevel2CommonValue() {
		return level2CommonValue;
	}

	public void setLevel2CommonValue(int level2CommonValue) {
		this.level2CommonValue = level2CommonValue;
	}

	public int getLevel2FarValue() {
		return level2FarValue;
	}

	public void setLevel2FarValue(int level2FarValue) {
		this.level2FarValue = level2FarValue;
	}

	public int getLevel3Num() {
		return level3Num;
	}

	public void setLevel3Num(int level3Num) {
		this.level3Num = level3Num;
	}

	public int getLevel3NearValue() {
		return level3NearValue;
	}

	public void setLevel3NearValue(int level3NearValue) {
		this.level3NearValue = level3NearValue;
	}

	public int getLevel3CommonValue() {
		return level3CommonValue;
	}

	public void setLevel3CommonValue(int level3CommonValue) {
		this.level3CommonValue = level3CommonValue;
	}

	public int getLevel3FarValue() {
		return level3FarValue;
	}

	public void setLevel3FarValue(int level3FarValue) {
		this.level3FarValue = level3FarValue;
	}

	public int getLevel4Num() {
		return level4Num;
	}

	public void setLevel4Num(int level4Num) {
		this.level4Num = level4Num;
	}

	public int getLevel4NearValue() {
		return level4NearValue;
	}

	public void setLevel4NearValue(int level4NearValue) {
		this.level4NearValue = level4NearValue;
	}

	public int getLevel4CommonValue() {
		return level4CommonValue;
	}

	public void setLevel4CommonValue(int level4CommonValue) {
		this.level4CommonValue = level4CommonValue;
	}

	public int getLevel4FarValue() {
		return level4FarValue;
	}

	public void setLevel4FarValue(int level4FarValue) {
		this.level4FarValue = level4FarValue;
	}

	public int getLevel5Num() {
		return level5Num;
	}

	public void setLevel5Num(int level5Num) {
		this.level5Num = level5Num;
	}

	public int getLevel5NearValue() {
		return level5NearValue;
	}

	public void setLevel5NearValue(int level5NearValue) {
		this.level5NearValue = level5NearValue;
	}

	public int getLevel5CommonValue() {
		return level5CommonValue;
	}

	public void setLevel5CommonValue(int level5CommonValue) {
		this.level5CommonValue = level5CommonValue;
	}

	public int getLevel5FarValue() {
		return level5FarValue;
	}

	public void setLevel5FarValue(int level5FarValue) {
		this.level5FarValue = level5FarValue;
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
}
