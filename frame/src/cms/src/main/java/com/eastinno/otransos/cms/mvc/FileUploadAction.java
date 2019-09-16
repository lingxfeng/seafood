package com.eastinno.otransos.cms.mvc;

import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.application.core.service.IAttachmentService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年12月4日 下午12:47:13
 * @Intro 文件上传
 */
@Action
public class FileUploadAction extends SaaSCMSBaseAction {
    private IAttachmentService service;

    public IAttachmentService getService() {
        return service;
    }

    public void setService(IAttachmentService service) {
        this.service = service;
    }

    /**
     * 处理上传文件
     * 
     * @param form
     * @param module
     * @return
     */
    @Override
    public Page doInit(WebForm form, Module module) {
        String data = "";
        try {
            Map<String, FileItem> map = form.getFileElement();
            FileItem item = (FileItem) map.get(UploadFileConstant.FILE_UPLOAD_FIELDNAME);
            if (item != null) {
                String fileName = item.getName();
                if (!StringUtils.hasText(fileName)) {
                    this.addError("imgFile", "请选择文件");
                } else {
                    String path = this.parseUploadFileItem(form, item);
                    if (!hasErrors() && StringUtils.hasText(path)) {
                        data = "{\"error\" : 0,\"url\" : \"" + path + "\"}";
                    } else {
                        data = "{\"error\" : 1,\"message\" : \"" + this.getErrors().get(UploadFileConstant.FILE_UPLOAD_FIELDNAME) + "\"}";
                    }
                }
            }
            ActionContext.getContext().getResponse().setCharacterEncoding("utf-8");
            ActionContext.getContext().getResponse().getWriter().write(data);
        } catch (Exception e) {
            e.printStackTrace();
            data = "{\"error\" : 1,\"message\" : \"失败\"}";
        }
        return Page.nullPage;
    }

    /**
     * 对上传文件进行一系列处理
     * 
     * @param form
     * @param item
     * @return
     */
    private String parseUploadFileItem(WebForm form, FileItem item) {
        String fileName = item.getName();
        String dir = ActionContext.getContext().getRequest().getParameter("dir");// 客户端上传文件的类型
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().replace(".", "");
        String ext = "";// 合法的文件后缀名
        Integer fileType = 0;// //上传附件类型 1附件 2媒体文件 3FLASH 4图片
        // 图片
        if (UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE.equals(dir)) {
            ext = FileUtil.IMAGE_FILE_EXT;
            fileType = UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE_NM;
        }// FLASH
        else if (UploadFileConstant.FILE_UPLOAD_TYPE_FLASH.equals(dir)) {
            ext = "swf";
            fileType = UploadFileConstant.FILE_UPLOAD_TYPE_FLASH_NM;
        }// flv
        else if (UploadFileConstant.FILE_UPLOAD_TYPE_FLV.equals(dir)) {
            ext = "flv";
            fileType = UploadFileConstant.FILE_UPLOAD_TYPE_FLASH_NM;
        } else if (UploadFileConstant.FILE_UPLOAD_TYPE_FILE.equals(dir)) {// 电子文档(附件)
            ext = FileUtil.IMAGE_FILE_EXT + FileUtil.ATTACHE_FILE_EXT + FileUtil.VIDIO_FILE_EXT + FileUtil.AUDIO_FILE_EXT
                    + FileUtil.ELDOC_FILE_EXT;
            fileType = UploadFileConstant.FILE_UPLOAD_TYPE_FILE_NM;
        } else if (UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA.equals(dir)) {// 媒体文件
            // 如果上传文件是视频
            ext = FileUtil.VIDIO_FILE_EXT + FileUtil.AUDIO_FILE_EXT;
            if (FileUtil.VIDIO_FILE_EXT.contains(suffix)) {
                fileType = UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE_NM;
            } else
            // 如果上传文件是音频
            if (FileUtil.AUDIO_FILE_EXT.contains(suffix)) {
                fileType = UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA_NM;
            } else {
                // 其它上传的媒体文件则约定默认为音频文件
                fileType = UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA_NM;
            }
        } else {
            // 从其它位置非kindeditor中上传的文件则约定默认为附件（电子文档）
            ext = FileUtil.IMAGE_FILE_EXT;
            fileType = UploadFileConstant.FILE_UPLOAD_TYPE_FILE_NM;
        }

        // 修改图片上传路径
        String path = "";
        String fieldName = UploadFileConstant.FILE_UPLOAD_FIELDNAME;
        if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
            ITenantObject co = (ITenantObject) UserContext.getUser();
            String p = UploadFileConstant.FILE_UPLOAD_PATH + "/" + co.getTenant().getCode() + "/kindEditor";
            path = FileUtil.uploadFile(form, fieldName, p, ext);
        }
        if (!hasErrors() && StringUtils.hasText(path)) {
            // 保存上传文件相关信息到数据库
            this.addAttachment(form, path, fileType);
        }
        return path;
    }

    private void addAttachment(WebForm form, String path, Integer fileType) {
        Map<String, FileItem> map = form.getFileElement();
        FileItem item = (FileItem) map.get(UploadFileConstant.FILE_UPLOAD_FIELDNAME);
        Long fileSize = item.getSize();
        String fileName = item.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().replace(".", "");

        Attachment file = new Attachment();
        file.setPath(path);
        file.setFileName(path.substring(path.lastIndexOf("/") + 1));
        file.setCreateUser(UserContext.getUser());
        file.setExt(suffix);
        file.setLength(fileSize);
        file.setTypes(fileType);
        file.setOldName(fileName);
        this.service.addAttachment(file);
    }

    /**
     * 在线浏览上传文件 (Kindeditor富文本编辑器中调用)
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doShowByKindeditor(WebForm form, Module module) {
        String dir = CommUtil.null2String(form.get("dir"));
        if ("image".equals(dir)) {
            // 浏览上传图片
        } else if ("image".equals(dir)) {
            // 浏览上传false
        }
        return null;
    }

    /**
     * 在线浏览上传文件 (后台Extjs中调用)
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doShowByExtjs(WebForm form, Module module) {
        String dir = CommUtil.null2String(form.get("dir"));
        if ("image".equals(dir)) {
            // 浏览上传图片
        } else if ("image".equals(dir)) {
            // 浏览上传false
        }
        return null;
    }

}
