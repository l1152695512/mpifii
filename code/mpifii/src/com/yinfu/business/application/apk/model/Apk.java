package com.yinfu.business.application.apk.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
@TableBind(tableName = "bp_apk")
public class Apk extends Model<Apk> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static final Apk dao = new Apk();
	//@formatter:off 
	/**
	 * Title: getApkList
	 * Description:获得APK列表
	 * Created On: 2014年9月26日 下午4:50:47
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getApkList(SplitPage splitPage) {
		String sql = "SELECT s.*,th.name as themename ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_apk s left join bp_apk_theme th on th.id = s.theme ");
		formSqlSb.append("where 1=1 and s.delete_date is null   ");
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
	 * Title: getApkThemes
	 * Description:获得apk主题
	 * Created On: 2014年9月26日 下午4:53:32
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getApkThemes() {
		String sql = " select id,name from bp_apk_theme where delete_date is null ";
		return Db.find(sql);
	}
}
