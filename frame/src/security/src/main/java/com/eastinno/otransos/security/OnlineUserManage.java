package com.eastinno.otransos.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ajax.IJsonObject;

public class OnlineUserManage implements Runnable {
    private Map<String, OnlineUser> users = new HashMap<String, OnlineUser>();
    private static OnlineUserManage singleton = new OnlineUserManage();

    private int interval = 60000;

    private int checkTimes = 5;

    public static OnlineUserManage getInstance() {
        return singleton;
    }

    public void setCheckTimes(int checkTimes) {
        if (checkTimes > 3)
            this.checkTimes = checkTimes;
    }

    public void setInterval(int interval) {
        if (interval > 1000)
            this.interval = interval;
    }

    public void run() {
        while (true) {
            checkOnline();
            try {
                Thread.sleep(this.interval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkOnline() {
        List offUsers = new ArrayList();
        for (OnlineUser ou : this.users.values()) {
            ou.status += 1;
            if (ou.status > this.checkTimes) {
                offUsers.add(ou);
            }

        }

        synchronized (this.users) {
            this.users.values().removeAll(offUsers);
        }
    }

    public List<OnlineUser> getUsers() {
        return new ArrayList(this.users.values());
    }

    public void joinUser(String ip) {
        if (!this.users.containsKey(ip)) {
            OnlineUser ou = new OnlineUser(ip);
            this.users.put(ip, ou);
            if (this.users.get(ou.getIp()) != null)
                synchronized (this.users) {
                    this.users.remove(ou.getIp());
                }
        }
    }

    public void joinUser(User user, String ip) {
        if (!this.users.containsKey(user.getName())) {
            OnlineUser ou = new OnlineUser(user);
            this.users.put(user.getName(), ou);
            ou.setIp(ip.split(":")[0]);
            synchronized (this.users) {
                this.users.remove(ip);
            }
        }
    }

    public void joinUser(User user) {
        joinUser(user, "");
    }

    private void refreshUser(OnlineUser ou) {
        ou.status = 0;
    }

    public void refreshUser(String ip) {
        OnlineUser u = (OnlineUser) this.users.get(ip);
        if (u != null)
            refreshUser(u);
        else
            joinUser(ip);
    }

    public void refreshUser(User user) {
        refreshUser(user, "");
    }

    public void refreshUser(User user, String ip) {
        OnlineUser u = (OnlineUser) this.users.get(user.getName());
        if (u != null)
            refreshUser(u);
        else
            joinUser(user, ip);
    }

    public int getGuestCount() {
        List<OnlineUser> us = getUsers();
        int count = 0;
        for (OnlineUser ou : us) {
            if (ou.getGuest())
                count++;
        }
        return count;
    }

    public int getUsetCount() {
        return this.users.size() - getGuestCount();
    }

    public OnlineUser getOnlineUser(String name) {
        return (OnlineUser) this.users.get(name);
    }

    public boolean removeUser(String name) {
        if ((name != null) && (!"".equals(name))) {
            this.users.remove(name);
        }
        return true;
    }

    public boolean isOnline(String name) {
        if ((name != null) && (!"".equals(name))) {
            try {
                for (OnlineUser ou : getUsers()) {
                    if (name.equals(ou.getName()))
                        return true;
                }
            } catch (NoSuchElementException e) {
                this.users.clear();
            }
        }
        return false;
    }

    public boolean isOnline(Long id) {
        for (OnlineUser ou : getUsers()) {
            if (id.equals(ou.getId()))
                return true;
        }
        return false;
    }

    public class OnlineUser implements IJsonObject {
        private String ip;
        private Date vdate = new Date();
        private User user;
        private String tempName;
        private String curPosition;
        private int status;

        public Object toJSonObject() {
            Map map = CommUtil.obj2mapExcept(this, new String[] {"user"});
            if (this.user != null)
                map.put("user", CommUtil.obj2map(this.user, new String[] {"id", "trueName", "name"}));
            return map;
        }

        OnlineUser(String ip) {
            this.tempName = ip;
            this.ip = ip.split(":")[0];
            this.status = 0;
        }

        OnlineUser(User user) {
            this.user = user;
            this.status = 0;
        }

        public String getName() {
            if (this.user != null) {
                return this.user.getName();
            }
            return this.tempName;
        }

        public boolean getGuest() {
            return this.user == null;
        }

        public String getNickName() {
            if (this.user == null) {
                return "guest";
            }

            return this.user.getName();
        }

        public String getCurPosition() {
            return this.curPosition;
        }

        public void setCurPosition(String curPosition) {
            this.curPosition = curPosition;
        }

        public String getIp() {
            return this.ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Date getVdate() {
            return this.vdate;
        }

        public void setVdate(Date vdate) {
            this.vdate = vdate;
        }

        public String getId() {
            if (this.user != null) {
                return user.getId() + "";
            }
            return this.tempName;
        }

        public int getStatus() {
            return this.status;
        }

        public User getUser() {
            return this.user;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String toString() {
            return "ip:[" + this.ip + "],vdate:[" + this.vdate + "],user:[" + this.user + "],status:[" + this.status + "]";
        }

        public String getTempName() {
            return this.tempName;
        }

        public void setTempName(String tempName) {
            this.tempName = tempName;
        }
    }
}
