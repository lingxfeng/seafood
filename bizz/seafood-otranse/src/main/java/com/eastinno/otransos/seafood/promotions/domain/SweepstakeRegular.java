package com.eastinno.otransos.seafood.promotions.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;

/**
 * 商品
 * @author dll
 */
@Entity(name = "Disco_Shop_Sweepstakes")
public class SweepstakeRegular{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	@Column(nullable = false, unique = true)
	private Integer position;//转盘上的顺时针位置,0,未开始
	private int rating;//概率
	private Short status=0;//奖品类型，0，不中奖，1，中奖为商品，2，中奖为优惠券，3，中奖为积分
	private String productname;//中奖商品
	private Integer integal=0;//若中奖为积分，获得积分额
	//@OneToOne
	//private Coupon coupon;//中奖的优惠券
	private String imgPaths;//奖品图片路径
	private String name;//奖项名称
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public Integer getIntegal() {
		return integal;
	}
	public void setIntegal(Integer integal) {
		this.integal = integal;
	}
	public String getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
