package com.eastinno.otransos.install.mvc;

import com.eastinno.otransos.core.service.IConfigService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractWizardAction;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class SetupAction extends AbstractWizardAction {
    private IConfigService configService;
    private boolean licenceValidity = false;
    private String[] initializtionResources;
    private boolean needRestart = true;

    public void setInitializtionResources(String[] initializtionResources) {
        this.initializtionResources = initializtionResources;
    }

    public void setConfigService(IConfigService configService) {
        this.configService = configService;
    }

    public void setNeedRestart(boolean needRestart) {
        this.needRestart = needRestart;
    }

    public void doIndex(WebForm form) {
    }

    public Page doLicence(WebForm form) {
        return findPage("licence", "classpath:com/eastinno/otransos/core/views/setupLicenceEdit.html");
    }

    public Page doCheckLicence(WebForm form) {
        return null;
    }

    public Page doDatabaseConfig(WebForm form) {
        return findPage("databaseConfig", "classpath:com/eastinno/otransos/core/views/setupDatabaseConfig.html");
    }

    public Page doSaveDatabaseConfig(WebForm form) {
        return null;
    }

    public Page doInitApplicationData(WebForm form) {
        return findPage("initApplicationData", "classpath:com/eastinno/otransos/core/views/setupInitApplicationData.html");
    }

    public Page doSaveInitApplicationData(WebForm form) {
        return null;
    }

    public Page doInitAdministrator(WebForm form) {
        return findPage("initAdministrator", "classpath:com/eastinno/otransos/core/views/setupInitAdministrator.html");
    }

    public Page doFinish(WebForm form) {
        return findPage("finish", "classpath:com/eastinno/otransos/core/views/setupFinish.html");
    }

    public void doRestart() {
        try {
            FrameworkEngine.getDiscoFilter().init(FrameworkEngine.getDiscoFilter().filterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
