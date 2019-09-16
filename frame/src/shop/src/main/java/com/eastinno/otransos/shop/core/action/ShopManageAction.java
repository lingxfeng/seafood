package com.eastinno.otransos.shop.core.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemLogService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.content.service.IShopDiscussService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

/**
 * 管理员和商户后台管理
 * @author nsz
 */
public class ShopManageAction extends ShopBaseAction{
	@Inject
    private IShopMemberService shopMemberService;
	
	@Inject
    private IShopProductService shopProductService;
	
	@Inject
    private IShopDiscussService shopDiscussService;
	
	@Inject
    private IShopOrderInfoService shopOrderInfoService;
	@Inject
	private ISystemLogService logService;
	
    public void setLogService(ISystemLogService logService) {
		this.logService = logService;
	}
	@Override
	public Page doInit(WebForm form, Module module) {
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()){
			return go("toLogin");
		}
		return new Page("/shopmanage/index.html");
	}
	
	
	
	@Override
	public Object doBefore(WebForm form, Module module) {
		form.addResult("shopUser", ShiroUtils.getUser());
		return super.doBefore(form, module);
	}



	/**
	 * 跳转到登录页面
	 * @param form
	 * @return
	 */
	public Page doToLogin(WebForm form){
		return new Page("/shopmanage/login.html");
	}
	public Page doLogin(WebForm form){
		String randomCode = CommUtil.null2String(form.get("code"));
        String randomCodeS = ActionContext.getContext().getSession().getAttribute("rand") + "";
        if (!randomCode.equals(randomCodeS)) {
            form.addResult("erroMsg", "验证码不正确！！！");
        } else {
            String userName = CommUtil.null2String(form.get("username"));
            String password = CommUtil.null2String(form.get("password"));
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            Subject currentUser = SecurityUtils.getSubject();
            try {
                currentUser.login(token);
                SystemLog log = ShiroUtils.getSystemLog(ActionContext.getContext().getRequest());
                log.setParams("电商后台登录日志");
                log.setTypes(1);
                logService.addSystemLog(log);
                return go("init");
            } catch (AuthenticationException ae) {
                form.addResult("erroMsg", "用户名或密码不正确!");
            }
        }
		return go("toLogin");
	}
	/**
	 * 退出登录
	 * @param form
	 * @return
	 */
	public Page doLogout(WebForm form){
		SecurityUtils.getSubject().logout();
		return go("init");
	}
	public Page doWelcom(WebForm form){
		return new Page("/shopmanage/welcome.html");
	}
	
	/**
	 * 调转欢迎界面
	 * @param form
	 * @return
	 */
	public Page doToWelcome(WebForm form){
		
		Tenant tenant = ShiroUtils.getTenant();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.tenant", tenant, "=");
		qo.addQuery("obj.type", 4, "=");
		int registerNum=this.shopMemberService.getShopMemberBy(qo).getRowCount();
		form.addResult("registerNum", registerNum);
		
		//Long aaa=new Date().getTime()/(1000*60*60*24);
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();  
		String time=format.format(date);
	    qo.addQuery("date_format(obj.registerTime,'%Y-%m-%d') = '"+time+"'");
	    int xzregisterNum=this.shopMemberService.getShopMemberBy(qo).getRowCount();
	    form.addResult("xzregisterNum", xzregisterNum);
		
	    //商品
		QueryObject qoPro = new QueryObject();
		qoPro.addQuery("obj.tenant", tenant, "=");
		int proNum=this.shopProductService.getShopProductBy(qoPro).getRowCount();
		form.addResult("proNum", proNum);
		qoPro.addQuery("obj.status", (short)1, "=");//商品状态；0，未发布，1.已发布，2.违规下架
		int sellProNum=this.shopProductService.getShopProductBy(qoPro).getRowCount();
		form.addResult("sellProNum", sellProNum);
		qoPro = new QueryObject();
		qoPro.addQuery("obj.tenant", tenant, "=");
		qoPro.addQuery("obj.status", (short)0, "=");
		int warehouseProNum=this.shopProductService.getShopProductBy(qoPro).getRowCount();
		form.addResult("warehouseProNum", warehouseProNum);
		//新增商品数
		qoPro = new QueryObject();
		qoPro.addQuery("obj.tenant", tenant, "=");
		qoPro.addQuery("date_format(obj.createDate, '%Y-%m-%d')", time, "=");
		int xzPro=this.shopProductService.getShopProductBy(qoPro).getRowCount();
		form.addResult("xzPro", xzPro);
		
		//订单
		QueryObject qoOrder = new QueryObject();
		qoOrder.addQuery("obj.tenant", tenant, "=");
		qoOrder.addQuery("obj.status", 1, "=");//订单状态:0未支付 ，1已支付待发货，2商家已发货，3用户已收货，-1已取消订单
		int dfhorderNum=this.shopOrderInfoService.getShopOrderInfoBy(qoOrder).getRowCount();
		form.addResult("dfhorderNum", dfhorderNum);
		qoOrder = new QueryObject();
		qoOrder.addQuery("obj.tenant", tenant, "=");
		qoOrder.addQuery("obj.status", 0, "=");
		int noPayorderNum=this.shopOrderInfoService.getShopOrderInfoBy(qoOrder).getRowCount();
		form.addResult("noPayorderNum", noPayorderNum);
		qoOrder = new QueryObject();
		qoOrder.addQuery("obj.tenant", tenant, "=");
		qoOrder.addQuery("date_format(obj.ceateDate, '%Y-%m-%d')", time, "=");
		int xzorderNum=this.shopOrderInfoService.getShopOrderInfoBy(qoOrder).getRowCount();
		form.addResult("xzorderNum", xzorderNum);
		
		//
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
		  form.addResult("system001", "window"); 
		} else if(os.toLowerCase().startsWith("linux")){
			form.addResult("system001", "linux"); 
		}
		String javaVer=System.getProperty("java.version");
		form.addResult("javaVer", javaVer);
		
		//咨询
		QueryObject qodis = new QueryObject();
		qodis.addQuery("obj.tenant", tenant, "=");
		qodis.addQuery("date_format(obj.createDate , '%Y-%m-%d')", time, "=");
		qodis.addQuery("obj.tenant", tenant, "=");
		qodis.addQuery("obj.type", 2, "=");
		int xzdis=this.shopDiscussService.getShopDiscussBy(qodis).getRowCount();
		form.addResult("xzDis", xzdis);
		
		return new Page("/shopmanage/welcome.html");
	}
	
	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
}
