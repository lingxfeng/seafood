package com.eastinno.otransos.ext.platform.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.PageObject;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.support.query.GenericPageList;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * @intro 抽象基础JPA Repository 实现
 * @version v_0.1
 * @author lengyu
 * @since 2014年1月17日 下午10:47:29
 */
public class CustomJpaGenericDAO<E, ID extends Serializable> extends SimpleJpaRepository<E, ID> implements IJpaGenericDAO<E, ID> {
    private Class<E> clz;// 实体类
    private String entityName;// 实体类名
    private final EntityManager em;// 实体管理器
    private final JpaEntityInformation<E, ?> entityInformation;

    @Override
    public E get(ID id) {
        return findOne(id);
    }

    @Override
    public void remove(ID id) {
        delete(id);
    }

    @Override
    public void update(E entity) {
        this.save(entity);
    }

    @Override
    @SuppressWarnings("null")
    public E getBy(final String propertyName, final Object value) {
        if (propertyName == null || "".equals(propertyName) || value == null) {
            String str = "ext.Call.parameter.is.not.correct.attribute.names.and.values.are.not.empty";
            throw new IllegalArgumentException(I18n.getLocaleMessage(str));
        }
        StringBuffer sb = new StringBuffer("select obj from ").append(entityName).append(" obj");
        Query query = null;
        if (propertyName != null && value != null) {
            sb.append(" where obj.").append(propertyName).append(" = :value");
            query = this.em.createQuery(sb.toString()).setParameter("value", value);
            query.setHint("org.hibernate.cacheable", true);// 查询是否与二级缓存交互(默认值为false)
        } else {
            query = this.em.createQuery(sb.toString());
            query.setHint("org.hibernate.cacheable", true);// 查询是否与二级缓存交互(默认值为false)
        }

        List<E> ret = query.getResultList();
        if (ret != null && ret.size() == 1) {
            return ret.get(0);
        } else if (ret != null && ret.size() > 1) {
            throw new IllegalStateException("worning  --more than one object find!!");
        } else {
            return null;
        }
    }

    @Override
    public List<?> executeNamedQuery(final String queryName, final Object[] params, final int begin, final int max) {
        Query query = this.em.createNamedQuery(queryName);
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        List<?> ret = query.getResultList();
        if (ret != null && ret.size() >= 0) {
            return ret;
        } else {
            return new ArrayList<Object>();
        }
    }

    @Override
    public List<E> find(final String queryStr, final Object[] params, final int begin, final int max) {
        StringBuffer sb = new StringBuffer("select obj from ");
        sb.append(entityName).append(" obj").append(" where ").append(queryStr);
        Query query = this.em.createQuery(sb.toString());
        query.setHint("org.hibernate.cacheable", true);// 查询是否与二级缓存交互(默认值为false)
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        List<E> ret = query.getResultList();
        if (ret != null && ret.size() >= 0) {
            return ret;
        } else {
            return new ArrayList<E>();
        }

    }

    @Override
    public List<?> query(String jpql, Object[] params, int begin, int max) {
        Query query = this.em.createQuery(jpql);
        query.setHint("org.hibernate.cacheable", true);// 查询是否与二级缓存交互(默认值为false)
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        List<?> ret = query.getResultList();
        if (ret != null && ret.size() >= 0) {
            return ret;
        } else {
            return new ArrayList<Object>();
        }

    }

    @Override
    public List<?> query(String jpql, Object[] params) {
        return query(jpql, params, 0, -1);
    }

    @Override
    public List<?> query(String jpql) {
        return query(jpql, null);
    }

    @Override
    public int batchUpdate(String jpql, Object[] params) {
        Query query = this.em.createQuery(jpql);
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        return (Integer) query.executeUpdate();
    }

    @Override
    public List<?> executeNativeNamedQuery(final String sql) {
        Query query = this.em.createNamedQuery(sql);
        return query.getResultList();
    }

    @Override
    public List<?> executeNativeNamedQuery(String nnq, Object[] params) {
        Query query = this.em.createNamedQuery(nnq);
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        return query.getResultList();
    }

    @Override
    public List<?> executeNativeQuery(final String nnq, final Object[] params, final int begin, final int max) {
        Query query = this.em.createNativeQuery(nnq);
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        List<?> ret = query.getResultList();
        if (ret != null && ret.size() >= 0) {
            return ret;
        } else {
            return new ArrayList<Object>();
        }
    }
    @Override
    public <T> List<T> executeNativeQuery(final String nnq, final Object[] params, final int begin, final int max,Class<T> c) {
        Query query = this.em.createNativeQuery(nnq,c);
        int parameterIndex = 1;
        if (params != null && params.length > 0) {
            for (Object obj : params) {
                query.setParameter(parameterIndex++, obj);
            }
        }
        if (begin >= 0 && max > 0) {
            query.setFirstResult(begin);
            query.setMaxResults(max);
        }
        List<T> ret = query.getResultList();
        if (ret != null && ret.size() >= 0) {
            return ret;
        } else {
            return new ArrayList<T>();
        }
    }
    @Override
    public int executeNativeSQL(String nnq) {
        Query query = this.em.createNativeQuery(nnq);
        return query.executeUpdate();
    }

    @Override
    public Object getSingleResult(String jpql, Object[] params) {
        List<?> list = this.query(jpql, params, 0, 1);
        if (list != null && !list.isEmpty())
            return list.get(0);
        else
            return null;
    }

    public CustomJpaGenericDAO(JpaEntityInformation<E, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
        this.entityInformation = entityInformation;
        this.clz = this.entityInformation.getJavaType();
        this.entityName = this.entityInformation.getEntityName();
        this.entityInformation.getIdAttributeNames().iterator().next();
    }

    @Override
    public IPageList findBy(IQueryObject qo) {
        PageObject pageObj = qo.getPageObj();
        int currentPage = pageObj.getCurrentPage();
        int pageSize = pageObj.getPageSize();
        GenericPageList pageList = new GenericPageList(clz, qo, this);
        pageList.doList(currentPage, pageSize);// 查询第几页，每页多少条

        return pageList;
    }

    public Class<E> getClz() {
        return clz;
    }

    public void setClz(Class<E> clz) {
        this.clz = clz;
    }

    @Override
    public List<?> queryBySql(String sql) {
        if (StringUtils.isEmpty(sql))
            return null;
        // EntityTransaction transaction = this.em.getTransaction();
        // transaction.begin();
        Query query = this.em.createNativeQuery(sql);
        return query.getResultList();
        // transaction.commit();
    }

}
