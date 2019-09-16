package com.eastinno.otransos.core.mvc.ajax;

import java.util.Properties;

import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.service.IConfigService;
import com.eastinno.otransos.core.service.IEmailService;
import com.eastinno.otransos.core.service.impl.EmailServiceImpl;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

public abstract class ConfigAction extends AbstractPageCmdAction {
    protected IConfigService configService;
    private IEmailService emailService;

    public void setConfigService(IConfigService configService) {
        this.configService = configService;
    }

    public void setEmailService(IEmailService emailService) {
        this.emailService = emailService;
    }

    public void doDatabase(WebForm form) {
        Properties p = this.configService.getDatabaseConfig();
        form.addPo(p);
    }

    public Page doTestConnection(WebForm form) {
        Properties p = this.configService.getDatabaseConfig();
        form.toPo(p);
        try {
            this.configService.checkConnection(p);
            form.addResult("msg", "true");
        } catch (LogicException e) {
            form.addResult("msg", e.toString());
        }
        return new Page("blank", "classpath:com/eastinno/otransos/core/views/blank.html");
    }

    public void doSaveDatabase(WebForm form) {
        Properties p = this.configService.getDatabaseConfig();
        form.toPo(p);
        this.configService.updateDatabaseConfig(p);
        forward("editDatabaseConfig");
    }

    public void doApplication(WebForm form) {
        Properties p = this.configService.getApplicationConfig();
        form.addPo(p);
    }

    public void doSaveApplication(WebForm form) {
        Properties p = this.configService.getApplicationConfig();
        form.toPo(p);
        this.configService.updateApplicationConfig(p);
        forward("email");
    }

    public void doEmail(WebForm form) {
        doApplication(form);
    }

    public void doTestEmail(WebForm form) {
        String reciveMail = CommUtil.null2String(form.get("email.reciveMail"));
        Properties p = this.configService.getApplicationConfig();
        form.toPo(p);
        EmailServiceImpl service = (this.emailService != null) && ((this.emailService instanceof EmailServiceImpl)) ? (EmailServiceImpl) this.emailService
                : new EmailServiceImpl();
        service.setSmtpHost(p.getProperty("email.smtpHost"));
        service.setUserName(p.getProperty("email.smtpUserName"));
        service.setPassword(p.getProperty("email.smtpPassword"));
        service.setTrueName(p.getProperty("email.trueName"));
        service.sendEmail(reciveMail, "测试邮件", "服务器配置测试邮件!", false);
        form.addResult("msg", "测试邮件已发送到" + reciveMail + "，请检查是否收到邮件!");
        forward("email");
    }

    public void doSaveEmail(WebForm form) {
        doSaveApplication(form);
        forward("email");
    }
}
