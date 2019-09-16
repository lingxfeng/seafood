package com.eastinno.otransos.cms.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.application.core.service.IAttachmentService;
import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.query.LinkImgGroupQuery;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 
 */
@Action
public class LinkImgGroupAction extends SaaSCMSBaseAction {
	@Inject
	private ILinkImgGroupService service;
	@Inject
	private IAttachmentService attachmentService;
	@Inject
	private ISystemDictionaryService systemDictionaryService;

	public void setSystemDictionaryService(
			ISystemDictionaryService systemDictionaryService) {
		this.systemDictionaryService = systemDictionaryService;
	}

	public void setService(ILinkImgGroupService service) {
		this.service = service;
	}

	/**
	 * 查询ppt
	 * 
	 * @param form
	 * @return
	 */
	public Page doGetPPt(WebForm form) {
		String code = CommUtil.null2String(form.get("code"));
		if (StringUtils.hasText(code)) {
			SystemDictionary sd = systemDictionaryService.getBySn(code);
			if (sd != null && sd.getChildren().size() > 0) {
				List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
				for (SystemDictionaryDetail sdd : sd.getChildren()) {
					Map<String, Object> map = sdd2treemap(sdd, form);
					nodes.add(map);
				}
				form.jsonResult(nodes);
			}
		}
		return Page.JSONPage;
	}

	public Page doList(WebForm form) {
		LinkImgGroupQuery qo = form.toPo(LinkImgGroupQuery.class);
		qo.setOrderBy("sequence");// TODO 这儿如果DESC排序了则上移下移就有问题了原因未知
		IPageList pl = service.getLinkImgGroupBy(qo);
		form.jsonResult(pl);
		return Page.JSONPage;
	}

	private Map<String, Object> sdd2treemap(SystemDictionaryDetail sdd,
			WebForm form) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", sdd.getId() + "");
		map.put("text", sdd.getTitle());
		map.put("leaf", true);
		return map;
	}

	public Page doSave(WebForm form) {
		LinkImgGroup ppt = form.toPo(LinkImgGroup.class);
		LinkImgType linkImgType = ppt.getType();
		QueryObject qo = new QueryObject();
		if (linkImgType != null) {
			qo.addQuery("obj.type", linkImgType, "=");
		} else {
			qo.addQuery("obj.type is EMPTY");
		}
		qo.addQuery("obj.sequence", new Integer(0), ">");
		qo.setOrderBy("sequence");
		qo.setOrderType("desc");
		List<?> linkImgGroups = this.service.getLinkImgGroupBy(qo).getResult();
		if (linkImgGroups != null && linkImgGroups.size() > 0) {
			ppt.setSequence(((LinkImgGroup) linkImgGroups.get(0)).getSequence() + 1);
		}
		Map<String, FileItem> map = form.getFileElement();

		FileItem item = (FileItem) map.get("imgPath");
		FileItem videoItem = (FileItem) map.get("videoPath");
		if (item != null) {
			String fileName = item.getName();
			if (!StringUtils.hasText(fileName)) {
			} else {
				String path = this.parseUploadFileItem(form, item);
				ppt.setImgPath(path);
				ppt.setResourceType((short) 1);
			}
		}
		if (videoItem != null) {
			String videoFileName = videoItem.getName();
			if (!StringUtils.hasText(videoFileName)) {
				String bigData = CommUtil.null2String(form.get("bigData"));
				if (StringUtils.hasText(bigData)) {
					ppt.setVideoPath(bigData);
				}
				// ppt.setVideoPath(CommUtil.null2String(form.get("bigData")));
				ppt.setResourceType((short) 2);
			} else {
				String videoPath1 = this.parseUploadFileItem2(form, videoItem);
				ppt.setVideoPath(videoPath1);
				ppt.setResourceType((short) 2);
			}
		}
		if (!hasErrors()) {
			service.addLinkImgGroup(ppt);
		}
		Page page = pageForExtForm(form);
		page.setContentType("html");
		return page;
	}

	public Page doUpdate(WebForm form) {
		String id = CommUtil.null2String(form.get("id"));
		Map<String, FileItem> map = form.getFileElement();
		String path = "";
		String videoPath = "";
		LinkImgGroup entity = service.getLinkImgGroup(Long.valueOf(id));
		entity = (LinkImgGroup) form.toPo(entity);
		FileItem item = (FileItem) map.get("imgPath");
		FileItem videoItem = (FileItem) map.get("videoPath");
		if (item != null) {
			String fileName = item.getName();
			if (!StringUtils.hasText(fileName)) {
			} else {
				path = this.parseUploadFileItem(form, item);
				entity.setImgPath(path);
			}
		}
		if (videoItem != null) {
			String videoFileName = videoItem.getName();
			if (!StringUtils.hasText(videoFileName)) {
				String bigData = CommUtil.null2String(form.get("bigData"));
				if (StringUtils.hasText(bigData)) {
					entity.setVideoPath(bigData);
				}

				entity.setResourceType((short) 2);
			} else {
				videoPath = this.parseUploadFileItem2(form, videoItem);
				entity.setVideoPath(videoPath);
				entity.setResourceType((short) 2);
			}
		}
		if (!this.hasErrors()) {
			service.updateLinkImgGroup(Long.valueOf(id), entity);
		}

		Page page = pageForExtForm(form);
		page.setContentType("html");
		return page;
	}

	public Page doRemove(WebForm form) {
		String mulitId = CommUtil.null2String((form.get("mulitId")));
		if (StringUtils.hasText(mulitId)) {
			String[] ids = mulitId.split(",");
			for (String id : ids) {
				service.delLinkImgGroup(Long.parseLong(id));
			}
		} else {
			Long id = Long.valueOf((String) form.get("id"));
			service.delLinkImgGroup(id);
		}
		return pageForExtForm(form);
	}

	private void addAttachment(WebForm form, String path, Integer fileType) {
		Map<String, FileItem> map = form.getFileElement();
		FileItem item = (FileItem) map.get("imgPath");
		Long fileSize = item.getSize();
		String fileName = item.getName();
		String suffix = fileName.substring(fileName.lastIndexOf("."))
				.toLowerCase().replace(".", "");

		Attachment file = new Attachment();
		file.setPath(path);
		file.setFileName(path.substring(path.lastIndexOf("/") + 1));
		file.setCreateUser(UserContext.getUser());
		file.setExt(suffix);
		file.setLength(fileSize);
		file.setTypes(fileType);
		file.setOldName(fileName);
		this.attachmentService.addAttachment(file);
	}

	private void addAttachment2(WebForm form, String path, Integer fileType) {
		Map<String, FileItem> map = form.getFileElement();
		FileItem item = (FileItem) map.get("videoPath");
		Long fileSize = item.getSize();
		String fileName = item.getName();
		String suffix = fileName.substring(fileName.lastIndexOf("."))
				.toLowerCase().replace(".", "");
		Attachment file = new Attachment();
		file.setPath(path);
		file.setFileName(path.substring(path.lastIndexOf("/") + 1));
		file.setCreateUser(UserContext.getUser());
		file.setExt(suffix);
		file.setLength(fileSize);
		file.setTypes(fileType);
		file.setOldName(fileName);
		this.attachmentService.addAttachment(file);
	}

	/**
	 * 上移下移
	 * 
	 * @param form
	 * @return
	 */
	public Page doSwapSequence2(WebForm form) {
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		String id = CommUtil.null2String(form.get("id"));
		LinkImgGroup newsDocCur = service.getLinkImgGroup(Long.parseLong(id));
		LinkImgType linkImgType = newsDocCur.getType();
		if (linkImgType != null) {
			qo.addQuery("obj.type", linkImgType, "=");
		} else {
			qo.addQuery("obj.type is EMPTY");
		}
		qo.setPageSize(-1);
		qo.setOrderType("asc");
		qo.setOrderBy("sequence");
		List<?> list = this.service.getLinkImgGroupBy(qo).getResult();
		int sq = CommUtil.null2Int(form.get("sq"));
		String down = CommUtil.null2String(form.get("down"));
		int cursequence = newsDocCur.getSequence();
		if (!"".equals(down)) {
			qo.setOrderBy("sequence desc");
			qo.addQuery("obj.sequence", cursequence, "<");
		} else {
			qo.setOrderBy("sequence");
			qo.addQuery("obj.sequence", cursequence, ">");
		}

		// if ((sq - 1 < 1 && !StringUtils.hasText(down)) ||
		// (("true".equals(down)) && (sq >= list.size()))) {
		// addError("msg", "未满足移动排序规则");
		// }
		// else {
		// LinkImgGroup currently = (LinkImgGroup) list.get(sq - 1);
		// Integer sequence = currently.getSequence();
		// LinkImgGroup other = (LinkImgGroup) list.get("true".equals(down) ? sq
		// : sq - 2);
		// currently.setSequence(other.getSequence());
		// other.setSequence(sequence);
		// this.service.updateLinkImgGroup(currently.getId(), currently);
		// this.service.updateLinkImgGroup(other.getId(), other);
		// }

		if (list != null && list.size() > 0) {
			LinkImgGroup otherObj = (LinkImgGroup) list.get(0);
			newsDocCur.setSequence(otherObj.getSequence());
			this.service.updateLinkImgGroup(newsDocCur.getId(), newsDocCur);
			otherObj.setSequence(cursequence);
			this.service.updateLinkImgGroup(otherObj.getId(), otherObj);
		}

		return pageForExtForm(form);
	}

	/**
	 * 排序(移动数据的顺序号)
	 * 
	 * @param form
	 * @return
	 */
	public Page doSwapSequence(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);

		int sq = CommUtil.null2Int(form.get("sq"));
		String id = CommUtil.null2String(form.get("id"));
		String op = CommUtil.null2String(form.get("down"));
		String parentId = CommUtil.null2String(form.get("parentId"));
		LinkImgGroup newsDir = this.service.getLinkImgGroup(Long.parseLong(id));
		qo.setPageSize(-1);
		if (!"".equals(id)) {
			qo.addQuery("obj.type.id", newsDir.getType().getId(), "=");
		} else {
			qo.addQuery("obj.type is EMPTY", null);
		}

		// sq = !"true".equals(op) ? sq + 2 : sq - 2;
		// if (!"".equals(id)) {
		// LinkImgGroup newsDir =
		// this.service.getLinkImgGroup(Long.parseLong(id));
		// if (newsDir.getOrg() == null) {
		// qo.addQuery("obj.org is EMPTY", null);
		// } else {
		// qo.addQuery("obj.org", newsDir.getOrg(), "=");
		// }
		// }
		qo.setOrderBy("sequence");
		qo.setOrderType("asc");
		List list = service.getLinkImgGroupBy(qo).getResult();
		if (sq - 2 < 0 && !"true".equals(op) || sq > list.size()) {
			this.addError("msg", "当前记录位于第一条不能再向上移动");
		} else if ("true".equals(op) && sq >= list.size()) {
			this.addError("msg", "当前记录位于最后一条不能再向下移动");
		} else {
			LinkImgGroup obj1 = (LinkImgGroup) list.get(sq - 1);
			Integer sequence = obj1.getSequence();
			LinkImgGroup obj2 = (LinkImgGroup) list.get("true".equals(op)
					? sq
					: sq - 2);

			if (sequence.equals(obj2.getSequence())) {
				// 下移
				if ("true".equals(op)) {
					obj1.setSequence(obj2.getSequence() + 2);
					obj2.setSequence(sequence + 1);
				} else {
					obj1.setSequence(obj2.getSequence() + 1);
					obj2.setSequence(sequence + 2);
				}
			} else {
				obj1.setSequence(obj2.getSequence());
				obj2.setSequence(sequence);
			}
			this.service.updateLinkImgGroup(obj1.getId(), obj1);
			this.service.updateLinkImgGroup(obj2.getId(), obj2);
		}
		return pageForExtForm(form);
	}

	/**
	 * 对上传文件进行一系列处理
	 * 
	 * @param form
	 * @param item
	 * @return
	 */
	private String parseUploadFileItem(WebForm form, FileItem item) {
		// 1附件
		// 2媒体文件
		// 3FLASH
		// 4图片
		Integer fileType = UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE_NM;
		// 图片
		String ext = FileUtil.IMAGE_FILE_EXT + FileUtil.VIDIO_FILE_EXT;
		String fieldName = "imgPath";
		ITenantObject co = null;
		if (UserContext.getUser() != null
				&& UserContext.getUser() instanceof ITenantObject) {
			co = (ITenantObject) UserContext.getUser();
		}
		String path = FileUtil.uploadFile(form, fieldName,
				UploadFileConstant.FILE_UPLOAD_PATH + "/"
						+ co.getTenant().getCode(), ext);
		if (!hasErrors() && StringUtils.hasText(path)) {
			// 保存上传文件相关信息到数据库
			this.addAttachment(form, path, fileType);
		}
		return path;
	}

	private String parseUploadFileItem2(WebForm form, FileItem item) {
		Integer fileType = UploadFileConstant.FILE_UPLOAD_TYPE_MEDIA_NM;// 1附件
																		// 2媒体文件
																		// 3FLASH
																		// 4图片
		// 图片
		String ext = FileUtil.VIDIO_FILE_EXT;
		String fieldName = "videoPath";
		ITenantObject co = null;
		if (UserContext.getUser() != null
				&& UserContext.getUser() instanceof ITenantObject) {
			co = (ITenantObject) UserContext.getUser();
		}
		String path = FileUtil.uploadFile(form, fieldName,
				UploadFileConstant.FILE_UPLOAD_PATH + "/"
						+ co.getTenant().getCode(), ext);
		if (!hasErrors() && StringUtils.hasText(path)) {
			// 保存上传文件相关信息到数据库
			this.addAttachment2(form, path, fileType);
		}
		return path;
	}

	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

}