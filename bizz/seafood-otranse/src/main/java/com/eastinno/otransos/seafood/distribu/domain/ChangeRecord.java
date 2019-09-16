package com.eastinno.otransos.seafood.distribu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 分销商
 * @author nsz
 *
 */
@Entity(name = "Disco_Shop_ChangeRecord")
public class ChangeRecord implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private ShopMember oldMember;//更改前的推荐人
	private ShopMember newMembe;//更改后的推荐人
	private Date createTime=new Date();//更改推荐关系时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ShopMember getOldMember() {
		return oldMember;
	}
	public void setOldMember(ShopMember oldMember) {
		this.oldMember = oldMember;
	}
	public ShopMember getNewMembe() {
		return newMembe;
	}
	public void setNewMembe(ShopMember newMembe) {
		this.newMembe = newMembe;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
