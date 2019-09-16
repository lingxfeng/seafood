package $!{packageName}.mvc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.eastinno.otransos.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.AbstractCmdAction;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.core.support.ActionUtil;
import $!{packageName}.service.I$!{domainName}Service;
import $!{packageName}.domain.$!{domainName};
//import $!{packageName}.query.$!{domainName}QueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import $!{packageName}.mvc.command.$!{domainName}Command;

##set ($domain = $!domainName.toLowerCase())
public class $!{domainName}Action extends AbstractCmdAction {
	@InjectDisable
	List<String> errors = new ArrayList<String>();
	
	private I$!{domainName}Service service;

	public void setService(I$!{domainName}Service service) {
		this.service = service;
	}

	@Override
	public Page doInit(WebForm form, Module module) {		
		return doList(form,module);
	}

	/**
	 * 客戶
	 * @param form
	 * @param module
	 * @return
	 */
	public Page doList(WebForm form, Module module) {
	//	$!{domainName}QueryObject qo = new $!{domainName}QueryObject();
		QueryObject qo =form.toPo(QueryObject.class);		
		IPageList pageList = service.get$!{domainName}By(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		return module.findPage("list");
	}
	
	public Page doDelete(WebForm form, Module module) {
		List<Serializable> ids = ActionUtil.processIds(form);
		boolean ret=this.service.batchDel$!{domainName}s(ids);	
		return this.doList(form, module);
	}

	public Page doEdit(WebForm form, Module module) {
		Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
		$!domainName $!domain = this.service.get$!domainName(id);
		if ($!domain != null) {
			form.addPo($!domain);
		}
		return module.findPage("edit");
	}

	public Page doUpdate(WebForm form, Module module) {
		Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
		$!{domainName}Command cmd = new $!{domainName}Command();
		form.toPo(cmd);
		errors = cmd.vaild();
		if(errors.size()>0){
			form.addResult("errors", errors);
			return module.findPage("edit");
		}
		$!{domainName} $!domain = this.service.get$!{domainName}(id);
		com.eastinno.otransos.beans.BeanUtils.copyProperties(cmd, $!domain);
		boolean ret = this.service.update$!{domainName}(id, $!domain);		
		return this.doList(form, module);
	}

	public Page doNew(WebForm form, Module module) {
		return module.findPage("edit");
	}

	public Page doCreate(WebForm form, Module module) {
		$!{domainName}Command cmd = new $!{domainName}Command();
		form.toPo(cmd);		
		errors = cmd.vaild();
		if(errors != null && errors.size()>0){
			form.addResult("errors", errors);			
			return module.findPage("edit");
		}		
		$!{domainName} $!domain = new $!{domainName}();
		com.eastinno.otransos.beans.BeanUtils.copyProperties(cmd, $!domain);
		Long ret = this.service.add$!{domainName}($!domain);		
		return this.doList(form, module);
	}
}