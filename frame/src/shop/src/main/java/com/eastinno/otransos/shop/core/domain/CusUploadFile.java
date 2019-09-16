package com.eastinno.otransos.shop.core.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.TenantObject;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ajax.IJsonObject;
/**
 * 上传图片管理
 * @author nsz
 *
 */
@Entity(name = "Disco_Shop_CusUploadFile")
public class CusUploadFile extends TenantObject implements IJsonObject{
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	private String imgPath;//文件路径
	private Date createDate = new Date(); //上传时间
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;//上传人
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public Object toJSonObject() {
		Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"user"});
        return map;
	}
}
