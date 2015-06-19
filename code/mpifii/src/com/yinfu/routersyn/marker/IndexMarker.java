package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.util.PropertyUtils;

public class IndexMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(IndexMarker.class);
	private Object shopId;
	
	private IndexMarker(Object shopId) throws IOException {
		super("index_new",".html","index");
		this.shopId = shopId;
		setContentData();
	}
	
	 public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new IndexMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成index.html异常！", e);
		}
    	return success;
	}
	
    protected void setContentData(){
    	root.put("title", "PIFII");
        root.put("content" , "广州因孚网络科技技术支持");
        root.put("adlist",getAdv());
        root.put("shop", getShopInfo());
        root.put("applist",getApp());
        String authServerPath = PropertyUtils.getProperty("router.auth.url");
    	authServerPath = authServerPath.substring(0, authServerPath.lastIndexOf("/"));
        root.put("help",authServerPath+"/portal/mb/help");
//        root.put("bottomAdv",getBottomAdv());
    }
    
    private List<Record> getAdv(){
    	String authServerPath = PropertyUtils.getProperty("router.auth.url");
    	authServerPath = authServerPath.substring(0, authServerPath.lastIndexOf("/"));
    	StringBuffer sql = new StringBuffer();
    	sql.append("select ifnull(substring_index(batr.res_url,'/',-1),'') src,concat('"+authServerPath+"','/portal/mb/adv?id=',bac.id) href ");
    	sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id=? and basp.adv_type='adv' and basp.id=bas.adv_spaces) ");
    	sql.append("join bp_adv_type bat on (bat.template_id=? and basp.id=bat.adv_spaces) ");
    	sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
    	sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
    	sql.append("order by basp.adv_index ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{shopId,PageUtil.getTemplateId(shopId)});
    	return list;
    }
    
    private Record getShopInfo(){
    	String sql = "select id,name,REVERSE(LEFT(REVERSE(icon),LOCATE('/',REVERSE(icon))-1)) as icon,addr,tel from bp_shop where id=?";
    	Record rd = Db.findFirst(sql,new Object[]{shopId});
    	if(null != rd){
    		if(StringUtils.isBlank(rd.getStr("icon"))){
    			rd.set("icon", "morentouxiang.png");
    		}
    	}
    	return rd;
    }
    
    private List<Record> getApp(){
    	String authServerPath = PropertyUtils.getProperty("router.auth.url");
    	authServerPath = authServerPath.substring(0, authServerPath.lastIndexOf("/"));
    	StringBuffer sql = new StringBuffer();
//    	sql.append("select concat(?,'_',c.id) as id,c.name,REVERSE(LEFT(REVERSE(c.icon),LOCATE('/',REVERSE(c.icon))-1)) as icon,ifnull(c.link,'#') as link ");
//    	sql.append("from bp_shop_page_app a ");
//    	sql.append("left join bp_shop_page b ");
//    	sql.append("on a.page_id = b.id ");
//    	sql.append("left join bp_app c ");
//    	sql.append("on a.app_id=c.id ");
//    	sql.append("where b.shop_id=?");
    	sql.append("select concat(?,'_',a.id) id,ifnull(a.link,if(a.cloud_url is null,'#',concat('"+authServerPath+"','/',a.cloud_url,'/app-',a.id))) as link,");
    	sql.append("substring_index(ifnull(sac.icon,ifnull(tai.icon,a.icon)),'/',-1) icon,ifnull(sac.name,a.name) name ");
    	sql.append("from bp_app a ");
    	sql.append("left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
    	sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
    	sql.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
    	sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
    	sql.append("where spa.id is not null and a.template_id=? ");
    	List<Record> list = Db.find(sql.toString(), 
    			new Object[]{PropertyUtils.getProperty("route.upload.type.app"),
    		PageUtil.getPageIdByShopId(shopId),shopId,PageUtil.getTemplateId(shopId)});
    	return list;
    }
   
    private Record getBottomAdv(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select sgr.image,sgr.link ");
		sql.append("from bp_shop s join bp_shop_group_role sgr on (s.id=? and s.group_id=sgr.shop_group_id) ");
		sql.append("join bp_adv_type bat on (sgr.adv_type_id=bat.id) ");
		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv_bottom' and basp.id=bat.adv_spaces) ");
    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
    	if(null != rd){
    		String image = rd.getStr("image");
    		if(StringUtils.isNotBlank(image)){
    			image = "logo/"+image.substring(image.lastIndexOf("/")+1);
    		}else{
    			image = "index/img/ad.png";
    		}
    		rd.set("image", image);
    	}else{
    		rd = new Record().set("image", "index/img/ad.png").set("link", "#");
    	}
    	return rd;
    }
    
    public static void main(String[] args)throws Exception{
//    	InitDemoDbConfig.initPlugin();
//    	IndexMarker.execute("2", "E:/");
//    	IndexMarker marker = getInstance();
//    	marker.createHtml(1);
    }
}
