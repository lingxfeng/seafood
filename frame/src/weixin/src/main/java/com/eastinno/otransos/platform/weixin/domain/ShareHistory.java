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
 * 分享记录
 * @author nsz
 */
@Entity
@Table(name = "Disco_WeiXin_ShareHistory")
public class ShareHistory  extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@POLoad(name = "followerId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Follower follower;//分享人
	private Date createDate = new Date();//分享时间时间
	private String shareUrl;//分享的路径
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
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
