package com.eastinno.otransos.seafood.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakeRegularService;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakeSystemConfigService;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakesRecordService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

public class PcShopMemberAction extends AbstractPageCmdAction{
	
	@Inject
    private ISweepstakeRegularService sweepstakeRegularService;
	@Inject
    private ISweepstakeSystemConfigService sweepstakeSystemConfigService;
	@Inject
    private ISweepstakesRecordService sweepstakesRecordService;
	
	@Inject
    private IShopMemberService shopMemberService;
	@Inject
    private IShopOrderInfoService shopOrderInfoService;
	@Inject
    private IShopAddressService shopAddressService;
	@Inject
	private ISystemRegionService systemRegionService;
	@Inject 
	private IIntegralHistoryService integralHistoryService;
	
	/**
	 * 活动--跳转--转盘
	 * @param form
	 * @return
	 */
	public Page doToSweepStake(WebForm form){
		SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);
    	}
    	form.addResult("obj", sc);
    	if(sc.getStatus()==0){
    		form.addResult("flag","当前未进行抽奖活动，敬请期待");
    		return new Page("/bcd/wxshop/promotions/sweepstakes/noStart.html");
    	}
    	QueryObject qo2 = new QueryObject();
    	qo2.addQuery("obj.status",Short.parseShort("0"), "!=");
    	qo2.setPageSize(10);
    	qo2.setOrderBy("createTime");
    	qo2.setOrderType("desc");
    	List<SweepstakesRecord> list2 = this.sweepstakesRecordService.getSweepstakesRecordBy(qo2).getResult();
    	form.addResult("winner", list2);
		return new Page("/bcd/wxshop/promotions/sweepstakes/pcSweepstake.html");
	}
	
	/**
	 * 活动--积分--兑换
	 * @param form
	 * @return
	 */
	public Page doToIntegralExchange(WebForm form){
		return new Page("/pcbcd/activity/integralExchange.html");
	}
	
	/**
     * 抽奖方法
     * 
     * @param form
     */
    public Page doSweepstake(WebForm form) {
    	Map map = new HashMap<>();
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		map.put("flag",1);
    		form.jsonResult(map);
    		return Page.JSONPage;
    	}
    	member = this.shopMemberService.getShopMember(member.getId());
    	Long aviala = member.getAvailableIntegral();
    	
    	/**
    	 *查询抽奖全局配置 
    	 *
    	 */
    	int minbase=0;
    	SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);
    		minbase = sc.getMinbase();
    	}
    	int cost = sc.getCostInterval();
    	int limit = sc.getCount();
    	if(aviala < cost){
    		map.put("flag",2);
    		form.jsonResult(map);
    		return Page.JSONPage;
    	}else{
    		/**
        	 * 检查抽奖次数
        	 */
        	Integer count = this.sweepstakesRecordService.getTodayCount(member);
        	if(count >= limit){
        		SweepstakesRecord sweepstakesRecord = this.sweepstakesRecordService.getMenberTodayLastSR(member);
        		if(!sweepstakesRecord.getIntegal().equals(sc.getCostInterval())){
        			map.put("flag",3);
            		form.jsonResult(map);
            		return Page.JSONPage;
        		}
        		map = this.sweepstake(minbase, member, cost, map);
    		    form.jsonResult(map);
        	}else{
        		map = this.sweepstake(minbase, member, cost, map);
    		    form.jsonResult(map);
    		}
    	}
		return Page.JSONPage;
    }
    
    public Map sweepstake(int minbase,ShopMember member,int cost,Map map){
    	QueryObject qo = new QueryObject();
    	int position = this.sweepstakeRegularService.checkSweepstake(minbase);
    	SweepstakeRegular regular = null;
    	QueryObject qo2 = new QueryObject();
    	qo.addQuery("obj.position",position,"=");
    	List<SweepstakeRegular> list2 = this.sweepstakeRegularService.getSweepstakeRegularBy(qo).getResult();
    	if(list2 != null && list2.size() != 0 ){
    		regular = list2.get(0);
	    	SweepstakesRecord sr = new SweepstakesRecord();
	    	sr.setStatus(regular.getStatus());
	    	sr.setUser(member);
	    	sr.setCost(cost);
	    	sr.setImgPaths(regular.getImgPaths());
	    	if(regular.getStatus() == 1){
	    		sr.setProductname(regular.getProductname());
	    	}else if(regular.getStatus() == 3){
	    		sr.setIntegal(regular.getIntegal());
	    	}
	    	if(regular.getStatus() == 3){
	    		sr.setIsDispatch(Short.parseShort("1"));
	    		member.setAvailableIntegral((long)(member.getAvailableIntegral()-cost + regular.getIntegal()));//抽一次
	    		member.setTotalIntegral((long)(member.getTotalIntegral() + regular.getIntegal()));
	    	}else{
	    		member.setAvailableIntegral((long)(member.getAvailableIntegral()-cost));//抽一次
	    	}
	    	this.shopMemberService.updateShopMember(member.getId(),member);
	    	this.sweepstakesRecordService.addSweepstakesRecord(sr);
	    	//需生成积分使用记录
	    	IntegralHistory ih = new IntegralHistory();
	    	ih.setType(5);
	    	ih.setDescription("抽奖消费");
	    	ih.setIntegral((long)-cost);
	    	ih.setUser(member);
	    	this.integralHistoryService.addIntegralHistory(ih);	
	    	if(regular.getStatus() == 3){
	    		IntegralHistory ih2 = new IntegralHistory();
		    	ih2.setType(7);
		    	ih2.setDescription("抽奖获得");
		    	ih2.setIntegral((long)regular.getIntegal());
		    	ih2.setUser(member);
		    	this.integralHistoryService.addIntegralHistory(ih2);	
	    	}
	    	int angle = this.sweepstakeRegularService.returnangle(position);
	    	map.put("position", position);
	    	map.put("angle", angle);
	    	map.put("name", regular.getName());
	    	map.put("productname", regular.getProductname());
	    	map.put("type", regular.getStatus());
	    	map.put("integal", regular.getIntegal());
    	}
    	return map;
    }
    
    /**
     * 客户端抽奖记录列表页面
     * 
     * @param form
     */
    public Page doCustomList(WebForm form) {
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.user.id",member.getId(), "=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        qo.setPageSize(-1);
        IPageList pl = this.sweepstakesRecordService.getSweepstakesRecordBy(qo);
        form.addResult("pl", pl);
        return new Page("/bcd/wxshop/promotions/sweepstakes/sweepstakeRecord.html");
    }
    
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public void setSweepstakeRegularService(
			ISweepstakeRegularService sweepstakeRegularService) {
		this.sweepstakeRegularService = sweepstakeRegularService;
	}

	public void setSweepstakeSystemConfigService(
			ISweepstakeSystemConfigService sweepstakeSystemConfigService) {
		this.sweepstakeSystemConfigService = sweepstakeSystemConfigService;
	}

	public void setSweepstakesRecordService(
			ISweepstakesRecordService sweepstakesRecordService) {
		this.sweepstakesRecordService = sweepstakesRecordService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public IShopAddressService getShopAddressService() {
		return shopAddressService;
	}

	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}

	public ISweepstakeRegularService getSweepstakeRegularService() {
		return sweepstakeRegularService;
	}

	public ISweepstakeSystemConfigService getSweepstakeSystemConfigService() {
		return sweepstakeSystemConfigService;
	}

	public ISweepstakesRecordService getSweepstakesRecordService() {
		return sweepstakesRecordService;
	}

	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
	}

	public IIntegralHistoryService getIntegralHistoryService() {
		return integralHistoryService;
	}

	public void setIntegralHistoryService(
			IIntegralHistoryService integralHistoryService) {
		this.integralHistoryService = integralHistoryService;
	}
	
}
