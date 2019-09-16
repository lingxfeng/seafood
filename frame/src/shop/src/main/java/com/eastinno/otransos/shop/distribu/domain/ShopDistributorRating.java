package com.eastinno.otransos.shop.distribu.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 分销等级
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_disRating")
public class ShopDistributorRating {
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Short type=0;//佣金获取类型:0.自动升级，1.自定义等级
	@Column(length=100,nullable=false)
	private String name;//等级名称
	private Float conditionf=0F;//等级条件
	private Float  leve1=1F;//一级分销佣金倍数
	private Float  leve2=1F;//二级分销佣金倍数
	private Float  leve3=1F;//三级分销佣金倍数
	private String remark;//备注
	private Date createTime=new Date();//创建时间
	private Date modifiedTime;//修改时间
	private Boolean isdefault;//是否是初始默认等级 
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Float getConditionf() {
		return conditionf;
	}
	public void setConditionf(Float conditionf) {
		this.conditionf = conditionf;
	}
	public Float getLeve1() {
		return leve1;
	}
	public void setLeve1(Float leve1) {
		this.leve1 = leve1;
	}
	public Float getLeve2() {
		return leve2;
	}
	public void setLeve2(Float leve2) {
		this.leve2 = leve2;
	}
	public Float getLeve3() {
		return leve3;
	}
	public void setLeve3(Float leve3) {
		this.leve3 = leve3;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Boolean getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}
	
}
