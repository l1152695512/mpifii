
package com.yinfu.business.operation.adv.controller;

<<<<<<< HEAD
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
=======
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

<<<<<<< HEAD
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.operation.adv.model.Permission;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/oper/adv/permission", viewPath = "/page/business/operation/adv/permission")
public class PermissionController extends Controller<Record> {
	@SuppressWarnings("unchecked")
	public void index() {
		SplitPage splitPages = Permission.dao.findList(splitPage);
		List<Record> orgs = (List<Record>) splitPages.getPage().getList();
		List<Record> orgsId = new ArrayList<Record>();
		for(int i=0;i<orgs.size();i++){
			orgsId.add(orgs.get(i));
		}
		Map<Object,List<Record>> orgsChildrens = DataOrgUtil.getChildrensForOrgs(orgsId,true);
		Iterator<Record> ite = orgs.iterator();
		while(ite.hasNext()){
			Record row = ite.next();
			String orgsSqlIn = DataOrgUtil.recordListToSqlIn(orgsChildrens.get(row.get("id")), "id");
			row.set("persons", getOrgsPersonNum(orgsSqlIn));//显示该组织下的用户数量
			row.set("shops", getOrgsShopNum(orgsSqlIn));//显示该组织下的商铺数量
		}
		setAttr("splitPage", splitPages);
		render("index.jsp");
	}
	private Object getOrgsPersonNum(String orgsSqlIn){
		Record count = Db.findFirst("select count(*) count from system_user where org_id in ("+orgsSqlIn+")");
		return count.get("count");
	}
	private Object getOrgsShopNum(String orgsSqlIn){
		Record count = Db.findFirst("select count(*) count from bp_shop where org_id in ("+orgsSqlIn+")");
		return count.get("count");
	}
	
	public void edit(){
		StringBuffer sql = new StringBuffer();
		sql.append("select basp.id,basp.name adv_name,basp.des,IF(ao2.id is NULL,'0',IF(ao2.edit_able,'1','0')) checked ");
		sql.append("from system_user su join sys_org so on (su.id=? and so.id=? and su.org_id=so.pid) ");
		sql.append("join bp_adv_org ao1 on (ao1.org_id=so.pid) ");
		sql.append("join bp_adv_spaces basp on (basp.id=ao1.adv_spaces) ");
		sql.append("left join bp_adv_org ao2 on (ao2.org_id=so.id and basp.id=ao2.adv_spaces) ");
		sql.append("order by basp.create_date ");
		List<Record> advs = Db.find(sql.toString(), new Object[]{ContextUtil.getCurrentUserId(),getPara("orgId")});
		setAttr("advs", advs);
		setAttr("orgId", getPara("orgId"));
		render("edit.jsp");
	}
	public void save() {
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			String orgId = getPara("orgId");
			List<String> selectedAdvs = new ArrayList<String>();
			if(null != getParaValues("selectedAdvs")){
				Collections.addAll(selectedAdvs, getParaValues("selectedAdvs"));
			}
			List<Record> oldAdvs = Db.find("select id,adv_spaces,edit_able from bp_adv_org where org_id=? ", new Object[]{orgId});
//			List<Object[]> disableOrgAdvs = new ArrayList<Object[]>();
			StringBuffer disableSqlIn = new StringBuffer("''");
			List<Object[]> enableOrgAdvs = new ArrayList<Object[]>();
			
			for(int i=0;i<oldAdvs.size();i++){
				Record row = oldAdvs.get(i);
				if(!selectedAdvs.contains(row.get("adv_spaces").toString())){
					if(!"0".equals(row.get("edit_able").toString())){
//						disableOrgAdvs.add(new Object[]{row.get("id").toString()});
						disableSqlIn.append(",'"+row.get("adv_spaces").toString()+"'");
					}
				}else{
					if(!"1".equals(row.get("edit_able").toString())){
						enableOrgAdvs.add(new Object[]{row.get("id").toString()});
					}
					int index = selectedAdvs.indexOf(row.get("adv_spaces").toString());
					selectedAdvs.remove(index);
				}
			}
			if(disableSqlIn.length() > 2){//禁止当前组织及其下的组织对广告的编辑权限
				Db.update("update bp_adv_org set edit_able=0 where adv_spaces in ("+disableSqlIn.toString()+") and org_id in ("+DataOrgUtil.recordListToSqlIn(DataOrgUtil.getChildrens(orgId, true), "id")+") ");
//				DbExt.batch("update bp_adv_org set edit_able=0 where id=? ", DataOrgUtil.listTo2Array(disableOrgAdvs));
			}
			if(enableOrgAdvs.size() > 0){
				DbExt.batch("update bp_adv_org set edit_able=1 where id=? ", DataOrgUtil.listTo2Array(enableOrgAdvs));
			}
			if(selectedAdvs.size() > 0){
				List<Object[]> addAdvs = new ArrayList<Object[]>();
				Iterator<String> iteAdd = selectedAdvs.iterator();
				while(iteAdd.hasNext()){
					addAdvs.add(new Object[]{UUID.randomUUID().toString(),iteAdd.next(),orgId});
				}
				DbExt.batch("insert into bp_adv_org(id,adv_spaces,org_id,edit_able,create_date) values(?,?,?,1,now())", 
						DataOrgUtil.listTo2Array(addAdvs));
			}
			return true;
		}});
		renderJsonResult(success);
	}
=======
import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.operation.adv.model.Permission;
import com.yinfu.business.util.PageUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/operation/adv/permission", viewPath = "/page/business/operation/adv/permission")
public class PermissionController extends Controller<Device> {
	private static final String PREVIEW_PATH = "upload"+File.separator+"image"+File.separator+"adv"+File.separator;
	
	public void index() {
		SplitPage splitPages = Permission.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		setAttr("groupId", getPara("_query.groupId"));
		render("index.jsp");
	}
	
	public void edit(){
		setAttr("roleId", getPara("roleId"));
		render("edit.jsp");
	}
	public void getRoleList(){
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select id,name ","from system_role where delete_date is null order by create_date ");
		renderJson(returnData);
	}
	
	public void save() {
		if(ContextUtil.isAdmin()){
			boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
				String groupRoleId = getPara("groupRoleId");
				if(StringUtils.isNotBlank(getPara("groupRoleId"))){
					Db.update("update bp_shop_group_role set role_id=?,image='',link='',des='' where id=? ", new Object[]{getPara("roleId"),groupRoleId});
				}else{
					Db.update("insert into bp_shop_group_role(id,shop_group_id,role_id,adv_type_id,create_date) values(?,?,?,?,now())", 
							new Object[]{UUID.randomUUID().toString(),getPara("groupId"),getPara("roleId"),getPara("advId"),});
				}
				return true;
			}});
			renderJsonResult(success);
		}else{
			renderError(401);
		}
	}
	
	public void edit_old() {
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.id,bat.name,IFNULL(sgr.role_id,sr.id) role_id ");
		sql.append("from bp_adv_type bat left join bp_shop_group_role sgr on (sgr.shop_group_id=? and sgr.adv_type_id=bat.id) ");
		sql.append("join system_role sr on (bat.default_access_role = sr.unique_mark)");
		sql.append("order by bat.create_date ");
		List<Record> rec = Db.find(sql.toString(), new Object[]{getPara("id")});
		setAttr("advList", JsonKit.toJson(rec));
		setAttr("roles", JsonKit.toJson(Db.find("select id,name from system_role where delete_date is null order by create_date ")));
		setAttr("shopGroupId", getPara("id"));
		setAttr("editable", getPara("editable"));
		render("permission.jsp");
	}
	
	public void save_old() {
		if(ContextUtil.isAdmin()){
			boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
//				Db.update("delete from bp_shop_group_role where shop_group_id=? ", new Object[]{getPara("shopGroupId")});
				Map<String,Record> permissionsMap = getOldPermission();
				
				Map<String, String[]> params = getParaMap();
				List<Object[]> sqlAddParams = new ArrayList<Object[]>();
				List<Object[]> sqlEditParams = new ArrayList<Object[]>();
				Iterator<String> ite = params.keySet().iterator();
				while(ite.hasNext()){
					String paramKey = ite.next();
					String value = params.get(paramKey)[0];
					if("shopGroupId".equals(paramKey) || StringUtils.isBlank(value)){
						continue;
					}
					if(permissionsMap.containsKey(paramKey)){
						if(null != permissionsMap.get(paramKey).get("role_id")){
							if(null != getPara(paramKey) && !getPara(paramKey).equals(permissionsMap.get(paramKey).get("role_id").toString())){
								sqlEditParams.add(new Object[]{getPara(paramKey),permissionsMap.get(paramKey).get("group_role_id")});
							}
						}else{
							String id = UUID.randomUUID().toString();
							sqlAddParams.add(new Object[]{id,getPara("shopGroupId"),getPara(paramKey),paramKey});
						}
					}
				}
				if(sqlEditParams.size() > 0){
					int[] changRows = DbExt.batch("update bp_shop_group_role set role_id=?,image='',link='',des='' where id=?", listTo2Array(sqlEditParams,2));
					for(int i=0;i<changRows.length;i++){
						if(changRows[i] != 1){
							return false;
						}
					}
				}
				if(sqlAddParams.size() > 0){
					int[] changRows = DbExt.batch("insert into bp_shop_group_role(id,shop_group_id,role_id,adv_type_id,create_date) values(?,?,?,?,now())", listTo2Array(sqlAddParams,4));
					for(int i=0;i<changRows.length;i++){
						if(changRows[i] != 1){
							return false;
						}
					}
				}
				return true;
			}});
			renderJsonResult(success);
		}else{
			renderError(401);
		}
	}
	
	private Map<String,Record> getOldPermission(){
		Map<String,Record> permissionsMap = new HashMap<String,Record>();
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.id,sgr.id group_role_id,sgr.role_id ");
		sql.append("from bp_adv_type bat ");
		sql.append("left join bp_shop_group_role sgr on (sgr.shop_group_id=? and bat.id=sgr.adv_type_id) ");
		List<Record> permissions = Db.find(sql.toString(), new Object[]{getPara("shopGroupId")});
		Iterator<Record> ite = permissions.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			permissionsMap.put(rec.getStr("id"), rec);
		}
		return permissionsMap;
	}
	
	private Object[][] listTo2Array(List<Object[]> list,int size){
		Object[][] paramsArr = new Object[list.size()][size];
		for(int i=0;i<list.size();i++){
			paramsArr[i] = list.get(i);
		}
		return paramsArr;
	}

	public void setting_old(){
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.id,bat.adv_type,bat.name,ifnull(sgr.id,'') group_role_id,ifnull(sgr.image,'') image,ifnull(sgr.link,'') link,ifnull(sgr.des,'') des ");
		sql.append("from bp_adv_type bat join system_role sr on (bat.default_access_role=sr.unique_mark) ");
		sql.append("left join bp_shop_group_role sgr on (sgr.shop_group_id=? and bat.id=sgr.adv_type_id) ");
		sql.append("where (sgr.id is null and sr.id in ("+roleToSqlIn()+")) or sgr.role_id in ("+roleToSqlIn()+") ");
		sql.append("order by bat.create_date ");
		List<Record> advs = Db.find(sql.toString(), new Object[]{getPara("id")});
		boolean containsBannerAdv = false;
		boolean containsBottomAdv = false;
		boolean containsStartPageAdv = false;
		Iterator<Record> ite = advs.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			Object type = rowData.get("adv_type");
			if("adv".equals(type)){
				containsBannerAdv = true;
			}else if("adv_bottom".equals(type)){
				containsBottomAdv = true;
			}else if("adv_start".equals(type)){
				containsStartPageAdv = true;
			}
		}
		if(containsBannerAdv){
			setAttr("banner","1");
		}
		if(containsBottomAdv){
			setAttr("bottom","1");
		}
		if(containsStartPageAdv){
			setAttr("start","1");
		}
		setAttr("advs",advs);
		setAttr("groupId",getPara("id"));
		render("setting.jsp");
	}
	
	private String roleToSqlIn(){
		StringBuffer sqlIn = new StringBuffer();
		sqlIn.append("'");
		List<Record> roles = Db.find("select distinct role_id from system_user_role where user_id=? ", new Object[]{ContextUtil.getCurrentUserId()});
		Iterator<Record> ite = roles.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			sqlIn.append(rowData.get("role_id").toString()+"','");
		}
		sqlIn.append("'");
		return sqlIn.toString();
	}

	public void editAdv_old(){
		setAttr("size", getPara("size"));
		setAttr("groupRoleId", getPara("groupRoleId"));
		setAttr("advId", getPara("id"));
		setAttr("groupId", getPara("groupId"));
		System.err.println(getPara("size")+"---"+getPara("groupRoleId")+"----"+getPara("id")+"----"+getPara("groupId"));
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
	
	public void saveAdv_old(){
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
