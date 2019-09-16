package com.eastinno.otransos.security.domain;

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
import javax.persistence.OrderBy;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;

/**
 * 公司部门信息
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年10月28日 下午8:09:25
 * @Intro
 */
@Entity(name = "Disco_SaaS_Department")
public class Department extends TenantObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 16)
    private String sn;// 部门编码

    @Column(length = 200)
    private String title;// 部门名称

    private Integer types;// 部门类别

    @Column(length = 1000)
    private String intro;// 部门简介

    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department parent;// 上级部门

    private String dirPath;// 用于递归查询的路径

    private String tel;// 部门电话

    private String fax;// 部门传真

    @OneToMany(mappedBy = "parent", cascade = javax.persistence.CascadeType.REMOVE)
    @OrderBy("sequence")
    private List<Department> children = new ArrayList<Department>();

    @ManyToOne(fetch = FetchType.LAZY)
    @POLoad
    private User manager;// 经理,负责人

    @ManyToOne(fetch = FetchType.LAZY)
    @POLoad
    private User admin;// 部门管理员

    private Integer sequence;

    private Boolean isOperation = false; // 是否发生了业务, 这个在入库单的审核中改变,

    public Object toJSonObject() {
        Map map = (Map) super.toJSonObject();
        /*
         * CommUtil.obj2mapExcept(this, new String[] { "parent", "children", "manager", "admin", "company" });
         */
        if (parent != null) {
            map.put("parent", CommUtil.obj2map(parent, new String[] {"id", "sn", "title"}));
        }
        map.remove("children");
        return map;
    }

    public Department() {

    }

    public Department(Long id, String title, String intro) {
        this.id = id;
        this.title = title;
        this.intro = intro;
    }

    private Integer status = 0;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Boolean getIsOperation() {
        return isOperation;
    }

    public void setIsOperation(Boolean isOperation) {
        this.isOperation = isOperation;
    }

}
