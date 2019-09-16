package $!{packageName}.mvc;

import java.io.Serializable;
import java.util.List;

import cn.disco.container.annonation.Action;
import cn.disco.container.annonation.Inject;
import cn.disco.core.support.query.IQueryObject;
import cn.disco.web.tools.AbstractCrudAction;
import cn.disco.web.tools.IPageList;

import $!{packageName}.domain.$!{domainName};
import $!{packageName}.service.I$!{domainName}Service;
##set ($domain = $!domainName.toLowerCase())

/**
 * $!{domainName}Action
 * @author ksmwly@gmail.com
 */
@Action
public class $!{domainName}Action extends AbstractCrudAction {
	@Inject
	private I$!{domainName}Service service;

	
	protected String getIdName() {
		return "$!{idName}";
	}
	@SuppressWarnings("unchecked")
	protected Class getIdClass() {
		return ${idType}.class;
	}
	/*
	 * to get the entity class
	 */
	@SuppressWarnings("unchecked")
	protected Class entityClass() {
		return $!{domainName}.class;
	}

	/*
	 * to find the entity object
	 */
	protected Object findEntityObject(Serializable id) {
		return service.get$!{domainName}(($!{idType}) id);
	}

	/*
	 * to get the entity query
	 * param queryObject
	 * return IPageList
	 */
	protected IPageList queryEntity(IQueryObject queryObject) {		
		return service.get$!{domainName}By(queryObject);
	}

	/*
	 * to remove an entity
	 * param id
	 */
	protected void removeEntity(Serializable id) {
		service.del$!{domainName}(($!{idType}) id);
	}
	
	/*
	 * to batch remove the entities
	 * param ids
	 */
	protected void batchRemoveEntity(List<Serializable> ids) {
		service.batchDel$!{domainName}s(ids);
	}

	/*
	 * save object to entity
	 */
	protected void saveEntity(Object object) {
		service.add$!{domainName}(($!{domainName}) object);
	}

	/*
	 * update an entited object 
	 */
	protected void updateEntity(Object object) {
		service.update$!{domainName}((($!{domainName}) object).get$!{id}(), ($!{domainName}) object);
	}
	public void setService(I$!{domainName}Service service) {
		this.service = service;
	}
}