package com.eastinno.otransos.cms.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;

@Entity(name = "Disco_SaaS_CMS_LinkImgType")
public class LinkImgType extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 20)
    @Size(max = 20, message = "长度必须在20个字符以内")
    @NotBlank(message = "该字段不能为空")
    private String title;

    private String text;

    @Column(unique = true, length = 32)
    @NotBlank(message = "code不能为空")
    @Size(max = 32, min = 2, message = "长度必须在20个字符以内")
    private String code;

    @OrderBy("sequence desc")
    @OneToMany(mappedBy = "type", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<LinkImgGroup> bannerPPTs = new ArrayList<LinkImgGroup>();

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

    public List<LinkImgGroup> getBannerPPTs() {
        return bannerPPTs;
    }

    public void setBannerPPTs(List<LinkImgGroup> bannerPPTs) {
        this.bannerPPTs = bannerPPTs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tenant"});

        if (this.tenant != null) {
            map.put("tenant", CommUtil.obj2map(this.tenant, new String[] {"id", "title", "code"}));
        }
        return map;
    }

}
