package com.eastinno.otransos.cms.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 专题活动
 * @version v0.1
 * @author maowei
 * @since 2014年6月8日 下午4:12:44
 */
@Entity(name = "Disco_SaaS_CMS_SpecialTopic")
public class SpecialTopic extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    @Column(unique = true, length = 32)
    @NotBlank(message = "code不能为空")
    @Size(max = 32, min = 2, message = "长度必须在20个字符以内")
    private String code;
    private String title;
    private String description;
    private String logo;
    private String banner;
    private String intro;
    private Date inputTime;

    @ManyToOne
    private TemplateFile tpl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subject")
    private List<SpecialTopicDir> dirs = new ArrayList<SpecialTopicDir>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public TemplateFile getTpl() {
        return tpl;
    }

    public void setTpl(TemplateFile tpl) {
        this.tpl = tpl;
    }

    public List<SpecialTopicDir> getDirs() {
        return dirs;
    }

    public void setDirs(List<SpecialTopicDir> dirs) {
        this.dirs = dirs;
    }

    @Override
    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tpl", "dirs"});
        if (tpl != null)
            map.put("tpl", CommUtil.obj2map(this.tpl, new String[] {"id", "title"}));
        return map;
    }

}
