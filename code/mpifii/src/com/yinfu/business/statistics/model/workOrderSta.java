package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;


public class workOrderSta extends Model<workOrderSta>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final workOrderSta dao = new workOrderSta();
	
	private static final Log log= LogFactory.getLog(workOrderSta.class);
	
	public SplitPage wordOrderStatis(SplitPage splitPage){
	    List pagelist=new ArrayList();
	    List<Record> wolist=getWordOrderDatas(splitPage.getQueryParam());
	    splitPage = splitPageBase(splitPage, "select u.id,u.name");
	    List userList = splitPage.getPage().getList();
	    for(int i=0;i<userList.size();i++){
			long shopSum=0;
			long woSum=0;
		    long apSum=0;
		    long routerSum=0;
	    	Record staRe = new Record();
	    	Record ure=(Record)userList.get(i);
	    	int uid=ure.get("id");
	    	String name=ure.getStr("name");
	    	if(uid!=0){
	    		for(Record wo:wolist){
	    			int userId=wo.getInt("user_id");
	    			int woType=wo.getInt("wo_type");
	    			int apNum=wo.get("ap_num")==null?0:wo.getInt("ap_num");
	    			int routerNum=wo.get("router_num")==null?0:wo.getInt("router_num");
                    if(uid==userId){
                    	woSum+=1;
                    	if(woType==1){
                    		shopSum+=1;
                    		apSum+=apNum;
                    		routerSum+=routerNum;
                    	}else if(woType==2){
                    		apSum+=apNum;
                    		routerSum+=routerNum;
                    	}
                    }
	    		}
	    	}
	    	staRe.set("name", name);
	    	staRe.set("shopSum", shopSum);
	    	staRe.set("woSum", woSum);
	    	staRe.set("apSum", apSum);
	    	staRe.set("routerSum", routerSum);
	    	pagelist.add(staRe);
	    }
	    int totalPage=0;
		totalPage = splitPage.getPage().getTotalPage();
	    splitPage.setPage(new Page(pagelist, splitPage.getPageNumber(), splitPage.getPageSize(), totalPage, pagelist.size()));
	    //splitPage.getPage().getList().addAll(pagelist);
	    return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String userName = queryParam.get("userName");
		String shopId = ContextUtil.getShopByUser();
		formSqlSb.append(" from system_user u");
		formSqlSb.append(" inner join bp_work_order w on u.id=w.user_id");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" and w.shop_id in("+shopId+")");
		}
		if(userName!=null && !userName.equals("")){
			formSqlSb.append(" and u.name='"+userName+"'");
		}
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" +startDate+"'");
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" +endDate+"'");
		}
		formSqlSb.append(" group by u.id,u.name");
	}
	
	public List<Record> getWordOrderDatas(Map<String, String> queryParam){
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String userName = queryParam.get("userName");
		String shopId = ContextUtil.getShopByUser();
		StringBuffer sql = new StringBuffer("");
		sql.append("select w.wo_id,w.wo_type,w.user_id,w.ap_num,w.router_num,DATE_FORMAT(w.created_date,'%Y-%m-%d') as created_date");
		sql.append(" from bp_work_order w");
		sql.append(" where 1=1");
		if(shopId!=null && !shopId.equals("")){
			sql.append(" and w.shop_id in("+shopId+")");
		}
		if(userName!=null && !userName.equals("")){
			int userId=getUserIdByName(userName);
			sql.append(" and w.user_id="+userId);
		}
		if(startDate!=null && !startDate.equals("")){
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" +startDate+"'");
		}
		if(endDate!=null && !endDate.equals("")){
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" +endDate+"'");
		}
		return Db.find(sql.toString());
	}

	/**
	 * @param userName
	 * @return
	 */
	public int getUserIdByName(String userName){
		Record user = Db.findFirst("select id from system_user where name=?", new Object[]{userName});
		int rel = user.getInt("id");
		return rel;
	}

}
