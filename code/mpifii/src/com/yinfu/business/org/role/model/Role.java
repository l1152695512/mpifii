package com.yinfu.business.org.role.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.org.shop.model.Shop;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Res;

@TableBind(tableName = "system_role")
public class Role extends Model<Role>
{
	private static final long serialVersionUID = -5747359745192545106L;
	public static Role dao = new Role();


	public List<Role> getRole(int uid)
	{
		return Role.dao.find(sql("system.role.getRole"), uid);
	}

	public SplitPage getRoleList(SplitPage splitPage) {
		String sql = "SELECT * ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM system_role ");
		formSqlSb.append(" WHERE  delete_date IS NULL ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and "+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and "+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		formSqlSb.append(" GROUP BY id ");
	}

	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存信息
	 * Created On: 2014年10月28日 上午10:05:21
	 * @author JiaYongChao
	 * <p>
	 * @param pid
	 * @param name
	 * @param orderid
	 * @return 
	 */
	//@formatter:on
	public String save(String pid, String name, int orderid) {
		Role prole = Role.dao.findById(pid);
		prole.set("isParent", "true").update();
		
		String images = "";
		if(orderid < 2 || orderid > 9){
			orderid = 2;
			images = "2.png";
		}else{
			images = orderid + ".png";
		}
		
		Role role = new Role();
		role.set("isParent", "false");
		role.set("pid", pid);
		role.set("seq", orderid);
		role.set("name", name);
		role.set("iconCls", images);
		role.save();
		return role.get("id").toString();
	}

	//@formatter:off 
	/**
	 * Title: deletes
	 * Description:
	 * Created On: 2014年11月17日 上午11:18:58
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public boolean deletes(String id ) {
		String sql =" select count(*) as counts from system_role where pid = ?";
		Record record = Db.findFirst(sql, id);
		Long counts = record.getNumber("counts").longValue();
	    if(counts > 1){
	    	return false;
	    }
	    // 删除
	    dao.deleteById(id);
	    return true;
	}


	public void setRoleAuth(final String roleId,final  String resIds) {
		Db.tx(new IAtom() {@Override
			public boolean run() throws SQLException {
			boolean success = false;
			if(roleId != null && resIds != null){
				List<String> sqlList = new ArrayList<String>();
				sqlList.add("delete from system_role_res where role_id="+roleId);
				String[] src = resIds.split(",");
				for(int i=0;i<src.length;i++){
					sqlList.add("insert into system_role_res(res_id,role_id) values("+src[i]+","+roleId+")");
				}
				int[] result = Db.batch(sqlList, sqlList.size());
				if(result.length>0){
					success = true;
				}
				return success;
			}
			return success;
			}
		});
	}
	
	
	public void setRoleShopAuth(final String roleId,final  String shopIds) {
		Db.tx(new IAtom() {@Override
			public boolean run() throws SQLException {
			boolean success = false;
			if(roleId != null && shopIds != null){
				List<String> sqlList = new ArrayList<String>();
				sqlList.add("delete from system_role_shop where role_id="+roleId);
				String[] src = shopIds.split(",");
				for(int i=0;i<src.length;i++){
					sqlList.add("insert into system_role_shop(shop_id,role_id) values("+src[i]+","+roleId+")");
				}
				int[] result = Db.batch(sqlList, sqlList.size());
				if(result.length>0){
					success = true;
				}
				return success;
			}
			return success;
			}
		});
	}

	//@formatter:off 
	/**
	 * Title: findRoleAuth
	 * Description:查找角色拥有的权限
	 * Created On: 2014年11月24日 下午2:35:05
	 * @author JiaYongChao
	 * <p>
	 * @param id
	 * @return 
	 */
	//@formatter:on
	public Record findRoleAuth(String id) {
		String sql=" SELECT  r.name AS NAMES,GROUP_CONCAT(res.`id`) AS moduleids,r.`id` AS id FROM system_res res LEFT JOIN system_role_res rr ON res.`id` = rr.`res_id` LEFT JOIN system_role r ON r.`id` = rr.`role_id` WHERE rr.`role_id` = ?";
		Record r = Db.findFirst(sql,id);
		return r;
	}
	
	public String childNodeData(String moduleIds,String roleId,String rootId,HttpServletRequest request) {
		List<Res> listModule = new ArrayList<Res>();
		List<Res> operatorList = new ArrayList<Res>();
		String left = "";
		if(!roleId.equals(rootId)){
			left = "left";
		}
		
		if (null == moduleIds) {
			// 1.模块功能初始化调用
			String sql = " SELECT a.* FROM system_res a join system_role_res bb on a.id=bb.res_id and bb.role_id="+rootId+" "+left+" join system_role_res b ON a.id=b.res_id  AND b.role_id= "+roleId +" WHERE a.pid IS null";
					/*ToolSqlXml.getSql("pingtai.operator.rootModule");*/
			listModule = Res.dao.find(sql);
		} else if (null != moduleIds) {
			moduleIds = moduleIds.replace("module_", "");
			// 2.通用子节点查询
			String sqlModule = "SELECT a.* FROM  system_res a join system_role_res bb on a.id=bb.res_id and bb.role_id="+rootId+" "+left+" join system_role_res b ON a.id=b.res_id  AND b.role_id= "+roleId +" WHERE a.pid=? and a.type=2 ";
					/*ToolSqlXml.getSql("pingtai.operator.childModule");*/
			listModule = Res.dao.find(sqlModule, moduleIds);
			String sqlOperator = "SELECT a.* FROM  system_res a join system_role_res bb on a.id=bb.res_id and bb.role_id="+rootId+" "+left+" join system_role_res b ON a.id=b.res_id AND b.role_id= "+roleId +" WHERE a.pid=? and a.type=3 ";
					/*ToolSqlXml.getSql("pingtai.operator.byModuleIds");*/
			operatorList = Res.dao.find(sqlOperator, moduleIds);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int operatorSize = operatorList.size();
		int operatorIndexSize = operatorSize - 1;
		for (Res res : operatorList) {
			sb.append(" { ");
			sb.append(" id : '").append(res.get("id")).append("', ");
			sb.append(" name : '").append(res.getStr("names")).append("', ");
			sb.append(" isParent : false, ");
//			if(res.getInt("checked") != null && res.getInt("checked")==2){
//				sb.append(" checked : true, ");
//			}else{
//				sb.append(" checked : false, ");
//			}
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append(request.getContextPath()+"/js/jsFile/zTree/css/zTreeStyle/img/diy/").append(res.getStr("iconCls")).append("' ");
			sb.append(" }");
			if (operatorList.indexOf(res) < operatorIndexSize) {
				sb.append(", ");
			}
		}
		int moduleSize = listModule.size();
		int moduleIndexSize = moduleSize - 1;
		if (operatorSize > 0 && moduleSize > 0) {
			sb.append(", ");
		}
		for (Res res : listModule) {
			sb.append(" { ");
			sb.append(" id : '").append(res.get("id")).append("', ");
			sb.append(" name : '").append(res.getStr("name")).append("', ");
			sb.append(" isParent : true,");
		/*	sb.append(" isParent : ").append(res.getStr("isparent")).append(", ");*/
//			sb.append(" nocheck : true, ");
			if(res.getInt("checked") != null && res.getInt("checked")==2){
				sb.append(" checked : true, ");
			}else{
				sb.append(" checked : false, ");
			}
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append(request.getContextPath()+"/js/jsFile/zTree/css/zTreeStyle/img/diy/").append(res.getStr("iconCls")).append("' ");
			sb.append(" }");
			if (listModule.indexOf(res) < moduleIndexSize) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	
	public String childNodeShopData(String moduleIds,String orgId,String roleId,HttpServletRequest request) {
		List<Shop> listModule = new ArrayList<Shop>();
		List<Shop> operatorList = new ArrayList<Shop>();
		if (null == moduleIds) {
			// 1.模块功能初始化调用
			String sql = " SELECT a.*,b.id checked FROM bp_shop a LEFT JOIN system_role_shop b ON a.id=b.shop_id AND b.role_id="+roleId+" AND 1=1 WHERE a.org_id="+orgId;
			listModule = Shop.dao.find(sql);
		}else{
			
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int operatorSize = operatorList.size();
		int operatorIndexSize = operatorSize - 1;
		for (Shop shop : operatorList) {
			sb.append(" { ");
			sb.append(" id : '").append(shop.get("id")).append("', ");
			sb.append(" name : '").append(shop.getStr("name")).append("', ");
			sb.append(" isParent : false, ");
//			if(shop.getInt("checked") != null && shop.getInt("checked")==1){
//				sb.append(" checked : true, ");
//			}else{
//				sb.append(" checked : false, ");
//			}
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" }");
			if (operatorList.indexOf(shop) < operatorIndexSize) {
				sb.append(", ");
			}
		}
		int moduleSize = listModule.size();
		int moduleIndexSize = moduleSize - 1;
		if (operatorSize > 0 && moduleSize > 0) {
			sb.append(", ");
		}
		for (Shop shop : listModule) {
			sb.append(" { ");
			sb.append(" id : '").append(shop.get("id")).append("', ");
			sb.append(" name : '").append(shop.getStr("name")).append("', ");
			sb.append(" isParent : true,");
		/*	sb.append(" isParent : ").append(res.getStr("isparent")).append(", ");*/
//			sb.append(" nocheck : true, ");
			if(shop.getInt("checked") != null){
				sb.append(" checked : true, ");
			}else{
				sb.append(" checked : false, ");
			}
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" }");
			if (listModule.indexOf(shop) < moduleIndexSize) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
