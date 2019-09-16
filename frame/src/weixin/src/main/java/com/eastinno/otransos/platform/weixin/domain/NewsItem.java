package com.eastinno.otransos.platform.weixin.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;

@Entity
@Table(name = "Disco_WeiXin_NewsItem")
public class NewsItem extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String title;
    private String author;
    private String imagePath;
    private String content;
    private String description;
    @POLoad(name = "tplId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Template tpl;
    private String url;//外链url
    /** 创建时间 */
    private Date createDate=new Date();
    private String mediaId;//微信多媒体id，有效期为3天，3天后再次用时，重新获取
    private Long mediaCreateAt;
    private Integer sequence=1;
    private Integer count=0;//点击量
	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "imagepath")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Template getTpl() {
		return tpl;
	}

	public void setTpl(Template tpl) {
		this.tpl = tpl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Long getMediaCreateAt() {
		return mediaCreateAt;
	}

	public void setMediaCreateAt(Long mediaCreateAt) {
		this.mediaCreateAt = mediaCreateAt;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
