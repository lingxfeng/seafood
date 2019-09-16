package com.eastinno.otransos.security.mvc.ajax;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.container.annonation.PermissionVerify;
import com.eastinno.otransos.container.annonation.RoleVerify;
import com.eastinno.otransos.core.service.ExcelImport;
import com.eastinno.otransos.core.service.ExcelReport;
import com.eastinno.otransos.core.support.ActionUtil;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.annotation.Remark;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.security.domain.ResourceType;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.IPermissionService;
import com.eastinno.otransos.security.service.IResourceService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebConfig;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.tools.IPageList;

@Action
@PermissionVerify
public class ResourceAction extends AbstractPageCmdAction {
    @Inject
    private IResourceService service;
    @Inject
    private IPermissionService permissionService;
    @InjectDisable
    private static Map<String, List<String>> moduls = new HashMap();

    public Page doIndex(WebForm f, Module m) {
        return page("list");
    }

    public Page doList(WebForm form) {
        String pack = CommUtil.null2String(form.get("pack"));
        String action = CommUtil.null2String(form.get("action"));
        String actionName = CommUtil.null2String(form.get("actionName"));
        QueryObject query = (QueryObject) form.toPo(QueryObject.class);
        String permissionId = CommUtil.null2String(form.get("permissionId"));
        if (!"".equals(permissionId)) {
            Permission permission = this.permissionService.getPermission(Long.parseLong(permissionId));
            String msg = "0";
            for (Resource r : permission.getResources()) {
                msg = msg + "," + r.getId();
            }
            query.addQuery("obj.id in (" + msg + ")");
        } else {
            if (!"".equals(action)) {
                query.addQuery("obj.resStr", "%" + action + "%", "like");
            } else if (!"".equals(pack)) {
                query.addQuery("obj.resStr", "%" + pack + "%", "like");
            } else if(!"".equals(actionName)){
            	query.addQuery("obj.resStr", "%" + actionName + "%", "like");
            }
            Tenant tenant = ShiroUtils.getTenant();
            query.addQuery("( obj.tenant.id=" + tenant.getId() + " or obj.id in (" + ShiroUtils.getShiroUser().getPermissionIds() + "))");
        }
        IPageList pageList = this.service.getResourceBy(query);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        List<Serializable> list = ActionUtil.processIds(form);
        if ((list != null) && (!list.isEmpty())) {
            for (Serializable i : list) {
                this.service.delResource((Long) i);
            }
        } else
            this.service.delResource(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        Resource cmd = new Resource();
        form.toPo(cmd, true, false);
        if (!hasErrors()) {
            this.service.addResource(cmd);
        }
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        String idstr = CommUtil.null2String(form.get("id"));
        Resource resource = this.service.getResource(Long.parseLong(idstr));
        form.toPo(resource, true, false);
        if (!hasErrors())
            this.service.updateResource(Long.parseLong(idstr), resource);
        return pageForExtForm(form);
    }

    public Page doImportModuleResource(WebForm form) {
        String action = CommUtil.null2String(form.get("action"));
        String pack = CommUtil.null2String(form.get("pack"));
        List list = new ArrayList();
        if (!"".equals(action)) {
            list.add(action);
        } else if (!"".equals(pack)) {
            List as = (List) moduls.get(pack);
            if (as != null)
                list.addAll(as);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                Class clz = Class.forName((String) list.get(i));
                Method[] methods = clz.getMethods();
                Remark actionAnn = (Remark) clz.getAnnotation(Remark.class);
                String actionName = actionAnn == null ? clz.getName() : actionAnn.value();
                String simActionName = clz.getSimpleName();
                for (Method m : methods)
                    if ((Modifier.isPublic(m.getModifiers())) && (!m.getDeclaringClass().equals(Object.class))) {
                        if ((!m.getName().equals("execute")) && (!m.getName().equals("doBefore")) && (!m.getName().equals("doAfter"))) {
                            Class[] p = m.getParameterTypes();
                            boolean is = false;
                            if ((p == null) || (p.length == 0))
                                is = true;
                            else if ((WebForm.class.isAssignableFrom(p[0])) || (Module.class.isAssignableFrom(p[0])))
                                is = true;
                            if (is) {
                                String name = m.getName();
                                RoleVerify rv = (RoleVerify) clz.getAnnotation(RoleVerify.class);
                                if ((rv != null) && (StringUtils.hasLength(rv.value()))) {
                                    name = rv.value();
                                }
                                Remark methodAnn = (Remark) m.getAnnotation(Remark.class);
                                String methodName = methodAnn == null ? m.getName() : methodAnn.value();

                                String desc = clz.getName() + ":" + m.getName();
                                String descName = actionName + ":" + methodName;
                                addModuleResource(desc, name, methodName, descName);
                            }
                        }
                    }
                addModuleResource(clz.getName() + ":ALL", "ALL", "所有方法", actionName + ":所有方法");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pageForExtForm(form);
    }

    private void addModuleResource(String res, String desc, String methodName, String descName) {
        Resource r = this.service.getResource(res);
        if (r == null) {
            Resource rs = new Resource();
            rs.setResStr(res);
            rs.setType(ResourceType.MODULE);
            rs.setDesciption(desc);
            rs.setActionName(methodName);
            rs.setTenant(ShiroUtils.getTenant());
            rs.setDescName(descName);
            this.service.addResource(rs);
        } else if (desc != r.getDesciption()) {
            r.setResStr(res);
            r.setType(ResourceType.MODULE);
            r.setDesciption(desc);
            r.setActionName(methodName);
            r.setTenant(ShiroUtils.getTenant());
            r.setDescName(descName);
            this.service.updateResource(r.getId(), r);
        }
    }

    /**
     * 获取所有的系统模块(系统资源)
     * 
     * @param form
     * @return
     */
    public Page doGetModules(WebForm form) {
        String pack = CommUtil.null2String(form.get("id"));
        if (moduls.isEmpty()) {
            WebConfig config = FrameworkEngine.getWebConfig();
            Iterator it = config.getModules().entrySet().iterator();
            List list = new ArrayList();
            while (it.hasNext()) {
                Map.Entry en = (Map.Entry) it.next();
                Module m = (Module) en.getValue();
                list.add(m.getAction());
            }
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                String a = (String) list.get(i);
                String p = a.substring(0, a.lastIndexOf('.'));
                List as = (List) moduls.get(p);
                if (as == null) {
                    as = new ArrayList();
                    moduls.put(p, as);
                }
                as.add(a);
            }
        }
        List nodes = new ArrayList();
        List ps;
        if ("".equals(pack)) {
            Iterator it = moduls.keySet().iterator();
            List ls = new ArrayList();
            ps = new ArrayList();
            while (it.hasNext()) {
                String s = (String) it.next();
                String r = null;
                Package p = Package.getPackage(s);
                if ((p != null) && (p.isAnnotationPresent(Remark.class))) {
                    Remark remark = (Remark) p.getAnnotation(Remark.class);
                    r = remark.value();
                }

                ls.add(r == null ? s : r);
                ps.add(s);
            }

            for (int i = 0; i < ls.size(); i++) {
                String s = (String) ls.get(i);
                Map map = new HashMap();
                map.put("id", ps.get(i));
                map.put("text", s);
                map.put("qtip", ps.get(i));
                nodes.add(map);
            }
        } else {
            List<String> list = (List) moduls.get(pack);
            for (String s : list) {
                Map map = new HashMap();
                String text = s.substring(s.lastIndexOf('.') + 1);
                String qtip = null;
                try {
                    Class c = Class.forName(s, false, getClass().getClassLoader());
                    if (c.isAnnotationPresent(Remark.class))
                        qtip = ((Remark) c.getAnnotation(Remark.class)).value();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("id", s);
                map.put("text", qtip == null ? text : qtip);
                map.put("qtip", text);

                map.put("leaf", Boolean.valueOf(true));
                nodes.add(map);
            }
        }
        if (nodes.size() == 0) {
            Map map = new HashMap();
            map.put("text", "无分类");
            map.put("id", Integer.valueOf(0));
            map.put("leaf", Boolean.valueOf(true));
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    public Page doImport(WebForm form) throws Exception {
        FileItem item = (FileItem) form.getFileElement().get("file");
        String[] fields = {"resStr", "type", "desciption", "status"};
        ExcelImport im = new ExcelImport(item.getInputStream(), new ExcelImport.ImportService() {
            public void doImport(Map obj) {
                Resource a = new Resource();
                FrameworkEngine.form2Obj(obj, a, false, true);
                if (!ResourceAction.this.hasErrors())
                    ResourceAction.this.service.addResource(a);
            }
        }, fields);
        try {
            im.run();
        } catch (Exception e) {
            addError("msg", "数据进入出错，请检查所选择的文件格式是否正确！");
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doExport(WebForm form) throws Exception {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setPageSize(Integer.valueOf(-1));
        String[] lables = {"资源描述", "资源类型", "简介", "状态"};
        String[] fields = {"resStr", "type", "desciption", "status"};
        IPageList pageList = this.service.getResourceBy(qo);
        ExcelReport report = new ExcelReport("系统资源信息", lables, fields, pageList.getResult());
        report.export();
        return Page.nullPage;
    }

    public void setService(IResourceService service) {
        this.service = service;
    }

    public void setPermissionService(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }
}
