package com.eastinno.otransos.security.domain;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.domain.Trade;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 租户信息
 * @version v0.1
 * @author maowei
 * @since 2014年5月19日 下午4:41:22
 */
@Entity(name = "Disco_SaaS_Tenant")
public class Tenant implements IJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    @NotBlank(message = "code不能为空")
    @Size(max = 32, min = 2, message = "长度必须在2-32个字符以内")
    private String code;// 公司简短名称，必须唯一，当用户还没有创建公司时，该值自动为用户名(ajax校验)

    @Column(nullable = false)
    private String title;// 公司名称，必须唯一(ajax校验)

    private Integer types;// 租户类型
    private String tel;// 电话号码
    private String address;// 详细地址
    private String url;// 租户一级域名多个域名使用逗号分隔

    @POLoad(name = "tradeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Trade trade;// 行业

    @POLoad(name = "regionId")
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemRegion region;// 所在地区

    private Date registerTime = new Date();// 注册时间

    private Date beginTime;// 开通时间

    private Date endTime;// 到期时间,该属性只有云平台系统超级管理员用户才能修改

    // @POLoad
    @ManyToOne(fetch = FetchType.LAZY)
    private User manager;// 管理员

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;// 创建者

    private Integer status = 0;// 状态，小于0为停用,0为未激活,1为已激活未审核,2为已经审核

    private String intro;// 简介

    private String logo;// Logo 租户管理员上传LOGO图片

    private Integer maxUser = 10;// 最大用户数,该属性只能由平台管理员修改

    private String checkInfo;// 审核信息
    private Date checkTime;// 审核时间,平台用户修改

    @ManyToOne(fetch = FetchType.LAZY)
    private User checker;// 审核人,平台用户

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<Role>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant parent;
    private String depthPath;//深度路径
    public Map<String, Object> toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this,
                new String[] {"manager", "belongTo", "region", "creator", "checker", "trade","parent","roles"});

        if (region != null) {
            map.put("region", CommUtil.obj2map(region, new String[] {"id", "sn", "title"}));
        }
        if (trade != null) {
            map.put("trade", trade.toJSonObject());
        }
        if (parent != null) {
            map.put("parent", CommUtil.obj2map(parent, new String[] {"id", "code", "title"}));
        }
        return map;
    }
    
    public Integer getTypes() {
		return types;
	}
    
	public void setTypes(Integer types) {
		this.types = types;
	}

	public Long getId() {
        return id;
    }
    
    public Tenant getParent() {
		return parent;
	}

	public void setParent(Tenant parent) {
		this.parent = parent;
	}

	public String getDepthPath() {
		return depthPath;
	}

	public void setDepthPath(String depthPath) {
		this.depthPath = depthPath;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SystemRegion getRegion() {
        return region;
    }

    public void setRegion(SystemRegion region) {
        this.region = region;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getChecker() {
        return checker;
    }

    public void setChecker(User checker) {
        this.checker = checker;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(Integer maxUser) {
        this.maxUser = maxUser;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addTenantRole(Role role) {
        for (Role r : this.roles) {
            if (r.getId().equals(role.getId())) {
                return;
            }
        }
        this.roles.add(role);
    }
    public void setDePath(){
    	String depthPath = "@"+this.code;
    	if(this.parent!=null){
    		depthPath = this.parent.getDepthPath()+depthPath;
    	}
    	this.depthPath=depthPath;
    }
    public void delTenantRole(Role role) {
        for (Role r : this.roles) {
            if (r.getId().equals(role.getId())) {
                this.roles.remove(r);
                return;
            }
        }
    }
}
