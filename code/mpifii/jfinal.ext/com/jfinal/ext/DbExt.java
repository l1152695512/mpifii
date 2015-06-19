package com.jfinal.ext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;

/**
 * 批量处理可开启事务
 * @author l
 *
 */
public class DbExt{
	private final Config config;
	private static final Map<String, DbExt> map = new HashMap<String, DbExt>();
	
	public DbExt() {
		if (DbKit.getConfig() == null)
			throw new RuntimeException("The main config is null, initialize ActiveRecordPlugin first");
		this.config = DbKit.getConfig();
	}
	
	public DbExt(String configName) {
		this.config = DbKit.getConfig(configName);
		if (this.config == null)
			throw new IllegalArgumentException("Config not found by configName: " + configName);
	}
	
	public static DbExt use(String configName) {
		DbExt result = map.get(configName);
		if (result == null) {
			result = new DbExt(configName);
			map.put(configName, result);
		}
		return result;
	}
	
	public static DbExt use() {
		return use(DbKit.getConfig().getName());
	}
	
	public static int[] batch(String sql, Object[][] paras) {
//		if(paras.length>1000){
//			throw new ActiveRecordException("批量处理的数据条数过大！");
//		}
		return DbExt.use().execute(sql, paras);
	}
	
	public static int[] batch(List<String> sqlList) {
//		if(sqlList.size()>1000){
//			throw new ActiveRecordException("批量处理的数据条数过大！");
//		}
		return DbExt.use().execute(sqlList);
    }
	
	private int[] execute(String sql, Object[][] paras) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = config.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			if (paras == null || paras.length == 0)
				throw new IllegalArgumentException("The paras array length must more than 0.");
			int pointer = 0;
			int[] result = new int[paras.length];
			PreparedStatement pst = conn.prepareStatement(sql);
			for (int i=0; i<paras.length; i++) {
				for (int j=0; j<paras[i].length; j++) {
					Object value = paras[i][j];
					if (config.getDialect().isOracle() && value instanceof java.sql.Date)
						pst.setDate(j + 1, (java.sql.Date)value);
					else
						pst.setObject(j + 1, value);
				}
				pst.addBatch();
			}
			int[] r = pst.executeBatch();
			for (int k=0; k<r.length; k++)
				result[pointer++] = r[k];
			closeQuietly(pst);
			return result;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {conn.setAutoCommit(autoCommit);} catch (Exception e) {e.printStackTrace();}
			config.close(conn);
		}
	}
	
	private int[] execute(List<String> sqlList) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = config.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			if (sqlList == null || sqlList.size() == 0)
				throw new IllegalArgumentException("The sqlList length must more than 0.");
			int pointer = 0;
			int size = sqlList.size();
			int[] result = new int[size];
			Statement st = conn.createStatement();
			for (int i=0; i<size; i++) {
				st.addBatch(sqlList.get(i));
			}
			int[] r = st.executeBatch();
			for (int k=0; k<r.length; k++)
				result[pointer++] = r[k];
			closeQuietly(st);
			return result;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {conn.setAutoCommit(autoCommit);} catch (Exception e) {e.printStackTrace();}
			config.close(conn);
		}
	}
	
	private void closeQuietly(Statement st){
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}
	
}
