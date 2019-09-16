package com.eastinno.otransos.shop.util;

import org.springframework.stereotype.Component;

import com.eastinno.otransos.web.tools.AutoChangeLink;
/**
 * 静态化
 * @author Administrator
 *
 */
@Component
public class ShopHtmlUtil {
	public String autoLink(AutoChangeLink obj){
		return obj.getDynamicUrl();
	}
}
