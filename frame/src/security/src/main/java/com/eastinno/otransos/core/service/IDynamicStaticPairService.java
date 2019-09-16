package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.DynamicStaticPair;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * DynamicStaticPairService
 */
public interface IDynamicStaticPairService {

    /**
     * 保存一个DynamicStaticPair，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addDynamicStaticPair(DynamicStaticPair entity);

    /**
     * 根据一个ID得到DynamicStaticPair
     * 
     * @param id
     * @return
     */
    DynamicStaticPair getDynamicStaticPair(Long id);

    /**
     * 删除一个DynamicStaticPair
     * 
     * @param id
     * @return
     */
    boolean delDynamicStaticPair(Long id);

    /**
     * 批量删除DynamicStaticPair
     * 
     * @param ids
     * @return
     */
    boolean batchDelDynamicStaticPairs(List<Serializable> ids);

    /**
     * 通过一个查询对象得到DynamicStaticPair
     * 
     * @param properties
     * @return
     */
    IPageList getDynamicStaticPairBy(IQueryObject properties);

    /**
     * 更新一个DynamicStaticPair
     * 
     * @param id 需要更新的DynamicStaticPair的id
     * @param dir 需要更新的DynamicStaticPair
     */
    boolean updateDynamicStaticPair(Long id, DynamicStaticPair instance);

    /**
     * 发布一个指定的配置信息
     * 
     * @param id
     * @return 返回成功发布的数量
     */
    int publish(DynamicStaticPair dsp);

    /**
     * 自动发布
     */
    void autoPublish();

    /**
     * 根据一个动态URL地址查询DynamicStaticPair对象
     * 
     * @param url 如XXXX.java动态URL
     * @return
     */
    DynamicStaticPair getDynamicStaticPairByUrl(String url);

    /**
     * 动态URL转静态URL，静态URL转动态URL
     * 
     * @param url
     * @return
     */
    String converUrl(String url);
}
