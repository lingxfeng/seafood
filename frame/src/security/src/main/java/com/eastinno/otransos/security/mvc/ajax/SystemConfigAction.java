package com.eastinno.otransos.security.mvc.ajax;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemConfigService;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 平台管理action
 * 
 * @author nsz
 */
@Action
public class SystemConfigAction extends AbstractPageCmdAction {
    @Inject
    private ISystemConfigService service;

    public Page doList(WebForm form) {
        IQueryObject qo = new QueryObject();
        form.toPo(qo);
        IPageList pl = service.getSystemConfigBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSPage;
    }

    public Page doSave(WebForm form) {
        Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<?> list = this.service.getSystemConfigBy(qo).getResult();
        if (list != null && list.size() > 0) {
            SystemConfig systemConfig = (SystemConfig) list.get(0);
            return updateEntry(form, systemConfig);
        }
        SystemConfig systemConfig = new SystemConfig();
        form.toPo(systemConfig, true, false);
        String jsonStr = CommUtil.null2String(form.get("jsonStrs"));
        Map<String, Object> map = new HashMap<String, Object>();
        if (!"".equals(jsonStr)) {
            String[] jsonStrs = jsonStr.split(",");
            for (String key : jsonStrs) {
                String value = CommUtil.null2String(form.get(key));
                if (!"".equals(value)) {
                    map.put(key, value);
                }
            }
        }
        String jsonFiles = CommUtil.null2String(form.get("jsonFiles"));
        if (!"".equals(jsonFiles)) {
            String[] files = jsonFiles.split(",");
            for (String fileStr : files) {
                String[] fileStrs = fileStr.split("_");
                String key = fileStrs[0];
                String fileType = fileStrs[1].replaceAll("-", ";");
                String path = FileUtil.uploadFile(form, key,
                        UploadFileConstant.FILE_UPLOAD_PATH + tenant == null ? "" : ("/" + tenant.getCode()) + "/"
                                + UploadFileConstant.FILE_UPLOAD_TYPE_FILE, fileType);
                if (!"".equals(path)) {
                    map.put(key, path);
                }
            }
        }
        if (!map.isEmpty()) {
            systemConfig.setJsonStr(JSONObject.toJSONString(map));
        }
        if (!hasErrors()) {
            Long ret = this.service.addSystemConfig(systemConfig);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            }
        }
        form.jsonResult(systemConfig.toJSonObject());
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemConfig systemConfig = service.getSystemConfig(id);
        return updateEntry(form, systemConfig);
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delSystemConfig(id);
        return pageForExtForm(form);
    }

    public void setService(ISystemConfigService service) {
        this.service = service;
    }

    /**
     * 获取配置信息
     * 
     * @param form
     * @return
     */
    public Page doGetConfig(WebForm form) {
        Tenant tenant = TenantContext.getTenant();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", tenant, "=");
        List<SystemConfig> syscons = this.service.getSystemConfigBy(qo).getResult();
        if (syscons != null && syscons.size() > 0) {
            SystemConfig sysconf = syscons.get(0);
            Map<String, String> map = (Map<String, String>) sysconf.toJSonObject();
            Map<String, String> map2;
            String returnStr = CommUtil.null2String(form.get("returnStrs"));
            if (returnStr != "") {
                map2 = new HashMap<String, String>();
                String[] returnStrs = returnStr.split(",");
                for (String rs : returnStrs) {
                    map2.put(rs, map.get(rs));
                }
            } else {
                map2 = map;
            }
            form.jsonResult(map2);
        }
        return Page.JSONPage;
    }

    /**
     * 删除文件
     * 
     * @param fileNames
     */
    private void deleFile(String... fileNames) {
        for (String filename : fileNames) {
            File file = new File(Globals.APP_BASE_DIR + filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public Page updateEntry(WebForm form, SystemConfig systemConfig) {
        form.toPo(systemConfig, true);
        String jsonStr = CommUtil.null2String(form.get("jsonStrs"));
        String jsonStrOld = CommUtil.null2String(systemConfig.getJsonStr());
        Map<String, Object> map;
        if (!"".equals(jsonStrOld)) {
            map = JSONObject.parseObject(jsonStrOld);
        } else {
            map = new HashMap<String, Object>();
        }
        if (!"".equals(jsonStr)) {
            String[] jsonStrs = jsonStr.split(",");
            for (String key : jsonStrs) {
                String value = CommUtil.null2String(form.get(key));
                if (!"".equals(key)) {
                    map.put(key, value);
                }
            }
        }
        Tenant tenant = TenantContext.getTenant();
        String jsonFiles = CommUtil.null2String(form.get("jsonFiles"));
        if (!"".equals(jsonFiles)) {
            String[] files = jsonFiles.split(",");
            for (String fileStr : files) {
                String[] fileStrs = fileStr.split("_");
                String key = fileStrs[0];
                String fileType = fileStrs[1].replaceAll("-", ";");
                String pathOld = map.get(key) == null ? "" : map.get(key).toString();
                String path = FileUtil.uploadFile(form, key,
                        tenant == null ? "" : (UploadFileConstant.FILE_UPLOAD_PATH + "/" + tenant.getCode()) + "/"
                                + UploadFileConstant.FILE_UPLOAD_TYPE_FILE, fileType);
                if (!"".equals(path)) {
                    map.put(key, path);
                    deleFile(pathOld);
                }
            }
        }
        if (!map.isEmpty()) {
            systemConfig.setJsonStr(JSONObject.toJSONString(map));
        }
        if (!hasErrors()) {
            boolean ret = service.updateSystemConfig(systemConfig.getId(), systemConfig);
            form.addResult("msg", "修改成功");
        }
        form.jsonResult(systemConfig.toJSonObject());
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
}