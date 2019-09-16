package com.eastinno.otransos.cms.utils;

/**
 * <b> Intro: 常量工具类 <b>
 * 
 * @Version v_0.1
 * @Author lengyu
 * @CreateDate 2012-11-26 下午2:59:37
 */
public class CmsConstant {
    /**
     * 上传文件存放目录
     */
    public final static String FILE_UPLOAD_PATH_TEM = "/WEB-INF/views/template";
   
    public final static Integer NEWS_DOC_STATE_ONE = 1;// 审核中
    public final static Integer NEWS_DOC_STATE_TWO = 2;// 已发表
  //书法大赛活动状态
	public static final Integer YIBAOMING=0;//已报名未上传作品
	public static final Integer YISHANGCHUAN=1;//已上传作品
	public static final Integer CSYIPINGFEN=2;//初赛已评分
	public static final Integer YIFUSAI=3;//已进入复赛
	public static final Integer CSGULI=4;//获得初赛鼓励奖
	public static final Integer CSCHUJU=5;//初赛已出局
	public static final Integer FSYIPINGFEN=6;//复赛已评分
	public static final Integer YIJUESE=7;//已进入决赛
	public static final Integer FSGULI=8;//已获得复赛鼓励奖
	public static final Integer FSCHUJU=9;//复赛已出局
}
