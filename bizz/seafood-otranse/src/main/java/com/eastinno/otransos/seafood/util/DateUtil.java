package com.eastinno.otransos.seafood.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取日期工具类
 * 
 * @author Administrator
 *
 */
public class DateUtil {
	public static String getDateStr(Date date, String fomatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(fomatStr);
		String strDate = formatter.format(date);
		return strDate;
	}

	public static String getNowDay() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(now);
		return strDate;
	}

	public static String getNowMonth() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String strDate = formatter.format(now);
		return strDate;
	}

	public static String getNowYear() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String strDate = formatter.format(now);
		return strDate;
	}

	public static String getDateStr() {
		return getDateStr(new Date(), "yyyy-MM-dd");
	}

	public static String getDateStr(Date date) {
		return getDateStr(date, "yyyy-MM-dd");
	}

	public static String getDateStr(String fomatStr) {
		return getDateStr(new Date(), fomatStr);
	}

	/**
	 * 时间格式串转date
	 * 
	 * @param date
	 * @param fomatStr
	 * @return
	 * @throws ParseException
	 */
	public static Date getStrDate(String date, String fomatStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fomatStr);
		return sdf.parse(date);
	}

	public static Date getCurDate() {
		return new Date();
	}

	public static Long getCurrentTime() {
		return System.currentTimeMillis();
	}

}
