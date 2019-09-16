package com.eastinno.otransos.cms.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @intro 友情链接管理
 * @author nsz
 * @since 2014/4/25 13:10
 */
@Entity(name = "Disco_SaaS_CMS_LinksManage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LinksManage extends TenantObject {
    private static final long serialVersionUID = 1619198546364080906L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 100)
    @NotBlank(message = "该字段不能为空")
    @Size(max = 100, message = "长度必须在100个字符以内")
    private String name;// 链接名称
    private String logoImg;// LOGO图片
    private String logoUrl;// LOGO图片链接地址
    private Date startDate;// 生效日期
    private Date endDate;// 失效日期

    @Column(length = 200)
    private String linkUrl;// 链接URL地址

    @Column(length = 20)
    private String target;// 目标打开的方式(_blank,_parent,_self,_top)

    private Date createTime = new Date();// 录入时间

    private String intro;// 简介
    private Integer sequence = 1;// 排序

    private Short status;// 状态1启用 0禁用

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Long getId() {
        return id;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, null);
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getTarget() {
        return target;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

}
