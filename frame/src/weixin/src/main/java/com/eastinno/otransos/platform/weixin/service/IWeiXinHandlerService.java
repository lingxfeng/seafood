package com.eastinno.otransos.platform.weixin.service;

import javax.servlet.http.HttpServletRequest;

import com.eastinno.otransos.platform.weixin.bean.ReqMessage;
import com.eastinno.otransos.platform.weixin.bean.RespMessage;
import com.eastinno.otransos.platform.weixin.domain.Follower;

/**
 * 处理微信平台推送过来的消息服务
 * 
 * @author maowei
 * @createDate 2013-11-27下午1:49:56
 */
public interface IWeiXinHandlerService {
    /**
     * 文本内容的消息处理
     * 
     * @param msg
     * @return
     */
    RespMessage textHandler(ReqMessage msg,HttpServletRequest req);

    /**
     * 地理位置类型的消息处理
     * 
     * @param msg
     * @return
     */
    RespMessage locationHandler(ReqMessage msg);

    /**
     * 图片类型的消息处理
     * 
     * @param msg
     * @return
     */
    RespMessage imageHandler(ReqMessage msg);

    /**
     * 链接类型的消息处理
     * 
     * @param msg
     * @return
     */
    RespMessage linkHandler(ReqMessage msg);

    /**
     * 语音类型的消息处理
     * 
     * @param msg
     * @return
     */
    RespMessage voiceHandler(ReqMessage msg,HttpServletRequest req);

    /**
     * 关注事件处理
     * @param msg
     * @return
     */
    RespMessage subscribeHandler(ReqMessage msg,HttpServletRequest req);
    /**
     * 取消关注事件处理
     * @param msg
     * @return
     */
    RespMessage unsubscribeHandler(ReqMessage msg);
    /**
     * 菜单点击事件处理
     * @param msg
     * @return
     */
    RespMessage clickHandler(ReqMessage msg,HttpServletRequest req);
    

    /**
     * 微信所有动作处理的入口
     * 
     * @param req
     * @return 返回给微信服务器的XML数据
     */
    String parseHandler(HttpServletRequest req);
    /**
     * 关注时间后续处理
     * @param follower
     */
    void subscribeAfter(Follower follower);
    /**
     * 扩展点击事件
     * @param msg
     * @return
     */
    RespMessage clickHandlerAfter(ReqMessage msg);
    int getShareSto();//获取分享加分
	int getSignatureSto();//获取签到加分
	int getFirstfollowSto();//获取首次关注加分
	int getCjjSto();//抽奖扣积分
}
