package com.eastinno.otransos.seafood.content.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 橱窗管理
 * @author Administrator
 */
@Entity(name = "Disco_Shop_ShopDw")
public class ShopDisw {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length=100)
	private String imgPath;
	@Column(length=50)
	private String name;
	@Column(length=200)
	private String proDuctids;
	private Integer sequence=1;
	private Integer status=1;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProDuctids() {
		return proDuctids;
	}
	public void setProDuctids(String proDuctids) {
		this.proDuctids = proDuctids;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
