package com.eastinno.otransos.cms.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @intro 专题分类
 * @version v0.1
 * @author maowei
 * @since 2014年6月8日 下午4:14:41
 */
@Entity(name = "Disco_SaaS_CMS_SpecialTopicDir")
public class SpecialTopicDir extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String title;
    private Integer sequence;
    @ManyToOne
    private SpecialTopic subject;

    @OrderBy("sequence")
    @OneToMany(fetch = FetchType.LAZY)
    private List<NewsDoc> docs = new ArrayList<NewsDoc>();

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public SpecialTopic getSubject() {
        return subject;
    }

    public void setSubject(SpecialTopic subject) {
        this.subject = subject;
    }

    public List<NewsDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<NewsDoc> docs) {
        this.docs = docs;
    }
}
