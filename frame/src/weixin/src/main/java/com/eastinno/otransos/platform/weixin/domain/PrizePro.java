package com.eastinno.otransos.platform.weixin.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * 奖品管理
 * @author nsz
 */
@Entity
@Table(name = "Disco_WeiXin_PrizePro")
public class PrizePro extends TenantObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@POLoad(name = "accountId")
	@ManyToOne(fetch=FetchType.LAZY)
	private Account account;
	private String name;//奖品名称
	private String imgPath;//奖品图片
	private Integer status;//是否有效
	private String intro;
	private Integer probability=0;//获奖几率
	private Integer sequence=0;//排序
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getProbability() {
		return probability;
	}
	public void setProbability(Integer probability) {
		this.probability = probability;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
