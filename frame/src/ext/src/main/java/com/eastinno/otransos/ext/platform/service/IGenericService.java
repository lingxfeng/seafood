package com.eastinno.otransos.ext.platform.service;

import java.io.Serializable;

import com.eastinno.otransos.ext.platform.entity.AbstractEntity;
import com.eastinno.otransos.web.tools.IPageList;

public abstract interface IGenericService<E extends AbstractEntity, ID extends Serializable> {
    /**
     * 保存单个实体并刷新
     * 
     * @param entity 实体
     * @return 返回保存的实体
     */
    E saveAndFlush(E entity);

    /**
     * 更新单个实体
     * 
     * @param entity 实体
     * @return 返回更新的实体
     */
    E update(E entity);

    /**
     * 根据主键删除相应实体
     * 
     * @param id 主键
     */
    void delete(ID id);

    /**
     * 删除实体
     * 
     * @param entity 实体
     */
    void delete(E entity);

    /**
     * 根据主键删除相应实体
     * 
     * @param ids 实体
     */
    void delete(ID[] ids);

    /**
     * 按照主键查询
     * 
     * @param id 主键
     * @return 返回id对应的实体
     */
    E findOne(ID id);

    /**
     * 实体是否存在
     * 
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    boolean exists(ID id);

    /**
     * 统计实体总数
     * 
     * @return 实体总数
     */
    long count();

    /**
     * 通过一个查询对象分页查询数据
     * 
     * @param pl 分页、排序、查询条件
     * @return
     */
    IPageList findList(IPageList pl);
}
