package com.eastinno.otransos.platform.weixin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @Title: 粉丝、微信关注者
 * @author maowei
 * @date 2014-05-21 00:53:47
 * @version V1.0
 */
@Entity
@Table(name = "Disco_WeiXin_Group")
public class FollowerGroup implements IJsonObject{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @NotBlank(message = "组别名称不能为空")
    @Size(max = 30, min = 2, message = "组别名称长度必须在2-30之间")
	private String name;//分组姓名
    @NotBlank(message = "组别编码不能为空")
    @Size(max = 50, min = 2, message = "组别编码长度必须在2-50之间")
    private String code;//分组编码唯一切不可更改
	private Integer wxGroupId;//微信端分组Id
	@POLoad(name = "tenantId")
    @ManyToOne(fetch = FetchType.LAZY)
    protected Tenant tenant;
	@POLoad(name = "accountId")
	@ManyToOne(fetch = FetchType.LAZY)
    private Account account;
	private Integer count = 0;//该组下的粉丝数

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
 
	public Integer getWxGroupId() {
		return wxGroupId;
	}

	public void setWxGroupId(Integer wxGroupId) {
		this.wxGroupId = wxGroupId;
	}

	public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public Object toJSonObject() {
		 Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tenant"});
	     return map;
	}
    
}
