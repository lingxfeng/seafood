package com.eastinno.otransos.cms.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.cms.domain.SystemAnnounce;
import com.eastinno.otransos.cms.service.IGuestBookService;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.cms.service.ISinglePageNewsService;
import com.eastinno.otransos.cms.service.ISystemAnnounceService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.service.ISystemConfigService;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.web.tools.widget.Html;


/**
 * @intro 网站内容处理工具，主要是内容查询引擎
 * @version v0.1
 * @author maowei
 * @since 2014年6月8日 下午5:58:50
 */
@Component
public class NewsUtil {
    @Resource
    private INewsDirService dirService;// 栏目服务
    @Resource
    private INewsDocService newsDocService;// 文章新闻服务
    @Resource
    private ILinkImgTypeService linkImgTypeService;// 链接图片组服务
    @Resource
    private ILinkImgGroupService linkImgGroupService;// 链接图片服务
    @Resource
    private ISinglePageNewsService singlePageNewsService;// 单页新闻服务
    @Resource
    private ISystemConfigService systemConfigService;// 系统配置服务
    @Resource
    private IGuestBookService guestBookService;// 留言簿服务
    @Resource
    private ISystemAnnounceService systemAnnounceService;// 系统公告服务


    public IPageList getMessageList() {
        IQueryObject qo = new QueryObject();
        return this.guestBookService.getGuestBookBy(qo);
    }


    public SuperNewsQuery getSnq() {
        return getSuperNewsQuery();
    }


    public SuperNewsQuery getSnq(String properties) {
        return getSuperNewsQuery(properties);
    }


    /**
     * 根据NewsUtil创建SuperNewsQuery
     * 
     * @return 返回一个新的SuperNewsQuery对象
     */
    public SuperNewsQuery getSuperNewsQuery() {
        return new SuperNewsQuery(this);
    }


    public SuperNewsQuery getSuperNewsQuery(String properties) {
        return new SuperNewsQuery(this, properties);
    }


    /**
     * 生成以ListForm为基础的分页脚本信息
     * 
     * @return
     */
    public String pageScript() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"/ejf/easyajax/prototype\"></script>");
        sb.append("<script language='javascript'>");
        sb.append("function gotoPage(n){");
        sb.append("$('ListForm').currentPage.value=n;");
        sb.append("$('ListForm').submit();");
        sb.append("}");
        sb.append("</script>");
        return sb.toString();
    }


    /**
     * 生成名为ListForm的表单及currentPage元素
     * 
     * @param action
     *            提交地址
     * @return
     */
    public String pageForm(String action) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form name=\"ListForm\" id=\"ListForm\" method=\"post\" action=\"" + action + "\">");
        sb.append("<input type=\"hidden\" name=\"currentPage\"   value=\"\"/>");
        sb.append("</form>");
        return sb.toString();
    }


    /**
     * 分页列表信息
     * 
     * @param currentPages
     *            当前页
     * @param pagess
     *            每页条数
     * @return
     */
    public String pageation(String currentPages, String pagess) {
        int currentPage = StringUtils.hasText(currentPages) ? Integer.valueOf(currentPages) : 1;
        int pages = StringUtils.hasText(pagess) ? Integer.valueOf(pagess) : 1;
        return showPageHtml(currentPage, pages);
    }


    public String pagination(String currentPages, String pagess, String url) {
        try {
            int currentPage = StringUtils.hasText(currentPages) ? Integer.valueOf(currentPages) : 1;
            int pages = StringUtils.hasText(pagess) ? Integer.valueOf(pagess) : 1;
            return showPageHtmlByUrl(currentPage, pages, url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 分页信息
     * 
     * @param currentPages
     * @param pagess
     * @param url
     * @return
     */
    public String pageation(String currentPages, String pagess, String url) {
        try {
            int currentPage = StringUtils.hasText(currentPages) ? Integer.valueOf(currentPages) : 1;
            int pages = StringUtils.hasText(pagess) ? Integer.valueOf(pagess) : 1;
            return showPageHtmlByUrl(currentPage, pages, url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private String showPageHtml(int currentPage, int pages) {
        String s = "";
        if (currentPage > 1) {
            s += "<a href=# onclick='return gotoPage(1)'>首页</a> ";
            s += "<a href=# onclick='return gotoPage(" + (currentPage - 1) + ")'>上一页</a> ";
        }
        int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
        if (beginPage < pages) {
            s += "第　";
            for (int i = beginPage, j = 0; i <= pages && j < 6; i++, j++) {
                if (i == currentPage)
                    s += "<font color=red>" + i + "</font> ";
                else
                    s += "<a href=# onclick='return gotoPage(" + i + ")'>" + i + "</a> ";
            }
            s += "页　";
        }
        if (currentPage < pages) {
            s += "<a href=# onclick='return gotoPage(" + (currentPage + 1) + ")'>下一页</a> ";
            s += "<a href=# onclick='return gotoPage(" + pages + ")'>末页</a> ";
        }
        // s+=" 转到<input type=text size=2>页";
        return s;
    }


    /*
     * [news.tutorial]对应news.ejf?cmd=tutorial 对应的静态文件为news/tutorial/xx.htm
     * [news.
     * list/dirSn.type]=news.ejf?cmd=list&dirSn=WebForm.get(dirSn)&type=WebForm
     * .get(type) 对应的静态文件为news/list/WebForm.get(dirSn)/WebForm.get(type)
     */
    private String showPageHtmlByUrl(int currentPage, int pages, String url) {
        String s = "", suffix = "";
        Html htmlTool = Html.getInstance();
        String p =
                htmlTool.isShowHtmlPage() ? htmlTool.handleModuleCmdHtml(url) : htmlTool
                    .handleModuleCmdUrl(url);
        if (htmlTool.isShowHtmlPage()) {
            p = "/" + p;
            suffix = ".html";
        }
        else {
            String mark = p.indexOf('?') >= 0 ? "&currentPage=" : "?currentPage=";
            p += mark;
        }
        if (currentPage > 1) {
            s += "<a href=" + p + "1" + suffix + ">首页</a> ";
            s += "<a href=" + p + (currentPage - 1) + suffix + ">上一页</a> ";
        }
        int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
        if (beginPage < pages) {
            s += "第　";
            for (int i = beginPage, j = 0; i <= pages && j < 6; i++, j++) {
                if (i == currentPage)
                    s += "<font color=red>" + i + "</font> ";
                else
                    s += "<a href=" + p + i + suffix + ">" + i + "</a> ";
            }
            s += "页　";
        }
        if (currentPage < pages) {
            s += "<a href=" + p + (currentPage + 1) + suffix + ">下一页</a> ";
            s += "<a href=" + p + pages + suffix + ">末页</a> ";
        }
        // s+=" 转到<input type=text size=2>页";
        return s;
    }


    // 常用方法getNewsList的简写形式
    /**
     * 得到某一个目录及下级目录的前n条信息
     * 
     * @param dirSn
     * @param number
     * @return
     */
    public List<NewsDoc> nl(String dirSn, int number) {
        return getNewsList(dirSn, number);
    }


    public List<NewsDoc> nl(String dirSn, int number, int titleSize) {
        return getNewsList(dirSn, number, titleSize);
    }


    public List<NewsDoc> nl(String dirSn, int number, int titleSize, boolean hasDot) {
        return getNewsList(dirSn, number, titleSize, hasDot);
    }


    /**
     * 根据目录编号得到一个目录的下级目录
     * 
     * @param dirSn
     * @return
     */
    public List<NewsDir> getChildren(String dirCode) {
        NewsDir dir = this.dirService.getNewsDirByCode(dirCode);
        if (dir != null)
            return dir.getNormalChildren();
        else
            return null;
    }


    /**
     * 根据目录编号，返回某一个目录下的前几篇文章
     * 
     * @param dirSn
     *            目录编号
     * @param number
     *            需要返回前几篇
     * @return
     */
    public List<NewsDoc> getNewsList(String dirSn, int number) {
        return this.getSuperNewsQuery().dir(dirSn).number(number).getList();
    }


    /**
     * 得到编号dirSn目录下的前几篇文章,不包含内容
     * 
     * @param dirSn
     *            目录编号
     * @param number
     *            要显示的条数
     * @param titleSize
     *            标题的显示长度
     * @return
     */
    public List<NewsDoc> getNewsList(String dirSn, int number, int titleSize) {
        return getNewsList(dirSn, number, titleSize, false);
    }


    /**
     * 得到编号dirSn目录下的前几篇文章文章列表，不包含内容
     * 
     * @param dirSn
     *            目录编号
     * @param number
     *            要显示的条数
     * @param titleSize
     *            标题的显示长度
     * @param hasDot
     *            是否使用省略号来替换被截去的字符
     * @return
     */
    public List<NewsDoc> getNewsList(String dirSn, int number, int titleSize, boolean hasDot) {
        SuperNewsQuery snq = this.getSuperNewsQuery().dir(dirSn).number(number).titleSize(titleSize);
        if (hasDot)
            snq.hasDot();
        return snq.getList();
    }


    public List<NewsDoc> getNewsList(String dirSn, int number, int titleSize, int contentSize) {
        return getNewsList(dirSn, number, titleSize, contentSize, true);
    }


    public List<NewsDoc> getNewsList(String dirSn, int number, int titleSize, int contentSize, boolean hasDot) {
        SuperNewsQuery snq =
                this.getSuperNewsQuery().dir(dirSn).number(number).titleSize(titleSize)
                    .introSize(contentSize);
        if (hasDot)
            snq.hasDot();
        return snq.getList();

    }


    public List<NewsDoc> getNewsList(String dirSn, int number, boolean hasTop, boolean isElite,
            int titleSize, boolean hasDot) {
        SuperNewsQuery snq = this.getSuperNewsQuery().dir(dirSn).number(number).titleSize(titleSize);
        if (isElite)
            snq.elite();
        if (hasDot)
            snq.hasDot();
        if (hasTop) {
            snq.top();
        }
        return snq.getList();
    }


    /**
     * 得到一个文章栏目
     * 
     * @param dirSn
     *            栏目编号
     * @return 若该编号存在，则返回该目录编号，否则返回null
     */
    public NewsDir dir(String dirSn) {
        return getDir(dirSn);
    }


    public NewsDir dirById(String dirId) {
        return dir(new Long(dirId));
    }


    public NewsDir dir(Long dirId) {
        return this.dirService.getNewsDir(dirId);
    }


    public NewsDir dir(Integer dirId) {
        return this.dirService.getNewsDir(new Long(dirId));
    }


    public List<NewsDir> getRootDirs() {
        return this.dirService.getRootsDir();
    }


    /**
     * 此处修改
     */
    public NewsDir getDir(String dirSn) {
        return this.dirService.getNewsDirByCode(dirSn);
    }


    public List<SinglePageNews> getPageNews() {
        return pageNews();
    }


    /**
     * 获取所有前台显示的单页栏目页面对象
     * 
     * @return 前台显示的顶级单页信息
     */
    @SuppressWarnings("unchecked")
    public List<SinglePageNews> pageNews() {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.display", true, "=");
        qo.addQuery("(obj.parent IS EMPTY)", null);
        qo.setOrderBy("sequence desc");
        return this.singlePageNewsService.getSinglePageNewsBy(qo).getResult();
    }


    /**
     * 得到一个单页新闻对象
     * 
     * @param code
     * @return
     */
    public SinglePageNews singlePageNews(String code) {
        return this.singlePageNewsService.getSinglePageNewsByCode(code);
    }


    /**
     * 获取链接图片组
     * 
     * @param pageSize
     * @return
     */
    public List<LinkImgGroup> getLinkImg(String code, int pageSize) {
        LinkImgType linkImgType = linkImgTypeService.getLinkImgTypeByCode(code);
        List<LinkImgGroup> imgGroups = new ArrayList<LinkImgGroup>();
        if (linkImgType != null) {
            QueryObject qo = new QueryObject();
            qo.addQuery("type", linkImgType, "=");
            qo.setOrderType("asc");
            qo.setOrderBy("sequence");
            qo.setLimit(pageSize);
            IPageList pageList = linkImgGroupService.getLinkImgGroupBy(qo);
            if (!(pageList == null || pageList.getResult() == null || pageList.getResult().size() == 0)) {
                imgGroups = pageList.getResult();
            }
        }
        return imgGroups;
    }


    /**
     * 根据指定排序号查询指定CODE图片组下的单张图片
     * 
     * @param code
     *            图片组所属分类CODE
     * @param sequence
     *            指定排序号
     * @return
     */
    public LinkImgGroup getLinkImgBysequence(String code, int sequence) {
        return this.linkImgGroupService.getLinkImgGroupBy(code, sequence);
    }


    @SuppressWarnings("unchecked")
    public List<SystemAnnounce> systemAnnounce(String type, int num) {
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.types", type, "=");
        qo.addQuery("obj.auditing", true, "=");
        qo.setOrderBy("displayTime");
        qo.setOrderType("desc");
        qo.setPageSize(num);
        List<SystemAnnounce> list = this.systemAnnounceService.getSystemAnnounceBy(qo).getResult();
        return list;
    }


    /**
     * 获取站点全局配置
     * 
     * @return 返回SystemConfig对象
     */
    @SuppressWarnings("unchecked")
    public SystemConfig getSystemConfig() {
        QueryObject qo = new QueryObject();
        List<SystemConfig> list = this.systemConfigService.getSystemConfigBy(qo).getResult();
        if (list != null && list.size() == 1) {
            return (SystemConfig) list.get(0);
        }
        return null;
    }

    public List<NewsDoc> getNewDocList(String code,int num){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.dir.code", code, "=");
    	qo.setOrderBy("sequence");
    	qo.setOrderType("asc");
    	if(num==-1){
    		qo.setLimit(-1);
    	}else{
    		qo.setPageSize(num);
    	}
    	return this.newsDocService.getNewsDocBy(qo).getResult();
    }
    
    
    public INewsDocService getNewsDocService() {
        return newsDocService;
    }


    public void setNewsDocService(INewsDocService newsDocService) {
        this.newsDocService = newsDocService;
    }


    public INewsDirService getDirService() {
        return dirService;
    }


    public void setDirService(INewsDirService dirService) {
        this.dirService = dirService;
    }


    public ISinglePageNewsService getSinglePageNewsService() {
        return singlePageNewsService;
    }


    public void setSinglePageNewsService(ISinglePageNewsService singlePageNewsService) {
        this.singlePageNewsService = singlePageNewsService;
    }


    public ISystemConfigService getSystemConfigService() {
        return systemConfigService;
    }


    public void setSystemConfigService(ISystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }


    public IGuestBookService getGuestBookService() {
        return guestBookService;
    }


    public void setGuestBookService(IGuestBookService guestBookService) {
        this.guestBookService = guestBookService;
    }


    public ISystemAnnounceService getSystemAnnounceService() {
        return systemAnnounceService;
    }


    public void setSystemAnnounceService(ISystemAnnounceService systemAnnounceService) {
        this.systemAnnounceService = systemAnnounceService;
    }


    public ILinkImgTypeService getLinkImgTypeService() {
        return linkImgTypeService;
    }


    public void setLinkImgTypeService(ILinkImgTypeService linkImgTypeService) {
        this.linkImgTypeService = linkImgTypeService;
    }

}
