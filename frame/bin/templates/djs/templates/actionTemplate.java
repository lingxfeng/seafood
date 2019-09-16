package $!{packageName}.mvc;

import cn.disco.container.annonation.Action;
import cn.disco.container.annonation.Inject;
import cn.disco.core.support.query.QueryObject;
import cn.disco.web.core.AbstractPageCmdAction;
import cn.disco.core.util.CommUtil;
import cn.disco.web.Module;
import cn.disco.web.Page;
import cn.disco.web.WebForm;
import cn.disco.web.tools.IPageList;
import $!{packageName}.domain.$!{domainName};
import $!{packageName}.service.I$!{domainName}Service;
##set ($domain = $!domainName.toLowerCase())

/**
 * $!{domainName}Action
 * @author 
 */
@Action
public class $!{domainName}Action extends AbstractPageCmdAction {
    @Inject
    private I$!{domainName}Service service;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public void doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.get$!{domainName}By(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        $!{domainName} entry = ($!{domainName})form.toPo($!{domainName}.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.add$!{domainName}(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }
    /**
     * 导入添加页面
     */
    public Page doAdd() {
        return go("edit");
    }
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public void doEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            $!{domainName} entry = this.service.get$!{domainName}(id);
            form.addPo(entry);
        }
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        $!{domainName} entry = this.service.get$!{domainName}(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.update$!{domainName}(entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.del$!{domainName}(id);
        return go("list");
    }
    
    public void setService(I$!{domainName}Service service) {
        this.service = service;
    }
}