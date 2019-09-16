package com.eastinno.otransos.shop.promotions.action;

import java.util.Date;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.web.tools.PageList;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRegular;
import com.eastinno.otransos.shop.promotions.query.IntegralBuyRegularQuery;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRegularService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;

/**
 * IntegralBuyRegularAction
 * @author 
 */
@Action
public class IntegralBuyRegularAction extends AbstractPageCmdAction {
    @Inject
    private IIntegralBuyRegularService service;
    @Inject
    private IShopProductService productService;
    @Inject
    private ILinkImgGroupService imgGroupService;
    @Inject 
    private ILinkImgTypeService imgTypeService;
    @Inject
    private ICusUploadFileService cusUploadFileService;

	/**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        IntegralBuyRegularQuery qo = (IntegralBuyRegularQuery) form.toPo(IntegralBuyRegularQuery.class);
        qo.setOrderBy("createDate");
        qo.setOrderType("DESC");
        IPageList pageList = this.service.getIntegralBuyRegularBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
        return new Page("/bcd/promotions/integral/integralRegularList.html");
    }
    
    /**
     * 保存积分活动页
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/bcd/promotions/integral/integralRegularEdit.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	IntegralBuyRegular entry = (IntegralBuyRegular)form.toPo(IntegralBuyRegular.class);
        form.toPo(entry);
    	
    	ShopProduct pro = this.getProById(form); 
		if(pro == null){
			form.addResult("entry", entry);
			return new Page("/bcd/promotions/integral/integralRegularEdit.html");	
    	}
		//更新商品正常售卖信息，将商品下架    	
    	this.productService.updateShopProduct(pro.getId(), pro);
        entry.setPro(pro);
        
        if (!hasErrors()) {
            entry = this.service.addIntegralBuyByAdmin(entry);
            if (entry != null) {
            	//更新商品是否可积分购买信息
            	entry.getPro().setStatus((short)2);
            	entry.getPro().setIsPointBuy(true);
            	entry.getPro().setAmt(1.0*entry.getIntegralPrice());
            	entry.getPro().setTydAmt(1.0*entry.getIntegralPrice());
            	entry.getPro().setStore_price(1.0*entry.getIntegralPrice());
            	this.productService.updateShopProduct(entry.getPro().getId(), entry.getPro());
                form.addResult("msg", "添加成功");
            }else{
            	form.addResult("msg", "积分商品添加失败");
            }
        }
        form.addResult("entry", entry);
        return new Page("/bcd/promotions/integral/integralRegularEdit.html");
    }
    
    /**
     * 积分活动更新页
     * @param form
     * @return
     */
    public Page doToUpdate(WebForm form){
    	Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        IntegralBuyRegular entry = this.service.getIntegralBuyRegular(id);
        form.addResult("entry", entry);
        return new Page("/bcd/promotions/integral/integralRegularEdit.html");
    }
    
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	ShopProduct pro = this.getProById(form); 
		if(pro == null){
			return new Page("/bcd/promotions/integral/integralRegularEdit.html");	
    	}
    	
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        IntegralBuyRegular entry = this.service.getIntegralBuyRegular(id);
        form.toPo(entry);
        entry.setPro(pro);
        if (!hasErrors()) {
            boolean ret = this.service.updateIntegralBuyByAdmin(entry);
            if(ret){
            	entry.getPro().setAmt(1.0*entry.getIntegralPrice());
            	entry.getPro().setTydAmt(1.0*entry.getIntegralPrice());
            	entry.getPro().setStore_price(1.0*entry.getIntegralPrice());
                form.addResult("msg", "修改成功");
            }else{
            	form.addResult("msg", "修改失败");
            }
        }
        return new Page("/bcd/promotions/integral/integralRegularEdit.html");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        if(this.service.delIntegralBuyRegular(id)){
        	form.addResult("msg", "积分商品删除成功！");
        }
        return this.go("list");
    }
    
    /**
     * 反转积分活动的推荐设置
     * @param form
     * @return
     */
    public Page doToggleRecmmend(WebForm form){
    	String idStr = CommUtil.null2String(form.get("id"));
    	if(idStr.equals("")){
    		form.addResult("result", "failure");
        	form.addResult("msg", "没有活动id信息，更改失败！");
        	return new Page("/bcd/promotions/integral/ajax.html");
    	}    		
    	Long id = Long.parseLong(idStr);
    	IntegralBuyRegular regular = this.service.getIntegralBuyRegular(id);
    	if(regular == null){
    		form.addResult("result", "failure");
        	form.addResult("msg", "没有id对应的活动，更改失败！");
        	return new Page("/bcd/promotions/integral/ajax.html");
    	}
    	if(regular.getIsRecmmend()==null){
    		regular.setIsRecmmend(true);	
    	}else{
    		regular.setIsRecmmend(!regular.getIsRecmmend());	
    	}    	
    	this.service.updateIntegralBuyRegular(regular.getId(), regular);
    	form.addResult("result", "success");
    	form.addResult("msg", "更改成功！");
    	return new Page("/bcd/promotions/integral/ajax.html");
    }
    
    /**
     * 更换两个活动对应的权重weight
     * @param form
     * @return
     */
    public Page doExchangeRegularWeight(WebForm form){
    	String regularFirstId = CommUtil.null2String(form.get("regularFirstId"));
    	String regularSecondId = CommUtil.null2String(form.get("regularSecondId"));
    	if(regularFirstId.equals("") || regularSecondId.equals("")){
    		form.addResult("result", "failure");
    		form.addResult("msg", "缺少活动id，更改位置失败！");
    		return new Page("/bcd/promotions/integral/ajax.html");
    	}
    	this.exchangeRegularWeight(Long.parseLong(regularFirstId), Long.parseLong(regularSecondId));
    	form.addResult("result", "success");
    	form.addResult("msg", "更改成功！");
    	return new Page("/bcd/promotions/integral/ajax.html");
    }
    
    /**
     * 积分商城-首页轮播列表
     * @param form
     * @return
     */
    public Page doCircleImgList(WebForm form){
    	QueryObject qo = new QueryObject();
    	LinkImgType imgType = this.imgTypeService.getLinkImgTypeByCode("integralCircle");
    	if(imgType == null){
    		return new Page("/bcd/promotions/integral/integralCircleImgList.html");	
    	}
    	qo.addQuery("obj.type.id", imgType.getId(), "=");    	
    	IPageList pageList = this.imgGroupService.getLinkImgGroupBy(qo);    	
    	CommUtil.saveIPageList2WebForm(pageList, form);
    	form.addResult("pl", pageList);
    	return new Page("/bcd/promotions/integral/integralCircleImgList.html");
    }
    
    /**
     * 积分商城-首页轮播设置
     * @param form
     * @return
     */
    public Page doToCircleImgEdit(WebForm form){
    	String idStr = CommUtil.null2String(form.get("id"));
    	if(idStr.equals("")){
    		form.addResult("msg", "未获取到轮播图id，不能进行修改操作！");
    		return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    	}
    	Long id = new Long(idStr);
    	LinkImgGroup imgGroup = this.imgGroupService.getLinkImgGroup(id);
    	if(imgGroup == null){
    		form.addResult("msg", "该id对应的轮播图不存在，不能进行修改操作！");
    		return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    	}
    	form.addResult("entry", imgGroup);
    	return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    }
    
    /**
     * 积分商城-首页轮播设置
     * @param form
     * @return
     */
    public Page doToCircleImgSave(WebForm form){
    	return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    }
    
    /**
     * 积分商城-轮播图编辑页
     * @param form
     * @return
     */
    public Page doCircleImgEdit(WebForm form){
    	//创建轮播图group
    	Long id = Long.valueOf(CommUtil.null2Int(form.get("id")));
    	LinkImgGroup imgGroup = this.imgGroupService.getLinkImgGroup(id);
    	form.toPo(imgGroup);
    	String lu = CommUtil.null2String(form.get("linkUrl"));
    	if(!StringUtils.hasText(lu)){
    		String linkUrl = "/pcIntegralShop.java?cmd=detail&id=";
        	IntegralBuyRegular integralRegular = this.getIntegralRegularByCode(form);
        	linkUrl = linkUrl+integralRegular.getId();
        	imgGroup.setLinkUrl(linkUrl);
    	}
    	
    	imgGroup.setType(this.imgTypeService.getLinkImgTypeByCode("integralCircle"));
    	String imgPath = CommUtil.null2String(form.get("imgPath"));
    	if(!imgPath.equals("")){
    		imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                    + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
            if(!"".equals(imgPath)){
            	this.cusUploadFileService.addCusUploadFile(imgPath);
            	imgGroup.setImgPath(imgPath);
            }
    	}
        
        if (!hasErrors()) {
            if(!this.imgGroupService.updateLinkImgGroup(imgGroup.getId(), imgGroup)){
                form.addResult("msg", "修改成功");
            }
        }
    	return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    }
    
    /**
     * 创建pc端积分首页轮播图存储记录
     */
    public Page doCircleImgSave(WebForm form){    	
    	//创建轮播图group
    	LinkImgGroup imgGroup = form.toPo(LinkImgGroup.class);
    	String linkUrl = "/pcIntegralShop.java?cmd=detail&id=";
    	IntegralBuyRegular integralRegular = this.getIntegralRegularByCode(form);
    	if(integralRegular == null){    		
    		return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    	}
    	linkUrl = linkUrl+integralRegular.getId();
    	imgGroup.setLinkUrl(linkUrl);
    	imgGroup.setType(this.getIntegralLinkImgType());
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(imgPath)){
        	this.cusUploadFileService.addCusUploadFile(imgPath);
        	imgGroup.setImgPath(imgPath);
        }
        if (!hasErrors()) {
            Long id = this.imgGroupService.addLinkImgGroup(imgGroup);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }        
        return new Page("/bcd/promotions/integral/integralCircleImgEdit.html");
    }
    
    /**
     * 推荐积分商品-排序
     * @param form
     * @return
     */
    public Page doToSquenceRecommendRegular(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.isRecmmend", true, "=");
    	qo.setOrderBy("weight");
    	qo.setOrderType("desc");
    	qo.setPageSize(-1);
    	IPageList pageList = this.service.getIntegralBuyRegularBy(qo);
    	form.addResult("pl", pageList);
    	return new Page("/bcd/promotions/integral/integralRegularSequence.html");
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
    
    /**
     * 依据form中存储的商品id获取商品对象
     * @param form
     * @return
     */
    private IntegralBuyRegular getIntegralRegularByCode(WebForm form){
    	//查看商品code是否存在    	
    	String code = CommUtil.null2String(form.get("regularId"));
    	if(code.equals("")){
    		form.addResult("msg", "添加失败，没有指定活动！");
    		return null;    		
    	}
    	Long regularId = Long.parseLong(code);
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.id", regularId, "=");
    	IPageList pageList = this.service.getIntegralBuyRegularBy(qo);
    	if(pageList.getResult()!=null && pageList.getResult().size()!=1){
    		form.addResult("msg", "添加失败，没有找到id="+code+" 的商品！");
    		return null;
    	}
    	return (IntegralBuyRegular)(pageList.getResult().get(0));
    }
    
    private LinkImgType getIntegralLinkImgType(){
    	//创建轮播图类型
    	LinkImgType imgType = this.imgTypeService.getLinkImgTypeByCode("integralCircle");
    	if(imgType == null){
	    	imgType = new LinkImgType();
	    	imgType.setCode("integralCircle");
	    	imgType.setTitle("积分首页轮播图");
	    	imgType.setText(new Date().toString());
	    	Long imgTypeId = this.imgTypeService.addPPtType(imgType);
	    	imgType = this.imgTypeService.getPPtType(imgTypeId);
    	}
    	return imgType;
    }
    
    /**
     * 交换活动权重
     * @return
     */
    private boolean exchangeRegularWeight(Long firstId, Long secondId){
    	IntegralBuyRegular firstRegular = this.service.getIntegralBuyRegular(firstId);
    	IntegralBuyRegular secondRegular = this.service.getIntegralBuyRegular(secondId);
    	Long tempFirstWeight = firstRegular.getWeight();
    	Long tempSecondWeight = secondRegular.getWeight();
    	firstRegular.setWeight(secondRegular.getWeight());
    	secondRegular.setWeight(tempFirstWeight);
    	boolean firstResult = this.service.updateIntegralBuyRegular(firstRegular.getId(), firstRegular);
    	boolean secondResult = this.service.updateIntegralBuyRegular(secondRegular.getId(), secondRegular);
    	if(firstResult && secondResult){
    		return true;
    	}else{
    		return false;
    	}    	
    }
    
    public void setService(IIntegralBuyRegularService service) {
        this.service = service;
    }
    public IShopProductService getProductService() {
		return productService;
	}
	public void setProductService(IShopProductService productService) {
		this.productService = productService;
	}
	public IIntegralBuyRegularService getService() {
		return service;
	}
	public ILinkImgGroupService getImgGroupService() {
		return imgGroupService;
	}

	public void setImgGroupService(ILinkImgGroupService imgGroupService) {
		this.imgGroupService = imgGroupService;
	}

	public ILinkImgTypeService getImgTypeService() {
		return imgTypeService;
	}

	public void setImgTypeService(ILinkImgTypeService imgTypeService) {
		this.imgTypeService = imgTypeService;
	}
    
	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}

	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
}