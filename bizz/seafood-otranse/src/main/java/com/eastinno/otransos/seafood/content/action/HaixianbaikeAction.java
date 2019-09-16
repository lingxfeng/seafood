package com.eastinno.otransos.seafood.content.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.core.action.WxShopBaseAction;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

/**
 * ShopReplyAction
 * 
 * @author nsz
 */
@Action
public class HaixianbaikeAction extends WxShopBaseAction {

	public Page doInit(WebForm form, Module module) {
		return new Page("/baike/index.html");
	}

	public Page doDetail(WebForm form, Module module) {
		form.addResult("msg", CommUtil.null2String(form.get("msg")));
		form.addResult("image", CommUtil.null2String(form.get("image")));
		form.addResult("vedio", CommUtil.null2String(form.get("vedio")));
		return new Page("/baike/detail.html");
	}
}