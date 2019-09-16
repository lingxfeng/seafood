package com.eastinno.otransos.cms.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @intro 模版管理
 * @verion 1.0
 * @author nsz
 * @since 2014年5月7日 上午10:07:11
 */
@Entity(name = "Disco_SaaS_CMS_TemplateFile")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(disInject = "serialVersionUID,inputTime")
public class TemplateFile extends TenantObject {

    @Field(gener = false)
    private static final long serialVersionUID = 1339584693515524277L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 100)
    @NotBlank(message = "该字段不能为空")
    @Size(max = 100, message = "长度必须在100个字符以内")
    private String title;

    @ManyToOne
    @POLoad(name = "dirId")
    @Field(name = "模板分类", validator = @Validator(name = "required"))
    private TemplateDir dir;
    private String path;

    @Column(length = 200)
    @Size(max = 200, message = "长度必须在200个字符以内")
    private String description;
    private Date inputTime = new Date();

    private Integer status = Integer.valueOf(1);
    private String dirPath;
    private Integer sequence = Integer.valueOf(1);

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getInputTime() {
        return this.inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"dir", "tenant"});
        if (this.dir != null) {
            map.put("dir", CommUtil.obj2map(this.dir, new String[] {"id", "sn", "title"}));
        }
        if (this.tenant != null) {
            map.put("tenant", CommUtil.obj2map(this.tenant, new String[] {"id", "title", "code"}));
        }
        return map;
    }

    public TemplateDir getDir() {
        return this.dir;
    }

    public void setDir(TemplateDir dir) {
        this.dir = dir;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDirPath() {
        return this.dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public static long getSerialversionuid() {
        return 1339584693515524277L;
    }
}
