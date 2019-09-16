package com.eastinno.otransos.seafood.spokesman.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 代言人等级
 * @author dll
 *
 */

@Entity(name = "Disco_Shop_spokesmanRating")
public class SpokesmanRating {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length=100,nullable=false)
	private String name;//等级名称
	private Short rating=0;//等级序列(0,1,2,3等表示等级由低到高)
	private Float conditionf=0F;//等级条件
	private Float  leve=0F;//补贴比例
	private String remark;//备注
	private Date createTime=new Date();//创建时间
	private Date modifiedTime;//修改时间
	@Column(nullable = false, length = 100)
	private String imgPath;// 默认图片
	private Integer sequence = 1;// 排序
	
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
	
	public Short getRating() {
		return rating;
	}
	public void setRating(Short rating) {
		this.rating = rating;
	}
	public Float getConditionf() {
		return conditionf;
	}
	public void setConditionf(Float conditionf) {
		this.conditionf = conditionf;
	}
	public Float getLeve() {
		return leve;
	}
	public void setLeve(Float leve) {
		this.leve = leve;
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
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
