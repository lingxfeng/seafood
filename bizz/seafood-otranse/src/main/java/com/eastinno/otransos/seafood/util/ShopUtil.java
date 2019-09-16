package com.eastinno.otransos.seafood.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.Brand;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.service.IAttributeKeyService;
import com.eastinno.otransos.seafood.droduct.service.IAttributeValueService;
import com.eastinno.otransos.seafood.droduct.service.IBrandService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
/**
 * 前台查询数据工具类
 * @author nsz
 *
 */
@Component
public class ShopUtil {
	@Autowired
    private IProductTypeService productTypeService;
	@Autowired
	private IShopProductService shopProductService;
	@Autowired
	private IAttributeKeyService attributeKeyService;
	@Autowired
	private IAttributeValueService attributeValueService;
	@Autowired
	private IBrandService brandService;
	@Autowired
    private IShopDistributorService shopDistributorService;
	@Autowired
	private IShopMemberService shopMemberService;
	public IProductTypeService getProductTypeService() {
		return productTypeService;
	}

	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public IAttributeKeyService getAttributeKeyService() {
		return attributeKeyService;
	}

	public void setAttributeKeyService(IAttributeKeyService attributeKeyService) {
		this.attributeKeyService = attributeKeyService;
	}

	public IAttributeValueService getAttributeValueService() {
		return attributeValueService;
	}

	public void setAttributeValueService(
			IAttributeValueService attributeValueService) {
		this.attributeValueService = attributeValueService;
	}

	public IBrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(IBrandService brandService) {
		this.brandService = brandService;
	}

	/**
	 * 商品分类查询
	 * @return
	 */
	public PtypeQueryUtil getPtypeQuery(){
		return new PtypeQueryUtil(this);
	}
	/**
	 * 查询商品
	 * @return
	 */
	public ProductQueryUtil getProductQuery(){
		return new ProductQueryUtil(this);
	}
	public boolean isDistributor(ShopMember member){
		if(member!=null){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member", member, "=");
//			qo.addQuery("obj.exStatus", "1", "=");
			qo.setPageSize(1);
			List<?> list = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			if(list!=null && list.size()>0)	{
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取用户身份:0普通会员，1微点，2体验店
	 * @param member
	 * @return
	 */
	public int getMemberType(ShopMember member){
		if(member!=null){
			return member.getDisType();
		}
		return 0;
	}
	/**
	 * 获取所属店铺
	 * @param member
	 * @return
	 */
	public ShopDistributor getDistributor(ShopMember member){
		if(member!=null){
			member = this.shopMemberService.getShopMember(member.getId());
			ShopDistributor distributor = member.getMyDistributor();
			if(distributor==null || (distributor!=null && distributor.getExStatus()!=1 && distributor.getStatus()!=1)){
				distributor = member.getDistributor();
				if(distributor==null){
					return getDistributor(member.getPmember());
				}
			}
			return distributor;
		}
		return null;
	}
	
	public Brand getBrandByCode(String code){
		return this.brandService.getBrandByCode(code);
	}
	public String getDomain(){
		return WeixinBaseUtils.getDomain();
	}
	
	public Double getYj(Double dvalue){
		if(dvalue!=null){
			BigDecimal b = new BigDecimal(dvalue);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}
		return 0.0;
	}
	
	public String datetoString(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d=sf.format(date);
		return d;
	}
	
	public Boolean judgeSpeProType(Long id){
		ProductType productType=this.productTypeService.getProductType(id);
		if(productType!=null){
			boolean b=productType.getIsSpecialProType();
			return b;
		}
		return false;
	}
	public String subStr(String strval,int len){
		if(strval.length()>len){
			return strval.substring(0, len);
		}
		return strval;
	}
	/**
	 * 检查是否可以申请分销
	 * * 
	 * @author dll
	 * @version 创建时间：2016年7月20日 上午10:58:35
	 * @param member
	 * @return
	 */
	public static String checkApply(ShopMember member){
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
}
