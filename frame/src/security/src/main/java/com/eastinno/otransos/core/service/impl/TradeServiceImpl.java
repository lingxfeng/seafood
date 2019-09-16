package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.dao.ITradeDAO;
import com.eastinno.otransos.core.domain.Trade;
import com.eastinno.otransos.core.service.ITradeService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * TradeServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class TradeServiceImpl implements ITradeService {
    @Resource
    private ITradeDAO tradeDao;

    public void setTradeDao(ITradeDAO tradeDao) {
        this.tradeDao = tradeDao;
    }

    public Long addTrade(Trade trade) {
        parseTrade(trade);
        this.tradeDao.save(trade);
        if (trade != null && trade.getId() != null) {
            return trade.getId();
        }
        return null;
    }

    public Trade getTrade(Long id) {
        Trade trade = this.tradeDao.get(id);
        return trade;
    }

    public boolean delTrade(Long id) {
        Trade trade = this.getTrade(id);
        if (trade != null) {
            this.tradeDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelTrades(List<Serializable> tradeIds) {

        for (Serializable id : tradeIds) {
            delTrade((Long) id);
        }
        return true;
    }

    public IPageList getTradeBy(IQueryObject queryObject) {
        return this.tradeDao.findBy(queryObject);
    }

    public boolean updateTrade(Long id, Trade trade) {
        if (id != null) {
            trade.setId(id);
        } else {
            return false;
        }
        parseTrade(trade);
        this.tradeDao.update(trade);
        return true;
    }

    public Trade getTradeByTitle(String title) {
        return this.tradeDao.getBy("title", title);
    }

    private void parseTrade(Trade trade) {
        if (trade.getParent() != null) {
            trade.setDirPath(trade.getParent().getDirPath() + trade.getCode() + "@");
        } else {
            trade.setDirPath(trade.getCode() + "@");
        }
        if (trade.getSequence() == null) {
            QueryObject qo = new QueryObject();
            qo.setPageSize(1);
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            if (trade.getParent() != null)
                qo.addQuery("obj.parent", trade.getParent(), "=");
            List list = this.getTradeBy(qo).getResult();
            if (list != null && list.size() > 0) {
                Trade m = (Trade) list.get(0);
                trade.setSequence(m.getSequence() + 1);
            } else
                trade.setSequence(1);
        }
    }
}
