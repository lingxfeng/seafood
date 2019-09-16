package com.eastinno.otransos.shop.droduct.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.service.IRemoteRegionService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Action
public class RemoteRegionAction extends AbstractPageCmdAction{
	@Inject
	private IRemoteRegionService remoteRegionService;
	@Inject
	private ISystemRegionService systemRegionService;

	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public IRemoteRegionService getRemoteRegionService() {
		return remoteRegionService;
	}

	public void setRemoteRegionService(IRemoteRegionService remoteRegionService) {
		this.remoteRegionService = remoteRegionService;
	}
	
	/**
	 * 批量设置偏远地区
	 * @param form
	 * @return
	 */
	public void doBatchSetRemoteRegion(WebForm form){		
		String[] areaIds = ActionContext.getContext().getRequest().getParameterValues("areaIds");
		if(areaIds == null){
			this.go("toBatchSetRemoteRegion");
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
		if(this.remoteRegionService.batchSetRemoteRegion(regionList)){
			form.addResult("result", "success");
			form.addResult("message", "添加成功！");
		}else{
			form.addResult("result", "failure");
			form.addResult("message", "添加失败！");
		}		
		this.go("toBatchSetRemoteRegion");
	}
	
	/**
	 * 进入批量设置偏远地区的设置页
	 * @param form
	 * @return
	 */
	public Page doToBatchSetRemoteRegion(WebForm form){
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList);
		form.addResult("remoteRegionList", this.remoteRegionService.getAllRemoteRegion());
		return new Page("/bcd/system/remote_region/remoteEdit.html");
	}
}
