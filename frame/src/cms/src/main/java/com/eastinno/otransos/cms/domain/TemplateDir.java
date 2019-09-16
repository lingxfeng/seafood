package com.eastinno.otransos.cms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 模版栏目分类
 * @verion 1.0
 * @author nsz
 * @since 2014年5月7日 上午9:55:21
 */
@Entity(name = "Disco_SaaS_CMS_TemplateDir")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TemplateDir extends TenantObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    @NotBlank(message = "该字段不能为空")
    @Size(max = 32, message = "长度必须在32个字符以内")
    private String sn;
    @Column(length = 100)
    @NotBlank(message = "该字段不能为空")
    @Size(max = 100, message = "长度必须在100个字符以内")
    private String title;

    @Column(length = 200)
    @NotBlank(message = "该字段不能为空")
    @Size(max = 200, message = "长度必须在200个字符以内")
    private String description;
    private String path;

    @ManyToOne
    @POLoad(name = "parentId")
    private TemplateDir parent;

    @OrderBy("sequence")
    @BatchSize(size = 1)
    @OneToMany(mappedBy = "parent", cascade = {javax.persistence.CascadeType.REMOVE})
    private List<TemplateDir> childrens = new ArrayList<TemplateDir>();
    private Integer sequence = 1;
    private Date inputDate = new Date();
    private Integer status = Integer.valueOf(1);

    public TemplateDir() {
    }

    public TemplateDir(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public TemplateDir getParent() {
        return this.parent;
    }

    public void setParent(TemplateDir parent) {
        this.parent = parent;
    }

    public List<TemplateDir> getChildrens() {
        return this.childrens;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setChildrens(List<TemplateDir> childrens) {
        this.childrens = childrens;
    }

    public static long getSerialversionuid() {
        return 1L;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Date getInputDate() {
        return this.inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"parent"});
        if (this.parent != null) {
            map.put("parentId", CommUtil.obj2map(this.parent, new String[] {"id", "title"}));
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "title"}));
        }
        return map;
    }
}
