
package com.yinfu.business.page.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_shop_page_app")
public class PageApp extends Model<PageApp> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static PageApp dao = new PageApp();
	
}
