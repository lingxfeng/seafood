package com.eastinno.otransos.seafood.usercenter.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 购物车商品
 * @author cl
 */
@Entity(name = "Disco_Shop_ShoppingCart")
public class ShoppingCart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3203377820378664203L;
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	
	@POLoad(name = "shopProductId")
	@ManyToOne(fetch = FetchType.EAGER)
	private ShopProduct shopProduct;//所选商品
	
	@POLoad(name = "shopSpecId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopSpec shopSpec;//所选商品规格
	
	@POLoad(name = "memberId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopMember member; //当前会员
	
	@POLoad(name = "shopDistributorId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ShopDistributor shopDistributor;
	
	private Date operateTime=new Date();// 操作时间
	
	private Integer buyNum;//购买个数
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShopProduct getShopProduct() {
		return shopProduct;
	}

	public void setShopProduct(ShopProduct shopProduct) {
		this.shopProduct = shopProduct;
	}

	public ShopSpec getShopSpec() {
		return shopSpec;
	}

	public void setShopSpec(ShopSpec shopSpec) {
		this.shopSpec = shopSpec;
	}

	public ShopMember getMember() {
		return member;
	}

	public void setMember(ShopMember member) {
		this.member = member;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public ShopDistributor getShopDistributor() {
		return shopDistributor;
	}

	public void setShopDistributor(ShopDistributor shopDistributor) {
		this.shopDistributor = shopDistributor;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	
}
