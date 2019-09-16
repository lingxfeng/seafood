package com.eastinno.otransos.core.dao.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.web.POLoadDao;

@Service
public class JpaPOLoaderImpl extends JpaEntityManagerImpl implements POLoadDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Object get(Class clz, Serializable id) {
        return this.getEntityManager().find(clz, id);
    }

}
