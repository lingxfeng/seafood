package com.eastinno.otransos.cms.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * @intro 栏目管理
 * @author nsz
 * @since 2012-10-24 下午11:16:29
 */
@SuppressWarnings("rawtypes")
@Entity(name = "Disco_SaaS_CMS_NewsDir")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NewsDir extends TenantObject implements AutoChangeLink, Comparable, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, length = 50)
    @NotBlank(message = "栏目编码不能为空")
    @Size(max = 50, min = 2, message = "栏目编码长度必须在2-50之间")
    private String code;
    @Column(length=50)
    private String tCode;
    @Column(length = 50)
    @NotBlank(message = "栏目名称不能为空")
    @Size(max = 50, min = 2, message = "栏目名称长度必须在2-50之间")
    private String name;// 名称

    private String dirPath;// 栏目深度路径
    private Integer docNum = 0;// 该栏目下的文章数量

    @Size(max = 100)
    private String url;// 外链URL地址

    private String bannerImg;// 栏目下的导航条
    private String bannerImgUrl;// 导航条外链地址

    private Integer types = 0;// 0实体栏目 1链接栏目 2单页栏目

    @Column(length = 30)
    @Size(max = 30, message = "长度必须在30字符以内")
    private String keywords;// 关键字词

    @Column(length = 100)
    @Size(max = 100, message = "长度必须在100字符以内")
    private String description;// 栏目描述

    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private NewsDir parent;// 父级栏目

    @OrderBy("sequence asc")
    @BatchSize(size = 15)
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<NewsDir> children = new ArrayList<NewsDir>();

    private Integer sequence = 1;// 栏目排序号
    private Date createDate = new Date();// 创建日期

    private Integer status = 1;// 栏目状态 1:启用 0:停用
    private String openStyle = "_self";// 超链接打开方式：_blank；_parent；_self；_top

    @POLoad(name = "docTplId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateFile docTpl;// 文章模版

    @POLoad(name = "dirTplId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateFile dirTpl;// 栏目模版

    @POLoad(name = "pPtTypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkImgType pPtType;

    public String getOpenStyle() {
        return openStyle;
    }

    public void setOpenStyle(String openStyle) {
        this.openStyle = openStyle;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public Integer getDocNum() {
        return docNum;
    }

    public void setDocNum(Integer docNum) {
        this.docNum = docNum;
    }

    public NewsDir getParent() {
        return parent;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }
    
    public String gettCode() {
		return tCode;
	}

	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	public void setParent(NewsDir parent) {
        this.parent = parent;
    }

    public List<NewsDir> getChildren() {
        return children;
    }

    public void setChildren(List<NewsDir> children) {
        this.children = children;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TemplateFile getDocTpl() {
        return docTpl;
    }

    public void setDocTpl(TemplateFile docTpl) {
        this.docTpl = docTpl;
    }

    public TemplateFile getDirTpl() {
        return dirTpl;
    }

    public void setDirTpl(TemplateFile dirTpl) {
        this.dirTpl = dirTpl;
    }

    public LinkImgType getpPtType() {
        return pPtType;
    }

    public void setpPtType(LinkImgType pPtType) {
        this.pPtType = pPtType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    /**
     * 得到栏目下可用的一级子栏目列表
     * 
     * @return
     */
    public List<NewsDir> getNormalChildren() {
        List<NewsDir> ret = new ArrayList<NewsDir>();
        for (NewsDir dir : children) {
            if (dir.status == 1) {
                ret.add(dir);
            }
        }
        return ret;
    }

    @Override
    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"parent", "pPtType", "dirTpl", "docTpl", "children", "tenant"});
        if (this.parent != null) {
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "code", "name"}));
        }
        if (this.pPtType != null) {
            map.put("pPtType", CommUtil.obj2map(this.pPtType, new String[] {"id", "code", "title"}));
        }
        if (this.dirTpl != null) {
            map.put("dirTpl", CommUtil.obj2map(this.dirTpl, new String[] {"id", "title"}));
        }
        if (this.docTpl != null) {
            map.put("docTpl", CommUtil.obj2map(this.docTpl, new String[] {"id", "title"}));
        }
        if (this.tenant != null) {
            map.put("tenant", CommUtil.obj2map(this.tenant, new String[] {"id", "title", "code"}));
        }
        return map;
    }

    @Override
    public String getStaticUrl() {
        if (this.code.equals(TenantContext.getTenant().getCode() + "_index")) {
            return "/html/" + TenantContext.getTenant().getCode() + "/";
        }
        return "/html/" + TenantContext.getTenant().getCode() + "/dir/" + this.getId() + "/";
    }

    public String getStaticPath() {
        if (this.code.equals(TenantContext.getTenant().getCode() + "_index")) {// 生成首页(首页code必须为index)
            return "/html/" + TenantContext.getTenant().getCode() + "/index.shtml";
        }
        return "/html/" + TenantContext.getTenant().getCode() + "/dir/" + this.getId() + "/index.shtml";
    }

    @Override
    public String getDynamicUrl() {
        if (StringUtils.hasText(this.url)) {
            return this.url;
        }
        return "/news.java?cmd=dir&code=" + this.code;
    }

    public static void main(String[] args) {
        String s = "name {0}";
        System.out.println(s.replace("{0}", "111"));
    }

    /**
     * 获取当前栏目对应的栏目模板
     * 
     * @return
     */
    public Page getDirTemplate() {
        if (this.isLinkDir()) {// 栏目外链
            return new Page("url", this.getUrl(), "html");
        }
        if (this.getDirTpl() != null) {// 如果栏目有模板则直接使用当前栏目对应的模板
            System.out.println(this.getDirTpl().getPath());
            return new Page(this.getDirTpl().getPath().replace("/WEB-INF/views/", ""));
        }
        if (this.getParent() != null) {
            NewsDir parent = this.getParent();
            if (parent.getDirTpl() != null && StringUtils.hasText(parent.getDirTpl().getPath())) {
                return new Page(parent.getDirTpl().getPath());
            } else {
                return getDirTemplate(parent);
            }
        }
        return buildDirTemplatePath();
    }

    /**
     * 根据指定栏目获取此栏目对应的栏目模板
     * 
     * @param dir
     * @return
     */
    public Page getDirTemplate(NewsDir dir) {
        if (dir.getDirTpl() != null && StringUtils.hasText(dir.getDirTpl().getPath())) {
            return new Page(dir.getDirTpl().getPath().replace("/WEB-INF/views/", ""));
        } else {
            if (dir.getParent() == null) {
                return buildDirTemplatePath();
            }
            return getDirTemplate(dir.getParent());
        }
    }

    /**
     * 默认使用栏目的code作为模板名,如果对应的code模板名文件不存在则返回空页面
     * 
     * @return
     */
    private Page buildDirTemplatePath() {
        String tenantCode = TenantContext.getTenant().getCode();
        String p = tenantCode + "/news/dir/" + this.getCode() + ".html";
        // 默认使用栏目的code作为模板名,如果对应的code模板名文件不存在则使用dir.html
        String path = Globals.APP_BASE_DIR + Globals.DEFAULT_TEMPLATE_PATH + p;
        File file = new File(path);
        if (file.exists()) {
            return new Page(p);
        } else if ((tenantCode + "_index").equals(this.code)) {// 首页直接返回
            return new Page("/" + TenantContext.getTenant().getCode() + "/news/index.html");
        } else {
            return Page.nullPage;
        }
    }

    public boolean isShowDir() {
        return getStatus().intValue() == 1 ? true : false;
    }

    public boolean isEntityDir() {
        return !isLinkDir();
    }

    public boolean isLinkDir() {
        return this.types.intValue() == 1 ? true : false;
    }

    /**
     * 返回前台显示的栏目，show标志被设置成false的即为不显示的
     * 
     * @return
     */
    public Set<NewsDir> getFrontShowChildren() {
        Set<NewsDir> ret = new TreeSet<NewsDir>();
        for (NewsDir dir : children) {
            if (dir.isShowDir()) {
                ret.add(dir);
            }
        }
        return ret;
    }

    /**
     * 返回实体型栏目，也就是可以添加内容的栏目。
     * 
     * @return
     */
    public Set<NewsDir> getEntityChildren() {
        Set<NewsDir> ret = new TreeSet<NewsDir>();
        for (NewsDir dir : children) {
            if (dir.status >= 0 && dir.isEntityDir()) {
                ret.add(dir);
            }
        }
        return ret;
    }

    public int compareTo(Object o) {
        if (o == null)
            return 1;
        NewsDir target = (NewsDir) o;
        if (target.getSequence() == null)
            return 1;
        if (this.sequence == null)
            return -1;
        int ret = this.sequence - target.getSequence();
        if (ret == 0) {
            ret = this.code.compareToIgnoreCase(target.getCode());
        }
        return ret;
    }
}
