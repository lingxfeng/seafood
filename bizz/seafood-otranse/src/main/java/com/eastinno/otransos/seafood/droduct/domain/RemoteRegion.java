package com.eastinno.otransos.seafood.droduct.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.domain.SystemRegion;

/**
 * 偏远地区实体类
 * @author wb
 *
 */
@Entity(name="Disco_Shop_RemoteRegion")
public class RemoteRegion {	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(nullable=false, unique=true)
	private Long id;	//主键标示		
	@OneToOne(fetch = FetchType.LAZY)
	private SystemRegion systemRegion;	//区域信息记录
	
	@POLoad(name="regionClassId")
	@ManyToOne(fetch=FetchType.LAZY)
	private RegionClass regionClass;
	
	private Date createDate;	//记录创建时间
	private Date modifyDate;	//记录修改时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public SystemRegion getSystemRegion() {
		return systemRegion;
	}
	public void setSystemRegion(SystemRegion systemRegion) {
		this.systemRegion = systemRegion;
	}
	
	public RegionClass getRegionClass() {
		return regionClass;
	}
	public void setRegionClass(RegionClass regionClass) {
		this.regionClass = regionClass;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
