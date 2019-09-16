package com.eastinno.otransos.report.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表基本信息
 * 
 * @author maowei
 * @createDate 2013-6-21下午3:20:20
 */
public class ReportInfo {
	private Boolean hasDown;// 生成的excel是否可下载
	private String htmlDom;// 需解析的DOM片段
	private String password;// 密码值
	private Boolean hasPersist;// 生成的报表是否持久化
	private Boolean orientation = true;// 报表方向（true横向[默认]false纵向）
	private String fileName;// 生成的报表名称

	private List<TableCellInfo> cells = new ArrayList<TableCellInfo>();// 表格的单元格集合
	private List<TableCellInfo> thead = new ArrayList<TableCellInfo>();// 表格表头的单元格集合
	private Integer maxRow = 0;// 最格最大行
	private Integer theadRow = 0;// 表头最大行
	private Integer maxColumn = 0;// 表格最大列

	public Boolean getOrientation() {
		return orientation;
	}

	public void setOrientation(Boolean orientation) {
		this.orientation = orientation;
	}

	public String getHtmlDom() {
		return htmlDom;
	}

	public void setHtmlDom(String htmlDom) {
		this.htmlDom = htmlDom;
	}

	public Boolean getHasDown() {
		return hasDown;
	}

	public void setHasDown(Boolean hasDown) {
		this.hasDown = hasDown;
	}

	public Boolean getHasPersist() {
		return hasPersist;
	}

	public void setHasPersist(Boolean hasPersist) {
		this.hasPersist = hasPersist;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<TableCellInfo> getCells() {
		return cells;
	}

	public void setCells(List<TableCellInfo> cells) {
		this.cells = cells;
	}

	public List<TableCellInfo> getThead() {
		return thead;
	}

	public void setThead(List<TableCellInfo> thead) {
		this.thead = thead;
	}

	public Integer getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(Integer maxRow) {
		this.maxRow = maxRow;
	}

	public Integer getTheadRow() {
		return theadRow;
	}

	public void setTheadRow(Integer theadRow) {
		this.theadRow = theadRow;
	}

	public Integer getMaxColumn() {
		return maxColumn;
	}

	public void setMaxColumn(Integer maxColumn) {
		this.maxColumn = maxColumn;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
