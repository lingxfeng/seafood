package com.eastinno.otransos.security.mvc.ajax;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ActionContext;

public class SimplePermisstionFilter implements Filter {
    private List<String> unFilter;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        User user = UserContext.getUser();
        HttpServletRequest req = ActionContext.getContext().getRequest();
        HttpServletResponse res = ActionContext.getContext().getResponse();
        if ((user == null) && (req != null)) {
            if ((req.getServletPath() != null) && (!this.unFilter.contains(req.getServletPath())))
                res.setHeader("LoginRequired", "true");
            else
                filterChain.doFilter(request, response);
        } else
            filterChain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String str = filterConfig.getInitParameter("unFilter");
        this.unFilter = Arrays.asList(str.split(","));
    }
}
