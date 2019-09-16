package com.eastinno.otransos.application.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 网络爬取指定图片
 * 
 * @version 2.0
 * @author lengyu
 * @date 2014年7月26日-下午7:02:28
 */
public class DouBanMeiApp {
    private static final int timeoutMillis = 2000;

    public static String[] downFile(String[] urls, String savePath) throws IOException {
        if (urls == null) {
            return null;
        }
        // 保存文件路径
        String[] reStatus = new String[urls.length];
        String saveName = "";
        for (int i = 0; i < urls.length; i++) {
            if (urls[i] == null || urls[i].trim().length() < 1 || urls[i].lastIndexOf(".") < 0) {
                continue;
            }
            reStatus[i] = FilenameUtils.getName(urls[i]);
            // 格式验证
            saveName = "rmt" + new Date().getTime() + urls[i].substring(urls[i].lastIndexOf("."));
            // 大小验证
            URL fileUrl = null;
            fileUrl = new URL(urls[i]);
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            System.out.println(fileUrl + "           " + saveName);
            File savetoFile = new File(savePath + "/" + saveName);
            FileUtils.copyURLToFile(fileUrl, savetoFile);
            reStatus[i] = saveName;
        }
        return reStatus;
    }

    public String[] getIndex3(String[] index2s) throws IOException {
        Document document = null;
        String express = ".pic>img";
        int i = 0, j = 0;
        for (String index2 : index2s) {
            document = Jsoup.parse(new java.net.URL(index2), timeoutMillis);
            Elements list = document.select(express);
            for (i = 0, j = list == null ? 0 : list.size(); i < j; i++) {
                Element element = list.get(i);
                if (element.hasAttr("src")) {
                    downFile(new String[] {element.attr("src").toString()}, "D:\\meiz");
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String[] urlStrings = new String[] {"http://www.dbmeizi.com/category/12", "http://www.dbmeizi.com/archive/20140702"};
        try {
            new DouBanMeiApp().getIndex3(urlStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}