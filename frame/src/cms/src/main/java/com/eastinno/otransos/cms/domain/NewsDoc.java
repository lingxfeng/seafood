package com.eastinno.otransos.cms.domain;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * @intro 文章实体模型
 * @version v_0.1
 * @author lengyu
 * @since 2012-10-25 下午10:23:39
 */
@Entity(name = "Disco_SaaS_CMS_NewsDoc")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(disInject = "id,dirPath,createDate")
public class NewsDoc extends TenantObject implements AutoChangeLink {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long Id;

    private String title;// 标题
    @POLoad(name = "dirId")
    @ManyToOne(fetch = FetchType.LAZY)
    private NewsDir dir;// 所属栏目

    private String dirPath;// 文章所在栏目的路径

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;// 内容(大字段)

    @Size(min = 0, max = 10, message = "来源长度应在0-10位之间")
    private String source = ""; // 来源

    private Integer status = 2;// 文章状态 1:待审核(发布) 0:回收站(删除)2发布
    private String url;// 文章外链地址
    private Date createDate = new Date();// 创建日期
    private Date putDate = new Date();// 发布日期
    private Integer count = 0;// 查看次数
    private Integer sequence = 1;// 排序号

    private String keywords;// SEO关键字[同时具有tags功能 在查找相关文章时要用到]
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;// 描述简介

    @POLoad(name = "imgTypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkImgType imgType;

    private String iconPath;// 文章图标ICON
    private Integer readtime = 0;// 阅读次数
    private Integer isTop = 0;// 是否置顶(1置顶 0不置顶)
    private Integer elite = 0;// 是否推荐(1推荐 0不推荐)

    @POLoad(name = "docTypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemDictionaryDetail docType;// 文章类型（普通文章、视频文章）

    @POLoad(name = "templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateFile templateFile;
    @Column(length=100)
    private String imgPath;//头图
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NewsDir getDir() {
        return dir;
    }

    public void setDir(NewsDir dir) {
        this.dir = dir;
    }

    public String getDirPath() {
        return dirPath;
    }
    
    public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPutDate() {
        return putDate;
    }

    public void setPutDate(Date putDate) {
        this.putDate = putDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Integer getElite() {
        return elite;
    }

    public void setElite(Integer elite) {
        this.elite = elite;
    }

    @Override
    public String getDynamicUrl() {
        return "/news.java?cmd=show&id=" + this.getId();
    }

    @Override
    public String getStaticUrl() {
        return "/html/" + TenantContext.getTenant().getCode() + "/doc/" + this.getId() + "/";
    }

    public String getStaticPath() {
        return "/html/" + TenantContext.getTenant().getCode() + "/doc/" + this.getId() + "/index.shtml";
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCount() {
        return count;
    }

    public TemplateFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(TemplateFile templateFile) {
        this.templateFile = templateFile;
    }

    public LinkImgType getImgType() {
        return imgType;
    }

    public void setImgType(LinkImgType imgType) {
        this.imgType = imgType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getReadtime() {
        return readtime;
    }

    public void setReadtime(Integer readtime) {
        this.readtime = readtime;
    }

    @Override
    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"dir", "content", "docType", "imgType", "docType", "templateFile", "tenant"});

        if (this.dir != null) {
            map.put("dir", CommUtil.obj2map(this.dir, new String[] {"id", "code", "name"}));
        }
        if (this.docType != null) {
            map.put("docType", CommUtil.obj2map(this.docType, new String[] {"id", "title"}));
        }
        if (this.templateFile != null) {
            map.put("templateFile", CommUtil.obj2map(this.templateFile, new String[] {"id", "title"}));
        }
        if (this.imgType != null) {
            map.put("imgType", CommUtil.obj2map(this.templateFile, new String[] {"id", "title"}));
        }
        if (this.tenant != null) {
            map.put("tenant", CommUtil.obj2map(this.tenant, new String[] {"id", "title", "code"}));
        }

        return map;
    }

    @Override
    public NewsDoc clone() {
        NewsDoc o = null;
        try {
            o = (NewsDoc) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public SystemDictionaryDetail getDocType() {
        return docType;
    }

    public void setDocType(SystemDictionaryDetail docType) {
        this.docType = docType;
    }

    /**
     * 获取当前文章对应的文章模板
     * 
     * @return
     */
    public Page getDocTemplate() {
        String tenantCode = "/" + TenantContext.getTenant().getCode();
        if (this.isLink()) {// 文章外链
            return new Page("url", this.getUrl(), "html");
        }
        if (this.getTemplateFile() != null && this.getTemplateFile().getPath() != null) {// 如果文章有模板则直接使用文章模板
            return new Page(this.getTemplateFile().getPath().replace("/WEB-INF/views/", ""));
        }
        if (this.getDir() != null) {// 获取文章所属的栏目对应的文章模板
            NewsDir dir = this.getDir();
            if (dir.getDocTpl() != null && StringUtils.hasText(dir.getDocTpl().getPath())) {
                return new Page(dir.getDocTpl().getPath().replace("/WEB-INF/views/", ""));
            } else {
                return getDocTemplate(dir);
            }
        }
        // 最后找不到模板则使用文章默认的模板show.html
        return new Page(tenantCode + "/news/doc/show.html");
    }

    /**
     * 默认使用栏目的code作为模板名,如果对应的code模板名文件不存在则返回空页面
     * 
     * @return
     */
    private Page buildDocTemplatePath() {
        String tenantCode = TenantContext.getTenant().getCode();
        // 默认使用doc_for+栏目的code作为模板名,如果对应的模板文件不存在则使用公共的show.html
        String docShow = "/" + tenantCode + "/news/doc/" + "doc_for_" + this.getDir().getCode().trim() + ".html";
        String path = Globals.APP_BASE_DIR + Globals.DEFAULT_TEMPLATE_PATH + docShow;
        File file = new File(path);
        if (file.exists()) {
            return new Page(docShow);
        } else {
            return new Page(tenantCode + "/news/doc/show.html");
        }
    }

    /**
     * 根据指定栏目获取此栏目对应的文章模板
     * 
     * @param dir
     * @return
     */
    public Page getDocTemplate(NewsDir dir) {
        if (dir.getDocTpl() != null && StringUtils.hasText(dir.getDocTpl().getPath())) {
            return new Page(dir.getDocTpl().getPath().replace("/WEB-INF/views/", ""));
        } else {
            if (dir.getParent() == null) {
                return this.buildDocTemplatePath();
            }
            return getDocTemplate(dir.getParent());
        }
    }

    public boolean isLink() {
        return StringUtils.hasText(getUrl());
    }
}
