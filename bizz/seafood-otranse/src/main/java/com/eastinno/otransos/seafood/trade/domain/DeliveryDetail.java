package com.eastinno.otransos.seafood.trade.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.security.domain.TenantObject;
/**
 * 配送详情
 * @author nsz
 *
 */
@Entity(name = "Disco_Shop_DeliveryDetail")
public class DeliveryDetail extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private Delivery delivery;//所属配送方式
	private Integer sequence=0;
	private String resgIds;//地区ids
	private String resgNames;//地区名称s
	private Integer weightIndex=0;//首重
	private Integer weightAdd=0;//续重
	private Double amtIndex=0.0;//首付
	private Double amtAdd=0.0;//续费
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Delivery getDelivery() {
		return delivery;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getResgIds() {
		return resgIds;
	}
	public void setResgIds(String resgIds) {
		this.resgIds = resgIds;
	}
	public String getResgNames() {
		return resgNames;
	}
	public void setResgNames(String resgNames) {
		this.resgNames = resgNames;
	}
	public Integer getWeightIndex() {
		return weightIndex;
	}
	public void setWeightIndex(Integer weightIndex) {
		this.weightIndex = weightIndex;
	}
	public Integer getWeightAdd() {
		return weightAdd;
	}
	public void setWeightAdd(Integer weightAdd) {
		this.weightAdd = weightAdd;
	}
	public Double getAmtIndex() {
		return amtIndex;
	}
	public void setAmtIndex(Double amtIndex) {
		this.amtIndex = amtIndex;
	}
	public Double getAmtAdd() {
		return amtAdd;
	}
	public void setAmtAdd(Double amtAdd) {
		this.amtAdd = amtAdd;
	}
	
}
