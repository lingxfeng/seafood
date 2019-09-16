package com.eastinno.otransos.shop.content.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

/**
 * 评论资讯管理
 * @author nsz
 */
@Entity(name = "Disco_Shop_ShopDiscuss")
public class ShopDiscuss extends TenantObject{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Date createDate=new Date();//评论日期
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String content;//评论内容
	private Integer startdis=1;//评论星级 1:差评 2-3中评 4-5：好评
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopMember user;
	private Integer type=1;//类型1，评论 2:咨询
	@POLoad(name = "proId")
	@ManyToOne(fetch=FetchType.LAZY)
	private ShopProduct pro;
	private Integer otherType=1;
	private Boolean isHf=false;
	@OneToOne(fetch=FetchType.LAZY)
	private ShopOrderdetail shopOrderdetail;
	@OneToMany(mappedBy="discuss",fetch=FetchType.LAZY)
	private List<ShopReply> replysList = new ArrayList<ShopReply>();
	
	private Boolean isShow=true;//
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStartdis() {
		return startdis;
	}
	public void setStartdis(Integer startdis) {
		this.startdis = startdis;
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
	public ShopProduct getPro() {
		return pro;
	}
	public void setPro(ShopProduct pro) {
		this.pro = pro;
	}
	public Boolean getIsHf() {
		return isHf;
	}
	public void setIsHf(Boolean isHf) {
		this.isHf = isHf;
	}
	public Integer getOtherType() {
		return otherType;
	}
	public void setOtherType(Integer otherType) {
		this.otherType = otherType;
	}
	
	public ShopOrderdetail getShopOrderdetail() {
		return shopOrderdetail;
	}
	public void setShopOrderdetail(ShopOrderdetail shopOrderdetail) {
		this.shopOrderdetail = shopOrderdetail;
	}
	public List<ShopReply> getReplysList() {
		return replysList;
	}
	public void setReplysList(List<ShopReply> replysList) {
		this.replysList = replysList;
	}
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
}
