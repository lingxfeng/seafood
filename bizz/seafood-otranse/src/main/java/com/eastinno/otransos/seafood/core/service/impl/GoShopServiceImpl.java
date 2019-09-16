package com.eastinno.otransos.seafood.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.domain.ShopFloor;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.content.service.IShopFloorService;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.IGoShopService;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.service.IAttributeValueService;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
@Component
public class GoShopServiceImpl implements IGoShopService{
	@Autowired
	private IProductTypeService productTypeService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
    private IAttributeValueService attributeValueService;
	@Autowired
    private IShopFloorService shopFloorService;
	@Autowired
    private IShopSystemConfigService shopSystemConfigService;
	@Autowired
	private IShopOrderInfoService shopOrderInfoService;
	@Autowired
	private IShopMemberService shopMemberService;
	@Autowired
	private IShopDiscussService shopDiscussService;
	@Override
	public ProductType getProductType(Long id) {
		return this.productTypeService.getProductType(id);
	}
	@Override
	public List<ProductType> getTopPTypes() {
		QueryObject qo =new QueryObject();
		qo.addQuery("obj.parent is EMPTY");
		qo.setOrderBy("sequence");
		qo.setOrderType("asc");
		return this.productTypeService.getProductTypeBy(qo).getResult();
	}
	public IPageList queryProduct(QueryObject qo) {
		return this.shopProductService.getShopProductBy(qo);
	}
	@Override
	public ShopProduct getIsTop(ProductType pType) {
		QueryObject qo = new QueryObject();
		if(pType!=null){
			if(pType.getLevel()==3){
				qo.addQuery("obj.productType",pType,"=");
			}else if(pType.getLevel()==2){
				qo.addQuery("obj.productType.parent",pType,"=");
			}else if(pType.getLevel()==1){
				qo.addQuery("obj.productType.parent.parent",pType,"=");
			}
		}
		qo.addQuery("obj.isTop",Boolean.TRUE,"=");
		
		List<ShopProduct>  list = this.shopProductService.getShopProductBy(qo).getResult();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public ShopProduct getProduct(Long id) {
		return this.shopProductService.getShopProduct(id);
	}
	@Override
	public List<ShopProduct> getProBySaleNum() {
		QueryObject qo = new QueryObject();
		qo.setPageSize(10);
		qo.setOrderBy("saleNum desc");
		return this.shopProductService.getShopProductBy(qo).getResult();
	}
	@Override
	public List<ShopProduct> getProCollectNum() {
		QueryObject qo = new QueryObject();
		qo.setPageSize(10);
		qo.setOrderBy("collectNum desc");
		return this.shopProductService.getShopProductBy(qo).getResult();
	}
	@Override
	public List<ShopFloor> getFloors() {
		QueryObject qo = new QueryObject();
		qo.setOrderBy("sequence");
		return this.shopFloorService.getShopFloorBy(qo).getResult();
	}
	@Override
	public ShopSystemConfig getSysConfig() {
		QueryObject qo = new QueryObject();
		List<ShopSystemConfig> list = this.shopSystemConfigService.getShopSystemConfigBy(qo).getResult();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<ProductType> getProductType(QueryObject qo) {
		List<ProductType> list = this.productTypeService.getProductTypeBy(qo).getResult();
		return list;
	}
	@Override
	public List<ShopOrderInfo> getOrderInfo(QueryObject qo) {
		List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		return list;
	}
	@Override
	public ShopMember getShopMember() {
		User user = DiscoShopUtil.getUser();
		if(user!=null){
			return this.shopMemberService.getShopMember(user.getId());
		}
		return null;
	}
	@Override
	public void updateMember(ShopMember sp) {
		this.shopMemberService.updateShopMember(sp.getId(), sp);
	}
	@Override
	public void updatePro(ShopProduct pro) {
		this.shopProductService.updateShopProduct(pro.getId(), pro);
	}
	@Override
	public List<ShopDiscuss> getGoodsAdvice(Long id,Integer type) {
		QueryObject qoObject = new QueryObject();
		qoObject.addQuery("obj.type", 2, "=");
		if(type>0){
			qoObject.addQuery("obj.otherType", type, "=");
		}
		qoObject.addQuery("obj.pro.id", id, "=");
		return this.shopDiscussService.getShopDiscussBy(qoObject).getResult();
	}
	@Override
	public Integer getAdviceCount(Long id, Integer type) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.type", 2, "=");
		qo.addQuery("obj.pro.id", id, "=");
		if(type!=0){
			qo.addQuery("obj.otherType", type, "=");
		}
		qo.setLimit(-1);
		
		return this.shopDiscussService.getShopDiscussBy(qo).getRowCount();
	}
	@Override
	public List<ShopDiscuss> getGoodsEvaluation(WebForm form,Long id, Integer type) {
		Integer currentPage=CommUtil.null2Int(form.get("currentPage"));
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.type", 1, "=");
		qo.addQuery("obj.pro.id", id, "=");
		qo.setCurrentPage(currentPage);
		if (type==1) {
			qo.addQuery("obj.startdis="+1);
		}else if(type==2){
			qo.addQuery("obj.startdis>"+1+" and obj.startdis<5");
		}else if(type==3){
			qo.addQuery("obj.startdis="+5);
		}
		IPageList iPageList=this.shopDiscussService.getShopDiscussBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		return iPageList.getResult();
	}
	@Override
	public Integer getEvaluationCount(Long id, Integer type) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pro.id", id, "=");
		qo.addQuery("obj.type", 1, "=");
		if (type==1) {
			qo.addQuery("obj.startdis="+1);
		}else if(type==2){
			qo.addQuery("obj.startdis>"+1+" and obj.startdis<5");
		}else if(type==3){
			qo.addQuery("obj.startdis="+5);
		}
		return this.shopDiscussService.getShopDiscussBy(qo).getRowCount();
	}
}
