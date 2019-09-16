package com.eastinno.otransos.seafood.content.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 广告
 * @author cl
 */
@Entity(name = "Disco_Shop_ShopAdvert")
public class ShopAdvert implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@Column(length = 30)
	private String name;//名称
	@Column(length=30)
	private String title;//标题
	@Column(length=100)
	private String url;//图片地址
	private Integer sequence = 1;// 排序号
	@Column(length=100)
	private String linkUrl;//链接地址
	
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {""});
		return map;
	}
	
}
