package com.eastinno.otransos.platform.weixin.bean;

import java.io.Serializable;

/**
 * 微信平台推送过来的消息实体
 * 
 * @author maowei
 * @createDate 2013-11-27下午1:52:43
 */
public class ReqMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ToUserName;
    private String FromUserName;
    private String CreateTime;// 创建日期
    private String MsgType = "text";// 消息类型
    private String MsgId;
    private String Content;// 文本消息
    private String PicUrl;// 图片消息
    // 位置消息
    private String Location_X;
    private String Location_Y;
    private String Scale;
    private String Label;
    // 链接消息
    private String Title;
    private String Description;
    private String Url;
    // 语音信息
    private String MediaId;
    private String Format;
    private String Recognition;
    // 事件
    private String Event;
    private String EventKey;
    private String Ticket;
    private String Latitude;
    private String Longitude;
    private String Precision;
    private String MsgID;
    private String TotalCount;
    private String FilterCount;
    private String Status;
    private String SentCount;
    private String ErrorCount;
    private String ThumbMediaId;
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


    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
    
    public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getRecognition() {
        return Recognition;
    }

    public void setRecognition(String recognition) {
        Recognition = recognition;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getMsgID() {
		return MsgID;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	}

	public String getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(String totalCount) {
		TotalCount = totalCount;
	}

	public String getFilterCount() {
		return FilterCount;
	}

	public void setFilterCount(String filterCount) {
		FilterCount = filterCount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSentCount() {
		return SentCount;
	}

	public void setSentCount(String sentCount) {
		SentCount = sentCount;
	}

	public String getErrorCount() {
		return ErrorCount;
	}

	public void setErrorCount(String errorCount) {
		ErrorCount = errorCount;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
}
