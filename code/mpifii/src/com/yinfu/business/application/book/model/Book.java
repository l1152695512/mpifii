package com.yinfu.business.application.book.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
@TableBind(tableName = "bp_book")
public class Book extends Model<Book> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Book dao = new Book();
	//@formatter:off 
	/**
	 * Title: getBookList
	 * Description:获得小说列表
	 * Created On: 2014年9月26日 下午3:21:17
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getBookList(SplitPage splitPage) {
		String sql = "SELECT s.*,t.name as typename,th.`name` AS themname";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_book s LEFT JOIN bp_book_type t ON t.id = s.type  LEFT JOIN bp_book_theme th ON s.`theme` = th.`id` ");
		formSqlSb.append("where 1=1 and s.delete_date is null ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and s."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and s."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		formSqlSb.append(" order BY s.create_date desc  ");
	}
	//@formatter:off 
	/**
	 * Title: getBookType
	 * Description:获得小说分类
	 * Created On: 2014年9月26日 下午3:21:15
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getBookType() {
		String sql = " select id,name from bp_book_type where delete_date is null ";
		return Db.find(sql);
	}
	//@formatter:off 
	/**
	 * Title: getBookThemes
	 * Description:获得小说主题
	 * Created On: 2014年9月26日 下午3:21:11
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getBookThemes() {
		String sql = " select id,name from bp_book_theme where delete_date is null ";
		return Db.find(sql);
	}
}
