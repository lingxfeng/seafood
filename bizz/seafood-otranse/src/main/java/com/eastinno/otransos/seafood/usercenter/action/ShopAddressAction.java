package com.eastinno.otransos.seafood.usercenter.action;

import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ShopAddressAction 会员-》收货地址对应的action类
 * 
 * @author nsz
 */
@Action
public class ShopAddressAction extends AbstractPageCmdAction {
	@Inject
	private IShopAddressService service;
	@Inject
	private ISystemRegionService systemRegionService;

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
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);

		String trueName = CommUtil.null2String(form.get("trueName")); // 真实姓名
		if (StringUtils.hasText(trueName)) {
			qo.addQuery("obj.trueName like '%" + trueName + "%'");
		}
		// ShopAddressQuery qo = form.toPo(ShopAddressQuery.class);
		String addressFilter = CommUtil.null2String(form.get("addressFilter")); // 收货地址
		if (StringUtils.hasText(addressFilter)) {
			QueryObject qo2 = new QueryObject();
			qo2.setLimit(-1);
			qo2.addQuery("obj.title like '%" + addressFilter + "%'");
			List<SystemRegion> list = this.systemRegionService.querySystemRegion(qo2).getResult();
			if (list != null && list.size() != 0) {
				// Set<Long> regionList = new HashSet();
				StringBuilder regionList = new StringBuilder("0");
				for (SystemRegion region : list) {
					regionList.append("," + region.getId());
					QueryObject qo3 = new QueryObject();
					qo3.setLimit(-1);
					qo3.addQuery("obj.path like '" + region.getPath() + "%'");
					List<SystemRegion> list2 = this.systemRegionService.querySystemRegion(qo3).getResult();
					if (list2 != null && list2.size() != 0) {
						for (SystemRegion region2 : list2) {
							regionList.append("," + region2.getId());
						}
					}
				}

				qo.addQuery("(obj.area.id in(" + regionList + ") or obj.area_info like '%" + addressFilter + "%')");
			} else {
				qo.addQuery("obj.area_info like '%" + addressFilter + "%'");
			}

		}
		IPageList pl = this.service.getShopAddressBy(qo);
		CommUtil.saveIPageList2WebForm(pl, form);
		form.addResult("pl", pl);
		return new Page("/bcd/member/shopaddress/ShopAddressList.html");
	}

	/**
	 * 进入添加页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doToSave(WebForm form) {
		return new Page("/bcd/member/shopaddress/ShopAddressEdit.html");
	}

	/**
	 * 保存数据
	 * 
	 * @param form
	 */
	public Page doSave(WebForm form) {
		ShopAddress entry = form.toPo(ShopAddress.class);
		form.toPo(entry);
		if (!hasErrors()) {
			Long id = this.service.addShopAddress(entry);
			if (id != null) {
				form.addResult("msg", "添加成功");
			}
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
		if (!"".equals(idStr)) {
			Long id = Long.valueOf(Long.parseLong(idStr));
			ShopAddress entry = this.service.getShopAddress(id);
			form.addResult("entry", entry);
		}
		return new Page("/bcd/member/shopaddress/ShopAddressEdit.html");
	}

	/**
	 * 修改数据
	 * 
	 * @param form
	 */
	public Page doUpdate(WebForm form) {
		Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
		ShopAddress entry = this.service.getShopAddress(id);
		form.toPo(entry);
		if (!hasErrors()) {
			boolean ret = service.updateShopAddress(id, entry);
			if (ret) {
				form.addResult("msg", "修改成功");
			}
		}
		return go("list");
	}

	/**
	 * 保存地址
	 * 
	 * @param form
	 */
	public Page doUpdateAddress(WebForm form) {
		Integer id = CommUtil.null2Int(form.get("id"));
		String code = CommUtil.null2String(form.get("area_id"));
		ShopAddress entry = this.service.getShopAddress((long) id);
		SystemRegion systemRegion = this.systemRegionService.getSystemRegionBySn(code);
		entry.setArea(systemRegion);
		this.service.updateShopAddress(entry.getId(), entry);
		return Page.nullPage;
	}

	/**
	 * 删除数据
	 * 
	 * @param form
	 */
	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		this.service.delShopAddress(id);
		return go("list");
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public void setService(IShopAddressService service) {
		this.service = service;
	}
}