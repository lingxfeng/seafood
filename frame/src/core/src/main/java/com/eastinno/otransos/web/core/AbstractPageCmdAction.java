package com.eastinno.otransos.web.core;

import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

/**
 * 用来支持更加灵活的参数调用方式。
 * 
 * @author lengyu
 */

abstract public class AbstractPageCmdAction extends AbstractCmdAction {
    public Page execute(WebForm form, Module module) throws Exception {
        Page forward = super.execute(form, module);
        if (forward == null && !"".equals(this.command)) {
            forward = this.forwardPage;
            if (forward == null)
                forward = module.findPage(this.command);
            if (forward == null) {
                String templateUrl = module.getViews() + module.getPath() + "/" + this.command;
                if (this.getTemplatePerfix() != null)
                    templateUrl = this.getTemplatePerfix() + templateUrl;
                String ext = getTemplateExt();
                if (ext.charAt(0) == '.')
                    ext = ext.substring(1);
                forward = new Page(this.getClass() + this.command, templateUrl + "." + ext);
            }
        }
        reset();
        return forward;
    }

    protected String getTemplateExt() {
        return Globals.DEFAULT_TEMPLATE_EXT;
    }

    /**
     * 设置模板路径前缀
     * 
     * @return
     */
    protected String getTemplatePerfix() {
        return "/";
    }

    public Page doInit(WebForm form, Module module) {
        return forward("index");
    }
}
