package com.eastinno.otransos.seafood.usercenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 用户等级
 * @author Administrator
 *
 */
@Entity(name = "Disco_Shop_Rating")
public class ShopMemberRating {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false)
	private String name;//等级名称;
	
	private Long integral;//积分  铜牌:100 以下  银牌 300 以下  500以下
	@Column(length=200)
	private String imgPath;//等级图标
	
	private Integer sequence = 1;// 排序
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getIntegral() {
		return integral;
	}

	public void setIntegral(Long integral) {
		this.integral = integral;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
}
