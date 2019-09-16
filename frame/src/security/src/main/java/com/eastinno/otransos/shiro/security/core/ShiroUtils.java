package com.eastinno.otransos.shiro.security.core;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.ClientIPUtil;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.IResourceService;
import com.eastinno.otransos.security.service.ISystemConfigService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.web.ActionContext;
import com.google.inject.internal.Lists;

/**
 * 权限工具类
 * 
 * @author nsz
 */
public class ShiroUtils {
	public static final String ListSec = "list";// 访问权限名称
	public static final String ROOT = "root";// 超级管理员
	public static final String ADMIN = "admin";// 超级管理员
	public static final String PASSWORD = "123456";// 默认密码
	public static final String PASSWORDTYPE = "md5";// 加密方式
	public static final int PASSWORDSIZE = 1024;// 加密算法次数

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	public static ShiroUser getShiroUser() {
		Subject subject = SecurityUtils.getSubject();
		ShiroUser user = (ShiroUser) subject.getPrincipal();
		return user;
	}

	public static boolean hasMenu(SystemMenu menu) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.hasRole(ShiroUtils.ROOT)) {
			return true;
		}
		String tenantCode = ShiroUtils.getTenant().getCode();
		return subject.isPermitted(tenantCode + ":menu:" + menu.getSn());
	}

	/**
	 * 验证资源
	 * 
	 * @param permissionStr
	 * @return
	 */
	public static boolean hasPermission(String permissionStr) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.hasRole(ShiroUtils.ROOT)
				|| subject.hasRole(ShiroUtils.ADMIN)) {
			return true;
		}
		return subject.isPermitted(permissionStr);
	}

	/**
	 * 获取加密凭证
	 * 
	 * @return
	 */
	public static String getSalt() {
		return new SecureRandomNumberGenerator().nextBytes().toHex();
	}

	/**
	 * 加密密码
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String getPassWord(String password, String salt) {
		SimpleHash hash = new SimpleHash(PASSWORDTYPE, password, salt,
				PASSWORDSIZE);
		return hash.toHex();
	}

	/**
	 * 获取当前登录后台的用户
	 * 
	 * @return
	 */
	public static User getUser() {
		ServletContext sc = ActionContext.getContext().getServletContext();
		return getUser(sc);
	}

	public static User getUser(HttpServletRequest req) {
		ServletContext sc = req.getServletContext();
		return getUser(sc);
	}

	public static User getUser(ServletContext sc) {
		ShiroUser shiroUser = getShiroUser();
		if (shiroUser != null) {
			ApplicationContext ac2 = WebApplicationContextUtils
					.getWebApplicationContext(sc);
			IUserService userService = ac2.getBean(IUserService.class);
			if (userService != null) {
				return userService.getUser(shiroUser.getId());
			}
		}
		return null;
	}

	/**
	 * 获取登录用户所属的租户
	 * 
	 * @return
	 */
	public static Tenant getTenant(HttpServletRequest req) {
		User user = getUser(req);
		return user.getTenant();
	}

	public static Tenant getTenant() {
		User user = getUser();
		return user.getTenant();
	}

	public static boolean login(String userName, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(userName,
				password);
		token.setRememberMe(true);
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(token);
		// try {
		// currentUser.login(token);
		// } catch (UnknownAccountException uae) {
		// System.out.println("账户不存在!");
		// } catch (IncorrectCredentialsException ice) {
		// System.out.println("密码不正确!");
		// } catch (LockedAccountException lae) {
		// System.out.println("账户被禁了!");
		// } catch (AuthenticationException ae) {
		// System.out.println("认证错误!");
		// }
		return true;
	}

	/**
	 * 根据属性名获取配置中属性值
	 * 
	 * @param key
	 * @return
	 */
	public static String getConfigStr(String key) {
		ServletContext sc = ActionContext.getContext().getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		ISystemConfigService systemConfigService = ac2
				.getBean(ISystemConfigService.class);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.tenant", TenantContext.getTenant(), "=");
		List<SystemConfig> configs = systemConfigService.getSystemConfigBy(qo)
				.getResult();
		if (configs != null && configs.size() > 0) {
			Map<String, Object> map = (Map<String, Object>) configs.get(0);
			Object obj = map.get(key);
			if (obj != null) {
				return obj.toString();
			}
		}
		return "";
	}

	/**
	 * 获取所有配置值
	 * 
	 * @return
	 */
	public static Map<String, Object> getConfigStr() {
		ServletContext sc = ActionContext.getContext().getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		ISystemConfigService systemConfigService = ac2
				.getBean(ISystemConfigService.class);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.tenant", TenantContext.getTenant(), "=");
		List<SystemConfig> configs = systemConfigService.getSystemConfigBy(qo)
				.getResult();
		if (configs != null && configs.size() > 0) {
			Map<String, Object> map = (Map<String, Object>) configs.get(0);
			return map;
		}
		return null;
	}

	/**
	 * 获取日志
	 * 
	 * @param req
	 * @return
	 */
	public static SystemLog getSystemLog(HttpServletRequest req) {
		User user = getUser(req);
		String ip = ClientIPUtil.getIP(req);
		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();
		int i = uri.indexOf(contextPath);
		if (i > -1) {
			uri = uri.substring(i + contextPath.length());
		}
		if ("".equals(uri) || "/".equals(uri)) {
			return null;
		}
		String actionName = uri.substring(1);
		String paramscmd = CommUtil.null2String(req.getParameter("cmd"));
		SystemLog log = new SystemLog();
		log.setAction(actionName);
		log.setActionName(upIndexC(actionName.replaceAll(".java", "")) + "Action");
		if (!"".equals(paramscmd)) {
			log.setCmd(paramscmd);
			log.setCmdName("do" + upIndexC(paramscmd));
		}else{
			log.setCmd("init");
			log.setCmdName("doInit");
		}
		log.setIp(ip);
		log.setUser(user);
		return log;
	}

	public static String upIndexC(String fldName) {
		String first = fldName.substring(0, 1).toUpperCase();
		String rest = fldName.substring(1, fldName.length());
		return new StringBuffer(first).append(rest).toString();
	}

	public static ShiroUser getShiroUserFromUser(User user) {
		StringBuffer roleIdssb = new StringBuffer("0");
		StringBuffer permissionIdssb = new StringBuffer("0");
		StringBuffer menuIdssb = new StringBuffer("0");
		StringBuffer resourceIdssb = new StringBuffer("0");

		List<String> roleNames = Lists.newArrayList();
		List<String> menuPerStrs = Lists.newArrayList();// 菜单权限字符串
		List<String> reslurcePerStrs = Lists.newArrayList();// 资源权限字符串

		List<Role> roles = null;
		Tenant t = user.getTenant();
		if (user.getIsTenantAdmin()) {
			roles = t.getRoles();
			roleNames.add(t.getCode() + "_ROOT");
			reslurcePerStrs.addAll(getAllCPStr(t));
		} else {
			roles = user.getRoles();
		}

		for (Role role : roles) {
			roleIdssb.append("," + role.getId());
			roleNames.add(role.getName());
			List<Permission> permissions = role.getPermissions();
			for (Permission permission : permissions) {
				permissionIdssb.append("," + permission.getId());
				/**
				 * 加载菜单
				 */
				List<SystemMenu> menus = permission.getMenus();
				for (SystemMenu sm : menus) {
					menuIdssb.append("," + sm.getId());
					menuPerStrs.add(t.getCode() + ":menu:" + sm.getSn());
				}
				/**
				 * 加载资源
				 */
				List<Resource> resources = permission.getResources();
				for (Resource r : resources) {
					resourceIdssb.append("," + r.getId());
					String msg = r.getResStr();
					String[] pstr = msg.split(":");
					String startstr = pstr[0];
					String endstr = pstr[1].replace("do", "").toUpperCase()
							.replace("ALL", "*");
					startstr = startstr
							.substring(startstr.lastIndexOf(".") + 1).replace(
									"Action", ".java");
					String perstr = startstr + ":" + endstr;
					reslurcePerStrs.add(t.getCode() + ":" + perstr);
				}

			}
		}
		ShiroUser sUser = new ShiroUser(user.getId(), user.getName(),
				user.getTrueName(), roleIdssb.toString(),
				permissionIdssb.toString(), menuIdssb.toString(),
				resourceIdssb.toString(), roleNames, menuPerStrs,
				reslurcePerStrs);
		return sUser;
	}

	public static List<String> getAllCPStr(Tenant t) {
		List<String> list = Lists.newArrayList();
		ServletContext sc = ActionContext.getContext().getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		ITenantService tenantService = ac2.getBean(ITenantService.class);
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.depthPath like '" + t.getDepthPath() + "%'");
		List<Tenant> ts = tenantService.getTenantBy(qo).getResult();
		if (ts != null && ts.size() > 0) {
			for (Tenant to : ts) {
				list.add(to.getCode());
			}
		}
		return list;
	}

	/**
	 * 获取完整路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String url = "";
		url = request.getContextPath();
		url = url + request.getServletPath();
		java.util.Enumeration names = request.getParameterNames();
		if (!"".equals(request.getQueryString())
				|| request.getQueryString() != null) {
			url = url + "?" + request.getQueryString();
		}
		return url;
	}

	public static boolean hasResource(SystemLog log) {
		HttpServletRequest req = ActionContext.getContext().getRequest();
		return hasResource(req,log);
	}

	public static boolean hasResource(HttpServletRequest request, SystemLog log) {
		ServletContext sc = request.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		IResourceService resourceService = ac.getBean(IResourceService.class);
		String actionName = log.getActionName()+":"+log.getCmdName();;
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.resStr","%"+actionName,"like");
		List<?> list = resourceService.getResourceBy(qo).getResult();
		return list!=null && list.size()>0;
	}
	public static void main(String[] args) {
		ShiroUtils su = new ShiroUtils();
		String pass = su.getPassWord("2c3a1338403d6b9c7450cee7a511637d","86fd6fddd0a7c0bb310fa7f5761bba46");
		System.out.println(pass);
	}
}
