package com.eastinno.otransos.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.tools.AutoChangeLink;
import com.eastinno.otransos.web.tools.widget.Html;

/**
 * @intro 动静态地址互换
 * @version v_0.1
 * @author lengyu
 * @since 2014年4月7日 下午7:51:51
 */
@Entity(name = "Disco_DynamicStaticPair")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(disInject = "id,vdate,status")
public class DynamicStaticPair extends TenantObject implements AutoChangeLink, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank(message = "URL不能为空")
    @Column(length = 200, unique = true)
    private String url;// 动态URL地址

    @Column(length = 200, unique = true)
    private String path;// 静态化后的URL地址

    @Column(length = 50)
    private String title;// 名称

    private Integer intervals;// 生存周期
    private Date vdate = new Date();

    private Integer status = Integer.valueOf(1);// 1启用0停用

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIntervals() {
        return this.intervals;
    }

    public void setIntervals(Integer intervals) {
        this.intervals = intervals;
    }

    public Date getVdate() {
        return this.vdate;
    }

    public void setVdate(Date vdate) {
        this.vdate = vdate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDynamicUrl() {
        String ret = this.url.trim();
        if (ret.charAt(0) == '[') {
            String[] us = ret.split("\\.");
            ret = "/" + us[0].substring(1) + ".java?cmd=" + us[1].substring(0, us[1].length() - 1);
        }
        return ret;
    }

    public String getStaticUrl() {
        String ret = StringUtils.hasLength(this.path) ? this.path.trim() : "";
        if (StringUtils.hasText(ret) && ret.charAt(0) == '[') {
            String[] us = ret.split("\\.");
            ret = "/" + us[0].substring(1) + "/" + us[1].substring(0, us[1].length() - 1) + ".shtml";
        }
        return ret;
    }

    @Override
    public String getStaticPath() {
        return this.getStaticUrl();
    }

    public AutoChangeLink[] getAutoChangeLinks() {
        if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1) {
            url = TenantContext.getTenant().getUrl() + url;
        }
        Integer r = Html.getInstance().handleModuleCmdMaxPage(this.url);
        if (r.intValue() < 0) {
            return new AutoChangeLink[] {this};
        }
        AutoChangeLink[] links = new AutoChangeLink[r.intValue()];
        for (int i = 0; i < links.length; i++) {
            final Integer page = i + 1;
            links[i] = new AutoChangeLink() {
                public String getDynamicUrl() {
                    String path1 = Html.getInstance().handleModuleCmdUrl(url);
                    path1 += path1.indexOf('?') >= 0 ? "&currentPage=" : "?currentPage=";
                    return path1 += page + "";
                }

                @Override
                public String getStaticPath() {
                    return this.getStaticUrl();
                }

                public String getStaticUrl() {
                    String path1 = Html.getInstance().handleModuleCmdHtml(url);
                    return path1 += page + ".shtml";
                }
            };
        }
        return links;
    }
}
