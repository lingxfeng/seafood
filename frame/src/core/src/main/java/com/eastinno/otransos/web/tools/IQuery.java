package com.eastinno.otransos.web.tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 分页查询算法接口
 * 
 * @author lengyu
 */
public interface IQuery extends Serializable{
    /**
     * 根据查询条件返回记录总数
     * 
     * @param qeryStr 查询条件
     * @return 查询记录结果总数
     */
    int getRows(String qeryStr);

    /**
     * 根据查询条件返回符合条件的结果数
     * 
     * @param qeryStr 查询条件
     * @return 根据条件获得查询结果集
     */
    List getResult(String qeryStr);

    /**
     * 设置有效结果记录的开始位置
     * 
     * @param begin
     */
    void setFirstResult(int begin);

    /**
     * 最大返回记录数
     * 
     * @param max
     */
    void setMaxResults(int max);

    /**
     * 设置查询参数
     * 
     * @param paraValues 查询条件值
     */
    void setParaValues(Collection paraValues);

    /**
     * 根据查询条件，记录开始位置及最大记录数返回有效查询结果
     * 
     * @param qeryStr
     * @param begin
     * @param max
     * @return 指定范围内的查询结果记录集
     */
    List getResult(String qeryStr, int begin, int max);
}
