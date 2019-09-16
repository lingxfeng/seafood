package com.eastinno.otransos.platform.weixin.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.bean.AccessToken;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerGroup;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.domain.TimedTask;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerGroupService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.platform.weixin.service.INewsItemService;
import com.eastinno.otransos.platform.weixin.service.ITemplateService;
import com.eastinno.otransos.platform.weixin.service.ITimedTaskService;
import com.eastinno.otransos.platform.weixin.service.IWxplatService;
import com.eastinno.otransos.web.Page;

@Component
public class TimedTaskUtils {
	@Autowired
    private ITimedTaskService service;
    
    @Autowired
    private IFollowerService followerService;
    
    @Autowired
    private IAccountService accountService;
    
    @Autowired
    private ITemplateService templateService;
    
    @Autowired
    private INewsItemService newsItemService;
    @Autowired
    private IWxplatService wxplatService;
    
    @Autowired
    private IFollowerGroupService followerGroupService;
    
    /**
     * 定时发送消息
     * 
     * @param form
     */
    @Scheduled(cron = "0 0/15 * * * ?")  
    public void doSendMsg() {
    	QueryObject qo = new QueryObject();
    	List<?> list=this.service.getTimedTaskBy(qo).getResult();
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
    	Long yt=(long) (24*60*60*1000);
    	Date currentTime = new Date();
    	String time_=sf.format(currentTime);
    	String time=time_.substring(6, time_.length());
    	String time2=time_.substring(8, time_.length());
    	//String time2=sf2.format(currentTime);
    	//System.out.println("------------------------------------1");
    	if(list!=null){
    		for (int i = 0; i < list.size(); i++) {
    			StringBuffer sb = new StringBuffer();
				TimedTask timedTask=(TimedTask)list.get(i);
				String date=timedTask.getDate();
				String hour=timedTask.getHour();
				String minute=timedTask.getMinute();
				String state=timedTask.getState();// 1:所有人 2：群 3:24小时有操作
				if(StringUtils.hasText(date)){
					sb.append(date).append(hour).append(minute);
					if(time.equals(sb.toString())){
						if("1".equals(state)){
							//发送给关注的所有人
							QueryObject fssyr= new QueryObject();
							fssyr.addQuery("obj.status", new Integer(1), "=");
							fssyr.addQuery("obj.account.id", timedTask.getAccount().getId(), "=");
							List<?> fssyrList=this.followerService.getFollowerBy(fssyr).getResult();
							if(fssyrList!=null){
								this.sendMessage(fssyrList, timedTask);
							}
							
						}else if("2".equals(state)){
							//发送给所选分组的人
							QueryObject qog = new QueryObject();
							List<?> list2=this.followerGroupService.getFollowerGroupBy(qog).getResult();
							if(list2!=null){
								for (int j = 0; j < list2.size(); j++) {
									FollowerGroup followerGroup=(FollowerGroup)list2.get(j);
									QueryObject qof = new QueryObject();
									qof.addQuery("obj.followerGroup", followerGroup, "=");
									List<?> listf=this.followerService.getFollowerBy(qof).getResult();
									this.sendMessage(listf, timedTask);
								}
							}
							
						}else if("3".equals(state)){
							//发送给24小时有操作的人
							QueryObject qo2 = new QueryObject();
							qo2.addQuery("obj.status", new Integer(1), "=");
							qo2.addQuery("obj.lastTimesend>='"+(currentTime.getTime()-yt)+"'");
							List<?> list2=this.followerService.getFollowerBy(qo2).getResult();
							if(list2!=null){
								this.sendMessage(list2, timedTask);
							}
						}
					}
				}else{
					sb.append(hour).append(minute);
					if(time2.equals(sb.toString())){
						if("1".equals(state)){
							//发送给关注的所有人
							QueryObject fssyr= new QueryObject();
							fssyr.addQuery("obj.account.id", timedTask.getAccount().getId(), "=");
							fssyr.addQuery("obj.status", new Integer(1), "=");
							List<?> fssyrList=this.followerService.getFollowerBy(fssyr).getResult();
							if(fssyrList!=null){
								this.sendMessage(fssyrList, timedTask);
							}
							
						}else if("2".equals(state)){
							//发送给所选分组的人
							QueryObject qog = new QueryObject();
							List<?> list2=this.followerGroupService.getFollowerGroupBy(qog).getResult();
							if(list2!=null){
								for (int j = 0; j < list2.size(); j++) {
									FollowerGroup followerGroup=(FollowerGroup)list2.get(j);
									QueryObject qof = new QueryObject();
									qof.addQuery("obj.followerGroup", followerGroup, "=");
									List<?> listf=this.followerService.getFollowerBy(qof).getResult();
									this.sendMessage(listf, timedTask);
								}
							}
						}else if("3".equals(state)){
							//发送给24小时有操作的人
							QueryObject qo2 = new QueryObject();
							qo2.addQuery("obj.account.id", timedTask.getAccount().getId(), "=");
							qo2.addQuery("obj.status", new Integer(1), "=");
							qo2.addQuery("obj.lastTimesend>='"+(currentTime.getTime()-yt)+"'");
							List<?> list2=this.followerService.getFollowerBy(qo2).getResult();
							if(list2!=null){
								this.sendMessage(list2, timedTask);
							}
						}
					}
				}
			}
    	}
    }
    
    /**
     * 消息群发
     * 
     * @param form
     * @return
     */
    public Boolean sendMessage(List<?> list,TimedTask timedTask) {
    	Account account=timedTask.getAccount();
    	Template template=timedTask.getTemplate();
    	String description=timedTask.getDescription();
        List<String> followerIds = new ArrayList<String>();
        for (Object obj : list) {
            followerIds.add(((Follower) obj).getWeixinOpenId());
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> mapdata = new HashMap<String, Object>();
        jsonMap.put("touser", followerIds);
        if (template==null) {
            jsonMap.put("msgtype", "text");
            mapdata.put("content", description);
            jsonMap.put("text", mapdata);
        } else {
            String temType = template.getType();
            if ("1".equals(temType)) {
                jsonMap.put("msgtype", "text");
                mapdata.put("content", template.getContent());
                jsonMap.put("text", mapdata);
            } else if ("2".equals(temType)) {
                QueryObject qo = new QueryObject();
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
        JSONObject jSONObject = RequestWxUtils.sendMsgToAll(account, jsonStr,wxplatService);
        //form.jsonResult(jSONObject);
        Page page=Page.JSONPage;
        System.out.println(page);
        return true;
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
}
