package com.eastinno.otransos.security.service.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.security.dao.IDomainOperationLogDAO;
import com.eastinno.otransos.security.domain.DomainOperationLog;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.IDomainOperationService;
import com.eastinno.otransos.web.ajax.JSonConvertUtil;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class DomainOperationLogServiceImpl implements IDomainOperationService {
    @Resource
    private IDomainOperationLogDAO dao;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
    private List<String> excepts = new ArrayList<String>();
    private List<String> includes = new ArrayList<String>();

    private <T> T getObject(Class<T> clz, List list) {
        if ((list != null) && (!list.isEmpty())) {
            try {
                DomainOperationLog log = (DomainOperationLog) list.get(0);
                T obj = clz.newInstance();
                decodeObject(obj, log.getContent());
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T getObject(Class<T> clz, Serializable id, Integer ver) {
        if (ver == null)
            return getObject(clz, id);
        String sql = "obj.entityType=? and obj.entityId=? and ver=?";
        List list = this.dao.find(sql, new Object[] {clz.getName(), id.toString(), ver}, 0, 1);
        return getObject(clz, list);
    }

    public <T> T getObject(Class<T> clz, Serializable id) {
        String sql = "obj.entityType=? and obj.entityId=? order by ver desc";
        List list = this.dao.find(sql, new Object[] {clz.getName(), id.toString()}, 0, 1);
        return getObject(clz, list);
    }

    private void record(Object obj, Integer action, User user, boolean refresh) {
        String clzName = obj.getClass().getName();
        if ((DomainOperationLog.class.isAssignableFrom(obj.getClass())) || (SystemLog.class.isAssignableFrom(obj.getClass())))
            return;
        if (refresh) {
            String sql = "select obj from " + obj.getClass().getName() + " obj where obj.id=?";
            List list = this.dao.query(sql, new Object[] {getId(obj)}, 0, 1);
            if ((list != null) && (!list.isEmpty()))
                obj = list.get(0);
        }
        if ((this.excepts != null) && (this.excepts.contains(clzName)))
            return;
        if ((this.includes != null) && (!this.includes.isEmpty()) && (!this.includes.contains(clzName))) {
            return;
        }
        EncodeHanlder dh = new EncodeHanlder(user);
        dh.recordObject(obj, action);
    }

    public void record(Object obj, Integer action, User user) {
        record(obj, action, user, true);
    }

    public void record(Object obj, Integer action) {
        record(obj, action, null, true);
    }

    public void revert(Class clz, Serializable id, Integer ver) {
        Object obj = getObject(clz, id, ver);
        if (obj != null) {
            Object target = null;
            String sql = "select obj from " + clz.getName() + " obj where obj.id=?";
            List list = this.dao.query(sql, new Object[] {getId(obj)}, 0, 1);
            if ((list != null) && (!list.isEmpty()))
                target = list.get(0);
            if (target != null)
                BeanUtils.copyProperties(obj, target);
        }
    }

    private void decodeObject(Object obj, String content) {
        String[] pvs = content.split("@@@");
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        for (int i = 0; i < pvs.length; i++) {
            int l = pvs[i].indexOf("=");
            String pname = pvs[i].substring(0, l);
            String value = pvs[i].substring(l + 1);
            if (wrapper.isWritableProperty(pname)) {
                if (!"[NULL]".equals(value)) {
                    Class pclz = wrapper.getPropertyDescriptor(pname).getPropertyType();
                    Object v = null;

                    if (JSonConvertUtil.isSimpleType(pclz)) {
                        v = value;
                    } else if (Map.class.isAssignableFrom(pclz)) {
                        Map map = new HashMap();
                        v = map;
                    } else if (Collection.class.isAssignableFrom(pclz)) {
                        Collection cs;
                        if (pclz.isAssignableFrom(Set.class))
                            cs = new HashSet();
                        else {
                            cs = new ArrayList();
                        }
                        Class rawType = Object.class;
                        try {
                            Type type = obj.getClass().getDeclaredField(pname).getGenericType();
                            if ((type instanceof ParameterizedType)) {
                                ParameterizedType aType = (ParameterizedType) type;
                                Type[] fieldArgTypes = aType.getActualTypeArguments();
                                rawType = (Class) fieldArgTypes[0];
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        if (value.endsWith("{"))
                            value = value.substring(1);
                        if (value.endsWith("}"))
                            value = value.substring(0, value.length() - 1);
                        String[] ids = value.split(";");
                        if (ids != null)
                            for (int j = 0; j < ids.length; j++) {
                                String id = ids[j];
                                if (id.indexOf("-") > 0)
                                    id = id.substring(0, id.indexOf("-"));
                                if (id.charAt(0) == '{')
                                    id = id.substring(1);
                                if (id.length() >= 1) {
                                    Object tempV = getCurrentObject(rawType, id);
                                    if (tempV != null)
                                        cs.add(tempV);
                                }
                            }
                        v = cs;
                    } else if (value.length() > 0) {
                        String id = value.substring(0, value.indexOf("-"));
                        if (id.charAt(0) == '{')
                            id = id.substring(1);
                        v = getCurrentObject(pclz, id);
                    }
                    wrapper.setPropertyValue(pname, v);
                }
            }
        }
    }

    private Object getCurrentObject(Class clz, Serializable id) {
        if (this.dao == null)
            return null;
        List list = this.dao
                .query("select obj from " + clz.getName() + " obj where obj.id=?", new Object[] {new Long(id.toString())}, 0, 1);
        if ((list == null) || (list.isEmpty())) {
            System.out.println("从日志中读取" + id);
            return getObject(clz, id);
        }

        return list.get(0);
    }

    private Integer getLastVer(Class clz, Serializable id) {
        if (this.dao == null)
            return Integer.valueOf(0);
        String sql = "select obj.ver from DomainOperationLog obj where obj.entityType=? and obj.entityId=? order by ver desc";
        String clzName = clz.getName();
        if (clzName.indexOf("$") > 0)
            clzName = clzName.substring(0, clzName.indexOf("$"));
        List list = this.dao.query(sql, new Object[] {clzName, id.toString()}, 0, 1);
        if ((list == null) || (list.isEmpty())) {
            return Integer.valueOf(0);
        }
        return (Integer) list.get(0);
    }

    private Serializable getId(Object entityObject) {
        try {
            Method m = entityObject.getClass().getMethod("getId", null);
            if (m != null)
                return (Serializable) m.invoke(entityObject, null);
        } catch (Exception localException) {
        }
        return null;
    }

    public void record(DomainOperationLog log) {
        if (this.dao != null)
            this.dao.save(log);
    }

    public DomainOperationLog getDomainOperationLog(Long id) {
        return (DomainOperationLog) this.dao.get(id);
    }

    public void setDao(IDomainOperationLogDAO dao) {
        this.dao = dao;
    }

    public void setExcepts(List<String> excepts) {
        this.excepts = excepts;
    }

    public IPageList getDomainOperationLogBy(IQueryObject properties) {
        if (properties == null) {
            properties = new QueryObject();
        }
        return QueryUtil.query(properties, DomainOperationLog.class, this.dao);
    }

    private class EncodeHanlder {
        private Set caches = new HashSet();
        private User user;

        private EncodeHanlder(User user) {
            this.user = user;
        }

        private void recordObject(Object obj, Integer action) {
            String clzName = obj.getClass().getName();
            if (clzName.indexOf("$") > 0)
                clzName = clzName.substring(0, clzName.indexOf("$"));
            DomainOperationLog log = new DomainOperationLog();
            log.setEntityType(clzName);
            Serializable entityId = DomainOperationLogServiceImpl.this.getId(obj);
            if (entityId != null) {
                log.setEntityId(entityId.toString());
                Integer lastVer = DomainOperationLogServiceImpl.this.getLastVer(obj.getClass(), entityId);
                log.setVer(Integer.valueOf(lastVer.intValue() + 1));
            }
            log.setUser(this.user);
            if (log.getUser() != null)
                log.setIp(log.getUser().getLastLoginIP());
            log.setAction(action);
            log.setContent(encodeObject(obj));
            DomainOperationLogServiceImpl.this.record(log);
        }

        private String encodeObject(Object obj) {
            StringBuffer ret = new StringBuffer("");
            PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(obj.getClass());
            for (int i = 0; i < pds.length; i++) {
                try {
                    if (pds[i].getReadMethod() != null) {
                        Object p = pds[i].getReadMethod().invoke(obj, new Object[0]);
                        if (pds[i].getName() != "class") {
                            ret.append(pds[i].getName()).append("=");
                            if (p == null) {
                                ret.append("[NULL]");
                            } else if (JSonConvertUtil.isSimpleType(p.getClass())) {
                                if ((p instanceof Date))
                                    ret.append(DomainOperationLogServiceImpl.this.df.format(p));
                                else
                                    ret.append(p.toString());
                            } else if ((p instanceof Map)) {
                                ret.append("{");
                                Map map = (Map) p;
                                int j = 0;
                                Iterator it = map.keySet().iterator();
                                while (it.hasNext()) {
                                    Object propertyName = (String) it.next();
                                    ret.append(propertyName).append("=");
                                    Object v = map.get(propertyName);
                                    if (v == null) {
                                        ret.append("[NULL]");
                                    } else if (JSonConvertUtil.isSimpleType(v.getClass())) {
                                        ret.append(v.toString());
                                    } else {
                                        Serializable innerId = DomainOperationLogServiceImpl.this.getId(v);
                                        if (innerId != null) {
                                            ret.append(innerId).append("-").append(searchVerAndRecord(v.getClass(), innerId));
                                        }
                                    }
                                    j++;
                                    if (j < map.size())
                                        ret.append(";");
                                }
                                ret.append("}");
                            } else if ((p instanceof Collection)) {
                                ret.append("{");
                                Iterator it = ((Collection) p).iterator();
                                int j = 0;
                                while (it.hasNext()) {
                                    Object v = it.next();
                                    if (v == null) {
                                        ret.append("[NULL]");
                                    } else if (JSonConvertUtil.isSimpleType(v.getClass())) {
                                        ret.append(v.toString());
                                    } else {
                                        Serializable innerId = DomainOperationLogServiceImpl.this.getId(v);
                                        if (innerId != null) {
                                            ret.append(innerId).append("-").append(searchVerAndRecord(v.getClass(), innerId));
                                        }
                                    }
                                    j++;
                                    if (j < ((Collection) p).size())
                                        ret.append(";");
                                }
                                ret.append("}");
                            } else if (p.getClass().isArray()) {
                                ret.append("{");
                                int max = Array.getLength(p);
                                for (int t = 0; t < max; t++) {
                                    Object v = Array.get(p, t);
                                    if (v == null) {
                                        ret.append("[NULL]");
                                    } else if (JSonConvertUtil.isSimpleType(v.getClass())) {
                                        ret.append(v.toString());
                                    } else {
                                        Serializable innerId = DomainOperationLogServiceImpl.this.getId(v);
                                        if (innerId != null) {
                                            ret.append(innerId).append("-").append(searchVerAndRecord(v.getClass(), innerId));
                                        }
                                    }

                                    if (t < max - 1)
                                        ret.append(";");
                                }
                                ret.append("}");
                            } else {
                                Serializable innerId = DomainOperationLogServiceImpl.this.getId(p);
                                if (innerId != null) {
                                    ret.append(innerId).append("-").append(searchVerAndRecord(p.getClass(), innerId));
                                }

                            }

                            if (pds.length > i + 1)
                                ret.append("@@@");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return ret.toString();
        }

        private Integer searchVerAndRecord(Class clz, Serializable id) {
            String clzName = clz.getName();
            if (clzName.indexOf("$") > 0)
                clzName = clzName.substring(0, clzName.indexOf("$"));
            if (id == null)
                return Integer.valueOf(0);
            if (this.caches.contains(clzName + id))
                return Integer.valueOf(1);
            this.caches.add(clzName + id);
            Integer v = DomainOperationLogServiceImpl.this.getLastVer(clz, id);
            if (v.intValue() == 0) {
                String sql = "select obj from " + clzName + " obj where obj.id=?";
                List list = DomainOperationLogServiceImpl.this.dao.query(sql, new Object[] {id}, 0, 1);
                if ((list != null) && (!list.isEmpty())) {
                    recordObject(list.get(0), Integer.valueOf(0));
                    v = Integer.valueOf(1);
                }
            }
            return v;
        }
    }
}
