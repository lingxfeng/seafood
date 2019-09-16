package com.eastinno.otransos.seafood.core.service;

import java.util.List;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.domain.ShopFloor;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 购物前台显示接口
 * @author Administrator
 *
 */
public interface IGoShopService {
	/**
	 * 根据主键获取商品分类
	 * @param id
	 * @return
	 */
	ProductType getProductType(Long id);
	/**
	 * 根据查询条件查询分类
	 * @param qo
	 * @return
	 */
	List<ProductType> getProductType(QueryObject qo);
	/**
	 * 获取一级商品分类
	 * @return
	 */
	List<ProductType> getTopPTypes();
	/**
	 * 查询商品
	 * @param qo
	 * @return
	 */
	IPageList queryProduct(QueryObject qo);
	/**
	 * 查询分类下置顶的商品
	 * @param pType
	 * @return
	 */
	ShopProduct getIsTop(ProductType pType);
	/**
	 * 根据主键获取商品
	 * @param id
	 * @return
	 */
	ShopProduct getProduct(Long id);
	/**
	 * 热销排行
	 * @return
	 */
	List<ShopProduct> getProBySaleNum();
	/**
	 * 收藏排行
	 */
	List<ShopProduct> getProCollectNum();
	/**
	 * 查询楼层
	 * @return
	 */
	List<ShopFloor> getFloors();
	/**
	 * 获取站点配置
	 * @return
	 */
	ShopSystemConfig getSysConfig();
	/**
	 * 查询订单
	 * @param qo
	 * @return
	 */
	List<ShopOrderInfo> getOrderInfo(QueryObject qo);
	/**
	 * 从数据库获取登录用户
	 * @return
	 */
	ShopMember getShopMember();
	/**
	 * 更新用户
	 * 
	 */
	void updateMember(ShopMember sp);
	/**
	 * 更新商品
	 * @param pro
	 */
	void updatePro(ShopProduct pro);
	
	/**
	 * 得到商品的咨询
	 * @param id
	 */
	List<ShopDiscuss> getGoodsAdvice(Long id,Integer type);
	
	/**
	 * 得到商品的咨询数量
	 * @param id
	 */
	Integer getAdviceCount(Long id,Integer type);
	
	/**
	 * 得到商品的评价
	 * @param id
	 */
	List<ShopDiscuss> getGoodsEvaluation(WebForm form,Long id,Integer type);
	
	/**
	 * 得到商品的评论数量
	 * @param id
	 */
	Integer getEvaluationCount(Long id,Integer type);
}

