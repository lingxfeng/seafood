package com.eastinno.otransos.core.domain;

/**
 * @intro 处理数据状态信息
 * @version v0.1
 * @author maowei
 * @since 2014年5月20日 下午5:45:37
 */
public abstract interface Recover {
    /**
     * 逻辑性删除用户帐号状态改为-2 （只有租户管理员才可以删除）
     */
    public abstract void temporaryRemove();

    /**
     * 逻辑性恢复用户帐号状态改为-1（锁定）（可以由锁定变回正常）
     */
    public abstract void revert();
}
