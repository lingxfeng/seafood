package com.eastinno.otransos.report.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.eastinno.otransos.report.domain.TableCellInfo;

/**
 * 报表工具类
 * 
 * @author maowei
 * @createDate 2013-6-21上午8:42:32
 */
public class ReportUtil {
	private static final Logger log = Logger.getLogger(ReportUtil.class);

	/**
	 * 初始化所有的内部样式表
	 * 
	 * @param el
	 * @return
	 */
	public static Map<String, StyleUtil> initGlobalStyle(Document el) {
		Map<String, StyleUtil> styleMap = new HashMap<String, StyleUtil>();
		Elements els = el.select("style");
		Matcher cssMatcher = Pattern.compile("(\\w+)\\s*[{]([^}]+)[}]").matcher(els.html());
		while (cssMatcher.find()) {
			String style = cssMatcher.group(2).trim();
			styleMap.put(cssMatcher.group(1).trim(), new StyleUtil(style));
		}
		return styleMap;
	}

	public static Map<String, Object> parseCellStyle(Element td, Map<String, StyleUtil> globalStyle) {
		// 样式级别 全局样式--->class id 样式--->行内样式 后进的覆盖先前的原则
		Map<String, Object> map = new HashMap<String, Object>();

		/*---------------------------------全局样式-----------------------------*/
		if (!globalStyle.isEmpty() && globalStyle.size() > 0) {
			StyleUtil table = globalStyle.get("table");
			if (table != null) {
				// TODO....
			}
		}
		/*---------------------------------表头样式-----------------------------*/
		StyleUtil theadStyle = null;
		Element thead = searchUpThead(td);
		if (thead != null) {
			String theadStr = thead.attr("style");
			if (StringUtils.isNotEmpty(theadStr)) {
				theadStyle = new StyleUtil().style2Map(theadStr);
			}
		}
		if (theadStyle != null) {
			Colour bgColor = theadStyle.getBackgroundColor();
			if (bgColor != null) {
				map.put(StyleUtil.BG_COLOR, bgColor);
			}
		}

		/*---------------------------------tr行内样式-----------------------------*/
		StyleUtil trStyle = null;
		String trStyleStr = td.parent().attr("style");
		if (StringUtils.isNotEmpty(trStyleStr)) {
			trStyle = new StyleUtil().style2Map(trStyleStr);
		}
		if (trStyle != null) {
			Colour bgColor = trStyle.getBackgroundColor();
			Integer height = trStyle.getHeight();
			if (bgColor != null) {
				map.put(StyleUtil.BG_COLOR, bgColor);
			}
			if (height != null) {
				map.put(StyleUtil.HEIGHT, height);
			}
		}

		/*---------------------------------td行内样式-----------------------------*/
		StyleUtil tdStyle = null;
		String tdStyleStr = td.attr("style");
		if (StringUtils.isNotEmpty(tdStyleStr)) {
			tdStyle = new StyleUtil().style2Map(tdStyleStr);
		}
		if (tdStyle != null) {
			Integer width = tdStyle.getWidth();
			Integer height = tdStyle.getHeight();
			Colour bgColor = tdStyle.getBackgroundColor();
			Object[] border = tdStyle.getDefaultBorder();
			if (width != null) {
				map.put(StyleUtil.WIDTH, width);
			}
			if (height != null) {
				map.put(StyleUtil.HEIGHT, height);
			}
			if (bgColor != null) {
				map.put(StyleUtil.BG_COLOR, bgColor);
			}
			map.put(StyleUtil.BORDER, border);
		}
		return map;
	}

	/**
	 * 从一个节点向上查找thead
	 * 
	 * @param td
	 * @return
	 */
	public static Element searchUpThead(Element td) {
		Element tr = td.parent();
		while (!"tr".equals(tr.tagName())) {
			tr = tr.parent();
		}
		Element thead = tr.parent();
		while (!"thead".equals(thead.tagName())) {
			if ("table".equals(thead.tagName())) {
				thead = null;
				break;
			}
			thead = thead.parent();
		}
		return thead;
	}

	/**
	 * 表头单元格样式的设定 (边框颜色及粗线\背景颜色\宽高\)
	 */
	public static WritableCellFormat setCellStyle(TableCellInfo cell) {
		Map<String, Object> style = cell.getStyle();
		// WritableFont.createFont("宋体")：设置字体为宋体
		// 10设置字体大小
		// WritableFont.BOLD设置字体加粗（BOLD：加粗 NO_BOLD：不加粗）
		// false：设置非斜体
		// UnderlineStyle.NO_UNDERLINE：没有下划线
		WritableFont font = null;
		if (cell.getThead()) {
			font = new WritableFont(WritableFont.createFont(StyleUtil.FONT_FAMILY), StyleUtil.HEAD_FONT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		} else {
			font = new WritableFont(WritableFont.createFont(StyleUtil.FONT_FAMILY), StyleUtil.body_font_size, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
		}

		WritableCellFormat cellFormat = new WritableCellFormat(NumberFormats.TEXT);
		try {
			cellFormat.setAlignment(Alignment.LEFT);
			cellFormat.setFont(font);// 添加字体设置

			// 背景色
			Colour colour = (Colour) style.get(StyleUtil.BG_COLOR);
			if (colour != null) {
				cellFormat.setBackground(colour);
			}
			// thead背景色
			if (cell.getThead()) {
				cellFormat.setBackground(StyleUtil.HEAD_DEFAULT_COLOUR);
			}

			// 单元格宽高

			// 设置表头表格边框样式 整个表格线为粗细、黑色
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			cellFormat.setAlignment(Alignment.CENTRE);// 单元格内容水平居中
			cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);// 单元格内容垂直居中
		} catch (WriteException e) {
			log.error("表头单元格样式设置失败！");
		}
		return cellFormat;
	}

	/**
	 * 设置单元格宽高
	 * 
	 * @param sheet
	 *            表空间对象
	 * @param cell
	 *            单元格对象
	 */
	public static void setCellWidthHeight(WritableSheet sheet, TableCellInfo cell) {
		try {
			Integer width = (Integer) cell.getStyle().get(StyleUtil.WIDTH);
			Integer height = (Integer) cell.getStyle().get(StyleUtil.HEIGHT);
			if (height != null) {
				// jxl中的行高单位为像素值*20
				sheet.setRowView(cell.getY(), height * 20);// 设置行高
			}
			if (width != null) {
				// jxl中的列宽单位为像素值*6.9
				sheet.setColumnView(cell.getX(), (int) Math.round(width / 6.9));// 设置列宽
			}
		} catch (RowsExceededException e) {
			e.printStackTrace();
		}
	}
}
