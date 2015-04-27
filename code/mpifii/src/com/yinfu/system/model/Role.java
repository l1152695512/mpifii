package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.model.tree.Tree;
import com.yinfu.shiro.ShiroCache;

@TableBind(tableName = "system_role")
public class Role extends Model<Role>
{
	private static final long serialVersionUID = -5747359745192545106L;
	public static Role dao = new Role();

	public List<String> getResUrl(String name)
	{
		return Res.dao.getAttr(sql("system.res.getResUrl"), "url", name);

	}

	public List<Role> getRole(int uid)
	{
		return Role.dao.find(sql("system.role.getRole"), uid);
	}

	@Override
	public List<Role> list()
	{
		List<Role> list = Role.dao.find(sql("system.role.list"));

		for (Role r : list)
		{
			List<Res> res = Res.dao.getRes(r.getId());
			r.put("res_ids", ListUtil.listToString(res, "id"));
			r.put("res_names", ListUtil.listToString(res, "name"));
		}

		return list;
	}

	public List<Tree> getTree(Integer id, Integer passId)
	{
		// 根据用户角色来获取 列表
		List<Tree> trees = new ArrayList<Tree>();

		for (Role res : getChild(id))
		{
			if (res.getId().equals(passId)) continue;
			Tree tree = new Tree(res.getId(), res.getPid(), res.getName(), res.getIconCls(), res, false);

			tree.children = getTree(res.getId(), passId);
			if (tree.children.size() > 0) tree.changeState();

			trees.add(tree);
		}
		return trees;
	}

	public List<Role> getChild(Integer id)
	{
		if (id == null) return dao.list(" where pid is null order by seq");
		return dao.list(" where pid = ? order by seq ", id);

	}

	public boolean grant(int roleId, String resIds)
	{
		boolean result=false;
		if (!Validate.isEmpty(resIds)) result= Res.dao.batchAdd(roleId, resIds);
		ShiroCache.clearAuthorizationInfoAll();
		return result;
	}

	/***
	 * 批量授权 会先删除所有权限再授权
	 * 
	 * @param roleId
	 * @param resIds
	 * @return
	 */
	public boolean batchGrant(int roleId, String resIds)
	{
		boolean result = Db.deleteById("system_role_res", "role_id", roleId);
		if (!Validate.isEmpty(resIds)) result = Res.dao.batchAdd(roleId, resIds);

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	//@formatter:off 
	/**
	 * Title: childNodeData
	 * Description:节点数据加载
	 * Created On: 2014年8月4日 下午5:36:17
	 * @author JiaYongChao
	 * <p>
	 * @param id
	 * @return 
	 */
	//@formatter:on
	public String childNodeData(String id,HttpServletRequest request) {
		String sql = null;
		List<Role> list = null;
		if(null != id){
			sql = " select id, name, iconCls from system_role where pid = ? order by seq asc ";
			list = Role.dao.find(sql, id);
		}else{
			String userId = ContextUtil.getCurrentUserId();
			Record rd = Db.findFirst("select role_id from system_user_role where user_id=?",new Object[]{userId});
			sql = " select id, name, iconCls from system_role where id=? order by seq asc ";
			list = Role.dao.find(sql,new Object[]{rd.getInt("role_id")});
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int size = list.size() - 1;
		for (Role role : list) {
			sb.append(" { ");
			sb.append(" id : '").append(role.get("id")).append("', ");
			sb.append(" name : '").append(role.getStr("name")).append("', ");
			sb.append(" isParent : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append(request.getContextPath()+"/js/jsFile/zTree/css/zTreeStyle/img/diy/").append(role.getStr("iconCls")).append("' ");
			sb.append(" }");
			if(list.indexOf(role) < size){
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	//@formatter:off 
	/**
	 * Title: findList
	 * Description:角色查询
	 * Created On: 2014年9月10日 上午10:26:42
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage findList(SplitPage splitPage) {
		String sql = "SELECT r.`id` AS roleid,r.`name` AS rolename,r.`des` AS des,r.`seq` AS status,IF(u.`id`,COUNT(*),0) AS counts ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM system_role r LEFT JOIN system_user_role ur ON   r.`id` = ur.`role_id` LEFT JOIN SYSTEM_USER u    ON ur.`user_id` = u.`id` ");
		formSqlSb.append(" WHERE  r.delete_date IS NULL  GROUP BY r.`id` ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and r."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and r."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
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


	//@formatter:off 
	/**
	 * Title: setRoleAuth
	 * Description:设置角色权限
	 * Created On: 2014年11月18日 下午3:11:51
	 * @author JiaYongChao
	 * <p>
	 * @param id
	 * @param moduleIds
	 * @param resIds 
	 */
	//@formatter:on
	public void setRoleAuth(String id, String moduleIds, String resIds) {
		Role role = Role.dao.findById(id);
		role.set("moduleids", moduleIds).set("operatorids", resIds).update();
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
		String sql="SELECT s.name as names,GROUP_CONCAT(t.res_id) AS moduleids,t.role_id as id FROM system_role_res t left JOIN system_role s ON s.`id` = role_id WHERE t.role_id=?";
		Record r = Db.findFirst(sql,id);
		return r;
	}
}
