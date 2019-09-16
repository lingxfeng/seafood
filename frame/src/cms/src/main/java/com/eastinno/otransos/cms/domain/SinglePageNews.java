package com.eastinno.otransos.cms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * 单页新闻 直接归属于网站中的单页新闻信息，这些信息不归属为某个栏目，而是跟指定的模板相对应。
 * 
 * @intro
 * @version v0.1
 * @author maowei
 * @since 2014年6月8日 下午4:15:28
 */
@Entity(name = "Disco_SaaS_CMS_SinglePageNews")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SinglePageNews extends TenantObject implements AutoChangeLink, Comparable, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, length = 32)
    @NotBlank(message = "code不能为空")
    @Size(max = 32, min = 2, message = "长度必须在20个字符以内")
    private String code;

    @Column(length = 50)
    private String title;

    @Column(length = 200)
    private String subTitle;// 副标题

    @ManyToOne
    @POLoad(name = "parentId")
    private SinglePageNews parent;

    @Column(length = 250)
    private String url;// 直接URL连接

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;// 文章内容

    private String types;

    @POLoad(name = "templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateFile tpl;// 模板文件

    private Boolean display = true;// 是否前台显示

    private Integer sequence = 1;
    private Integer status = 2;// 文章状态 1:待审核(发布) 0:回收站(删除)2发布

    /**
     * 子页面
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<SinglePageNews> children = new ArrayList<SinglePageNews>();

    private Integer readTimes = 0;// 点击数

    private Date createDate = new Date();// 创建时间
    private Date updateDate = new Date();// 修改时间

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SinglePageNews getParent() {
        return parent;
    }

    public void setParent(SinglePageNews parent) {
        this.parent = parent;
    }

    public List<SinglePageNews> getChildren() {
        return children;
    }

    public void setChildren(List<SinglePageNews> children) {
        this.children = children;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TemplateFile getTpl() {
        return tpl;
    }

    public void setTpl(TemplateFile tpl) {
        this.tpl = tpl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int compareTo(Object o) {
        if (o == null)
            return 1;
        SinglePageNews menu = (SinglePageNews) o;
        if (menu.getSequence() == null)
            return 1;
        if (this.sequence == null)
            return -1;
        return this.sequence - menu.getSequence() == 0 ? this.title.compareTo(menu.getTitle()) : this.sequence - menu.getSequence();
    }

    public String getDynamicUrl() {
        return "/news.ejf?cmd=singlePage&sn=" + this.id;
    }

    public String getStaticUrl() {
        return "/html/single/" + this.id + "/";
    }

    @Override
    public String getStaticPath() {
        return "/html/single/" + this.id + "/index.shtml";
    }

    /**
     * 递归获取单页新闻对应的模板
     * 
     * @param singlePageNews
     * @return
     */
    public Page getShowTemplate(SinglePageNews singlePageNews) {
        if (isLink()) {
            return new Page("url", this.getUrl(), "html");
        }
        if (singlePageNews.getTpl() != null && StringUtils.hasText(singlePageNews.getTpl().getPath())) {
            return new Page(singlePageNews.getTpl().getPath().substring("/WEB-INF/views".length()));
        } else {
            if (singlePageNews.getParent() == null) {
                return new Page("/news/show.html");
            }
            return getShowTemplate(singlePageNews.getParent());
        }
    }

    public boolean isLink() {
        return StringUtils.hasText(url) ? true : false;
    }

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"parent", "children", "company"});
        if (parent != null)
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "sn", "title"}));
        return map;
    }

}
