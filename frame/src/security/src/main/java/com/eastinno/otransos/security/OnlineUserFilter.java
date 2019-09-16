package com.eastinno.otransos.security;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ActionContext;

public class OnlineUserFilter implements Filter {
    private OnlineUserManage oum;
    private Thread onlineUserThread;
    private static final Logger logger = Logger.getLogger(OnlineUserFilter.class);

    public void destroy() {
        this.oum = null;
        this.onlineUserThread.interrupt();
        this.onlineUserThread = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tempName = request.getRemoteAddr();
        if (!InetAddress.getLocalHost().getHostAddress().equals(tempName)) {
            if (ActionContext.getContext().getSession() != null) {
                tempName = (String) ActionContext.getContext().getSession().getAttribute("Disco_Temp_NAME");
                if (tempName == null) {
                    tempName = request.getRemoteAddr() + ":" + request.getRemotePort() + CommUtil.getRandomVal(10);
                    ActionContext.getContext().getSession().setAttribute("Disco_Temp_NAME", tempName);
                }
            }
            if (UserContext.getUser() != null)
                this.oum.refreshUser(UserContext.getUser(), tempName);
            else
                this.oum.refreshUser(tempName);
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("在线用户检测过滤系统初初始化!");
        this.oum = OnlineUserManage.getInstance();
        String interval = filterConfig.getInitParameter("interval");
        if (interval != null)
            this.oum.setInterval(Integer.parseInt(interval) * 1000);
        String checkTimes = filterConfig.getInitParameter("checkTimes");
        if (checkTimes != null)
            this.oum.setCheckTimes(Integer.parseInt(checkTimes));
        this.onlineUserThread = new Thread(this.oum);
        this.onlineUserThread.start();
    }
}
