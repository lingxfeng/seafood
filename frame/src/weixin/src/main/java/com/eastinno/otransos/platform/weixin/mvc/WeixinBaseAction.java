package com.eastinno.otransos.platform.weixin.mvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.domain.PrizeHistory;
import com.eastinno.otransos.platform.weixin.domain.PrizePro;
import com.eastinno.otransos.platform.weixin.domain.ShareHistory;
import com.eastinno.otransos.platform.weixin.domain.WxSignature;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.platform.weixin.service.INewsItemService;
import com.eastinno.otransos.platform.weixin.service.IPrizeHistoryService;
import com.eastinno.otransos.platform.weixin.service.IPrizeProService;
import com.eastinno.otransos.platform.weixin.service.IShareHistoryService;
import com.eastinno.otransos.platform.weixin.service.IWeiXinHandlerService;
import com.eastinno.otransos.platform.weixin.service.IWxSignatureService;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * 微信业务公共基类action
 * 
 * @author nsz
 */
public class WeixinBaseAction extends AbstractPageCmdAction {
	@Inject
	protected IAccountService accountService;
	@Inject
	protected IFollowerService followerService;
	@Inject
	protected IPrizeHistoryService prizeHistoryService;
	@Inject
	protected IShareHistoryService shareHistoryService;
	@Inject
	protected IPrizeProService prizeProService;
	@Inject
	protected INewsDocService newsDocService;
	@Inject
	protected INewsItemService newsItemsService;
	@Inject
	private IWxSignatureService wxSignatureService;

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setFollowerService(IFollowerService followerService) {
		this.followerService = followerService;
	}

	public void setPrizeHistoryService(IPrizeHistoryService prizeHistoryService) {
		this.prizeHistoryService = prizeHistoryService;
	}

	public void setShareHistoryService(IShareHistoryService shareHistoryService) {
		this.shareHistoryService = shareHistoryService;
	}

	public void setPrizeProService(IPrizeProService prizeProService) {
		this.prizeProService = prizeProService;
	}

	public void setNewsDocService(INewsDocService newsDocService) {
		this.newsDocService = newsDocService;
	}

	public void setNewsItemsService(INewsItemService newsItemsService) {
		this.newsItemsService = newsItemsService;
	}

	public void setWxSignatureService(IWxSignatureService wxSignatureService) {
		this.wxSignatureService = wxSignatureService;
	}

	/**
	 * 通过code获取follower
	 * 
	 * @param form
	 * @return
	 */
	protected Follower getFollowerByCode(WebForm form) {
		Account account = getAccount(form);
		String code = CommUtil.null2String(form.get("code"));
		return this.followerService.getFollowerByCode(account, code);
	}

	/**
	 * 获取当前公众号
	 * 
	 * @param form
	 * @return
	 */
	protected Account getAccount(WebForm form) {
		Account account = null;
		Object accountObj = ActionContext.getContext().getSession().getAttribute("wx_account");
		if (accountObj != null) {
			account = (Account) accountObj;
		} else {
			String accountId = CommUtil.null2String(form.get("accountId"));
			if (!"".equals(accountId)) {
				account = this.accountService.getAccount(Long.parseLong(accountId));
				account.setTenant(account.getTenant());
				ActionContext.getContext().getSession().setAttribute("wx_account", account);
			}
		}
		return account;
	}

	protected IWeiXinHandlerService getHandler(Account account) {
		ApplicationContext ac;
		ServletContext sc = ActionContext.getContext().getServletContext();
		ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		String handlerName = account.getHandlerName();
		IWeiXinHandlerService weiXinHandlerService;
		if (handlerName != null && !"".equals(handlerName)) {
			weiXinHandlerService = (IWeiXinHandlerService) ac.getBean(handlerName);
		} else {
			weiXinHandlerService = (IWeiXinHandlerService) ac.getBean("weiXinHandlerServiceImpl");
		}
		return weiXinHandlerService;
	}

	@Override
	public Object doBefore(WebForm form, Module module) {
		Account account = getAccount(form);
		form.addResult("account", account);
		form.addResult("domain", WeixinBaseUtils.getDomain());
		return super.doBefore(form, module);
	}

	/**
	 * 显示图文消息
	 * 
	 * @param form
	 * @return
	 */
	public Page doShowNewsItem(WebForm form) {
		getFollowerByCode(form);
		Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
		NewsItem newsItem = this.newsItemsService.getNewsItem(id);
		form.addResult("defaultImg", newsItem.getImagePath());
		form.addResult("title", newsItem.getTitle());
		WeixinBaseUtils.setWeixinjs(form, getAccount(form));
		form.addResult("newsItem", newsItem);
		return new Page("/" + newsItem.getTenant().getCode() + "/" + newsItem.getTpl().getAccount().getCode()
				+ "/news/defaultItem.html");
	}

	/**
	 * 显示文章详情
	 * 
	 * @param form
	 * @return
	 */
	public Page doShowNewsDoc(WebForm form) {
		getFollowerByCode(form);
		Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
		NewsDoc newsDoc = this.newsDocService.getNewsDoc(id);
		form.addResult("defaultImg", newsDoc.getIconPath());
		form.addResult("title", newsDoc.getTitle());
		WeixinBaseUtils.setWeixinjs(form, getAccount(form));
		form.addResult("newsDoc", newsDoc);
		return new Page("/" + newsDoc.getTenant().getCode() + "/news/defaultdoc.html");
	}

	/**
	 * 积分中心
	 * 
	 * @param form
	 * @return
	 */
	public Page doJfzx(WebForm form) {
		Follower follower = getFollowerByCode(form);
		form.addResult("scorepoints", follower.getScorepoints());
		return new Page(
				"/" + follower.getTenant().getCode() + "/" + follower.getAccount().getCode() + "/news/points.html");
	}

	/**
	 * 分享加分
	 * 
	 * @param form
	 * @return
	 */
	public Page doAddScorepoints(WebForm form) {
		Follower follower = this.followerService.getFollowerBySession();
		Account account = getAccount(form);
		String url = CommUtil.null2String(form.get("surl"));
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.shareUrl", url, "=");
		qo.addQuery("obj.account", account, "=");
		List<?> list = this.shareHistoryService.getShareHistoryBy(qo).getResult();
		if (list == null || list.size() == 0) {
			ShareHistory shareHistory = new ShareHistory();
			shareHistory.setShareUrl(url);
			shareHistory.setFollower(follower);
			shareHistory.setAccount(account);
			shareHistory.setTenant(account.getTenant());
			this.shareHistoryService.addShareHistory(shareHistory);
			follower.setScorepoints(follower.getScorepoints() + getHandler(account).getShareSto());
			this.followerService.updateFollower(follower.getId(), follower);
		}
		return Page.JSONPage;
	}

	/**
	 * 积分抽奖
	 * 
	 * @param form
	 * @return
	 */
	public Page doJfcj(WebForm form) {
		Follower follower = getFollowerByCode(form);
		QueryObject qo = new QueryObject();
		qo.setOrderBy("sequence");
		qo.addQuery("obj.account", follower.getAccount(), "=");
		List<PrizePro> list = this.prizeProService.getPrizeProBy(qo).getResult();
		List<Map<String, String>> listPri = new ArrayList<Map<String, String>>();
		if (list != null && list.size() > 0) {
			for (PrizePro pri : list) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", pri.getId() + "");
				map.put("prize", pri.getName());
				map.put("v", pri.getProbability() + "");
				listPri.add(map);
			}
		}
		String jsonString = JSONObject.toJSON(listPri).toString();
		form.addResult("prizePros", jsonString);
		form.addResult("scorepoints", follower.getScorepoints());
		return new Page(
				"/" + follower.getTenant().getCode() + "/" + follower.getAccount().getCode() + "/news/jfcj.html");
	}

	/**
	 * 签到
	 * 
	 * @param form
	 * @return
	 */
	public Page doCheckin(WebForm form) {
		Map<String, String> map = new HashMap<String, String>();
		Follower follower = getFollowerBySession();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.follower", follower, "=");
		qo.addQuery("obj.createDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString(), "=");
		qo.setOrderBy("createDate desc");
		qo.setPageSize(1);
		List<?> list = this.wxSignatureService.getWxSignatureBy(qo).getResult();
		if (list != null && list.size() > 0) {
			map.put("status", "1");
		} else {
			WxSignature wxSignature = new WxSignature();
			wxSignature.setAccount(follower.getAccount());
			wxSignature.setFollower(follower);
			wxSignature.setTenant(follower.getTenant());
			this.wxSignatureService.addWxSignature(wxSignature);
			follower.setScorepoints(follower.getScorepoints() + getHandler(getAccount(form)).getSignatureSto());
			map.put("status", "0");
		}
		map.put("scorepoints", follower.getScorepoints() + "");
		form.jsonResult(map);
		return Page.JSONPage;
	}

	/**
	 * 抽奖扣积分
	 * 
	 * @param form
	 * @return
	 */
	public Page doCjkjf(WebForm form) {
		Map<String, String> rm = new HashMap<String, String>();
		Follower follower = getFollowerBySession();
		Tenant tenant = TenantContext.getTenant();
		if (follower != null) {
			String id = CommUtil.null2String(form.get("id"));
			if (!"".equals(id) && !"0".equals(id)) {
				PrizePro prizePro = this.prizeProService.getPrizePro(Long.parseLong(id));
				PrizeHistory prih = new PrizeHistory();
				prih.setAccount(follower.getAccount());
				prih.setFollower(follower);
				prih.setPrizePro(prizePro);
				prih.setTenant(tenant);
				this.prizeHistoryService.addPrizeHistory(prih);
			}
			follower.setScorepoints(follower.getScorepoints() - getHandler(getAccount(form)).getCjjSto());
			this.followerService.updateFollower(follower.getId(), follower);
			rm.put("status", "success");
		} else {
			rm.put("status", "error");
		}
		form.jsonResult(rm);
		return Page.JSPage;
	}

	protected Follower getFollowerBySession() {
		return this.followerService.getFollowerBySession();
	}

	/**
	 * 获取粉丝
	 * 
	 * @param form
	 * @return
	 */
	protected Follower getFollower(WebForm form) {
		Follower follower = getFollowerBySession();
		if (follower == null) {
			follower = getFollowerByCode(form);
		}
		return follower;
	}

	public IFollowerService getFollowerService() {
		return followerService;
	}

}
