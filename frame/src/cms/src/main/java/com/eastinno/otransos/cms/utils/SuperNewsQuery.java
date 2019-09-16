package com.eastinno.otransos.cms.utils;

import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.query.NewsDocQuery;
import com.eastinno.otransos.core.service.DateUtil;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 用来供页面中直接嵌入式查询文章信息，使得在页面中加入信息查询代码更加容易
 * 
 * @version 2.0
 * @author lengyu
 * @date 2014年6月12日-下午4:52:50
 */
public class SuperNewsQuery {

    private Integer number;// 列表条数

    private Integer topNumber;// 推荐新闻或图片新闻的条数，也即在查询中要排除的信息条数

    private boolean elite;// 查询推荐新闻

    private boolean top = false;// 查询置顶头条新闻

    private String order;// 结果排序(createDate desc)

    private String dirCode;// 目录编号

    private int page = 1;
    private int pageSize = 20;

    private boolean exclude;// 是否排除顶级列表信息

    private Date startDate;
    private Date endDate;

    private ResultFormat resultFomat = new ResultFormat();

    private List<NewsDoc> topList;// 图片信息列表

    private List<NewsDoc> list;

    private NewsUtil newsUtil;

    private String key;

    public void setTop(boolean top) {
        this.top = top;
    }

    /**
     * 返回一个SuperNewsQuery，然后可以使用News。该对象不直接使用，一般由 #set(NQ=$NU.dirCodeq.hasPic().topNumber
     * (10).orderBy().number(20).hasPic().topNumber(2)) #foreach($info in $NQ.picList) #end #foreach($info in $NQ.list)
     * #end
     * 
     * @param newsUtil
     */
    public SuperNewsQuery(NewsUtil newsUtil) {
        this.newsUtil = newsUtil;
    }

    /**
     * 根据属性信息，返回一个SuperNewsQuery，属性参数之间以分，在页面中可以使用下面的方式调用 #foreach($info in
     * $NU.dirCodeq("dir:'news';number:7;elite;haspic").list) #end
     * 
     * @param newsUtil
     * @param properties
     */
    public SuperNewsQuery(NewsUtil newsUtil, String properties) {
        this.newsUtil = newsUtil;
        if (properties != null) {
            this.parseProperty(properties);
        }
    }

    /**
     * 解析构造参数的属性
     * 
     * @param properties
     */
    private void parseProperty(String properties) {
        String[] p = properties.split(";");
        for (int i = 0; i < p.length; i++) {
            if (p[i] != null && !"".equals(p[i])) {
                String pro = p[i].trim();
                String[] pvalue = new String[2];
                if (pro.indexOf(":") > 0) {
                    pvalue = pro.split(":");

                } else
                    pvalue[0] = pro;
                pvalue[0] = pvalue[0].trim();// 去除空格
                if (pvalue[1] != null)
                    pvalue[1] = pvalue[1].trim();// 去除空格
                if ("number".equalsIgnoreCase(pvalue[0])) {
                    this.number(Integer.parseInt(pvalue[1]));
                } else if ("topnumber".equalsIgnoreCase(pvalue[0])) {
                    this.topNumber(Integer.parseInt(pvalue[1]));
                } else if ("elite".equalsIgnoreCase(pvalue[0])) {
                    this.elite();
                } else if ("order".equalsIgnoreCase(pvalue[0])) {
                    this.order(pvalue[1]);
                } else if ("dirCode".equalsIgnoreCase(pvalue[0]) || "dir".equalsIgnoreCase(pvalue[0])) {
                    this.dir(pvalue[1]);
                } else if ("titleSize".equalsIgnoreCase(pvalue[0])) {
                    this.titleSize(Integer.parseInt(pvalue[1]));
                } else if ("introSize".equalsIgnoreCase(pvalue[0])) {
                    this.introSize(Integer.parseInt(pvalue[1]));
                } else if ("hasDot".equalsIgnoreCase(pvalue[0])) {
                    this.hasDot();
                } else if ("exclude".equalsIgnoreCase(pvalue[0])) {
                    this.exclude();
                } else if ("loadContent".equalsIgnoreCase(pvalue[0])) {
                    this.loadContent();
                } else if ("page".equalsIgnoreCase(pvalue[0])) {
                    this.page = Integer.parseInt(pvalue[1]);
                } else if ("pageSize".equalsIgnoreCase(pvalue[1])) {
                    this.pageSize = Integer.parseInt(pvalue[1]);
                } else if ("key".equalsIgnoreCase(pvalue[1])) {
                    this.key = pvalue[1];
                }
            }
        }
    }

    /**
     * 列表总条数为
     * 
     * @param number
     * @return
     */
    public SuperNewsQuery number(int number) {
        this.number = number;
        return this;
    }

    public SuperNewsQuery topNumber(int number) {
        this.topNumber = number;
        return this;
    }

    public SuperNewsQuery elite() {
        this.elite = true;
        return this;
    }

    public SuperNewsQuery top() {
        this.top = true;
        return this;
    }

    public SuperNewsQuery order(String order) {
        this.order = order;
        return this;
    }

    public SuperNewsQuery dir(String dirCode) {
        this.dirCode = dirCode;
        return this;
    }

    public SuperNewsQuery titleSize(int size) {
        this.resultFomat.titleSize = size;
        return this;
    }

    public SuperNewsQuery introSize(int size) {
        this.resultFomat.introSize = size;
        return this;
    }

    public SuperNewsQuery hasDot() {
        this.resultFomat.hasDot = true;
        return this;
    }

    public SuperNewsQuery loadContent() {
        this.resultFomat.loadContent = true;
        return this;
    }

    public SuperNewsQuery exclude() {
        this.exclude = true;
        return this;
    }

    public SuperNewsQuery key(String key) {
        this.key = key;
        return this;
    }

    public SuperNewsQuery page(String page) {
        this.page = StringUtils.hasText(page) ? Integer.valueOf(page) : 1;
        return this;
    }

    public SuperNewsQuery pageSize(String sz) {
        this.pageSize = Integer.parseInt(sz);
        return this;
    }

    /**
     * 查询days天前的文章
     * 
     * @param days
     * @return
     */
    public SuperNewsQuery before(Integer days) {
        Date[] ds = DateUtil.getBefore(days);
        this.startDate = ds[0];
        this.endDate = ds[1];
        return this;
    }

    /**
     * 查询本周内发布的文章
     * 
     * @return
     */
    public SuperNewsQuery thisWeek() {
        return this.before(7);
    }

    /**
     * 查询本年度内发布的文章
     * 
     * @return
     */
    public SuperNewsQuery thisYear() {
        Date[] ds = DateUtil.getThisYear();
        this.startDate = ds[0];
        this.endDate = ds[1];
        return this;
    }

    /**
     * 查询本月内发布的文章
     * 
     * @return
     */
    public SuperNewsQuery thisMonth() {
        Date[] ds = DateUtil.getThisMonth();
        this.startDate = ds[0];
        this.endDate = ds[1];
        return this;
    }

    public IPageList getPageList() {
        NewsDocQuery docQuery = parseNewsDocQuery();
        if (topList != null && topList.size() > 0) {
            for (int i = 0; i < topList.size(); i++)// 这里可以考虑改成not
            // in查询
            {
                docQuery.addQuery("obj.id", topList.get(i).getId(), "<>");
            }
        }
        if (page > 0)
            docQuery.setCurrentPage(page);
        if (pageSize != 0)
            docQuery.setPageSize(pageSize);
        IPageList pageList = newsUtil.getNewsDocService().getNewsDocBy(parseNewsDocQuery());// 执行查询
        List<NewsDoc> list = pageList.getResult();
        // 下面开始执行根据ResultFormat数据信息进行转换处理
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++)
                list.set(i, this.resultFomat.newsDocPO2VO(list.get(i)));
        }
        return pageList;
    }

    public List<NewsDoc> getList() {
        if (list == null) {
            loadList();
        }
        return list;
    }

    public List<NewsDoc> getPicList() {
        if (topList == null) {
            loadTopList();
        }
        return topList;
    }

    public List<NewsDoc> getEliteList() {
        return getPicList();
    }

    private void loadList() {
        if (this.exclude && this.topList == null) {
            loadTopList();
        }
        NewsDocQuery docQuery = parseNewsDocQuery();
        if (topList != null && topList.size() > 0) {
            for (int i = 0; i < topList.size(); i++)// 这里可以考虑改成not in查询
            {
                docQuery.addQuery("obj.id", topList.get(i).getId(), "<>");
            }
        }
        IPageList pageList = newsUtil.getNewsDocService().getNewsDocBy(parseNewsDocQuery());// 执行查询
        List<NewsDoc> list = pageList.getResult();
        // 下面开始执行根据ResultFormat数据信息进行转换处理
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++)
                list.set(i, this.resultFomat.newsDocPO2VO(list.get(i)));
        }
        this.list = list;
    }

    private NewsDocQuery parseNewsDocQuery() {
        NewsDocQuery docQuery = new NewsDocQuery();

        if (this.elite) {
            docQuery.addQuery("obj.elite", new Integer(1), "=");
        }
        if (this.top) {
            docQuery.addQuery("obj.isTop", new Integer(1), "=");
        }
        if (this.dirCode != null) {
            NewsDir dir = newsUtil.getDirService().getNewsDirByCode(dirCode);
            if (dir != null) {
                docQuery.addQuery("obj.dirPath", dir.getDirPath() + "%", "like");
            } else
                docQuery.addQuery("1<>1", null);
        }

        // docQuery.addQuery("obj.auditing", true, "=");
        docQuery.addQuery("obj.status", 2, "=");
        if (top) {
            docQuery.addQuery("obj.top", true, "=");
        }
        if (this.startDate != null)
            docQuery.addQuery("obj.createDate", startDate, ">=");
        if (this.endDate != null)
            docQuery.addQuery("obj.createDate", endDate, "<=");
        docQuery.setCurrentPage(this.page);
        docQuery.setPageSize(this.topNumber != null ? this.topNumber : this.number != null ? this.number : this.pageSize);
        if (docQuery.getPageSize() == null || docQuery.getPageSize() == 0)
            docQuery.setPageSize(10);
        if (StringUtils.hasText(this.order)) {
            docQuery.setOrderBy(this.order);
        } else {
            // 置顶>推荐>排序号>输入日期
            docQuery.setOrderBy("isTop desc,obj.elite desc,obj.sequence desc,obj.createDate desc");
        }
        if (this.key != null)
            docQuery.setKeyword(this.key);
        return docQuery;
    }

    private void loadTopList() {
        // 只有开取了exclude标志，才会打开picList及eliteList功能
        if (this.exclude && (this.elite)) {
            IPageList pageList = newsUtil.getNewsDocService().getNewsDocBy(parseNewsDocQuery());// 执行查询
            List<NewsDoc> list = pageList.getResult();
            // 下面开始执行根据ResultFormat数据信息进行转换处理
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++)
                    list.set(i, this.resultFomat.newsDocPO2VO(list.get(i)));
            }
            this.topList = list;
        }
    }

    private class ResultFormat {
        private int introSize;// 简介显示的长度
        private int titleSize;// 文章标题显示条数
        private boolean hasDot;// 是否使用省略号来替换被截去的字符
        private boolean loadContent;

        public NewsDoc newsDocPO2VO(NewsDoc doc) {
            NewsDoc d = new NewsDoc();
            d.setId(doc.getId());
            d.setTitle(doc.getTitle());
            d.setContent(doc.getContent());
            d.setDir(doc.getDir());
            d.setDirPath(doc.getDirPath());
            d.setUrl(doc.getUrl());
            d.setIconPath(doc.getIconPath());
            d.setDescription(doc.getDescription());
            d.setKeywords(doc.getKeywords());
            d.setIsTop(doc.getIsTop());
            d.setSequence(doc.getSequence());
            d.setElite(doc.getElite());
            d.setPutDate(doc.getPutDate());
            d.setCount(doc.getCount());
            if (loadContent)
                d.setContent(doc.getContent());
            if (titleSize > 0) {
                d.setTitle(CommUtil.format(doc.getTitle(), titleSize, hasDot));
            }
            if (introSize > 0) {
                d.setDescription(CommUtil.format(doc.getDescription(), introSize, hasDot));
            }
            return d;
        }
    };

}
