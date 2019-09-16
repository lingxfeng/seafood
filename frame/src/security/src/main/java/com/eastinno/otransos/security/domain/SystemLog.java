package com.eastinno.otransos.security.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 系统日志记录
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年05月11日 上午11:32:32
 * @Intro
 */
@Entity(name = "Disco_SystemLog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(inject = "")
public class SystemLog implements IJsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Date vdate = new Date();
    private String ip;
    private String action;
    private String cmd;
    private Integer types;
    private String actionName;
    private String cmdName;

    @Lob
    private String params;

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getCmdName() {
        return this.cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getVdate() {
        return this.vdate;
    }

    public void setVdate(Date vdate) {
        this.vdate = vdate;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getTypes() {
        return this.types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"user"});
        if (this.user != null)
            map.put("user", CommUtil.obj2map(this.user, new String[] {"id", "name"}));
        return map;
    }
}
