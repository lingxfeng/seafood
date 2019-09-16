package com.eastinno.otransos.shop.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 获取日期工具类
 * @author Administrator
 *
 */
public class DateUtil {
	public static String getDateStr(Date date,String fomatStr){
		SimpleDateFormat formatter = new SimpleDateFormat(fomatStr);
		String strDate = formatter.format(date);
		return strDate;
	}
	public static String getDateStr(){
		return getDateStr(new Date(),"yyyy-MM-dd");
	}
	public static String getDateStr(Date date){
		return getDateStr(date,"yyyy-MM-dd");
	}
	public static String getDateStr(String fomatStr){
		return getDateStr(new Date(),fomatStr);
	}
	public static Date getCurDate(){
		return new Date();
	}
	public static Long getCurrentTime(){
		return System.currentTimeMillis();
	}
}
