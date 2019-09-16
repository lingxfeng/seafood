package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.User;

/**
 * @Title: 粉丝、微信关注者
 * @author maowei
 * @date 2014-05-21 00:53:47
 * @version V1.0
 */
@Entity
@Table(name = "Disco_WeiXin_Follower")
@Inheritance(strategy = InheritanceType.JOINED)
public class Follower extends User {
	@POLoad(name = "accountId")
    @ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	private String accessToken;
	private String citystr;//城市
	private String provincestr;
	private String countrystr;
	private String headimgurl;//头像
	private String latitude;
	private String longitude;
	private String precisionstr;
	private Long lastTimesend=new Date().getTime();
	private Integer scorepoints=0;
	private Integer zlPoint=0;
	@POLoad(name = "followerGroupId")
    @ManyToOne(fetch = FetchType.LAZY)
	private FollowerGroup followerGroup;
	@OneToOne(fetch=FetchType.LAZY)
	public FollowerGroup getFollowerGroup() {
		return followerGroup;
	}
	public void setFollowerGroup(FollowerGroup followerGroup) {
		this.followerGroup = followerGroup;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
	public String getCitystr() {
		return citystr;
	}
	public void setCitystr(String citystr) {
		this.citystr = citystr;
	}
	public String getProvincestr() {
		return provincestr;
	}
	public void setProvincestr(String provincestr) {
		this.provincestr = provincestr;
	}
	public String getCountrystr() {
		return countrystr;
	}
	public void setCountrystr(String countrystr) {
		this.countrystr = countrystr;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPrecisionstr() {
		return precisionstr;
	}
	public void setPrecisionstr(String precisionstr) {
		this.precisionstr = precisionstr;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Long getLastTimesend() {
		return lastTimesend;
	}
	
	public Integer getZlPoint() {
		return zlPoint;
	}
	public void setZlPoint(Integer zlPoint) {
		this.zlPoint = zlPoint;
	}
	public void setLastTimesend(Long lastTimesend) {
		this.lastTimesend = lastTimesend;
	}
	public Integer getScorepoints() {
		return scorepoints;
	}
	public void setScorepoints(Integer scorepoints) {
		this.scorepoints = scorepoints;
	}
	@Override
	public Object toJSonObject() {
		return super.toJSonObject();
	}
	
}
