package com.eastinno.otransos.core.service;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年12月16日 下午3:21:31
 * @Intro
 */
public class String2SpellUtil {
    private char[] chartable;
    private char[] alphatable;
    private int[] table;

    public String2SpellUtil() {
        this.chartable = new char[] {'啊', 33453, '擦', '搭', 34558, '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒',
                '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'};
        this.alphatable = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'};
        this.table = new int[27];
        for (int i = 0; i < 27; i++)
            this.table[i] = gbValue(this.chartable[i]);
    }

    public char char2Alpha(char ch) {
        if ((ch >= 'a') && (ch <= 'z'))
            return (char) (ch - 'a' + 65);
        if (((ch >= 'A') && (ch <= 'Z')) || ((ch >= '0') && (ch <= '9')) || ((ch > 0) && (ch < 'a')))
            return ch;
        int gb = gbValue(ch);
        if (gb < this.table[0]) {
            return '0';
        }
        int i;
        for (i = 0; i < 26; i++) {
            if (match(i, gb)) {
                break;
            }
        }
        if (i >= 26) {
            return '0';
        }
        return this.alphatable[i];
    }

    public String string2Alpha(String SourceStr) {
        String result = "";
        int StrLength = SourceStr.length();
        try {
            for (int i = 0; i < StrLength; i++)
                result = result + char2Alpha(SourceStr.charAt(i));
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    private boolean match(int i, int gb) {
        if (gb < this.table[i]) {
            return false;
        }
        int j = i + 1;

        while ((j < 26) && (this.table[j] == this.table[i])) {
            j++;
        }
        if (j == 26) {
            return gb <= this.table[j];
        }
        return gb < this.table[j];
    }

    private int gbValue(char ch) {
        String str = new String();
        str = str + ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xFF00) + (bytes[1] & 0xFF);
        } catch (Exception e) {
        }
        return 0;
    }

    public static String getBeginCharacter(String s) {
        String2SpellUtil ut = new String2SpellUtil();
        return ut.string2Alpha(s);
    }

    public static void main(String[] args) {
        System.out.println(getBeginCharacter("showgril"));
    }
}
