package com.eastinno.otransos.shop.droduct.query;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
/**
 * 商品分类查询
* @ClassName: ProductTypeQuery   
* @Description: TODO(这里用一句话描述这个类的作用)   
* @author zhenggang   
* @date 2015年4月2日 下午2:16:09
 */
public class ProductTypeQuery extends QueryObject{
	@POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
	private ProductType parent;
	private String name;
	@Override
    public void customizeQuery() {
		if(this.parent!=null){
			this.addQuery("obj.parent",this.parent,"=");
		}else{
			this.addQuery("obj.parent is EMPTY");
		}
		if(this.name!=null){
			this.addQuery("obj.name",this.name,"=");
		}
        super.customizeQuery();
    }
	public ProductType getParent() {
		return parent;
	}
	public void setParent(ProductType parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
