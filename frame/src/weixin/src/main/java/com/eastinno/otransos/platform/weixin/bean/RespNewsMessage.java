package com.eastinno.otransos.platform.weixin.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 输出图文消息
 * 
 * @author maowei
 */
public class RespNewsMessage extends RespMessage {

    private String MsgType = "news";
    private Integer ArticleCount;
    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;

    private List<Article> Articles;

    public String getMsgType() {
        return MsgType;
    }

    public int getArticleCount() {
        return ArticleCount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        if (articles != null) {
            if (articles.size() > 10)
                articles = new ArrayList<Article>(articles.subList(0, 10));

            ArticleCount = articles.size();
        }
        Articles = articles;
    }

	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}
    
}
