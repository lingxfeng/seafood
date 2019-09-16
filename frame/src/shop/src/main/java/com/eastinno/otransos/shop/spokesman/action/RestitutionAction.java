package com.eastinno.otransos.shop.spokesman.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.spokesman.domain.Restitution;
import com.eastinno.otransos.shop.spokesman.service.IRestitutionService;

/**
 * RestitutionAction
 * @author 
 */
@Action
public class RestitutionAction extends AbstractPageCmdAction {
    @Inject
    private IRestitutionService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.setOrderBy("time");
    	qo.setOrderType("desc");
        IPageList pageList = this.service.getRestitutionBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/spokesman/restitution/restitutionList.html");
    }
    /**
     * 统计金额页面
     * 
     * @param form
     */
    public Page doCalculate(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String name = CommUtil.null2String(form.get("name"));
        String starttime = CommUtil.null2String(form.get("starttime"));
        String endtime = CommUtil.null2String(form.get("endtime"));
        String code = CommUtil.null2String(form.get("endtime"));
        form.addResult("name",name);
        form.addResult("starttime",starttime);
        form.addResult("endtime",endtime);
        form.addResult("code",code);
        Float calculateCount = 0F;
        Date startdate = null;
        Date enddate = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	qo.setPageSize(-1);
        if(!"".equals(name)){
        	qo.addQuery("obj.spokesman.member.nickname like '"+name+"%'");
        }
        if(!"".equals(starttime)){
        	try {
    			startdate = sdf.parse(starttime);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
			qo.addQuery("obj.time",startdate, ">");
        }
        if(!"".equals(endtime)){
        	try {
    			enddate = sdf.parse(starttime);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
			qo.addQuery("obj.time",enddate, "<");
        }
    	if(!"".equals(code)){
    		qo.addQuery("obj.order.code",code,"=");
    	}
    	qo.setOrderBy("time");
    	qo.setOrderType("desc");
        IPageList pageList = this.service.getRestitutionBy(qo);
        List<Restitution> list = pageList.getResult();
        if(list != null && list.size()!=0){
        	for(Restitution restitution:list){
        		calculateCount += restitution.getRestitution();
        	}
        	
        }
        form.addResult("calculateCount", calculateCount);
        form.addResult("pl", pageList);
        return new Page("/bcd/spokesman/restitution/restitutionList.html");
    }
    
    public void setService(IRestitutionService service) {
        this.service = service;
    }
}