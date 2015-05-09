<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/shop/index');">商铺管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加商铺</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加商铺
			</h2>
<!-- 			<div class="box-icon"> -->
<!-- 				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> -->
<!-- 				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
<!-- 			</div> -->
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="shop_add_from" action="${cxt}/business/shop/save" method="POST">
				<input type="hidden" name="shop.id" value="${shop.id}">
				<div class="control-group">
					<label class="control-label">商铺名称</label>
					<div class="controls">
						<input type="text" name="shop.name" value="${shop.name}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">3-50位</span>
					</div>
				</div>
<!-- 				<div class="control-group"> -->
<!-- 					<label class="control-label">商铺图标</label> -->
<!-- 					<div class="controls"> -->
<%-- 						 <img  src="${cxt}/${shop.icon}" id="shop_icon" style="width: 48px;height: 48px" onerror= "this.src='${cxt}/images/guest.jpg'"> --%>
<!-- 						<input  type="file" name="shopImage" id="shopImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  /> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<div class="control-group">
					<label class="control-label">商铺位置</label>
					<div class="controls">
						<input type="text" name="shop.location"  readonly="readonly" value="${shop.location}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<input  type="button"  class="btn"  value="选择位置"  onclick="mapLocation()"/>
						<input type="hidden" name="shop.lng" value="${shop.lng}" />
						<input type="hidden" name="shop.lat" value="${shop.lat}" />
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">关联用户</label>
					<div class="controls">
<!-- 						<select name="shop.owner" class="combox"> -->
<%-- 							<c:forEach var="user" items="${userList}"> --%>
<%-- 								<option value="${user.id}" <c:if test="${user.id==shop.owner}"> selected="selected"</c:if>>${user.name}(设备数:${user.sbs})</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
						<input type="text" name="userName" disabled="disabled" maxlength="50" value="${shop.owner_name}" 
								class="input-xlarge" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<c:if test="${empty shop.owner}">
							<input type="button" class="btn" value="选择用户"  onclick="listUsers();"/>
						</c:if>
						<input type="hidden" name="shop.owner" value="${shop.owner}" />
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">分组</label>
					<div class="controls">
						<input type="text" name="shopGroupName" readonly="readonly" value="${shop.groupName}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<input type="button" class="btn" value="设置分组"  onclick="listShopGroup();"/>
						<input type="hidden" name="shop.group_id" value="${shop.group_id}" />
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">联系方式</label>
					<div class="controls">
						<input type="text" name="shop.tel" value="${shop.tel}" class="input-xlarge" 
							maxlength="30" vMin="0" vType="phone" onchange="onblurVali(this);" />
						<span class="help-inline">手机号码</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="shop.des" value="${shop.des}" class="input-xlarge"
							maxlength="500" vMin="0" vType="length"  />
						<span class="help-inline">0-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitShopInfo(this.form);">提交</button>
					<button type="button" class="btn" onclick="ajaxContent('/business/shop/index');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	function submitShopInfo(form){
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#shop_add_from").ajaxSubmit({
	        success: function(resp){
				ajaxContent('/business/shop');
	        }
	  	});
	}
	$("#shopImage").on("change", function() {
		var files = !!this.files ? this.files : [];
		if (!files.length || !window.FileReader)
			return;
		if (/^image/.test(files[0].type)) {
			var reader = new FileReader();
			reader.readAsDataURL(files[0]);
			reader.onloadend = function() {
				$("#shop_icon").attr("src", this.result);
			};
		}
	});
	var mlab;
	function getAddr(lng,lat,addr){
		$("#shop_add_from input[name='shop.lng']").val(lng);
		$("#shop_add_from input[name='shop.lat']").val(lat);
		$("#shop_add_from input[name='shop.location']").val(addr);
		onblurVali($("#shop_add_from input[name='shop.location']"));
		mlab.hideModal();
	}
	function addUserCallback(data){//添加完新用户后回调
		if(undefined != data){
			$("#shop_add_from input[name='userName']").val(data.name);
			$("#shop_add_from input[name='shop.owner']").val(data.id);
			onblurVali($("#shop_add_from input[name='userName']"));
		}
	}
	function mapLocation(){
		var lng = $("#shop_add_from input[name='shop.lng']").val();
		var lat = $("#shop_add_from input[name='shop.lat']").val();
		mlab = $.fn.SimpleModal({
// 			model: 'modal-ajax',
			title: '地图定位',
			width: 800,
			height: 550,
// 			hideFooter: true,
			param: {
				url: "business/shop/mapLab?lng="+lng+"&lat="+lat
			}
		});
		mlab.showModal();
	}
	function listUsers(){
		$.fn.SimpleModal({
			title: '关联用户',
			width: 400,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var userId = $("#choice_user_list input[name='select_user']:checked").val();
	    			var userName = $("#choice_user_list input[name='select_user']:checked").parent().next().text();
	    			
	    			$("#shop_add_from input[name='userName']").val(userName);
	    			$("#shop_add_from input[name='shop.owner']").val(userId);
	    			onblurVali($("#shop_add_from input[name='userName']"));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'business/shop/listUser'
			}
		}).showModal();
	}
	function listShopGroup(){
		var shopOwner = $("#shop_add_from input[name='shop.owner']").val();
		if(shopOwner == undefined || shopOwner==""){
			myAlert("请先选择“关联用户”！");
			return;
		}
		$.fn.SimpleModal({
			title: '选择分组',
			width: 400,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var shopGroupId = $("#choice_shop_group_list input[name='select_group']:checked").val();
	    			var shopGroupName = $("#choice_shop_group_list input[name='select_group']:checked").parent().next().text();
	    			
	    			$("#shop_add_from input[name='shopGroupName']").val(shopGroupName);
	    			$("#shop_add_from input[name='shop.group_id']").val(shopGroupId);
	    			
	    			onblurVali($("#shop_add_from input[name='shopGroupName']"));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'business/shop/listShopGroup',
				data: {userId:shopOwner},
			}
		}).showModal();
	}
</script>