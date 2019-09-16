package com.eastinno.otransos.core.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 用于生成并处理各种各样标签的工具类
 * 
 * @author lengyu
 */
public class TagUtil {

	private static TagUtil singleton = new TagUtil();

	public static String checkBox(final Object value) {
		String ret = "";
		if (value instanceof Boolean)
			if (((Boolean) value).booleanValue())
				ret = "checked";
		return ret;
	}

	public static String checkBox(final String obj, final Object value) {
		String ret = "";
		if (obj != null && obj.equals(value))
			ret = "checked";
		return ret;
	}

	public static TagUtil getInstance() {
		return TagUtil.singleton;
	}

	public static String options(final int min, final int max, final int value) {
		String s = "";
		for (int i = min; i <= max; i++)
			s += "<option value='" + i + "' " + (i == value ? "selected" : "")
					+ ">" + i + "</option>";
		return s;
	}

	public static String options(final List<?> list, final String valuePoperty,
			final String titlePoperty, final String value) {
		if (list == null)
			return "";
		final String[][] items = new String[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			BeanWrapper wrapper = new BeanWrapperImpl(list.get(i));
			items[i][0] = wrapper.getPropertyValue(valuePoperty).toString();
			items[i][1] = wrapper.getPropertyValue(titlePoperty).toString();

		}
		return TagUtil.options(items, value);
	}

	public static String options(final String[][] items, final String value) {
		String s = "";
		for (final String[] element : items)
			s += "<option value='" + element[0] + "' "
					+ (element[0].equals(value) ? "selected" : "") + ">"
					+ element[1] + "</option>";
		return s;
	}

	/**
	 * DIV+CSS标准分页效果
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pages
	 *            总页数
	 * @param rowCount
	 *            总记录数
	 * @return 返回客户端HTML分页字符串
	 */
	public static String paginationDC(final int currentPage, final int pages,
			final int rowCount) {
		final StringBuffer sb = new StringBuffer();
		final int prePage = new Integer(currentPage - 1);
		final int nextPage = new Integer(currentPage + 1);
		final int beginPage = currentPage - 2 < 1 ? 1 : currentPage - 2;
		sb.append("<div class=\"p_bar\"><a title=\"共" + rowCount
				+ "条记录\" class=\"p_total\">" + rowCount + "</a><a title=\"当前第"
				+ currentPage + "页/共" + pages + "页\" class=\"p_pages\">"
				+ currentPage + "/" + pages + "</a>");
		if (currentPage > 3)
			sb.append("<a title=\"第一页\" class=\"p_redirect\" onclick=\"return gotoPage("
					+ 1 + ")\">|&lsaquo;</a>");
		if (currentPage > 2)
			sb.append("<a title=\"上一页\" class=\"p_redirect\" onclick=\"return gotoPage("
					+ prePage + ")\" style=\"font-weight: bold\">«</a>");
		if (currentPage >= 1)
			for (int i = beginPage, j = 0; i <= pages && j < 10; i++, j++)
				if (i == currentPage)
					sb.append("<a class=\"p_curpage\">" + i + "</a>");
				else
					sb.append("<a class=\"p_num\" title=\"转到第" + i
							+ "页\" onclick=\"return gotoPage(" + i + ")\">" + i
							+ "</a>");
		if (currentPage < pages)
			sb.append("<a title=\"下一页\" class=\"p_redirect\" onclick=\"return gotoPage("
					+ nextPage + ")\" style=\"font-weight: bold\">»</a>");
		if (pages > 10 && currentPage < pages - 7)
			sb.append("<a title=\"末页\" class=\"p_redirect\" onclick=\"return gotoPage("
					+ pages + ")\">&#8250;|</a>");
		sb.append("<a class=\"p_pages\" style=\"padding:0\">"
				+ "<input type=\"text\" class=\"p_input\" onkeydown=\"if(event.keyCode==13){return gotoPage(this.value)}\" /></a></div>");
		return sb.toString();
	}

	/**
	 * 分页标签 上海交通银行所用
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pages
	 *            总页数
	 * @param rowCount
	 *            总记录数
	 * @return 返回客户端HTML分页字符串
	 */
	public static String pagingForBankCMS(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		final int prePage = new Integer(currentPage - 1);
		final int nextPage = new Integer(currentPage + 1);
		final int beginPage = currentPage - 2 < 1 ? 1 : currentPage - 2;
		sb.append("<div class=\"pagination\">");
		if (currentPage > 3)
			sb.append("<a  class=\"page_previous\" onclick=\"return gotoPage("
					+ 1 + ")\">首页</a>");
		if (currentPage > 2)
			sb.append("<a  class=\"page_previous\" onclick=\"return gotoPage("
					+ prePage + ")\" >上一页</a>");
		if (currentPage >= 1)
			for (int i = beginPage, j = 0; i <= pages && j < 10; i++, j++)
				if (i == currentPage)
					sb.append("<a class=\"current\">" + i + "</a>");
				else
					sb.append("<a class=\"inactive\"  onclick=\"return gotoPage("
							+ i + ")\">" + i + "</a>");
		if (currentPage < pages)
			sb.append("<a  class=\"page_next\" onclick=\"return gotoPage("
					+ nextPage + ")\" >下一页</a>");
		if (pages > 10 && currentPage < pages - 7)
			sb.append("<a class=\"page_next\" onclick=\"return gotoPage("
					+ pages + ")\">末页</a>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * Verify That the given String is in valid URL format.
	 * 
	 * @param url
	 *            The url string to verify.
	 * @return a boolean indicating whether the URL seems to be incorrect.
	 */
	public final static boolean verifyUrl(String url) {
		if (url == null)
			return false;

		if (url.startsWith("https://"))
			url = "http://" + url.substring(8);

		try {
			new URL(url);

			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	/**
	 * 分页标签 梦坊国际所用
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pages
	 *            总页数
	 * @param rowCount
	 *            总记录数
	 * @return 返回客户端HTML分页字符串
	 */
	public static String pagingForMobile(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		final int prePage = new Integer(currentPage - 1);
		final int nextPage = new Integer(currentPage + 1);
		sb.append("<div class=\"pagingForMobile\">");
		if (currentPage == 1 && pages > 1) {
			sb.append("<a title=\"上一页\" class=\"huiL\"></a>");
		} else if (pages > 1) {
			sb.append("<a href=\"javascript:gotoPage(" + prePage
					+ ")\" title=\"上一页\" class=\"lanL\"></a>");
		}
		for (int i = 1, j = 0; i <= pages & j < 6; i++, j++) {
			if (i == currentPage) {
				sb.append("<a title=\"当前第" + i + "页\" class=\"hlan\"></a>");
			} else {
				sb.append("<a href=\"javascript:gotoPage(" + i
						+ ")\" title=\"第" + i + "页\" class=\"ylan\"></a>");
			}
		}
		if (currentPage == pages && pages > 1) {
			sb.append("<a title=\"下一页\" class=\"huiR\"></a>");
		} else if (pages > 1) {
			sb.append("<a href=\"javascript:gotoPage(" + nextPage
					+ ")\" title=\"下一页\" class=\"lanR\"></a>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * bootstrap样式分页效果,需在页面中引入bootstrap对应的CSS JS
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String pagingForBootStrap(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<ul class=\"pagination pull-right\" style=\"margin: 0;\">");
		if (currentPage == 1 || currentPage == 0) {
			sb.append("<li class=\"disabled\"><a title=\"首页\">首页</a></li>");
		} else if (pages > 1) {
			sb.append("<li><a href=\"javascript:gotoPage(1)\" title=\"首页\" >首页</a></li>");
		}
		if (currentPage < 6) {
			sb.append("<li class=\"disabled\"><a title=\"上一组\">&laquo;</a></li>");
		} else if (pages >= 6) {
			sb.append("<li><a href=\"javascript:gotoPage("
					+ (((currentPage - 1) / 5 - 1) * 5 + 1)
					+ ")\" title=\"上一组\" >&laquo;</a></li>");
		}
		for (int i = ((currentPage - 1) / 5) * 5 + 1, c = i + 5; i < c
				&& i <= pages; i++) {
			if (i == currentPage) {
				sb.append("<li class=\"active\"><a title=\"当前第" + i + "页\">"
						+ i + "</a></li>");
			} else {
				sb.append("<li><a href=\"javascript:gotoPage(" + i
						+ ")\" title=\"第" + i + "页\">" + i + "</a></li>");
			}
		}
		if (currentPage / 5 == pages / 5) {
			sb.append("<li class=\"disabled\"><a title=\"下一组\" class=\"huiR\">&raquo;</a>");
		} else if (pages > 5) {
			sb.append("<li><a href=\"javascript:gotoPage("
					+ (((currentPage - 1) / 5 + 1) * 5 + 1)
					+ ")\" title=\"下一组\">&raquo;</a></li>");
		}
		if (currentPage == pages) {
			sb.append("<li class=\"disabled\"><a title=\"尾页\" class=\"huiR\">尾页</a>");
		} else if (pages > 1) {
			sb.append("<li><a href=\"javascript:gotoPage(" + pages
					+ ")\" title=\"尾页\">尾页</a></li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}

	/**
	 * 基于新的蓝色理想BBS分页样式
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String paginationForBlueIdea(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"pg\">");
		if (currentPage == 1 || currentPage == 0) {
			sb.append("<a class=\"prev\">上一页</a>");
		} else {
			sb.append("<a href=\"javascript:gotoPage(" + (currentPage - 1)
					+ ")\" class=\"prev\">上一页</a>");
		}
		if (currentPage >= 6 && pages > 10) {
			sb.append("<a href=\"javascript:gotoPage(1)\" class=\"first\">1...</a>");
		}
		int start = 1;
		if (currentPage >= 6 && (pages - currentPage) >= 5) {
			start = currentPage - 4;
		} else if (currentPage >= 6 && (pages - currentPage) < 5) {
			if(pages - 9>0){
				start = pages - 9;
			}
		}
		for (int i = start; i <= (start + 9) && i <= pages; i++) {
			if (i == currentPage) {
				sb.append("<strong>" + i + "</strong>");
			} else {
				sb.append("<a href=\"javascript:gotoPage(" + i + ")\">" + i
						+ "</a>");
			}
		}
		if (pages > 10 && (pages - currentPage) > 5) {
			sb.append("<a href=\"javascript:gotoPage(" + pages
					+ ")\" class=\"last\">..." + pages + "</a>");
		}
		sb.append("<label><input type=\"text\" class=\"px\" size=\"2\" title=\"输入页码，按回车快速跳转\" value=\""
				+ currentPage
				+ "\" onkeydown=\"if(event.keyCode==13) {gotoPage(this.value)}\">"
				+ "<span title=\"共 "
				+ pages
				+ " 页\"> / "
				+ pages
				+ " 页</span></label>");
		if (currentPage == pages) {
			sb.append("<a class=\"nxt\">下一页</a>");
		} else {
			sb.append("<a href=\"javascript:gotoPage(" + (currentPage + 1)
					+ ")\" class=\"nxt\">下一页</a>");
		}
		sb.append("</div>");
		return sb.toString();
	}
	/**
	 * baichuda
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String paginationForBcd(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"userlist_allop\">");
		sb.append("<div class=\"user_list_page\">");
		sb.append("<span class=\"user_page_sp\">");
		if(currentPage>0){
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage(1)\">首页</a>");
			
			if (currentPage >=2) {
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage-1)+")\">上一页</a>");
			}
			sb.append("第&nbsp;&nbsp;");
			if(currentPage<=3){
				int count=pages<=5?pages:5;
				for(int i=1;i<=count;i++){
					if(i==currentPage){
						sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}else{
						sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}
				}
			}else{
				if(pages-currentPage>=2){
					for(int i=currentPage-2;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage>=1){
					for(int i=currentPage-3;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage==0){
					for(int i=currentPage-4;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}
			}
			sb.append("页&nbsp;&nbsp;");
			if(currentPage<pages){
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage+1)+")\">下一页</a>");
			}
			sb.append("共"+pages+"页&nbsp;&nbsp;");
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+pages+")\">末页</a>");
		}else{
			sb.append("共0页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		sb.append("</span>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	/**
	 * baichuda  2
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String paginationForBcd2(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"fenye\">");
		sb.append("<span>");
		if(currentPage>0){
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage(1)\">首页</a>");
			if (currentPage >=2) {
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage-1)+")\">上一页</a>");
			}
			sb.append("第&nbsp;&nbsp;");
			if(currentPage<=3){
				int count=pages<=5?pages:5;
				for(int i=1;i<=count;i++){
					if(i==currentPage){
						sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}else{
						sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}
				}
			}else{
				if(pages-currentPage>=2){
					for(int i=currentPage-2;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage>=1){
					for(int i=currentPage-3;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage==0){
					for(int i=currentPage-4;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}
			}
			sb.append("页&nbsp;&nbsp;");
			if(currentPage<pages){
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage+1)+")\">下一页</a>");
			}
			sb.append("共"+pages+"页&nbsp;&nbsp;");
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+pages+")\">末页</a>");
		}
		sb.append("</span>");
		sb.append("</div>");
		return sb.toString();
	}
	/**
	 * baichuda  2
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String paginationForBcd3(int currentPage, int pages,
			int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"details_discus_page\">");
		sb.append("<span target_id=\"goods_evas_list\">");
		if(currentPage>0){
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage(1)\">首页</a>");
			if (currentPage >=2) {
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage-1)+")\">上一页</a>");
			}
			sb.append("第&nbsp;&nbsp;");
			if(currentPage<=3){
				int count=pages<=5?pages:5;
				for(int i=1;i<=count;i++){
					if(i==currentPage){
						sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}else{
						sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}
				}
			}else{
				if(pages-currentPage>=2){
					for(int i=currentPage-2;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage>=1){
					for(int i=currentPage-3;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage==0){
					for(int i=currentPage-4;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}
			}
			sb.append("页&nbsp;&nbsp;");
			if(currentPage<pages){
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage+1)+")\">下一页</a>");
			}
			sb.append("共"+pages+"页&nbsp;&nbsp;");
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+pages+")\">末页</a>");
		}
		sb.append("</span>");
		sb.append("</div>");
		return sb.toString();
	}
	
	/**
	 * baichuda  4
	 * 
	 * @param currentPage
	 * @param pages
	 * @param rowCount
	 * @return
	 */
	public static String paginationForBcd4(int currentPage, int pages,int rowCount) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"fenye\">");
		sb.append("<span>");
		if(currentPage>0){
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage(1)\">首页</a>");
			if (currentPage >=2) {
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage-1)+")\">上一页</a>");
			}
			sb.append("第&nbsp;&nbsp;");
			if(currentPage<=3){
				int count=pages<=5?pages:5;
				for(int i=1;i<=count;i++){
					if(i==currentPage){
						sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}else{
						sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
					}
				}
			}else{
				if(pages-currentPage>=2){
					for(int i=currentPage-2;i<=currentPage+2;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage>=1){
					for(int i=currentPage-3;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}else if(pages-currentPage==0){
					for(int i=currentPage-4;i<=pages;i++){
						if(i==currentPage){
							sb.append("<a class=\"this\" href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}else{
							sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+i+")\">"+i+"</a>");
						}
					}
				}
			}
			sb.append("页&nbsp;&nbsp;");
			if(currentPage<pages){
				sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+(currentPage+1)+")\">下一页</a>");
			}
			sb.append("共"+pages+"页&nbsp;&nbsp;");
			sb.append("<a href=\"javascript:void(0);\" onclick=\"javascript:gotoPage("+pages+")\">末页</a>");
		}
		sb.append("</span>");
		sb.append("</div>");
		return sb.toString();
	}
}
