package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.application.util.SmsUtils;
import com.eastinno.otransos.core.service.IEmailService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.core.util.RegexUtils;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.dao.IDeliveryRuleDAO;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.trade.dao.IShopOrderInfoDAO;
import com.eastinno.otransos.shop.trade.domain.ApplyRefund;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.dao.IShopMemberDAO;
import com.eastinno.otransos.shop.usercenter.dao.IShopMemberRatingDAO;
import com.eastinno.otransos.shop.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShopMemberRating;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberRatingService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.shop.util.Verification;
import com.eastinno.otransos.shop.util.msgUtils;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.web.validate.ValidateType;


/**
 * ShopMemberServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopMemberServiceImpl implements IShopMemberService{
	@Resource
	private IShopMemberDAO shopMemberDao;
	
	@Autowired
    private IEmailService emailService;
	
	@Autowired
	private IShopMemberRatingService ratingService;
	
	@Autowired
	private IIntegralHistoryService integralHistoryService;
	
	@Autowired
	private IDeliveryRuleService deliveryRuleService;
	@Autowired
	private IShopDistributorService shopDistributorService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRemainderAmtHistoryService remainderAmtHistoryService;
	
	public void setRatingService(IShopMemberRatingService ratingService) {
		this.ratingService = ratingService;
	}
	
	
	
	
	@Autowired
	private IShopMemberRatingDAO shopMemberRatingDao;
	
	@Resource
	private IShopOrderInfoDAO shopOrderInfoDao;
	
	public IPageList getShopMemberListForDisplayByQO(QueryObject qo){
		IPageList pageList = this.shopMemberDao.findBy(qo);
		List data = pageList.getResult();
		if(data == null){
			data = new ArrayList();
		}
		int columns = data.size();
		ShopMember temp;
		for(int i=0; i<columns; ++i){
			temp = (ShopMember) data.get(i);
			ShopMemberRating rating = this.ratingService.getShopMemberRatingByShopMember(temp);
			if(rating != null){
				temp.setUserRatingName(rating.getName());
			}else{
				temp.setUserRatingName("");
			}
		}
		return pageList;
	}
	
	public void setShopOrderInfoDao(IShopOrderInfoDAO shopOrderInfoDao) {
		this.shopOrderInfoDao = shopOrderInfoDao;
	}

	public void setShopMemberDao(IShopMemberDAO shopMemberDao){
		this.shopMemberDao=shopMemberDao;
	}
	
	public Long addShopMember(ShopMember shopMember) {
		String dePath="@"+shopMember.getCode();
		if(shopMember.getPmember()!=null){
			dePath= shopMember.getPmember().getDePath()+dePath;
		}
		shopMember.setDePath(dePath);
		this.shopMemberDao.save(shopMember);
		if (shopMember != null && shopMember.getId() != null) {
			return shopMember.getId();
		}
		return null;
	}
	
	public ShopMember getShopMember(Long id) {
		ShopMember shopMember = this.shopMemberDao.get(id);
		return shopMember;
		}
	
	public boolean delShopMember(Long id) {
			ShopMember shopMember = this.getShopMember(id);
			if (shopMember != null) {
				this.shopMemberDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopMembers(List<Serializable> shopMemberIds) {
		
		for (Serializable id : shopMemberIds) {
			delShopMember((Long) id);
		}
		return true;
	}
	
	public IPageList getShopMemberBy(IQueryObject queryObj) {
		return this.shopMemberDao.findBy(queryObj);	
	}
	
	public boolean updateShopMember(Long id, ShopMember shopMember) {
		if (id != null) {
			shopMember.setId(id);
		} else {
			return false;
		}
		this.shopMemberDao.update(shopMember);
		return true;
	}

	@Override
	public boolean MemberRegisterVerification(ShopMember shopMember ,String code) {
		String name=shopMember.getName();
		String password=shopMember.getPassword();
		String password2=shopMember.getPassword2();
		String email=shopMember.getEmail();
		if(!StringUtils.hasText(name)){
			FrameworkEngine.getValidateManager().addCustomError("msg", "用户名不能为空", ValidateType.Action);
			return false;
		}else{
			if(!Pattern.matches("[A-Za-z]\\w{3,20}?", name)){
				FrameworkEngine.getValidateManager().addCustomError("msg", "请输入4-20位英文字母开头的字母或数字和下划线", ValidateType.Action);
				return false;
			}
		}
		if(!StringUtils.hasText(password) || !StringUtils.hasText(password2)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码、确认密码不能为空", ValidateType.Action);
			return false;
		}else {
			if(password.length()<6 && password.length()>20){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "密码的长度为6-20字符", ValidateType.Action);
				return false;
			}
			if(!password.equals(password2)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "密码确认密码不能为空", ValidateType.Action);
				return false;
			}
		}
		if(!StringUtils.hasText(email)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "邮箱不能为空", ValidateType.Action);
			return false;
		}else {
			if(!RegexUtils.checkEmail(email)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "邮箱格式不正确", ValidateType.Action);
				return false;
			}
		}
		String rand=(String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!rand.equals(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "输入的验证码不正确", ValidateType.Action);
			return false;
		}
		
		String hql="from " +User.class.getName() +" obj where obj.name = ?";
		String[] params ={ name };
		List<?> list=this.shopMemberDao.query(hql, params);
		if(list!=null && list.size()>0){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此用户已注册", ValidateType.Action);
			return false;
		}
		
		String hql2="from " +User.class.getName() +" obj where obj.email = ?";
		String[] params2 ={ email };
		List<?> list2=this.shopMemberDao.query(hql2, params2);
		if(list2!=null && list2.size()>0){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此邮箱已注册", ValidateType.Action);
			return false;
		}
		return true;
	}

	@Override
	public boolean MemberLogin(String name, String password, String code) {
		if(!StringUtils.hasText(name)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "用户名不能为空", ValidateType.Action);
			return false;
		}if(!StringUtils.hasText(password)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码不能为空", ValidateType.Action);
			return false;
		}
		String rand=(String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!rand.equals(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码不正确", ValidateType.Action);
			return false;
		}
		ShopMember member=this.shopMemberDao.getBy("name", name);
		if(member==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此用户", ValidateType.Action);
			return false;
		}
		if(!MD5.encode(password).equals(member.getPassword())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "用户名或密码不正确", ValidateType.Action);
			return false;
		}
		UserContext.setMember(member);
		return true;
	}

	@Override
	public boolean ValidateAndSendCode(String name, String emailOrMobileTel,String code) {
		if(!StringUtils.hasText(name)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入用户名", ValidateType.Action);
			return false;
		}
		if(!StringUtils.hasText(emailOrMobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入申请用户是对应的邮箱或手机", ValidateType.Action);
			return false;
		}
		if(!StringUtils.hasText(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入验证码", ValidateType.Action);
			return false;
		}
		
		String rand=(String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!code.equals(rand)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
			return false;
		}
		
		if(RegexUtils.checkEmail(emailOrMobileTel)){
			boolean b=emailSendCode(name,emailOrMobileTel);
			if(!b){
				return false;
			}
		}
		if(RegexUtils.checkMobile(emailOrMobileTel)){
			boolean b=mobileTelSendCode(name, emailOrMobileTel);
			if(!b){
				return false;
			}
		}
		if(!RegexUtils.checkEmail(emailOrMobileTel) && !RegexUtils.checkMobile(emailOrMobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入正确的邮箱或手机格式", ValidateType.Action);
			return false;
		}
		return true;
	}
	
	public boolean emailSendCode(String name, String emailOrMobileTel){
		QueryObject qoObject = new QueryObject();
		qoObject.addQuery("obj.email", emailOrMobileTel, "=");
		List<?> list=this.shopMemberDao.findBy(qoObject).getResult();
		if(list==null && list.size()<1){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此邮箱", ValidateType.Action);
			return false;
		}
		ShopMember shopMember=(ShopMember)list.get(0);
		if(!name.equals(shopMember.getName())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此邮箱对应的用户名不正确", ValidateType.Action);
			return false;
		}
		String emailTitle = "找回密码";
        String emailContent = "邮件内容";
        String sixRand = getSixRand();
		// 发送邮件
        emailService.sendEmail(emailOrMobileTel, emailTitle, sixRand, false);
        ActionContext.getContext().getSession().setAttribute("sixRand", sixRand);
        ActionContext.getContext().getSession().setAttribute("shopMember", shopMember);
		return true;
	}
	
	public boolean mobileTelSendCode(String name, String emailOrMobileTel){
		QueryObject qoObject = new QueryObject();
		qoObject.addQuery("obj.mobileTel", emailOrMobileTel, "=");
		List<?> list=this.shopMemberDao.findBy(qoObject).getResult();
		if(list==null && list.size()<1){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此手机", ValidateType.Action);
			return false;
		}
		ShopMember shopMember=(ShopMember)list.get(0);
		if(!name.equals(shopMember.getName())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此手机对应的用户名不正确", ValidateType.Action);
			return false;
		}
		String sixRand = getSixRand();
        String result = SmsUtils.sendMessage(emailOrMobileTel, "注册验证码：" + sixRand);
        if (!"Success".equals(result)) {
            FrameworkEngine.findValidatorManager().addCustomError("msg", "发送验证码失败", ValidateType.Action);
            return false;
        }
        ActionContext.getContext().getSession().setAttribute("sixRand", sixRand);
        ActionContext.getContext().getSession().setAttribute("shopMember", shopMember);
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean getPwd(String name,String emailOrMobileTel,String code) {
		if (!StringUtils.hasText(name)) {
			FrameworkEngine.findValidatorManager().addCustomError("msg", "用户名不能为空", ValidateType.Action);
            return false;
		}
		if (!StringUtils.hasText(emailOrMobileTel)) {
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请填写邮箱或者手机号码", ValidateType.Action);
            return false;
		}
		if (!StringUtils.hasText(code)) {
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码不能为空", ValidateType.Action);
            return false;
		}
		if(RegexUtils.checkEmail(emailOrMobileTel)){
			boolean b=emailGetPwd(name,emailOrMobileTel,code);
			if(!b){
				return false;
			}
		}
		if(RegexUtils.checkMobile(emailOrMobileTel)){
			boolean b=mobleTelGetPwd(name, emailOrMobileTel,code);
			if(!b){
				return false;
			}
		}
		if(!RegexUtils.checkEmail(emailOrMobileTel) && !RegexUtils.checkMobile(emailOrMobileTel)){
			FrameworkEngine.getValidateManager().addCustomError("msg", "请输入正确的邮箱或手机格式", ValidateType.Action);
			return false;
		}
		
		return true;
	}
	
	public boolean emailGetPwd(String name,String email,String code) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.name", name, "=");
		List<?> list=this.shopMemberDao.findBy(qo).getResult();
		if(list==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此用户", ValidateType.Action);
			return false;
		}
		ShopMember shopMember=(ShopMember)list.get(0);
		if(!email.equals(shopMember.getEmail())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入此用户名对应的邮箱", ValidateType.Action);
			return false;
		}
		String rand=(String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!rand.equals(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
			return false;
		}
		
		String sixRand = getSixRand();
		String emailTitle = "找回密码";
        String emailContent = sixRand;
		// 发送邮件
        emailService.sendEmail(email, emailTitle, emailContent, false);
        ActionContext.getContext().getSession().setAttribute("sixRand", sixRand);
        ActionContext.getContext().getSession().setAttribute("shopMember", shopMember);
		
		return true;
	}
	
	public boolean mobleTelGetPwd(String name,String mobileTel,String code) {
		ShopMember shopMember=this.shopMemberDao.getBy("name", name);
		if(shopMember==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此手机对应的用户", ValidateType.Action);
			return false;
		}
		if(!mobileTel.equals(shopMember.getMobileTel())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入此用户名对应的手机", ValidateType.Action);
			return false;
		}
		String rand=(String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!rand.equals(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
			return false;
		}
		String sixRand = getSixRand();
        //String result = SmsUtils.sendMessage(mobileTel, "注册验证码：" + sixRand);
		String result = msgUtils.sendMessage(mobileTel, "【百春达】注册验证码：" + sixRand);
        if (!"Success".equals(result)) {
            FrameworkEngine.findValidatorManager().addCustomError("msg", "发送验证码失败", ValidateType.Action);
            return false;
        }
        ActionContext.getContext().getSession().setAttribute("sixRand", sixRand);
        ActionContext.getContext().getSession().setAttribute("shopMember", shopMember);
		return true;
	}
	
	@Override
	public boolean judgeCode(ShopMember shopMember, String code) {
		if(shopMember==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "获取验证码失败,请重新发送", ValidateType.Action);
			return false;
		}
		if (!StringUtils.hasText(code)) {
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码不能为空", ValidateType.Action);
			return false;
		}
		String sixRand=(String)ActionContext.getContext().getSession().getAttribute("sixRand");
		if(!code.equals(sixRand)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
            return false;
		}
		return true;
	}
	

	// 生成验证码
    public static String getSixRand() {
        int i = 1;// i在此程序中只作为判断是否是将随机数添加在首位，防止首位出现0；
        Random r = new Random();
        int tag[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        String str = "";
        int temp = 0;

        while (str.length() < 6) {
            temp = r.nextInt(10);// 取出0(含)~10(不含)之间任意数
            if (i == 1 && temp == 0) {
                continue;
            }
            else {
                i = 2;
                if (tag[temp] == 0) {
                    str = str + temp;
                    tag[temp] = 1;
                }
            }
        }
        return str;
    }

	@Override
	public boolean updatePwd(String password, String password2, ShopMember shopMember) {
		if(shopMember==null){
			FrameworkEngine.getValidateManager().addCustomError("msg", "修改密码失败", ValidateType.Action);
			return false;
		}
		if(!StringUtils.hasText(password) || !StringUtils.hasText(password2)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码、确认密码不能为空", ValidateType.Action);
            return false;
		}
		if(password.length()<6 && password.length()>20){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码的长度为6-20字符", ValidateType.Action);
			return false;
		}
		if(!password.equals(password2)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码、确认密码输入的不一致", ValidateType.Action);
            return false;
		}
		shopMember.setPassword(MD5.encode(password));
		this.shopMemberDao.update(shopMember);
		return true;
	}

	@Override
	public ShopMember getUserInfo(ShopMember shopMember) {
		
		QueryObject qo = new QueryObject();
		qo.setOrderBy("sequence");
		qo.setOrderType("asc");
		List<?> list=this.shopMemberRatingDao.findBy(qo).getResult();
		long tpjf = 0;
		long ypjf = 0;
		long jpjf = 0;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				ShopMemberRating shopMemberRating=(ShopMemberRating)list.get(i);
				if("金牌用户".equals(shopMemberRating.getName())){
					jpjf=shopMemberRating.getIntegral();
				}else if ("银牌用户".equals(shopMemberRating.getName())) {
					ypjf=shopMemberRating.getIntegral();
				}else if ("铜牌用户".equals(shopMemberRating.getName())) {
					tpjf=shopMemberRating.getIntegral();
				}
				
			}
		}
		//用户等级
		if(shopMember.getTotalIntegral()<=tpjf){
			shopMember.setUserRating(3);
		}else if(shopMember.getTotalIntegral()>=tpjf && shopMember.getTotalIntegral()<=ypjf){
			shopMember.setUserRating(2);
		}else if(shopMember.getTotalIntegral()>=jpjf){
			shopMember.setUserRating(1);
		}
		//安全级别
		if(shopMember.getMobileTel()==null && shopMember.getIdCard()==null){
			shopMember.setSecurityLevel(1);
		}else if (shopMember.getMobileTel()!=null || shopMember.getIdCard()!=null) {
			if (shopMember.getMobileTel()!=null && shopMember.getIdCard()!=null) {
				shopMember.setSecurityLevel(3);
			}else {
				shopMember.setSecurityLevel(2);
			}
		}
		
		return shopMember;
	}

	@Override
	public boolean updatePassword(String oldPwd, String newPwd, String confirmPwd, ShopMember shopMember) {
		if(!StringUtils.hasText(newPwd) || !StringUtils.hasText(confirmPwd)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "新密码、确认新密码不能为空", ValidateType.Action);
			return false;
		}else {
			if(newPwd.length()<6 && newPwd.length()>20){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "密码的长度为6-20字符", ValidateType.Action);
				return false;
			}
		}
		if(!newPwd.equals(confirmPwd)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "新密码、确认新密码输入的不一致", ValidateType.Action);
			return false;
		}
		if(!MD5.encode(oldPwd).equals(shopMember.getPassword())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "旧密码输入不正确", ValidateType.Action);
			return false;
		}
		shopMember.setPassword(MD5.encode(newPwd));
		this.shopMemberDao.update(shopMember);
		return true;
	}

	@Override
	public boolean updateEmail(String password, String email,ShopMember shopMember) {
		if(!StringUtils.hasText(password)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码不能为空", ValidateType.Action);
			return false;
		}else {
			if(!shopMember.getPassword().equals(MD5.encode(password))){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "密码输入不正确", ValidateType.Action);
				return false;
			}
		}
		if(!RegexUtils.checkEmail(email)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "邮箱格式不正确", ValidateType.Action);
			return false;
		}
		shopMember.setEmail(email);
		this.shopMemberDao.update(shopMember);
		return true;
	}

	@Override
	public boolean updateMobileTel(String mobileTel,String code,ShopMember shopMember) {
		if(!RegexUtils.checkMobile(mobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "手机输入不正确", ValidateType.Action);
			return false;
		}
		if(!StringUtils.hasText(code)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码不能为空", ValidateType.Action);
			return false;
		}
		String mTel=(String)ActionContext.getContext().getSession().getAttribute("bcd_mobileTel");
		if(!mTel.equals(mobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "你的验证手机为"+mTel, ValidateType.Action);
			return false;
		}
		String sixRand = (String)ActionContext.getContext().getSession().getAttribute("bcd_sixRand");
		if(StringUtils.hasText(sixRand)){
			if(!sixRand.equals(code)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
				return false;
			}
		}else{
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码已失效", ValidateType.Action);
			return false;
		}
		shopMember.setMobileTel(mobileTel);
		this.shopMemberDao.update(shopMember);
		return true;
	}

	@Override
	public boolean sendCode(String mobileTel) {
		// TODO Auto-generated method stub
		if(!RegexUtils.checkMobile(mobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "手机输入不正确", ValidateType.Action);
			return false;
		}
		String sixRand = getSixRand();
		//String result = SmsUtils.sendMessage(mobileTel, "【百春达】验证码：" + sixRand);
        String result = msgUtils.sendMessage(mobileTel, "【百春达】验证码：" + sixRand);
        if (!"Success".equals(result)) {
            FrameworkEngine.findValidatorManager().addCustomError("msg", "发送验证码失败", ValidateType.Action);
            return false;
        }
        ActionContext.getContext().getSession().setAttribute("bcd_mobileTel", mobileTel);
        ActionContext.getContext().getSession().setAttribute("bcd_sixRand", sixRand);
		return true;
	}

	@Override
	public List<?> getOrderInfo(WebForm form , ShopMember shopMember) {
		int status=CommUtil.null2Int(form.get("status"));
		QueryObject qoObject = new QueryObject();
    	qoObject.addQuery("obj.user.id", shopMember.getId(), "=");
    	qoObject.setOrderBy("ceateDate");
    	qoObject.setOrderType("desc");
    	List<?> nonPaymentList=this.shopOrderInfoDao.findBy(qoObject.addQuery("obj.status", Integer.parseInt("0"), "=")).getResult();//未支付订单
    	if(nonPaymentList!=null){
    		int count=0;
    		for (int i = 0; i < nonPaymentList.size(); i++) {
				ShopOrderInfo shopOrderInfo=(ShopOrderInfo)nonPaymentList.get(i);
				for (int j = 0; j < shopOrderInfo.getOrderdetails().size(); j++) {
					count+=shopOrderInfo.getOrderdetails().get(j).getNum();
				}
			}
    		form.addResult("nonPaymentCount", count);
    		form.addResult("nonPaymentList", nonPaymentList);
    	}
    	QueryObject qoObject2 = new QueryObject();
    	qoObject2.addQuery("obj.user.id", shopMember.getId(), "=");
    	qoObject2.setOrderBy("ceateDate");
    	qoObject2.setOrderType("desc");
    	List<?> paymentList=this.shopOrderInfoDao.findBy(qoObject2.addQuery("obj.status", Integer.parseInt("2"), "=")).getResult();//支付到收货订单
    	if(paymentList!=null){
    		int count=0;
    		for (int i = 0; i < paymentList.size(); i++) {
				ShopOrderInfo shopOrderInfo=(ShopOrderInfo)paymentList.get(i);
				for (int j = 0; j < shopOrderInfo.getOrderdetails().size(); j++) {
					count+=shopOrderInfo.getOrderdetails().get(j).getNum();
				}
				
			}
    		form.addResult("paymentCount", count);
    		form.addResult("paymentList", paymentList);
    	}
    	QueryObject qoObject3 = new QueryObject();
    	qoObject3.addQuery("obj.user.id", shopMember.getId(), "=");
    	qoObject3.setOrderBy("ceateDate");
    	qoObject3.setOrderType("desc");
    	List<?> completeList=this.shopOrderInfoDao.findBy(qoObject3.addQuery("obj.status", Integer.parseInt("3"), "=")).getResult();//支付到收货订单
    	if(completeList!=null){
    		int count=0;
    		for (int i = 0; i < completeList.size(); i++) {
				ShopOrderInfo shopOrderInfo=(ShopOrderInfo)completeList.get(i);
				for (int j = 0; j < shopOrderInfo.getOrderdetails().size(); j++) {
					count+=shopOrderInfo.getOrderdetails().get(j).getNum();
				}
			}
    		form.addResult("completeCount", count);
    		form.addResult("completeList", completeList);
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.user.id", shopMember.getId(), "=");
    	qo.setOrderBy("status,ceateDate");
    	qo.setOrderType("desc");
    	if(status>-1){
    		qo.addQuery("obj.status", status, "=");
    	}
    	
    	List<?> list= this.shopOrderInfoDao.findBy(qo).getResult();
    	
    	
		return list;
	}
	
	/**
	 * 更新积分信息
	 * @param IntegralHistory
	 */
	public boolean updateIntegral(IntegralHistory integral){
		boolean result = false;
		Long num = integral.getIntegral();
		if(integral.getUser() == null){
			return result;
		}
		integral.getUser().setTotalIntegral(num+integral.getUser().getTotalIntegral());		
		this.shopMemberDao.update(integral.getUser());
		this.integralHistoryService.addIntegralHistory(integral);
		result = true;
		return result;
	}
	
	@Override
	public ShopMember CheckMember(String name, String password) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.name", name, "=");
		qo.addQuery("obj.password", MD5.encode(password), "=");
		List<?> list=this.shopMemberDao.findBy(qo).getResult();
		if(list!=null){
			ShopMember sMember=(ShopMember)list.get(0);
			return sMember;
		}
		return null;
	}

	@Override
	public boolean UpdateMemberAfterPay(ShopMember member, Integer integral,Double totalConsumption) {
		if(member!=null){
			member.setAvailableIntegral(member.getAvailableIntegral()+integral);
			member.setTotalIntegral(member.getTotalIntegral()+integral);
			member.setConsumptionAmount(member.getConsumptionAmount()+totalConsumption);
			this.shopMemberDao.update(member);
		}
		return false;
	}

	@Override
	public ShopMember getShopMemberByName(String name,String value) {
		return this.shopMemberDao.getBy(name, value);
	}

	@Override
	public boolean UpdateMemberAfterPay(ShopOrderInfo orderInfo) {
		ShopMember s=orderInfo.getUser();
		Double orderTotalPrice = orderInfo.getGross_price();
		List<ShopOrderdetail> list=orderInfo.getOrderdetails();
		if(s.getDisType()==0){
			for (ShopOrderdetail sDetail : list) {
				ShopProduct sProduct=sDetail.getPro();
				s.setAvailableIntegral(s.getAvailableIntegral()+(sDetail.getNum()*sProduct.getSendPoints()));//可用积分
				s.setTotalIntegral(s.getTotalIntegral()+(sDetail.getNum()*sProduct.getSendPoints()));
				s.setConsumptionAmount(s.getConsumptionAmount()+orderTotalPrice);
				this.shopMemberDao.update(s);
			}
		}else{
			for (ShopOrderdetail sDetail : list) {
				ShopProduct sProduct=sDetail.getPro();
				s.setConsumptionAmount(s.getConsumptionAmount()+orderTotalPrice);
				this.shopMemberDao.update(s);
			}
		}
		
		if(orderInfo.getStatus()==0 && orderInfo.getBalancePay()!=null){
			ShopMember member =this.shopMemberDao.get(s.getId());
			if(member.getRemainderAmt()>0){
				double rAmt = member.getRemainderAmt();//账户余额
				double bPay=orderInfo.getBalancePay();//账户支付金额
				BigDecimal amt = new BigDecimal(Double.toString(rAmt));
				BigDecimal pay = new BigDecimal(Double.toString(bPay));
				member.setRemainderAmt(amt.subtract(pay).doubleValue());
				this.shopMemberDao.update(member);
				//余额变动记录
				//System.out.println("购物消费余额记录"+bPay);
				//System.out.println("购物消费剩余余额"+member.getRemainderAmt());
				//System.out.println("-------------------"+amt.subtract(pay).doubleValue());
				this.remainderAmtHistoryService.addRemainderAmtHistory(member, 1, -orderInfo.getBalancePay(), member.getNickname()+"购物消费余额");
			}
		}
		return true;
	}
	public List<ShopMember> querymethod(String sql) {
		List<ShopMember> list = (List<ShopMember>) this.shopMemberDao.executeNativeQuery(sql, null, 0, 0,ShopMember.class);
		return list;
	}
	
	public void confirmweidian(ShopDistributor distributor){
    	//查询当前微店所关联的会员
    	ShopMember member = distributor.getMember();
    	//查询被推荐人已经成为微店
    	if(member.getDistributor() == null){
    		String sql1 = "update Disco_Shop_Distributor t1 set t1.parent_id= '"+distributor.getId()+"'where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.parent_id is null and t1.id != '"+distributor.getId()+"')";
    		this.shopDistributorService.piupdate(sql1);
    	}else{
    		String sql1 = "update Disco_Shop_Distributor t1 set t1.parent_id= '"+distributor.getId()+"'where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%' and t1.parent_id = '"+member.getDistributor().getId()+"')";
    		this.shopDistributorService.piupdate(sql1);
    	}
    	
    	/**
    	 * 批量更新子distributor的depath
    	 */
    	String olddelPath=null;
    	if(member.getDistributor()!=null){
    		olddelPath=member.getDistributor().getDePath();
    		String newdepath=member.getDistributor().getDePath()+"@"+member.getCode();
    		distributor.setDePath(newdepath);
        	String sql = "update Disco_Shop_Distributor t1 set t1.dePath=replace(t1.dePath,'"+olddelPath+"','"+newdepath+"') where exists (select 1 from Disco_Shop_ShopMember m where m.id=t1.member_id and m.dePath like '"+member.getDePath()+"%') and t1.dePath like '"+olddelPath+"%'";
        	this.shopDistributorService.piupdate(sql);
    	}else{
    		String newdepath= "@"+member.getCode();
    		distributor.setDePath(newdepath);
    		String sql = "update `Disco_Shop_Distributor` `t1` set `t1`.`dePath`=concat('"+newdepath+"',`t1`.`dePath`) where exists (select 1 from `Disco_Shop_ShopMember` `m` where `m`.`id`=`t1`.`member_id` and `m`.`dePath` like '"+member.getDePath()+"%' and `m`.`dePath` <> '"+member.getDePath()+"')";
        	this.shopDistributorService.piupdate(sql);
    	}
    	distributor.setParent(member.getDistributor());
    	distributor.setJoinTime(new Date());
    	distributor.setStatus(1);
    	if(distributor.getMember().getDistributor() != null){
    		if(distributor.getMember().getDistributor().getExStatus() == 1){
    			distributor.setTopDistributor(distributor.getMember().getDistributor());
    		}else{
    			distributor.setTopDistributor(distributor.getMember().getDistributor().getTopDistributor());
    		}
    	}else{
    		distributor.setTopDistributor(null);
    	}
    	
    	/**
    	 * 批量跟新结束
    	 */

    	if(member.getDistributor() != null){
    		//我所属微店
    		
    		String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+distributor.getId()+" where m.dePath like '"+member.getDePath()+"%' and (m.distributor_id="+member.getDistributor().getId()+" or m.distributor_id is null)";
        	this.shopDistributorService.piupdate(memberSql);
    	}else{
    		//我没有所属微店
    		String memberSql = "update Disco_Shop_ShopMember m set m.distributor_id="+distributor.getId()+" where m.dePath like '"+member.getDePath()+"%' and  m.distributor_id is null and m.id <> "+member.getId();
        	this.shopDistributorService.piupdate(memberSql);
    	}
    	
    	//更新member表数据
    	member.setDisType(1);
    	member.setDistributor(null);
    	member.setMyDistributor(distributor);
    	updateShopMember(member.getId(),member);
	}

	@Override
	public Boolean register(String name, String password,String email,String mobileTel,String pmemberId) {
		if(!Verification.checkName(name)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "用户名长度只能在4-20位字符之间", ValidateType.Action);
			return false;
		}
		boolean b2=Verification.checkPassword(password);//验证用户名
		if(!b2){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "密码长度只能在6-20位字符之间", ValidateType.Action);
			return false;
		}
		if(StringUtils.hasLength(email)){
			if(!RegexUtils.checkEmail(email)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "邮箱格式不正确", ValidateType.Action);
				return false;
			}
		}
		if(StringUtils.hasLength(mobileTel)){
			if(!RegexUtils.checkMobile(mobileTel)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "手机格式不正确", ValidateType.Action);
				return false;
			}
		}
		//ShopMember sMember=this.shopMemberDao.getBy("name", name);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.name", name, "=");
		qo.setLimit(-1);
		Integer uCount=this.userService.getUserBy(qo).getRowCount();
		if(uCount>0){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此用户名已存在", ValidateType.Action);
			return false;
		}
		ShopMember m = new ShopMember();
		m.setName(name);
		m.setNickname(name);
		m.setPassword(MD5.encode(password));
		m.setCode(new Date().getTime()+"");
		if(StringUtils.hasText(pmemberId)){
			ShopMember member=this.shopMemberDao.get(Long.valueOf(pmemberId));
			if(member!=null){
				m.setPmember(member);
				m.setDePath(member.getDePath()+"@"+new Date().getTime());
				if(member.getDisType()==0){
					m.setDistributor(member.getDistributor());
				}else if(member.getDisType()==1 || member.getDisType()==2){
					m.setDistributor(member.getMyDistributor());
				}
			}else{
				m.setDePath("@"+new Date().getTime());
			}
		}else{
			m.setDePath("@"+new Date().getTime());
		}
		this.shopMemberDao.save(m);
		return true;
	}

	@Override
	public Boolean SendCodeByEmail(String email) {
		String name=(String)ActionContext.getContext().getSession().getAttribute("shopMemberName");
		if(name==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取用户名,请重新输入", ValidateType.Action);
			return false;
		}
		if(!RegexUtils.checkEmail(email)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "请输入有效的邮箱", ValidateType.Action);
			return false;
		}
		ShopMember member=this.shopMemberDao.getBy("email", email);
		if(!email.equals(member.getEmail())){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "此用户绑定的邮箱不是此邮箱", ValidateType.Action);
			return false;
		}
		if(member==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有获取此邮箱", ValidateType.Action);
			return false;
		}
		String emailTitle = "找回密码";
        String sixRand = getSixRand();
		// 发送邮件
        emailService.sendEmail(email, emailTitle, sixRand, false);
        ActionContext.getContext().getSession().setAttribute("sixRand", sixRand);
		return true;
	}
	
	/**
	 * 后台管理员增加或修改积分
	 * @param member
	 * @param integral
	 * @return
	 */
	@Override
	public boolean changeIntegralByAdmin(ShopMember member, Long integral){
		return this.changeIntegral(member, integral, 4, "后台管理员：");
	}
	
	/**
	 * 前台用户充值积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean rechargeIntegralByCustomer(ShopMember member, Long integral){		
		return this.changeIntegral(member, integral, 2, "前台用户充值：");
	}

	/**
	 * 添加用户积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean addIntegralByRegister(ShopMember member, Long integral){
		return this.changeIntegral(member, integral, 3, "用户注册赠送：");
	}
	
	@Override
	public boolean delIntegralByShopping(ShopMember member, Long integral) {
		if(integral > 0){
			integral = -integral;
		}
		return this.changeIntegral(member, integral, 1, "用户购物扣除：");
	}
	
	/**
	 * 修改用户积分
	 * @param member
	 * @param integral
	 * @param type
	 * @return
	 */
	private boolean changeIntegral(ShopMember member, Long integral, int type, String desc){
		boolean result = false;
		//更新用户的积分信息
		Long beforeIntegral = member.getAvailableIntegral();
		member.setAvailableIntegral(beforeIntegral+integral);
		if(integral > 0){
			member.setTotalIntegral(member.getTotalIntegral()+integral);	
		}
		if(this.updateShopMember(member.getId(), member)){
			//添加积分变更记录
			IntegralHistory history = new IntegralHistory();		
			history.setUser(member);
			history.setType(type);
			history.setIntegral(integral);
			history.setCreateDate(new Date());
			history.setDescription(desc+" 原积分"+beforeIntegral+"->变更后"+member.getAvailableIntegral());
			this.integralHistoryService.addIntegralHistory(history);
			result = true;
		}else{
			result = false;
		}
		return result;
	}

	@Override
	public Boolean returnIntegrall(ShopOrderInfo shopOrderInfo,String description) {
		ShopMember member=shopOrderInfo.getUser();
		List<ShopOrderdetail> list=shopOrderInfo.getOrderdetails();
		Long integral=0L;
		for (ShopOrderdetail shopOrderdetail : list) {
			ShopProduct sProduct=shopOrderdetail.getPro();
			integral+=(shopOrderdetail.getNum()*sProduct.getSendPoints());
		}
		member.setAvailableIntegral(member.getAvailableIntegral()-integral);//可用积分
		member.setTotalIntegral(member.getTotalIntegral()-integral);//总积分
		this.shopMemberDao.update(member);
		this.integralHistoryService.saveIntegralHistory(integral, member, description,6);
		return true;
	}

	@Override
	public Boolean returnmoney(ApplyRefund applyRefund,ShopOrderInfo shopOrderInfo,Double freight) {
		ShopMember member=shopOrderInfo.getUser();
		if(freight==null){
			freight=this.deliveryRuleService.getDeliveryCost(shopOrderInfo);
		}
		Double refundAmount=shopOrderInfo.getGross_price()-freight;
		//修改用户信息 （余额、消费总金额）
		member.setRemainderAmt(member.getRemainderAmt()+refundAmount);//消费余额
		member.setConsumptionAmount(member.getConsumptionAmount()-shopOrderInfo.getGross_price());//消费总金额
		this.shopMemberDao.update(member);
		applyRefund.setSums(refundAmount);
		
		//金额变动记录
		this.remainderAmtHistoryService.addRemainderAmtHistory(member, 3, refundAmount, member.getNickname()+"退款,余额增加记录");
		return true;
	}

	@Override
	public boolean MobileLogin(String mobileTel,String code) {
		if(!RegexUtils.checkMobile(mobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "手机号码输入不正确", ValidateType.Action);
			return false;
		}
		String rand=(String)ActionContext.getContext().getSession().getAttribute("mSixRand");
		if(StringUtils.hasText(code)){
			if(!rand.equals(code)){
				FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码输入不正确", ValidateType.Action);
				return false;
			}
		}else{
			FrameworkEngine.findValidatorManager().addCustomError("msg", "验证码不能为空", ValidateType.Action);
			return false;
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.mobileTel", mobileTel, "=");
		qo.setLimit(-1);
		List list=this.shopMemberDao.findBy(qo).getResult();
		if(list==null){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "没有查询到此用户", ValidateType.Action);
			return false;
		}
		if(list.size()>1){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "多人已绑定此手机，请联系商家", ValidateType.Action);
			return false;
		}
		ShopMember member=(ShopMember)list.get(0);
		UserContext.setMember(member);
		return true;
	}

	@Override
	public boolean updateShopMemberAmt(ShopMember member, Double amt) {
		BigDecimal rAmt01 = new BigDecimal(member.getRemainderAmt());
		BigDecimal amt01 = new BigDecimal(amt);
		member.setRemainderAmt(rAmt01.subtract(amt01).doubleValue());
		this.shopMemberDao.update(member);
		return true;
	}

	@Override
	public boolean sendCode(String mobileTel, String msg) {
		if(!RegexUtils.checkMobile(mobileTel)){
			FrameworkEngine.findValidatorManager().addCustomError("msg", "手机号码输入不正确", ValidateType.Action);
			return false;
		}
		String sixRand = getSixRand();
        String result = msgUtils.sendMessage(mobileTel,  msg+sixRand);
        if (!"Success".equals(result)) {
            FrameworkEngine.findValidatorManager().addCustomError("msg", "发送验证码失败", ValidateType.Action);
            return false;
        }
        ActionContext.getContext().getSession().setAttribute("mSixRand", sixRand);
		return true;
	}

	@Override
	public boolean updateMemberIntegral(ShopMember member, Integer integral) {
		member.setAvailableIntegral(member.getAvailableIntegral()-integral);
		this.shopMemberDao.update(member);
		return true;
	}

	@Override
	public boolean addMemberCookie(String name, String password) {
		String session_login_name = DiscoShopUtil.getCookie("SESSION_LOGIN_NAME");
		if(!StringUtils.hasText(session_login_name)){
			if(StringUtils.hasText(name) || StringUtils.hasText(password)){
				Cookie c= new Cookie("SESSION_LOGIN_NAME", name);
				c.setMaxAge(2592000);
				HttpServletResponse res = ActionContext.getContext().getResponse();
				res.addCookie(c);
				c = new Cookie("SESSION_LOGIN_VALUE", MD5.encode(password));
				c.setMaxAge(2592000);
				res.addCookie(c);
			}
		}
		return true;
	}
}

