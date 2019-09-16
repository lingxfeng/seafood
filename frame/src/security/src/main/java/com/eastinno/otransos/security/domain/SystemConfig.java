package com.eastinno.otransos.security.domain;

import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.core.util.CommUtil;

/**
 * @intro 租户平台全局管理
 * @author nsz
 */
@Entity(name = "Disco_SaaS_SystemConfig")
public class SystemConfig extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(length = 50)
    @Size(max = 50, message = "长度必须在50字符以内")
    private String siteName;// 站点名称

    @Field(name = "域名")
    @Column(length = 50)
    @Size(max = 50, message = "长度必须在50字符以内")
    private String domain;

    @Column(length = 50)
    @Size(max = 50, message = "长度必须在50字符以内")
    private String webmaster;// 站点创建者

    @Column(length = 100)
    @Size(max = 100, message = "长度必须在100字符以内")
    @Email
    private String email;// 联系邮箱
    private String tel;// 联系电话

    @Column(length = 200)
    @Size(max = 200, message = "长度必须在200字符以内")
    private String copyright;// 版权信息

    private String bgStyle;// 网站的背景颜色或图片

    private String icp;// 备案号

    private String keywords;// 关键字词
    @Column(length = 500)
    @Size(max = 500, message = "长度必须在500字符以内")
    private String description;// 站点描述

    @Column(length = 2000)
    @Size(max = 2000, message = "长度必须在2000字符以内")
    private String jsonStr;// 额外的业务字段用JSON字符串来存储

    @Transient
    private JSONObject jsonObj;// JSON对象

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWebmaster() {
        return webmaster;
    }

    public void setWebmaster(String webmaster) {
        this.webmaster = webmaster;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCopyright() {
        return copyright;
    }

    public JSONObject getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getBgStyle() {
        return bgStyle;
    }

    public void setBgStyle(String bgStyle) {
        this.bgStyle = bgStyle;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
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

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"jsonStr", "tenant"});
        if (this.jsonStr != null) {
            Map mapjson = JSONObject.parseObject(this.jsonStr);
            Set<String> keys = mapjson.keySet();
            for (String k : keys) {
                map.put(k, mapjson.get(k));
            }
        }
        return map;
    }

    public Map<String, Object> toJsonMap() {
        Map<String, Object> mapjson = null;
        if (this.jsonStr != null) {
            mapjson = JSONObject.parseObject(this.jsonStr);
        }
        return mapjson;
    }
}
