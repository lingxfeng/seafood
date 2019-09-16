package com.eastinno.otransos.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.domain.Trade;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ITradeService
 * 
 * @author ksmwly@gmail.com
 */
public interface ITradeService {
    /**
     * 保存一个Trade，如果保存成功返回该对象的id，否则返回null
     * 
     * @param instance
     * @return 保存成功的对象的Id
     */
    Long addTrade(Trade instance);

    /**
     * 根据一个ID得到Trade
     * 
     * @param id
     * @return
     */
    Trade getTrade(Long id);

    /**
     * 删除一个Trade
     * 
     * @param id
     * @return
     */
    boolean delTrade(Long id);

    /**
     * 批量删除Trade
     * 
     * @param ids
     * @return
     */
    boolean batchDelTrades(List<Serializable> ids);

    /**
     * 通过一个查询对象得到Trade
     * 
     * @param properties
     * @return
     */
    IPageList getTradeBy(IQueryObject queryObject);

    /**
     * 更新一个Trade
     * 
     * @param id 需要更新的Trade的id
     * @param dir 需要更新的Trade
     */
    boolean updateTrade(Long id, Trade instance);

    Trade getTradeByTitle(String title);
}
