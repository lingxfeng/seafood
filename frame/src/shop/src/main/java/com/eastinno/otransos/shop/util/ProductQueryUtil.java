package com.eastinno.otransos.shop.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shop.droduct.domain.AttributeValue;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * 商品查询
 * @author Administrator
 *
 */
public class ProductQueryUtil {
	private ShopUtil shopUtil;//工具
	private Short status=1;
	private ProductType productType;
	
	private String properties;//属性
	private String keyword;
	private Brand brand;
	private String name;
	private Boolean isRecommend=null;
	private Boolean isTop=null;
	private String orderBy;
	private Boolean isDesc=false;
	private Integer num=20;
	private Integer curPage=1;
	private String brandName;
	private String brandCode;//品牌code
	
	public ProductQueryUtil setBrandCode(String brandCode) {
		this.brandCode = brandCode;
		return this;
	}

	public ProductQueryUtil setBrandName(String brandName) {
		this.brandName = brandName;
		return this;
	}

	public ProductQueryUtil(ShopUtil shopUtil){
		this.shopUtil = shopUtil;
	}
	
	public ProductQueryUtil setShopUtil(ShopUtil shopUtil) {
		this.shopUtil = shopUtil;
		return this;
	}

	public ProductQueryUtil setStatus(Short status) {
		this.status = status;
		return this;
	}
	public ProductQueryUtil setStatus(String status) {
		this.status = Short.parseShort(status);
		return this;
	}

	public ProductQueryUtil setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}
	public ProductQueryUtil setProductTypeId(String id){
		this.productType = this.shopUtil.getProductTypeService().getProductType(Long.parseLong(id));
		return this;
	}
	public ProductQueryUtil setProperties(String properties) {
		this.properties = properties;
		return this;
	}

	public ProductQueryUtil setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public ProductQueryUtil setBrand(Brand brand) {
		this.brand = brand;
		return this;
	}
	public ProductQueryUtil setBrandId(String id){
		this.brand = this.shopUtil.getBrandService().getBrand(Long.parseLong(id));
		return this;
	}
	public ProductQueryUtil setName(String name) {
		this.name = name;
		return this;
	}

	public ProductQueryUtil setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
		return this;
	}
	public ProductQueryUtil setIsRecommend(String isRecommend) {
		this.isRecommend = Boolean.parseBoolean(isRecommend);
		return this;
	}
	public ProductQueryUtil setIsTop(Boolean isTop) {
		this.isTop = isTop;
		return this;
	}
	public ProductQueryUtil setIsTop(String isTop) {
		this.isTop = Boolean.parseBoolean(isTop);
		return this;
	}
	public ProductQueryUtil setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	
	public ProductQueryUtil setIsDesc(Boolean isDesc) {
		this.isDesc = isDesc;
		return this;
	}
	public ProductQueryUtil setIsDesc(String isDesc) {
		this.isDesc = Boolean.parseBoolean(isDesc);
		return this;
	}
	public ProductQueryUtil setNum(String num) {
		this.num = Integer.parseInt(num);
		return this;
	}
	public ProductQueryUtil setNum(Integer num) {
		this.num = num;
		return this;
	}
	public ProductQueryUtil setCurPage(String curPage) {
		this.curPage = Integer.parseInt(curPage);
		return this;
	}
	public ProductQueryUtil setCurPage(Integer curPage) {
		this.curPage = curPage;
		return this;
	}
	public List<?> list(){
		QueryObject qo = new QueryObject();
		if(StringUtils.hasText(this.name)){
			qo.addQuery("obj.name","%"+this.name+"%","like");
		}
		if(this.status!=null){
			qo.addQuery("obj.status",this.status,"=");
		}else{
			qo.addQuery("obj.status",Short.parseShort("1"),"=");
		}
		if(StringUtils.hasText(this.keyword)){
			qo.addQuery("obj.name","%"+this.keyword+"%","like");
		}
		if(this.brand!=null){
			qo.addQuery("obj.brand",this.brand,"=");
		}
		if(this.productType!=null){
			qo.addQuery("obj.productType.dePath",this.productType.getDePath()+"%","like");
		}
		if(this.isRecommend!=null){
			qo.addQuery("obj.isRecommend",this.isRecommend,"=");
		}
		if(this.isTop!=null){
			qo.addQuery("obj.isTop",this.isTop,"=");
		}
		if(this.properties!=null && !"".equals(properties)){
			QueryObject qoav = new QueryObject();
			String[] keyVals = properties.split(",");
			String queryAttrs = "(";
			for(String keyValStr:keyVals){
				String[] keyVal = keyValStr.split("_");
				queryAttrs+="(obj.attributeKey.id="+keyVal[0]+" and obj.value='"+keyVal[1]+"') or ";
			}
			queryAttrs = queryAttrs.substring(0, queryAttrs.length()-4)+")";
			qoav.addQuery(queryAttrs);
			List<AttributeValue> attrVals = this.shopUtil.getAttributeValueService().getAttributeValueBy(qo).getResult();
			String ids = "0";
			if(attrVals!=null){
				for(AttributeValue a:attrVals){
					ids+=","+a.getProduct().getId();
				}
			}
			ids = "("+ids+")";
			qo.addQuery("obj.id in "+ids);
		}
		if(StringUtils.hasText(this.orderBy)){
			qo.setOrderBy(orderBy);
			if(this.isDesc){
				qo.setOrderType("desc");
			}
		}
		qo.setPageSize(this.num);
		qo.setCurrentPage(this.curPage);
		Tenant t = null;
		try {
			t = TenantContext.getTenant();
		} catch (Exception e) {
		}
		if(t!=null){
			qo.addQuery("obj.tenant.id",t.getId(),"=");
		}
		if(StringUtils.hasText(this.brandName)){
			try {
				brandName = new String(this.brandName.getBytes("ISO-8859-1"),"utf-8");
				qo.addQuery("obj.brand.name","%"+this.brandName+"%","like");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		if(StringUtils.hasText(this.brandCode)){
			qo.addQuery("obj.brand.code", this.brandCode, "=");
		}
		IPageList pl = this.shopUtil.getShopProductService().getShopProductBy(qo);
		if(pl.getPages()<this.curPage){
			return null;
		}
		return pl.getResult();
	}
}
