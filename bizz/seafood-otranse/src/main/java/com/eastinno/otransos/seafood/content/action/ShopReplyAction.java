package com.eastinno.otransos.seafood.content.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.domain.ShopReply;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.content.service.IShopReplyService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopReplyAction
 * 
 * @author nsz
 */
@Action
public class ShopReplyAction extends AbstractPageCmdAction {
	@Inject
	private IShopReplyService service;
	@Inject
	private IShopDiscussService shopDiscussService;

	public IShopDiscussService getShopDiscussService() {
		return shopDiscussService;
	}

	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}

	public IShopReplyService getService() {
		return service;
	}

	/**
	 * 默认方法
	 * 
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
		String discussId = CommUtil.null2String(form.get("discussId"));
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		qo.addQuery("obj.discuss.id", Long.parseLong(discussId), "=");
		qo.setOrderBy("date");
		qo.setOrderType("DESC");
		IPageList pl = this.service.getShopReplyBy(qo);
		CommUtil.saveIPageList2WebForm(pl, form);
		form.addResult("pl", pl);
		form.addResult("discussId", discussId);
		return new Page("/shopmanage/product/shopReply/shopReplyList.html");
	}

	/**
	 * 进入添加页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToSave(WebForm form) {
		String discussId = CommUtil.null2String(form.get("discussId"));
		if (!"".equals(discussId)) {
			ShopDiscuss discuss = this.shopDiscussService.getShopDiscuss(Long.parseLong(discussId));
			form.addResult("discuss", discuss);
			form.addResult("productId", discuss.getPro().getId());
			return new Page("/shopmanage/product/shopReply/shopReplyEdit.html");
		}
		return DiscoShopUtil.goPage("shopReply.java?cmd=list&discussId=" + discussId);
	}

	/**
	 * 保存数据
	 * 
	 * @param form
	 */
	public Page doSave(WebForm form) {
		ShopReply entry = (ShopReply) form.toPo(ShopReply.class);
		form.toPo(entry);
		if (!hasErrors()) {
			Long id = this.service.addShopReply(entry);
			if (id != null) {
				form.addResult("msg", "添加成功");
			}
		}

		return DiscoShopUtil.goPage("shopReply.java?cmd=list&discussId=" + entry.getDiscuss().getId());
	}

	/**
	 * 导入编辑页面，根据id值导入
	 * 
	 * @param form
	 */
	public Page doToEdit(WebForm form) {
		String idStr = CommUtil.null2String(form.get("id"));
		String discussId = CommUtil.null2String(form.get("discussId"));
		if (!"".equals(discussId)) {
			ShopDiscuss discuss = this.shopDiscussService.getShopDiscuss(Long.parseLong(discussId));
			form.addResult("discuss", discuss);
			Long id = Long.valueOf(Long.parseLong(idStr));
			ShopReply entry = this.service.getShopReply(id);
			form.addResult("entry", entry);
			form.addResult("productId", discuss.getPro().getId());
			return new Page("/shopmanage/product/shopReply/shopReplyEdit.html");
		}
		return DiscoShopUtil.goPage("shopReply.java?cmd=list&discussId=" + discussId);
	}

	/**
	 * 修改数据
	 * 
	 * @param form
	 */
	public Page doUpdate(WebForm form) {
		Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
		ShopReply entry = this.service.getShopReply(id);
		form.toPo(entry);
		if (!hasErrors()) {
			boolean ret = service.updateShopReply(id, entry);
			if (ret) {
				form.addResult("msg", "修改成功");
			}
		}
		return DiscoShopUtil.goPage("shopReply.java?cmd=list&discussId=" + entry.getDiscuss().getId());
	}

	/**
	 * 删除数据
	 * 
	 * @param form
	 */
	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		String discussId = CommUtil.null2String(form.get("discussId"));
		this.service.delShopReply(id);
		return DiscoShopUtil.goPage("shopReply.java?cmd=list&discussId=" + discussId);
	}

	public void setService(IShopReplyService service) {
		this.service = service;
	}
}