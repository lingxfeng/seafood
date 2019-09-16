package com.eastinno.otransos.seafood.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.utils.NewsUtil;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.core.service.IGoShopService;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShoppingCartService;
import com.eastinno.otransos.seafood.util.DateUtil;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.ShopHtmlUtil;
import com.eastinno.otransos.seafood.util.ShopUtil;
import com.eastinno.otransos.seafood.util.ToolUtils;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * 公共action
 * @author nsz
 */
@Action
public class ShopBaseAction extends AbstractPageCmdAction{
	@Inject
    private IShopSystemConfigService shopSystemConfigService;
	@Inject
	private ToolUtils tu;
	@Inject
    private NewsUtil newsUtil;
	@Inject
	private IGoShopService goShopService;
	@Inject
	private ShopHtmlUtil shopHtmlUtil;
	@Inject
	private ShopUtil shopUtil;
	@Inject
	protected IShoppingCartService shoppingCartService;
	@Override
	public Object doBefore(WebForm form, Module module) {
		form.addResult("sysconfig", this.shopSystemConfigService.getSystemConfig());
		form.addResult("tu", tu);//工具
		form.addResult("NU", newsUtil);
		form.addResult("su", this.shopUtil);
		form.addResult("html", shopHtmlUtil);
		form.addResult("dateUL", new DateUtil());
		form.addResult("topPTypes", this.goShopService.getTopPTypes());//商品分类
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(user!=null){
			form.addResult("user", user);
		}
		String valueStr = DiscoShopUtil.getCookie("proHistory");
		try {
			valueStr = URLDecoder.decode(valueStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(!"".equals(valueStr)){
			/**
			 * 浏览记录
			 */
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.id in ("+valueStr+")");
			qo.setPageSize(4);
			qo.setOrderBy("createDate");
			qo.setOrderType("desc");
			IPageList pList = this.getGoShopService().queryProduct(qo);
			form.addResult("yhistory", pList);//你浏览过的
			List<ShopProduct> list = pList.getResult();
			if(list!=null){
				/**
				 * 猜你喜欢
				 */
				qo = new QueryObject();
				String pType="0";
				for(ShopProduct sp:list){
					pType+=","+sp.getProductType().getId();
				}
				qo.addQuery("obj.productType.id in ("+pType+")");
				qo.setOrderBy("saleNum desc");
				IPageList ylikes = this.getGoShopService().queryProduct(qo);
				form.addResult("ylikes", ylikes);// 猜你喜欢的
			}
		}
		/**
		 * 购物车
		 */
		ShopMember member = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(member!=null){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id",member.getId(),"=");
			qo.setPageSize(-1);
			int row = this.shoppingCartService.getShoppingCartBy(qo).getRowCount();
			form.addResult("carNum", row);
		}else{
			valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if("".equals(valueStr)){
				form.addResult("carNum", 0);
			}else{
				form.addResult("carNum", valueStr.split(",").length);
			}
		}
		
		return super.doBefore(form, module);
	}
	
	/**
	 * 跳转到指定页面
	 * @param form
	 * @return
	 */
	public Page doToPage(WebForm form){
		String toPage = CommUtil.null2String(form.get("topage"));
		if(!StringUtils.hasText(toPage)){
			toPage = "/index.html";
		}
		return new Page(toPage);
	}
	
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public void setShopSystemConfigService(
			IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	public void setTu(ToolUtils tu) {
		this.tu = tu;
	}
	public ToolUtils getTu() {
		return tu;
	}
	public IGoShopService getGoShopService() {
		return goShopService;
	}
	public void setGoShopService(IGoShopService goShopService) {
		this.goShopService = goShopService;
	}
	public ShopHtmlUtil getShopHtmlUtil() {
		return shopHtmlUtil;
	}
	public void setShopHtmlUtil(ShopHtmlUtil shopHtmlUtil) {
		this.shopHtmlUtil = shopHtmlUtil;
	}
	public void setNewsUtil(NewsUtil newsUtil) {
		this.newsUtil = newsUtil;
	}
	
	public void setShopUtil(ShopUtil shopUtil) {
		this.shopUtil = shopUtil;
	}
	public NewsUtil getNewsUtil() {
		return newsUtil;
	}
	public ShopUtil getShopUtil() {
		return shopUtil;
	}

	public IShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	public void setShoppingCartService(IShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}
	
}
