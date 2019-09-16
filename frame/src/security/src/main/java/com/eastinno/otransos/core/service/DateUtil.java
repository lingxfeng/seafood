package com.eastinno.otransos.core.service;

import java.util.Calendar;
import java.util.Date;

import com.eastinno.otransos.core.util.CommUtil;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2010年12月4日 下午7:39:49
 * @Intro 日期处理函数，提供一些实用的日期封装，方便进行统计
 */
public class DateUtil {
    public static Date[] getThisMonth() {
        return getThisMonth(new Date());
    }

    public static Date[] getThisMonth(Date d) {
        Date[] ds = new Date[2];
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        ca.set(11, 0);
        ca.set(12, 0);
        ca.set(13, 0);
        ca.set(14, 0);
        ca.set(5, 1);
        ds[0] = ca.getTime();
        ca.roll(2, 1);
        if (ca.get(2) == 0)
            ca.roll(1, 1);
        ds[1] = ca.getTime();
        return ds;
    }

    public static Date[] getThisSeason() {
        return getThisSeason(new Date());
    }

    public static Date[] getThisSeason(Date date) {
        Date[] ds = new Date[2];
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(11, 0);
        ca.set(12, 0);
        ca.set(13, 0);
        ca.set(14, 0);
        int month = ca.get(2);
        int s = month % 4 * 4 + 1;
        ca.set(2, s);
        ds[0] = ca.getTime();
        ca.roll(2, 4);
        ds[1] = ca.getTime();
        return ds;
    }

    public static Date[] getThisYear() {
        return getThisYear(new Date());
    }

    public static Date[] getThisYear(Date date) {
        Date[] ds = new Date[2];
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(11, 0);
        ca.set(12, 0);
        ca.set(13, 0);
        ca.set(14, 0);
        ca.set(2, 0);
        ca.set(5, 1);
        ds[0] = ca.getTime();
        ca.roll(1, 1);
        ds[1] = ca.getTime();
        return ds;
    }

    public static Date[] getBefore(int days) {
        Date[] ds = new Date[2];
        ds[1] = new Date();
        ds[0] = getBefore(days, ds[1]);
        return ds;
    }

    public static Date getBefore(int days, Date d) {
        Date now = d;
        if (now == null)
            now = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(now);
        int m = ca.get(2);
        ca.roll(6, -days);
        if ((ca.get(2) != m) && (ca.get(2) == 11))
            ca.roll(1, -1);
        return ca.getTime();
    }

    public static Date[] getAfter(int days) {
        Date[] ds = new Date[2];
        ds[0] = new Date();
        ds[1] = getAfter(days, ds[0]);
        return ds;
    }

    public static Date getAfter(int days, Date d) {
        Date now = d;
        if (now == null)
            now = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(now);
        int m = ca.get(2);
        ca.roll(6, days);
        if ((ca.get(2) != m) && (ca.get(2) == 0))
            ca.roll(1, 1);
        return ca.getTime();
    }

    /**
     * JAVA判断当前日期是星期几
     * 
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void main(String[] args) {
        System.out.println(getWeekOfDate(new Date()));
        System.out.println(CommUtil.formatDate("H", new Date()));
        System.out.println(CommUtil.formatDate("mm", new Date()));
    }
}
