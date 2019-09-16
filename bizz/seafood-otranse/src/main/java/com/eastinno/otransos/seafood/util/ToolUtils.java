package com.eastinno.otransos.seafood.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.service.IAttributeKeyService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
/**
 * 前台取数据工具
 * @author Administrator
 */
@Component
public class ToolUtils {
	@Autowired
    private IProductTypeService productTypeService;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
	private IAttributeKeyService attributeKeyService;
	@Autowired
	private IShopMemberService shopMemberService;
	@Autowired
	private ISystemMenuService systemMenuService;
	/**
	 * 按顺序获取1级分类下前20第三级小类
	 * @param pType
	 * @return
	 */
	public List<ProductType> getChilrenType(ProductType pType,int num){
		QueryObject qo = new QueryObject();
		if(pType.getLevel()==1){
			qo.addQuery("obj.parent.parent",pType,"=");
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.parent",pType,"=");
		}else{
			return null;
		}
		qo.setPageSize(num);
		qo.setOrderBy("sequence");
		return this.productTypeService.getProductTypeBy(qo).getResult();
	}
	/**
	 * 获取前num个推荐的小类
	 * @param pType
	 * @return
	 */
	public List<ProductType> getChilrenTypeIsRecommend(ProductType pType,int num){
		QueryObject qo = new QueryObject();
		if(pType.getLevel()==1){
			qo.addQuery("obj.parent.parent",pType,"=");
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.parent",pType,"=");
		}else{
			return null;
		}
		qo.setPageSize(num);
		qo.setOrderBy("sequence");
		qo.addQuery("obj.isRecommend",Boolean.TRUE,"=");
		return this.productTypeService.getProductTypeBy(qo).getResult();
	}
	/**
	 * 获取下级分类下推荐的商品
	 * @param pType
	 * @param num
	 * @return
	 */
	public List<ShopProduct> getProIsRecommend(ProductType pType,int num){
		QueryObject qo = new QueryObject();
		if(pType.getLevel()==3){
			qo.addQuery("obj.productType",pType,"="	);
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.productType.parent",pType,"="	);
		}else{
			qo.addQuery("obj.productType.parent.parent",pType,"="	);
		}
		qo.setPageSize(num);
		qo.setOrderBy("sequence");
		qo.addQuery("obj.isRecommend",Boolean.TRUE,"=");
		return this.shopProductService.getShopProductBy(qo).getResult();
	}
	/**
	 * 获取置顶商品
	 * @param pType
	 * @param num
	 * @return
	 */
	public List<ShopProduct> getProIsTop(ProductType pType,int num){
		QueryObject qo = new QueryObject();
		if(pType.getLevel()==3){
			qo.addQuery("obj.productType",pType,"="	);
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.productType.parent",pType,"="	);
		}else{
			qo.addQuery("obj.productType.parent.parent",pType,"="	);
		}
		qo.addQuery("obj.isTop",Boolean.TRUE,"=");
		qo.setPageSize(num);
		qo.setOrderBy("sequence");
		List<ShopProduct> list = this.shopProductService.getShopProductBy(qo).getResult();
		return list;
	}
	/**
	 * 按销量查询商品
	 * @param pType
	 * @param num
	 * @return
	 */
	public List<ShopProduct> getProSaleNum(ProductType pType,int num){
		QueryObject qo = new QueryObject();
		if(pType.getLevel()==3){
			qo.addQuery("obj.productType",pType,"="	);
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.productType.parent",pType,"="	);
		}else{
			qo.addQuery("obj.productType.parent.parent",pType,"="	);
		}
		qo.setPageSize(num);
		qo.setOrderBy("saleNum desc");
		List<ShopProduct> list = this.shopProductService.getShopProductBy(qo).getResult();
		return list;
	}
	/**
	 * 获取属性字符串
	 * @param attriVal
	 * @return
	 */
	public String getAttrValStr(String attriVal){
		String str = "";
		String kVal[] = attriVal.split("_");
		if(kVal.length>0){
			AttributeKey ak = this.attributeKeyService.getAttributeKey(Long.parseLong(kVal[0]));
			int valInt = Integer.parseInt(kVal[1]);
			/*String[] vals = ak.getValue().split(",");
			if(vals.length>(valInt-1)){
				str = vals[(valInt-1)];
			}*/
		}
		return str;
	}
	/**
	 * 获取推荐的小类
	 * @param pType
	 * @return
	 */
	public List<ProductType> getIsRecommend(ProductType pType){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.isRecommend",Boolean.TRUE,"=");
		if(pType.getLevel()==1){
			qo.addQuery("obj.parent.parent",pType,"=");
		}else if(pType.getLevel()==2){
			qo.addQuery("obj.parent",pType,"=");
		}
		List<ProductType> list = this.productTypeService.getProductTypeBy(qo).getResult();
		return list;
	}
	public String urlenCode(String url){
		String newUrl="";
		try {
			newUrl = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newUrl;
	}
	/**
	 * 计算时间距离当前时间
	 * @param date1
	 * @param date2
	 * @return
	 */
	public double datetoDate(int dates,Date date2){
		long datel1 = new Date().getTime();
		long datel2 = date2.getTime()+(dates*24*60*60*1000);
		double cha = (datel2-datel1)/(1000*60*60*24);
		return Math.floor(cha);
	}
	public int strtoint(String val){
		return Integer.parseInt(val)-1;
	}
	public List<SystemMenu> getMenu(SystemMenu menu){
		ShiroUser sUser = ShiroUtils.getShiroUser();
		QueryObject qo = new QueryObject();
		User user = ShiroUtils.getUser();
		if(user.getIsTenantAdmin()){
			qo.addQuery("(obj.tenant.id="+user.getTenant().getId()+" or obj.id in("+sUser.getMenuIds()+"))");
		}else{
			qo.addQuery("obj.id in("+sUser.getMenuIds()+")");
		}
		if(menu==null){
			qo.addQuery("obj.parent is EMPTY");
		}else{
			qo.addQuery("obj.parent",menu,"=");
		}
		qo.addQuery("obj.type",Integer.parseInt("2"),"=");
		qo.addQuery("obj.status",Integer.parseInt("0"),"=");
		qo.setOrderBy("sequence");
		List<SystemMenu> menus = this.systemMenuService.getSystemMenuBy(qo).getResult();
		return menus;
	}
}
