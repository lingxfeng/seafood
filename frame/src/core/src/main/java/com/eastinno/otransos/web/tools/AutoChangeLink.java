package com.eastinno.otransos.web.tools;

/**
 * 动静态地址自动转换对象
 * 
 * @author lengyu
 */
public interface AutoChangeLink {
    /**
     * 返回对象的静态html地址 一般是REST URL
     * 
     * @return 返回对象的静态html地址，当没有设置html地址时，返回url
     */
    String getStaticUrl();

    /**
     * 返回生成静态HTML的目录
     * 
     * @return 返回生成静态HTML的目录，注意此目录是指生成静态化时HTML文件保存到磁盘的目录
     */
    String getStaticPath();

    /**
     * 返回对象的动态显示地址
     * 
     * @return 返回对象的动态显示url
     */
    String getDynamicUrl();
}
