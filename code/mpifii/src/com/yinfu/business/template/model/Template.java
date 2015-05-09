
package com.yinfu.business.template.model;

import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_temp")
public class Template extends Model<Template> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Template dao = new Template();
	
	public List<Template> list() {
		return dao.find("select name,preview_img from bp_temp where delete_date is null and is_used order by create_date ");
	}
	
	public JSONObject delete(String id) {
		Template template = new Template().setDate("delete_date");
		JSONObject json = new JSONObject();
		json.put("success", template.save());
		return json;
	}
	
	public JSONObject add(String name, String imgPath, String filePath,String isUsed) {
		Template template = new Template().set("name", name)
				.set("preview_img", imgPath)
				.set("file_path", filePath)
				.set("is_used", isUsed);
		JSONObject json = new JSONObject();
		json.put("success", template.save());
		return json;
	}
	
	public JSONObject edit(String id, String name, String imgPath, String filePath,String isUsed) {
		Template template = new Template().set("id", id)
				.set("name", name)
				.set("preview_img", imgPath)
				.set("file_path", filePath)
				.set("is_used", isUsed);
		JSONObject json = new JSONObject();
		json.put("success", template.update());
		return json;
	}
	
}
