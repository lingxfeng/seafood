package com.eastinno.otransos.seafood.promotions.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.seafood.promotions.query.RushBuyRegularQuery;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.formatUtil;

/**
 * RushBuyRegularAction
 * @author 
 */
@Action
public class RushBuyRegularAction extends AbstractPageCmdAction {
	@Inject
    private IRushBuyRecordService recordService;
	@Inject
    private IRushBuyRegularService regularService;
	@Inject
	private IShopProductService productService;
	@Inject
	private IShopOrderInfoService orderService;
	
	public IShopOrderInfoService getOrderService() {
		return orderService;
	}

	public void setOrderService(IShopOrderInfoService orderService) {
		this.orderService = orderService;
	}

	public IShopProductService getProductService() {
		return productService;
	}

	public void setProductService(IShopProductService productService) {
		this.productService = productService;
	}

	public IRushBuyRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}

	public IRushBuyRegularService getRegularService() {
		return regularService;
	}

	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}	
    
    /**
     * 秒杀活动列表页面
     * 
     * @param form
     */
    public Page doSecKillList(WebForm form) {
    	RushBuyRegularQuery qo = (RushBuyRegularQuery) form.toPo(RushBuyRegularQuery.class);
        qo.addQuery("obj.activityType", 0, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("DESC");
        IPageList pageList = this.regularService.getAllSecKillRegularByQO(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/promotions/seckill/seckillList.html");
    }
      
    /**
     * 
     * @param form
     */
    public Page doToSecKillSave(WebForm form) { 
       return new Page("/bcd/promotions/seckill/seckillEdit.html");
    }
    
    /**
     * 保存秒杀活动数据
     * 
     * @param form
     */
    public Page doSecKillSave(WebForm form) {    	
    	ShopProduct pro = this.getProById(form);
    	RushBuyRegular entry = (RushBuyRegular)form.toPo(RushBuyRegular.class);
    	if(pro == null){
    		form.addResult("entry", entry);
    		return new Page("/bcd/promotions/seckill/seckillEdit.html");	
    	}
    	//更新商品正常售卖信息，将商品下架，更新实体店、微店、零售价为活动价格
    	pro.setStatus((short)2);
    	pro.setAmt(entry.getActivityPrice());
    	pro.setTydAmt(entry.getActivityPrice());
    	pro.setStore_price(entry.getActivityPrice());
    	pro.setInventory(entry.getBuyNum());
    	pro.setSaleNum(0);
    	this.productService.updateShopProduct(pro.getId(), pro);
    	
        form.toPo(entry);
        entry.setPro(pro);
        if (!hasErrors()) {
            entry = this.regularService.createSecKillRegular(entry);
            if (entry != null) {
                form.addResult("msg", "添加成功");
            }
        }
        form.addResult("entry", entry);
        return new Page("/bcd/promotions/seckill/seckillEdit.html");
    }
    
    /**
     * 
     * @param form
     */
    public Page doToSecKillUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRegular entry = this.regularService.getRushBuyRegular(id);
        form.addResult("entry", entry);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/promotions/seckill/seckillEdit.html");
    }
    
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doSecKillUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRegular entry = this.regularService.getRushBuyRegular(id);
        
        ShopProduct pro = getProById(form);        
        if(pro == null){
            //修改后展示修改信息
            form.addResult("entry", entry);
        	return new Page("/bcd/promotions/seckill/seckillEdit.html");
        }
        
        if (!hasErrors()) {
        	form.toPo(entry);
            entry.setPro(pro);
            entry.setBuyNum(pro.getInventory());
            entry = this.regularService.updateSecKillRegular(entry);            
            if(entry != null){
            	ShopProduct product = entry.getPro();
                //product.setInventory(entry.getBuyNum());
                product.setAmt(entry.getActivityPrice());
                product.setTydAmt(entry.getActivityPrice());
                product.setStore_price(entry.getActivityPrice());
                this.productService.updateShopProduct(product.getId(), product);
                form.addResult("msg", "修改成功");
            }
        }
        //修改后展示修改信息
        form.addResult("entry", entry);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/promotions/seckill/seckillEdit.html");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doSecKillRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        boolean result = this.regularService.delRushBuyRegular(id);
        if(result){
        	form.addResult("result", "success");
            form.addResult("msg", "删除成功");
        }else{
        	form.addResult("result", "failure");
            form.addResult("msg", "删除失败");
        }        
        return new Page("/bcd/promotions/seckill/seckillList.html");
    }

    /**
     * 秒杀活动列表页面
     * 
     * @param form
     */
    public Page doTimeLimitList(WebForm form) {
    	RushBuyRegularQuery qo = (RushBuyRegularQuery) form.toPo(RushBuyRegularQuery.class);
        qo.addQuery("obj.activityType", 1, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("DESC");
        IPageList pageList = this.regularService.getAllTimeLimitRegularByQO(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/promotions/timelimit/timelimitList.html");
    }
      
    /**
     * 
     * @param form
     */
    public Page doToTimeLimitSave(WebForm form) { 
       return new Page("/bcd/promotions/timelimit/timelimitEdit.html");
    }
    
    /**
     * 保存限时抢购活动数据
     * 
     * @param form
     */
    public Page doTimeLimitSave(WebForm form) {    	
    	ShopProduct pro = this.getProById(form);
    	RushBuyRegular entry = (RushBuyRegular)form.toPo(RushBuyRegular.class);        
        form.toPo(entry);
    	if(pro == null){
    		return new Page("/bcd/promotions/timelimit/timelimitEdit.html");	
    	}
    	//更新商品正常售卖信息，将商品下架
    	pro.setStatus((short)2);
    	pro.setAmt(entry.getActivityPrice());
    	pro.setTydAmt(entry.getActivityPrice());
    	pro.setStore_price(entry.getActivityPrice());
    	pro.setInventory(entry.getBuyNum());
    	pro.setSaleNum(0);
    	this.productService.updateShopProduct(pro.getId(), pro);    	
        
        entry.setPro(pro);
        if (!hasErrors()) {
            entry = this.regularService.createTimeLimitRegular(entry);
            if (entry != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return new Page("/bcd/promotions/timelimit/timelimitEdit.html");
    }
    
    /**
     * 
     * @param form
     */
    public Page doToTimeLimitUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRegular entry = this.regularService.getRushBuyRegular(id);
        form.addResult("entry", entry);            
        return new Page("/bcd/promotions/timelimit/timelimitEdit.html");
    }
    
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doTimeLimitUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        RushBuyRegular entry = this.regularService.getRushBuyRegular(id);
        
        ShopProduct pro = getProById(form);        
        if(pro == null){
            //修改后展示修改信息
            form.addResult("entry", entry);
        	return new Page("/bcd/promotions/timelimit/timelimitEdit.html");
        }
        
        if (!hasErrors()) {
        	form.toPo(entry);
            entry.setPro(pro);
            entry.setBuyNum(pro.getInventory());
            entry = this.regularService.updateTimeLimitRegular(entry);            
            if(entry != null){
            	ShopProduct product = entry.getPro();
                //product.setInventory(entry.getBuyNum());
                product.setAmt(entry.getActivityPrice());
                product.setTydAmt(entry.getActivityPrice());
                product.setStore_price(entry.getActivityPrice());
                this.productService.updateShopProduct(product.getId(), product);
                form.addResult("msg", "修改成功");
            }
        }
        //修改后展示修改信息
        form.addResult("entry", entry);
        return new Page("/bcd/promotions/timelimit/timelimitEdit.html");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doTimeLimitRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        boolean result = this.regularService.delRushBuyRegular(id);
        if(result){
        	form.addResult("result", "success");
            form.addResult("msg", "删除成功");
        }else{
        	form.addResult("result", "failure");
            form.addResult("msg", "删除失败");
        }        
        return new Page("/bcd/promotions/timelimit/timelimitList.html");
    }
    
    /**
     * 将失效的订单设置为取消状态
     * @param form
     * @return
     */
    public Page doClearOutExpireOrder(WebForm form){
    	String type = CommUtil.null2String(form.get("type"));
    	String idStr = CommUtil.null2String(form.get("regularId"));
    	
    	if(StringUtils.isEmpty(idStr)){
    		form.addResult("msg", "未传入活动id，清理失败！");
    		if(type.equals("timeLimit")){
        		return new Page("/bcd/promotions/timelimit/timelimitList.html");
        	}else if(type.equals("secKill")){
        		return new Page("/bcd/promotions/seckill/seckillList.html");	
        	}	
    		return new Page("/bcd/promotions/timelimit/timelimitList.html");
    	}
    	Long id = new Long(idStr);    	
    	RushBuyRegular regular = this.regularService.getRushBuyRegular(id);
    	
    	//获取该活动产生的所有抢购记录
    	List<RushBuyRecord> records = new ArrayList();
    	if(type.equals("timeLimit")){
    		records = this.recordService.getAllTimeLimitRecordByRegular(regular).getResult();
    	}else if(type.equals("secKill")){
    		records = this.recordService.getAllSecKillRecordByRegular(regular).getResult();	
    	}	
    	
    	//筛选出下单时间超过活动expire设置未支付的时间抢购记录
    	List<RushBuyRecord> outRecords = new ArrayList();
    	for(int i=0; i<records.size(); ++i){
    		RushBuyRecord tempRecord = records.get(i);
    		if( (tempRecord.getOrder()==null || tempRecord.getOrder().getStatus()==0) 
    				&& tempRecord.isOutExpire() ){
    			outRecords.add(tempRecord);
    		}
    	}
    	
    	//依据抢购记录将对应的订单状态设置为取消状态
    	for(int i=0; i<outRecords.size(); ++i){
    		RushBuyRecord tempRecord = outRecords.get(i);
    		if(tempRecord.getOrder() != null){
    			tempRecord.getOrder().setStatus(-1);
    			this.orderService.updateShopOrderInfo(tempRecord.getOrder().getId(), tempRecord.getOrder());
    		}
    	}
    	
		form.addResult("msg", "已将超时抢购记录对应的订单设置为取消订单！");
    	if(type.equals("timeLimit")){
    		return new Page("/bcd/promotions/timelimit/timelimitList.html");
    	}else if(type.equals("secKill")){
    		return new Page("/bcd/promotions/seckill/seckillList.html");
    	}
    	return new Page("/bcd/promotions/timelimit/timelimitList.html");
    }
    
    /**
     * 依据form中存储的商品id获取商品对象
     * @param form
     * @return
     */
    private ShopProduct getProById(WebForm form){    	    	
    	//查看商品ID是否存在
    	Long proId = null;
    	String proStr = CommUtil.null2String(form.get("proId"));
    	if(proStr.equals("")){
    		form.addResult("msg", "添加失败，没有商品信息！");
    		return null;    		
    	}else{
    		proId = Long.parseLong(proStr);
    	}
    	ShopProduct pro = this.productService.getShopProduct(proId);
    	if(pro == null){
    		form.addResult("msg", "添加失败，没有找到id="+proStr+" 的商品！");
    		return null;    		
    	}
    	return pro;
    }
}