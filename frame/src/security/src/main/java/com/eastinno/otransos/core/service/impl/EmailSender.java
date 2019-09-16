package com.eastinno.otransos.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.util.StringUtils;

public class EmailSender {
    private static final Log logger = LogFactory.getLog(EmailSender.class);
    private String domain;
    private String smtpHost;
    private String userName;
    private String trueName;
    private String password;
    private String toUser;
    private String copyTo;
    private String secretTo;
    private String replyTo;
    private String replyName;
    private String subject;
    private String content;
    private String[] files;
    private Map fileItems;

    public EmailSender() {
    }

    public EmailSender(String domain, String smtpHost, String userName, String password) {
        this.domain = domain;
        this.smtpHost = smtpHost;
        this.userName = userName;
        this.password = password;
    }

    public boolean sendMail() {
        boolean ret = false;
        try {
            Email email = createMail();
            email.setCharset("UTF-8");
            email.setSubject(this.subject);
            email.setFrom(this.userName + "@" + this.domain, this.trueName != null ? this.trueName : this.userName);
            if (StringUtils.hasLength(this.replyTo))
                email.addReplyTo(this.replyTo, this.replyName);
            List list = formatAddress(this.toUser);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    email.addTo((String) map.get("email"), (String) map.get("name"));
                }
            }
            list = formatAddress(this.copyTo);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    email.addCc((String) map.get("email"), (String) map.get("name"));
                }
            }
            list = formatAddress(this.secretTo);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    email.addBcc((String) map.get("email"), (String) map.get("name"));
                }
            }
            email.setHostName(this.smtpHost);
            System.getProperties().put("mail.smtp.host", this.smtpHost);

            email.setAuthentication(this.userName, this.password);
            try {
                email.send();
            } catch (EmailException e) {
                logger.error(e.getMessage());
                email.setAuthentication(this.userName + "@" + this.domain, this.password);
                email.send();

            }
            ret = true;
        } catch (Exception e) {
            System.out.println("发送邮件出错!" + e);
            e.printStackTrace();
        }
        return ret;
    }

    public Email createMail() throws Exception {
        Email email = new HtmlEmail();
        boolean isHtml = false;

        if ((this.content.indexOf("<html>") == 0) && (this.content.endsWith("</html>"))) {
            this.content = this.content.substring("<html>".length(), this.content.lastIndexOf("</html>"));
            isHtml = true;
        }

        ((HtmlEmail) email).setHtmlMsg(this.content);

        if ((this.fileItems != null) && (!this.fileItems.isEmpty())) {
            Iterator it = this.fileItems.values().iterator();
            while (it.hasNext()) {
                FileItem file = (FileItem) it.next();
                if ((file.getName() != null) && (!file.getName().equals(""))) {
                    String fileName = file.getName();
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    try {
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(file.getInputStream(), null);
                        ((MultiPartEmail) email).attach(dataSource, MimeUtility.encodeText(fileName), fileName);
                    } catch (Exception e) {
                        System.out.println("附件处理错误：" + e);
                        e.printStackTrace();
                    }
                }
            }
        }
        if ((this.files != null) && (this.files.length > 0)) {
            for (int i = 0; i < this.files.length; i++) {
                try {
                    String fileName = new File(this.files[i]).getName();
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    ByteArrayDataSource dataSource = new ByteArrayDataSource(new FileInputStream(new File(this.files[i])), null);
                    ((MultiPartEmail) email).attach(dataSource, MimeUtility.encodeText(fileName), fileName);
                } catch (Exception e) {
                    System.out.println("本地附件处理错误：" + e);
                    e.printStackTrace();
                }
            }
        }

        return email;
    }

    public List formatAddress(String s) {
        if ((s == null) || (s.equals(""))) {
            return null;
        }
        String[] t = s.split(",");
        List list = null;
        if (t != null) {
            list = new ArrayList();
            for (int i = 0; i < t.length; i++) {
                String tmp = t[i];
                int tagN = tmp.indexOf("<");
                String name;
                String email;
                if (tagN > 0) {
                    email = tmp.substring(tagN + 1, tmp.indexOf(">", tagN));
                    name = tmp.substring(0, tagN);
                } else {
                    if (tmp.indexOf("@") > 0) {
                        name = tmp.substring(0, tmp.indexOf("@"));
                        email = tmp;
                    } else {
                        name = tmp;
                        email = tmp;
                    }
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", name);
                map.put("email", email);
                list.add(map);
            }
        }
        return list;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCopyTo() {
        return this.copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public Map getFileItems() {
        return this.fileItems;
    }

    public void setFileItems(Map fileItems) {
        this.fileItems = fileItems;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretTo() {
        return this.secretTo;
    }

    public void setSecretTo(String secretTo) {
        this.secretTo = secretTo;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String senderName) {
        this.userName = senderName;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String serverDomain) {
        this.domain = serverDomain;
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTrueName() {
        return this.trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String[] getFiles() {
        return this.files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getReplyTo() {
        return this.replyTo;
    }

    public String getReplyName() {
        return this.replyName;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }
}
