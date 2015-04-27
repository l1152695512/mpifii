
package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.sun.xml.internal.ws.api.server.Module;
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.model.tree.Tree;

@TableBind(tableName = "system_res")
public class Res extends Model<Res> {
	private static final long serialVersionUID = 9204284399513186930L;
	
	public static Res dao = new Res();
	
	/** 
	 * type define
	 */
	public static int TYPE_MEUE = 1;
	public static final int TYPE_PERMISSION = 2;
	
	/**
	 * 转化为 easyui Tree 对象
	 * 
	 * @param type
	 * @return
	 */
	public List<Tree> getTree(Integer pid, int type, Integer passId) {
		// 根据用户角色来获取 列表
		List<Tree> trees = new ArrayList<Tree>();
		for (Res res : getChild(pid, type)) {
			
			if (res.getId().equals(passId))
				continue;
			
			Tree tree = new Tree(res.getId(), res.getPid(), res.getName(), res.getIconCls(), res, false);
			tree.children = getTree(res.getId(), type, passId);
			if (tree.children.size() > 0)
				tree.changeState();
			
			trees.add(tree);
		}
		
		return trees;
	}
	
	public List<String> getUrls() {
		return dao.getAttr(sql("system.res.getUrls"), "url");
	}
	
	public List<Res> getChild(Integer id, Integer type) {
		ShiroExt ext = new ShiroExt();
		List<Res> list = null;
		
		if (type == null)
			return dao.list("where pid =?", id);
		else if (id == null && type == 1)
			list = dao.listOrderBySeq(" where  pid is null and type =? ", type);
		else if (id == null && type == 2)
			list = dao.listOrderBySeq("where pid is null");
		else if (type == 2)
			list = dao.listOrderBySeq(" where  pid =? and type =? ", id, type);
		else if (type == 1)
			list = dao.listOrderBySeq(" where  pid =?  ", id);
		
		if (id == null)
			return list;
		else if (TYPE_PERMISSION == type)
			return list;
		else {
			ListIterator<Res> itor = list.listIterator();
			while (itor.hasNext()) {
				Res r = itor.next();
				if (r.getStr("url") == null)
					continue;
				if (!ext.hasPermission(r.getStr("url"))) {
					itor.remove();
				}
			}
		}
		
		return list;
	}
	
	/***
	 * 通过 role id 获得 res
	 * 
	 * @param r
	 * @return
	 */
	public List<Res> getRes(int id) {
		return dao.find(sql("system.res.getRes"), id);
		
	}
	
	public boolean batchAdd(int roleId, String resIds) {
		Object[][] params = ListUtil.stringToArray(roleId, resIds);
		
		Db.batch("insert into system_role_res(role_id,res_id)  values(?,?)", params, params.length);
		
		return true;
	}

	//@formatter:off 
	/**
	 * Title: findList
	 * Description:菜单列表
	 * Created On: 2014年10月14日 下午3:21:24
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.* ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from system_res t ");
		formSqlSb.append("where 1=1   ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and t."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and t."+key+"='"+queryParam.get(key)+"' ");
				}
			}	
		}
		formSqlSb.append("order by t.seq ");
	}

	//@formatter:off 
	/**
	 * Title: childNodeData
	 * Description:获取子节点数据
	 * Created On: 2014年11月17日 下午6:06:28
	 * @author JiaYongChao
	 * <p>
	 * @param moduleIds
	 * @return 
	 */
	//@formatter:on
	public String childNodeData(String moduleIds) {
		List<Res> listModule = new ArrayList<Res>();
		List<Res> operatorList = new ArrayList<Res>();
		if (null == moduleIds) {
			// 1.模块功能初始化调用
			String sql = " select * from  system_res  where pid is null  ";
					/*ToolSqlXml.getSql("pingtai.operator.rootModule");*/
			listModule = Res.dao.find(sql);
		} else if (null != moduleIds) {
			moduleIds = moduleIds.replace("module_", "");
			// 2.通用子节点查询
			String sqlModule = "select * from system_res where pid=? and type=2 ";
					/*ToolSqlXml.getSql("pingtai.operator.childModule");*/
			listModule = Res.dao.find(sqlModule, moduleIds);
			String sqlOperator = "select * from system_res where pid=? and type=3 ";
					/*ToolSqlXml.getSql("pingtai.operator.byModuleIds");*/
			operatorList = Res.dao.find(sqlOperator, moduleIds);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int operatorSize = operatorList.size();
		int operatorIndexSize = operatorSize - 1;
		for (Res res : operatorList) {
			sb.append(" { ");
			sb.append(" id : '").append("operator_").append(res.get("id")).append("', ");
			sb.append(" name : '").append(res.getStr("names")).append("', ");
			sb.append(" isParent : false, ");
			sb.append(" checked : false, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '/jsFile/zTree/css/zTreeStyle/img/diy/5.png' ");
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
			sb.append(" id : '").append("module_").append(res.get("id")).append("', ");
			sb.append(" name : '").append(res.getStr("name")).append("', ");
			sb.append(" isParent : true,");
		/*	sb.append(" isParent : ").append(res.getStr("isparent")).append(", ");*/
			sb.append(" nocheck : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '/jsFile/zTree/css/zTreeStyle/img/diy/").append(res.getStr("images")).append("' ");
			sb.append(" }");
			if (listModule.indexOf(res) < moduleIndexSize) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
