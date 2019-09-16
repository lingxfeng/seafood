package com.eastinno.otransos.platform.weixin.bean;

/**
 * 回复消息 基类 开发账号->普通用户
 * 
 * @version 2.0
 * @author lengyu
 * @date 2014年9月8日-下午9:59:58
 */
public class RespMessage {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private int FuncFlag = 0;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public int getFuncFlag() {
        return FuncFlag;
    }

    public void setFuncFlag(int funcFlag) {
        FuncFlag = funcFlag;
    }

}