
package com.yinfu.business.operation.adv.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.operation.adv.model.PutIn;
import com.yinfu.business.util.PageUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/operation/adv/putin", viewPath = "/page/business/operation/adv/putin")
public class PutInController extends Controller<Device> {
	private static final String PREVIEW_PATH = "upload"+File.separator+"image"+File.separator+"adv"+File.separator;
	
	public void index() {
		SplitPage splitPages = PutIn.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		setAttr("groupId", getPara("_query.groupId"));
		render("index.jsp");
	}

	public void edit(){
		setAttr("size", getPara("size"));
		setAttr("groupRoleId", getPara("groupRoleId"));
		setAttr("advId", getPara("advId"));
		setAttr("groupId", getPara("groupId"));
		System.err.println(getPara("size")+"---"+getPara("groupRoleId")+"----"+getPara("advId")+"----"+getPara("groupId"));
		if(StringUtils.isNotBlank(getPara("groupRoleId"))){
			Record rec = Db.findFirst("select adv_type_id,image,link,des from bp_shop_group_role where id=? ", getPara("groupRoleId"));
			if(null != rec){
				setAttr("advId", rec.get("adv_type_id"));
				setAttr("image", rec.get("image"));
				setAttr("url", rec.get("link"));
				setAttr("des", rec.get("des"));
			}
		}
		render("edit.jsp");
	}
	
	public void save(){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			UploadFile file = getFile("upload", PathKit.getWebRootPath()+File.separator+PREVIEW_PATH);
			String image="";
			if(file!=null){
				String name = String.valueOf(System.currentTimeMillis());
				File sourceFile = ImageKit.renameFile(file, name);
				Fs.copyFileToHome(sourceFile, "image", "adv");//将图片复制到商铺平台服务器上
				File src = file.getFile();
				image = "upload/image/adv/" + name + src.getName().substring(src.getName().indexOf('.'));
			}
			String groupRoleID = getPara("groupRoleId");
//			int changeRow = 0;
			if(StringUtils.isNotBlank(groupRoleID)){
				if(!image.equals("")){
					Db.update("update bp_shop_group_role set image=?,link=?,des=? where id=? ", 
							new Object[]{image,getPara("url"),getPara("des"),groupRoleID});
				}else{
					Db.update("update bp_shop_group_role set link=?,des=? where id=? ", 
							new Object[]{getPara("url"),getPara("des"),groupRoleID});
				}
			}else{
				Record roles = Db.findFirst("select distinct role_id from system_user_role where user_id=? ", new Object[]{ContextUtil.getCurrentUserId()});
				if(null != roles){
					Object roleId = roles.get("role_id");
					groupRoleID = UUID.randomUUID().toString();
					Db.update("insert into bp_shop_group_role(id,shop_group_id,role_id,adv_type_id,image,link,des,create_date) "
							+ "values(?,?,?,?,?,?,?,now())", new Object[]{groupRoleID,getPara("groupId"),roleId,getPara("advId"),image,getPara("url"),getPara("des")});
				}
			}
			StringBuffer sql = new StringBuffer();
			sql.append("select sgr.image,sgr.link,sgr.des,bat.adv_index,bat.adv_type ");
			sql.append("from bp_shop_group_role sgr join bp_adv_type bat on (sgr.adv_type_id=bat.id) ");
			sql.append("where sgr.id=? ");
			Record rec = Db.findFirst(sql.toString(), new Object[]{groupRoleID});
			if(null != rec){
				if("adv".equals(rec.get("adv_type").toString())){//轮播广告
					StringBuffer changAdvSql = new StringBuffer();
					changAdvSql.append("update bp_adv a inner join bp_shop s on (a.shop_id=s.id) set a.image=?,a.link=?,a.des=? ");
					changAdvSql.append("where s.group_id=? and a.serial=?");
					int changeRow = Db.update(changAdvSql.toString(),new Object[]{rec.get("image"),rec.get("url"),rec.get("des"),getPara("groupId"),rec.get("adv_index")});
					if(changeRow > 0){
						return synShopGroupAdv(rec.get("adv_index"));
					}else{
						return true;
					}
				}else if("adv_bottom".equals(rec.get("adv_type").toString())){//底部广告
					return synAllOtherAdv(groupRoleID,"index_adv_bottom",rec.get("adv_index"));
				}else if("adv_start".equals(rec.get("adv_type").toString())){//渐变页广告
					return synAllOtherAdv(groupRoleID,"index_adv_start",rec.get("adv_index"));
				}else{
					return false;
				}
			}else{
				return false;
			}
		}});
		renderJsonResult(success);
	}
	
	private boolean synShopGroupAdv(Object serial){
		boolean success = true;
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.shop_id ");
		sql.append("from bp_shop s join bp_device d on (s.group_id=? and s.id = d.shop_id) ");
		sql.append("join bp_adv a on (a.serial = ? and a.shop_id=s.id) ");
		List<Record> advs= Db.find(sql.toString(), new Object[]{getPara("groupId"),serial});
		Iterator<Record> ite = advs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(DataSynUtil.addTask(rec.getInt("shop_id")+"", "index_adv", rec.getInt("id")+"", "1")){
				boolean b = PageUtil.changPageLog(rec.getInt("shop_id")+"", "index_adv", rec.getInt("id")+"", "2");
				success = success && b;
			}else{
				success = success && false;
			}
		}
		return true;//这里不能返回success，DataSynUtil.addTask的返回值为false不代表文件同步出问题
	}
	
	private boolean synAllOtherAdv(Object groupRoleId,String taskType,Object serial){
		boolean success = true;
		StringBuffer sql = new StringBuffer();
		sql.append("select s.id ");
		sql.append("from bp_shop s join bp_device d on (s.group_id=? and s.id = d.shop_id) ");
		sql.append("join bp_adv a on (a.serial = ? and a.shop_id=s.id) ");
		List<Record> advs= Db.find(sql.toString(),new Object[]{getPara("groupId"),serial});
		Iterator<Record> ite = advs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			boolean result = DataSynUtil.addTask(rec.get("id"), taskType, groupRoleId, "1");
			success = success && result;
		}
		return true;//这里不能返回success，DataSynUtil.addTask的返回值为false不代表文件同步出问题
	}
}
