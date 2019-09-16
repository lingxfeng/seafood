package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.dao.IDynamicStaticPairDAO;
import com.eastinno.otransos.core.domain.DynamicStaticPair;
import com.eastinno.otransos.core.service.IDynamicStaticPairService;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.tools.AutoChangeLink;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.web.tools.widget.Html;

/**
 * DynamicStaticPairServiceImpl
 */
@Service
public class DynamicStaticPairServiceImpl implements IDynamicStaticPairService {
    @Resource
    private IHtmlGeneratorService htmlGenerator;
    // private long interval = 60;// 检测周期
    // private long firstWaitTime = 60;// 首次等待300秒
    @Resource
    private IDynamicStaticPairDAO dynamicStaticPairDAO;
    private static final Logger logger = Logger.getLogger(DynamicStaticPairServiceImpl.class);

    public Long addDynamicStaticPair(DynamicStaticPair dynamicStaticPair) {
        TenantObjectUtil.setObject(dynamicStaticPair);
        this.dynamicStaticPairDAO.save(dynamicStaticPair);
        if (dynamicStaticPair != null && dynamicStaticPair.getId() != null) {
            return dynamicStaticPair.getId();
        }
        return null;
    }

    public DynamicStaticPair getDynamicStaticPair(Long id) {
        DynamicStaticPair DynamicStaticPair = this.dynamicStaticPairDAO.get(id);
        if (DynamicStaticPair != null) {
            return DynamicStaticPair;
        }
        return null;
    }

    public boolean delDynamicStaticPair(Long id) {
        if (id != null) {
            this.dynamicStaticPairDAO.remove(id);
            DynamicStaticPair DynamicStaticPair = this.getDynamicStaticPair(id);
            if (DynamicStaticPair == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean batchDelDynamicStaticPairs(List<Serializable> dynamicStaticPairIds) {
        for (Serializable id : dynamicStaticPairIds) {
            delDynamicStaticPair((Long) id);
        }
        return true;
    }

    public IPageList getDynamicStaticPairBy(IQueryObject qo) {
        return this.dynamicStaticPairDAO.findBy(qo);
    }

    public boolean updateDynamicStaticPair(Long id, DynamicStaticPair dynamicStaticPair) {
        this.dynamicStaticPairDAO.saveAndFlush(dynamicStaticPair);
        return true;
    }

    public int publish(DynamicStaticPair dsp) {
        AutoChangeLink[] auto = dsp.getAutoChangeLinks();
        int count = 0;
        for (int j = 0; j < auto.length; j++) {
            this.htmlGenerator.process(auto[j]);
            count++;
        }
        dsp.setVdate(new Date());
        updateDynamicStaticPair(dsp.getId(), dsp);
        return count;
    }

    public void autoPublish() {
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);// 不分页查询全部的数据
        qo.addQuery("obj.status", new Integer(1), "=");
        List<?> list = getDynamicStaticPairBy(qo).getResult();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                DynamicStaticPair dsp = (DynamicStaticPair) list.get(i);
                Date date = new Date();
                // (当前服务器日期－当前对象最后一次静态化日期)/1000与当前对象静态化周期比较
                Boolean b = (date.getTime() - dsp.getVdate().getTime()) / 1000 > dsp.getIntervals();
                if (dsp.getIntervals() != null && dsp.getIntervals() > 0 && b) {
                    Long startDate = new Date().getTime();
                    this.publish(dsp);
                    Long t = new Date().getTime() - startDate;
                    logger.info("【静态化耗时:" + t + "毫秒】" + dsp.getDynamicUrl());
                }
            }
        }
    }

    public String converUrl(String url) {
        if (StringUtils.hasText(url)) {
            if (url.indexOf(".java") != -1) {// 动转静
                if (!Html.getInstance().isShowHtmlPage()) {
                    return url;
                }
                DynamicStaticPair ds = this.dynamicStaticPairDAO.getBy("url", url);
                return ds != null ? ds.getPath() : url;
            } else {// 静转动
                DynamicStaticPair ds = this.dynamicStaticPairDAO.getBy("path", url);
                return ds != null ? ds.getUrl() : url;
            }
        }
        return url;
    }

    @Override
    public DynamicStaticPair getDynamicStaticPairByUrl(String url) {
        if (StringUtils.hasText(url)) {
            return this.dynamicStaticPairDAO.getBy("url", url);
        }
        return null;
    }

    public void setDynamicStaticPairDAO(IDynamicStaticPairDAO dynamicStaticPairDAO) {
        this.dynamicStaticPairDAO = dynamicStaticPairDAO;
    }

    public void setHtmlGenerator(IHtmlGeneratorService htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }
}
