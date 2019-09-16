package com.eastinno.otransos.report.service;

import java.io.ByteArrayOutputStream;

import com.eastinno.otransos.report.domain.ReportInfo;

/**
 * 报表业务处理
 * 
 * @author maowei
 * @createDate 2013-6-19上午9:22:55
 */
public interface ReportService {

	/**
	 * 解析html dom结构生成对应的excel文件
	 * 
	 * @param htmldom
	 *            dom结构
	 * @param excelFileName
	 *            生成的excel文件名（可以带目录结构）
	 * @return
	 */
	Boolean html2Excel(String htmldom, String excelFileName);

	/**
	 * 解析html dom结构生成对应的PDF
	 * 
	 * @param htmldom
	 *            dom结构
	 * @param pdfFileName
	 *            生成的PDF文件名（可以带目录结构）
	 * @return
	 */
	Boolean html2Pdf(String htmldom, String pdfFileName);

	/**
	 * 解析html dom结构把每一个td单元格转化为对应的JAVA对象
	 * 
	 * @param htmldom
	 *            dom结构
	 * @return 转化后的报表对象
	 */
	ReportInfo html2Obj(String htmldom);

	/**
	 * svg格式的图表转为图形文件
	 * 
	 * @param svg
	 * @return 返回图形文件流
	 */
	ByteArrayOutputStream chart2Img(String svg, String type);
}
