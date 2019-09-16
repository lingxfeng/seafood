package com.eastinno.otransos.seafood.droduct.action;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.seafood.droduct.service.INearRegionService;

/**
 * NearRegionAction
 * @author 
 */
@Action
public class NearRegionAction extends AbstractPageCmdAction {
	@Inject
	private INearRegionService nearRegionService;
	@Inject
	private ISystemRegionService systemRegionService;

	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public INearRegionService getNearRegionService() {
		return nearRegionService;
	}

	public void setNearRegionService(INearRegionService nearRegionService) {
		this.nearRegionService = nearRegionService;
	}
	
	/**
	 * 批量设置附近地区
	 * @param form
	 * @return
	 */
	public void doBatchSetNearRegion(WebForm form){		
		String[] areaIds = ActionContext.getContext().getRequest().getParameterValues("areaIds");
		if(areaIds == null){
			this.go("toBatchSetNearRegion");
			return;
		}
			
		List<SystemRegion> regionList = new ArrayList();
		Long areaId = 0L;
		for(int i=0; i<areaIds.length; ++i){
			try{
				areaId = Long.parseLong(areaIds[i]);
				SystemRegion region = this.systemRegionService.getSystemRegion(areaId);
				if(region != null){
					regionList.add(region);	
				}				
			}catch(ClassCastException e){
				e.printStackTrace();
			}
		}
		if(this.nearRegionService.batchSetNearRegion(regionList)){
			form.addResult("result", "success");
			form.addResult("message", "添加成功！");
		}else{
			form.addResult("result", "failure");
			form.addResult("message", "添加失败！");
		}		
		this.go("toBatchSetNearRegion");
	}
	
	/**
	 * 进入批量设置附近地区的设置页
	 * @param form
	 * @return
	 */
	public Page doToBatchSetNearRegion(WebForm form){
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList);
		form.addResult("nearRegionList", this.nearRegionService.getAllNearRegion());
		return new Page("/bcd/system/near_region/nearEdit.html");
	}
}