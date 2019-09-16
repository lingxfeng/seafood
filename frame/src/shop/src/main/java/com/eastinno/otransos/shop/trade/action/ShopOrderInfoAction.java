package com.eastinno.otransos.shop.trade.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.shop.core.action.ShopBaseAction;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.trade.domain.LogisticsCompany;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.ILogisticsCompanyService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopPayMentService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 订单管理
 * ShopOrderInfoAction
 * @author nsz
 */
@Action
public class ShopOrderInfoAction extends WxShopBaseAction {
    @Inject
    private IShopOrderInfoService service;
    @Inject
    private IShopPayMentService shopPayMentService;
    @Inject
    private IPaymentConfigService paymentConfigService;
    @Inject
    private IShopDistributorService shopDistributorService;
    @Inject
    private IDeliveryRuleService deliveryRuleService;
    @Inject
    private ILogisticsCompanyService logisticsCompanyService;
    @Inject
    private IShopProductService shopProductservice;
    @Inject
    private IShopSpecService shopSpecservice;
	/**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    public Page doViewList(WebForm form){
    	String status = CommUtil.null2String(form.get("status"));
    	String orderType = CommUtil.null2String(form.get("orderType"));
    	String userId = CommUtil.null2String(form.get("userId"));
    	int currentPage=CommUtil.null2Int(form.get("currentPage"));
    	form.toPo(ShopOrderInfo.class);
    	QueryObject qo = new QueryObject();
    	if(!"".equals(status)){
    		qo.addQuery("obj.status",Integer.parseInt(status),"=");
    	}
    	if(!"".equals(userId)){
    		qo.addQuery("obj.user.id", Long.parseLong(userId), "=");
    	}
    	if(StringUtils.hasText(orderType)){
    		qo.addQuery("obj.orderType", Short.valueOf(orderType), "=");
    	}
    	qo.setOrderBy("ceateDate");
    	qo.setOrderType("desc");
    	qo.setCurrentPage(currentPage);
        IPageList pl = this.service.getShopOrderInfoBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        form.addResult("fu", formatUtil.fu);
    	return new Page("/shopmanage/trade/ShopOrderInfo/orderInfoViewList.html");
    }
    /**
     * 发货列表
     * @param form
     * @return
     */
    public Page doSendList(WebForm form){
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.status",Integer.parseInt("1"),"=");
    	qo.setOrderBy("ceateDate");
    	qo.setOrderType("desc");
        IPageList pl = this.service.getShopOrderInfoBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        form.addResult("fu", formatUtil.fu);
    	return new Page("/shopmanage/trade/ShopOrderInfo/sendList.html");
    }
    /**
     * 收货管理
     * @param form
     * @return
     */
    public Page doReceiveList(WebForm form){
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.status",Integer.parseInt("2"),"=");
    	qo.setOrderBy("ceateDate");
    	qo.setOrderType("desc");
        IPageList pl = this.service.getShopOrderInfoBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        form.addResult("fu", formatUtil.fu);
    	return new Page("/shopmanage/trade/ShopOrderInfo/orderReceiveList.html");
    }
    /**
     * 订单详情
     * @param form
     * @return
     */
    public Page doOrderDetail(WebForm form){
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.service.getShopOrderInfo(Long.parseLong(orderId));
    	form.addResult("order", order);
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.status",Short.parseShort("1"), "=");
        qo.setPageSize(-1);
        IPageList pageList = this.logisticsCompanyService.getLogisticsCompanyBy(qo);
        form.addResult("lc", pageList.getResult());
        form.addResult("fu", formatUtil.fu);
    	return new Page("/shopmanage/trade/ShopOrderInfo/orderDetail.html");
    }

    /**
     * 客户订单详情
     * @param form
     * @return
     */
    public Page doOrderDetail2(WebForm form){
    	
    	//判断身份
    	HttpSession session = ActionContext.getContext().getSession();
    	ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
    	user = this.shopMemberService.getShopMember(user.getId());
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
    	
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.service.getShopOrderInfo(Long.parseLong(orderId));

        //邮费计算和设置,每次进入订单支付页都会及时的计算邮费并更新订单总价
        Map<Long, Double> costMap = this.deliveryRuleService.getDeliveryCostMap(order);
//      Double freight = this.deliveryRuleService.getDeliveryCost(order);
        Double freight = this.getCostByBrandCostMap(costMap);
        Double gross_price = order.getGross_price()-order.getFreight()+freight;
        order.setFreight(freight);
        order.setGross_price(gross_price);
        this.service.updateShopOrderInfo(order.getId(), order);
        form.addResult("costMap", costMap);

    	form.addResult("order", order);
    	/*if(order.getStatus()==0){
        	QueryObject qo = new QueryObject();
        	qo.addQuery("obj.type",PayTypeE.WEIXINMPAPI,"=");
        	qo.setPageSize(1);
        	List<PaymentConfig> configs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
        	if(configs!=null && configs.size()>0){
        		PaymentConfig config = configs.get(0);
        		String reMsg = this.shopPayMentService.paySubmit(order, config, "");
        		form.addResult("jsStr", reMsg);
        	}
    	}*/
    	BigDecimal b = new BigDecimal(user.getRemainderAmt());
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	form.addResult("rAmt", f1);
    	form.addResult("fu", formatUtil.fu);
    	return new Page("/bcd/wxshop/trading/orderDetails.html");
    }

    /**
     * 依据cost map获取总邮费
     * @param costMap
     * @return
     */
    private Double getCostByBrandCostMap(Map<Long, Double> costMap){
        Double result = 0D;
        Set keySet = costMap.keySet();
        Iterator iter = keySet.iterator();
        while(iter.hasNext()){
            result += (Double)costMap.get((Long)iter.next());
        }
        return result;
    }
    /**
     * 取消订单
     * @param form
     * @return
     */
    public Page doCancel(WebForm form){
    	ShiroUser user = (ShiroUser)ShiroUtils.getShiroUser();
		if(!"root".equals(user.getName())){
			this.addError("msg", "你没有权限修改");
			pageForExtForm(form);
		}
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.service.getShopOrderInfo(Long.parseLong(orderId));
    	order.setStatus(-1);
		this.service.updateShopOrderInfo(order.getId(), order);
		List<ShopOrderdetail> list2 = order.getOrderdetails();
		for(ShopOrderdetail orderdetail:list2){
			ShopProduct shopProduct = orderdetail.getPro();
			ShopSpec spec = orderdetail.getShopSpec();
			if(spec != null){
				int count = orderdetail.getNum();
    			spec.setInventory(spec.getInventory()+count);
    			shopProduct.setInventory(shopProduct.getInventory()+count);
    			this.shopProductservice.updateShopProduct(shopProduct.getId(), shopProduct);
    			this.shopSpecservice.updateShopSpec(spec.getId(), spec);
			}else{
				int count = orderdetail.getNum();
    			shopProduct.setInventory(shopProduct.getInventory()+count);
    			this.shopProductservice.updateShopProduct(shopProduct.getId(), shopProduct);
    		}
		}
    	return pageForExtForm(form);
    }
    /**
     * 打印订单
     * @param form
     * @return
     */
    public Page doPutsea(WebForm form){
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.service.getShopOrderInfo(Long.parseLong(orderId));
    	form.addResult("order", order);
    	form.addResult("fu", formatUtil.fu);
    	return new Page("/shopmanage/trade/ShopOrderInfo/orderPutsea.html");
    }
    /**
     * 确认发货
     * @param form
     * @return
     */
    public Page doSendPro(WebForm form){
    	String orderId = CommUtil.null2String(form.get("id"));
    	ShopOrderInfo order = this.service.getShopOrderInfo(Long.parseLong(orderId));
    	String expressCode = CommUtil.null2String(form.get("expressCode"));
    	String expressid = CommUtil.null2String(form.get("expressid"));
    	LogisticsCompany lc = this.logisticsCompanyService.getLogisticsCompany(Long.parseLong(expressid));
    	order.setLogisticsCompany(lc);
    	order.setExpressCode(expressCode);
    	order.setStatus(2);
    	order.setSendDate(new Date());
    	this.service.updateShopOrderInfo(Long.parseLong(orderId), order);
    	ShopMember member = order.getUser();
		Follower f=member.getFollower();
		if(f!=null){
			Account a = f.getAccount();
			WeixinBaseUtils.sendMsgToFollower(a, f, "【北京百春达电子商务有限公司】尊敬的客户"+member.getNickname()+"您好！您的订单:"+order.getCode()+"已经发出，"+order.getLogisticsCompany().getName()+"快递单号："+order.getExpressCode()+"，请保持电话通畅，您可在手机端和PC端的订单内查询实时物流信息，收货时请当面验货。也可随时致电010-53646367查询物流信息。");
		}
    	return go("sendList");
    }
    /**
     * 所有订单
     * @param form
     * @return
     */
    public Page doOrderAllList1(WebForm form){
    	String status = CommUtil.null2String(form.get("status"));
    	QueryObject qo = new QueryObject();
    	if(!"".equals(status)){
    		qo.addQuery("obj.status",Integer.parseInt(status),"=");
    	}
    	IPageList pl = this.service.getShopOrderInfoBy(qo);
    	CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
    	return new Page("/shopmanage/trade/ShopOrderInfo/orderAllList.html");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pl = this.service.getShopOrderInfoBy(qo);
        form.addResult("pl", pl);
        return new Page("/shopmanage/trade/shopOrderInfo/shopOrderInfoList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/trade/shopOrderInfo/shopOrderInfoEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopOrderInfo entry = (ShopOrderInfo)form.toPo(ShopOrderInfo.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopOrderInfo(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopOrderInfo entry = this.service.getShopOrderInfo(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/trade/shopOrderInfo/shopOrderInfoEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopOrderInfo entry = this.service.getShopOrderInfo(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopOrderInfo(id, entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delShopOrderInfo(id);
        return go("list");
    }
    
    public void setService(IShopOrderInfoService service) {
        this.service = service;
    }
	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}
	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	public IDeliveryRuleService getDeliveryRuleService() {
		return deliveryRuleService;
	}
	public void setDeliveryRuleService(IDeliveryRuleService deliveryRuleService) {
		this.deliveryRuleService = deliveryRuleService;
	}
	public ILogisticsCompanyService getLogisticsCompanyService() {
		return logisticsCompanyService;
	}
	public void setLogisticsCompanyService(
			ILogisticsCompanyService logisticsCompanyService) {
		this.logisticsCompanyService = logisticsCompanyService;
	}
	public void setShopProductservice(IShopProductService shopProductservice) {
		this.shopProductservice = shopProductservice;
	}
	public void setShopSpecservice(IShopSpecService shopSpecservice) {
		this.shopSpecservice = shopSpecservice;
	}
	
}
