package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.dao.IEmailNotifyDAO;
import com.eastinno.otransos.core.domain.EmailNotify;
import com.eastinno.otransos.core.service.IEmailService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.web.tools.IPageList;

public class EmailServiceImpl implements IEmailService {
    private static final Log logger = LogFactory.getLog(EmailServiceImpl.class);
    private int times;
    private String domain = "gmail.com";
    private String smtpHost = "smtp.gmail.com";
    private String password = "maoweiwer";
    private String userName = "dobeext";
    private String trueName = "Disco For JAVA EE快速开发平台";
    private String reciveNotifyMail = "";
    @Resource
    private IEmailNotifyDAO notifyDao;

    public void sendEmail(final EmailNotify notify, final boolean save) {
        final EmailSender emailSender = createEmailSender(notify.getToUser(), notify.getSubject(), notify.getContent());
        if (notify.getFiles() != null) {
            String[] fs = StringUtils.tokenizeToStringArray(notify.getFiles(), ",");
            emailSender.setFiles(fs);
        }
        emailSender.setSecretTo(notify.getSecretTo());
        emailSender.setCopyTo(notify.getCopyTo());
        new Thread() {
            public void run() {
                boolean ret = false;
                if (emailSender.sendMail())
                    ret = true;
                else {
                    ret = false;
                }
                if (save) {
                    notify.setStatus(ret ? 1 : 0);
                    notify.setTimes(notify.getTimes() + 1);
                    if (notify.getId() == null)
                        EmailServiceImpl.this.saveNotify(notify);
                    else
                        EmailServiceImpl.this.updateNotify(notify.getId(), notify);
                }
            }
        }.start();
    }

    public void sendEmail(final String email, final String subject, final String content, String replyTo, String replyName,
            final boolean save) {
        final EmailSender emailSender = createEmailSender(email, subject, content);
        emailSender.setReplyTo(replyTo);
        emailSender.setReplyName(replyName);
        new Thread() {
            public void run() {
                boolean ret = false;
                if (emailSender.sendMail()) {
                    ret = true;
                }
                if (save) {
                    EmailNotify notify = new EmailNotify();
                    notify.setContent(content);
                    notify.setSubject(subject);
                    notify.setToUser(email);
                    notify.setStatus(ret ? 1 : 0);
                    notify.setTimes(1);
                    notify.setInputTime(new Date());
                    notify.setTypes("Normal");
                    EmailServiceImpl.this.saveNotify(notify);
                }
            }
        }.start();
    }

    public void sendEmail(String email, String subject, String content, boolean save) {
        sendEmail(email, subject, content, null, null, save);
    }

    private EmailSender createEmailSender(String email, String subject, String content) {
        EmailSender emailSender = new EmailSender(this.domain, this.smtpHost, this.userName, this.password);
        emailSender.setTrueName(this.trueName);
        emailSender.setToUser((email == null) || ("".equals(email)) ? this.reciveNotifyMail : email);
        emailSender.setSubject(subject);
        emailSender.setContent(content);
        return emailSender;
    }

    public static void main(String[] args) {
        EmailServiceImpl email = new EmailServiceImpl();
        email.sendEmail("10334096@qq.com", "123", "456", false);
    }

    public EmailNotify getNotify(Long id) {
        return (EmailNotify) this.notifyDao.get(id);
    }

    public IPageList getNotifyBy(IQueryObject properties) {
        return QueryUtil.query(properties, EmailNotify.class, this.notifyDao);
    }

    public void saveNotify(EmailNotify notify) {
        logger.info("保存邮件信息!");
        this.notifyDao.save(notify);
    }

    public void delNotify(Long id) {
        this.notifyDao.remove(id);
    }

    public void updateNotify(Long id, EmailNotify notify) {
        notify.setId(id);
        this.notifyDao.update(notify);
    }

    public void batchDelNotify(List<Serializable> ids) {
        for (Serializable id : ids)
            delNotify((Long) id);
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public void setNotifyDao(IEmailNotifyDAO notifyDao) {
        this.notifyDao = notifyDao;
    }

    public int getTimes() {
        return this.times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setReciveNotifyMail(String reciveNotifyMail) {
        this.reciveNotifyMail = reciveNotifyMail;
    }
}
