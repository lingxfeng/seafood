package com.eastinno.otransos.security.service.impl;

import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.validate.ValidateType;


/**
 * 租户数据权限过滤
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2014年05月23日 下午1:49:31
 * @Intro
 */
public class TenantObjectUtil {

    public static void setObject(ITenantObject to) {
        Tenant t = null;
        // 1.优先根据平台登陆用户获取对应的租户
        User user = UserContext.getUser();
        if (user != null && user instanceof ITenantObject && user.getTenant() != null) {
            t = user.getTenant();
        }
        else {
            t = TenantContext.getTenant();
        }
        if (to != null) {
            to.setTenant(t);
        }
    }


    /**
     * 添加查询限制
     * 
     * @param qo
     */
    public static void addQuery(IQueryObject qo) {
        addQuery(qo, "obj.tenant");
    }


    /**
     * 添加查询限制
     * 
     * @param qo
     * @param objName
     */
    public static void addQuery(IQueryObject qo, String objName) {
        StringBuilder sb = new StringBuilder();
        Tenant t = (Tenant) TenantContext.getTenant();
        // 1.优先根据登陆用户获取对应的租户
        User user = UserContext.getUser();
        if (user != null && user instanceof ITenantObject && user.getTenant() != null) {
            qo.addOcc();
            ITenantObject co = (ITenantObject) UserContext.getUser();
            sb.append("(").append(objName).append(" is EMPTY or ").append(objName).append(
                "=?" + qo.getOcc() + ")");
            qo.addQuery(sb.toString(), new Object[] { co.getTenant() });

        }
        else if (t != null) {// 2.然后根据session中的租户对象来获取
            qo.addQuery("obj.tenant.id", t.getId(), "=");
        }
        else {
            sb.append("(").append(objName).append(" is EMPTY)");
            qo.addQuery(sb.toString(), null);// 只能查询公开的信息
        }
    }


    public static void addQuery(IQueryObject qo, List<Long> includes) {
        addQuery(qo, "obj.tenant", includes);
    }


    public static void addQuery(IQueryObject qo, String objName, List<Long> includes) {
        StringBuilder sb = new StringBuilder();
        Tenant t = null;
        if ((UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject)
                || TenantContext.getTenant() != null) {
            ITenantObject to = (ITenantObject) UserContext.getUser();
            if (to != null) {
                t = to.getTenant();
            }
            else {
                t = TenantContext.getTenant();
            }
            if (includes != null && includes.size() > 0) {
                String instr = "";
                for (Long id : includes) {
                    instr += id.toString() + ",";
                }
                instr = instr.substring(0, instr.length() - 1);
                sb.append("(").append(objName).append("=? or ").append(objName).append(" in (").append(instr)
                    .append("))");
                qo.addQuery(sb.toString(), new Object[] { t.getId() });
            }
            else {
                sb.append("(").append(objName).append(" is EMPTY or ").append(objName).append("=?)");
                qo.addQuery(sb.toString(), new Object[] { t });
            }
        }
        else {
            sb.append("(").append(objName).append(" is EMPTY)");
            qo.addQuery(sb.toString(), null);// 只能查询公开的信息
        }
    }


    /**
     * 检查数据访问权限
     * 
     * @param to
     */
    public static void checkObject(ITenantObject to) {
        if (!checkObjectService(to)) {
            FrameworkEngine.findValidatorManager().addCustomError("msg", "没有数据操作权限！", ValidateType.Action);
        }
    }


    /**
     * 检测数据模型是否为租户下的数据模型
     * 
     * @param to
     * @return
     */
    public static boolean checkObjectService(ITenantObject to) {
        if (to != null && to.getTenant() != null) {
            if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
                ITenantObject u = (ITenantObject) UserContext.getUser();
                if (u.getTenant() != null && to.getTenant().getId().equals(u.getTenant().getId())) {
                    return true;
                }
            }
            else if (TenantContext.getTenant() != null) {
                return true;
            }
            return false;
        }
        else {
            return true;
        }
    }
}
