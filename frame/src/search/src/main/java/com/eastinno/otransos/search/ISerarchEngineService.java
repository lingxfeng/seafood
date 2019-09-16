package com.eastinno.otransos.search;

import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;

/**
 * 站内搜索引擎实现<br />
 * 构建索引.删除索引.更新索引.检索等操作.
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2015年3月18日 下午3:01:49
 * @Intro
 */
public interface ISerarchEngineService {
    /**
     * 创建索引(考虑线程安全)
     * 
     * @param domains 对象
     * @throws Exception
     */
    void doIndex(List<Searchable> domains) throws Exception;

    /**
     * 删除索引
     * 
     * @param domain 对象
     * @throws Exception
     */
    void deleteIndex(Searchable domain) throws Exception;

    /**
     * 删除索引(删除多个)
     * 
     * @param domains 对象
     * @throws Exception
     */
    void deleteIndexs(List<Searchable> domains) throws Exception;

    /**
     * 进行检索
     * 
     * @param domain 检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param start 开始值
     * @param num 偏移量
     * @return
     * @throws Exception
     */
    IPageList doSearch(Searchable domain, boolean isHighlighter, int start, int num) throws Exception;

    /**
     * 进行多个检索对象的检索
     * 
     * @param domains 多个检索对象(一般只需要放入值keyword,即用来检索的关键字)
     * @param isHighlighter 是否高亮
     * @param start 开始值
     * @param num 偏移量
     * @return
     * @throws Exception
     */
    IPageList doSearch(List<Searchable> domains, boolean isHighlighter, int start, int num) throws Exception;

    /**
     * 删除某个类型的所有索引(考虑线程安全)
     * 
     * @param clazz 索引类型
     * @throws Exception
     */
    void deleteIndexsByIndexType(Class<? extends Searchable> clazz) throws Exception;

    /**
     * 删除某个类型的所有索引(考虑线程安全)
     * 
     * @param indexType 索引类型
     * @throws Exception
     */
    void deleteIndexsByIndexType(String indexType) throws Exception;

    /**
     * 删除所有的索引
     * 
     * @throws Exception
     */
    void deleteAllIndexs() throws Exception;

    /**
     * 更新索引
     * 
     * @param domain 需要更新的domain
     * @throws Exception
     */
    void updateIndex(Searchable domain) throws Exception;

    /**
     * 批量更新索引
     * 
     * @param domains 需要更新的domains
     * @throws Exception
     */
    void updateIndexs(List<Searchable> domains) throws Exception;
}
