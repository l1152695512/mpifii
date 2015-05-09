package com.yinfu.business.application.model;

import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.jbase.jfinal.ext.Model;

/**
 * @author JiaYongChao
 *	优惠券实体类
 */
@TableBind(tableName = "bp_coupon")
public class Coupon extends Model<Coupon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Coupon dao = new Coupon();


	//@formatter:off 
	/**
	 * Title: findByShopId
	 * Description:通过商铺ID获得优惠券列表
	 * Created On: 2014年7月30日 下午12:28:43
	 * @author JiaYongChao
	 * <p>
	 * @param pageNum
	 * @param pageSize
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public Page<Coupon> findByShopId(int pageNum, int pageSize, String shopId) {
		String sql ="select t.id,t.companyname,t.shopname ,CASE t.type when 0 THEN '火锅' when 2 then '水煮' when 3 then '烹饪' end as type,t.description,t.icon,t.storename,t.validity  ";
		String sqlExceptSelect = " from bp_coupon t  where 1=1 and t.delete_date is null ";
		if(shopId!=null && StringUtils.isNotEmpty(shopId)){
			sqlExceptSelect+=" and t.shopId="+shopId;
		}
		Page<Coupon> page = dao.paginate(pageNum, pageSize, sql, sqlExceptSelect);
		return page;
	}
	
}
