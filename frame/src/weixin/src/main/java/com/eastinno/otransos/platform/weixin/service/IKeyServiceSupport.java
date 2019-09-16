package com.eastinno.otransos.platform.weixin.service;

import com.eastinno.otransos.platform.weixin.bean.RespTextMessage;

/**
 * 关键字扩展接口服务
 * 
 * @version 2.0
 * @author lengyu
 * @date 2014年9月8日-下午9:58:10
 */
public interface IKeyServiceSupport {

    /**
     * 获取接口关键字，例如："翻译"
     * 
     * @return
     */
    String getKey();

    /**
     * 针对关键字的功能处理方法
     * 
     * @param content 请求文本
     * @param defaultMessage 默认回复此文本消息
     * @param request 请求
     * @return
     */
    String excute(String content, RespTextMessage defaultMessage);
}
