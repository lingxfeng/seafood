package com.eastinno.otransos.cms.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @intro 链接型图片库
 * @version v0.1
 * @author maowei
 * @since 2014年6月7日 下午7:03:03
 */
@Entity(name = "Disco_SaaS_CMS_LinkImgGroup")
public class LinkImgGroup extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 20)
    @Size(max = 20, message = "长度必须在20个字符以内")
    private String title;

    @POLoad(name = "typeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkImgType type;
    private String imgPath;// 图片地址
    private String linkUrl;

    private Short resourceType = 1;// 资源类型1图片型 2视频型
    private String videoPath;// 视频地址
    private String videoImg;// 视频头图

    private Boolean elite = false;// 是否推荐

    private String text;

    private Date createTime = new Date();// 上传日期
    private Integer sequence = 1;
    private String operation;// 图片操作类型，0，1 ，2
    
    private String classy;//分类商城专用
    private Long allid;//商城专用
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public LinkImgType getType() {
        return type;
    }

    public void setType(LinkImgType type) {
        this.type = type;
    }

    public Short getResourceType() {
        return resourceType;
    }

    public void setResourceType(Short resourceType) {
        this.resourceType = resourceType;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public Boolean getElite() {
        return elite;
    }

    public void setElite(Boolean elite) {
        this.elite = elite;
    }
    
    public String getClassy() {
		return classy;
	}

	public void setClassy(String classy) {
		this.classy = classy;
	}

	public Long getAllid() {
		return allid;
	}

	public void setAllid(Long allid) {
		this.allid = allid;
	}

	public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"type"});
        if (this.type != null)
            map.put("type", CommUtil.obj2map(this.type, new String[] {"id", "title"}));
        return map;
    }

}
