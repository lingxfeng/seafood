package com.eastinno.otransos.web.tools;

import java.io.Serializable;

/**
 * 具有顽强生命能力的持久层对象，通过save、update、del等方法，知道如何维护自己的持久数据。
 * 
 * @author lengyu
 */
public interface ICommObj {
    /**
     * 新增
     * 
     * @return 若保存成功则返回true，否则返回false
     * @throws IdExistException
     */
    boolean save() throws IdExistException;

    /**
     * 修改
     * 
     * @return 若修改成功则返回true，否则返回false
     */
    boolean update();

    /**
     * 删除
     * 
     * @return 若删除成功则返回true，否则返回false
     */
    boolean del();

    /**
     * 根据id返回
     * 
     * @param id
     * @return 若查找到对象则返回该对象，否则返回null
     */
    Object get(Serializable id);
}
