package com.eastinno.otransos.seafood.content.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 回复
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopReply")
public class ShopReply {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@POLoad(name = "discussId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopDiscuss discuss;
	
	private String content;
	@POLoad(name = "userId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;
	@POLoad(name = "productId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct product;
	private Date date = new Date();
	private Integer type=1;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopDiscuss getDiscuss() {
		return discuss;
	}
	public void setDiscuss(ShopDiscuss discuss) {
		this.discuss = discuss;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ShopMember getUser() {
		return user;
	}
	public void setUser(ShopMember user) {
		this.user = user;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public ShopProduct getProduct() {
		return product;
	}
	public void setProduct(ShopProduct product) {
		this.product = product;
	}
}
