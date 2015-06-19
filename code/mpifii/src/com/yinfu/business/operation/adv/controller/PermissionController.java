
package com.yinfu.business.operation.adv.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
}
