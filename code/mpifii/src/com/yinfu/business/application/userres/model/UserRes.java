package com.yinfu.business.application.userres.model;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
@TableBind(tableName="bp_user_res")
public class UserRes extends Model<UserRes>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final UserRes dao=new UserRes();
	
	public static final String OPERA_TYPE_A="A";//新增功能
	
	public static final String OPERA_TYPE_U="U";//修改
	
	public static final String OPERA_TYPE_D="D";//删除
	
	public static final String OPERA_TYPE_E="E";//导出
	
	public static final String OPERA_TYPE_I="I";//导入
	
	/**
	 * @param ur
	 * @return
	 */
	public boolean setUserRel(UserRes ur){
		
		return false;
	}
	
	/**
	 * 根据用户编号得到系统菜单列表
	 * @param userId
	 * @return json 菜单树数据
	 */
	public String getSysResByUser(String userId,String loginUserId){
		String sql="select distinct res_id from system_role_res where role_id =(select role_id from system_user_role where user_id="+loginUserId+")";
		List<Record> residlist=Db.find(sql);
		String resId="";
		for(int i=0;i<residlist.size();i++){
			Record res=residlist.get(i);
			if(i!=residlist.size()-1){
				resId+=res.get("res_id")+",";	
			}else{
				resId+=res.get("res_id");
			}
		}
		List<Record> muenlist=Db.find("select id,ifnull(pid,0) pid,name from system_res where id in("+resId+")");
		List<Record> muenChildlist=Db.find("select concat('o_',id) id,res_id as pid,opera_name name from bp_operation_res where res_id in("+resId+")");
		muenlist.addAll(muenChildlist);
		List<Record> reslist=Db.find("select res_id,opera_id from bp_user_res where user_id="+userId+"");
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0;i<muenlist.size();i++){
			Record muen=muenlist.get(i);
			String muenId=String.valueOf(muen.get("id"));
			long muenPid=Long.parseLong(String.valueOf(muen.get("pid")));
			sb.append(" { ");
			sb.append(" id : '").append(muenId).append("', ");
			sb.append(" pId : '").append(muenPid).append("', ");
			sb.append(" name : '").append(muen.getStr("name")).append("', ");
			if(checkUserIsRes(reslist,muenId,String.valueOf(muenPid))){
				sb.append(" checked:true,");
				sb.append(" open:true,");
				
			}
			if(muenPid==0){
				sb.append(" open:true,");
				sb.append(" nocheck:true,");
			}
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" }");
			if (i!=muenlist.size()-1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 判断这个用户是否关联该菜单
	 * @param userId
	 * @param resId
	 * @return
	 */
	public boolean checkUserIsRes(List<Record> reslist,String operaId,String muenPid){

		 for(Record re:reslist){
			 if(operaId.indexOf("o_")!=-1){
				 if(muenPid.equals(String.valueOf(re.get("res_id")))
						 &&operaId.split("o_")[1].equals(String.valueOf(re.get("opera_id")))){
					 return true;
				 }
			 }else{
				 if(operaId.equals(String.valueOf(re.get("res_id")))){
					 return true;
				 }
			 }

		 }
		 return false;
	}
	
	/**
	 * 配置用户菜单功能关系
	 * @param treeIds
	 * @param userId
	 * @return
	 */
	public boolean saveUserRes(final String treeIds,final String userId){
		boolean success = Db.tx(new IAtom() {public boolean run() throws SQLException {
			boolean success=false;
				String[] strs=treeIds.split(",");
				for(String str:strs){
					String[] arr=str.split("o_");
					UserRes ur = findFirst("select * from bp_user_res where user_id=? and res_id=? and opera_id=?"
							,new Object[]{userId,arr[0],arr[1]});
					if(ur!=null){
						success=ur.delete();
					}else{
						ur=new UserRes();
						ur.set("user_id", userId);
						ur.set("res_id", arr[0]);
						ur.set("opera_id", arr[1]);
						success=ur.save();
					}
				}
				return success;
			}
		});
		return success;
	}
	
	/**
	 * 得到该用户的菜单功能使用权限
	 * @param userId
	 * @param resId
	 * @param operaType
	 * @return true 有该功能个权限
	 */
	public boolean getResUseRole(String userId,String resId,String operaType){
		StringBuffer sql=new StringBuffer("select ifnull(count(1),0) as num from bp_user_res where user_id="+userId);
		sql.append(" and opera_id=(select id from bp_operation_res where res_id="+resId);
		sql.append(" and opera_type='"+operaType+"')");
		long num=Db.findFirst(sql.toString()).get("num");
		if(num==0){
			return false;	
		}
		return true;
	}
	
	/**
	 * 根据该菜单的名称和类型获取该功能是否显示
	 * @param resName
	 * @param operaType
	 * @return 返回display block or none
	 */
	public String getResOperaByResName(String resName,String operaType){
		String userId = ContextUtil.getCurrentUserId();
		String resId=Db.findFirst("select id from system_res where name='"+resName+"'").get("id")+"";
		if(getResUseRole(userId,resId,operaType)){
			return "block";
		}
		return "none";
	}
	

}
