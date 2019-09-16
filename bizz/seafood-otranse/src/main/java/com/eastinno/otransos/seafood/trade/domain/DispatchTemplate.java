package com.eastinno.otransos.seafood.trade.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 运单模版
 * @author cl
 *
 */
@Entity(name = "Disco_Shop_DispatchTemplate")
public class DispatchTemplate extends TenantObject implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 30)
	private String name;//模板名称
	private Double width;//宽
	private Double height;//高
	private String pic;//运单图片
	private String print;//打印项
	
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

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}
	
	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPrint() {
		return print;
	}

	public void setPrint(String print) {
		this.print = print;
	}

	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {""});
		return map;
	}
}
