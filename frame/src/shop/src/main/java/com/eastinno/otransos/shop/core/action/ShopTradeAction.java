package com.eastinno.otransos.shop.core.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.StringUtil;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPayCallOrderService;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.shop.spokesman.service.ISubsidyService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.shop.trade.service.IShopPayMentService;
import com.eastinno.otransos.shop.usercenter.domain.ShopAddress;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.shop.usercenter.service.IShopAddressService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

/**
 * 交易控制action
 * @author nsz
 *
 */
@Action
public class ShopTradeAction extends ShopBaseAction{
	@Inject
	private ISystemRegionService systemRegionService;
	@Inject
    private IShopAddressService shopAddressService;
	@Inject
    private IShopOrderInfoService shopOrderInfoService;
	@Inject
    private IShopOrderdetailService shopOrderdetailService;
	@Inject
    private IPaymentConfigService paymentConfigService;
	@Inject
	private IShopPayMentService shopPayMentService;
	@Inject
	private IShopProductService shopProductService;
	@Inject
	private IShopDistributorService shopDistributorService;
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private IDeliveryRuleService deliveryRuleService;
	@Inject
	private ICustomCouponService customCouponService;
	@Inject
	private IShopMemberService shopMemberService;
	@Inject
	private IRushBuyRegularService regularService;
	@Inject
	private IRushBuyRecordService recordService;
	@Inject
	private ISubsidyService subsidyService;
	
	@Override
	public Object doBefore(WebForm form, Module module) {
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(user==null){
			return go("shopMemberCenter.toLogin");
		}
		return super.doBefore(form, module);
	}
	/**
	 * 确认订单
	 * @param form
	 * @return
	 */
	public Page doConfirmOrder(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String proIdNumsStr = CommUtil.null2String(form.get("proIdNums"));
		String ccId = CommUtil.null2String(form.get("ccId"));
		List<Map<String,String>> pros = new ArrayList<Map<String,String>>();
		if(!"".equals(proIdNumsStr)){
			String[] proIdNums = proIdNumsStr.split(",");
			Double allAmt = 0.0;
			for(String proIdNumStr:proIdNums){
				String[] idNums=proIdNumStr.split("_");
				int num = 1;
				//商品数量
				if(idNums.length>1){
					num = Integer.parseInt(idNums[1]);
				}
				//商品购买规格ID
				Long shopspecId = null;
				if(idNums.length>2){
					shopspecId = Long.parseLong(idNums[2]);
				}
				
				String id = idNums[0];
				ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
				Map<String,String> promap = new HashMap<String,String>();
				if(shopspecId == null){
					if(member.getDisType()==0){
						allAmt+=pro.getAmt()*num;
					}else if(member.getDisType()==1){
						allAmt+=pro.getStore_price()*num;
						promap.put("store_price", pro.getStore_price()+"");//微店价格
						promap.put("allStore_price", pro.getStore_price()*num+"");//微店商品总价格（单个商品）
					}else if(member.getDisType()==2){
						allAmt+=pro.getTydAmt()*num;
						promap.put("tydAmt", pro.getTydAmt()+"");//体验店价格
						promap.put("allTydAmt", pro.getTydAmt()*num+"");//体验店总价格（单个商品）
					}
					promap.put("amt", pro.getAmt()+"");
				}else{
					ShopSpec spec = this.shopSpecService.getShopSpec(shopspecId);
					if(member.getDisType()==0){
						allAmt+=spec.getAmt()*num;
					}else if(member.getDisType()==1){
						allAmt+=spec.getStore_price()*num;
						promap.put("store_price", spec.getStore_price()+"");//微店价格
						promap.put("allStore_price", spec.getStore_price()*num+"");//微店商品总价格（单个商品）
					}else if(member.getDisType()==2){
						allAmt+=spec.getTydAmt()*num;
						promap.put("tydAmt", spec.getTydAmt()+"");//体验店价格
						promap.put("allTydAmt", spec.getTydAmt()*num+"");//体验店总价格（单个商品）
					}
					promap.put("amt", spec.getAmt()+"");
					promap.put("shopspecName", spec.getName());
					promap.put("shopspecId", shopspecId+"");
				}
				promap.put("name", pro.getName());				
				promap.put("num", num+"");
				promap.put("amtAll", allAmt+"");
				promap.put("imgPath", pro.getImgPaths().split("_")[0]);
				promap.put("id", pro.getId()+"");
				pros.add(promap);
			}
			form.addResult("allAmt", allAmt); //总价格
		}
		form.addResult("proList", pros);
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user",user,"=");
		List<?> addresses  = this.shopAddressService.getShopAddressBy(qo).getResult();
		/*if(StringUtils.hasText(ccId)){
			this.getShoppingCartService().clearShoppingCart(ccId);
		}*/
		form.addResult("addLists", addresses);
		List<?> dateList=findDates();
		form.addResult("dateList", dateList);
		String tjfs=CommUtil.null2String(form.get("tjfs"));
		form.addResult("tjfs", tjfs);
		form.addResult("ccId", ccId);
		return new Page("/trade/confirmOrder.html");
	}
	
	/**
	 * 获取订单价格 product_price
	 */
	public Double getOrderProductPrice(String proIdNumsStr,ShopMember member){
		Double allAmt = 0.0;
		if(!"".equals(proIdNumsStr)){
			String[] proIdNums = proIdNumsStr.split(",");
			for(String proIdNumStr:proIdNums){
				String[] idNums=proIdNumStr.split("_");
				int num = 1;
				//商品数量
				if(idNums.length>1){
					num = Integer.parseInt(idNums[1]);
				}
				//商品购买规格ID
				Long shopspecId = null;
				if(idNums.length>2){
					shopspecId = Long.parseLong(idNums[2]);
				}
				String id = idNums[0];
				ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
				if(shopspecId == null){
					if(member.getDisType()==0){
						allAmt+=pro.getAmt()*num;
					}else if(member.getDisType()==1){
						allAmt+=pro.getStore_price()*num;
					}else if(member.getDisType()==2){
						allAmt+=pro.getTydAmt()*num;
					}
				}else{
					ShopSpec spec = this.shopSpecService.getShopSpec(shopspecId);
					if(member.getDisType()==0){
						allAmt+=spec.getAmt()*num;
					}else if(member.getDisType()==1){
						allAmt+=spec.getStore_price()*num;
					}else if(member.getDisType()==2){
						allAmt+=spec.getTydAmt()*num;
					}
				}
			}
		}
		return allAmt;
	}
	
	
	
	/**
	 * 进入添加收货地址页面
	 * @param form
	 * @return
	 */
	public Page doAddAdress(WebForm form){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent is EMPTY");
		List<?> list = systemRegionService.getRootSystemRegions().getResult();
		form.addResult("rootRegions", list);
		return new Page("/trade/addAdress.html");
	}
	/**
	 * 添加收货地址
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public Page doSaveAddress(WebForm form) throws UnsupportedEncodingException{
		String trueName = new String(CommUtil.null2String(form.get("trueName")).getBytes("ISO-8859-1"),"UTF-8");
		String area_info = new String(CommUtil.null2String(form.get("area_info")).getBytes("ISO-8859-1"),"UTF-8");
		ShopAddress obj = form.toPo(ShopAddress.class);
		obj.setTrueName(trueName);
		obj.setArea_info(area_info);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		obj.setUser(user);
		if (!hasErrors()) {
            Long id = this.shopAddressService.addShopAddress(obj);
        }
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user",user,"=");
		List<?> list = this.shopAddressService.getShopAddressBy(qo).getResult();
		form.addResult("list", list);
		return new Page("/trade/addresses.html");
	}
	/**
	 * 编辑收货地址
	 * @param form
	 * @return
	 */
	public Page doToEditAddress(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		ShopAddress obj = this.shopAddressService.getShopAddress(Long.parseLong(id));
		SystemRegion region = obj.getArea();
		Long proId=null;
		if(region.getLev()==2){
			proId=region.getParent().getId();
		}else if(region.getLev()==3){
			proId=region.getParent().getParent().getId();
		}
		form.addResult("entry", obj);
		form.addResult("proId", proId);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent is EMPTY");
		List<?> list = systemRegionService.getRootSystemRegions().getResult();
		form.addResult("rootRegions", list);
		return new Page("/trade/editAddress.html");
	}
	/**
	 * 更新收货地址
	 * @param form
	 * @return
	 */
	public Page doUpdateAddress(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		String code=CommUtil.null2String(form.get("area_id"));
		try {
			ShopAddress entry = this.shopAddressService.getShopAddress(Long.parseLong(id));
			form.toPo(entry);
			String trueName = new String(CommUtil.null2String(form.get("trueName")).getBytes("ISO-8859-1"),"UTF-8");
			String area_info = new String(CommUtil.null2String(form.get("area_info")).getBytes("ISO-8859-1"),"UTF-8");
			entry.setTrueName(trueName);
			entry.setArea_info(area_info);
	        SystemRegion systemRegion=this.systemRegionService.getSystemRegionBySn(code);
	        entry.setArea(systemRegion);
	        if (!hasErrors()) {
	            boolean ret = this.shopAddressService.updateShopAddress(Long.parseLong(id), entry);
	        }
	        form.addResult("entry", entry);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Page("/trade/oneAddress.html");
	}
	/**
	 * 设为默认地址
	 * @param form
	 * @return
	 */
	public Page doSetDeafaultAdd(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		ShopAddress entry = this.shopAddressService.getShopAddress(Long.parseLong(id));
		QueryObject qo = new QueryObject();
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		qo.addQuery("obj.user",user,"=");
		qo.addQuery("obj.isDefault",Boolean.TRUE,"=");
		List<ShopAddress> list = this.shopAddressService.getShopAddressBy(qo).getResult();
		if(list!=null){
			for(ShopAddress add:list){
				add.setIsDefault(false);
				this.shopAddressService.updateShopAddress(add.getId(), add);
			}
		}
		entry.setIsDefault(true);
		this.shopAddressService.updateShopAddress(Long.parseLong(id), entry);
		form.jsonResult(true);
		return Page.JSONPage;
	}
	public Page doCreateOrder(WebForm form){
		//检查库存是否满足购买需求
		String tjfs=CommUtil.null2String(form.get("tjfs"));//提交方式
		String carId=CommUtil.null2String(form.get("carId"));
		if("cart".equals(tjfs)){
			this.checkInventoryEnough(form, "cart");
		}else{
			this.checkInventoryEnough(form, "direct");
		}
		if(StringUtils.hasText(carId)){
			this.getShoppingCartService().clearShoppingCart(carId);
		}
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, null, null, errorStr);
		}
		
		/**
		 * 生成订单
		 */
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		user=this.shopMemberService.getShopMember(user.getId());
		if(user==null){
			return new Page("/userCenter/login.html");
		}
		order.setUser(user);
		order.setCode(new Date().getTime()+"");
		order.setOrderType(Short.valueOf("1"));
		if(order.getUser().getDisType()==0){
			order.setDistributor(order.getUser().getDistributor());
			if(order.getUser().getDistributor()!=null){
				order.setTopDistributor(order.getUser().getDistributor().getTopDistributor());
			}
		}else if(order.getUser().getDisType()==1){
			order.setDistributor(order.getUser().getMyDistributor());
			order.setTopDistributor(order.getUser().getMyDistributor().getTopDistributor());
		}else if(order.getUser().getDisType()==2){
			order.setDistributor(order.getUser().getMyDistributor().getTopDistributor());
			order.setDistributor(order.getUser().getMyDistributor().getTopDistributor());
		}
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String proIdNumsStr = CommUtil.null2String(form.get("proIdNums"));
		/**------------------cl-----------------------**/
		Double product_price = this.getOrderProductPrice(proIdNumsStr, user);
		order.setProduct_price(product_price);
		/**-----------------------------------------**/
		
		List<Map<String,String>> pros = new ArrayList<Map<String,String>>();
		if(!"".equals(proIdNumsStr)){
			String[] proIdNums = proIdNumsStr.split(",");
			Double allAmt = 0.0;
			for(String proIdNumStr:proIdNums){
				String[] idNums=proIdNumStr.split("_");
				//购买数量
				int num = 1;
				if(idNums.length>1){
					num = Integer.parseInt(idNums[1]);
				}
				//购买规格
				Long shopspecId = null;
				ShopSpec spec = null;
				if(idNums.length > 2){
					shopspecId = Long.parseLong(idNums[2]);
					spec = this.shopSpecService.getShopSpec(shopspecId);
				}				
				
				String id = idNums[0];
				ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
				ShopOrderdetail orderDetail = new ShopOrderdetail();
				orderDetail.setPro(pro);
				orderDetail.setNum(num);
				orderDetail.setUser(user);
				orderDetail.setCode(new Date().getTime()+"");
				orderDetail.setUnit_price(pro.getAmt());
				orderDetail.setGross_price(pro.getAmt()*num);
				orderDetail.setOrderInfo(order);
				orderDetail.setShopSpec(spec);
				this.shopOrderdetailService.addShopOrderdetail(orderDetail);
				pro.setSaleNum(pro.getSaleNum()+num);
				pro.setInventory(pro.getInventory()-num);
				this.shopProductService.updateShopProduct(pro.getId(), pro);
				
				//促销活动回调函数
				try {
					this.createOrderCallBack(order, pro, "direct");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return DiscoShopUtil.goPage("/shopTrade.java?cmd=toPay&orderId="+order.getId());		
	}
	
	/**
	 * 直接购买跳转页面
	 */
	public Page doBeforeCreateOrder(WebForm form){
		String pid = CommUtil.null2String(form.get("pid"));//商品ID
		String specid = CommUtil.null2String(form.get("specid"));//规格ID
		String disid = CommUtil.null2String(form.get("disid"));//分销商ID
		String numstr = CommUtil.null2String(form.get("num"));//商品数量
		
		
		ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
		ShopDistributor dis = this.shopDistributorService.getShopDistributor(Long.parseLong(disid));
		ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
		
		form.addResult("product",pro);
		form.addResult("spec",spec);
		form.addResult("numstr",numstr);
		return new Page("/wxshop/trading/payorder.html");
	}
	
	/**
	 * 购物车购买跳转页面
	 */
	public Page doBeforeCreateOrder2(WebForm form){
		String carid = CommUtil.null2String(form.get("carId"));
		if(!"".equals(carid)){
			String[] carIdNums = carid.split(",");
			List<ShoppingCart> list = new ArrayList<>();
			for(String id:carIdNums){
				ShoppingCart car = this.getShoppingCartService().getShoppingCart(Long.parseLong(id));
				list.add(car);
			}
			form.addResult("list",list);
		}
		return new Page("/wxshop/trading/payorder.html");
	}
	
	public Page doCreateOrder2(WebForm form){
		//检查库存是否满足购买需求
		this.checkInventoryEnough(form, "cart");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, null, null, errorStr);
		}
		
		/**
		 * 购物车生成订单2
		 */
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		order.setUser(user);
		order.setCode(new Date().getTime()+"");
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String carid = CommUtil.null2String(form.get("carId"));
		if(!"".equals(carid)){
			String[] carIdNums = carid.split(",");
			Double allAmt = 0.0;
			for(String id:carIdNums){
				ShoppingCart car = this.getShoppingCartService().getShoppingCart(Long.parseLong(id));
				ShopProduct pro = car.getShopProduct();
				ShopDistributor dis = car.getShopDistributor();
				int num=1;
				num=car.getBuyNum();
				ShopSpec spec = car.getShopSpec();
				
				ShopOrderdetail orderDetail = new ShopOrderdetail();
				orderDetail.setPro(pro);
				orderDetail.setNum(num);
				orderDetail.setUser(user);
				orderDetail.setSeller(dis);
				orderDetail.setShopSpec(spec);
				orderDetail.setCode(new Date().getTime()+"");
				orderDetail.setUnit_price(pro.getAmt());//单价
				orderDetail.setGross_price(pro.getAmt()*num);//总价
				orderDetail.setOrderInfo(order);
				this.shopOrderdetailService.addShopOrderdetail(orderDetail);
				pro.setSaleNum(pro.getSaleNum()+num);
				pro.setInventory(pro.getInventory()-num);
				this.shopProductService.updateShopProduct(pro.getId(), pro);
			}
		}
		return DiscoShopUtil.goPage("/shopTrade.java?cmd=toPay&orderId="+order.getId());
	}
	public Page doCreateOrder3(WebForm form){
		//检查库存是否满足购买需求
		this.checkInventoryEnough(form, "direct");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, null, null, errorStr);
		}
		
		/**
		 * 直接购买生成订单2
		 */
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		order.setUser(user);
		order.setCode(new Date().getTime()+"");
		if(user.getDistributor()!=null){
			order.setDistributor(user.getDistributor());
			//i
		}
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String pid = CommUtil.null2String(form.get("pid"));//商品ID
		String specid = CommUtil.null2String(form.get("specid"));//规格ID
		String disid = CommUtil.null2String(form.get("disid"));//分销商ID
		String numstr = CommUtil.null2String(form.get("num"));//商品数量
		int num = Integer.parseInt(numstr);
		if(!"".equals(pid)){
			Double allAmt = 0.0;
			ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
			ShopDistributor dis = this.shopDistributorService.getShopDistributor(Long.parseLong(disid));
			ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
			
			ShopOrderdetail orderDetail = new ShopOrderdetail();
			orderDetail.setPro(pro);
			orderDetail.setNum(num);
			orderDetail.setUser(user);
			orderDetail.setSeller(dis);
			orderDetail.setShopSpec(spec);
			orderDetail.setCode(new Date().getTime()+"");
			orderDetail.setUnit_price(pro.getAmt());//单价
			orderDetail.setGross_price(pro.getAmt()*num);//总价
			orderDetail.setOrderInfo(order);
			this.shopOrderdetailService.addShopOrderdetail(orderDetail);
			pro.setSaleNum(pro.getSaleNum()+num);
			pro.setInventory(pro.getInventory()-num);
			this.shopProductService.updateShopProduct(pro.getId(), pro);
		}
	
		return DiscoShopUtil.goPage("/shopTrade.java?cmd=toPay&orderId="+order.getId());
	}
	
	/**
	 * 进入支付页面
	 * @param form
	 * @return
	 */
	public Page doToPay(WebForm form){
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		user = this.shopMemberService.getShopMember(user.getId());
		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		if(order.getStatus()!=0 && order.getStatus()!=-1){
			return DiscoShopUtil.goPage("/shopTrade.java?cmd=paySuccess&orderId="+orderId);
		}
		List<CustomCoupon> ccList=null;
		CustomCoupon customCoupon = order.getMyCoupon();
		if(customCoupon==null){
			//运费 -- 订单价格
			Double freight=this.deliveryRuleService.getDeliveryCost(order);
			order.setFreight(freight);
			Double proproduct_price = order.getProduct_price();
			BigDecimal product_price_ = new BigDecimal(proproduct_price.toString());
			BigDecimal freight_ = new BigDecimal(freight.toString());
			order.setGross_price(product_price_.add(freight_).doubleValue());
			this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
			//判断是否有可用优惠券
			ccList=this.customCouponService.judgeCustomCoupon(user, order);
		}
		form.addResult("order", order);
		form.addResult("ccList", ccList);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",Integer.parseInt("1"),"=");
		List<?> payConfigs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
		form.addResult("payConfs", payConfigs);
		form.addResult("rAmt", user.getRemainderAmt());
		form.addResult("fu", formatUtil.fu);
		return new Page("/trade/paySubmit.html");
	}
	
	/**
	 * 支付
	 * @param form
	 * @return
	 */
	public Page doPaySubmit(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
        String payTypeId = CommUtil.null2String(form.get("payTypeId"));
        String defaultbank = CommUtil.null2String(form.get("defaultbank"));
        PaymentConfig payConfig = this.paymentConfigService.getPaymentConfig(Long.parseLong(payTypeId));
        ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
        if(orderInfo.getStatus()!=0){
        	return DiscoShopUtil.goPage("/shopTrade.java?cmd=paySuccess&orderId"+orderId);
        }
        String reMsg = this.shopPayMentService.paySubmit(orderInfo, payConfig, defaultbank);
        return PaymentUtil.sentmsg(reMsg);
	}
	/**
	 * 支付成功回调页面
	 * @param form
	 * @return
	 */
	public Page doPaySuccess(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		form.addResult("order", orderInfo);
		return new Page("/trade/paySuccess.html");
	}
	
	/**
     * 选择支付方式
     * 
     * @param form
     */
    public Page doToChoicepayment(WebForm form) {
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	member = this.shopMemberService.getShopMember(member.getId());
    	String cbox = CommUtil.null2String(form.get("cbox"));
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	Integer payTypeId=CommUtil.null2Int(form.get("payTypeId"));//支付方式
    	Integer orderId=CommUtil.null2Int(form.get("orderId"));
    	ShopOrderInfo order=this.shopOrderInfoService.getShopOrderInfo((long)orderId);
    	if(order==null){
    		return new Page("/userCenter/login.html");
    	}
    	if(order.getStatus()!=0){
        	return DiscoShopUtil.goPage("/shopTrade.java?cmd=paySuccess&orderId="+order.getId());
        }
    	CustomCoupon cc=null;
    	if(order.getMyCoupon()==null){
    		String couponId=CommUtil.null2String(form.get("couponId"));//优惠券
        	if(StringUtils.hasText(couponId)){
        		boolean flag=this.judgeCoupon(member, order, couponId);
            	if(flag){
            		cc=this.customCouponService.getCustomCoupon(Long.valueOf(couponId));
            	}else{
            		return new Page("/userCenter/login.html");
            	}
        	}
    	}
    	PaymentConfig paymentConfig=this.paymentConfigService.getPaymentConfig((long)payTypeId);
    	String returnStr="";
		if("1".equals(cbox)){
			if(member.getRemainderAmt()>0){
				returnStr=this.shopPayMentService.paySubmitByAmt(order, paymentConfig, "", cc, member);
				if("0".equals(returnStr)){//账户余额大于订单价格
					form.addResult("order", order);
					ActionContext.getContext().getSession().setAttribute("DISCO_MEMBER", member);
					PayReturnObj pro = new PayReturnObj(order.getUuid(), order.getCode(), new Date().getTime()+"");
					//this.payCallOrderService.updateOrder(pro);
					this.updOrder(order, pro,member);
					return new Page("/trade/amtPayment.html");
				}else if("1".equals(returnStr)){
					return new Page("/userCenter/login.html");
				}
				form.addResult("je", order.getGross_price()-order.getBalancePay());
			}else{
				returnStr=this.shopPayMentService.paySubmit2(order, paymentConfig, "", cc);
			}
    	}else{
    		returnStr=this.shopPayMentService.paySubmit2(order, paymentConfig, "", cc);
    	}
    	if(cc!=null){
			cc.setStatus((short)1);
			this.customCouponService.updateCustomCoupon(cc.getId(), cc);
		}
    	form.addResult("order", order);
		if(paymentConfig.getType().equals(PayTypeE.WEIXINMPSM)){
			form.addResult("imgUrl", returnStr);
			form.addResult("fu", formatUtil.fu);
			return new Page("/trade/wxPayment.html");
		}else{
			return PaymentUtil.sentmsg(returnStr);
    	}
    }
    
    /**
     * 修改订单
     * @param order
     * @param payreturn
     */
    public void updOrder(ShopOrderInfo order,PayReturnObj payreturn,ShopMember member){
		if(order != null && order.getStatus() != 1) {
			System.out.println("余额支付订单----------------------"+order.getId());
			order.setTradeCode(payreturn.getTransactionId());
			order.setTradeDate(new Date());
			this.shopMemberService.UpdateMemberAfterPay(order);
			this.shopOrderInfoService.disPaySuccess(order);
			if(order.getIsSpokesman() == 0){
				this.shopOrderInfoService.disTributorAmt(order);//处理佣金
			}else{
				this.subsidyService.calculateSubsidyFirst(order);//计算第一次补贴
				this.shopOrderInfoService.spokesmanTeam(order);//处理团队业绩
			}
		}
    }
    
    /**
     * 选择优惠券
     * @param member
     * @param order
     * @param couponId
     * @return
     */
    public Page doChooseCoupon(WebForm form){
    	Map<String,Object> map = new HashMap();
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	member=this.shopMemberService.getShopMember(member.getId());
    	String couponId=CommUtil.null2String(form.get("couponId"));
    	String orderId=CommUtil.null2String(form.get("orderId"));
    	String flag=CommUtil.null2String(form.get("flag"));
    	ShopOrderInfo order=this.shopOrderInfoService.getShopOrderInfo(Long.valueOf(orderId));
    	boolean b=this.judgeCoupon(member, order, couponId);
    	if(!b){
    		map.put("success", false);
    		map.put("msg", "没有获取此信息");
    		form.jsonResult(map);
    	}else{
    		CustomCoupon customCoupon=this.customCouponService.getCustomCoupon(Long.valueOf(couponId));
    		map.put("success", true);
    		map.put("cValue", customCoupon.getCoupon().getValue());
    		Double je=order.getGross_price()-customCoupon.getCoupon().getValue();
    		if("true".equals(flag)){
    			if(member.getRemainderAmt()>=je){
    				map.put("orderPrice", "0");
    			}else{
    				BigDecimal je_ = new BigDecimal(Double.toString(je));
    				BigDecimal rAmt_ = new BigDecimal(Double.toString(member.getRemainderAmt()));
    				map.put("orderPrice", je_.subtract(rAmt_).doubleValue());
    			}
    		}else{
    			map.put("orderPrice", je);
    		}
    		
    		form.jsonResult(map);
    	}
    	return Page.JSONPage;
    }
    
    /**
     * 使用余额
     * @param member
     * @param order
     * @param couponId
     * @return
     */
    public Page doUserbal(WebForm form){
    	Map<String,Object> map = new HashMap();
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member== null){
    		map.put("status", "0");
    		form.jsonResult(map);
    		return Page.JSONPage;
    	}
    	member = this.shopMemberService.getShopMember(member.getId());
    	String xfje = CommUtil.null2String(form.get("xfje"));//消费金额
    	if(StringUtils.hasText(xfje)){
    		Double je_ = Double.valueOf(xfje);//消费金额
    		Double rAmt = Double.valueOf(member.getRemainderAmt());//账户余额
    		BigDecimal amt = new BigDecimal(Double.toString(rAmt));
    		BigDecimal je= new BigDecimal(Double.toString(je_));
    		if(rAmt>=je_){
    			Double ye = amt.subtract(je).doubleValue();
    			BigDecimal b = new BigDecimal(ye);
    			String f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    			String je2 = je.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    			map.put("status", "1");
    			map.put("sje", je2);//金额
    			map.put("sye", f1);//余额
    			map.put("syfje", "0");//应付金额
    			map.put("shxzf", "0");
    		}else{
    			Double yfje = je.subtract(amt).doubleValue();
    			BigDecimal b = new BigDecimal(yfje);
    			String f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    			BigDecimal b2 = new BigDecimal(member.getRemainderAmt());
    			String sje = b2.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    			map.put("status", "1");
    			map.put("sje", sje);
    			map.put("sye", "0");
    			map.put("syfje", f1);//应付金额
    			map.put("shxzf", f1);
    		}
    	}
    	form.jsonResult(map);
    	return Page.JSONPage;
    }
    
    public boolean judgeCoupon(ShopMember member,ShopOrderInfo order,String couponId){
    	boolean falg=false;
    	if(StringUtils.hasText(couponId)){
    		List<CustomCoupon> list=this.customCouponService.judgeCustomCoupon(member, order);
    		for (CustomCoupon customCoupon : list) {
				if(customCoupon.getId().equals(Long.valueOf(couponId))){
					falg=true;
					break;
				}
			}
    	}
    	return falg;
    }
    
    public Page doToIndex(WebForm form){
    	Map<String,String> map = new HashMap<String,String>();
    	String orderId=CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo shopOrderInfo=this.shopOrderInfoService.getShopOrderInfo(Long.valueOf(orderId));
    	if(shopOrderInfo.getStatus()>0){
    		map.put("status", "1");
    	}
    	form.jsonResult(map);
    	return Page.JSONPage;
    }
    
    /**
	 * 检查提交的购买需求在库存方面是否满足
	 * @param form
	 * @param orderFrom
	 */
	private void checkInventoryEnough(WebForm form, String orderFrom){		
		if(orderFrom.equals("direct")){
			String proIdNumsStr = CommUtil.null2String(form.get("proIdNums"));
			String[] idNums = proIdNumsStr.split("_");
			String pid = idNums[0];//商品ID
			String numstr = idNums[1];//商品数量
			String specId = "";
			if(idNums.length > 2){
				specId = idNums[2];//规格	
			}
			
			if(pid.equals("")){
				form.addResult("inventoryError", "该订单没有指定商品，请选购商品重新下单！<br/><a style=\"color:red;\" href=\""+"/goShop.java?cmd=init"+"\">去首页选购商品</a>");				
			}
			if(numstr.equals("")){
				form.addResult("inventoryError", "该订单商品选购数量为0，不能下单！<br/><a style=\"color:red;\" href=\"/goShop.java?cmd=init\">去首页选购商品</a>");
			}
			int num = Integer.parseInt(numstr);
			int inventory = 0;
			if(!specId.equals("")){
				ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specId));
				inventory = spec.getInventory();
			}else{
				ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
				inventory = pro.getInventory();				
			}
			if(0 > (inventory-num) ){
				form.addResult("inventoryError", "该订单商品库存【"+inventory+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+"/goShop.java?cmd=init"+"\">去首页选购其他商品</a>");
			}
		}else if(orderFrom.equals("cart")){
			String carid = CommUtil.null2String(form.get("carId"));			
			String[] carIdNums = carid.split(",");
			for(String id:carIdNums){
				//获取一条购物车数据
				ShoppingCart car = this.shoppingCartService.getShoppingCart(Long.parseLong(id));
				ShopProduct pro = car.getShopProduct();
				int num=car.getBuyNum();
				ShopSpec spec = car.getShopSpec();
				
				if(spec!=null){
					if(0 > (spec.getInventory()-num) ){
						String preError = (String) form.get("inventoryError");
						form.addResult("inventoryError", preError+"<br/>该订单中 <<<"+spec.getProduct().getName()+">>>库存【"+spec.getInventory()+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+"/goShop.java?cmd=toShopCar"+"\">调整数量再下单</a>");
					}
				}else{
					if(0 > (pro.getInventory()-num) ){
						String preError = (String) form.get("inventoryError");
						form.addResult("inventoryError", preError+"<br/>该订单中 <<<"+pro.getName()+">>>库存【"+pro.getInventory()+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+"/goShop.java?cmd=toShopCar"+"\">调整数量再下单</a>");
					}
				}
			}
		}
	}
    
	/**
	 * 错误页
	 * @param form
	 * @param alertMsg
	 * @param returnUrl
	 * @param staticMsg
	 * @return
	 */
	private Page error(WebForm form, String alertMsg, String returnUrl, String staticMsg){
		form.addResult("alertMsg", alertMsg);
		form.addResult("returnUrl", returnUrl);
		form.addResult("staticMsg", staticMsg);
		return new Page("/bcd/pcshop/integral/error.html");
	}
	
	/**
	 * 创建完订单后，回调函数
	 * @param orderInfo
	 * @throws Exception 
	 */
	private void createOrderCallBack(ShopOrderInfo orderInfo, ShopProduct pro, String orderFrom) throws Exception{
		if(orderFrom.equals("direct")){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.pro.id", pro.getId(), "=");
			//商品是否属于秒杀活动商品
			List<RushBuyRegular> regularList = this.regularService.getAllSecKillRegularByQO(qo).getResult();
			if(regularList != null){
				int listNum = regularList.size();
				for(int i=0; i<listNum; ++i){
					RushBuyRegular tempRegular = regularList.get(i);
					QueryObject tempQo = new QueryObject();
					tempQo.addQuery("obj.regular.id", tempRegular.getId(), "=");
					tempQo.addQuery("obj.member.id", orderInfo.getUser().getId(), "=");
					tempQo.addQuery("obj.createDate", orderInfo.getCeateDate(), ">=");
					RushBuyRecord record = this.recordService.getSingleAvailableSecKillRecordByMemberAndRegular(tempRegular, orderInfo.getUser());
					if(record == null){
						System.out.println("下单错误：用户[id="+orderInfo.getUser().getId()+"]未参加促销活动[code="+tempRegular.getCode()+"]，却进入下单页，请程序员检查该BUG!");
						continue;
					}
					record.setOrder(orderInfo);
					this.recordService.updateSecKillRecord(record);
					orderInfo.setType("seckill");
					this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
				}
			}
			//商品是否属于限时抢购活动商品
			qo = new QueryObject();
			qo.addQuery("obj.pro.id", pro.getId(), "=");
			regularList = this.regularService.getAllTimeLimitRegularByQO(qo).getResult();
			if(regularList != null){
				int listNum = regularList.size();
				for(int i=0; i<listNum; ++i){
					RushBuyRegular tempRegular = regularList.get(i);
					QueryObject tempQo = new QueryObject();
					tempQo.addQuery("obj.regular.id", tempRegular.getId(), "=");
					tempQo.addQuery("obj.member.id", orderInfo.getUser().getId(), "=");
					tempQo.addQuery("obj.createDate", orderInfo.getCeateDate(), ">=");
					RushBuyRecord record = this.recordService.getSingleAvailableTimeLimitRecordByMemberAndRegular(tempRegular, orderInfo.getUser());
					if(record == null){
						System.out.println("下单错误：用户[id="+orderInfo.getUser().getId()+"]未参加促销活动[code="+tempRegular.getCode()+"]，却进入下单页，请程序员检查该BUG!");
						continue;
					}
					record.setOrder(orderInfo);
					this.recordService.updateTimeLimitRecord(record);
					orderInfo.setType("timelimit");
					this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
				}
			}
		}else if(orderFrom.equals("cart")){
			//购物车的暂时没写，待定
		}
	}
	
	//获取日期
		public List<?> findDates() {  
	        List lDate = new ArrayList();
	        List lDate2 = new ArrayList();
	        Date date = new Date();
	        Calendar calBegin = Calendar.getInstance();
	        calBegin.setTime(date);  
	        
	        Calendar calEnd = Calendar.getInstance();
	        calEnd.setTime(date);
	        calEnd.add(calEnd.DATE, 7);
	        // 测试此日期是否在指定日期之后    
	        while (calEnd.getTime().after(calBegin.getTime())) {  
	            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
	            calBegin.add(Calendar.DAY_OF_MONTH, 1);  
	            lDate.add((calBegin.getTime().getMonth()+1<10?"0"+(calBegin.getTime().getMonth()+1):calBegin.getTime().getMonth())+"-"+
	            (calBegin.getTime().getDay()<10?"0"+calBegin.getTime().getDay():calBegin.getTime().getDay())+","+getWeek(calBegin)+""); 
	        }  
	        return lDate;  
	    }
		
		//获取星期
		public String getWeek(Calendar calendar){
			String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
			int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if(week_index<0){
				week_index = 0;
			} 
			return weeks[week_index];
		}
	
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	public void setCustomCouponService(ICustomCouponService customCouponService) {
		this.customCouponService = customCouponService;
	}
	public void setDeliveryRuleService(IDeliveryRuleService deliveryRuleService) {
		this.deliveryRuleService = deliveryRuleService;
	}
	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}
	public IShopAddressService getShopAddressService() {
		return shopAddressService;
	}
	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}
	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}
	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public IShopOrderdetailService getShopOrderdetailService() {
		return shopOrderdetailService;
	}
	public void setShopOrderdetailService(
			IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}
	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}
	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}
	public IShopPayMentService getShopPayMentService() {
		return shopPayMentService;
	}
	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}
	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}	
	public IRushBuyRegularService getRegularService() {
		return regularService;
	}
	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}
	public IRushBuyRecordService getRecordService() {
		return recordService;
	}
	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}
	public IDeliveryRuleService getDeliveryRuleService() {
		return deliveryRuleService;
	}
	public ICustomCouponService getCustomCouponService() {
		return customCouponService;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
	
}
