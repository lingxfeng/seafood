package com.eastinno.otransos.application.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.domain.User;

/**
 * @intro 上传文件、附件管理
 * @version v_0.1
 * @author lengyu
 * @since 2012-11-18 上午9:54:56
 */
@Entity(name = "Disco_Attachment")
public class Attachment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 100)
    @Size(max = 100, min = 2, message = "长度必须在2-100之间")
    private String fileName;// 文件名称
    private String oldName;// 原始文件名称

    @Column(length = 200)
    private String path;// 文件相对路径
    private String ext;// 文件后缀名
    private String icon;// 图形类文件缩略图(文件名为fileName_icon)

    @Column(length = 1000)
    private String description;// 简介

    private Long length;// 文件大小
    private Date createTime = new Date();// 上传日期

    @ManyToOne
    @POLoad(name = "createUserId")
    private User createUser;// 上传者
    private Integer status = 1;// 文件状态 1正常 0逻辑删除
    private String source;// 来源(如：文章内容上传、栏目图标、图片库等，自己取名)

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getDescription() {
        return description;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 附件是否是图形文件
     * 
     * @return
     */
    public boolean isImgFile() {
        return FileUtil.isImgageFile(this.getPath());
    }

    /**
     * 附件是否是音频文件
     * 
     * @return
     */
    public boolean isAudioFile() {
        return FileUtil.isAudioFile(this.getPath());
    }

    /**
     * 附件是否是视频文件
     * 
     * @return
     */
    public boolean isVideoFile() {
        return FileUtil.isVideoFile(this.getPath());
    }

    /**
     * 附件是否是电子文档
     * 
     * @return
     */
    public boolean isElDoc() {
        return FileUtil.isElDoc(this.getPath());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Integer types;// 上传附件类型 1附件 2媒体文件 3FLASH 4图片

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

}
