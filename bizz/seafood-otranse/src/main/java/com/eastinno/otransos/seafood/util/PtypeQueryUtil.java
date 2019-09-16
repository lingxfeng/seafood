package com.eastinno.otransos.seafood.util;

import java.util.List;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;

/**
 * 商品分类查询
 * @author nsz
 */
public class PtypeQueryUtil {
	private ShopUtil shopUtil;//工具
	private Boolean isRecommend=false;//是否推荐
	private Boolean isChildren =false;
	private ProductType ancestor;
	private Integer level=0;
	private Boolean isShow=true;
	private Integer num=20;
	private Integer curPage=1;
	public PtypeQueryUtil setLevel(Integer level) {
		this.level = level;
		return this;
	}
	public PtypeQueryUtil(ShopUtil shopUtil){
		this.shopUtil = shopUtil;
	}
	
	public PtypeQueryUtil setIsShow(Boolean isShow) {
		this.isShow = isShow;
		return this;
	}
	public PtypeQueryUtil setIsShow(String isShow) {
		this.isShow = Boolean.parseBoolean(isShow);
		return this;
	}
	public PtypeQueryUtil setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
		return this;
	}
	public PtypeQueryUtil setIsRecommend(String isRecommend) {
		this.isRecommend = Boolean.parseBoolean(isRecommend);
		return this;
	}
	public PtypeQueryUtil setNum(String num) {
		this.num = Integer.parseInt(num);
		return this;
	}
	public PtypeQueryUtil setNum(Integer num) {
		this.num = num;
		return this;
	}
	public PtypeQueryUtil setIsChildren(Boolean isChildren) {
		this.isChildren = isChildren;
		return this;
	}
	public PtypeQueryUtil setIsChildren(String isChildren) {
		this.isChildren = Boolean.parseBoolean(isChildren);
		return this;
	}
	public PtypeQueryUtil setAncestor(ProductType ancestor) {
		this.ancestor = ancestor;
		return this;
	}
	public PtypeQueryUtil setAncestorId(String id){
		this.ancestor = this.shopUtil.getProductTypeService().getProductType(Long.parseLong(id));
		return this;
	}
	public List<?> list(){
		QueryObject qo = new QueryObject();
		if(this.isRecommend){
			qo.addQuery("obj.isRecommend",this.isRecommend,"=");
		}
		if(this.isShow){
			qo.addQuery("obj.isShow",this.isShow,"=");
		}
		if(this.isChildren){
			if(this.ancestor==null){
				qo.addQuery("obj.parent is EMPTY");
			}else{
				qo.addQuery("obj.parent",ancestor,"=");
			}
		}else{
			if(this.ancestor!=null){
				int leve = ancestor.getLevel();
				if(leve == 2){
					qo.addQuery("obj.dePath",ancestor.getDePath()+"%","like");
				}else{
					qo.addQuery("obj.dePath",ancestor.getDePath()+"@"+"%","like");
				}

			}
			if(this.level!=0){
				qo.addQuery("obj.level",this.level,"=");
			}
		}
		qo.setPageSize(num);
		qo.setCurrentPage(curPage);
		qo.setOrderBy("sequence");
		return this.shopUtil.getProductTypeService().getProductTypeBy(qo).getResult();
	}
}
