package com.eastinno.otransos.shop.usercenter.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import antlr.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.mvc.ajax.TenantAction;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorRatingService;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IProductTypeService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.shop.usercenter.service.IShoppingCartService;

/**
 * ShoppingCartAction
 * @author 
 */
@Action
public class ShoppingCartAction extends WxShopBaseAction {
    @Inject
    private IShoppingCartService service;
    
    @Inject
    private IShopDistributorService shopDistributorService;
    
    @Inject
    private IShopProductService shopProductService;
    
    @Inject
    private IShopSpecService shopSpecService;
    
    @Inject
    private IProductTypeService productTypeService;
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getShoppingCartBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShoppingCart entry = (ShoppingCart)form.toPo(ShoppingCart.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShoppingCart(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShoppingCart entry = this.service.getShoppingCart(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShoppingCart(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 跳转 购物车页面
     * 
     * @param form
     */
    public Page doToShoppingCart(WebForm form) {
    	//判断身份
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		String flag = null;
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member",user,"=");
		List<ShopDistributor> listdis = this.shopDistributorService.getShopDistributorBy(qos).getResult();
		if(listdis!=null && listdis.size()!=0){
			ShopDistributor mydis = listdis.get(0);
			if(mydis.getStatus()==1 && mydis.getExStatus()!=1){
				flag = "weidian";
			}else if(mydis.getExStatus()==1){
				flag = "tiyandian";
			}else{
				flag = "huiyuan";
			}
		}else{
			flag = "huiyuan";
		}
		form.addResult("flag", flag);
    	
    	
    	ShopMember member = this.getShopMember(form);
    	if(member!=null){
    		QueryObject qo = new QueryObject();
    		qo.addQuery("obj.member", member, "=");
    		qo.setLimit(-1);
    		List<ShoppingCart> sList=this.service.getShoppingCartBy(qo).getResult();
    		form.addResult("sList", sList);
    	}else{
    		return this.error(form, "用户登陆超时，请退出后重新进入！");
    	}
        return new Page("/bcd/wxshop/member/mycart.html");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("carproid")));
        this.service.delShoppingCart(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 保存购物车
     * 
     * @param form
     */
    public Page doAddToCart(WebForm form) {
    	ShopMember member = this.getShopMember(form);
    	Integer pId=CommUtil.null2Int(form.get("shopProductId"));
    	Integer sId=CommUtil.null2Int(form.get("shopSpecId"));
    	Integer buyNum=CommUtil.null2Int(form.get("buyNum"));
    	
    	//---------------
    	ShopProduct shopProduct=this.shopProductService.getShopProduct((long)pId);
    	if(shopProduct==null){
    		this.addError("msg", "没有获取此商品");
    		return pageForExtForm(form);
    	}else{
    		String dep=shopProduct.getProductType().getDePath();
    		String[] ptId=dep.split("@");//商品所属第一层分类
    		ProductType pType=this.productTypeService.getProductTypeByCode(ptId[1]);
    		if(pType.getIsSpecialProType()){
    			this.addError("msg", "此类商品不能加入购物车，请直接购买");
    			return pageForExtForm(form);
    		}
    	}
    	
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.member", member, "=");
    	qo.addQuery("obj.shopProduct.id", (long)pId, "=");
    	if(sId>0){
    		qo.addQuery("obj.shopSpec.id", (long)sId, "=");
    	}
    	List<?> list=this.service.getShoppingCartBy(qo).getResult();
    	if(list!=null){
    		ShoppingCart shoppingCart=(ShoppingCart)list.get(0);
    		shoppingCart.setBuyNum(shoppingCart.getBuyNum()+buyNum);
    		this.service.updateShoppingCart(shoppingCart.getId(), shoppingCart);
    		return pageForExtForm(form);
    	}
    	ShoppingCart cart = new ShoppingCart();
    	cart.setMember(member);
    	cart.setBuyNum(buyNum);
    	
    	cart.setShopProduct(shopProduct);
//    	/ShopDistributor distributor=(ShopDistributor)ActionContext.getContext().getSession().getAttribute("DISTRIBUTOR");
    	member=getShopMemberService().getShopMember(member.getId());
    	ShopSpec shopSpec=this.shopSpecService.getShopSpec((long)sId);
    	List list2=null;
    	if(member.getPmember()!=null){
    		QueryObject qoo = new QueryObject();
        	qoo.setPageSize(1);
        	qo.addQuery("obj.member", member.getPmember(), "=");
        	list2=this.shopDistributorService.getShopDistributorBy(qoo).getResult();
    	}
    	if(list2!=null){
    		ShopDistributor distributor=(ShopDistributor)list2.get(0);
    		cart.setShopDistributor(distributor);
    	}
    	if(shopSpec!=null){
    		cart.setShopSpec(shopSpec);
    	}
    	
    	this.service.addShoppingCart(cart);
        return pageForExtForm(form);
    }
    
    /**
     * 修改购物车
     * 
     * @param form
     */
    public Page doEditCart(WebForm form) {
    	Integer id=CommUtil.null2Int(form.get("carproid"));
    	Integer buyNum=CommUtil.null2Int(form.get("proNum"));
    	ShoppingCart cart=this.service.getShoppingCart((long)id);
    	if(cart==null){
    		this.addError("msg", "购物车没有此商品");
    		return pageForExtForm(form);
    	}
    	cart.setBuyNum(buyNum);
    	this.service.updateShoppingCart(cart.getId(), cart);
        return pageForExtForm(form);
    }
    public void setService(IShoppingCartService service) {
        this.service = service;
    }

	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}

	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
    
}
