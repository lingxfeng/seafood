package com.eastinno.otransos.security.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.service.ExcelReport;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.core.util.ClientIPUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.annotation.Remark;
import com.eastinno.otransos.security.dao.ISystemLogDAO;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ISystemLogService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.interceptor.IActionCommandInterceptor;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class SystemLogServiceImpl implements IActionCommandInterceptor, ISystemLogService {
    // TODO 后期改为配置在系统的全配配置之下(日志记录)
    private boolean before = false;// 是否前置拦截日志
    private boolean after = false;// 是否后置拦截日志
    private boolean login = false;// 是否记录登陆日志
    private List<String> excepts = new ArrayList<String>();// 排除拦截的Action
    @Resource
    private ISystemLogDAO dao;

    public void setDao(ISystemLogDAO dao) {
        this.dao = dao;
    }

    public void setBefore(boolean before) {
        this.before = before;
    }

    public void setAfter(boolean after) {
        this.after = after;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setExcepts(List<String> excepts) {
        this.excepts = excepts;
    }

    public Page doBefore(Method method) {
        if (this.before) {
            SystemLog log = createLog(method);
            if (log != null) {
                log.setTypes(Integer.valueOf(0));
                addSystemLog(log);
            }
        }
        return null;
    }

    public void doAfter(Method method) {
        if (this.after) {
            SystemLog log = createLog(method);
            if (log != null) {
                log.setTypes(Integer.valueOf(1));
                addSystemLog(log);
            }
        }
    }

    private SystemLog createLog(Method method) {
        if ((this.login) && (UserContext.getUser() == null))
            return null;
        String action = method.getDeclaringClass().getName();
        Remark actionAnn = (Remark) method.getDeclaringClass().getAnnotation(Remark.class);
        Remark methodAnn = (Remark) method.getAnnotation(Remark.class);
        String actionName = actionAnn == null ? method.getDeclaringClass().getName() : actionAnn.value();
        String methodName = methodAnn == null ? method.getName() : methodAnn.value();
        if ((!this.excepts.isEmpty()) && (this.excepts.contains(action)))
            return null;
        SystemLog log = new SystemLog();
        log.setIp(ActionContext.getContext().getRequest().getRemoteAddr());
        User user = UserContext.getUser();
        if ((user != null) && (user.getId().longValue() > 0L)) {
            log.setUser(UserContext.getUser());
        }
        log.setAction(action);
        log.setCmd(method.getName());
        log.setCmdName(methodName);
        log.setActionName(actionName);
        log.setTypes(Integer.valueOf(0));
        WebForm form = ActionContext.getContext().getWebInvocationParam().getForm();
        String ps = "id=" + form.get("id") + ";mulitiId=" + form.get("mulitiId") + ";cmd=" + form.get("cmd");
        log.setParams(ps);
        return log;
    }

    public void addSystemLog(SystemLog log) {
        if (log.getVdate() == null)
            log.setVdate(new Date());
        if (log.getIp() == null)
            log.setIp(ClientIPUtil.getIP(ActionContext.getContext().getRequest()));
        this.dao.save(log);
    }

    public void delSystemLog(Long id) {
        this.dao.remove(id);
    }

    public void batchDelSystemLog(IQueryObject properties) {
        String sql = "delete form SystemLog where " + properties.getQuery();
        this.dao.batchUpdate(sql, properties.getParameters().toArray());
    }

    public void exportSystemLog(IQueryObject properties) throws Exception {
        String[] lables = {"id", "操作人", "日期", "ip", "模块", "命令", "类别", "参数"};
        String[] fields = {"id", "user.name", "vdate", "ip", "action", "cmd", "types", "params"};
        IPageList pageList = getSystemLogBy(properties);
        ExcelReport report = new ExcelReport("系统操作日志", lables, fields, pageList.getResult());
        report.export();
    }

    public IPageList getSystemLogBy(IQueryObject properties) {
        if (properties == null) {
            properties = new QueryObject();
        }
        return QueryUtil.query(properties, SystemLog.class, this.dao);
    }

    public void updateSystemLog(SystemLog log) {
        if (log.getId() != null)
            this.dao.update(log);
    }
}
