package com.eastinno.otransos.seafood.droduct.action;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.domain.DeliveryRuleExt;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleExtService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * CouponlistAction
 * 
 * @author
 */
@Action
public class DeliveryRuleExtAction extends AbstractPageCmdAction {

	@Inject
	private IDeliveryRuleExtService deliveryRuleExtService;

	/**
	 * 列表页面
	 * 
	 * @param form
	 */
	public Page doList(WebForm form) {
		String name = CommUtil.null2String(form.get("name"));
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		if (StringUtils.hasText(name)) {
			qo.addQuery("obj.name like '%" + name + "%'");
		}
		IPageList pageList = this.deliveryRuleExtService.getDeliveryRuleExtBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("deliveryList", pageList.getResult());
		return new Page("/bcd/system/deliveryruleext/deliveryruleextlist.html");
	}

	/**
	 * 进入添加页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToSave(WebForm form) {
		return new Page("/bcd/system/deliveryruleext/deliveryruleextedit.html");
	}

	/**
	 * 保存数据
	 * 
	 * @param form
	 */
	public Page doSave(WebForm form) {
		DeliveryRuleExt entry = (DeliveryRuleExt) form.toPo(DeliveryRuleExt.class);
		form.toPo(entry);
		// if (!hasErrors()) {
		this.deliveryRuleExtService.addDeliveryRuleExt(entry);
		// }
		return go("list");
	}

	/**
	 * 导入编辑页面，根据id值导入
	 * 
	 * @param form
	 */
	public Page doToEdit(WebForm form) {
		String idStr = CommUtil.null2String(form.get("id"));
		Long id = Long.valueOf(Long.parseLong(idStr));
		DeliveryRuleExt entry = this.deliveryRuleExtService.getDeliveryRuleExt(id);
		form.addResult("deliveryRuleExt", entry);
		return new Page("/bcd/system/deliveryruleext/deliveryruleextedit.html");
	}

	/**
	 * 修改数据
	 * 
	 * @param form
	 */
	public Page doUpdate(WebForm form) {
		Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
		DeliveryRuleExt entry = this.deliveryRuleExtService.getDeliveryRuleExt(id);
		form.toPo(entry, false, false);

		boolean ret = this.deliveryRuleExtService.updateDeliveryRuleExt(id, entry);
		return go("list");
	}

	public IDeliveryRuleExtService getDeliveryRuleExtService() {
		return deliveryRuleExtService;
	}

	public void setDeliveryRuleExtService(IDeliveryRuleExtService deliveryRuleExtService) {
		this.deliveryRuleExtService = deliveryRuleExtService;
	}

}