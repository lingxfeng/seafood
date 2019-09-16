package com.eastinno.otransos.seafood.droduct.action;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.seafood.core.service.ICusUploadFileService;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.query.ProductTypeQuery;
import com.eastinno.otransos.seafood.droduct.service.IAttributeKeyService;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IBrandTypeService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 商品分类
 * @author 
 */
@Action
public class ProductTypeAction extends AbstractPageCmdAction {
    @Inject
    private IProductTypeService service;
    @Inject
    private IAttributeKeyService attributeKeyService;
    @Inject
    private IBrandService brandService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private IBrandTypeService brandTypeService;
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
    	String json = CommUtil.null2String(form.get("json"));
        QueryObject qo = (QueryObject) form.toPo(ProductTypeQuery.class);
        IPageList pl = null;
        if(!"".equals(json)){
        	qo.setOrderBy("sequence desc");
        	pl=this.service.getProductTypeBy(qo);
        	List<?> list = pl.getResult();
        	form.jsonResult((list==null?new ArrayList<Object>():list));
        	return Page.JSPage;
        }else{
        	qo.setOrderBy("sequence");//排序
        	pl=this.service.getProductTypeBy(qo);
        	CommUtil.saveIPageList2WebForm(pl, form);
        	form.addResult("pl", pl);
            return new Page("/shopmanage/product/productType/productTypeList.html");
        }
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	String parentId = CommUtil.null2String(form.get("parentId"));
    	setProductTypes(form);
    	setBrandType(form);
    	form.addResult("parentId", parentId);
    	//获取分类父类拥有的属性
    	if(!"".equals(parentId)){
    		ProductType ptP = this.service.getProductType(Long.parseLong(parentId));
        	List<?> attrsP = this.service.getParentAttrs(ptP, Short.parseShort("1"));
        	form.addResult("attrsP", attrsP);
        	
        	//获取分类父级拥有的参数
        	List<?> canshusP = this.service.getParentAttrs(ptP, Short.parseShort("2"));
        	form.addResult("canshusP", canshusP);
        	
        	//获取分类父级拥有的规格
        	List<?> guigsP = this.service.getParentAttrs(ptP, Short.parseShort("3"));
        	form.addResult("guigsP", guigsP);
        	
        	//获取分类父级拥有的品牌
        	List<?> brandsP = this.service.getParentBrands(ptP);
        	form.addResult("brandsP", brandsP);
    	}
    	return new Page("/shopmanage/product/productType/productTypeEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ProductType entry = (ProductType)form.toPo(ProductType.class);
        form.toPo(entry);
        if(entry.getParent()!=null){
        	entry.setLevel(entry.getParent().getLevel()+1);
        }
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
    	if(!"".equals(imgPath)){
    		this.cusUploadFileService.addCusUploadFile(imgPath);
    		entry.setImgPath(imgPath);
    	}
    	String advImg = FileUtil.uploadFile(form, "advImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
    	if(!"".equals(advImg)){
    		this.cusUploadFileService.addCusUploadFile(advImg);
    		entry.setAdvImg(advImg);
    	}
        if (!hasErrors()) {
        	/**
        	 * 保存属性
        	 */
            Long id = this.service.addProductType(entry);
            addAttrButeKey(Short.parseShort("1"), form, entry);
            addAttrButeKey(Short.parseShort("2"), form, entry);
            addAttrButeKey(Short.parseShort("3"), form, entry);
            this.service.updateProductType(id, entry);
        }
        return go("list");
    }
    /**
     * 导入编辑页面，根据id值导入
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        Long id = Long.valueOf(Long.parseLong(idStr));
        ProductType entry = this.service.getProductType(id);
        form.addResult("entry", entry);
        setProductTypes(form);
        setBrandType(form);
    	form.addResult("parentId", entry.getParent()!=null?entry.getParent().getId():"");
    	//获取分类拥有的属性
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.type",Short.parseShort("1"),"=");
    	qo.addQuery("obj.productType",entry,"=");
    	qo.setOrderBy("sequence");
    	List<?> listattr = this.attributeKeyService.getAttributeKeyBy(qo).getResult();
    	form.addResult("attrs", listattr);
    	
    	//获取分类拥有的参数
    	qo = new QueryObject();
    	qo.addQuery("obj.type",Short.parseShort("2"),"=");
    	qo.addQuery("obj.productType",entry,"=");
    	qo.setOrderBy("sequence");
    	List<?> listcanshu = this.attributeKeyService.getAttributeKeyBy(qo).getResult();
    	form.addResult("canshus", listcanshu);
    	
    	//获取分类拥有的规格
    	qo = new QueryObject();
    	qo.addQuery("obj.type",Short.parseShort("3"),"=");
    	qo.addQuery("obj.productType",entry,"=");
    	qo.setOrderBy("sequence");
    	List<?> guigs = this.attributeKeyService.getAttributeKeyBy(qo).getResult();
    	form.addResult("guigs", guigs);
    	
    	//获取分类父类拥有的属性
    	List<?> attrsP = this.service.getParentAttrs(entry.getParent(), Short.parseShort("1"));
    	form.addResult("attrsP", attrsP);
    	
    	//获取分类父级拥有的参数
    	List<?> canshusP = this.service.getParentAttrs(entry.getParent(), Short.parseShort("2"));
    	form.addResult("canshusP", canshusP);
    	
    	//获取分类父级拥有的规格
    	List<?> guigsP = this.service.getParentAttrs(entry.getParent(), Short.parseShort("3"));
    	form.addResult("guigsP", guigsP);
    	
    	//获取分类父级拥有的品牌
    	List<?> brandsP = this.service.getParentBrands(entry.getParent());
    	form.addResult("brandsP", brandsP);
        return new Page("/shopmanage/product/productType/productTypeEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ProductType entry = this.service.getProductType(id);
        form.toPo(entry,false,false);
        if(entry.getParent()!=null){
        	entry.setLevel(entry.getParent().getLevel()+1);
        }
        String imgPath = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
    	if(!"".equals(imgPath)){
    		this.cusUploadFileService.addCusUploadFile(imgPath);
    		entry.setImgPath(imgPath);
    	}
    	String advImg = FileUtil.uploadFile(form, "advImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
    	if(!"".equals(advImg)){
    		this.cusUploadFileService.addCusUploadFile(advImg);
    		entry.setAdvImg(advImg);
    	}
        if (!hasErrors()) {
            
            /**
             * 更新已经存在的属性/参数/规格
             */
            List<Serializable> delIds = updateAttibuteKey(form, entry);
            this.attributeKeyService.batchDelAttributeKeys(delIds);
            /**
             * 添加新的属性
             */
            addAttrButeKey(Short.parseShort("1"), form, entry);
            addAttrButeKey(Short.parseShort("2"), form, entry);
            addAttrButeKey(Short.parseShort("3"), form, entry);
            /**
             * 更新品牌
             */
            String brandStr = CommUtil.null2String(form.get("brand"));
            if(!"".equals(brandStr)){
            	String[] brands = brandStr.split("_");
            	List<Brand> brandList = new ArrayList<Brand>();
            	for(String brandId:brands){
            		Brand brand = this.brandService.getBrand(Long.parseLong(brandId));
            		brandList.add(brand);
            	}
            	entry.setBrands(brandList);
            }else{
            	entry.setBrands(new ArrayList<Brand>());
            }
            /**
             * 更新品牌结束
             */
            boolean ret = service.updateProductType(id, entry);
        }
        return go("list");
    }
    /**
     * 异步请求更新
    * @Title: doUpdateAjax 
      
    * @Description: TODO(这里用一句话描述这个方法的作用) 
      
    * @param @param form
    * @param @return    设定文件 
      
    * @return Page    返回类型 
      
    * @throws
     */
    public Page doUpdateAjax(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	String fieldName = CommUtil.null2String(form.get("fieldName"));
    	String value = CommUtil.null2String(form.get("value"));
    	ProductType pro = this.service.getProductType(Long.parseLong(id));
    	try {
			Field f = pro.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			DiscoShopUtil.setValueToField(f, pro, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	this.service.updateProductType(Long.parseLong(id), pro);
    	return Page.JSPage;
    }
    /**
     * 获取不带分页的分类列表
     * @param form
     * @return
     */
    public Page doFindTypeByPId(WebForm form){
    	QueryObject qo = form.toPo(ProductTypeQuery.class);
    	List<ProductType> list = this.service.getProductTypeBy(qo).getResult();
    	form.jsonResult(list==null?new ArrayList():list);
    	return Page.JSONPage;
    }
    /**
     * 根据分类名称查询同父类下所有分类
     * @param form
     * @return
     */
    public Page doFindTypeByName(WebForm form){
    	String name = CommUtil.null2String(form.get("name"));
    	try {
			name=new String(name.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ProductType obj = this.service.getProductByName(name);
    	List<List<ProductType>> list = new ArrayList<List<ProductType>>();
    	if(obj!=null){
    		Integer level = obj.getLevel();
    		if(level==2){
    			QueryObject qo = new QueryObject();
    			qo.addQuery("obj.parent",obj.getParent(),"=");
    			List<ProductType> list2 = this.service.getProductTypeBy(qo).getResult();
    			if(list2!=null){
    				list.add(list2);
    			}
    		}else if(level==3){
    			QueryObject qo = new QueryObject();
    			qo.addQuery("obj.parent",obj.getParent().getParent(),"=");
    			List<ProductType> list2 = this.service.getProductTypeBy(qo).getResult();
    			if(list2!=null){
    				list.add(list2);
    			}
    			qo = new QueryObject();
    			qo.addQuery("obj.parent",obj.getParent(),"=");
    			List<ProductType> list3 = this.service.getProductTypeBy(qo).getResult();
    			if(list3!=null){
    				list.add(list3);
    			}
    		}
    	}
    	form.jsonResult(list);
    	return Page.JSONPage;
    }
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delProductType(id);
        return go("list");
    }
    /**
     * 验证当前类别是否已经存在
     * @param form
     * @return
     */
    public Page doCheckName(WebForm form){
    	String name = CommUtil.null2String(form.get("name"));
    	String id = CommUtil.null2String(form.get("id"));
    	boolean isChecked = true;
    	if("".equals(name)){
    		isChecked = false;
    	}else{
        	QueryObject qo = form.toPo(ProductTypeQuery.class);
        	if(!"".equals(id)){
        		ProductType pro = this.service.getProductType(Long.parseLong(id));
        		if(!name.equals(pro.getName())){
        			qo.addQuery("obj.name",name,"=");
        			List<?> list = this.service.getProductTypeBy(qo).getResult();
                	if(list !=null && list.size()>0){
                		isChecked = false;
                	}
        		}
        	}
    	}
    	form.jsonResult(isChecked);
    	return Page.JSPage;
    }
    /**
     * 设置商品类型
     * @param service
     */
    private void setProductTypes(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setLimit(-1);
    	qo.addQuery("obj.parent is EMPTY");
    	List<?> list = this.service.getProductTypeBy(qo).getResult();
    	form.addResult("proTypes", list);
    }
    /**
     * 添加新的属性/参数/规格
     * @param type
     * @param form
     * @param pType
     */
    private void addAttrButeKey(Short type,WebForm form,ProductType pType){
    	String preStr = type==1?"attr_":type==2?"canshu_":"guig_";
    	String addNumName = type==1?"addNum":type==2?"canshuNum":"guigNum";
    	String addStr = "add_";
    	int addNum = CommUtil.null2Int(form.get(addNumName));
    	for(int i=1;i<=addNum;i++){
    		String name = CommUtil.null2String(form.get(preStr+addStr+"name_"+i));
    		if(!"".equals(name)){
    			AttributeKey a = new AttributeKey();
    			a.setProductType(pType);
    			a.setName(name);
    			a.setType(type);
    			setAttributekey((long)i, a, type, form, preStr, addStr);
    			this.attributeKeyService.addAttributeKey(a);
    		}
    	}
    }
    /**
     * 更新已经存在的
     * @param form
     * @param pType
     */
    private List<Serializable> updateAttibuteKey(WebForm form,ProductType pType){
    	String preStr,addStr="";
    	Short type;
    	List<AttributeKey> as = pType.getAttributeKeys();
    	List<Serializable> delIds = new ArrayList<Serializable>();
    	for(int i=(as.size()-1);i>=0;i--){
    		AttributeKey a = as.get(i);
    		type = a.getType();
    		preStr = a.getType()==1?"attr_":a.getType()==2?"canshu_":"guig_";
    		String name = CommUtil.null2String(form.get(preStr+addStr+"name_"+a.getId()));
    		if(!"".equals(name)){
    			a.setName(name);
    			setAttributekey(a.getId(), a, type, form, preStr, addStr);
    			this.attributeKeyService.updateAttributeKey(a.getId(), a);
    		}else{
    			delIds.add(a.getId());
    		}
    	}
    	return delIds;
    }
    
    /**
     * 检查编码是否重复
     * 
     * @param form
     */
    public Page doCheckCode(WebForm form) {
    	Integer id=CommUtil.null2Int(form.get("id"));
        String code=CommUtil.null2String(form.get("code"));
        ProductType pType=this.service.getProductType((long)id);
        if(pType!=null){
        	if(!pType.getCode().equals(code)){
        		ProductType productType=this.service.getProductTypeByCode(code);
                if(productType!=null){
                	this.addError("msg", "此编码已有，请重新输入");
                }
            }
        }else{
        	ProductType productType=this.service.getProductTypeByCode(code);
        	if(productType!=null){
            	this.addError("msg", "此编码已有，请重新输入");
            }
        }
        return pageForExtForm(form);
    }
    
    
    /**
     * 从页面设置属性参数规格值
     * @param i
     * @param a
     * @param type
     * @param form
     * @param preStr
     * @param addStr
     */
    private void setAttributekey(Long i,AttributeKey a,Short type,WebForm form,String preStr,String addStr){
    	a.setValue(CommUtil.null2String(form.get(preStr+addStr+"value_"+i)));
		a.setSequence(Integer.parseInt(CommUtil.null2String(form.get(preStr+addStr+"sequence_"+i))));
		if(type==2){
			String type2 = CommUtil.null2String(form.get(preStr+addStr+"colType_"+i));
			if(!"".equals(type2)){
				a.setColType(Short.parseShort(type2));
			}
		}
    }
    public void setService(IProductTypeService service) {
        this.service = service;
    }
	public void setAttributeKeyService(IAttributeKeyService attributeKeyService) {
		this.attributeKeyService = attributeKeyService;
	}
	public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	
	public void setBrandTypeService(IBrandTypeService brandTypeService) {
		this.brandTypeService = brandTypeService;
	}
	private void setBrandType(WebForm form){
    	QueryObject qo = new QueryObject();
    	qo.setOrderBy("sequence");
    	qo.setLimit(-1);
        List<?> bTypeList=this.brandTypeService.getBrandTypeBy(qo).getResult();
        form.addResult("brandTypes", bTypeList);
        
        qo = new QueryObject();
        qo.setPageSize(-1);
        List<?> brands=this.brandService.getBrandBy(qo).getResult();
        form.addResult("brands", brands);
    }
}