package com.eastinno.otransos.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * <p>
 * Title:通过工具类,处理一些数据转换及常用功能
 * </p>
 * <p>
 * Description: 包括一些类型转换,数据格式化,生成一些常用的html文本等
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 1.0
 */
public class CommUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat cnDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
    private static final SimpleDateFormat cnTimeFormat = new SimpleDateFormat("hh点mm分ss秒");
    private static final SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
    private static final SimpleDateFormat cnLongDateFormat = new SimpleDateFormat("yyyy年MM月dd日 H点m分s秒");
    private static final DecimalFormat numberFormat = new DecimalFormat("#,###,###,##0.00");

    private static final CommUtil util = new CommUtil();


    public static boolean beforeDays(Date date, Integer days) {
        if (date == null || days == null)
            return false;
        Long time = System.currentTimeMillis() - date.getTime();
        return time < (days * 86400000);
    }


    /**
     * @param htmlText
     * @return
     */
    public static String cleanHtmlTag(String htmlText) {
        String reg = "</?[a-z][a-z0-9]*[^<>]*>?";
        return htmlText.replaceAll(reg, "");
    }


    /**
     * 将"yyyy年MM月dd日"格式日期字符串转换为日期对象
     * 
     * @param s
     *            需转换的"yyyy年MM月dd日"格式日期字符串
     * @return 转换后的日期对象
     */
    public static Date cnFormatDate(String s) {
        Date d = null;
        try {
            d = cnDateFormat.parse(s);
        }
        catch (Exception e) {
        }
        return d;
    }


    /**
     * 将日期对象转换为"yyyy年MM月dd日 H点m分s秒"格式的日期字符串
     * 
     * @param v
     * @return
     */
    public static String cnLongDate(Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            ret = cnLongDateFormat.format(v);
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    /**
     * 将日期对象转换为"hh点mm分ss秒"格式的日期字符串
     * 
     * @param v
     * @return
     */
    public static String cnSortTime(Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            ret = cnTimeFormat.format(v);
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    /**
     * 将日期对象转换为"hh:mm:ss"格式的时间字符串
     * 
     * @param v
     * @return
     */
    public static String sortTime(Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            ret = timeFormat.format(v);
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    /**
     * 将日期对象转换为"yyyy-MM-dd"格式的日期字符串
     * 
     * @param v
     * @return
     */
    public static String format(Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            if (v instanceof Number)
                ret = numberFormat.format(v);
            else if (v instanceof Date)
                ret = dateFormat.format(v);
            else
                return v.toString();
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    public static String formatNumber(String format, Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            if (v instanceof Number) {
                DecimalFormat numberFormat = new DecimalFormat(format);
                ret = numberFormat.format(v);
            }
            else
                return v.toString();
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    /**
     * 将"yyyy-MM-dd"格式的日期字符串转换为日期对象
     * 
     * @param s
     *            日期字符串
     * @return 返回日期对象
     */
    public static Date formatDate(String s) {
        Date d = null;
        try {
            d = dateFormat.parse(s);
        }
        catch (Exception e) {
        }
        return d;
    }


    /**
     * 将日期对象转换为format参数指定的日期格式字符串
     * 
     * @param format
     *            日期格式字符串
     * @param v
     *            日期对象
     * @return 格式化后的日期字符串
     */
    public static String formatDate(String format, Object v) {
        if (v == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(v);
    }


    /**
     * 将Long型数据转换成时间格式的字符串，例如：formatLongToTimeStr(100000)=0小时1分钟 40秒
     */
    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (null2String(hour) + "小时" + null2String(minute) + "分钟" + null2String(second) + "秒");
    }


    /**
     * 将日期对象转换为"yyyy-MM-dd H:m:s"格式的日期时间字符串
     * 
     * @param v
     * @return
     */
    public static String longDate(Object v) {
        if (v == null)
            return null;
        String ret = "";
        try {
            ret = longDateFormat.format(v);
        }
        catch (IllegalArgumentException e) {

        }
        return ret;
    }


    public static <T> List<T> copyList(Class<T> classType, Iterator<T> src) {
        List<T> tag = new ArrayList<T>();
        while (src.hasNext()) {
            T obj = null, ormObj = src.next();
            try {
                obj = classType.newInstance();
                BeanWrapper wrapper = new BeanWrapperImpl(obj);
                BeanWrapper wrapper1 = new BeanWrapperImpl(ormObj);
                PropertyDescriptor descriptors[] = wrapper.getPropertyDescriptors();
                for (PropertyDescriptor element : descriptors) {
                    String name = element.getName();
                    wrapper.setPropertyValue(name, wrapper1.getPropertyValue(name));
                }

            }
            catch (Exception e) {
            }
            if (obj != null)
                tag.add(obj);
        }
        return tag;
    }


    public static void main(String[] args) {
        int s = 20;
        for (int i = 0; i < s; i++) {
            System.out.println(getRandomInt(4));
        }
    }


    /**
     * 格式化内容，只保留前n个字符，并进一步确认是否要在后台加上"..."
     * 
     * @param str
     *            要处理的字符串
     * @param num
     *            保留的字数
     * @param hasDot
     *            是否显示...
     * @return
     */
    public static String format(String str, int num, boolean hasDot) {
        if (str == null) {
            return "";
        }
        else {
            if (str.getBytes().length < num * 2) {
                return str;
            }
            else {
                byte[] ss = str.getBytes();
                byte[] bs = new byte[num * 2];
                for (int i = 0; i < bs.length; i++) {
                    bs[i] = ss[i];
                }
                String subStr = CommUtil.substring(str, num * 2);
                if (hasDot) {
                    subStr = subStr + "...";
                }
                return subStr;
            }
        }
    }


    /**
     * 格式化内容，只保留前n个字符
     * 
     * @param str
     * @param num
     * @return
     */
    public static String format(String str, int num) {
        return format(str, num, false);
    }


    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }


    public static CommUtil getInstance() {
        return util;
    }


    public static String getOnlyID() {
        String strRnd;
        double dblTmp;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMDDhhmmss");
        dblTmp = Math.random() * 100000;
        while (dblTmp < 10000)
            dblTmp = Math.random() * 100000;
        strRnd = String.valueOf(dblTmp).substring(0, 4);
        String s = df.format(new Date()) + strRnd;
        return s;
    }


    /**
     * 随机生成指定位数且不重复的纯数字型字符串.
     * 
     * @param length
     *            指定长度
     * @return 返回指定位数且不重复的数字型字符串
     */
    public static String getRandomInt(int length) {
        int i = 1;// i在此程序中只作为判断是否是将随机数添加在首位，防止首位出现0；
        Random r = new Random();
        int tag[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        String str = "";
        int temp = 0;

        while (str.length() < length) {
            temp = r.nextInt(10);// 取出0(含)~10(不含)之间任意数
            if (i == 1 && temp == 0) {
                continue;
            }
            else {
                i = 2;
                if (tag[temp] == 0) {
                    str = str + temp;
                    tag[temp] = 1;
                }
            }
        }
        return str;
    }


    /**
     * 随机生成指定位数且不重复的字符串.去除了部分容易混淆的字符，如1和l，o和0等， 随机范围1-9 a-z A-Z
     * 
     * @param length
     *            指定字符串长度
     * @return 返回指定位数且不重复的字符串
     */
    public static String getRandomVal(int length) {
        StringBuffer bu = new StringBuffer();
        String[] arr =
                { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                 "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D",
                 "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W",
                 "X", "Y", "Z" };
        Random random = new Random();
        while (bu.length() < length) {
            String temp = arr[random.nextInt(57)];
            if (bu.indexOf(temp) == -1)
                bu.append(temp);
        }
        return bu.toString();
    }


    /**
     * 把一个字符串或字符串数组转换成一个字符串数组
     * 
     * @param obj
     *            字符串或字符串数组对象
     * @return 新的字符串数组
     */
    public static String[] getStringArray(Object obj) {
        if (obj == null)
            return null;
        if (obj.getClass().isArray())
            return (String[]) obj;
        else
            return new String[] { obj.toString() };
    }


    /**
     * 判断是否是中文字符
     * 
     * @param c
     *            字符
     * @return
     */
    public static boolean isChinese(char c) {
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
            // 字母, 数字
            return false;
        else if (Character.isLetter(c))
            return true;
        else
            return false;
    }


    public static void map2obj(Map<String, Object> map, Object obj) {
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor descriptors[] = wrapper.getPropertyDescriptors();
        for (PropertyDescriptor element : descriptors) {
            String name = element.getName();
            Object v = map.get(name);
            if (v != null && element.getWriteMethod() != null)
                wrapper.setPropertyValue(name, v);
        }
    }


    /**
     * 将一个对象转换为int型，如果对象为空则返回0。注意：对象s必须为可以转换为int的对象，如数字字符串、Integer类型对象等。
     * 
     * @param s
     * @return
     */
    public static int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            }
            catch (Exception e) {
            }
        return v;
    }


    /**
     * 将对象转换为字符串，如果对象为空则返回""。
     * 
     * @param s
     * @return
     */
    public static String null2String(Object s) {
        return s == null ? "" : s.toString();
    }


    /**
     * 将一个对象中的属性值拷贝到一个map中，字段名为key。是Map2Obj方法的反向操作。
     * 
     * @deprecated 尽量改用obj2map方法
     * @param obj
     * @param map
     */
    public static void Obj2Map(Object obj, Map<String, Object> map) {
        if (map == null)
            map = new HashMap<String, Object>();
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor descriptors[] = wrapper.getPropertyDescriptors();
        for (PropertyDescriptor element : descriptors) {
            String name = element.getName();
            try {
                if (element.getReadMethod() != null)
                    map.put(name, wrapper.getPropertyValue(name));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 将一个double类型的数值保留param位小数，最后返回这个数值的字符串形式。
     * 
     * @param inNumber
     * @param param
     * @return
     */
    public static String round(double inNumber, int param) {
        String format = "#.";
        for (int i = 0; i < param; i++)
            format = format.concat("#");
        // 去掉多余小数点
        if (param == 0)
            format = format.substring(0, format.length() - 1);
        DecimalFormat df = new DecimalFormat(format);
        return df.format(inNumber);
    }


    /**
     * 把查询结果集分页显示
     * 
     * @param pList
     * @param form
     */
    public static void saveIPageList2WebForm(IPageList pList, WebForm form) {
        if (pList != null) {
            form.addResult("list", pList.getResult());
            form.addResult("pages", new Integer(pList.getPages()));
            form.addResult("rows", new Integer(pList.getRowCount()));
            form.addResult("page", new Integer(pList.getCurrentPage()));
            form.addResult("pageSize", new Integer(pList.getPageSize()));
            form.addResult("nextPage", new Integer(pList.getNextPage()));
            form.addResult("previousPage", new Integer(pList.getPreviousPage()));

            // 提供多种分页方式
            form.addResult("paginationDC", TagUtil.paginationDC(pList.getCurrentPage(), pList.getPages(),
                pList.getRowCount()));
            form.addResult("pagingForBankCMS", TagUtil.pagingForBankCMS(pList.getCurrentPage(), pList
                .getPages(), pList.getRowCount()));
            form.addResult("paginationForMobile", TagUtil.pagingForMobile(pList.getCurrentPage(), pList
                .getPages(), pList.getRowCount()));
            form.addResult("paginationForBootstrap", TagUtil.pagingForBootStrap(pList.getCurrentPage(), pList
                .getPages(), pList.getRowCount()));
            form.addResult("paginationForBlueIdea", TagUtil.paginationForBlueIdea(pList.getCurrentPage(),
                pList.getPages(), pList.getRowCount()));
            form.addResult("paginationForBcd", TagUtil.paginationForBcd(pList.getCurrentPage(),
                    pList.getPages(), pList.getRowCount()));
            form.addResult("paginationForBcd2", TagUtil.paginationForBcd2(pList.getCurrentPage(),
                    pList.getPages(), pList.getRowCount()));
            form.addResult("paginationForBcd3", TagUtil.paginationForBcd3(pList.getCurrentPage(),
                    pList.getPages(), pList.getRowCount()));
            form.addResult("paginationForBcd4", TagUtil.paginationForBcd4(pList.getCurrentPage(),
                    pList.getPages(), pList.getRowCount()));//pc端查询商品  goShop.java?cmd=queryPro
        }
    }


    /**
     * 截取一个字符串的前maxLength位，并在字符串尾加上"..."
     * 
     * @param s
     * @param maxLength
     * @return
     */
    public static String substring(String s, int maxLength) {
        if (!StringUtils.hasLength(s))
            return s;
        if (s.getBytes().length <= maxLength)
            return s;
        int i = 0;
        for (int k = 0; k < maxLength && i < s.length(); i++, k++)
            if (s.charAt(i) > '一')
                k++;
        if (i < s.length())
            s = s.substring(0, i) + "...";
        return s;
    }


    /**
     * 把字符串strvalue转换为"GBK"编码
     * 
     * @param strvalue
     * @return
     */
    public static String toChinese(String strvalue) {
        try {
            if (strvalue == null)
                return null;
            else {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
                return strvalue;
            }
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * 把字符串strvalue转换为charset指定的字符编码格式，如果charset为空则转换为"GBK"编码。
     * 
     * @param strvalue
     * @param charset
     * @return
     */
    public static String toChinese(String strvalue, String charset) {
        try {
            if (charset == null || charset.equals(""))
                return toChinese(strvalue);
            else {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), charset);
                return strvalue;
            }
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * 把字符串s转换为unicode编码
     * 
     * @param s
     * @param flag
     * @return
     */
    public static String convert2unicode(String s, boolean flag) {
        int i = s.length();
        int j = i * 2;
        if (j < 0)
            j = 2147483647;
        StringBuffer stringbuffer = new StringBuffer(j);
        for (int k = 0; k < i; k++) {
            char c = s.charAt(k);
            if (c > '=' && c < '\177') {
                if (c == '\\') {
                    stringbuffer.append('\\');
                    stringbuffer.append('\\');
                }
                else {
                    stringbuffer.append(c);
                }
                continue;
            }
            switch (c) {
            case 32: // ' '
                if (k == 0 || flag)
                    stringbuffer.append('\\');
                stringbuffer.append(' ');
                break;

            case 9: // '\t'
                stringbuffer.append('\\');
                stringbuffer.append('t');
                break;

            case 10: // '\n'
                stringbuffer.append('\\');
                stringbuffer.append('n');
                break;

            case 13: // '\r'
                stringbuffer.append('\\');
                stringbuffer.append('r');
                break;

            case 12: // '\f'
                stringbuffer.append('\\');
                stringbuffer.append('f');
                break;

            case 33: // '!'
            case 35: // '#'
            case 58: // ':'
            case 61: // '='
                stringbuffer.append('\\');
                stringbuffer.append(c);
                break;

            default:
                if (c < ' ' || c > '~') {
                    stringbuffer.append('\\');
                    stringbuffer.append('u');
                    stringbuffer.append(toHex(c >> 12 & 15));
                    stringbuffer.append(toHex(c >> 8 & 15));
                    stringbuffer.append(toHex(c >> 4 & 15));
                    stringbuffer.append(toHex(c & 15));
                }
                else {
                    stringbuffer.append(c);
                }
                break;
            }
        }

        return stringbuffer.toString();
    }


    public static String convert2json(String s) {
        boolean flag = true;
        int i = s.length();
        int j = i * 2;
        if (j < 0)
            j = 2147483647;
        StringBuffer stringbuffer = new StringBuffer(j);
        for (int k = 0; k < i; k++) {
            char c = s.charAt(k);
            if (c > '=' && c < '\177') {
                if (c == '\\') {
                    stringbuffer.append('\\');
                    stringbuffer.append('\\');
                }
                else {
                    stringbuffer.append(c);
                }
                continue;
            }
            switch (c) {
            case 32: // ' '
                // if (k == 0 || flag)
                // stringbuffer.append('\\');
                stringbuffer.append(' ');
                break;

            case 9: // '\t'
                stringbuffer.append('\\');
                stringbuffer.append('t');
                break;

            case 10: // '\n'
                stringbuffer.append('\\');
                stringbuffer.append('n');
                break;

            case 13: // '\r'
                stringbuffer.append('\\');
                stringbuffer.append('r');
                break;

            case 12: // '\f'
                stringbuffer.append('\\');
                stringbuffer.append('f');
                break;

            case 33: // '!'
            case 35: // '#'
            case 58: // '='
            case 61: // ':'
                // stringbuffer.append('\\');
                stringbuffer.append(c);
                break;
            default:
                stringbuffer.append(c);
                break;
            }
        }
        return stringbuffer.toString();
    }


    /**
     * 将一个list分为preNum个子List，并将这些子List作为元素添加到另一个List中，最终返回这个List。
     * 
     * @param list
     * @param perNum
     * @return
     */
    public static List<Object> toRowChildList(List<Object> list, int perNum) {
        List<Object> l = new ArrayList<Object>();
        if (list == null)
            return l;
        for (int i = 0; i < list.size(); i += perNum) {
            List<Object> cList = new ArrayList<Object>();
            for (int j = 0; j < perNum; j++)
                if (i + j < list.size())
                    cList.add(list.get(i + j));
            l.add(cList);
        }
        return l;
    }


    /**
     * 将list中的元素拆分为若干个List，每个子List包含perNum个元素，然后将这些子List作为元素添加到另一个List中并返回这个List
     * 。
     * 
     * @param list
     * @param perNum
     * @return
     */
    public static List<Object> toRowDivList(List<Object> list, int perNum) {
        List<Object> l = new ArrayList<Object>();
        if (list == null)
            return l;
        for (int i = 0; i < list.size();) {
            List<Object> cList = new ArrayList<Object>();
            for (int j = 0; j < perNum; j++, i++)
                if (i < list.size())
                    cList.add(list.get(i));
            l.add(cList);
        }
        return l;
    }


    /**
     * 把数字i转换为16进制
     * 
     * @param i
     * @return
     */
    private static char toHex(int i) {
        return hexDigit[i & 15];
    }

    private static final char hexDigit[] =
            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    /**
     * 对象属性拷贝，不会拷贝集合元素中的属性，只会拷贝集合的大小。
     * 
     * @param source
     *            拷贝源数据
     * @param target
     *            拷贝目标数据
     */
    public static void shallowCopy(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (int i = 0; i < targetPds.length; i++) {
            PropertyDescriptor targetPd = targetPds[i];
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd =
                        BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source, new Object[0]);
                        // 把集合清空
                        if (value != null) {
                            if (value instanceof List) {
                                value = new ArrayList<Object>(((List<?>) value).size());
                            }
                            else if (value instanceof Map) {
                                value = new HashMap<String, Object>(((Map<?, ?>) value).size());
                            }
                            else if (value instanceof Set) {
                                value = new HashSet<Object>(((Set<?>) value).size());
                            }
                        }
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, new Object[] { value });
                    }
                    catch (Exception ex) {
                        throw new RuntimeException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }


    /**
     * 把一个部份属性拷到一个Map中
     * 
     * @param source
     * @param ps
     * @return
     */
    public static Map<String, Object> obj2map(Object source, String[] ps) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (source == null)
            return null;
        if (ps == null || ps.length < 1) {
            CommUtil.Obj2Map(source, map);
            return map;
        }
        for (String p : ps) {
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), p);
            if (sourcePd != null && sourcePd.getReadMethod() != null) {
                try {
                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source, new Object[0]);
                    map.put(p, value);
                }
                catch (Exception ex) {
                    throw new RuntimeException("Could not copy properties from source to target", ex);
                }
            }
        }
        return map;
    }


    /**
     * 把对象转换成Map
     * 
     * @param source
     *            要转换的对象
     * @param ps
     *            不需要转换的属性
     * @return
     */
    public static Map<String, Object> obj2mapExcept(Object source, String[] ps) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (source == null)
            return null;
        if (ps != null && ps.length > 0) {
            PropertyDescriptor descriptors[] = BeanUtils.getPropertyDescriptors(source.getClass());
            for (PropertyDescriptor element : descriptors) {
                String name = element.getName();
                if (name == null || "class".equals(name) || "null".equals(name))
                    continue;
                try {
                    if (element.getReadMethod() != null) {
                        boolean find = false;
                        for (String p : ps) {
                            if (p.equals(name)) {
                                find = true;
                                break;
                            }
                        }
                        if (!find) {
                            Method readMethod = element.getReadMethod();
                            if (Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                Object value = readMethod.invoke(source, new Object[0]);
                                map.put(name, value);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
            CommUtil.Obj2Map(source, map);
        return map;
    }


    public static Map<String, Object> array2map(Object[] os, String[] fields) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++) {
            if (os.length > i) {
                map.put(fields[i], os[i]);
            }
        }
        return map;
    }


    /**
     * 得到一个类的所有属性
     * 
     * @param clz
     * @param ps
     * @return
     */
    public static List<String> getPropertysExcept(Class<?> clz, String[] ps) {
        List<String> list = new ArrayList<String>();
        if (clz != null) {
            PropertyDescriptor descriptors[] = BeanUtils.getPropertyDescriptors(clz);
            for (PropertyDescriptor element : descriptors) {
                String name = element.getName();
                if (name == null || "class".equals(name) || "null".equals(name))
                    continue;
                try {
                    if (element.getReadMethod() != null) {
                        boolean find = false;
                        if (ps != null && ps.length > 0) {
                            for (String p : ps) {
                                if (p.equals(name)) {
                                    find = true;
                                    break;
                                }
                            }
                        }
                        if (!find) {
                            Method readMethod = element.getReadMethod();
                            if (Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                list.add(name);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    /**
     * 某一个日期是否属于最近几天
     * 
     * @param date
     * @param days
     * @return
     */
    public boolean lastDay(Date date, Integer days) {
        if (date == null)
            return false;
        Calendar ca = Calendar.getInstance();
        // int m=ca.get(Calendar.MONTH);
        ca.roll(Calendar.DAY_OF_YEAR, -days);
        // if(ca.get(Calendar.MONTH)!=m)ca.roll(Calendar.YEAR, -1);
        // System.out.println(ca.getTime());
        return date.after(ca.getTime());
    }
}
