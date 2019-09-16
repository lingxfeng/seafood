package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.dao.IShoppingCartDAO;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.shop.usercenter.service.IShoppingCartService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ShoppingCartServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShoppingCartServiceImpl implements IShoppingCartService{
	@Resource
	private IShoppingCartDAO shoppingCartDao;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
	private IShopSpecService shopSpecService;
	public void setShoppingCartDao(IShoppingCartDAO shoppingCartDao){
		this.shoppingCartDao=shoppingCartDao;
	}
	
	public Long addShoppingCart(ShoppingCart shoppingCart) {	
		this.shoppingCartDao.save(shoppingCart);
		if (shoppingCart != null && shoppingCart.getId() != null) {
			return shoppingCart.getId();
		}
		return null;
	}
	
	public ShoppingCart getShoppingCart(Long id) {
		ShoppingCart shoppingCart = this.shoppingCartDao.get(id);
		return shoppingCart;
		}
	
	public boolean delShoppingCart(Long id) {	
			ShoppingCart shoppingCart = this.getShoppingCart(id);
			if (shoppingCart != null) {
				this.shoppingCartDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShoppingCarts(List<Serializable> shoppingCartIds) {
		
		for (Serializable id : shoppingCartIds) {
			delShoppingCart((Long) id);
		}
		return true;
	}
	
	public IPageList getShoppingCartBy(IQueryObject queryObj) {	
		return this.shoppingCartDao.findBy(queryObj);		
	}
	
	public boolean updateShoppingCart(Long id, ShoppingCart shoppingCart) {
		if (id != null) {
			shoppingCart.setId(id);
		} else {
			return false;
		}
		this.shoppingCartDao.update(shoppingCart);
		return true;
	}

	@Override
	public boolean clearShoppingCart(ShopOrderInfo shopOrderInfo) {
		List<ShopOrderdetail> sodList=shopOrderInfo.getOrderdetails();
		if(sodList!=null){
			for (ShopOrderdetail shopOrderdetail : sodList) {
				Long pid=shopOrderdetail.getPro().getId();
				DiscoShopUtil.removeCookie("discoshopCar", String.valueOf(pid));
			}
		}
		return true;
	}	
	public boolean liulanqitoCar(){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(member!=null){
			String valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(!"".equals(valueStr)){
				String[] id_num_specs = valueStr.split(",");
				QueryObject qo=null;
				for(String id_num_spec:id_num_specs){
					String eleId,eleNumStr,eleSpecStr;
					String[] eles=id_num_spec.split("_");
					int length=eles.length;
					if(length==1){
						eleId=eles[0];
						eleNumStr="1";
						eleSpecStr="0";
					}else if(length==2){
						eleId=eles[0];
						eleNumStr=eles[1];
						eleSpecStr="0";
					}else{
						eleId=eles[0];
						eleNumStr=eles[1];
						eleSpecStr=eles[2];
					}
					ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(eleId));
					qo = new QueryObject();
					qo.addQuery("obj.member.id",member.getId(),"=");
					qo.addQuery("obj.shopProduct.id",Long.parseLong(eleId),"=");
					if(eleSpecStr.equals("0")){
						qo.addQuery("obj.shopSpec is EMPTY");
					}else{
						qo.addQuery("obj.shopSpec.id",Long.parseLong(eleSpecStr),"=");
					}
					qo.setPageSize(1);
					List<?> list = this.getShoppingCartBy(qo).getResult();
					ShoppingCart shopCar=null;
					if(list!=null && list.size()>0){
						shopCar = (ShoppingCart) list.get(0);
						shopCar.setBuyNum(shopCar.getBuyNum()+Integer.parseInt(eleNumStr));
						this.updateShoppingCart(shopCar.getId(), shopCar);
					}else{
						shopCar = new ShoppingCart();
						shopCar.setBuyNum(Integer.parseInt(eleNumStr));
						shopCar.setMember(member);
						shopCar.setShopProduct(pro);
						if(!eleSpecStr.equals("0")){
							ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(eleSpecStr));
							shopCar.setShopSpec(spec);
						}
						this.addShoppingCart(shopCar);
					}
				}
			}
			
		}
		DiscoShopUtil.setCookieByVal("discoshopCar", null);
		return true;
	}

	@Override
	public boolean clearShoppingCart(String ccId) {
		String cIds[]=ccId.split(",");
		for (String cartId : cIds) {
			System.out.println("cartId="+cartId);
			this.shoppingCartDao.delete(Long.valueOf(cartId));
		}
		return true;
	}
}
