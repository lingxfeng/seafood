package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * 获奖记录
 * @author nsz
 *
 */
@Entity
@Table(name = "Disco_WeiXin_PrizeHistory")
public class PrizeHistory extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@POLoad(name = "followerId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Follower follower;//获奖人
	private Date createDate = new Date();//获奖时间
	@POLoad(name = "prizeProId")
	@ManyToOne(fetch=FetchType.LAZY)
	private PrizePro prizePro;//所获奖品
	@POLoad(name = "accountId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Account account;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Follower getFollower() {
		return follower;
	}
	public void setFollower(Follower follower) {
		this.follower = follower;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public PrizePro getPrizePro() {
		return prizePro;
	}
	public void setPrizePro(PrizePro prizePro) {
		this.prizePro = prizePro;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
