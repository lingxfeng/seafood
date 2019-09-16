package com.eastinno.otransos.demo;

import com.eastinno.otransos.search.impl.SearchHelper;

/**
 * 测试 IK 分词器
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 */
public class IKTester {

    public static void main(String[] args) {
        test_highlight();
        test_split();
    }

    protected static void test_highlight() {
        String text = "SQL server 是最好的 数据库 应用服务器";
        System.out.println("RESULT:" + SearchHelper.highlight(text, "sql server"));
    }

    protected static void test_split() {
        String text = "我们是中国人";
        long ct = System.currentTimeMillis();
        for (String word : SearchHelper.splitKeywords(text)) {
            System.out.println(word);
        }
        System.out.printf("TIME %d\n", (System.currentTimeMillis() - ct));
    }

}
