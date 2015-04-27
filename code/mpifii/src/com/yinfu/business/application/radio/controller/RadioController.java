package com.yinfu.business.application.radio.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.radio.model.Radio;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao
 *
 */
@ControllerBind(controllerKey = "/business/app/radio")
public class RadioController extends Controller<Radio> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "radio" + File.separator;
	private static final String Radio_PATH = "upload" + File.separator + "radio" + File.separator ;
	//@formatter:off 
	/**
	 * Title: index
//	 * Description:进入音乐配置页面
	 * Created On: 2014年9月25日 下午5:17:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index(){
		SplitPage splitPages = Radio.dao.getRadioList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/radio/radioIndex.jsp");
	}
	//@formatter:off 
	/**
	 * Title: add
	 * Description:进入音乐增加页面
	 * Created On: 2014年9月25日 下午5:59:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add(){
		List<Record> typeList = Radio.dao.getRadioType();
		setAttr("typeList", typeList);
		render("/page/business/appcenter/radio/radioAdd.jsp");
	}
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:进入音乐修改页面
	 * Created On: 2014年9月28日 下午4:22:30
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit(){
		List<Record> typeList = Radio.dao.getRadioType();
		setAttr("typeList", typeList);
		Radio radio = Radio.dao.findById(getPara("id"));
		setAttr("radio", radio);
		render("/page/business/appcenter/radio/radioAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: delete
	 * Description:删除音乐
	 * Created On: 2014年9月28日 下午4:53:18
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete(){
		String id = getPara("id");//主键ID
		Radio radio = Radio.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if(radio.set("delete_date", new Date()).update()){
			returnData.put("state","success");
			renderJson(returnData);
		}else{
			returnData.put("state","error");
			renderJson(returnData);
		}
	}
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存视频信息
	 * Created On: 2014年9月25日 下午6:36:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		int max = 1024*1204*1024; 
		UploadFile file1 = getFile("radioImage",PathKit.getWebRootPath() + "/" +LOGO_PATH,max);
		UploadFile file2 = getFile("radioInfo",PathKit.getWebRootPath() + "/" +Radio_PATH,max);
		String icon = "";
		String link ="";
		if(file1!=null){
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file1, name);
			icon = "upload/image/radio/" + name + src.getName().substring(src.getName().indexOf('.'));
			Fs.copyFileToHome(src, "image", "radio");
		}
		if(file2!=null){
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file2, name);
			link = "upload/file/radio/" + name + src.getName().substring(src.getName().indexOf('.'));
			Fs.copyFileToHome(src, "file", "radio");
			
		}
		Radio Radio = getModel();
		if (Radio.getId() == null) {//新增
			renderJsonResult(Radio.set("icon", icon).set("link", link).set("create_date",new Date()).save());
		}else{//修改
			if(!icon.equals("")){
				renderJsonResult(Radio.set("icon", icon).set("link", link).update());
			}else{
				renderJsonResult(Radio.update());
			}
		}
	}
	//@formatter:off 
	/**
	 * Title: changeStatus
	 * Description:改变状态
	 * Created On: 2014年10月8日 下午2:23:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void changeStatus() {
		String id = getPara("id");// id
		JSONObject returnData = new JSONObject();
		Radio radio = Radio.dao.findById(id);
		if (id != null) {
			if (radio.getInt("status") == 0) {//未发布(发布)
				if (radio.set("status", 1).update()) {
					if(DataSynUtil.addTask("0", "audio", id, "1")){
						PageUtil.changPageLog("0", "audio", id, "1");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {//已发布(取消发布)
				if (radio.set("status", 0).update()) {
					if(DataSynUtil.addTask("0", "audio", id, "2")){
						PageUtil.changPageLog("0", "audio", id, "3");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			}
			
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
}
