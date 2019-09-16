package com.eastinno.otransos.web.tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateDaoSurport<T> implements IDAO<T> {

    private static SessionFactory sessionFactory;

    private static java.lang.ThreadLocal<Session> localSession;

    private static HibernateDaoSurport singleton = new HibernateDaoSurport();

    public static HibernateDaoSurport getInstance() {
        return singleton;
    }

    static {
        sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    public Session getCurrentSession() {
        Session s = localSession.get();
        if (s == null) {
            s = sessionFactory.openSession();
            localSession.set(s);
        }
        return s;
    }

    public HibernateDaoSurport() {

    }

    public boolean del(Object obj) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().delete(obj);
        tx.commit();
        return true;
    }

    public int execute(String sql) {
        Query query = getCurrentSession().createQuery(sql);
        return query.executeUpdate();
    }

    public int execute(String sql, Collection paras) {
        Query query = getCurrentSession().createQuery(sql);
        int parameterIndex = 0;
        if (paras != null && paras.size() > 0) {
            for (Object obj : paras) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        return query.executeUpdate();
    }

    public T get(Class<T> clz, Serializable id) {
        return (T) getCurrentSession().get(clz, id);
    }

    public T getBy(Class<T> clz, String fieldName, Serializable value) {
        Query query = getCurrentSession().createQuery("from " + clz + " where fieldName=?");
        query.setParameter(0, value);
        return (T) query.uniqueResult();
    }

    public List query(Class clz, String scope) {
        return query(clz, scope, null);
    }

    public List query(Class clz, String scope, Collection paras) {
        return query(clz, scope, null, -1, -1);
    }

    public List query(Class clz, String scope, Collection paras, int begin, int max) {
        Query query = getCurrentSession().createQuery("from " + clz + " where " + scope);
        int parameterIndex = 0;
        if (paras != null && paras.size() > 0) {
            for (Object obj : paras) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        return query.list();
    }

    public boolean save(final Object obj) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().save(obj);
        tx.commit();
        return true;
    }

    public Object uniqueResult(String sql) {
        return uniqueResult(sql, null);
    }

    public Object uniqueResult(String sql, Collection paras) {
        Query query = getCurrentSession().createQuery(sql);
        int parameterIndex = 0;
        if (paras != null && paras.size() > 0) {
            for (Object obj : paras) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        return query.uniqueResult();
    }

    public boolean update(Object obj) {
        Transaction tx = getCurrentSession().beginTransaction();
        getCurrentSession().update(obj);
        tx.commit();
        return true;
    }
}
