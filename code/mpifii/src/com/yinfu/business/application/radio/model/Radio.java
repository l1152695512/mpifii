package com.yinfu.business.application.radio.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_audio")
public class Radio extends Model<Radio> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Radio dao = new Radio();
	//@formatter:off 
	/**
	 * Title: getRadioList
	 * Description:获得音乐列表
	 * Created On: 2014年9月26日 上午11:25:28
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getRadioList(SplitPage splitPage) {
		String sql = "SELECT s.*,t.name as typename";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_audio s left join bp_audio_type t on t.id = s.type ");
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
	 * Title: getRadioType
	 * Description:获得音乐分类
	 * Created On: 2014年9月26日 上午11:26:12
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getRadioType() {
		String sql = " select id,name from bp_audio_type where delete_date is null ";
		return Db.find(sql);
	}
	
	public  void test(){
		String tempSql =" select city_code,city_name,parent_code from temptable "; 
		List<Record> tempList = Db.find(tempSql);//临时数据表list 
		String dataSql = " select city_code,city_name,parent_code from datatable";
		List<Record> dataList =Db.find(dataSql);
		for (int i = 0; i < dataList.size(); i++) {
			if (tempList.containsAll(dataList)){
				tempList.removeAll(dataList);
			}
			
		}
	}
}
