package com.eastinno.otransos.seafood.spokesman.action;

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
import com.eastinno.otransos.seafood.spokesman.domain.Restitution;
import com.eastinno.otransos.seafood.spokesman.domain.Subsidy;
import com.eastinno.otransos.seafood.spokesman.service.ISubsidyService;

/**
 * SubsidyAction
 * @author 
 */
@Action
public class SubsidyAction extends AbstractPageCmdAction {
    @Inject
    private ISubsidyService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.setOrderBy("time");
    	qo.setOrderType("desc");
        IPageList pageList = this.service.getSubsidyBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/spokesman/subsidy/subsidyList.html");
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
        Date startdate = null;
        Date enddate = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Float level1 =0F;
    	Float level2 =0F;
    	Float level3 =0F;
    	Float calculateCount = 0F;
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
        IPageList pageList = this.service.getSubsidyBy(qo);
        List<Subsidy> list = pageList.getResult();
        if(list != null && list.size()!=0){
        	for(Subsidy subsidy:list){
        		if(subsidy.getRestitution1() != 0 ){
        			level1 += subsidy.getRestitution1();
        			calculateCount += subsidy.getRestitution1();
        		}
        		if(subsidy.getRestitution2() != 0){
        			level2 += subsidy.getRestitution2();
        			calculateCount += subsidy.getRestitution2();
        		}
        		if(subsidy.getRestitution3() != 0){
        			level3 += subsidy.getRestitution3();
        			calculateCount += subsidy.getRestitution3();
        		}
        	}
        	
        }
        form.addResult("level1", level1);
        form.addResult("level2", level2);
        form.addResult("level3", level3);
        form.addResult("calculateCount", calculateCount);
        form.addResult("pl", pageList);
        return new Page("/bcd/spokesman/subsidy/subsidyList.html");
    }
    
    public void setService(ISubsidyService service) {
        this.service = service;
    }
}