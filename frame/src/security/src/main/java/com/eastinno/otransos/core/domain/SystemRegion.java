package com.eastinno.otransos.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 系统地区信息(省市区县)
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2007年01月4日 上午11:12:49
 * @Intro
 */
@Entity(name = "Disco_RegionInfo")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(inject = "sn,title,parent,sequence,englishName,spell,shortSpell")
public class SystemRegion implements Serializable, IJsonObject {
    private static final long serialVersionUID = -7966633830261753869L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Field(validator = @Validator(name = "string", value = "blank;trim;max:100", required = true))
    private String sn;

    @Field(validator = @Validator(name = "string", value = "blank;trim;max:200", required = true))
    private String title;

    @Column(length = 100)
    private String englishName;

    @Column(length = 100)
    private String spell;

    @Column(length = 50)
    private String shortSpell;

    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_sn", referencedColumnName = "sn")
    private SystemRegion parent;

    private String path;
    private Integer lev;
    private Integer sequence = Integer.valueOf(1);

    private Date inputTime = new Date();
    private String inputUser;
    private Integer status = Integer.valueOf(0);

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<SystemRegion> children = new ArrayList<SystemRegion>();
    private String intro;

    public Object toJSonObject() {
        Map map = CommUtil.obj2map(this, new String[] {"id", "sn", "title"});
        return map;
    }

    public SystemRegion() {
    }

    public SystemRegion(Long id, String sn, String title) {
        this.id = id;
        this.sn = sn;
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInputTime() {
        return this.inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getInputUser() {
        return this.inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public Integer getLev() {
        return this.lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public SystemRegion getParent() {
        return this.parent;
    }

    public void setParent(SystemRegion parent) {
        this.parent = parent;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SystemRegion> getChildren() {
        return this.children;
    }

    public void setChildren(List<SystemRegion> children) {
        this.children = children;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : super.hashCode();
    }

    public boolean equals(Object obj) {
        if ((obj == null) || (!(obj instanceof SystemRegion)))
            return false;
        SystemRegion o = (SystemRegion) obj;
        if ((this.id != null) && (o.getId() != null))
            return this.id.equals(o.getId());
        return super.equals(obj);
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEnglishName() {
        return this.englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSpell() {
        return this.spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getShortSpell() {
        return this.shortSpell;
    }

    public void setShortSpell(String shortSpell) {
        this.shortSpell = shortSpell;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getFullTitle() {
        String s = this.title;
        if (getParent() != null)
            s = getParent().getFullTitle() + "->" + s;
        return s;
    }

    public String loadFullTitle(String seprator) {
        String s = this.title;
        if (this.parent != null) {
            return this.parent.loadFullTitle(seprator) + (StringUtils.hasLength(seprator) ? seprator : "-") + s;
        }
        return s;
    }

    public String loadFullTitle(String link, String seprator) {
        String s = "<a href = " + link + "&cityId=" + this.id + ">" + this.title + "</a>";
        if (this.parent != null) {
            return this.parent.loadFullTitle(link, seprator) + (StringUtils.hasLength(seprator) ? seprator : "-") + s;
        }
        return s;
    }

    public List<SystemRegion> getNormalChildren() {
        List ret = new ArrayList();
        for (SystemRegion dir : this.children) {
            if (dir.status.intValue() == 0) {
                ret.add(dir);
            }
        }
        return ret;
    }

    public String toString() {
        if (getParent() != null) {
            return getParent().toString() + this.title;
        }
        return this.title;
    }
}
