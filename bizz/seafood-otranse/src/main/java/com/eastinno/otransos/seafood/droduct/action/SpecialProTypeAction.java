package com.eastinno.otransos.seafood.droduct.action;

import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.SpecialProType;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.ISpecialProTypeService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

/**
 * SpecialProTypeAction
 * @author 
 */
@Action
public class SpecialProTypeAction extends AbstractPageCmdAction {
    @Inject
    private ISpecialProTypeService service;
    @Inject
    private IProductTypeService productTypeService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getSpecialProTypeBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        SpecialProType entry = (SpecialProType)form.toPo(SpecialProType.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addSpecialProType(entry);
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
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SpecialProType entry = this.service.getSpecialProType(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSpecialProType(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
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
        this.service.delSpecialProType(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 判断是否首次登陆
     * 
     * @param form
     */
    public Page doJudgeFirLogin(WebForm form) {
    	Integer ptId = CommUtil.null2Int(form.get("ptId"));
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
    	if(member==null){
    		this.addError("msg", "没有获取用户信息，请重新登陆");
    	}else{
//    		if(member.getDisType()!=0){
//    			this.addError("msg", "你已经是微店或会员，不需要买此产品");
//    		}
    	}
    	if(!hasErrors()){
    		boolean b=this.service.judegeFirstLogin((long)ptId, member);
    		if(b){
    			this.addError("msg", "0");//首次登陆输入密码
    		}
    	}
        return pageForExtForm(form);
    }
    
    /**
     * 会员首次登陆输入密码正确,增加一条记录
     * 
     * @param form
     */
    public Page doSaveSpeProType(WebForm form) {
    	Integer ptId = CommUtil.null2Int(form.get("protId"));
    	String password = CommUtil.null2String(form.get("password"));
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
    	if(member==null){
    		this.addError("msg", "没有获取用户信息，请重新登录");
    	}
    	if(!StringUtils.hasText(password)){
    		this.addError("msg", "密码不能为空");
    	}
    	if(!hasErrors()){
			ProductType pType=this.productTypeService.getProductType((long)ptId);
	    	QueryObject qo = new QueryObject();
			qo.addQuery("obj.member", member, "=");
			qo.addQuery("obj.pType", pType, "=");
			List<?> list=this.service.getSpecialProTypeBy(qo).getResult();
			if(list==null){
				if(pType.getIsSpecialProType()){
					if(password.equals(pType.getPassword())){
						SpecialProType spt = new SpecialProType();
						spt.setMember(member);
						spt.setpType(pType);
						this.service.addSpecialProType(spt);
					}else{
						this.addError("msg", "密码匹配错误，请重新输入");
					}
				}
			}
    	}
        return pageForExtForm(form);
    }
    
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	public void setService(ISpecialProTypeService service) {
        this.service = service;
    }
}