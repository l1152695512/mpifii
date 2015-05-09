package com.yinfu.business.application.game.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
@TableBind(tableName = "bp_game")
public class Game extends Model<Game> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static final Game dao = new Game();
	//@formatter:off 
	/**
	 * Title: getGameList
	 * Description:获得游戏列表
	 * Created On: 2014年9月26日 下午4:37:41
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getGameList(SplitPage splitPage) {
		String sql = "SELECT s.* ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_game s  ");
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
}
