package com.eastinno.otransos.seafood.droduct.action;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
import com.eastinno.otransos.seafood.droduct.service.INearRegionService;
import com.eastinno.otransos.seafood.droduct.service.IRegionClassService;
import com.eastinno.otransos.seafood.droduct.service.IRemoteRegionService;
import com.eastinno.otransos.seafood.util.formatUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * RegionClassAction
 * @author 
 */
@Action
public class RegionClassAction extends AbstractPageCmdAction {
    @Inject
    private IRegionClassService service;
    @Inject
    private ISystemRegionService systemRegionService;
    @Inject
    private IRemoteRegionService remoteRegionService;
    @Inject
    private INearRegionService nearRegionService;
    
    
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

	public INearRegionService getNearRegionService() {
		return nearRegionService;
	}

	public void setNearRegionService(INearRegionService nearRegionService) {
		this.nearRegionService = nearRegionService;
	}

	public IRegionClassService getService() {
		return service;
	}
	/**
     * 邮费方案列表页面
     * 
     * @param form
     */
    public Page doRegionClassList(WebForm form) {
    	QueryObject qo = new QueryObject();
    	qo.setLimit(-1);
        IPageList pageList = this.service.getRegionClassBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/shopmanage/regionclass/RegionClassList.html");
    }
    /**
     *新增邮费方案页面
     * 
     * @param form
     */
    public Page doToAddRegionClass(WebForm form) {
        return new Page("/shopmanage/regionclass/RegionClassEdit.html");
    }
    /**
     *新增邮费方案保存页面
     * 
     * @param form
     */
    public Page doAddRegionClass(WebForm form) {
    	RegionClass entry = (RegionClass)form.toPo(RegionClass.class);
    	form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addRegionClass(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        form.addResult("entry", entry);
        return new Page("/shopmanage/regionclass/RegionClassEdit.html");
    }
    /**
     *修改邮费方案页面
     * 
     * @param form
     */
    public Page doToUpdateRegionClass(WebForm form) {
    	 Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
    	 RegionClass reginclass = this.service.getRegionClass(id);
    	 form.addResult("entry", reginclass);
         form.addResult("fu", formatUtil.fu);
        return new Page("/shopmanage/regionclass/RegionClassEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdateRegionClass(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RegionClass reginclass = this.service.getRegionClass(id);
        form.toPo(reginclass);
        if (!hasErrors()) {
            boolean ret = service.updateRegionClass(id,reginclass);
            if(ret){
                form.addResult("msg", "修改成功");
                //修改后展示修改信息
                form.addResult("entry", reginclass);
                form.addResult("fu", formatUtil.fu);
            }
        }     
        return new Page("/shopmanage/regionclass/RegionClassEdit.html");
    }
	/**
	 * 进入批量设置偏远地区的设置页
	 * @param form
	 * @return
	 */
	public Page doToBatchSetRemoteRegion(WebForm form){
		Long regionId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("regionId"))));
		form.addResult("regionId",regionId);
		RegionClass regionClass = this.service.getRegionClass(regionId);
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList);
		form.addResult("remoteRegionList", this.remoteRegionService.getAllRemoteRegion2(regionClass));
		return new Page("/bcd/system/remote_region/remoteEdit2.html");
	}
	/**
	 * 批量设置偏远地区
	 * @param form
	 * @return
	 */
	public Page doBatchSetRemoteRegion(WebForm form){
		String[] areaIds = ActionContext.getContext().getRequest().getParameterValues("areaIds");
		
		Long regionId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("regionId"))));
		RegionClass regionClass = this.service.getRegionClass(regionId);
		form.addResult("regionId",regionId);
		
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
		if(this.remoteRegionService.batchSetRemoteRegion2(regionList,regionClass)){
			form.addResult("result", "success");
			form.addResult("message", "添加成功！");
		}else{
			form.addResult("result", "failure");
			form.addResult("message", "添加失败！");
		}
		
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList2 = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList2 = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList2);
		form.addResult("remoteRegionList", this.remoteRegionService.getAllRemoteRegion2(regionClass));
		return new Page("/bcd/system/remote_region/remoteEdit2.html");
	}
	/**
	 * 进入批量设置附近地区的设置页
	 * @param form
	 * @return
	 */
	public Page doToBatchSetNearRegion(WebForm form){
		Long regionId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("regionId"))));
		form.addResult("regionId",regionId);
		RegionClass regionClass = this.service.getRegionClass(regionId);
		
		
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList);
		form.addResult("nearRegionList", this.nearRegionService.getAllNearRegion2(regionClass));
		return new Page("/bcd/system/near_region/nearEdit2.html");
	}
	
	/**
	 * 批量设置附近地区
	 * @param form
	 * @return
	 */
	public Page doBatchSetNearRegion(WebForm form){		
		String[] areaIds = ActionContext.getContext().getRequest().getParameterValues("areaIds");
		
		Long regionId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("regionId"))));
		RegionClass regionClass = this.service.getRegionClass(regionId);
		form.addResult("regionId",regionId);
		
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
		if(this.nearRegionService.batchSetNearRegion2(regionList,regionClass)){
			form.addResult("result", "success");
			form.addResult("message", "添加成功！");
		}else{
			form.addResult("result", "failure");
			form.addResult("message", "添加失败！");
		}		
		QueryObject qo = new QueryObject();
		List<SystemRegion> regionList2 = this.systemRegionService.querySystemRegion(qo).getResult();
		regionList2 = this.systemRegionService.getRootSystemRegions().getResult();
		form.addResult("regionList", regionList2);
		form.addResult("nearRegionList", this.nearRegionService.getAllNearRegion2(regionClass));
		return new Page("/bcd/system/near_region/nearEdit2.html");
	}	
    /**
     * 删除数据
     * 
     * @param form
     */
    public void doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delRegionClass(id);
        go("regionClassList");
    }
    
    public void setService(IRegionClassService service) {
        this.service = service;
    }
}