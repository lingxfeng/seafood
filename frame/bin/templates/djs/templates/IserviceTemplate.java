package $!{packageName}.service;

import java.io.Serializable;
import java.util.List;

import cn.disco.web.tools.IPageList;
import cn.disco.core.support.query.IQueryObject;
import $!{packageName}.domain.$!{domainName};
/**
 * $!{domainName}Service
 * @author ksmwly@gmail.com
 */
public interface I$!{domainName}Service {
	/**
	 * 保存一个$!{domainName}，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	${idType} add$!{domainName}($!{domainName} domain);
	
	/**
	 * 根据一个ID得到$!{domainName}
	 * 
	 * @param id
	 * @return
	 */
	$!{domainName} get$!{domainName}(${idType} id);
	
	/**
	 * 删除一个$!{domainName}
	 * @param id
	 * @return
	 */
	boolean del$!{domainName}(${idType} id);
	
	/**
	 * 批量删除$!{domainName}
	 * @param ids
	 * @return
	 */
	boolean batchDel$!{domainName}s(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到$!{domainName}
	 * 
	 * @param properties
	 * @return
	 */
	IPageList get$!{domainName}By(IQueryObject queryObj);
	
	/**
	  * 更新一个$!{domainName}
	  * @param dir 需要更新的$!{domainName}
	  */
	boolean update$!{domainName}($!{domainName} entity);
}
