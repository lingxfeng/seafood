package com.eastinno.otransos.seafood.core.action;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.application.util.QRCodeUtil;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.query.ShopProductQuery;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 前台显示主action
 * 
 * @author Administrator
 *
 */
@Action
public class GoShopAction extends ShopBaseAction {
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private IBrandService brandService;
	@Inject
	private IShopOrderdetailService shopOrderdetailService;
	@Inject
	private IShopMemberService shopMemberService;
	
	
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	
	public void setShopOrderdetailService(IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}


	public IBrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}

	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}

	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}

	/**
	 * 首页
	 */
	@Override
	public Page doInit(WebForm form, Module module) {
//		ShopMember shopMember=(ShopMember)ActionContext.getContext().getServletContext().getAttribute("DISCO_MEMBER");
//		if(shopMember==null){
//			String name = DiscoShopUtil.getCookie("SESSION_LOGIN_NAME");
//			if(StringUtils.hasText(name)){
//				String password = DiscoShopUtil.getCookie("SESSION_LOGIN_VALUE");
//				ShopMember member=this.shopMemberService.getShopMemberByName("name", name);
//				if(password.equals(member.getPassword())){
//					form.addResult("user", member);
//					UserContext.setMember(member);
//				}
//			}
//		}
		form.addResult("indexfloors", this.getGoShopService().getFloors());// 楼层
		form.addResult("index", true);// 表示首页
		getNewsRight(form);
		getNewsFoot(form);
		return new Page("/pcbcd/index.html");
	}
	
	public void getNewsRight(WebForm form){
		List<NewsDoc> newsDocggList = getNewsDoc("bcdsright_gg",7);//公告
		List<NewsDoc> newsDocgzList = getNewsDoc("bcdsright_gz",7);//规则
		List<NewsDoc> newsDocaqList = getNewsDoc("bcdsright_aq",7);//安全
		List<NewsDoc> newsDocznList = getNewsDoc("bcdsright_zn",7);//指南
		form.addResult("newsDocggList", newsDocggList);
		form.addResult("newsDocgzList", newsDocgzList);
		form.addResult("newsDocaqList", newsDocaqList);
		form.addResult("newsDocznList", newsDocznList);
	}
	
	public void getNewsFoot(WebForm form){
		List<NewsDir> newsDirList = getNewsDir("bcdsfoot1");
		List<NewsDoc> newsDocList = getNewsDoc("bcdsfoot2",-1);
		List<NewsDoc> newsDocList2 = getNewsDoc("bcdsfoot3",-1);
		form.addResult("newsDirList", newsDirList);
		form.addResult("newsDocList1", newsDocList);
		form.addResult("newsDocList2", newsDocList2);
	}
	
	
	public List<NewsDir> getNewsDir(String name){
		QueryObject qo= new QueryObject();
		qo.addQuery("obj.code like '%"+name+"%'");
		qo.setOrderBy("sequence");
		qo.setOrderType("asc");
		qo.setPageSize(5);
		List<NewsDir> newsDirlist = this.getNewsUtil().getDirService().getNewsDirBy(qo).getResult();
		return newsDirlist;
	}
	
	public List<NewsDoc> getNewsDoc(String name,Integer num){
		QueryObject qo= new QueryObject();
		qo.addQuery("obj.dir.code like '%"+name+"%'");
		qo.setOrderBy("sequence");
		qo.setOrderType("asc");
		if(num==-1){
			qo.setLimit(-1);
		}else if(num>0){
			qo.setPageSize(num);
		}
		List<NewsDoc> newsDoclist = this.getNewsUtil().getNewsDocService().getNewsDocBy(qo).getResult();
		return newsDoclist;
	}
	
	/**
	 * 查询商品列表
	 * 
	 * @param form
	 * @return
	 */
	public Page doQueryPro(WebForm form) {
		String type = CommUtil.null2String(form.get("type"));
		QueryObject qo = form.toPo(ShopProductQuery.class);
		qo.addQuery("obj.status", (short) 1, "=");
		if (!"goods".equals(type)) {
			String id = CommUtil.null2String(form.get("pid"));
			if (!"".equals(id)) {
				ProductType productType = getGoShopService().getProductType(Long.parseLong(id));
				form.addResult("pType", productType);
				form.addResult("hotPro", this.getGoShopService().getIsTop(productType));
			}
		} else {
			String keyword = CommUtil.null2String(form.get("keyword"));
			//DiscoShopUtil.setCookie("search_history", keyword);
			form.addResult("hotPro", this.getGoShopService().getIsTop(null));
		}
		IPageList pList = this.getGoShopService().queryProduct(qo);
		CommUtil.saveIPageList2WebForm(pList, form);
		form.addResult("pList", pList);

		String valueStr = DiscoShopUtil.getCookie("duibipros");
		if (!"".equals(valueStr)) {
			qo = new QueryObject();
			qo.addQuery("obj.id in (" + valueStr + ")");
			List<?> comparisons = this.getGoShopService().queryProduct(qo).getResult();
			form.addResult("comparisons", comparisons);
		}
		return new Page("/shop/products.html");
	}
	
	/**
	 * 查询商品列表--优惠券
	 * 
	 * @param form
	 * @return
	 */
	public Page doQueryPro2(WebForm form) {
		String brandIds = CommUtil.null2String(form.get("brandIds"));
		String gIds = CommUtil.null2String(form.get("gIds"));
		QueryObject qo = form.toPo(ShopProductQuery.class);
		if(StringUtils.hasText(brandIds)){
			//qo.addQuery("obj.brand.id", Long.valueOf(brandIds), "=");
			qo.addQuery("obj.brand.id in ("+brandIds.substring(0, (brandIds.length()-1))+")");
		}
		if(StringUtils.hasText(gIds)){
			//qo.addQuery("obj.id", Long.valueOf(gIds), "=");
			qo.addQuery("obj.id in ("+gIds.substring(0, (gIds.length()-1))+")");
		}
		IPageList pList = this.getGoShopService().queryProduct(qo);
		form.addResult("pList", pList);
		return new Page("/shop/products.html");
	}

	/**
	 * 去品牌列表页
	 * 
	 * @param form
	 * @return
	 */
	public Page doToBrand(WebForm form) {
		String idStr = CommUtil.null2String(form.get("id"));
		Long brandId = Long.parseLong(idStr);
		String type = CommUtil.null2String(form.get("type"));
		QueryObject qo = form.toPo(ShopProductQuery.class);
		QueryObject qo2 = new QueryObject();
		qo2.setPageSize(5);
		qo2.setOrderBy("saleNum desc");
		qo2.addQuery("obj.brand.id",brandId,"=");
		qo2.addQuery("obj.status",Short.valueOf("1"),"=");
		List<ShopProduct> hotPros=this.getGoShopService().queryProduct(qo2).getResult();
		ShopProduct prohot = null;
/*		if(hotPros!=null && hotPros.size()>0){
			prohot=hotPros.get(0);
		}*/
		form.addResult("hotPros", hotPros);
		if (!"".equals(idStr)) {
			Brand brand = this.brandService.getBrand(brandId);
			qo.addQuery("obj.brand.id", brand.getId(), "=");
			form.addResult("pType", brand);
		}
		if ("goods".equals(type)) {
			String keyword = CommUtil.null2String(form.get("keyword"));
			DiscoShopUtil.setCookie("search_history", keyword);
			form.addResult("hotPro", this.getGoShopService().getIsTop(null));
		}
		IPageList pList = this.getGoShopService().queryProduct(qo);
		CommUtil.saveIPageList2WebForm(pList, form);
		form.addResult("pList", pList);

		String valueStr = DiscoShopUtil.getCookie("duibipros");
		if (!"".equals(valueStr)) {
			qo = new QueryObject();
			qo.addQuery("obj.id in (" + valueStr + ")");
			List<?> comparisons = this.getGoShopService().queryProduct(qo).getResult();
			form.addResult("comparisons", comparisons);
		}
		return new Page("/shop/products.html");
	}

	/**
	 * 查看商品详情
	 * 
	 * @param form
	 * @return
	 */
	public Page doProductDetail(WebForm form) {
		ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String id = CommUtil.null2String(form.get("id"));
		ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
		this.getGoShopService().updatePro(pro);
		form.addResult("product", pro);
		form.addResult("salePros", this.getGoShopService().getProBySaleNum());
		form.addResult("hotPro", this.getGoShopService().getIsTop(pro.getProductType()));
		form.addResult("collectPros", this.getGoShopService().getProCollectNum());
		DiscoShopUtil.setCookie("proHistory", id);
		// 产品咨询
		this.getAdviceCount(form, Long.valueOf(id));
		form.addResult("goodsAdviceList", this.getGoShopService().getGoodsAdvice(Long.valueOf(id), 0));
		// 商品评价
		this.getEvaluationCount(form, Long.valueOf(id));
		form.addResult("goodsEvaluation", this.getGoShopService().getGoodsEvaluation(form, Long.valueOf(id), 0));
		// 商品评论
		form.addResult("goodsEvaluationList", this.getGoShopService().getGoodsEvaluation(form, Long.valueOf(id), 0));
		// 产品规格
		Map<String, List<String>> specGroupMap = this.shopSpecService.getSpecGroupByProduct(pro);
		String specGroupJson = this.shopSpecService.getSpecJsonByProduct(pro);
		
		//成交记录
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.pro.id", pro.getId(), "=");
		qo.addQuery("obj.orderInfo.status in (1,2)");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<?> orderDetailList = this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
		form.addResult("specGroupMap", specGroupMap);
		form.addResult("specRecordJson", specGroupJson);
		form.addResult("orderDetailList", orderDetailList);
		form.addResult("user", member);
		return new Page("/shop/productdetail.html");
	}

	/**
	 * 添加到购物车
	 * 
	 * @param form
	 * @return
	 */
	public Page doSearchshopCar(WebForm form) {
		ShopMember member = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String pType = "0", ids = "0";
		if (member != null) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id", member.getId(), "=");
			qo.setPageSize(-1);
			List<ShoppingCart> shopCars = this.getShoppingCartService().getShoppingCartBy(qo).getResult();
			if (shopCars != null && shopCars.size() > 0) {
				for (ShoppingCart obj : shopCars) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", obj.getId());
					ShopProduct pro = obj.getShopProduct();
					map.put("pro", pro);
					map.put("num", obj.getBuyNum());
					map.put("spec", obj.getShopSpec());
					list.add(map);
				}
			}
		} else {
			String valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (!"".equals(valueStr)) {
				String[] id_num_specs = valueStr.split(",");
				for (String id_num_spec : id_num_specs) {
					Map<String, Object> map = new HashMap<String, Object>();
					String eleId, eleNumStr, eleSpecStr;
					String[] eles = id_num_spec.split("_");
					int length = eles.length;
					if (length == 1) {
						eleId = eles[0];
						eleNumStr = "1";
						eleSpecStr = "0";
					} else if (length == 2) {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = "0";
					} else {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = eles[2];
					}
					ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(eleId));
					ShopSpec spec = null;
					if (!eleSpecStr.equals("0")) {
						spec = this.shopSpecService.getShopSpec(Long.parseLong(eleSpecStr));
					}
					map.put("id", 0);
					map.put("pro", pro);
					map.put("num", Integer.parseInt(eleNumStr));
					map.put("spec", spec);
					list.add(map);
				}
			}
		}
		form.addResult("carList", list);
		return new Page("/common/ShopCar.html");
	}

	/**
	 * 查询分类
	 * 
	 * @param form
	 * @return
	 */
	public Page doSearchProBykey(WebForm form) {
		String keyword = CommUtil.null2String(form.get("keyword"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (!"".equals(keyword)) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.name", "%" + keyword + "%", "like");
			List<ProductType> pTypes = this.getGoShopService().getProductType(qo);
			if (pTypes != null) {
				List<Map<String, Object>> parent_gc = new ArrayList<Map<String, Object>>();
				List<List<Map<String, Object>>> list_child = new ArrayList<List<Map<String, Object>>>();
				for (ProductType p : pTypes) {
					Map<String, Object> pTypeMap = new HashMap<String, Object>();
					pTypeMap.put("id", p.getId());
					pTypeMap.put("name", p.getName());
					parent_gc.add(pTypeMap);
					qo = new QueryObject();
					if (p.getLevel() == 3) {
						qo.addQuery("obj.productType", p, "=");
					} else if (p.getLevel() == 2) {
						qo.addQuery("obj.productType.parent", p, "=");
					} else {
						qo.addQuery("obj.productType.parent.parent", p, "=");
					}
					List<ShopProduct> pros = this.getGoShopService().queryProduct(qo).getResult();
					if (pros != null) {
						List<Map<String, Object>> prosList = new ArrayList<Map<String, Object>>();
						for (ShopProduct pro : pros) {
							Map<String, Object> proMap = new HashMap<String, Object>();
							proMap.put("id", pro.getId());
							proMap.put("name", pro.getName());
							prosList.add(proMap);
						}
						list_child.add(prosList);
					}
				}
				map.put("parent_gc", parent_gc);
				map.put("list_child", list_child);
			}
		}
		form.jsonResult(map);
		return Page.JSONPage;
	}

	/**
	 * 进入购物车
	 * 
	 * @param form
	 * @return
	 */
	public Page doToShopCar(WebForm form) {
		ShopMember member = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String pType = "0", ids = "0";
		if (member != null) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id", member.getId(), "=");
			qo.setPageSize(-1);
			List<ShoppingCart> shopCars = this.getShoppingCartService().getShoppingCartBy(qo).getResult();
			if (shopCars != null && shopCars.size() > 0) {
				for (ShoppingCart obj : shopCars) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", obj.getId());
					ShopProduct pro = obj.getShopProduct();
					map.put("pro", pro);
					map.put("num", obj.getBuyNum());
					map.put("spec", obj.getShopSpec());
					list.add(map);
					pType += "," + pro.getProductType().getId();
					ids += "," + pro.getId();
				}
			}
		} else {
			String valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (!"".equals(valueStr)) {
				String[] id_num_specs = valueStr.split(",");
				for (String id_num_spec : id_num_specs) {
					Map<String, Object> map = new HashMap<String, Object>();
					String eleId, eleNumStr, eleSpecStr;
					String[] eles = id_num_spec.split("_");
					int length = eles.length;
					if (length == 1) {
						eleId = eles[0];
						eleNumStr = "1";
						eleSpecStr = "0";
					} else if (length == 2) {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = "0";
					} else {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = eles[2];
					}
					ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(eleId));
					ShopSpec spec = null;
					if (!eleSpecStr.equals("0")) {
						spec = this.shopSpecService.getShopSpec(Long.parseLong(eleSpecStr));
					}
					map.put("id", 0);
					map.put("pro", pro);
					map.put("num", eleNumStr);
					map.put("spec", spec);
					list.add(map);
					pType += "," + pro.getProductType().getId();
					ids += "," + pro.getId();
				}
			}
		}
		form.addResult("carPros", list);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.productType.id in (" + pType + ")");
		qo.addQuery("obj.id not in (" + ids + ")");
		qo.setOrderBy("saleNum desc");
		IPageList ynides = this.getGoShopService().queryProduct(qo);
		form.addResult("ynides", ynides);// 猜你需要
		return new Page("/trade/shopcar.html");
	}

	/**
	 * 首页个人中心
	 * 
	 * @param form
	 * @return
	 */
	public Page doUserCenterIndex(WebForm form) {
		User user = DiscoShopUtil.getUser();
		if (user != null) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.status", Integer.parseInt("0"), "=");
			qo.addQuery("obj.user", user, "=");
			qo.setLimit(-1);
			List<?> list0 = this.getGoShopService().getOrderInfo(qo);
			form.addResult("list0Num", list0 == null ? 0 : list0.size());// 待支付

			qo = new QueryObject();
			qo.addQuery("obj.user", user, "=");
			qo.addQuery("obj.status", Integer.parseInt("2"), "=");
			qo.setLimit(-1);
			List<?> list2 = this.getGoShopService().getOrderInfo(qo);
			form.addResult("list2Num", list2 == null ? 0 : list2.size());// 待收货

			qo = new QueryObject();
			qo.addQuery("obj.user", user, "=");
			qo.addQuery("obj.shopDiscuss is null");
			qo.addQuery("obj.orderInfo.status in (2,3)");
			qo.setLimit(-1);
			int pjCount = this.shopOrderdetailService.getShopOrderdetailBy(qo).getRowCount();
			form.addResult("pjCount", pjCount);// 待评价

			qo = new QueryObject();
			qo.addQuery("obj.user", user, "=");
			qo.addQuery("obj.status", Integer.parseInt("-2"), "=");
			qo.setLimit(-1);
			List<?> list2b = this.getGoShopService().getOrderInfo(qo);
			form.addResult("list2bNum", list2b == null ? 0 : list2b.size());// 待收货
			ShopMember member = this.getGoShopService().getShopMember();
			form.addResult("myCollectionsNum", member.getMyCollections().size());
		}
		return new Page("/userCenter/userCenterIndex.html");
	}

	/**
	 * 加入对比
	 * 
	 * @param form
	 * @return
	 */
	public Page doComparisonAdd(WebForm form) {
		QueryObject qo = new QueryObject();
		String id = CommUtil.null2String(form.get("id"));
		String valueStr = DiscoShopUtil.setCookie("duibipros", id);
		if (!"".equals(id)) {
			qo.addQuery("obj.id in (" + valueStr + ")");
			List<ShopProduct> list = this.getGoShopService().queryProduct(qo).getResult();
			form.addResult("comparisons", list);
		}
		return new Page("/shop/duibiAdd.html");
	}

	/**
	 * 删除对比
	 * 
	 * @param form
	 * @return
	 */
	public Page doComparisonRemove(WebForm form) {
		String id = CommUtil.null2String(form.get("id"));
		if (!"".equals(id)) {
			String valueStr = DiscoShopUtil.removeCookie("duibipros", id);
			if (!"".equals(valueStr)) {
				QueryObject qo = new QueryObject();
				qo.addQuery("obj.id in (" + valueStr + ")");
				List<ShopProduct> list = this.getGoShopService().queryProduct(qo).getResult();
				form.addResult("comparisons", list);
			}
		}
		return new Page("/shop/duibiAdd.html");
	}

	/**
	 * 对比
	 * 
	 * @param form
	 * @return
	 */
	public Page doComparison(WebForm form) {
		String removeId = CommUtil.null2String(form.get("removeId"));
		String valueStr;
		if (!"".equals(removeId)) {
			valueStr = DiscoShopUtil.removeCookie("duibipros", removeId);
		} else {
			valueStr = DiscoShopUtil.getCookie("duibipros");
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.id in (" + valueStr + ")");
		List<ShopProduct> list = this.getGoShopService().queryProduct(qo).getResult();
		form.addResult("comparisons", list);
		return new Page("/shop/proCompare.html");
	}

	/**
	 * 收藏
	 * 
	 * @param form
	 * @return
	 */
	public Page doCollectPro(WebForm form) {
		Map<String,String> map = new HashMap<String,String>();
		String id = CommUtil.null2String(form.get("id"));
		ShopMember member = this.getGoShopService().getShopMember();
		ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
		boolean isCollect = false;
		if (member != null) {
			List<ShopProduct> list = member.getMyCollections();
			for (ShopProduct sp : list) {
				if (sp.getId().equals(Long.parseLong(id))) {
					isCollect = true;
					break;
				}
			}
			if (!isCollect) {
				ShopProduct sp = this.getGoShopService().getProduct(Long.parseLong(id));
				list.add(sp);
				this.getGoShopService().updateMember(member);
				pro.setCollectNum(pro.getCollectNum() + 1);
				map.put("status", "0");
				map.put("collectNum", pro.getCollectNum().toString());
				form.jsonResult(map);
				form.jsonResult(map);
				
			} else {
				pro.setCollectNum(pro.getCollectNum() - 1);
				list.remove(pro);
				member.setMyCollections(list);
				this.shopMemberService.updateShopMember(member.getId(), member);
				map.put("status", "1");
				map.put("collectNum", pro.getCollectNum().toString());
				form.jsonResult(map);
			}
			this.getGoShopService().updatePro(pro);
		}
		return Page.JSONPage;
	}

	/**
	 * 同步添加到购物车
	 * 
	 * @param form
	 * @return
	 */
	public Page doAddToCar(WebForm form) {
		ShopMember member = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String id = CommUtil.null2String(form.get("id"));
		String numStr = CommUtil.null2String(form.get("num"));
		String specStr = CommUtil.null2String(form.get("spec"));
		Map<String,String> map = new HashMap<String,String>();
		map.put("success", "0");
		if (member != null) {
			int num = 1;
			Long spec = 0l;
			if (!numStr.equals("")) {
				num = Integer.parseInt(numStr);
			}
			if (!specStr.equals("")) {
				spec = Long.parseLong(specStr);
			}
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id", member.getId(), "=");
			qo.addQuery("obj.shopProduct.id", Long.parseLong(id), "=");
			if (spec != 0l) {
				qo.addQuery("obj.shopSpec.id", spec, "=");
			} else {
				qo.addQuery("obj.shopSpec is EMPTY");
			}
			qo.setPageSize(1);
			List<?> list = this.getShoppingCartService().getShoppingCartBy(qo).getResult();
			ShoppingCart shopCar = null;
			if (list != null && list.size() > 0) {
				shopCar = (ShoppingCart) list.get(0);
				shopCar.setBuyNum(shopCar.getBuyNum() + num);
			} else {
				shopCar = new ShoppingCart();
				shopCar.setBuyNum(num);
				shopCar.setMember(member);
				ShopProduct product = this.getGoShopService().getProduct(Long.parseLong(id));
				shopCar.setShopProduct(product);
				if (spec != 0l) {
					ShopSpec shopSpec = this.shopSpecService.getShopSpec(spec);
					shopCar.setShopSpec(shopSpec);
				}
				this.getShoppingCartService().addShoppingCart(shopCar);
			}
			qo = new QueryObject();
			qo.addQuery("obj.member.id",member.getId(),"=");
			qo.setPageSize(-1);
			int row = this.getShoppingCartService().getShoppingCartBy(qo).getRowCount();
			map.put("numj", row+"");
			map.put("success", "1");
		} else {
			int row =0;
			String valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String id_num_specs_new = "";
			if (!"".equals(valueStr)) {
				String[] id_num_specs = valueStr.split(",");
				boolean hasCur = false;
				for (String id_num_spec : id_num_specs) {
					row++;
					String eleId, eleNumStr, eleSpecStr;
					String[] eles = id_num_spec.split("_");
					int length = eles.length;
					if (length == 1) {
						eleId = eles[0];
						eleNumStr = "1";
						eleSpecStr = "0";
					} else if (length == 2) {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = "0";
					} else {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = eles[2];
					}
					if (eleId.equals(id) && eleSpecStr.equals(specStr)) {
						hasCur = true;
						eleNumStr = (Integer.parseInt(eleNumStr) + Integer.parseInt(numStr)) + "";
					}
					id_num_specs_new += eleId + "_" + eleNumStr + "_" + eleSpecStr + ",";
				}
				if (!hasCur) {
					row++;
					id_num_specs_new += id + "_" + numStr + "_" + specStr;
				} else if (!id_num_specs_new.equals("")) {
					id_num_specs_new = id_num_specs_new.substring(0, id_num_specs_new.length() - 1);
				}
			} else {
				row++;
				id_num_specs_new = id + "_" + numStr + "_" + specStr;
			}
			map.put("success", "1");
			map.put("num", row+"");
			DiscoShopUtil.setCookieByVal("discoshopCar", id_num_specs_new);
		}
		form.jsonResult(map);
		// return new Page("/shop/proaddtocar.html");
		return Page.JSONPage;
	}

	/**
	 * 从购物车中删除
	 * 
	 * @param form
	 * @return
	 */
	public Page doDeleteShopCar(WebForm form) {
		ShopMember member = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String id = CommUtil.null2String(form.get("id"));
		String numStr = CommUtil.null2String(form.get("num"));
		String specStr = CommUtil.null2String(form.get("spec"));
		if (member != null) {
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member.id", member.getId(), "=");
			qo.addQuery("obj.shopProduct.id", Long.parseLong(id), "=");
			if (specStr.equals("") || specStr.equals("0")) {
				qo.addQuery("obj.shopSpec is EMPTY");
			}else{
				qo.addQuery("obj.shopSpec.id", Long.parseLong(specStr), "=");
			}
			qo.setPageSize(1);
			List<ShoppingCart> list = this.getShoppingCartService().getShoppingCartBy(qo).getResult();
			if (list != null && list.size() > 0) {
				ShoppingCart car = list.get(0);
				this.getShoppingCartService().delShoppingCart(car.getId());
			}
		} else {
			String valueStr = DiscoShopUtil.getCookie("discoshopCar");
			try {
				valueStr = URLDecoder.decode(valueStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String id_num_specs_new = "";
			if (!"".equals(valueStr)) {
				String[] id_num_specs = valueStr.split(",");
				boolean hasCur = false;
				for (String id_num_spec : id_num_specs) {
					String eleId, eleNumStr, eleSpecStr;
					String[] eles = id_num_spec.split("_");
					int length = eles.length;
					if (length == 1) {
						eleId = eles[0];
						eleNumStr = "1";
						eleSpecStr = "0";
					} else if (length == 2) {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = "0";
					} else {
						eleId = eles[0];
						eleNumStr = eles[1];
						eleSpecStr = eles[2];
					}
					if (!eleId.equals(id) || !eleSpecStr.equals(specStr)) {
						id_num_specs_new += eleId + "_" + eleNumStr + "_" + eleSpecStr + ",";
					}
				}
			}
			DiscoShopUtil.setCookieByVal("discoshopCar", id_num_specs_new);
		}
		return Page.nullPage;
	}

	/**
	 * 所有分类
	 * 
	 * @param form
	 * @return
	 */
	public Page doAllPType(WebForm form) {
		return new Page("/shop/allType.html");
	}

	/**
	 * 我要咨询
	 * 
	 * @param form
	 * @return
	 */
	public Page doGetAdvice(WebForm form) {
		String proId = CommUtil.null2String(form.get("proId"));
		Integer otherType = CommUtil.null2Int(form.get("otherType"));
		form.addResult("goodsAdviceList", this.getGoShopService().getGoodsAdvice(Long.valueOf(proId), otherType));
		this.getAdviceCount(form, Long.valueOf(proId));
		return new Page("/shop/goods_consult.html");
	}

	/**
	 * 获取我要咨询类型的条数
	 * 
	 * @param form
	 * @return
	 */
	public void getAdviceCount(WebForm form, Long id) {
		int qbzxCount = this.getGoShopService().getAdviceCount(id, 0);
		int cpzxCount = this.getGoShopService().getAdviceCount(id, 1);
		int kcjpsCount = this.getGoShopService().getAdviceCount(id, 2);
		int zfjfpCount = this.getGoShopService().getAdviceCount(id, 3);
		int shzxCount = this.getGoShopService().getAdviceCount(id, 4);
		int cxhdCount = this.getGoShopService().getAdviceCount(id, 5);
		form.addResult("qbzxCount", qbzxCount);
		form.addResult("cpzxCount", cpzxCount);
		form.addResult("kcjpsCount", kcjpsCount);
		form.addResult("zfjfpCount", zfjfpCount);
		form.addResult("shzxCount", shzxCount);
		form.addResult("cxhdCount", cxhdCount);
	}

	/**
	 * 获取商品的好评数
	 * 
	 * @param form
	 * @return
	 */
	public void getEvaluationCount(WebForm form, Long id) {
		int count = this.getGoShopService().getEvaluationCount(id, 0);
		int cpcount = this.getGoShopService().getEvaluationCount(id, 1);
		int zpcount = this.getGoShopService().getEvaluationCount(id, 2);
		int hpcount = this.getGoShopService().getEvaluationCount(id, 3);
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后1位
		numberFormat.setMaximumFractionDigits(1);
		// 评价总数量
		form.addResult("count", count);
		// 差评数量
		form.addResult("cpcount", cpcount);
		form.addResult("cpbfb", count > 0 ? numberFormat.format((float) cpcount / (float) count * 100) : 0);
		// 中评数量
		form.addResult("zpcount", zpcount);
		form.addResult("zpbfb", count > 0 ? numberFormat.format((float) zpcount / (float) count * 100) : 0);
		// 好评数量
		form.addResult("hpcount", hpcount);
		form.addResult("hpbfb", count > 0 ? numberFormat.format((float) hpcount / (float) count * 100) : 0);
	}

	/**
	 * 获取商品评价
	 * 
	 * @param form
	 * @return
	 */
	public Page doGetGoodsEvaluation(WebForm form) {
		String proId = CommUtil.null2String(form.get("proId"));
		int type = CommUtil.null2Int(form.get("type"));
		List<ShopDiscuss> list = this.getGoShopService().getGoodsEvaluation(form, Long.valueOf(proId), type);
		form.addResult("eType", type);
		form.addResult("goodsEvaluationList", list);
		return new Page("/shop/goods_evaluation.html");
	}

	public Page doRcMpWxPayImg(WebForm form) throws Exception {
		String core_url = CommUtil.null2String(form.get("code_url"));
		BufferedImage image = QRCodeUtil.createImage(core_url, null, false);
		HttpServletResponse res = ActionContext.getContext().getResponse();
		res.setContentType("image/jpeg");
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0L);
		ImageIO.write(image, "JPEG", res.getOutputStream());
		res.getOutputStream().flush();
		res.getOutputStream().close();
		return Page.nullPage;
	}
	
	public Page doToService(WebForm form) throws Exception {
		String name = CommUtil.null2String(form.get("name"));
		List<NewsDir> newsDirList = getNewsDir(name);
		form.addResult("newsDirList", newsDirList);
		Integer docId=CommUtil.null2Int(form.get("docId"));
		NewsDoc newsDoc=this.getNewsUtil().getNewsDocService().getNewsDoc((long)docId);
		form.addResult("newsDoc", newsDoc);
		return new Page("/pcbcd/service.html");
	}
	
	public Page doMain(WebForm form){
		form.addResult("indexfloors", this.getGoShopService().getFloors());// 楼层
		return new Page("/pcbcd/mod_shop.html");
	}
	public Page doNewInfo(WebForm form){
		return Page.nullPage;
	}
}
