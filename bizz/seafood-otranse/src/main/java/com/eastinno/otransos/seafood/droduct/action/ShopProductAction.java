package com.eastinno.otransos.seafood.droduct.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.domain.AttributeValue;
import com.eastinno.otransos.seafood.droduct.domain.DeliveryRule;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.query.ShopProductQuery;
import com.eastinno.otransos.seafood.droduct.service.IAttributeValueService;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IRegionClassService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.ShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 商品
 * @author nsz
 */
@Action
public class ShopProductAction extends AbstractPageCmdAction {
    @Inject
    private IShopProductService service;    
	@Inject
    private IBrandService brandService;
    @Inject
    private IProductTypeService productTypeService;
    @Inject
    private IAttributeValueService attributeValueService;
    @Inject
    private IShopSpecService shopSpecService;
    @Inject
    private IShopDiscussService shopDiscussService;
    @Inject
    private IAccountService accountService;
    @Inject
	private ShopUtil shopUtil;
    @Inject
    private IDeliveryRuleService deliveryRuleServie;
	@Inject
	private IShopDistributorService shopDistributorService;
	@Inject
	private ICusUploadFileService cusUploadFileService;
	@Inject
	private IRegionClassService regionClassService;
	
	public IRegionClassService getRegionClassService() {
		return regionClassService;
	}
	public void setRegionClassService(IRegionClassService regionClassService) {
		this.regionClassService = regionClassService;
	}
	public IShopProductService getService() {
		return service;
	}
	public void setService(IShopProductService service) {
		this.service = service;
	}
	public IBrandService getBrandService() {
		return brandService;
	}
	public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}
	public IProductTypeService getProductTypeService() {
		return productTypeService;
	}
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
	public IAttributeValueService getAttributeValueService() {
		return attributeValueService;
	}
	public void setAttributeValueService(IAttributeValueService attributeValueService) {
		this.attributeValueService = attributeValueService;
	}
	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}
	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}
	public IShopDiscussService getShopDiscussService() {
		return shopDiscussService;
	}
	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}
	public IAccountService getAccountService() {
		return accountService;
	}
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	public ShopUtil getShopUtil() {
		return shopUtil;
	}
	public void setShopUtil(ShopUtil shopUtil) {
		this.shopUtil = shopUtil;
	}
	public IDeliveryRuleService getDeliveryRuleServie() {
		return deliveryRuleServie;
	}
	public void setDeliveryRuleServie(IDeliveryRuleService deliveryRuleServie) {
		this.deliveryRuleServie = deliveryRuleServie;
	}
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(ShopProductQuery.class);
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        String name=CommUtil.null2String(form.get("name"));
        String proId=CommUtil.null2String(form.get("proId"));
        if(StringUtils.hasText(proId)){
        	qo.addQuery("obj.id", Long.valueOf(proId), "=");
        }
        if(StringUtils.hasText(name)){
        	qo.addQuery("obj.name like '%"+name+"%'");
        }
        IPageList pl = this.service.getShopProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        setProductTypes(form);
        setBrands(form);
        String pid=CommUtil.null2String(form.get("pid"));
        if(pid!=null){
        	form.addResult("pid", pid);
        }
        String brandId=CommUtil.null2String(form.get("brandId"));
        if(pid!=null){
        	form.addResult("brandId", brandId);
        }
        return new Page("/shopmanage/product/ShopProduct/shopProductList.html");
    }
    
    /**
     * 服务项目列表页面
     * 
     * @param form
     */
    public Page doListVirtual(WebForm form) {
        QueryObject qo = form.toPo(ShopProductQuery.class);
        IPageList pl = this.service.getShopProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        setProductTypes(form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/ShopProduct/virtualShopProductList.html");
    }
    
    /**
     * 积分商城列表页面
     * 
     * @param form
     */
    public Page doListPoints(WebForm form) {
        QueryObject qo = form.toPo(ShopProductQuery.class);
        qo.addQuery("obj.isPointsBuy",Short.parseShort("0"),"=");
        IPageList pl = this.service.getShopProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/shopmanage/product/ShopProduct/pointShopProductList.html");
    }
    /**
     * 商品模块中查询列表
     * @param form
     * @return
     */
    public Page doViewList(WebForm form){
    	QueryObject qo = form.toPo(ShopProductQuery.class);
    	IPageList pl=this.service.getShopProductBy(qo);
    	CommUtil.saveIPageList2WebForm(pl, form);
    	form.addResult("pl", pl);
    	setProductTypes(form);
    	setBrands(form);
    	return new Page("/shopmanage/product/ShopProduct/shopProductViewList.html");
    }
    
	private void setProductTypes(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.productTypeService.getProductTypeBy(qo).getResult();
    	form.addResult("proTypes", list);
    }
	public Page doToChangeType(WebForm form){
		String pid = CommUtil.null2String(form.get("pid"));
		return new Page("/shopmanage/product/ShopProduct/addProduct.html");
	}
	
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToAdd(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setLimit(-1);
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.productTypeService.getProductTypeBy(qo).getResult();
    	form.addResult("proTypes", list);
    	return new Page("/shopmanage/product/ShopProduct/addProduct.html");
    }
    /**
     * 进入添加页面下一步
     * @param form
     * @return
     */
    public Page doToAdd2(WebForm form){
    	String pid = CommUtil.null2String(form.get("pid"));
    	if(!"".equals(pid)){
    		ProductType pType = this.productTypeService.getProductType(Long.parseLong(pid));
    		form.addResult("pTypeStr", getPTypeStr(pType));
    		List<?> attrs = this.productTypeService.getParentAttrs(pType, Short.parseShort("1"));
    		List<?> canshus = this.productTypeService.getParentAttrs(pType, Short.parseShort("2"));
    		List<?> guigs = this.productTypeService.getParentAttrs(pType, Short.parseShort("3"));
    		List<?> brands = this.productTypeService.getParentBrands(pType);
    		form.addResult("attrs", attrs);
    		form.addResult("canshus", canshus);
    		form.addResult("guigs", guigs);
    		form.addResult("brands", brands);
    		//获取运费方案
    		QueryObject qo2 = new QueryObject();
    		qo2.setLimit(-1);
    		List<RegionClass> regionClassList = this.regionClassService.getRegionClassBy(qo2).getResult();
    		form.addResult("regionClassList",regionClassList);
    		
    	}
    	return new Page("/shopmanage/product/ShopProduct/addProduct2.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopProduct entry = (ShopProduct)form.toPo(ShopProduct.class);
        form.toPo(entry);
//        String imgPath = FileUtil.uploadFile(form, "pcImgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
//                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
//        if(!"".equals(imgPath)){
//        	entry.setPcImgPath(imgPath);
//        	this.cusUploadFileService.addCusUploadFile(imgPath);
//        }
        
        String vedioPath = FileUtil.uploadFile(form, "videoPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA, "wav;rm;rmvb;mpg;mpeg;asf;avi;swf;wmv;mp4;avi;flv;");
        if(!"".equals(vedioPath)){
        	entry.setVideoPath(vedioPath);
        	this.cusUploadFileService.addCusUploadFile(vedioPath);
        }
        
        if (!hasErrors()) {
        	Tenant tenant=ShiroUtils.getTenant();
        	entry.setTenant(tenant);
            Long id = this.service.addShopProduct(entry);
            ProductType pType = entry.getProductType();
            List<AttributeKey> attrs = this.productTypeService.getParentAttrs(pType, Short.parseShort("1"));
            addAttrV(form, entry, Integer.parseInt("1"), attrs);
            List<AttributeKey> canshus = this.productTypeService.getParentAttrs(pType, Short.parseShort("2"));
            addAttrV(form, entry, Integer.parseInt("2"), canshus);
            //List<AttributeKey> guigs = this.productTypeService.getParentAttrs(pType, Short.parseShort("3"));
            //addAttrV(form, entry, Integer.parseInt("3"), guigs);
            addGuige(form, entry);
            
            
            //处理运费方案
    		Long regionClassId = Long.parseLong((String) form.get("regionClassId"));
    		RegionClass regionClass = this.regionClassService.getRegionClass(regionClassId);
    		entry.setRegionClass(regionClass);
            
            this.service.updateShopProduct(id, entry);
            
            //处理邮费相关信息          
            Double productFreepostStart = Double.parseDouble((String) form.get("productFreepostStart"));
            Double productNearPostCost = Double.parseDouble((String) form.get("productNearPostCost"));
    		Double productNormalPostCost = Double.parseDouble((String) form.get("productNormalPostCost"));
    		Double productRemotePostCost = Double.parseDouble((String) form.get("productRemotePostCost"));
    		this.deliveryRuleServie.setNormalFreepostStart(entry, productFreepostStart);
    		this.deliveryRuleServie.setNearRegionPostCost(entry, productNearPostCost);//设置附近地区邮费
    		this.deliveryRuleServie.setNormalPostCost(entry, productNormalPostCost);
    		this.deliveryRuleServie.setRemoteRegionPostCost(entry, productRemotePostCost);
    		form.addResult("productFreepostStart", productFreepostStart);
    		form.addResult("productNormalPostCost", productNormalPostCost);    		
    		form.addResult("productRemotePostCost", productRemotePostCost);    
    		
    		
    		
    		
        }
        return go("list");
    }
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopProduct entry = this.service.getShopProduct(id);
            form.addResult("entry", entry);
            form.addResult("pid", entry.getProductType().getId());
            form.addResult("pTypeStr", getPTypeStr(entry.getProductType()));
            List<?> attrs = this.productTypeService.getParentAttrs(entry.getProductType(), Short.parseShort("1"));
    		List<?> canshus = this.productTypeService.getParentAttrs(entry.getProductType(), Short.parseShort("2"));
    		List<?> guigs = this.productTypeService.getParentAttrs(entry.getProductType(), Short.parseShort("3"));
    		List<?> brands = this.productTypeService.getParentBrands(entry.getProductType());
    		form.addResult("attrs", attrs);
    		form.addResult("canshus", canshus);
    		form.addResult("guigs", guigs);
    		form.addResult("brands", brands);
    		QueryObject qo = new QueryObject();
    		qo.addQuery("obj.product",entry,"=");
    		List<?> attrVs = this.attributeValueService.getAttributeValueBy(qo).getResult();
    		
    		qo = new QueryObject();
    		qo.addQuery("obj.product", entry, "=");
    		List<?> shopSpecList=this.shopSpecService.getShopSpecBy(qo).getResult();
    		form.addResult("shopSpecList", shopSpecList);
    		form.addResult("attrVs", attrVs);
    		
    		//获取邮费相关信息
    		DeliveryRule productFreepostStart = this.deliveryRuleServie.getNormalFreepostStart(entry);
    		if(productFreepostStart == null){
    			productFreepostStart = this.deliveryRuleServie.getDefaultNormalFreepostStart();
    		}
    		//附近地区
    		DeliveryRule productNearPostCost = this.deliveryRuleServie.getNearRegionPostCost(entry);
    		if(productNearPostCost == null){
    			productNearPostCost = this.deliveryRuleServie.getDefaultNearRegionPostCost();
    		}
    		DeliveryRule productNormalPostCost = this.deliveryRuleServie.getNormalPostCost(entry);
    		if(productNormalPostCost == null){
    			productNormalPostCost = this.deliveryRuleServie.getDefaultNormalPostCost();
    		}
    		DeliveryRule productRemotePostCost = this.deliveryRuleServie.getRemoteRegionPostCost(entry);
    		if(productRemotePostCost == null){
    			productRemotePostCost = this.deliveryRuleServie.getDefaultRemoteRegionPostCost();
    		}
    		form.addResult("productNearPostCost", productNearPostCost.getRuleValue());
    		form.addResult("productFreepostStart", productFreepostStart.getRuleValue());
    		form.addResult("productNormalPostCost", productNormalPostCost.getRuleValue());
    		form.addResult("productRemotePostCost", productRemotePostCost.getRuleValue());
    		
    		//获取运费方案
    		QueryObject qo2 = new QueryObject();
    		qo2.setLimit(-1);
    		List<RegionClass> regionClassList = this.regionClassService.getRegionClassBy(qo2).getResult();
    		form.addResult("regionClassList",regionClassList);
        }
        return new Page("/shopmanage/product/ShopProduct/addProduct2.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopProduct entry = this.service.getShopProduct(id);
        form.toPo(entry);
//        String imgPath = FileUtil.uploadFile(form, "pcImgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
//                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
//        if(!"".equals(imgPath)){
//        	entry.setPcImgPath(imgPath);;
//        	this.cusUploadFileService.addCusUploadFile(imgPath);
//        }
        String inventory = CommUtil.null2String(form.get("inventory"));
        System.out.println(inventory);
        entry.setInventory(Integer.parseInt(inventory));
        String videoPath = FileUtil.uploadFile(form, "videoPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA, "wav;rm;rmvb;mpg;mpeg;asf;avi;swf;wmv;mp4;avi;flv;");
        if(!"".equals(videoPath)){
        	entry.setVideoPath(videoPath);
        	this.cusUploadFileService.addCusUploadFile(videoPath);
        }
        if (!hasErrors()) {
        	QueryObject qo = new QueryObject();
        	qo.addQuery("obj.product",entry,"=");
        	List<AttributeValue> avs = this.attributeValueService.getAttributeValueBy(qo).getResult();
        	if(avs!=null){
        		for(AttributeValue av:avs){
            		this.attributeValueService.delAttributeValue(av.getId());
            	}
        	}
        	ProductType pType = entry.getProductType();
            List<AttributeKey> attrs = this.productTypeService.getParentAttrs(pType, Short.parseShort("1"));
            addAttrV(form, entry, Integer.parseInt("1"), attrs);
            List<AttributeKey> canshus = this.productTypeService.getParentAttrs(pType, Short.parseShort("2"));
            addAttrV(form, entry, Integer.parseInt("2"), canshus);
            //List<AttributeKey> guigs = this.productTypeService.getParentAttrs(pType, Short.parseShort("3"));
            //addAttrV(form, entry, Integer.parseInt("3"), guigs);
            uppGuige(form, entry);
            
            //处理运费方案
    		Long regionClassId = Long.parseLong((String) form.get("regionClassId"));
    		RegionClass regionClass = this.regionClassService.getRegionClass(regionClassId);
    		entry.setRegionClass(regionClass);
            
            boolean ret = service.updateShopProduct(id, entry);
            
            //处理邮费相关信息
            Double productFreepostStart = Double.parseDouble((String) form.get("productFreepostStart"));
    		Double productNormalPostCost = Double.parseDouble((String) form.get("productNormalPostCost"));
    		Double productRemotePostCost = Double.parseDouble((String) form.get("productRemotePostCost"));
    		Double productNearPostCost = Double.parseDouble((String) form.get("productNearPostCost"));
    		this.deliveryRuleServie.setNearRegionPostCost(entry, productNearPostCost);//设置附近地区邮费
    		this.deliveryRuleServie.setNormalFreepostStart(entry, productFreepostStart);
    		this.deliveryRuleServie.setNormalPostCost(entry, productNormalPostCost);
    		this.deliveryRuleServie.setRemoteRegionPostCost(entry, productRemotePostCost);
    		form.addResult("productFreepostStart", productFreepostStart);
    		form.addResult("productNormalPostCost", productNormalPostCost);
    		form.addResult("productRemotePostCost", productRemotePostCost);
        }else{
        	System.out.println("保存出错"+getErrors());
        }
//        return go("list");
        return DiscoShopUtil.goPage("/shopProduct.java?cmd=toEdit&id="+id.toString());
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        ShopProduct product=this.service.getShopProduct(id);
        //属性，参数
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.product", product, "=");
        qo.setLimit(-1);
        List<?> aList=this.attributeValueService.getAttributeValueBy(qo).getResult();
        if(aList!=null){
        	for (int i = 0; i < aList.size(); i++) {
				this.attributeValueService.delAttributeValue(((AttributeValue)aList.get(i)).getId());
			}  
        }
        //规格
        QueryObject qo2 = new QueryObject();
        qo2.addQuery("obj.product", product, "=");
        qo2.setLimit(-1);
        List<?> sList=this.shopSpecService.getShopSpecBy(qo2).getResult();
        if(sList!=null){
        	for (int i = 0; i < sList.size(); i++) {
				this.shopSpecService.delShopSpec(((ShopSpec)sList.get(i)).getId());
			}  
        }
        this.service.delShopProduct(id);
        return go("list");
    }
    public Page doGoodDetails(WebForm form){
    	Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
    	ShopProduct pro = this.service.getShopProduct(id);
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("good_details", pro.getGoods_details());
    	form.jsonResult(map);
    	return Page.JSPage;
    }
    /**
     * 设置品牌
     * @param form
     */
    private void setBrands(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setLimit(-1);
    	List<?> list = this.brandService.getBrandBy(qo).getResult();
    	form.addResult("brands", list);
    }
    /**
     * 设置父级显示名称
     * @param pType
     * @return
     */
    private String getPTypeStr(ProductType pType){
    	String str = pType.getName();
    	while(pType.getParent()!=null){
    		str = pType.getParent().getName()+">"+str;
    		pType = pType.getParent();
    	}
    	return str;
    }
    public Page doPublic(WebForm form){
    	String status = CommUtil.null2String(form.get("status"));
    	Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
    	Short tostatus = Short.parseShort(CommUtil.null2String(form.get("tostatus")));
    	ShopProduct pro = this.service.getShopProduct(id);
    	pro.setStatus(tostatus);
    	this.service.updateShopProduct(id, pro);
    	return DiscoShopUtil.goPage("/shopProduct.java?cmd=list&status="+status);
    }
    /**
     * 添加属性值
     * @param form
     * @param product
     * @param attrTypeStr
     * @param attrs
     */
    private void addAttrV(WebForm form,ShopProduct product,Integer type,List<AttributeKey> attrs){
    	String attrTypeStr="guig_";
    	if(type == 1){
    		attrTypeStr="attr_";
    	}else if(type==2){
    		attrTypeStr = "canshu_";
    	}
    	for(AttributeKey attr:attrs){
        	String value = CommUtil.null2String(form.get(attrTypeStr+attr.getId()));
        	if(!"".equals(value)){
        		AttributeValue attrV = new AttributeValue();
        		attrV.setAttributeKey(attr);
        		attrV.setProduct(product);
        		attrV.setValue(value);
        		attrV.setType(type);
        		this.attributeValueService.addAttributeValue(attrV);
        	}
        }
    }
    
    /**
     * 添加规格
     * @param form
     * @param product
     * @param attrTypeStr
     * @param attrs
     */
    private void addGuige(WebForm form,ShopProduct product){
    	int guige_num=CommUtil.null2Int(form.get("guige_num"));
    	Integer count = 0;//
    	for (int i = 0; i < guige_num; i++) {
    		ShopSpec shopSpec = new ShopSpec();
    		if(i==0){
    			shopSpec.setIsDefault(true);
    		}else {
    			shopSpec.setIsDefault(false);
			}
    		Integer ggInv=CommUtil.null2Int(form.get("gg_inventory_"+i));
    		count+=ggInv;
    		shopSpec.setProduct(product);
    		if(StringUtils.hasText(CommUtil.null2String(form.get("gg_name_"+i)))){
    			shopSpec.setName(CommUtil.null2String(form.get("gg_name_"+i)));
    			shopSpec.setCode(CommUtil.null2String(form.get("gg_code_"+i)));
    			shopSpec.setInventory(CommUtil.null2Int(form.get("gg_inventory_"+i)));
    			shopSpec.setAmt(Double.valueOf(CommUtil.null2String(form.get("gg_amt_"+i))));
    			shopSpec.setCostAmt(Double.valueOf(CommUtil.null2String(form.get("gg_costAmt_"+i))));  //成本价格
    			shopSpec.setTydAmt(Double.valueOf(CommUtil.null2String(form.get("gg_tydAmt_"+i))));// 体验店价格
    			shopSpec.setStore_price(Double.valueOf(CommUtil.null2String(form.get("gg_store_price_"+i)))); //微店价格
    			this.shopSpecService.addShopSpec(shopSpec);
    		}
		}
    	if(guige_num>0){
    		product.setInventory(count);
    	}
    }
    
    /**
     * 修改规格规格
     * @param form
     * @param product
     * @param attrTypeStr
     * @param attrs
     */
    private void uppGuige(WebForm form,ShopProduct product){
    	int guige_num=CommUtil.null2Int(form.get("guige_num"));
    	int upp_guige_num=CommUtil.null2Int(form.get("upp_guige_num"));
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.product", product, "=");
    	List<?> shopSpecList=this.shopSpecService.getShopSpecBy(qo).getResult();
    	Integer count=0;
    	//修改规格
    	for (int i = 0; i < upp_guige_num; i++) {
    		Integer invCount=CommUtil.null2Int(form.get("uppgg_inventory_"+i));
    		count+=invCount;
    		if(StringUtils.hasText(CommUtil.null2String(form.get("uppgg_id_"+i)))){
    			long id=Long.valueOf(CommUtil.null2String(form.get("uppgg_id_"+i)));
        		ShopSpec shopSpec=(ShopSpec)shopSpecList.get(i);
        		if(id==shopSpec.getId()){
        			shopSpec.setName(CommUtil.null2String(form.get("uppgg_name_"+i)));
        			shopSpec.setCode(CommUtil.null2String(form.get("uppgg_code_"+i)));
        			shopSpec.setInventory(CommUtil.null2Int(form.get("uppgg_inventory_"+i)));
        			shopSpec.setAmt(Double.valueOf(CommUtil.null2String(form.get("uppgg_amt_"+i))));
        			shopSpec.setCostAmt(Double.valueOf(CommUtil.null2String(form.get("uppgg_costAmt_"+i))));  //成本价格
        			shopSpec.setTydAmt(Double.valueOf(CommUtil.null2String(form.get("uppgg_tydAmt_"+i))));// 体验店价格
        			shopSpec.setStore_price(Double.valueOf(CommUtil.null2String(form.get("uppgg_store_price_"+i)))); //微店价格
        			this.shopSpecService.updateShopSpec(id, shopSpec);
        		}
    		}
		}
    	//新增
    	for (int i = 0; i < guige_num; i++) {
    		ShopSpec shopSpec = new ShopSpec();
    		if(i==0){
    			shopSpec.setIsDefault(true);
    		}
    		shopSpec.setProduct(product);
    		if(StringUtils.hasText(CommUtil.null2String(form.get("gg_name_"+i)))){
    			Integer invCount=CommUtil.null2Int(form.get("gg_inventory_"+i));
    			count+=invCount;
    			shopSpec.setName(CommUtil.null2String(form.get("gg_name_"+i)));
    			shopSpec.setCode(CommUtil.null2String(form.get("gg_code_"+i)));
    			shopSpec.setInventory(CommUtil.null2Int(form.get("gg_inventory_"+i)));
    			shopSpec.setAmt(Double.valueOf(CommUtil.null2String(form.get("gg_amt_"+i))));
    			shopSpec.setCostAmt(Double.valueOf(CommUtil.null2String(form.get("gg_costAmt_"+i))));  //成本价格
    			shopSpec.setTydAmt(Double.valueOf(CommUtil.null2String(form.get("gg_tydAmt_"+i))));// 体验店价格
    			shopSpec.setStore_price(Double.valueOf(CommUtil.null2String(form.get("gg_store_price_"+i)))); //微店价格
    			this.shopSpecService.addShopSpec(shopSpec);
    		}
		}
    	if(upp_guige_num>0 || guige_num>0){
    		product.setInventory(count);
    	}
    }
    
    /**
     * 删除规格
     * @param form
     * @return
     */
    public Page doDelGuiGe(WebForm form){
    	String ggId = CommUtil.null2String(form.get("ggId"));
    	boolean b=this.shopSpecService.delShopSpec(Long.valueOf(ggId));
    	if(!b){
    		this.addError("msg", "保存失败");
    	}
    	return pageForExtForm(form);
    }
    
    /**
     * 进入手机显示服务项目页面
     * @param form
     * @return
     */
    public Page doTelPage(WebForm form){
    	String idStr = CommUtil.null2String(form.get("pdid"));
    	QueryObject qo = (QueryObject) form.toPo(ShopProductQuery.class);
    	qo.addQuery("obj.goods_choice_type",Short.parseShort("0"),"=");
    	if(!idStr.equals("")){
    		qo.addQuery("obj.productType.id",Long.parseLong(idStr),"=");
    	}
    	qo.setOrderBy("sequence");
    	IPageList pl=this.service.getShopProductBy(qo);
        form.addResult("pl", pl);
        
    	QueryObject qo1 = (QueryObject) form.toPo(QueryObject.class);
    	qo1.addQuery("obj.parent.id", Long.parseLong("1"), "=");
    	IPageList pl1=this.productTypeService.getProductTypeBy(qo1);
        form.addResult("pl1", pl1);
    	return new Page("/shopmanage/product/telephonepage/virtualShopProductList.html");
    }
    /**
     * 导入服务项目详情页面，根据id
     * 
     * @param form
     */
    public Page doToDetail(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopProduct entry = this.service.getShopProduct(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/product/telephonepage/virtualShopProductDetail.html");
    }
    
    /**
     * 搜索商品
     * 
     * @param form
     */
    public Page doSearchGoods(WebForm form) {
        String name=CommUtil.null2String(form.get("name"));
        //Tenant tenant=ShiroUtils.getTenant();
        QueryObject qoGoods = new QueryObject();
        //qoGoods.addQuery("obj.tenant", tenant, "=");
        qoGoods.addQuery("obj.status", (short)1, "=");
        if(StringUtils.hasText(name)){
        	qoGoods.addQuery("obj.name like '%"+name+"%'");
        }
        IPageList iPageList=this.service.getShopProductBy(qoGoods);
        CommUtil.saveIPageList2WebForm(iPageList, form);
        form.addResult("goodsList", iPageList.getResult());
        return new Page("/bcd/wxshop/product/goodList.html");
    }
    
    /**
     * 商品详情
     * 
     * @param form
     */
    public Page doToProDet(WebForm form) {
    	HttpSession session = ActionContext.getContext().getSession();
		ShopMember member = (ShopMember) session.getAttribute("SHOPMEMBER");
		Account account = (Account) session.getAttribute("wx_account");
		if(account!=null){
			form.addResult("account", account);
		}
		if(member!=null){
			form.addResult("member", member);
			form.addResult("su", this.shopUtil);
		}
		
        String id=CommUtil.null2String(form.get("pId"));
        //Tenant tenant=ShiroUtils.getTenant();
        Tenant t = (Tenant) TenantContext.getTenant();
        ShopProduct shopProduct=this.service.getShopProduct(Long.valueOf(id));
        form.addResult("pro", shopProduct);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.tenant", t, "=");
        qo.addQuery("obj.isRecommend", true, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        qo.addQuery("obj.status",Short.parseShort("1"),"=");
        qo.setPageSize(5);
        List<?> proList=this.service.getShopProductBy(qo).getResult();
        Integer proCount=this.service.getShopProductBy(qo).getRowCount();
        form.addResult("proList", proList);
        form.addResult("proCount", proCount);
        
        QueryObject qo2 = new QueryObject();
        qo2.addQuery("obj.pro", shopProduct ,"=");
        qo2.setOrderBy("createDate");
        qo2.setOrderType("desc");
        qo2.setPageSize(5);
        IPageList pl=this.shopDiscussService.getShopDiscussBy(qo2);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("discuss", pl.getResult());
        WeixinBaseUtils.setWeixinjs(form, getAccount(form));
        //浏览记录
        DiscoShopUtil.setCookie("proHistory",id);
        
        //判断 会员 微店 实体店
        QueryObject qo3 = new QueryObject();
        qo3.addQuery("obj.member", member, "=");
        qo3.setPageSize(1);
        List<?> sdList=this.shopDistributorService.getShopDistributorBy(qo3).getResult();
        if(sdList!=null){
        	ShopDistributor distributor=(ShopDistributor)sdList.get(0);
        	if(distributor.getExStatus()==1){
        		form.addResult("status", "1");
        		form.addResult("distributor", distributor);
        	}else if(distributor.getStatus()==1){
        		form.addResult("status", "2");
        		form.addResult("distributor", distributor);
        	}        	
        }
        
        //统计规格信息
        Map<String, List<String>> shopSpecGroup = this.shopSpecService.getSpecGroupByProduct(shopProduct);
        form.addResult("specGroup", shopSpecGroup);        
        form.addResult("productSpecJson", this.shopSpecService.getSpecJsonByProduct(shopProduct));
//        //wx分享信息
//		if(account!=null && member!=null){
//			WeixinBaseUtils.setWeixinjs(form, getAccount(form));
//			String url = WeixinBaseUtils.getDomain()+"/wxShopBase.java?cmd=init&pmemberId="+member.getId()+"&accountId="+account.getId();
//			String weixinUrl = WeixinBaseUtils.getWxUrl(url, account);
//			form.addResult("indexUrl", weixinUrl);
//		}
        String checkstatus = checkApply(member);
		form.addResult("checkstatus", checkstatus);
		
		
		//获取评论相关
		 QueryObject qo4 = new QueryObject();
	     qo4.addQuery("obj.pro", shopProduct ,"=");
	     qo4.setOrderBy("createDate");
	     qo4.setOrderType("desc");
	     qo4.setPageSize(-1);
		 IPageList pl4 = this.shopDiscussService.getShopDiscussBy(qo4);
		 form.addResult("pl4",pl4);
		
        return new Page("/bcd/wxshop/product/productDetails.html");
    }
    
    /**
	 * 检查是否可以申请分销
	 * * 
	 * @author dll
	 * @version 创建时间：2016年7月20日 上午10:58:35
	 * @param member
	 * @return
	 */
	public String checkApply(ShopMember member){
		if(member!=null){
			ShopMember parent = member.getPmember();
			if(parent == null){//会员是第一级
				return "1";
			}else{
				ShopMember p_parent = parent.getPmember();
				if(p_parent == null){//会员是第二级
					return "1";
				}else{//上两级中有加盟店才能申请
					if(p_parent.getDisType() == 2 || parent.getDisType() == 2){
						return "1";
					}
				}	
			}			
		}
		return "0";
	}
    
    /**
     * 获取当前公众号
     * 
     * @param form
     * @return
     */
    protected Account getAccount(WebForm form) {
    	Account account = null;
    	Object accountObj=ActionContext.getContext().getSession().getAttribute("wx_account");
    	if(accountObj!=null){
    		account = (Account) accountObj;
    	}else{
    		String accountId = CommUtil.null2String(form.get("accountId"));
    		if(!"".equals(accountId)){
    			account = this.accountService.getAccount(Long.parseLong(accountId));
        		account.setTenant(account.getTenant());
        		ActionContext.getContext().getSession().setAttribute("wx_account", account);
    		}
    	}
        return account;
    }
    
    /**
     * 获取更多的推荐商品
     * 
     * @param form
     */
    public Page doAddMoreProduct(WebForm form) {
    	Integer curPage = CommUtil.null2Int(form.get("curPage"));
    	Integer pId=CommUtil.null2Int(form.get("pId"));
    	Tenant tenant = TenantContext.getTenant();
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.tenant", tenant, "=");
    	qo.addQuery("obj.isRecommend", true, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	qo.setCurrentPage(curPage);
    	qo.setPageSize(5);
    	IPageList pl=this.service.getShopProductBy(qo);
    	if(pl.getPages()<curPage){
    		return new Page("/bcd/wxshop/product/recpromore.html");
    	}
    	CommUtil.saveIPageList2WebForm(pl, form);
    	form.addResult("recproList", pl.getResult());
        return new Page("/bcd/wxshop/product/recpromore.html");
    }
    
    /**
     * 删除pc端商品图片
     */
    public Page doDelPcProPic(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	String picPath = CommUtil.null2String(form.get("picPath"));
    	ShopProduct pro = this.service.getShopProduct(Long.valueOf(id));
    	if(!pro.getPcImgPath().equals(picPath)){
    		this.addError("msg", "你没有权限删除图片");
    		return pageForExtForm(form);
    	}
    	String path=(String)ActionContext.getContext().getServletContext().getRealPath(picPath);
    	File file = new File(path);
    	if(file.exists()){
    		boolean b=file.delete();
    		if(b){
    			pro.setPcImgPath(null);
    			this.service.updateShopProduct(pro.getId(), pro);
    		}
    	}
    	return pageForExtForm(form);
    }
    /**
     * 删除商品中的视频
     */
    public Page doDelVideo(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	String videoPath = CommUtil.null2String(form.get("videoPath"));
    	ShopProduct pro = this.service.getShopProduct(Long.valueOf(id));
    	if(!pro.getVideoPath().equals(videoPath)){
    		this.addError("msg", "你没有权限删除图片");
    		return pageForExtForm(form);
    	}
    	String path=(String)ActionContext.getContext().getServletContext().getRealPath(videoPath);
    	File file = new File(path);
    	if(file.exists()){
    		boolean b=file.delete();
    		if(b){
    			pro.setVideoPath(null);
    			this.service.updateShopProduct(pro.getId(), pro);
    		}
    	}
    	return pageForExtForm(form);
    }
	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
        
}
