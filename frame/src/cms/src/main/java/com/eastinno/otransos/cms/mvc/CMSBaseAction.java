package com.eastinno.otransos.cms.mvc;

import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.cms.service.ILinksManageService;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.cms.service.ISinglePageNewsService;
import com.eastinno.otransos.cms.utils.NewsUtil;
import com.eastinno.otransos.cms.utils.StatciStr;
import com.eastinno.otransos.cms.utils.cache.MemeryCache;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.IDynamicStaticPairService;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.service.ISystemConfigService;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;


/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年12月3日 下午6:09:48
 * @Intro CMS基础Action
 */
public abstract class CMSBaseAction extends SaaSCMSBaseAction {
    @Inject
    private NewsUtil newsUtil;
    @Inject
    private ILinksManageService linksManageService;
    @Inject
    private INewsDirService newsDirService;
    @Inject
    private ILinkImgGroupService linkImgGroupService;
    @Inject
    private INewsDocService newsDocService;
    @Inject
    private ISystemConfigService systemConfigService;
    @Inject
    private ILinkImgTypeService linkImgTypeService;
    @Inject
    private ISinglePageNewsService singlePageNewsService;
    @Inject
    private IDynamicStaticPairService dynamicStaticPairService;


    @Override
    public Object doAfter(WebForm form, Module module) {
        form.addResult("NU", newsUtil);
        form.addResult("user", UserContext.getUser());
        form.addResult("member", UserContext.getMember());
        String tenantCode = TenantContext.getTenant().getCode();
        form.addResult("tenantCode", tenantCode);

        List<?> linkList = null;// linksManageService.getShowLinksManage();

        if (MemeryCache.getInstance().exit(StatciStr.DO_BEFORE.LINK_LIST + tenantCode)) {
            linkList = (List<?>) MemeryCache.getInstance().get(StatciStr.DO_BEFORE.LINK_LIST + tenantCode);
        }
        else {
            linkList = linksManageService.getShowLinksManage();
            MemeryCache.getInstance().put(StatciStr.DO_BEFORE.LINK_LIST + tenantCode, linkList);
        }

        List<?> dirList = newsDirService.getShowNewsDir();

        if (MemeryCache.getInstance().exit(StatciStr.DO_BEFORE.DIR_LIST + tenantCode)) {
            dirList = (List<?>) MemeryCache.getInstance().get(StatciStr.DO_BEFORE.DIR_LIST + tenantCode);
        }
        else {
            dirList = newsDirService.getShowNewsDir();
            MemeryCache.getInstance().put(StatciStr.DO_BEFORE.DIR_LIST + tenantCode, dirList);
        }

        SystemConfig systemConfig = null;
        form.addResult("friendLink", linkList);
        form.addResult("dirList", dirList);
        form.addResult("pair", dynamicStaticPairService);// URL动静转换
        if (MemeryCache.getInstance().exit(StatciStr.DO_BEFORE.SYSTEM_CONFIG + tenantCode)) {
            systemConfig =
                    (SystemConfig) MemeryCache.getInstance().get(
                        StatciStr.DO_BEFORE.SYSTEM_CONFIG + tenantCode);
        }
        else {
            systemConfig = systemConfigService.getSystemConfig();
            MemeryCache.getInstance().put(StatciStr.DO_BEFORE.SYSTEM_CONFIG + tenantCode, systemConfig);
        }
        if (systemConfig != null && StringUtils.hasText(systemConfig.getJsonStr())) {
            JSONObject jsonObj = JSONObject.parseObject(systemConfig.getJsonStr());
            systemConfig.setJsonObj(jsonObj);
        }
        form.addResult("siteConfig", systemConfig);
        form.addResult("sysConfig", systemConfig);
        return super.doAfter(form, module);
    }


    public Page doForward(WebForm f) {
        String path = CommUtil.null2String(f.get("path"));
        return new Page("forward", "/" + TenantContext.getTenant().getCode() + "/news/" + path + ".html",
            "template");
    }


    public NewsUtil getNewsUtil() {
        return newsUtil;
    }


    public void setNewsUtil(NewsUtil newsUtil) {
        this.newsUtil = newsUtil;
    }


    public ILinksManageService getLinksManageService() {
        return linksManageService;
    }


    public void setLinksManageService(ILinksManageService linksManageService) {
        this.linksManageService = linksManageService;
    }


    public INewsDirService getNewsDirService() {
        return newsDirService;
    }


    public void setNewsDirService(INewsDirService newsDirService) {
        this.newsDirService = newsDirService;
    }


    public ILinkImgGroupService getLinkImgGroupService() {
        return linkImgGroupService;
    }


    public void setLinkImgGroupService(ILinkImgGroupService linkImgGroupService) {
        this.linkImgGroupService = linkImgGroupService;
    }


    public INewsDocService getNewsDocService() {
        return newsDocService;
    }


    public void setNewsDocService(INewsDocService newsDocService) {
        this.newsDocService = newsDocService;
    }


    public void setDynamicStaticPairService(IDynamicStaticPairService dynamicStaticPairService) {
        this.dynamicStaticPairService = dynamicStaticPairService;
    }


    public ISystemConfigService getSystemConfigService() {
        return systemConfigService;
    }


    public void setSystemConfigService(ISystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }


    public ILinkImgTypeService getLinkImgTypeService() {
        return linkImgTypeService;
    }


    public void setLinkImgTypeService(ILinkImgTypeService linkImgTypeService) {
        this.linkImgTypeService = linkImgTypeService;
    }


    public void setSinglePageNewsService(ISinglePageNewsService singlePageNewsService) {
        this.singlePageNewsService = singlePageNewsService;
    }


    public ISinglePageNewsService getSinglePageNewsService() {
        return singlePageNewsService;
    }


    public IDynamicStaticPairService getDynamicStaticPairService() {
        return dynamicStaticPairService;
    }

}
