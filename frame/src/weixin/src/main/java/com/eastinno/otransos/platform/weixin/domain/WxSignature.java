package com.eastinno.otransos.platform.weixin.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.security.domain.TenantObject;
/**
 * 梦坊国际微签名
 * @author Administrator
 *
 */
@Entity(name = "Disco_WeiXin_WxSignature")
public class WxSignature extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private String createDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	@POLoad(name = "accountId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Account account;
	@POLoad(name = "followerId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Follower follower;//获粉丝
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
