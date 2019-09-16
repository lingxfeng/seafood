package com.eastinno.otransos.platform.weixin.mvc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.http.util.HttpHelper;
import com.eastinno.otransos.platform.weixin.bean.AccessToken;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerGroup;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerGroupService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * GroupAction
 * @author 
 */
@Action
public class FollowerGroupAction extends AbstractPageCmdAction {
    @Inject
    private IFollowerGroupService service;
    @Inject
    private IAccountService accountService;
    @Inject
    private IFollowerService followerService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String div = CommUtil.null2String(form.get("div"));
    	if(StringUtils.isEmpty(div)){
    		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
	    	String  accountId = CommUtil.null2String(form.get("accountId"));
	        if(!StringUtils.isEmpty(accountId)){
	        	Long id = Long.valueOf(Long.parseLong(accountId));
	        	qo.addQuery("obj.account.id", id, "=");
//	        	try {
//					getAllGroupFromWX(id);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	        }
	        IPageList pageList = this.service.getFollowerGroupBy(qo);
			AjaxUtil.convertEntityToJson(pageList);
	        form.jsonResult(pageList);
    	}else{
    		QueryObject qo1 = (QueryObject) form.toPo(QueryObject.class);
        	String  groupId = CommUtil.null2String(form.get("groupId"));
            if(!StringUtils.isEmpty(groupId)){
            	Long id = Long.valueOf(Long.parseLong(groupId));
            	qo1.addQuery("obj.followerGroup.id", id, "=");
            }else{
            	return Page.nullPage;
            }
    	    IPageList ipList = followerService.getFollowerBy(qo1);
    	    AjaxUtil.convertEntityToJson(ipList);
	        form.jsonResult(ipList);
    	}   
    	 return Page.JSONPage;
    }
    
    public void getAllGroupFromWX(Long id ) throws Exception{
    	String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
    	Account account = accountService.getAccount(id);
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
		String contentstr = HttpHelper.get(requestUrl).getContent();
		System.out.println(contentstr);
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) throws Exception {
        FollowerGroup group = (FollowerGroup)form.toPo(FollowerGroup.class);
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
        	group.setTenant(tenant);
        }
        String aId = CommUtil.null2String(form.get("accountId"));
        if(StringUtils.isEmpty(aId)){
        	this.addError("msg", "请先选公共号！！！");
        	Page page = pageForExtForm(form);
		    page.setContentType("html");
		    return page;
        }
        Long accountId = Long.valueOf(Long.parseLong(aId));
        Account account = accountService.getAccount(accountId);
        if(account == null){
        	this.addError("msg", "无法获取您所选公共号信息！！！");
        }else{
        	group.setAccount(account);
        }
        //调用微信接口，将新建分组放到微信服务器 开始
        Map<String,Object> mapjson=null;
        String name = group.getName();
        String code = group.getCode();
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(code)){
			this.addError("msg", "请确认分组名称或编码是否填写");
		}
		if(hasErrors()){
			Page page = pageForExtForm(form);
		    page.setContentType("html");
		    return page;
		}
		//验证分组、分组编号是否存在
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.code", group.getCode(), "=");
		qo.addQuery("obj.account.id", accountId, "=");
		IPageList codeIPList = service.getFollowerGroupBy(qo);
		if(codeIPList != null && codeIPList.getResult() != null && codeIPList.getResult().size() > 0){
			this.addError("msg", "该编码已经被使用，请更换！！！");
			Page page = pageForExtForm(form);
		    page.setContentType("html");
		    return page;
		}
		//生成所需POST参数  开始
		Map<String , String > nameMap = new HashMap<String, String>();
		nameMap.put("name", name);
		Map<String , Object> jsonMap = new HashMap<String , Object>();
		jsonMap.put("group", nameMap);
		JSONObject jo = new JSONObject(jsonMap);
        String jsonPram = jo.toJSONString();
        //生成所需POST参数 结束
        //调用API 返回数据并解析  开始
        String url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        if("40001".equals(jSONObject.getShort("errcode"))){
        	 sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	 requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
             jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        }
		JSONObject groupJson = (JSONObject) jSONObject.get("group");
		Integer idJson = (Integer) groupJson.get("id");
		if(idJson != null){
			group.setWxGroupId(idJson);
		}else{
			this.addError("msg", "微信服务器端未返回，保存失败！！！");
		}
		//调用API 返回数据并解析 结束
        if (!hasErrors()) {
            Long id = this.service.addFollowerGroup(group);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	String name = CommUtil.null2String(form.get("name"));
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        FollowerGroup group = this.service.getFollowerGroup(id);
        if (!hasErrors()) {
        	//调用微信接口修改分组名 开始
        	Map<String , Object> map = new HashMap<String , Object>();
        	map.put("id", group.getWxGroupId());
        	map.put("name", name);
        	Map<String , Object> mapParam = new HashMap<String , Object>();
        	mapParam.put("group", map);
    		JSONObject jo = new JSONObject(mapParam);
            String jsonPram = jo.toJSONString();
            //生成所需POST参数 结束
            //调用API 返回数据并解析  开始
            String url = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
    		AccessToken sccessToken = RequestWxUtils.getAccessToken(group.getAccount());
    		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
            if("40001".equals(jSONObject.getShort("errcode"))){
            	 sccessToken = RequestWxUtils.getAccessTokenNew(group.getAccount());
            	 requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
                 jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
            }
    		String responseJson = (String) jSONObject.get("errmsg");
        	//调用微信接口修改分组名 结束 
    		if("ok".equals(responseJson)){
    			form.toPo(group);
                boolean ret = service.updateFollowerGroup(id, group);
                if(ret){
                    form.addResult("msg", "修改成功");
                }
    		}else{
    			this.addError("msg", "微信服务器端出错，更新失败");
    		}
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        String status = deleFromWXGroup(id);
        if("false".equals(status)){
        	this.addError("msg", "微信端失败，请重试！！！");
            Page page = pageForExtForm(form);
            page.setContentType("html");
            return page;
        }
        //查询该分组下的所有粉丝
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.followerGroup.id", id, "=");
        IPageList ipList = followerService.getFollowerBy(qo);
        if(ipList != null && ipList.getResult() != null && ipList.getResult().size() > 0){
        	List<Follower> followerList = ipList.getResult();
        	for(Follower follower : followerList){
        		follower.setFollowerGroup(null);
        		followerService.updateFollower(follower.getId(), follower);
        	}
        }
        this.service.delFollowerGroup(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 微信端删除分组
     * @param id
     * @return
     */
    public String deleFromWXGroup(Long id){
    	FollowerGroup group = service.getFollowerGroup(id);
    	//调用微信端接口 删除分组 开始
		Map<String , Object> map = new HashMap<String , Object>();
    	map.put("id", group.getWxGroupId());
    	Map<String , Object> parMap = new HashMap<String , Object>();
    	parMap.put("group", map);
    	JSONObject jo = new JSONObject(parMap);
        String jsonPram = jo.toJSONString();
        String url = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
		AccessToken sccessToken = RequestWxUtils.getAccessToken(group.getAccount());
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        if("40001".equals(jSONObject.getShort("errcode"))){
        	 sccessToken = RequestWxUtils.getAccessTokenNew(group.getAccount());
        	 requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
             jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        }
		String responseJson = (String) jSONObject.get("errmsg");
		if(responseJson == null){
			return "success";
		}
    	return "false";
    }
    
    /**
     * 功能：获取该公共号下所有未分组的粉丝
     * 描述：点击添加粉丝时查询所有该公共号下未分组的粉丝时调用该方法
     * @param form
     * @return
     */
    public Page doGetFollower(WebForm form){
    	String groupId = CommUtil.null2String(form.get("groupId"));
    	if(StringUtils.isEmpty(groupId) || "undefined".equals(groupId)){
            return Page.nullPage;
    	}
    	FollowerGroup followerGroup = service.getFollowerGroup(Long.parseLong(groupId));
    	//FollowerGroup followerGroup = service.getFollowerGroup(196608l);
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.account.id", followerGroup.getAccount().getId(), "=");
    	qo.addQuery("obj.followerGroup.id  is  null");
    	IPageList ipList = followerService.getFollowerBy(qo);
    	AjaxUtil.convertEntityToJson(ipList);
        form.jsonResult(ipList);
        return Page.JSONPage;
    }
    
    /**
     * 查询所有分组，除去当前粉丝所在的分组
     * @param form
     * @return
     */
    public Page doGetGroup(WebForm form){
    	Long followerId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
    	Follower follower = followerService.getFollower(followerId);
    	if(follower == null){
    		this.addError("msg", "获取粉丝失败，请重试！！！");
    		Page page = pageForExtForm(form);
            page.setContentType("html");
            return page;
    	}
    	Long groupId = follower.getFollowerGroup().getId();
    	Long accountId = follower.getAccount().getId();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.account.id", accountId, "=");
    	qo.addQuery("obj.id <> " + groupId);
    	IPageList ipList = service.getFollowerGroupBy(qo);
    	AjaxUtil.convertEntityToJson(ipList);
	    form.jsonResult(ipList);
	    return Page.JSONPage;
    }
    
    /**
     * 功能：向某个分组添加粉丝(支持多选)
     * 描述： 添加粉丝，选择完成后，点击保存时调用该方法
     * @param form
     * @return
     */
    public Page doAddFollower(WebForm form){
    	String groupId = CommUtil.null2String(form.get("groupId"));
    	if(StringUtils.isEmpty(groupId)){
    		this.addError("msg", "请选择分组！！！");
    		Page page = pageForExtForm(form);
            page.setContentType("html");
            return page;
    	}
    	FollowerGroup followerGroup = service.getFollowerGroup(Long.parseLong(groupId));
    	String allIds = CommUtil.null2String(form.get("ids"));
        String[] ids = allIds.split(",");
        if(ids.length == 0){
        	this.addError("msg", "未选择用户，添加失败！！！");
        	Page page = pageForExtForm(form);
            page.setContentType("html");
            return page;
        }
        if(ids.length == 1){
        	Follower follower = this.followerService.getFollower(Long.parseLong(ids[0]));
//        	if(follower.getAccount().getId() != followerGroup.getAccount().getId()){
//        		this.addError("msg", "您所选粉丝不属于该公共号");
//        		Page page = pageForExtForm(form);
//                page.setContentType("html");
//                return page;
//        	}
        	String message = AddFollowerSingle(follower.getWeixinOpenId(), followerGroup.getWxGroupId(), follower.getAccount());
        	if("false".equals(message)){
        		this.addError("msg", "微信端操作失败，添加失败！！！");
        	}else{
        		follower.setFollowerGroup(followerGroup);
        		followerGroup.setCount(followerGroup.getCount() + 1);
                followerService.updateFollower(follower.getId(), follower);
                service.updateFollowerGroup(followerGroup.getId(), followerGroup);
        	}
        }else{
        	List<String> openIdList = new ArrayList<String>();
            for (String id : ids) {
                Follower follower = this.followerService.getFollower(Long.parseLong(id));
                openIdList.add(follower.getWeixinOpenId());
                follower.setFollowerGroup(followerGroup);
                followerService.updateFollower(follower.getId(), follower);
                followerGroup.setCount(followerGroup.getCount() + 1);
                service.updateFollowerGroup(followerGroup.getId(), followerGroup);
            }
            AddFollowerBatch(openIdList,followerGroup.getWxGroupId(),followerGroup.getAccount());
        }
    	Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 添加单个粉丝/移动用户分组
     * @param openId
     * @param targetGroup
     * @param account
     * @return
     */
    public String AddFollowerSingle(String openId,Integer targetGroup,Account account){
    	Map<String , Object> parmMap = new HashMap<String , Object>();
    	parmMap.put("openid", openId);
    	parmMap.put("to_groupid", targetGroup);
    	JSONObject jo = new JSONObject(parmMap);
        String jsonPram = jo.toJSONString();
        //生成所需POST参数 结束
        //调用API 返回数据并解析  开始
        String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        if("40001".equals(jSONObject.getShort("errcode"))){
        	 sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	 requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
             jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        }
        String responseJson = (String) jSONObject.get("errmsg");
		if(responseJson == null || "ok".equals(responseJson)){
			return "success";
		}
    	return "false";
    }
    
    /**
     * 批量移动用户分组
     * @param openIdList
     * @param targetGroup
     * @param account
     * @return
     */
    public String AddFollowerBatch(List openIdList,Integer targetGroup,Account account){
    	Map<String , Object> parmMap = new HashMap<String , Object>();
    	parmMap.put("openid_list", openIdList);
    	parmMap.put("to_groupid", targetGroup);
    	JSONObject jo = new JSONObject(parmMap);
        String jsonPram = jo.toJSONString();
        //生成所需POST参数 结束
        //调用API 返回数据并解析  开始
        String url = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        if("40001".equals(jSONObject.getShort("errcode"))){
        	 sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	 requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
             jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonPram);
        }
        String responseJson = (String) jSONObject.get("errmsg");
		if(responseJson == null || "ok".equals(responseJson)){
			return "success";
		}
    	return "false";
    }
    
    /**
     * 功能：移动粉丝到其他分组
     * 描述：移动一个粉丝到另一个组别时调用此方法
     * @param form
     * @return
     */
    public Page doChangeFollowerGroup(WebForm form){
    	String id= CommUtil.null2String(form.get("abcid"));
    	if(StringUtils.isEmpty(id)){
    		this.addError("msg", "粉丝获取失败，操作失败");
    		Page page = pageForExtForm(form);
            page.setContentType("html");
            return page;
    	}
    	Long followerId = Long.valueOf(Long.parseLong(id));
    	Long groupId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("groupId"))));
    	Follower follower = followerService.getFollower(followerId);
    	//获取当前粉丝所在组
    	FollowerGroup oldFollowerGroup = follower.getFollowerGroup();
    	//获取当前新的组别
    	FollowerGroup followerGroup = service.getFollowerGroup(groupId);
    	String message =  AddFollowerSingle(follower.getWeixinOpenId(),followerGroup.getWxGroupId(),followerGroup.getAccount());
    	if("success".equals(message)){
    		follower.setFollowerGroup(followerGroup);
    		followerService.updateFollower(followerId, follower);
    		if(oldFollowerGroup != null){
    	   		if(oldFollowerGroup.getCount() == 0){
    	   			oldFollowerGroup.setCount(0);
        		}else{
        			oldFollowerGroup.setCount(oldFollowerGroup.getCount() - 1);
        		}
    			service.updateFollowerGroup(oldFollowerGroup.getId(), oldFollowerGroup);
    		}
    		followerGroup.setCount(followerGroup.getCount() + 1);
    		service.updateFollowerGroup(followerGroup.getId(), followerGroup);
    	}else{
    		this.addError("msg", "微信端移动失败，操作失败");
    	}
    	Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 基本组别操作，即粉丝移至“未分组”，“黑名单”，“星标组”
     * 0：未分组；1：黑名单；2：星标组
     * @param form
     * @return
     */
    public Page doBasicGroupOperation(WebForm form){
    	String type = CommUtil.null2String(form.get("type"));
    	Long followerId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
    	//Long groupId = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("groupId"))));//选择的目标组Id
    	Follower follower = followerService.getFollower(followerId);
    	FollowerGroup followerGroup = follower.getFollowerGroup();
    	String message =  AddFollowerSingle(follower.getWeixinOpenId(),Integer.valueOf(type),follower.getAccount());
    	if("success".equals(message)){
    		if(followerGroup.getCount() == 0){
    			followerGroup.setCount(0);
    		}else{
    			followerGroup.setCount(followerGroup.getCount() - 1);
    		}
    		service.updateFollowerGroup(followerGroup.getId(), followerGroup);
    		follower.setFollowerGroup(null);
    		followerService.updateFollower(followerId, follower);
    	}else{
    		this.addError("msg", "微信端移动失败，操作失败");
    	}
    	Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 获取分组
     * 
     * @param form
     * @return
     */
    public Page doGetTree(WebForm form) {
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        List<?> list = this.service.getFollowerGroupBy(qo).getResult();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
            	FollowerGroup followerGroup = (FollowerGroup) list.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", followerGroup.getId() + "");
                map.put("text", followerGroup.getName());
                map.put("leaf", true);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "0");
            map.put("text", "没有分组");
            map.put("leaf", true);
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }
    
    public void setService(IFollowerGroupService service) {
        this.service = service;
    }

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setFollowerService(IFollowerService followerService) {
		this.followerService = followerService;
	}
    
	
}