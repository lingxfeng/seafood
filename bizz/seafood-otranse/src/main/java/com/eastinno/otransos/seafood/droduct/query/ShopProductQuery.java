package com.eastinno.otransos.seafood.droduct.query;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.droduct.domain.AttributeValue;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.service.IAttributeValueService;
import com.eastinno.otransos.web.ActionContext;
/**
 * 商品查询
 * @author Administrator
 *
 */
public class ShopProductQuery extends QueryObject{
	private Short status;
	@POLoad(name = "pid")
	private ProductType productType;
	private String orderBy;
	private String properties;
	private String keyword;
	@POLoad(name="brandId")
	private Brand brand;
	private String name;
	private Integer goods_inventory=-1;
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public Integer getGoods_inventory() {
		return goods_inventory;
	}

	public void setGoods_inventory(Integer goods_inventory) {
		this.goods_inventory = goods_inventory;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public void customizeQuery() {
		if(this.name!=null){
			this.addQuery("obj.name","%"+this.name+"%","like");
		}
		if(this.status!=null){
			this.addQuery("obj.status",this.status,"=");
		}else{
			this.addQuery("obj.status",Short.parseShort("1"),"=");
		}
		if(this.keyword!=null && !"".equals(this.keyword)){
			this.addQuery("obj.name","%"+this.keyword+"%","like");
		}
		if(this.brand!=null){
			this.addQuery("obj.brand",this.brand,"=");
		}
		if(this.goods_inventory==0){
			this.addQuery("obj.inventory",Integer.parseInt("0"),">");
		}
		if(this.productType!=null){
			this.addQuery("(obj.productType.id="+this.productType.getId()+" or obj.productType.parent.id="+this.productType.getId()+" or obj.productType.parent.parent.id="+this.productType.getId()+")");
		}
		if(this.orderBy!=null){
			this.setOrderBy(orderBy);
			if(this.orderType!=null){
				this.setOrderType(orderType);
			}
		}
		if(this.properties!=null && !"".equals(properties)){
			ServletContext sc = ActionContext.getContext().getServletContext();
			ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(sc);
			IAttributeValueService attrValService = ac2.getBean(IAttributeValueService.class);
			QueryObject qo = new QueryObject();
			String[] keyVals = properties.split(",");
			String queryAttrs = "(";
			for(String keyValStr:keyVals){
				String[] keyVal = keyValStr.split("_");
				queryAttrs+="(obj.attributeKey.id="+keyVal[0]+" and obj.value='"+keyVal[1]+"') or ";
			}
			queryAttrs = queryAttrs.substring(0, queryAttrs.length()-4)+")";
			qo.addQuery(queryAttrs);
			List<AttributeValue> attrVals = attrValService.getAttributeValueBy(qo).getResult();
			String ids = "0";
			if(attrVals!=null){
				for(AttributeValue a:attrVals){
					ids+=","+a.getProduct().getId();
				}
			}
			ids = "("+ids+")";
			this.addQuery("obj.id in "+ids);
		}
		
        super.customizeQuery();
    }
}
