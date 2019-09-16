package com.eastinno.otransos.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 行业信息
 * @version v0.1
 * @author maowei
 * @since 2014年5月19日 下午4:42:46
 */
@Entity(name = "Disco_Trade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(inject = "title,intro,parent,sequence,sn")
public class Trade implements IJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String code;

    @Column(length = 100)
    private String title;// 标题

    @Column(length = 200)
    private String intro;// 简介

    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Trade parent;// 父目录

    private Integer sequence = 1;// 显示顺序

    @Column(length = 200)
    private String dirPath;// 目录路径

    @OneToMany(mappedBy = "parent")
    private List<Trade> children = new ArrayList<Trade>();// 子目录

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"parent", "children"});
        if (parent != null) {
            map.put("parent", CommUtil.obj2map(parent, new String[] {"id", "sn", "title"}));
        }
        return map;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Trade getParent() {
        return parent;
    }

    public void setParent(Trade parent) {
        this.parent = parent;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public List<Trade> getChildren() {
        return children;
    }

    public void setChildren(List<Trade> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String fullTitlePath(String link, String separator) {
        String url = "<a href = " + link + "&tradeId=" + this.id + ">" + this.title + "</a>";
        if (this.parent != null) {
            return this.parent.fullTitlePath(link, separator) + separator + url;
        }
        return url;
    }

}
