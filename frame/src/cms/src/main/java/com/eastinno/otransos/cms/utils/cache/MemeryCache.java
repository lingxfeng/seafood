package com.eastinno.otransos.cms.utils.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


/**
 * 
 * 项目名称：disco-cms</BR> <BR>
 * 包名文件名：com.eastinno.otransos.cms.utils.cache.MemeryCache </BR> <BR>
 * 类名称： 内存缓存</BR> <BR>
 * 类描述： TODO</BR> <BR>
 * 创建人：王隐by632656576@qq.com</BR> <BR>
 * 创建时间：2015年3月17日 上午9:49:32 </BR> <BR>
 * 修改人：王隐 </BR> <BR>
 * 修改时间：2015年3月17日 上午9:49:32 </BR> <BR>
 * 修改备注： </BR> <BR>
 * 
 * @version </BR>
 * 
 */
public class MemeryCache implements CmsCache {

    /**
     * cache实现
     */
    private final Cache<String, Object> cache;


    public MemeryCache(long duration, TimeUnit timeUnit, long maxSize) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(duration, timeUnit).maximumSize(maxSize).build();
    }

    private static class CacheHolder {

        /**
         * 时间跨度(duration分钟之后缓存才会更新)
         */
        private static final long duration = 5;

        /**
         * 时间单位(分钟)
         */
        private static final TimeUnit timeUnit = TimeUnit.MINUTES;
        /**
         * 最大个数
         */
        private static final long maxSize = 80000;

        /**
         * 实例
         */
        public static final MemeryCache instance = new MemeryCache(duration, timeUnit, maxSize);
    }


    /**
     * 获取实例
     * 
     * @return
     */
    public static MemeryCache getInstance() {
        return MemeryCache.CacheHolder.instance;
    }


    @Override
    public void put(String key, Object value) {
        if (value != null)
            this.cache.put(key, value);
    }


    @Override
    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        return this.cache.getIfPresent(key);
    }


    @Override
    public boolean exit(String k) {
        return get(k) != null;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.eastinno.otransos.cms.utils.cache.CmsCache#rebulit()
     */
    @Override
    public void clean() {
        this.cache.invalidateAll();
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.eastinno.otransos.cms.utils.cache.CmsCache#remove(java.lang.String)
     */
    @Override
    public void remove(String k) {
        if (exit(k))
            this.cache.invalidate(k);
    }

}
