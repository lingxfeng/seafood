package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.trade.domain.ApplyRefund;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShopMemberRating;
/**
 * ShopMemberService
 * @author ksmwly@gmail.com
 */
public interface IShopMemberService {
	/**
	 * 保存一个ShopMember，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopMember(ShopMember domain);
	
	/**
	 * 根据一个ID得到ShopMember
	 * 
	 * @param id
	 * @return
	 */
	ShopMember getShopMember(Long id);
	
	/**
	 * 删除一个ShopMember
	 * @param id
	 * @return
	 */
	boolean delShopMember(Long id);
	
	/**
	 * 批量删除ShopMember
	 * @param ids
	 * @return
	 */
	boolean batchDelShopMembers(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopMember
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopMemberBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopMember
	  * @param id 需要更新的ShopMember的id
	  * @param dir 需要更新的ShopMember
	  */
	boolean updateShopMember(Long id,ShopMember entity);
	
	/**
	  * 验证用户注册信息
	  * @param ShopMember
	  */
	boolean MemberRegisterVerification(ShopMember shopMember,String code);
	
	/**
	  * 用户登录
	  * @param name 用户名
	  * @param password 密码
	  * @param code 验证码
	  */
	boolean MemberLogin(String name,String password,String code);
	
	/**
	  * 验证发送验证码
	  * @param name 用户名
	  * @param emailOrMobileTel 手机获证邮箱
	  * @param code 验证码
	  */
	boolean ValidateAndSendCode(String name,String emailOrMobileTel,String code);
	
	/**
	  * 找回密码
	  * @param name 用户名
	  * @param emailOrMobileTel 手机获证邮箱
	  * @param code 验证码
	  */
	boolean getPwd(String name,String emailOrMobileTel,String code);
	
	/**
	  * 找回密码
	  * @param name 用户名
	  * @param emailOrMobileTel 手机获证邮箱
	  * @param code 验证码
	  */
	boolean judgeCode(ShopMember shopMember,String code);
	
	/**
	  * 修改密码(找回密码)
	  * @param password 密码
	  * @param password2 确认密码
	  * @param shopMember
	  */
	boolean updatePwd(String password,String password2,ShopMember shopMember);
	
	/**
	  * 查询个人信息
	  * @param name 用户名
	  * @param emailOrMobileTel 手机获证邮箱
	  * @param code 验证码
	  */
	ShopMember getUserInfo(ShopMember shopMember);
	
	/**
	  * 修改密码（个人资料）
	  * @param password 密码
	  * @param password2 确认密码
	  * @param shopMember
	  */
	boolean updatePassword(String oldPwd,String newPwd,String confirmPwd,ShopMember shopMember);
	
	/**
	  * 修改邮箱（个人资料）
	  * @param password 密码
	  * @param shopMember
	  */
	boolean updateEmail(String password,String email,ShopMember shopMember);
	
	/**
	  * 修改手机（个人资料）
	  * @param mobileTel 手机
	  * @param code 验证码
	  * @param shopMember
	  */
	boolean updateMobileTel(String mobileTel , String code , ShopMember shopMember);
	
	/**
	  * 修改手机-发送验证码（个人资料）
	  * @param password 密码
	  * @param shopMember
	  */
	boolean sendCode(String mobileTel);
	
	/**
	  * 得到订单
	  * @param form
	  */
	List<?> getOrderInfo(WebForm form , ShopMember shopMember);

	/**
	 * 获取后台管理页面所需的打印信息
	 * @param qo
	 * @return
	 */
	IPageList getShopMemberListForDisplayByQO(QueryObject qo);
	
	/**
	 * 更新用户的积分信息
	 * @param integral
	 * @return
	 */
	boolean updateIntegral(IntegralHistory integral);
	
	/**
	  * 微信绑定 -- 查找用户
	  * @param name 用户名
	  * @param password 密码
	  */
	ShopMember CheckMember(String name,String password);
	
	/**
	  * 支付完成-修改用户账户积分-消费金额
	  * @param member 用户
	  * @param integral 消费完成获取的积分
	  * @param totalConsumption 消费总额
	  */
	boolean UpdateMemberAfterPay(ShopMember member,Integer integral,Double totalConsumption);
	
	/**
	  * 获取用户
	  */
	ShopMember getShopMemberByName(String name,String value);
	
	/**
	  * 支付完成-修改用户账户积分-消费金额
	  * @param name 用户名
	  * @param integral 消费完成获取的积分
	  * @param totalConsumption 消费总额
	  */
	boolean UpdateMemberAfterPay(ShopOrderInfo orderInfo);
	List<ShopMember> querymethod(String sql);
	/**
	 * 
	 */
	void confirmweidian(ShopDistributor distributor);

	
	/**
	  * pc端用户注册
	  */
	Boolean register(String name, String password,String email,String mobileTel,String pmemberId);
	
	/**
	  * 发送验证码
	  */
	Boolean SendCodeByEmail(String email);
	
	/**
	 * 后台管理员增加或修改积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean changeIntegralByAdmin(ShopMember member, Long integral);
	
	/**
	 * 前台用户增加或修改积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean rechargeIntegralByCustomer(ShopMember member, Long integral);

	/**
	 * 添加用户积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean addIntegralByRegister(ShopMember member, Long integral);

	/**
	 * 购物扣除用户积分
	 * @param member
	 * @param integral
	 * @return
	 */
	public boolean delIntegralByShopping(ShopMember member, Long integral);
	
	/**
	  * 退款退货--退回积分
	  */
	Boolean returnIntegrall(ShopOrderInfo shopOrderInfo,String description);
	
	/**
	  * 退款退货--退回金额
	  */
	Boolean returnmoney(ApplyRefund applyRefund,ShopOrderInfo shopOrderInfo,Double freight);
	
	/**
	  * 手机登录
	  * @param mobileTel 手机号码
	  * @param code 手机号码
	  */
	boolean MobileLogin(String mobileTel,String code);
	
	/**
	 * 可用金额变动
	 *  
	 * @param member 用户
	 * @param amt 变动金额
	 * @return
	 */
	boolean updateShopMemberAmt(ShopMember member,Double amt);
	
	/**
	  * 发送验证码
	  * @param mobileTel 手机
	  * @param msg 发送内容
	  */
	boolean sendCode(String mobileTel,String msg);
	
	/**
	  * 支付完成-修改用户账户积分-消费金额
	  * @param member 用户
	  * @param integral 积分
	  */
	boolean updateMemberIntegral(ShopMember member,Integer integral);
	
	/**
	  * 添加 cookie
	  * @param name 用户
	  * @param password 积分
	  */
	boolean addMemberCookie(String name,String password);
	
}

