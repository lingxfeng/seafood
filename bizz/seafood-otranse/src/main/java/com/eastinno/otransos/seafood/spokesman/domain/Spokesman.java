package com.eastinno.otransos.seafood.spokesman.domain;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributorRating;
import com.eastinno.otransos.seafood.droduct.domain.Couponlist;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * 代言人
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_Spokesman")
public class Spokesman {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Lob
    @Basic(fetch = FetchType.LAZY)
	private String dePath;
	private Float totalRestitution=0F; //总返现
	private Float availableRestitution=0F;//可用返现
	private Float totalSubsidy=0F; //总补贴
	private Float availableSubsidy=0F;//可用补贴
	@OneToOne(fetch=FetchType.LAZY)
	private SpokesmanRating spokesmanRating;//代言等级
	@OneToOne(fetch=FetchType.LAZY)
	private ShopMember member;//关联的会员
	@POLoad(name = "pspokesmanId")
	@OneToOne(fetch=FetchType.LAZY)
	private Spokesman pspokesman;//通过谁推荐
	private Float teamAmount=0F; //团队业绩
	private Short status=0;//0,未启用 1，已启用
	private Date createDate = new Date();//成为代言人时间
	private Short customRating=0;//0,默认等级1，自定义等级
	private Short agreeProtocal=0;//0,未同意协议 1，已同意协议
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDePath() {
		return dePath;
	}
	public void setDePath(String dePath) {
		this.dePath = dePath;
	}
	
	public Float getTotalRestitution() {
		return totalRestitution;
	}
	public void setTotalRestitution(Float totalRestitution) {
		this.totalRestitution = totalRestitution;
	}
	public Float getAvailableRestitution() {
		return availableRestitution;
	}
	public void setAvailableRestitution(Float availableRestitution) {
		this.availableRestitution = availableRestitution;
	}
	public Float getTotalSubsidy() {
		return totalSubsidy;
	}
	public void setTotalSubsidy(Float totalSubsidy) {
		this.totalSubsidy = totalSubsidy;
	}
	public Float getAvailableSubsidy() {
		return availableSubsidy;
	}
	public void setAvailableSubsidy(Float availableSubsidy) {
		this.availableSubsidy = availableSubsidy;
	}
	
	public SpokesmanRating getSpokesmanRating() {
		return spokesmanRating;
	}
	public void setSpokesmanRating(SpokesmanRating spokesmanRating) {
		this.spokesmanRating = spokesmanRating;
	}
	public ShopMember getMember() {
		return member;
	}
	public void setMember(ShopMember member) {
		this.member = member;
	}
	public Spokesman getPspokesman() {
		return pspokesman;
	}
	public void setPspokesman(Spokesman pspokesman) {
		this.pspokesman = pspokesman;
	}
	public Float getTeamAmount() {
		return teamAmount;
	}
	public void setTeamAmount(Float teamAmount) {
		this.teamAmount = teamAmount;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Short getCustomRating() {
		return customRating;
	}
	public void setCustomRating(Short customRating) {
		this.customRating = customRating;
	}
	public Short getAgreeProtocal() {
		return agreeProtocal;
	}
	public void setAgreeProtocal(Short agreeProtocal) {
		this.agreeProtocal = agreeProtocal;
	}
	
	
	
}
