package com.eastinno.otransos.platform.weixin.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.security.domain.TenantObject;

/**
 * @Title: 微信自定义菜单管理,只有服务号才有自定义菜单功能
 * @author maowei
 * @date 2014-05-21 00:53:47
 * @version V1.0
 */
@Entity
@Table(name = "Disco_WeiXin_Menu")
public class Menu extends TenantObject {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private String menuKey;
    private String type;// click or viewin or viewout
    private String url;// 如果view url不能为空
    private String orders;
    @POLoad(name="accountId")
    @ManyToOne
    private Account account;
    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu parent;
    @BatchSize(size = 15)
    @OrderBy(value = "sequence")
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Menu> children = new ArrayList<Menu>();
    private Integer sequence=1;
    @POLoad(name = "templateId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Template template;//消息模版
    @POLoad(name = "newsDirId")
    @ManyToOne(fetch = FetchType.LAZY)
    private NewsDir newsDir;//关联的栏目
    private Integer newsDocOrderBy=0;//文章查询条件
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public NewsDir getNewsDir() {
		return newsDir;
	}

	public void setNewsDir(NewsDir newsDir) {
		this.newsDir = newsDir;
	}

	public Integer getNewsDocOrderBy() {
		return newsDocOrderBy;
	}

	public void setNewsDocOrderBy(Integer newsDocOrderBy) {
		this.newsDocOrderBy = newsDocOrderBy;
	}
}
