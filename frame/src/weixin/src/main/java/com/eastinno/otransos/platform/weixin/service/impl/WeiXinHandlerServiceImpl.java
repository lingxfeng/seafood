package com.eastinno.otransos.platform.weixin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.cms.dao.INewsDirDAO;
import com.eastinno.otransos.cms.dao.INewsDocDAO;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.bean.Article;
import com.eastinno.otransos.platform.weixin.bean.Image;
import com.eastinno.otransos.platform.weixin.bean.Music;
import com.eastinno.otransos.platform.weixin.bean.ReqMessage;
import com.eastinno.otransos.platform.weixin.bean.RespImgMessage;
import com.eastinno.otransos.platform.weixin.bean.RespMessage;
import com.eastinno.otransos.platform.weixin.bean.RespMusicMessage;
import com.eastinno.otransos.platform.weixin.bean.RespNewsMessage;
import com.eastinno.otransos.platform.weixin.bean.RespTextMessage;
import com.eastinno.otransos.platform.weixin.bean.RespVideoMessage;
import com.eastinno.otransos.platform.weixin.bean.RespVoiceMessage;
import com.eastinno.otransos.platform.weixin.bean.Video;
import com.eastinno.otransos.platform.weixin.bean.Voice;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.AutoResponse;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.domain.Subscribe;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.service.IWeiXinHandlerService;
import com.eastinno.otransos.platform.weixin.service.IWxplatService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;
import com.eastinno.otransos.security.domain.Tenant;

@Service
public class WeiXinHandlerServiceImpl implements IWeiXinHandlerService {
	@Autowired
	protected IWxplatService wxplatService;
	@Autowired
	private INewsDirDAO newsDirDao;
	@Autowired
	private INewsDocDAO newsDao;

	@Override
	public String parseHandler(HttpServletRequest req) {
		String respMessageStr = null;
		RespMessage respMessage = null;
		try {
			// xml请求解析
			ReqMessage msg = WeixinUtils.parseXmlToReqM(req);
			String msgType = msg.getMsgType();
			if (msgType.equals(WeixinUtils.REQ_MESSAGE_TYPE_EVENT)) {
				// 【微信触发类型】事件推送
				String eventType = msg.getEvent();
				if (eventType.equals(WeixinUtils.EVENT_TYPE_SUBSCRIBE)) {
					// 关注
					respMessage = subscribeHandler(msg,req);
				} else if (eventType.equals(WeixinUtils.EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注
					respMessage = unsubscribeHandler(msg);
				} else if (eventType.equals(WeixinUtils.EVENT_TYPE_CLICK)) {
					// 点击事件
					respMessage = clickHandler(msg,req);
				} else if (eventType.equals("LOCATION")) {
					// 上报地理位置
					respMessage = locationHandleVoid(msg);
				}
			} else {
				saveMsg(msg);
				if (msgType.equals(WeixinUtils.REQ_MESSAGE_TYPE_TEXT)) {
					respMessage = textHandler(msg,req);
				} else if (msgType.equals(WeixinUtils.REQ_MESSAGE_TYPE_IMAGE)) {
					// 【微信触发类型】图片消息
					respMessage = imageHandler(msg);
				} else if (msgType
						.equals(WeixinUtils.REQ_MESSAGE_TYPE_LOCATION)) {
					// 【微信触发类型】地理位置消息
					respMessage = locationHandler(msg);
				} else if (msgType.equals(WeixinUtils.REQ_MESSAGE_TYPE_LINK)) {
					// 【微信触发类型】链接消息
					respMessage = linkHandler(msg);
				} else if (msgType.equals(WeixinUtils.REQ_MESSAGE_TYPE_VOICE)) {
					// 【微信触发类型】音频消息
					respMessage = voiceHandler(msg,req);
				} else if (msgType.equals("video")) {
					// 【微信触发类型】音频消息
					respMessage = voiceHandler(msg,req);
				}
			}
			/**
			 * 设置发送信息
			 */
			if (respMessage != null) {
				respMessage.setToUserName(msg.getFromUserName());
				respMessage.setFromUserName(msg.getToUserName());
				respMessage.setCreateTime(new Date().getTime());
				if (respMessage instanceof RespTextMessage) {
					respMessageStr = WeixinUtils
							.textMessageToXml((RespTextMessage) respMessage);
				} else if (respMessage instanceof RespNewsMessage) {
					respMessageStr = WeixinUtils
							.newsMessageToXml((RespNewsMessage) respMessage);
				} else if (respMessage instanceof RespImgMessage) {
					respMessageStr = WeixinUtils
							.imgMessageToXml((RespImgMessage) respMessage);
				} else if (respMessage instanceof RespVoiceMessage) {
					respMessageStr = WeixinUtils
							.voiceMessageToXml((RespVoiceMessage) respMessage);
				} else if (respMessage instanceof RespVideoMessage) {
					respMessageStr = WeixinUtils
							.videoMessageToXml((RespVideoMessage) respMessage);
				} else if (respMessage instanceof RespMusicMessage) {
					respMessageStr = WeixinUtils
							.musicMessageToXml((RespMusicMessage) respMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessageStr;
	}

	private RespMessage allType(ReqMessage msg) {
		RespTextMessage out = new RespTextMessage();
		out.setContent("你的消息已经收到！");
		return out;
	}

	@Override
	public RespMessage textHandler(ReqMessage msg,HttpServletRequest req) {
		RespTextMessage out = new RespTextMessage();
		Account account = getAccount(msg);
		String msgContent = msg.getContent();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.account", account, "=");
		qo.addQuery("obj.keyWord", msgContent, "=");
		qo.setPageSize(1);
		List<AutoResponse> autoResponses = this.wxplatService
				.getAutoResponseBy(qo).getResult();

		if (autoResponses != null && autoResponses.size() > 0) {
			AutoResponse autoResponse = autoResponses.get(0);
			Template template = autoResponse.getTemplate();
			return autoOrSub(template, account, out,req.getServletContext());
		} else {
			out.setContent("你的消息已经收到！");
		}
		return out;
	}

	@Override
	public RespMessage locationHandler(ReqMessage msg) {
		return allType(msg);
	}

	@Override
	public RespMessage imageHandler(ReqMessage msg) {
		return allType(msg);
	}

	@Override
	public RespMessage linkHandler(ReqMessage msg) {
		return allType(msg);
	}

	@Override
	public RespMessage voiceHandler(ReqMessage msg,HttpServletRequest req) {
		String keyStr = msg.getRecognition();
		msg.setContent(keyStr);
		return textHandler(msg,req);
	}

	/**
	 * 上报地理位置
	 * 
	 * @param msg
	 * @return
	 */
	private RespMessage locationHandleVoid(ReqMessage msg) {
		Follower follower = getFollower(msg);
		if (follower != null) {
			follower.setLongitude(msg.getLongitude());
			follower.setLatitude(msg.getLatitude());
			follower.setPrecisionstr(msg.getPrecision());
			this.wxplatService.updateFollower(follower.getId(), follower);
		}
		return null;
	}

	/**
	 * 关注事件处理
	 */
	@Override
	public RespMessage subscribeHandler(ReqMessage msg,HttpServletRequest req) {
		RespTextMessage out = new RespTextMessage();
		QueryObject qo = new QueryObject();
		Account account = getAccount(msg);
		if (account == null) {
			return out;
		}
		Tenant tenant = account.getTenant();
		Follower follower = getFollower(msg);
		if (follower == null) {
			/**
			 * 如果该粉丝没有关注过
			 */
			follower = new Follower();
			follower.setTenant(tenant);
			follower.setAccount(account);
			follower.setCode(new Date().getTime() + "");
			follower.setName(msg.getFromUserName());
			follower.setWeixinOpenId(msg.getFromUserName());
			follower.setRegisterTime(new Date());
			follower.setStatus(new Integer(1));
			RequestWxUtils.getUserInfo(follower, account,req.getServletContext());
			this.wxplatService.addFollower(follower);
			out.setContent("欢迎您关注！");
		} else {
			/**
			 * 如果该粉丝以前关注过
			 */
			follower.setStatus(new Integer(1));
			follower.setModifyDate(null);
			RequestWxUtils.getUserInfo(follower, account,req.getServletContext());
			this.wxplatService.updateFollower(follower.getId(), follower);
			out.setContent("欢迎您再次关注！");
		}
		qo = new QueryObject();
		qo.addQuery("obj.account", account, "=");
		qo.setPageSize(1);
		List<Subscribe> subscribes = this.wxplatService.getSubscribeBy(qo)
				.getResult();
		this.subscribeAfter(follower);
		if (subscribes != null && subscribes.size() > 0) {
			Template template = subscribes.get(0).getTemplate();
			return autoOrSub(template, account, out,req.getServletContext());
		}
		return out;
	}

	/**
	 * 取消关注事件处理
	 */
	@Override
	public RespMessage unsubscribeHandler(ReqMessage msg) {
		// 取消订阅
		Follower follower = getFollower(msg);
		if (follower != null) {
			follower.setStatus(new Integer(-2));
			follower.setModifyDate(new Date());
			this.wxplatService.updateFollower(follower.getId(), follower);
		}
		return null;
	}

	/**
	 * 点击菜单事件处理
	 */
	@Override
	public RespMessage clickHandler(ReqMessage msg,HttpServletRequest req) {
		RespTextMessage out = new RespTextMessage();
		String eventkey = msg.getEventKey();
		Account account = getAccount(msg);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.account", account, "=");
		qo.addQuery("obj.menuKey", eventkey, "=");
		qo.setPageSize(1);
		List<Menu> menus = this.wxplatService.getMenuBy(qo).getResult();
		if (menus != null && menus.size() > 0) {
			Menu menu = menus.get(0);
			Template temp = menu.getTemplate();
			if (temp != null) {
				return autoOrSub(temp, account, out,req.getServletContext());
			}
			NewsDir newsDir = menu.getNewsDir();
			if (newsDir == null) {
				qo = new QueryObject();
				qo.addQuery("obj.code", account.getTenant().getCode()+"_"+menu.getMenuKey(), "=");
				qo.addQuery("obj.tenant",account.getTenant(),"=");
				qo.setPageSize(1);
				List<NewsDir> newsDirs = this.newsDirDao.findBy(qo)
						.getResult();
				if (newsDirs != null && newsDirs.size() > 0) {
					newsDir = newsDirs.get(0);
				}
			}
			if (newsDir != null) {
				qo = new QueryObject();
				Integer orderByInt = menu.getNewsDocOrderBy();
				if (orderByInt!= null && orderByInt==1) {
					qo.addQuery("obj.elite", new Integer(1), "=");
				} else if (orderByInt!= null && orderByInt == 2) {
					qo.setOrderBy("readtime desc");
				}
				qo.addQuery("obj.dir", newsDir, "=");
				qo.addQuery("obj.tenant",account.getTenant(),"=");
				qo.setPageSize(10);
				List<NewsDoc> newsDocs = this.newsDao.findBy(qo)
						.getResult();
				if (newsDocs != null) {
					List<Article> articleList = new ArrayList<Article>();
					for (NewsDoc newsDoc : newsDocs) {
						Article article = new Article();
						article.setTitle(newsDoc.getTitle());
						article.setPicUrl(account.getTenant().getUrl()
								+ newsDoc.getIconPath());
						String url = account.getTenant().getUrl()+WeixinUtils.NEWDOCURL+"&accountId="+account.getId()+"&id="+newsDoc.getId();
						url = WeixinBaseUtils.getWxUrl(url, account);
						article.setUrl(url);
						article.setDescription(newsDoc.getDescription());
						articleList.add(article);
					}
					RespNewsMessage newsResp = new RespNewsMessage();
					newsResp.setArticleCount(newsDocs.size());
					newsResp.setArticles(articleList);
					return newsResp;
				}
			}
			return clickHandlerAfter(msg);
		}
		out.setContent("你的消息已经收到！");
		return out;
	}

	/**
	 * 分类回复
	 * 
	 * @param template
	 * @param account
	 * @param out
	 * @return
	 */
	private RespMessage autoOrSub(Template template, Account account,
			RespTextMessage out,ServletContext sc) {
		String msgType = template.getType();
		if ("1".equals(msgType)) {
			/**
			 * 普通文本消息
			 */
			out.setContent(template.getContent());
		} else if ("2".equals(msgType)) {
			/**
			 * 图文消息
			 */
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.tpl", template, "=");
			qo.setOrderBy("sequence");
			List<NewsItem> newsList = this.wxplatService.getNewsItemBy(qo)
					.getResult();
			if (newsList != null && newsList.size() > 0) {
				List<Article> articleList = new ArrayList<Article>();
				for (NewsItem news : newsList) {
					Article article = new Article();
					article.setTitle(news.getTitle());
					article.setPicUrl(account.getTenant().getUrl() + news.getImagePath());
					if (news.getUrl() != null && !"".equals(news.getUrl())) {
						article.setUrl(news.getUrl());
					}else{
						String url =account.getTenant().getUrl()+ WeixinUtils.NEWSITEMURL+"&accountId="+account.getId()+"&id="+news.getId();
						url = WeixinBaseUtils.getWxUrl(url, account);
						article.setUrl(url);
					}
					article.setDescription(news.getDescription());
					articleList.add(article);
				}
				RespNewsMessage newsResp = new RespNewsMessage();
				newsResp.setArticleCount(newsList.size());
				newsResp.setArticles(articleList);
				return newsResp;
			}
		} else if ("3".equals(msgType)) {
			/**
			 * 音乐消息
			 */
			RespMusicMessage respMusic = new RespMusicMessage();
			Music music = new Music();
			music.setDescription(template.getContent());
			music.setMusicUrl(template.getTenant().getUrl()
					+ template.getMediaPath());
			music.setThumbMediaId(getThMediaId(template,sc));
			music.setTitle(template.getTitle());
			music.setHQMusicUrl(template.getTenant().getUrl()
					+ template.getMediaPath());
			respMusic.setMusic(music);
			return respMusic;
		} else if ("4".equals(msgType)) {
			/**
			 * 图片消息
			 */
			RespImgMessage respImg = new RespImgMessage();
			Image image = new Image();
			image.setMediaId(getMediaId(template,sc));
			respImg.setImage(image);
			return respImg;
		} else if ("5".equals(msgType)) {
			/**
			 * 音频消息
			 */
			RespVoiceMessage respVoice = new RespVoiceMessage();
			Voice voice = new Voice();
			voice.setMediaId(getMediaId(template,sc));
			respVoice.setVoice(voice);
			return respVoice;
		} else if ("6".equals(msgType)) {
			/**
			 * 视频消息
			 */
			RespVideoMessage respVideo = new RespVideoMessage();
			Video video = new Video();
			video.setMediaId(getMediaId(template,sc));
			video.setDescription("视频消息");
			video.setTitle("视频消息");
			respVideo.setVideo(video);
			return respVideo;
		}
		return out;
	}

	/**
	 * 保存粉丝发送的信息
	 * 
	 * @param reqmsg
	 */
	private void saveMsg(ReqMessage msg, Follower follower, Account account) {
		String msgId = msg.getMsgId();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.msgId", msgId, "=");
		List<?> list = this.wxplatService.getFollowerMessageBy(qo)
				.getResult();
		if (list == null || list.size() == 0) {
			FollowerMessage followermsg = new FollowerMessage();
			followermsg.setFollower(follower);
			followermsg.setIsFollowerMsg(true);
			followermsg.setMsgType(msg.getMsgType());
			followermsg.setMsgId(msg.getMsgId());
			followermsg.setContent(msg.getContent());
			followermsg.setDescriptionStr(msg.getDescription());
			followermsg.setFormat(msg.getFormat());
			followermsg.setLabel(msg.getLabel());
			followermsg.setLocation_X(msg.getLocation_X());
			followermsg.setLocation_Y(msg.getLocation_Y());
			followermsg.setMediaId(msg.getMediaId());
			followermsg.setPicUrl(msg.getPicUrl());
			followermsg.setScale(msg.getScale());
			followermsg.setTenant(follower.getTenant());
			followermsg.setThumbMediaId(msg.getThumbMediaId());
			followermsg.setTitleStr(msg.getTitle());
			followermsg.setUrlStr(msg.getUrl());
			followermsg.setAccount(account);
			this.wxplatService.addFollowerMessage(followermsg);
			follower.setLastTimesend(new Date().getTime());
			this.wxplatService.updateFollower(follower.getId(), follower);
		}
	}

	private void saveMsg(ReqMessage msg) {
		Follower follower = getFollower(msg);
		Account account = getAccount(msg);
		saveMsg(msg, follower, account);
	}

	/**
	 * 获取粉丝
	 * 
	 * @param reqmsg
	 * @return
	 */
	private Follower getFollower(ReqMessage msg) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.weixinOpenId", msg.getFromUserName(), "=");
		List<Follower> followers = this.wxplatService.getFollowerBy(qo)
				.getResult();
		Follower follower = null;
		if (followers != null && followers.size() > 0) {
			return followers.get(0);
		}
		return null;
	}

	/**
	 * 获取公众号
	 * 
	 * @param msg
	 * @return
	 */
	private Account getAccount(ReqMessage msg) {
		String accountid = msg.getToUserName();
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.accountid", accountid, "=");
		qo.setPageSize(1);
		List<Account> accounts = this.wxplatService.getAccountBy(qo)
				.getResult();
		if (accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}
		return null;
	}

	/**
	 * 获取多媒体
	 * 
	 * @param template
	 */
	private String getMediaId(Template template,ServletContext sc) {
		if (template.getMediaId() != null
				|| "".equals(template.getMediaId())
				|| (new Date().getTime() - template.getMeidaCreateAt()) > 3
						* 24 * 60 * 60 * 1000) {
			String temType = template.getType();
			String mediaUploadType = "";
			if ("4".equals(temType)) {
				mediaUploadType = "image";
			} else if ("5".equals(temType)) {
				mediaUploadType = "voice";
			} else if ("6".equals(temType)) {
				mediaUploadType = "video";
			}
			String mediaPath = template.getMediaPath();
			Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(
					template.getAccount(), mediaUploadType, template
							.getTenant().getUrl() + mediaPath,sc);
			template.setMediaId(mapreturn.get("media_id") + "");
			template.setMeidaCreateAt(new Date().getTime());
			this.wxplatService.updateTemplate(template.getId(), template);
		}
		return template.getMediaId();
	}

	/**
	 * 获取模板缩略图
	 * 
	 * @param template
	 * @return
	 */
	private String getThMediaId(Template template,ServletContext sc) {
		if (template.getThumbMediaId() == null
				|| "".equals(template.getThumbMediaId())
				|| (new Date().getTime() - template.getThumbMediaCreateAt()) > 3
						* 24 * 60 * 60 * 1000) {
			String mediaPath = template.getThumbMediaPath();
			Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(
					template.getAccount(), "image", template.getTenant()
							.getUrl() + mediaPath,sc);
			template.setThumbMediaId(mapreturn.get("media_id") + "");
			template.setThumbMediaCreateAt(new Date().getTime());
			this.wxplatService.updateTemplate(template.getId(), template);
		}
		return template.getThumbMediaId();
	}

	@Override
	public void subscribeAfter(Follower follower) {
	}

	@Override
	public RespMessage clickHandlerAfter(ReqMessage msg) {
		return allType(msg);
	}

	@Override
	public int getShareSto() {
		return WeixinBaseUtils.shareSto;
	}

	@Override
	public int getSignatureSto() {
		return WeixinBaseUtils.signatureSto;
	}

	@Override
	public int getFirstfollowSto() {
		return WeixinBaseUtils.firstfollowSto;
	}

	@Override
	public int getCjjSto() {
		return WeixinBaseUtils.cjjSto;
	}
}
