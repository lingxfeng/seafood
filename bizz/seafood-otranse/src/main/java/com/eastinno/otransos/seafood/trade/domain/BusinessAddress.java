package com.eastinno.otransos.seafood.trade.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;
/**
 * 商家发货地址
 * @author cl
 */
@Entity(name = "Disco_Shop_BusinessAddress")
public class BusinessAddress extends TenantObject implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String name;//地址名称
	private String contactName;//联系人名称
	private String mobile;//收货人手机
	@POLoad(name = "area_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private SystemRegion area;//县
	private String address;//地址
	@Column(length=6)
	private String zip;//邮编
	private String mobileTel;//电话号码
	
	private String company;// 单位或公司
	private String remark;//备注
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
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public SystemRegion getArea() {
		return area;
	}
	public void setArea(SystemRegion area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {""});
		return map;
	}
}
