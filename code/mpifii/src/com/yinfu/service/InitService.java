package com.yinfu.service;

import java.io.File;

import com.yinfu.jbase.util.Fs;
import com.jfinal.plugin.activerecord.Db;

public class InitService
{

	public void initDb(String path){
		
		try
		{
			String sql = Fs.readFile(new File(path));
			
			String[] sqls = sql.split(";");
			
			for(String s :sqls ){
				Db.update(s);
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	
}
