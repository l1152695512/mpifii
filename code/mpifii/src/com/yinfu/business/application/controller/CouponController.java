
package com.yinfu.business.application.controller;

import java.io.File;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.Coupon;
import com.yinfu.jbase.jfinal.ext.Controller;

/**
 * @author JiaYongChao 优惠券
 */

@ControllerBind(controllerKey = "/business/app/coupon", viewPath = "/page/business/application/coupon")
public class CouponController extends Controller<Coupon> {
	private static final String PREVIEW_PATH = "upload"+File.separator+"image"+File.separator+"coupon"+File.separator;
	//@formatter:off 
	/**
	 * Title: showList
	 * Description:优惠券列表
	 * Created On: 2014年7月30日 上午10:20:22
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void showList() {
		// 分页基本变量（jqGrid自带）
		int pageNum = getParaToInt("pageNum");
		int pageSize = getParaToInt("pageSize");
		String shopId = getPara("shopId");
		Page<Coupon> page = Coupon.dao.findByShopId(pageNum, pageSize, shopId);
		renderJson(page);
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: add
	 * Description: 
	 * Created On: 2014年7月30日 下午3:44:34
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#add()
	 */
	//@formatter:on
	public void add()
	{
		boolean success = false;
		JSONObject returnData = new JSONObject();
		try {
			UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
			String icon ="/upload/image/coupon/" + file.getFileName();
			if(success = getModel().set("icon", icon).save()){
				returnData.put("success",success);
				renderJson(returnData);
			}else{
				returnData.put("success",success);
				renderJson(returnData);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: edit
	 * Description: 
	 * Created On: 2014年7月30日 下午3:44:37
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#edit()
	 */
	//@formatter:on
	public void edit()
	{
		
		renderJsonResult(getModel().update());
	}
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 
	 * Created On: 2014年7月30日 下午3:44:40
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete(){
		int id = getParaToInt("id");
		renderJsonResult(Coupon.dao.deleteByIdAndPid(id));
	}
	
	
}
