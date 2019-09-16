package $!{packageName}.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import $!{packageName}.domain.$!{domainName};
import $!{packageName}.service.I$!{domainName}Service;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.easyjf.beans.BeanUtils;
import cn.disco.core.support.query.QueryObject;
import cn.disco.util.CommUtil;
import cn.disco.web.struts.BaseJSONCrudAction;
import cn.disco.web.tools.IPageList;

##set ($domain = $!domainName.toLowerCase())

/**
 * $!{domainName}Action
 * @author Disco Framework
 */
@Controller("/${domain}")
public class $!{domainName}Action extends BaseJSONCrudAction {
	
	@Autowired
	private I$!{domainName}Service service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(I$!{domainName}Service service) {
		this.service = service;
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryObject qo = new QueryObject();
		CommUtil.map2obj(request.getParameterMap(), qo);
		IPageList pageList = service.get${domainName}By(qo);
		setJSonData(pageList);
		return jsonForward(request, response);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		${domainName} object = new ${domainName}();
		CommUtil.map2obj(request.getParameterMap(), object);
		service.add${domainName}(object);
		return resultForExtForm(request, response);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		${idType} id = BeanUtils.convertType(request.getParameter("${idName}"), ${idType}.class);
		${domainName} object = service.get${domainName}(id);
		CommUtil.map2obj(request.getParameterMap(), object);
		service.update${domainName}(id, object);
		return resultForExtForm(request, response);
	}

	public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		${idType} id = BeanUtils.convertType(request.getParameter("${idName}"), ${idType}.class);
		service.del${domainName}(id);
		return resultForExtForm(request, response);
	}
}