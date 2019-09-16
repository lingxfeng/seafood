package com.eastinno.otransos.seafood.distribu.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
/**
 * 佣金设置
 * @author dll
 *
 */
@Entity(name = "Disco_Shop_commission")
public class CommissionConfig {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private Float totalcomission;//总佣金
	private Short type=0;//佣金获取类型:0.比例，1.固定金额
	private Boolean isdefault=false;//是否默认设置
	private Float  leve1=0f;//一级分销佣金
	private Float  leve2=0f;//二级分销佣金
	private Float  leve3=0f;//三级分销佣金
	@OneToOne(fetch=FetchType.LAZY)
	private ShopProduct product;//关联的商品
	private Date createTime=new Date();//创建时间
	private Date modifiedTime;//修改时间
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Float getTotalcomission() {
		return totalcomission;
	}
	public void setTotalcomission(Float totalcomission) {
		this.totalcomission = totalcomission;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Boolean getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}
	public Float getLeve1() {
		return leve1;
	}
	public void setLeve1(Float leve1) {
		this.leve1 = leve1;
	}
	public Float getLeve2() {
		return leve2;
	}
	public void setLeve2(Float leve2) {
		this.leve2 = leve2;
	}
	public Float getLeve3() {
		return leve3;
	}
	public void setLeve3(Float leve3) {
		this.leve3 = leve3;
	}
	public ShopProduct getProduct() {
		return product;
	}
	public void setProduct(ShopProduct product) {
		this.product = product;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
}
