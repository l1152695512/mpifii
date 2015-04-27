
package com.yinfu.jbase.jfinal.ext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.jbase.util.L;
import com.yinfu.jbase.util.Txt;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
public class Model<M extends com.jfinal.plugin.activerecord.Model<M>> extends com.jfinal.plugin.activerecord.Model<M> {
	
	private static final long serialVersionUID = 8924183967602127690L;
	
	/***
	 * 用来当 缓存名字 也用来 生成 简单sql
	 */
	
	public String tableName;
	
	/***
	 * 反射获取 注解获得 tablename
	 */
	public Model() {
		TableBind table = this.getClass().getAnnotation(TableBind.class);
		
		if (table != null)
			tableName = table.tableName();
		
	}
	
	// @formatter:off
	/**
	 * Title: update Description: Created On: 2014年7月17日 上午9:56:19
	 * 
	 * @author JiaYongChao
	 *         <p>
	 * @param key
	 * @param value
	 * @param id
	 * @return
	 */
	// @formatter:on
	public boolean update(String key, Object value, Object id) {
		
		return Db.update("update " + tableName + " set " + key + "=? where id =?", value, id) > 0;
	}
	
	// @formatter:off
	/**
	 * Title: findByName Description:通过名字查找 Created On: 2014年7月17日 上午9:56:17
	 * 
	 * @author JiaYongChao
	 *         <p>
	 * @param name
	 * @return
	 */
	// @formatter:on
	public M findByName(String name) {
		return findFirst("select * from " + tableName + " where name =? ", name);
	}
	
	// @formatter:off
	/**
	 * Title: checkNameExist Description: Created On: 2014年7月17日 上午9:56:23
	 * 
	 * @author JiaYongChao
	 *         <p>
	 * @param name
	 * @return
	 */
	// @formatter:on
	public boolean checkNameExist(String name) {
		
		return findFirst("select * from " + tableName + " where name ='" + name + "'") != null;
		
	}
	
	/***
	 * if empty remove the attr
	 * 
	 * @param attr
	 */
	public Model<M> emptyRemove(String attr) {
		if (get(attr) == null)
			remove(attr);
		
		return this;
	}
	
	public Model<M> emptyZreo(String attr) {
		if (get(attr) == null)
			set(attr, 0);
		return this;
	}
	
	/***
	 * 删除自己的同时 删除 所有 子节点 属性名 必需为pid
	 * 
	 * @param para
	 * @return
	 */
	public boolean deleteByIdAndPid(Integer id) {
		boolean result = deleteById(id);
		
		List<Model> list = (List<Model>) list("where pid=?", id);
		
		for (Model m : list) {
			deleteByIdAndPid(m.getId());
			
			Db.update("delete from " + tableName + " where pid=? ", id);
		}
		
		return result;
	}
	
	/***
	 * ids 必需为 连续的 1，2，3 这样子
	 * 
	 * @param ids
	 */
	public boolean batchDelete(String ids) {
		if (Validate.isEmpty(ids))
			return false;
		return Db.update("delete from " + tableName + " where id in (" + ids + ")") > 0;
		
	}
	
	//@formatter:off 
	/**
	 * Title: deleteById
	 * Description:通过id删除，假删除
	 * Created On: 2014年7月23日 上午10:39:58
	 * @author JiaYongChao
	 * <p> 
	 * @return 
	 */
	//@formatter:on
	public boolean deleteById() {
		return this.setDate("delete_date").update();
	}
	
	// @formatter:off
	/**
	 * Title: saveOrUpdate Description: Created On: 2014年7月17日 上午9:56:38
	 * 
	 * @author JiaYongChao
	 *         <p>
	 * @param model
	 */
	// @formatter:on
	public void saveOrUpdate(Model<M> model) {
		if (model.getId() != null)
			model.update();
		else
			model.save();
	}
	
	public boolean pidIsChild(Integer id, Integer pid) {
		boolean result = false;
		if (pid != null) {
			List<Model> list = (List<Model>) list(" where  pid =?  ", id);
			
			if (list.size() == 0)
				result = false;
			
			for (Model r : list) {
				if (pid.equals(r.getId())) {
					result = true;
					return result;
				} else {
					
					if (pidIsChild(r.getId(), pid)) {
						result = true;
						L.i("result =" + result);
						
						return result;
					}
				}
				
			}
		}
		
		return result;
		
	}
	/**
	 * 分页
	 * @param splitPage
	 * @param select
	 * @return
	 */
	public SplitPage splitPageBase(SplitPage splitPage, String select){
		// 接收返回值对象
		StringBuilder formSqlSb = new StringBuilder();
		List<Object> paramValue = new LinkedList<Object>();
		// 调用生成from sql，并构造paramValue
		makeFilter(splitPage.getQueryParam(), formSqlSb, paramValue);
		// 行级：过滤
		rowFilter(formSqlSb);
		// 排序
		String orderColunm = splitPage.getOrderColunm();
		String orderMode = splitPage.getOrderMode();
		if(null != orderColunm && !orderColunm.isEmpty() && null != orderMode && !orderMode.isEmpty()){
			formSqlSb.append(" order by ").append(orderColunm).append(" ").append(orderMode);
		}
		String formSql = formSqlSb.toString();
		Page<?> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select, formSql, paramValue.toArray());
		splitPage.setPage(page);
		return splitPage;
	}
	/**
	 * 行级：过滤
	 * @return
	 */
	protected void rowFilter(StringBuilder formSqlSb){
		
	}

	/**
	 * 查询条件过滤
	 * @return
	 */
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue){
		
	}
	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list() {
		
		return find(" select *from " + tableName);
	}
	
	public List<M> listOrderBySeq() {
		
		return list(" order by seq");
	}
	
	public List<M> listOrderBySeq(String where, Object... params) {
		
		return list(where + " order by seq", params);
	}
	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(String where) {
		
		return find(" select *from " + tableName + " " + where);
	}
	
	/***
	 * @return
	 */
	public List<M> list(String where, Object... params) {
		
		return find(" select *from " + tableName + " " + where, params);
	}
	
	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(int limit) {
		
		return find(" select *from " + tableName + " limit " + limit);
	}
	
	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(int page, int size) {
		
		if (page < 1)
			page = 0;
		return find(" select *from " + tableName + " limit " + (page - 1) * size + "," + size);
	}
	public List<M> findByCache(String sql) {
		
		return super.findByCache(tableName, sql, sql);
	}
	
	public List<M> findByCache(String sql, Object... params) {
		return super.findByCache(tableName, sql, sql, params);
	}
	
	public boolean saveAndDate() {
		
		return this.setDate("date").save();
	}
	
	//@formatter:off 
	/**
	 * Title: saveAndCreateDate
	 * Description:保存数据时添加create_date
	 * Created On: 2014年7月23日 上午10:22:55
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public boolean saveAndCreateDate() {
		return this.setDate("create_date").save();
	}
	
	//@formatter:off 
	/**
	 * Title: updateAndModifyDate
	 * Description:修改数据时添加modify_date
	 * Created On: 2014年7月23日 上午10:23:41
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public boolean updateAndModifyDate() {
		
		return this.setDate("modify_date").update();
	}
	public boolean updateAndSetDateNow(String fieldName) {
		return this.setDate(fieldName).update();
	}
	
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
	
	public M setDate(String date) {
		return this.set(date, new Timestamp(System.currentTimeMillis()));
		
	}
	
	/***
	 * 把 model 转化为 list 找到其中的单个属性
	 * 
	 * @param sql
	 * @param attr
	 * @return
	 */
	public List<String> getAttr(String sql, String attr) {
		
		List<String> list = new ArrayList<String>();
		
		for (M t : find(sql)) {
			
			list.add(t.getStr(attr));
		}
		return list;
		
	}
	
	/***
	 * 把 model 转化为 list 找到其中的单个属性
	 * 
	 * @param sql
	 * @param attr
	 * @return
	 */
	public List<String> getAttr(String sql, String attr, String... param) {
		
		List<String> list = new ArrayList<String>();
		
		for (M t : find(sql, param)) {
			
			list.add(t.getStr(attr));
		}
		return list;
		
	}
	
	public long getCount(String sql) {
		sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by"))
			sql = Txt.split(sql, "order by")[0];
		
		return findFirst(" select count(*) as c from" + sql).getLong("c");
	}
	
	public Long getCount(String sql, String countName, Object... params) {
		return findFirst(sql, params).getLong(countName);
	}
	
	/***
	 * 取值
	 * 
	 * @return
	 */
	public Long getCount() {
		
		return getLong("count");
	}
	
	public Integer getId() {
		return getInt("id");
	}
	
	public Integer getPid() {
		
		return getInt("pid");
	}
	
	public Integer getType() {
		return getInt("type");
	}
	
	/***
	 * return getStr("name");
	 * 
	 * @return
	 */
	public String getName() {
		return getStr("name");
	}
	
	/***
	 * return getStr("pwd");
	 * 
	 * @return
	 */
	public String getPwd() {
		return getStr("pwd");
	}
	
	/***
	 * return getStr("des");
	 * 
	 * @return
	 */
	public String getDes() {
		
		return getStr("des");
	}
	
	/***
	 * return getStr("date");
	 * 
	 * @return
	 */
	public String getDate() {
		
		return getStr("date");
	}
	
	public String getCreateDate() {
		
		return getStr("create_date");
		
	}
	
	public String getModifyDate() {
		
		return getStr("modify_date");
	}
	
	public String getIcon() {
		return getStr("icon");
	}
	
	public String getIconCls() {
		return getStr("iconCls");
	}
	
	public Integer getStatus() {
		return getInt("status");
	}
	
	public static String sql(String key) {
		
		return SqlKit.sql(key);
	}
}
