package com.eastinno.otransos.ext.platform.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * <p>
 * 抽象DAO层基类 提供一些简便方法<br/>
 * <p/>
 * 想要使用该接口需要在spring配置文件的jpa:repositories中添加<br />
 * factory-class="com.eastinno.otransos.ext.platform.dao.impl.GenericDaoImpl"
 * <p/>
 * <p>
 * 泛型 ： E 表示实体类型；ID表示主键类型
 * 
 * @version v_0.1
 * @author lengyu
 * @since 2014年1月17日 下午10:28:18
 */
@NoRepositoryBean
public interface IJpaGenericDAO<E, ID extends Serializable> extends JpaRepository<E, ID> {
    /**
     * 根据Id查找一个类型为T的对象。
     * 
     * @param id 传入的ID的值
     * @return 一个类型为T的对象
     */
    E get(ID id);

    /**
     * 根据对象id删除一个对象，该对象类型为T
     * 
     * @param id 需要删除的对象的id。
     */
    void remove(ID id);

    /**
     * 更新一个对象，主要用于更新一个在persistenceContext之外的一个对象。
     * 
     * @param entity 需要更新的对象，该对象不需要在persistenceContext中。
     */
    void update(E entity);

    /**
     * 根据对象的一个属性名和该属性名对应的值来查找一个对象。
     * 
     * @param propertyName 属性名
     * @param value 属性名对应的值
     * @return 一个对象，如果在该属性名和值的条件下找到多个对象，则抛出一个IllegalStateException异常
     */
    E getBy(String propertyName, Object value);

    /**
     * 根据一个查询条件及其参数，还有开始查找的位置和查找的个数来查找任意类型的对象。
     * 
     * @param queryName 命名查询的名字
     * @param params 查询条件中的参数的值。使用Object数组，要求顺序和查询条件中的参数位置一致。
     * @param begin 开始查询的位置
     * @param max 需要查询的对象的个数
     * @return 一个任意对象的List对象，如果没有查到任何数据，返回一个空的List对象。
     */
    List<?> executeNamedQuery(final String queryName, final Object[] params, final int begin, final int max);

    /**
     * 根据一个查询条件及其参数，还有开始查找的位置和查找的个数来查找类型为T的对象。
     * 
     * @param query 查询的条件，使用位置参数，对象名统一为obj，查询条件从where后开始。比如：obj.name = ?1 and obj.properties = ?2
     * @param params 查询条件中的参数的值。使用Object数组，要求顺序和查询条件中的参数位置一致。
     * @param begin 开始查询的位置
     * @param max 需要查询的对象的个数
     * @return 一个该类型对象的List对象，如果没有查到任何数据，返回一个空的List对象。
     */
    List<E> find(String query, Object[] params, int begin, int max);

    /**
     * 根据一个查询条件及其参数，还有开始查找的位置和查找的个数来查找任意类型的对象。
     * 
     * @param jpql 完整的查询语句，使用位置参数。比如：select user from User user where user.name = ?1 and user.properties = ?2
     * @param params 查询条件中的参数的值。使用Object数组，要求顺序和查询条件中的参数位置一致。
     * @param begin 开始查询的位置
     * @param max 需要查询的对象的个数
     * @return 一个任意对象的List对象，如果没有查到任何数据，返回一个空的List对象。
     */
    List<?> query(String jpql, Object[] params, int begin, int max);

    /**
     * 查询符合条件的所有记录
     * 
     * @param jpql 查询语句
     * @param params 参数列表
     * @return 返回符合条件的数据对象,如果没有数据对象则返回null
     */
    List<?> query(final String jpql, Object[] params);

    /**
     * 通过DAO接口查询任意对象
     * 
     * @param jpql
     * @return 返回符合条件的所有记录集,如果没有查到数据,则返回null
     */
    List<?> query(final String jpql);

    /**
     * 根据jpql语句执行批量数据更新等操作
     * 
     * @param jpql 需要执行jpql语句
     * @param params 语句中附带的参数
     * @return
     */
    int batchUpdate(final String jpql, Object[] params);

    /**
     * 执行SQL语句查询
     * 
     * @param nnq
     * @return
     */
    List<?> executeNativeNamedQuery(String nnq);

    List<?> executeNativeNamedQuery(final String nnq, final Object[] params);

    List<?> executeNativeQuery(final String nnq, final Object[] params, final int begin, final int max);
    <T> List<T> executeNativeQuery(final String nnq, final Object[] params, final int begin, final int max,Class<T> c);

    /**
     * 执行SQL语句
     * 
     * @param nnq
     * @return
     */
    int executeNativeSQL(final String nnq);

    /**
     * 执行SQL语句查询，返回单个查询结果
     * 
     * @param jpql 查询语句
     * @return Object
     */
    Object getSingleResult(final String jpql, final Object[] params);

    /*
     * 刷新实体
     */
    void flush();

    /**
     * 根据一个封装后的查询器分页查询一个或多个实体
     * 
     * @param qo disco查询对象
     * @return
     */
    IPageList findBy(IQueryObject qo);

    List<?> queryBySql(String sql);
}
