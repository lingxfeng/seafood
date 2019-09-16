package $!{packageName}.mvc;

import $!{packageName}.domain.$!{domainName};
import $!{packageName}.service.I$!{domainName}Service;

import com.easyjf.beans.BeanUtils;
import cn.disco.container.annonation.Action;
import cn.disco.container.annonation.Inject;
import cn.disco.core.support.query.QueryObject;
import cn.disco.web.Module;
import cn.disco.web.Page;
import cn.disco.web.WebForm;
import cn.disco.web.core.AbstractPageCmdAction;
import cn.disco.web.tools.IPageList;

##set ($domain = $!domainName.toLowerCase())

/**
 * $!{domainName}Action
 * @author Disco Framework
 */
@Action
public class $!{domainName}Action extends AbstractPageCmdAction {
	
	@Inject
	private I$!{domainName}Service service;
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = service.get$!{domainName}By(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		${idType} id=BeanUtils.convertType(form.get("$!{idName}"), ${idType}.class);
		service.del$!{domainName}(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		$!{domainName} object = form.toPo($!{domainName}.class);
		if (!hasErrors())
			service.add$!{domainName}(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		${idType} id=(${idType})BeanUtils.convertType(form.get("$!{idName}"), ${idType}.class);
		$!{domainName} object = service.get$!{domainName}(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.update$!{domainName}(id, object);
		return pageForExtForm(form);
	}
	
	public void setService(I$!{domainName}Service service) {
		this.service = service;
	}
}