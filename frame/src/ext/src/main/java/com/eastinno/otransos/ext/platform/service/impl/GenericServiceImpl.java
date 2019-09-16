package com.eastinno.otransos.ext.platform.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.ext.platform.entity.AbstractEntity;
import com.eastinno.otransos.ext.platform.service.IGenericService;
import com.eastinno.otransos.web.tools.IPageList;

public class GenericServiceImpl<E extends AbstractEntity, ID extends Serializable> implements IGenericService<E, ID> {
    protected IJpaGenericDAO<E, ID> genericDao;

    @Autowired
    public void setGenericDao(IJpaGenericDAO<E, ID> genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public E saveAndFlush(E entity) {
        return this.genericDao.saveAndFlush(entity);
    }

    @Override
    public E update(E entity) {
        this.genericDao.update(entity);
        return entity;
    }

    @Override
    public void delete(Serializable id) {
    }

    @Override
    public void delete(AbstractEntity entity) {
    }

    @Override
    public void delete(Serializable[] ids) {
    }

    @Override
    public E findOne(ID id) {
        return null;
    }

    @Override
    public boolean exists(Serializable id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public IPageList findList(IPageList pl) {
        return null;
    }

}
