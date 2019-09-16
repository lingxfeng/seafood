package com.eastinno.otransos.report.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 报表中每一个单元格的相关信息
 * 
 * @author maowei
 * @createDate 2013-6-10下午4:51:39
 */
public class TableCellInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer x;// X坐标
	private Integer y;// Y坐标
	private Map<String, Object> style;// 单元格样式
	private Integer colspan;// 合并列情况
	private Integer rowspan;// 合并行情况
	private byte[] content;// 单元格内容
	private Boolean thead = false;// 是否表头 如果是表头则默认约定一套样式 如字体14PX 加粗居中显示
	private Integer type = 1;// 单元格数据类型 1文本(默认) 2图表 3图标

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Boolean getThead() {
		return thead;
	}

	public void setThead(Boolean thead) {
		this.thead = thead;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, Object> getStyle() {
		return style;
	}

	public void setStyle(Map<String, Object> style) {
		this.style = style;
	}

	public Integer getColspan() {
		return colspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}

	public Integer getRowspan() {
		return rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
