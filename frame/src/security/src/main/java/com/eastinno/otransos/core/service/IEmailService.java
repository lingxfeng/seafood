package com.eastinno.otransos.core.service;

import com.eastinno.otransos.core.domain.EmailNotify;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 邮件发送服务
 * 
 * @version 2.0
 * @author lengyu
 * @date 2010年12月13日-下午02:11:09
 */
public interface IEmailService {
    /**
     * 发送自定义邮件，包括带附件的邮件等
     * 
     * @param notify 邮件通知信息，包括附件信息等
     * @param save 是否保存通知
     */
    void sendEmail(EmailNotify notify, boolean save);

    /**
     * 发送不带附件的简单邮件
     * 
     * @param email 接收人的email
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param save 是否自动保存到邮件通知列表中
     */
    void sendEmail(String email, String subject, String content, boolean save);

    void sendEmail(String email, String subject, String content, String replyTo, String replyName, boolean save);

    /**
     * 保存邮件通知
     * 
     * @param notify
     */
    void saveNotify(EmailNotify notify);

    /**
     * 根据一个ID得到保存的邮件通知
     * 
     * @param id
     * @return
     */
    EmailNotify getNotify(Long id);

    /**
     * 删除邮件通知信息
     * 
     * @param id
     */
    void delNotify(Long id);

    /**
     * 修改邮件通知
     * 
     * @param id 通知id
     * @param notify 要修改的通知信息
     */
    void updateNotify(Long id, EmailNotify notify);

    /**
     * 根据一个查询对象得到邮件通知列表
     * 
     * @param properties
     * @return
     */
    IPageList getNotifyBy(IQueryObject properties);
}
