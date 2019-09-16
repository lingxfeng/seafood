package com.eastinno.otransos.cms.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 系统公告
 * @version v0.1
 * @author maowei
 * @since 2014年6月8日 下午5:54:26
 */
@Entity(name = "Disco_SaaS_CMS_SystemAnnounce")
public class SystemAnnounce extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String title;
    private Date displayTime;
    private Date inputTime = new Date();
    private String types;// 类别
    private String url;// 外链地址

    @POLoad(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Boolean auditing;
    private Boolean elite;

    @Lob
    private String content;

    @Column(length = 300)
    private String intro;

    private Integer readTimes = 0;
    private String pic;

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"user"});
        if (user != null) {
            map.put("user", CommUtil.obj2map(user, new String[] {"id", "name", "trueName"}));
        }
        return map;
    }

    public String getStaticUrl() {
        if (StringUtils.hasLength(this.url))
            return this.url;
        else
            return "news.ejf?cmd=showSystemAnnounce&id=" + this.id;
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

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getAuditing() {
        return auditing;
    }

    public void setAuditing(Boolean auditing) {
        this.auditing = auditing;
    }

    public Boolean getElite() {
        return elite;
    }

    public void setElite(Boolean elite) {
        this.elite = elite;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
