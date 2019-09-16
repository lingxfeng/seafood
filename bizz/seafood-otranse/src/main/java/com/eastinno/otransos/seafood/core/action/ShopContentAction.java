package com.eastinno.otransos.seafood.core.action;

import java.util.List;

import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
/**
 * 文章帮助网站内容管理
 * @author nsz
 */
@Action
public class ShopContentAction extends ShopBaseAction{
	@Inject
    private INewsDirService newsDirService;
    @Inject
    private INewsDocService newsDocService;
	/**
	 * 获取文章信息
	 * @param form
	 * @return
	 */
	public Page doToNewsDoc(WebForm form){
		String newDocId = CommUtil.null2String(form.get("newDocId"));
		if(!"".equals(newDocId)){
			NewsDoc newsDoc = this.newsDocService.getNewsDoc(Long.parseLong(newDocId));
			form.addResult("newsDoc", newsDoc);
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.sequence",newsDoc.getSequence(),"<");
			qo.addQuery("obj.dir",newsDoc.getDir(),"=");
			qo.setOrderBy("sequence desc");
			qo.setPageSize(1);
			List<NewsDoc> preDocs = this.newsDocService.getNewsDocBy(qo).getResult();
			if(preDocs!=null && preDocs.size()>0){
				form.addResult("preDoc", preDocs.get(0));
			}
			qo = new QueryObject();
			qo.addQuery("obj.sequence",newsDoc.getSequence(),">");
			qo.addQuery("obj.dir",newsDoc.getDir(),"=");
			qo.setOrderBy("sequence");
			qo.setPageSize(1);
			List<NewsDoc> afterDocs = this.newsDocService.getNewsDocBy(qo).getResult();
			if(afterDocs!=null && afterDocs.size()>0){
				form.addResult("afterDoc", afterDocs.get(0));
			}
			return new Page("/news/newsDoc.html");
		}else{
			String newDirId = CommUtil.null2String(form.get("newDirId"));
			if(!"".equals(newDirId)){
				QueryObject qo = form.toPo(QueryObject.class);
				qo.addQuery("obj.dir.id",Long.parseLong(newDirId),"=");
				qo.setOrderBy("sequence");
				List<NewsDoc> newsDocs = this.newsDocService.getNewsDocBy(qo).getResult();
				form.addResult("newsDocs", newsDocs);
			}
			return new Page("/news/newsDir.html");
		}
	}
	public void setNewsDirService(INewsDirService newsDirService) {
		this.newsDirService = newsDirService;
	}
	public void setNewsDocService(INewsDocService newsDocService) {
		this.newsDocService = newsDocService;
	}
	
}
