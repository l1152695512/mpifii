
package com.yinfu.business.operation.adv.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_adv_putin")
public class AdvPutinModel extends Model<AdvPutinModel> {
	private static final long serialVersionUID = 1L;
	public static AdvPutinModel dao = new AdvPutinModel();
}
