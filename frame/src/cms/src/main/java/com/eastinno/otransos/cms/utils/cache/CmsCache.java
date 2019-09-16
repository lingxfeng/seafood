package com.eastinno.otransos.cms.utils.cache;

/**
 * 
 * 项目名称：disco-cms</BR> <BR>
 * 包名文件名：com.eastinno.otransos.cms.utils.cache.CmsCache </BR> <BR>
 * 类名称： </BR> <BR>
 * 类描述： TODO</BR> <BR>
 * 创建人：王隐by632656576@qq.com</BR> <BR>
 * 创建时间：2015年3月17日 上午9:28:54 </BR> <BR>
 * 修改人：王隐 </BR> <BR>
 * 修改时间：2015年3月17日 上午9:28:54 </BR> <BR>
 * 修改备注： </BR> <BR>
 * 
 * @version </BR>
 * 
 */
public interface CmsCache {

    /**
     * 放置缓存
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object T);


    /**
     * 返回缓存值
     * 
     * @param key
     * @return
     */
    public Object get(String key);


    /**
     * 测试是否存在
     * 
     * @param k
     * @return
     */
    public boolean exit(String k);


    /**
     * 
     * @param k
     */
    public void remove(String k);


    public void clean();
}
