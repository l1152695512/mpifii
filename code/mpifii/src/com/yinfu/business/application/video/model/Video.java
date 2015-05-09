package com.yinfu.business.application.video.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao
 *
 */
@TableBind(tableName = "bp_video")
public class Video extends Model<Video> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Video dao = new Video();
	//@formatter:off 
	/**
	 * Title: getVideoList
	 * Description:视频列表
	 * Created On: 2014年9月25日 下午5:23:06
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getVideoList(SplitPage splitPage) {
		String sql = "SELECT  ab.id,ab.name,ab.link,GROUP_CONCAT(ac.`name`) AS typename,ab.status ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_video AS ab,bp_video_type AS ac  ");
		formSqlSb.append(" WHERE INSTR(ab.type,ac.id) AND ab.delete_date IS NULL ");
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
		formSqlSb.append(" GROUP BY ab.id,ab.NAME  ");
	}
	//@formatter:off 
	/**
	 * Title: getVideoType
	 * Description:获得视频类型
	 * Created On: 2014年9月25日 下午6:24:07
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getVideoType() {
		String sql = " select id,name from bp_video_type where delete_date is null ";
		return Db.find(sql);
	}

}
