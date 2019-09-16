package com.eastinno.otransos.report.util;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;

import org.apache.commons.lang3.StringUtils;

public class StyleUtil {
	public static final String BG_COLOR = "background-color";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	public static final String BORDER = "border";
	public static final String BORDER_TOP = "border-top";
	public static final String BORDER_LEFT = "border-left";
	public static final String BORDER_RIGHT = "border-right";
	public static final String BORDER_bottom = "border-bottom";
	// 表格默认边框线样式
	// Border.ALL:上下左右都设置边框; BorderLineStyle.THIN: 细边框;
	// jxl.format.Colour.BLACK:黑颜色
	public static final Object[] default_Border = new Object[] { Border.ALL, BorderLineStyle.THIN, jxl.format.Colour.BLACK };

	/********************* -------------------表头样式预定义------------------- ********************/
	public static final Integer HEAD_FONT_SIZE = 12;// 表头字体大小
	public static final Colour HEAD_DEFAULT_COLOUR = Colour.GRAY_25;// 浅灰色表头背景

	public static final Integer body_font_size = 10;// 报表正文字体大小
	public static final String FONT_FAMILY = "宋体";

	public static final String TEXT_ALIGN = "text-align";
	public static final String VERTICAL_ALIGN = "vertical-align";
	private static Map<String, String> styleMap = new HashMap<String, String>();

	public StyleUtil() {
	}

	public StyleUtil(String style) {
		this.style2Map(style);
	}

	/**
	 * @param style
	 * @return
	 * @return
	 */
	public StyleUtil style2Map(String style) {
		if (StringUtils.isNotBlank(style)) {
			String[] styles = style.split(";");
			List<String> strList = Arrays.asList(styles);
			if (styleMap.size() > 0) {
				styleMap.clear();
			}
			for (String str : strList) {
				String[] kv = str.split(":");
				styleMap.put(kv[0].trim(), kv[1].trim());
			}
		}
		return this;
	}

	/**
	 * @return
	 */
	public Integer getHeight() {
		String h = styleMap.get(StyleUtil.HEIGHT);
		if (StringUtils.isNotBlank(h)) {
			h = h.trim();
			return Integer.parseInt(h.substring(0, h.length() - 2));
		}
		return null;
	}

	/**
	 * @return
	 */
	public Integer getWidth() {
		String w = styleMap.get(StyleUtil.WIDTH);
		if (StringUtils.isNotBlank(w)) {
			w = w.trim();
			return Integer.parseInt(w.substring(0, w.length() - 2));
		}
		return null;
	}

	/**
	 * @return
	 */
	public Colour getBackgroundColor() {
		String bgColor = styleMap.get(StyleUtil.BG_COLOR);
		if (StringUtils.isNotBlank(bgColor)) {
			return StyleUtil.getNearestColour(bgColor.trim());
		}
		return null;
	}

	/**
	 * @return
	 */
	public Object[] getDefaultBorder() {
		return default_Border;
	}

	/**
	 * 将十六进制颜色转换为jxl可用的颜色
	 * 
	 * @param strColor
	 *            #ff0000 or #ff334455
	 * @return
	 */
	public static Colour getNearestColour(String strColor) {
		Colour color = null;
		try {
			Color cl = Color.decode(strColor);
			Colour[] colors = Colour.getAllColours();
			if ((colors != null) && (colors.length > 0)) {
				Colour crtColor = null;
				int[] rgb = null;
				int diff = 0;
				int minDiff = 999;
				for (int i = 0; i < colors.length; i++) {
					crtColor = colors[i];
					rgb = new int[3];
					rgb[0] = crtColor.getDefaultRGB().getRed();
					rgb[1] = crtColor.getDefaultRGB().getGreen();
					rgb[2] = crtColor.getDefaultRGB().getBlue();

					diff = Math.abs(rgb[0] - cl.getRed()) + Math.abs(rgb[1] - cl.getGreen()) + Math.abs(rgb[2] - cl.getBlue());
					if (diff < minDiff) {
						minDiff = diff;
						color = crtColor;
					}
				}
			}
			if (color == null)
				color = Colour.BLACK;
		} catch (Exception e) {
			color = Colour.BLACK;
			e.printStackTrace();
		}

		return color;
	}
}
