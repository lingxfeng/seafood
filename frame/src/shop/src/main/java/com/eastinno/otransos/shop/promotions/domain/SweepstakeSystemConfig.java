package com.eastinno.otransos.shop.promotions.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * 商品
 * @author nsz
 */
@Entity(name = "Disco_Shop_SweepstakesSystem")
public class SweepstakeSystemConfig{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String details;//规则详情
	private String imgPaths;//背景图片路径
	private Short type=0;//抽奖方式：0，积分，1；佣金 2可选择
	private int costInterval=0;//耗费积分
	private Float costCommission=0F;//耗费佣金
	private Short status=0;//是否开启，0，不进行抽奖活动，1，进行抽奖活动
	private Integer minbase=10000;//中奖概率最小基数
	private Integer count=0;//每天抽奖次数限制
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}

	public int getCostInterval() {
		return costInterval;
	}
	public void setCostInterval(int costInterval) {
		this.costInterval = costInterval;
	}
	public Float getCostCommission() {
		return costCommission;
	}
	public void setCostCommission(Float costCommission) {
		this.costCommission = costCommission;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Integer getMinbase() {
		return minbase;
	}
	public void setMinbase(Integer minbase) {
		this.minbase = minbase;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	
	

}
