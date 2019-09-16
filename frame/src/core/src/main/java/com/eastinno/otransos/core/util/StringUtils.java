package com.eastinno.otransos.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public abstract class StringUtils {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 将字符串数组添加到List中
     * 
     * @param array
     * @param str
     * @return
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (array == null || array.length < 0)
            return new String[] {str};
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * 用relativePath更改路径替换原path中最后的“/”后的内容
     * 
     * @param path
     * @param relativePath
     * @return
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR))
                newPath += FOLDER_SEPARATOR;
            return newPath + relativePath;
        } else
            return relativePath;
    }

    /**
     * 将对象数组以“，”分割转化为字符串对象
     * 
     * @param arr 对象数组
     * @return 字符串对象
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    /**
     * 将对象数组以“delim”分割转化为字符串对象
     * 
     * @param arr 对象数组
     * @param delim 分隔符
     * @return 字符串对象
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (arr == null)
            return "";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0)
                sb.append(delim);
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 将String对象首字符大写
     * 
     * @param str String对象实例
     * @return 处理后的String对象
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * 根据boolean类型变量“capitalize”，处理String类型的对象，是否首字符大写
     * 
     * @param str
     * @param capitalize
     * @return
     */
    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0)
            return str;
        StringBuffer buf = new StringBuffer(str.length());
        if (capitalize)
            buf.append(Character.toUpperCase(str.charAt(0)));
        else
            buf.append(Character.toLowerCase(str.charAt(0)));
        buf.append(str.substring(1));
        return buf.toString();
    }

    /**
     * 清理路径
     * 
     * @param path
     * @return
     */
    public static String cleanPath(String path) {
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
        int prefixIndex = pathToUse.indexOf(":");
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            pathToUse = pathToUse.substring(prefixIndex + 1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--)
            if (CURRENT_PATH.equals(pathArray[i])) {
            } else if (TOP_PATH.equals(pathArray[i]))
                tops++;
            else if (tops > 0)
                tops--;
            else
                pathElements.add(0, pathArray[i]);

        for (int i = 0; i < tops; i++)
            pathElements.add(0, TOP_PATH);

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * 将对象数组以“，”号分割合成String对象
     * 
     * @param coll
     * @return
     */
    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * 将对象数组以“，”号分割合成String对象
     * 
     * @param coll
     * @param delim
     * @return
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * 将对象数组封装成String对象，用prefix和suffix分割
     * 
     * @param coll
     * @param delim
     * @param prefix
     * @param suffix
     * @return String对象
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (coll == null)
            return "";

        StringBuffer sb = new StringBuffer();
        Iterator<?> it = coll.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (i > 0)
                sb.append(delim);
            sb.append(prefix).append(it.next()).append(suffix);
            i++;
        }
        return sb.toString();
    }

    /**
     * 将字符串以“，”号分割后放到set中
     * 
     * @param str
     * @return Set对象
     */
    public static Set<String> commaDelimitedListToSet(String str) {
        Set<String> set = new TreeSet<String>();
        String[] tokens = commaDelimitedListToStringArray(str);
        for (String element : tokens)
            set.add(element);
        return set;
    }

    /**
     * 将字符串以“，”号分割成String[]对象
     * 
     * @param str
     * @return String[]对象
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * 计算字符串str中sub出现的次数
     * 
     * @param str
     * @param sub
     * @return int对象
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0)
            return 0;
        int count = 0, pos = 0, idx = 0;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * 删除字符串inString中pattern的内容
     * 
     * @param inString
     * @param pattern
     * @return String对象
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * 删除字符串inString中pattern的内容
     * 
     * @param inString
     * @param charsToDelete
     * @return String对象
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (inString == null || charsToDelete == null)
            return inString;
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1)
                out.append(c);
        }
        return out.toString();
    }

    /**
     * 将String对象str以String对象demimiter分割成String[]数组
     * 
     * @param str
     * @param delimiter
     * @return String[]对象
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        if (str == null)
            return new String[0];
        if (delimiter == null)
            return new String[] {str};

        List<String> result = new ArrayList<String>();
        if ("".equals(delimiter))
            for (int i = 0; i < str.length(); i++)
                result.add(str.substring(i, i + 1));
        else {
            int pos = 0;
            int delPos = 0;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(str.substring(pos, delPos));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length())
                result.add(str.substring(pos));
        }
        return toStringArray(result);
    }

    /**
     * 判断String对象str是否以String对象suffix结束，不区分大小写
     * 
     * @param str
     * @param suffix
     * @return boolean
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null)
            return false;
        if (str.endsWith(suffix))
            return true;
        if (str.length() < suffix.length())
            return false;

        String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
        String lcSuffix = suffix.toLowerCase();
        return lcStr.equals(lcSuffix);
    }

    /**
     * 根据路径名获取文件名称
     * 
     * @param path
     * @return String对象的文件名
     */
    public static String getFilename(String path) {
        if (path == null)
            return null;
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * 根据路径获取文件名，不带后缀
     * 
     * @param path
     * @return String类型的文件名
     */
    public static String getFilenameExtension(String path) {
        if (path == null)
            return null;
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
    }

    /**
     * 获取地区对象Locale
     * 
     * @param localeString
     * @return
     */
    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = (parts.length > 0 ? parts[0] : "");
        String country = (parts.length > 1 ? parts[1] : "");
        String variant = (parts.length > 2 ? parts[2] : "");
        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    /**
     * 判断清理后的字符串是否相等
     * 
     * @param path1
     * @param path2
     * @return boolean对象
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * 将String类型对象以‘’单引号引起来
     * 
     * @param str
     * @return
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * 如果对象是String类型的，则以单引号引起来，否则返回原对象
     * 
     * @param obj
     * @return
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * 去除字符数组中重复的对象
     * 
     * @param array
     * @return
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (array == null || array.length < 0)
            return array;
        Set<String> set = new TreeSet<String>();
        for (String element : array)
            set.add(element);
        return toStringArray(set);
    }

    /**
     * 用制定的String对象newpattern替换inString中oldPattern的内容，全部替换
     * 
     * @param inString
     * @param oldPattern
     * @param newPattern
     * @return 返回替换后的String对象
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (inString == null)
            return null;
        if (oldPattern == null || newPattern == null)
            return inString;

        StringBuffer sbuf = new StringBuffer();
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));
        return sbuf.toString();
    }

    /**
     * 给字符串排序
     * 
     * @param array
     * @return 返回排序后的String[]对象
     */
    public static String[] sortStringArray(String[] array) {
        if (array == null || array.length < 0)
            return new String[0];
        Arrays.sort(array);
        return array;
    }

    /**
     * 将String[]对象以delimiter分割封装成Properties对象
     * 
     * @param array
     * @param delimiter
     * @return
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * 将String[]对象以delimiter分割封装成Properties对象，驱除制定内容
     * 
     * @param array
     * @param delimiter
     * @param charsToDelete
     * @return
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

        if (array == null || array.length == 0)
            return null;

        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null)
                element = deleteAny(element, charsToDelete);
            String[] splittedElement = org.springframework.util.StringUtils.split(element, delimiter);
            if (splittedElement == null)
                continue;
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * 判断String对象str是否以prefix开始
     * 
     * @param str
     * @param prefix
     * @return boolean
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null)
            return false;
        if (str.startsWith(prefix))
            return true;
        if (str.length() < prefix.length())
            return false;
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * 根据路径获取文件名，去掉后缀
     * 
     * @param path
     * @return String对象
     */
    public static String stripFilenameExtension(String path) {
        if (path == null)
            return null;
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
    }

    /**
     * 用String对象delimiters分割字符串str去掉分割后的空格忽略空值
     * 
     * @param str
     * @param delimiters
     * @return String[]对象
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * 用String对象delimiters分割字符串Str，
     * 
     * @param str
     * @param delimiters
     * @param trimTokens 是否去掉空值
     * @param ignoreEmptyTokens 是否忽略空值
     * @return String[]对象
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null)
            return new String[] {};
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens)
                token = token.trim();
            if (!ignoreEmptyTokens || token.length() > 0)
                tokens.add(token);
        }
        return toStringArray(tokens);
    }

    /**
     * 将对象数组封装成Stirng[]对象
     * 
     * @param collection
     * @return
     */
    public static String[] toStringArray(Collection<?> collection) {
        if (collection == null)
            return null;
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    /**
     * 去掉String中开始位置所有空格
     * 
     * @param str
     * @return 返回去掉空格后的字符串
     */
    public static String trimLeadingWhitespace(String str) {
        if (!org.springframework.util.StringUtils.hasLength(str))
            return str;
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0)))
            buf.deleteCharAt(0);
        return buf.toString();
    }

    /**
     * 去掉String中结尾部分所有空格
     * 
     * @param str
     * @return 返回去掉空格后的字符串
     */
    public static String trimTrailingWhitespace(String str) {
        if (!org.springframework.util.StringUtils.hasLength(str))
            return str;
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1)))
            buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * 去掉String中所有空格
     * 
     * @param str
     * @return 返回去掉空格后的字符串
     */
    public static String trimWhitespace(String str) {
        if (!org.springframework.util.StringUtils.hasLength(str))
            return str;
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0)))
            buf.deleteCharAt(0);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1)))
            buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * 处理String类型的对象，首字符大写
     * 
     * @param str
     * @param capitalize
     * @return
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    /**
     * 获取字符串最后一个“."之后的内容
     * 
     * @param qualifiedName
     * @return
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * 获取指定String对象之后的内容
     * 
     * @param qualifiedName
     * @param separator
     * @return
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * 获取一个带参数的URL中指定参数名所对应的参数值
     * 
     * @param url 带参数的URL
     * @param paraName 参数名
     * @return 返回参数名对应的参数值
     */
    public static String getUrlParameter(String url, String paraName) {
        String paraVal = null;
        Integer m = url.indexOf(paraName);
        if (m == -1) {
            return paraVal;
        }
        String[] paras = url.substring(m, url.length()).split("&");
        for (String obj : paras) {
            if (obj.indexOf(paraName) != -1) {
                paraVal = obj.substring(paraName.length() + 1);
                break;
            }
        }
        return paraVal;
    }
}
