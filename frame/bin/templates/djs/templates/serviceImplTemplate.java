##set ($domain = $!domainName.toLowerCase())
package $!{packageName}.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.disco.core.support.query.IQueryObject;
import cn.disco.web.tools.IPageList;
import $!{packageName}.domain.$!{domainName};
import $!{packageName}.service.I$!{domainName}Service;
import $!{packageName}.dao.I$!{domainName}DAO;

#macro (upperCase $str)
#set ($upper=$!str.substring(0,1).toUpperCase())
#set ($l=$!str.substring(1))
$!upper$!l#end

/**
 * $!{domainName}ServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class $!{domainName}ServiceImpl implements I$!{domainName}Service{
	@Resource
	private I$!{domainName}DAO $!{domain}Dao;
	
	public void set#upperCase($!{domain})Dao(I$!{domainName}DAO $!{domain}Dao){
		this.$!{domain}Dao=$!{domain}Dao;
	}
	
	public ${idType} add$!{domainName}($!{domainName} $!{domain}) {	
		this.$!{domain}Dao.save($!{domain});
		if ($!{domain} != null && $!{domain}.get$!{id}() != null) {
			return $!{domain}.get$!{id}();
		}
		return null;
	}
	
	public $!{domainName} get$!{domainName}(${idType} id) {
		$!{domainName} $!{domain} = this.$!{domain}Dao.get(id);
		return $!{domain};
		}
	
	public boolean del$!{domainName}(${idType} id) {	
			$!{domainName} $!{domain} = this.get$!{domainName}(id);
			if ($!{domain} != null) {
				this.$!{domain}Dao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDel$!{domainName}s(List<Serializable> $!{domain}Ids) {
		
		for (Serializable id : $!{domain}Ids) {
			del$!{domainName}((${idType}) id);
		}
		return true;
	}
	
	public IPageList get$!{domainName}By(IQueryObject queryObj) {	
		return this.$!{domain}Dao.findBy(queryObj);		
	}
	
	public boolean update$!{domainName}($!{domainName} $!{domain}) {
		if ($!{domain} != null && $!{domain}.getId() != null) {
			this.$!{domain}Dao.update($!{domain});
			return true;
		}
		return false;
	}	
	
}
