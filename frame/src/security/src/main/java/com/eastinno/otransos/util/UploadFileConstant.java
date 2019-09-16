package com.eastinno.otransos.util;

/**
 * <b> Intro: 常量工具类 <b>
 * 
 * @Version v_0.1
 * @Author lengyu
 * @CreateDate 2012-11-26 下午2:59:37
 */
public class UploadFileConstant {
    /**
     * 上传文件存放目录
     */
    public final static String FILE_UPLOAD_PATH = "/static/upload";
    public final static String FILE_UPLOAD_PATH_TEM = "/WEB-INF/views/template";
    public final static String FILE_UPLOAD_TYPE_IMAGE = "img";
    public final static String FILE_UPLOAD_TYPE_FLASH = "flash";
    public final static String FILE_UPLOAD_TYPE_FLV = "flv";
    public final static String FILE_UPLOAD_TYPE_MEDIA = "media";
    public final static String FILE_UPLOAD_TYPE_FILE = "file";
    public final static String FILE_UPLOAD_FIELDNAME = "imgFile";
    // 上传附件类型 1附件 2媒体文件 3FLASH 4图片
    public final static int FILE_UPLOAD_TYPE_IMAGE_NM = 4;
    public final static int FILE_UPLOAD_TYPE_FLASH_NM = 3;
    public final static int FILE_UPLOAD_TYPE_MEDIA_NM = 2;
    public final static int FILE_UPLOAD_TYPE_FILE_NM = 1;

    public final static Integer NEWS_DOC_STATE_ONE = 1;// 审核中
    public final static Integer NEWS_DOC_STATE_TWO = 2;// 已发表

}
