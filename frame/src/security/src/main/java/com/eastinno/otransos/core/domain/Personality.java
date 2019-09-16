package com.eastinno.otransos.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ajax.IJsonObject;

@Entity(name = "Disco_Personality")
public class Personality implements IJsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @OneToOne
    private User user;
    private Integer maxTabs = Integer.valueOf(10);
    private Boolean singleTabMode = Boolean.valueOf(false);
    private Boolean iframe = Boolean.valueOf(false);
    private Boolean enableAnimate = Boolean.valueOf(true);
    private String homePage = "menu";
    private String lang;
    private String style = "default";
    private String viewMode = "standard";

    @Lob
    private String portals = "";
    private String commonFunctions;
    private Integer portalMode = Integer.valueOf(5);
    private String backgroundImg;

    public String getCommonFunctions() {
        return this.commonFunctions;
    }

    public void setCommonFunctions(String commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"user", "portals", "portalConfig"});
        List pc = getPortalConfig();
        if ((pc != null) && (pc.size() > 0)) {
            map.put("portalConfig", pc);
        }
        return map;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMaxTabs() {
        return this.maxTabs;
    }

    public void setMaxTabs(Integer maxTabs) {
        this.maxTabs = maxTabs;
    }

    public Boolean getSingleTabMode() {
        return this.singleTabMode;
    }

    public void setSingleTabMode(Boolean singleTabMode) {
        this.singleTabMode = singleTabMode;
    }

    public Boolean getIframe() {
        return this.iframe;
    }

    public void setIframe(Boolean iframe) {
        this.iframe = iframe;
    }

    public String getPortals() {
        return this.portals;
    }

    public void setPortals(String portals) {
        this.portals = portals;
    }

    public Boolean getEnableAnimate() {
        return this.enableAnimate;
    }

    public void setEnableAnimate(Boolean enableAnimate) {
        this.enableAnimate = enableAnimate;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLang() {
        if (this.lang == null)
            this.lang = Locale.getDefault().toString();
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<Map> getPortalConfig() {
        List list = null;
        if (StringUtils.hasLength(this.portals)) {
            String[] ps = this.portals.split("@@");
            list = new ArrayList();
            if ((ps != null) && (ps.length > 0)) {
                for (int i = 0; i < ps.length; i++) {
                    String[] en = ps[i].split(",");
                    if ((en != null) && (en.length > 0)) {
                        Map map = new HashMap();
                        for (int j = 0; j < en.length; j++) {
                            int index = en[j].indexOf(':');
                            if (index >= 0) {
                                String name = en[j].substring(0, index);
                                String value = "";
                                if (en[j].length() > index)
                                    value = en[j].substring(index + 1);
                                map.put(name, value);
                            }
                        }
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    public String getBackgroundImg() {
        return this.backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public Integer getPortalMode() {
        return this.portalMode;
    }

    public void setPortalMode(Integer portalMode) {
        this.portalMode = portalMode;
    }

    public void setPortalConfig(List<Map> s) {
        String v = "";
        if ((s != null) && (!s.isEmpty())) {
            for (Map m : s) {
                Iterator it = m.entrySet().iterator();
                String p = "";
                while (it.hasNext()) {
                    Map.Entry en = (Map.Entry) it.next();
                    p = p + en.getKey() + ":" + en.getValue() + ",";
                }
                if (p.length() > 2)
                    p = p.substring(0, p.length() - 1);
                v = v + p + "@@";
            }
        }
        setPortals(v);
    }

    public String getViewMode() {
        return this.viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }
}
