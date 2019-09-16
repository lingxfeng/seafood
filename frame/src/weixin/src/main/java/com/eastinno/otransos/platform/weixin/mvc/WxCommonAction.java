package com.eastinno.otransos.platform.weixin.mvc;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.http.util.HttpHelper;
import com.eastinno.otransos.platform.weixin.bean.AccessToken;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerMessageService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.platform.weixin.service.INewsItemService;
import com.eastinno.otransos.platform.weixin.service.ITemplateService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Action
public class WxCommonAction extends AbstractPageCmdAction {
    @Inject
    private IFollowerService followerService;
    @Inject
    private ITemplateService templateService;
    @Inject
    private IAccountService accountService;
    @Inject
    private INewsItemService newsItemService;
    @Inject
    private IFollowerMessageService followerMessageService;

    public void setFollowerService(IFollowerService followerService) {
        this.followerService = followerService;
    }

    public void setTemplateService(ITemplateService templateService) {
        this.templateService = templateService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    public void setNewsItemService(INewsItemService newsItemService) {
        this.newsItemService = newsItemService;
    }

    public void setFollowerMessageService(IFollowerMessageService followerMessageService) {
        this.followerMessageService = followerMessageService;
    }

    public Page doNewscontent(WebForm form) {
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public void doLoginQQ(WebForm form) {
        String userName = CommUtil.null2String(form.get("userName"));
        String password = CommUtil.null2String(form.get("password"));
        ShiroUtils.login(userName, password);
    }

    public void doGetMyMenu(WebForm form) {
        String appid = "wx941e30655db822a6";
        String appsecret = "6d74284dbd880742d2bdee78e9d32dbc";
        AccessToken accessToken = RequestWxUtils.getAccessToken(appid, appsecret);
        String token = accessToken.getToken();
        try {
            String contextreturn = HttpHelper.get("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + token).getContent();
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println(contextreturn);
            System.out.println("++++++++++++++++++++++++++++++++++++++");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错了吗");
        }

    }

    /**
     * 消息群发
     * 
     * @param form
     * @return
     */
    public Page doSendMessage(WebForm form) {
        String templateId = CommUtil.null2String(form.get("templateId"));
        String accountId = CommUtil.null2String(form.get("accountId"));
        String type = CommUtil.null2String(form.get("type"));
        String contentMsg = CommUtil.null2String(form.get("contentMsg"));
        Account account = this.accountService.getAccount(Long.parseLong(accountId));
        List<String> followerIds = new ArrayList<String>();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status", new Integer(1), "=");
        if ("select".equals(type)) {
            String followerIdsStr = CommUtil.null2String(form.get("foollowers"));
            followerIdsStr = followerIdsStr.substring(0, followerIdsStr.length() - 1);
            qo.addQuery("obj.id in (" + followerIdsStr + ")");
        }
        List<?> list = this.followerService.getFollowerBy(qo).getResult();
        for (Object obj : list) {
            followerIds.add(((Follower) obj).getWeixinOpenId());
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> mapdata = new HashMap<String, Object>();
        jsonMap.put("touser", followerIds);
        if ("".equals(templateId)) {
            jsonMap.put("msgtype", "text");
            mapdata.put("content", contentMsg);
            jsonMap.put("text", mapdata);
        } else {
            Template template = this.templateService.getTemplate(Long.parseLong(templateId));
            String temType = template.getType();
            if ("1".equals(temType)) {
                jsonMap.put("msgtype", "text");
                mapdata.put("content", template.getContent());
                jsonMap.put("text", mapdata);
            } else if ("2".equals(temType)) {
                qo = new QueryObject();
                qo.addQuery("obj.tpl", template, "=");
                qo.setOrderBy("sequence");
                List<?> news = this.newsItemService.getNewsItemBy(qo).getResult();
                List<Map<String, Object>> articles = new ArrayList<Map<String, Object>>();
                if (news != null && news.size() > 0) {
                    for (Object obj : news) {
                        NewsItem newsItem = (NewsItem) obj;
                        Map<String, Object> mapItem = new HashMap<String, Object>();
                        mapItem.put("thumb_media_id", getItemThMediaId(newsItem));
                        mapItem.put("title", newsItem.getTitle());
                        if (newsItem.getUrl() != null && !"".equals(newsItem.getUrl())) {
                            mapItem.put("content_source_url", newsItem.getUrl());
                        } else {
                            String url = newsItem.getTenant().getUrl() + WeixinUtils.NEWSITEMURL + "&accountId=" + account.getId() + "&id="
                                    + newsItem.getId();
                            url = WeixinBaseUtils.getWxUrl(url, account);
                            mapItem.put("content_source_url", url);
                        }
                        mapItem.put("content", newsItem.getContent());
                        articles.add(mapItem);
                    }
                }
                mapdata.put("articles", articles);
                JSONObject jocur = new JSONObject(mapdata);
                String jsonStrcur = jocur.toJSONString();
                AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
                String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
                url = url.replace("ACCESS_TOKEN", sccessToken.getToken());
                JSONObject jSONObjectcur = WeixinUtils.httpRequest(url, "POST", jsonStrcur);
                String mediaId = jSONObjectcur.getString("media_id");
                mapdata = new HashMap<String, Object>();
                if (mediaId != null && !"".equals(mediaId)) {
                    jsonMap.put("msgtype", "mpnews");
                    mapdata.put("media_id", mediaId);
                    jsonMap.put("mpnews", mapdata);
                } else {
                    jsonMap.put("msgtype", "text");
                    mapdata.put("content", template.getContent());
                    jsonMap.put("text", mapdata);
                }
            } else if ("4".equals(temType)) {
                jsonMap.put("msgtype", "image");
                mapdata.put("media_id", template.getMediaId());
                jsonMap.put("image", mapdata);
            } else if ("5".equals(temType) || "3".equals(temType)) {
                jsonMap.put("msgtype", "voice");
                mapdata.put("media_id", template.getMediaId());
                jsonMap.put("voice", mapdata);
            } else if ("6".equals(temType)) {
                mapdata.put("media_id", getMediaId(template));
                mapdata.put("title", template.getTitle());
                mapdata.put("description", template.getContent());
                JSONObject jocur = new JSONObject(jsonMap);
                String jsonStrcur = jocur.toJSONString();
                AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
                String urlcur = "https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=ACCESS_TOKEN";
                urlcur = urlcur.replace("ACCESS_TOKEN", sccessToken.getToken());
                JSONObject jSONObjectcur = WeixinUtils.httpRequest(urlcur, "POST", jsonStrcur);
                String mediaId = jSONObjectcur.getString("media_id");
                mapdata = new HashMap<String, Object>();
                if (mediaId != null && !"".equals(mediaId)) {
                    jsonMap.put("msgtype", "mpvideo");
                    mapdata.put("media_id", mediaId);
                    jsonMap.put("mpvideo", mapdata);
                } else {
                    jsonMap.put("msgtype", "text");
                    mapdata.put("content", template.getContent());
                    jsonMap.put("text", mapdata);
                }
            }
        }

        JSONObject jo = new JSONObject(jsonMap);
        String jsonStr = jo.toJSONString();
        JSONObject jSONObject = RequestWxUtils.sendMsgToAll(account, jsonStr);
        form.jsonResult(jSONObject);
        return Page.JSONPage;
    }

    /**
     * 发送客服消息
     * 
     * @param form
     * @return
     */
    public Page doSendMessageKf(WebForm form) {
        String templateId = CommUtil.null2String(form.get("templateId"));
        String accountId = CommUtil.null2String(form.get("accountId"));
        String followerId = CommUtil.null2String(form.get("followerId"));

        Account account = this.accountService.getAccount(Long.parseLong(accountId));
        String domain = account.getTenant().getUrl();
        Follower follower = this.followerService.getFollower(Long.parseLong(followerId));
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> mapdata = new HashMap<String, Object>();
        jsonMap.put("touser", follower.getWeixinOpenId());
        String content = CommUtil.null2String(form.get("contentMsg"));
        Template template = null;
        String msgType = "text";
        if ("".equals(templateId)) {
            jsonMap.put("msgtype", "text");
            mapdata.put("content", content);
            jsonMap.put("text", mapdata);
        } else {
            template = this.templateService.getTemplate(Long.parseLong(templateId));
            String temType = template.getType();
            if ("1".equals(temType)) {
                jsonMap.put("msgtype", "text");
                mapdata.put("content", template.getContent());
                jsonMap.put("text", mapdata);
            } else if ("2".equals(temType)) {
                msgType = "news";
                jsonMap.put("msgtype", "news");
                QueryObject qo = new QueryObject();
                qo.addQuery("obj.tpl", template, "=");
                qo.setOrderBy("sequence");
                List<?> list = this.newsItemService.getNewsItemBy(qo).getResult();
                List<Map<String, Object>> articles = new ArrayList<Map<String, Object>>();
                if (list != null && list.size() > 0) {
                    for (Object obj : list) {
                        NewsItem newsItem = (NewsItem) obj;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", newsItem.getTitle());
                        map.put("description", newsItem.getDescription());
                        if (newsItem.getUrl() != null && !"".equals(newsItem.getUrl())) {
                            map.put("url", newsItem.getUrl());
                        } else {
                            String url = newsItem.getTenant().getUrl() + WeixinUtils.NEWSITEMURL + "&accountId=" + account.getId() + "&id="
                                    + newsItem.getId();
                            url = WeixinBaseUtils.getWxUrl(url, account);
                            map.put("url", url);
                        }
                        map.put("picurl", domain + newsItem.getImagePath());
                        articles.add(map);
                    }
                }
                mapdata.put("articles", articles);
                jsonMap.put("news", mapdata);
            } else if ("3".equals(temType)) {
                msgType = "music";
                jsonMap.put("msgtype", "music");
                mapdata.put("title", template.getTitle());
                mapdata.put("description", template.getContent());
                mapdata.put("musicurl", template.getTenant().getUrl() + template.getMediaPath());
                mapdata.put("thumb_media_id", getThMediaId(template));
                jsonMap.put("music", mapdata);
            } else if ("4".equals(temType)) {
                msgType = "image";
                jsonMap.put("msgtype", "image");
                mapdata.put("media_id", getMediaId(template));
                jsonMap.put("image", mapdata);
            } else if ("5".equals(temType)) {
                msgType = "voice";
                jsonMap.put("msgtype", "voice");
                mapdata.put("media_id", getMediaId(template));
                jsonMap.put("voice", mapdata);
            } else if ("6".equals(temType)) {
                msgType = "video";
                jsonMap.put("msgtype", "video");
                mapdata.put("media_id", getMediaId(template));
                mapdata.put("thumb_media_id", getThMediaId(template));
                mapdata.put("title", template.getTitle());
                mapdata.put("description", template.getContent());
                jsonMap.put("video", mapdata);
            }
        }
        JSONObject jo = new JSONObject(jsonMap);
        String jsonStr = jo.toJSONString();
        JSONObject jSONObject = RequestWxUtils.sendMsgToKF(account, jsonStr);
        if (jSONObject != null && "0".equals(jSONObject.get("errcode") + "")) {
            FollowerMessage follmsg = new FollowerMessage();
            follmsg.setIsFollowerMsg(false);
            follmsg.setAccount(account);
            follmsg.setFollower(follower);
            follmsg.setMsgType(msgType);
            if ("".equals(templateId)) {
                follmsg.setContent(content);
            } else {
                follmsg.setTemplate(template);
                follmsg.setContent(template.getContent());
            }
            follmsg.setCreateDate(new Date());
            this.followerMessageService.addFollowerMessage(follmsg);
        }
        form.jsonResult(jSONObject);
        return Page.JSONPage;
    }

    /**
     * 获取多媒体
     * 
     * @param template
     */
    private String getMediaId(Template template) {
        if (template.getMediaId() != null || "".equals(template.getMediaId())
                || (new Date().getTime() - template.getMeidaCreateAt()) > 3 * 24 * 60 * 60 * 1000) {
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
            Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(template.getAccount(), mediaUploadType, template.getTenant()
                    .getUrl() + mediaPath);
            template.setMediaId(mapreturn.get("media_id") + "");
            template.setMeidaCreateAt(new Date().getTime());
            this.templateService.updateTemplate(template.getId(), template);
        }
        return template.getMediaId();
    }

    /**
     * 获取模板缩略图
     * 
     * @param template
     * @return
     */
    private String getThMediaId(Template template) {
        if (template.getThumbMediaId() == null || "".equals(template.getThumbMediaId())
                || (new Date().getTime() - template.getThumbMediaCreateAt()) > 3 * 24 * 60 * 60 * 1000) {
            String mediaPath = template.getThumbMediaPath();
            Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(template.getAccount(), "image", template.getTenant().getUrl()
                    + mediaPath);
            template.setThumbMediaId(mapreturn.get("media_id") + "");
            template.setThumbMediaCreateAt(new Date().getTime());
            this.templateService.updateTemplate(template.getId(), template);
        }
        return template.getThumbMediaId();
    }

    /**
     * 获取图文缩略图
     * 
     * @param template
     * @return
     */
    private String getItemThMediaId(NewsItem newsItem) {
        if (newsItem.getMediaId() == null || "".equals(newsItem.getMediaId())
                || (new Date().getTime() - newsItem.getMediaCreateAt()) > 3 * 24 * 60 * 60 * 1000) {
            String mediaPath = newsItem.getImagePath();
            Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(newsItem.getTpl().getAccount(), "image", newsItem.getTenant()
                    .getUrl() + mediaPath);
            newsItem.setMediaId(mapreturn.get("media_id") + "");
            newsItem.setMediaCreateAt(new Date().getTime());
            this.newsItemService.updateNewsItem(newsItem.getId(), newsItem);
        }
        return newsItem.getMediaId();
    }

    /**
     * 查看消息详情
     * 
     * @param form
     * @return
     */
    public Page doMessageDefail(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        FollowerMessage followerMessage = this.followerMessageService.getFollowerMessage(Long.parseLong(id));
        form.addResult("followerMessage", followerMessage);
        if (followerMessage.getIsFollowerMsg()) {
            if (followerMessage.getMediaId() != null && !"".equals(followerMessage.getMediaId())
                    && (followerMessage.getMediaUrl() == null || "".equals(followerMessage.getMediaUrl()))) {
                String mediaUrl = RequestWxUtils.downloadMedia(followerMessage.getAccount(), followerMessage.getMediaId(),
                        UploadFileConstant.FILE_UPLOAD_PATH + "/" + followerMessage.getTenant().getCode() + "/"
                                + followerMessage.getAccount().getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA);
                followerMessage.setMediaUrl(mediaUrl);
                this.followerMessageService.updateFollowerMessage(followerMessage.getId(), followerMessage);
            }
        } else if ("news".equals(followerMessage.getMsgType())) {
            QueryObject qo = new QueryObject();
            qo.addQuery("obj.tpl", followerMessage.getTemplate(), "=");
            List<?> list = this.newsItemService.getNewsItemBy(qo).getResult();
            form.addResult("newsItems", list);
        }
        return new Page("/common/followerMsg.html");
    }

    /**
     * 删除粉丝发送的消息
     * 
     * @param form
     * @return
     */
    public Page doDelMessage(WebForm form) {
        String type = CommUtil.null2String(form.get("type"));
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        String accountId = CommUtil.null2String(form.get("accountId"));
        Account account = null;
        if (!"".equals(accountId)) {
            account = this.accountService.getAccount(Long.parseLong(accountId));
        }
        if ("all".equals(type)) {
            qo.addQuery("obj.account", account, "=");
        } else if ("48".equals(type)) {
            qo.addQuery("obj.account", account, "=");
            qo.addQuery("UNIX_TIMESTAMP(obj.createDate)>" + (new Date().getTime() - (48 * 60 * 60 * 1000)));
        } else if ("one".equals(type)) {
            String ids = CommUtil.null2String(form.get("ids"));
            ids = ids.substring(0, ids.length() - 1);
            qo.addQuery("obj.id in(" + ids + ")");
        }
        List<FollowerMessage> follmsgs = this.followerMessageService.getFollowerMessageBy(qo).getResult();
        if (follmsgs != null && follmsgs.size() > 0) {
            for (FollowerMessage follmsg : follmsgs) {
                if (follmsg.getMediaUrl() != null && !"".equals(follmsg.getMediaUrl())) {
                    deleFile(follmsg.getMediaUrl());
                }
                this.followerMessageService.delFollowerMessage(follmsg.getId());
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    private void deleFile(String filename) {
        File file = new File(Globals.APP_BASE_DIR + filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
