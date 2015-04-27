<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
			<li><a href="javascript:void(0);" onclick="ajaxContentReturn();">组织管理</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContentReturn();">商铺管理</a> <span
			class="divider">/</span></li>
		<c:choose>
			<c:when test="${empty shop.id}"><li><a href="#">添加商铺</a></li></c:when>
			<c:otherwise><li><a href="#">修改商铺</a></li></c:otherwise>
		</c:choose>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty shop.id}">添加商铺</c:when>
					<c:otherwise>修改商铺</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="shop_add_from" action="${cxt}/business/org/shop/save" method="POST">
				<input type="hidden" name="shop.id" value="${shop.id}">
				<input type="hidden" name="shop.org_id" value="${shop.org_id}">
				<div class="control-group">
					<label class="control-label">所属客户</label>
					<div class="controls">
						<input type="text" name="shop.customer" value="${shop.customer}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商铺编号</label>
					<div class="controls">
						<input type="text" name="shop.sn" value="${shop.sn}" class="input-xlarge"
							maxlength="12" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">3-12位</span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">商铺名称</label>
					<div class="controls">
						<input type="text" name="shop.name" value="${shop.name}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">3-50位</span>
					</div>
				</div>
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
					<label class="control-label">联系方式</label>
					<div class="controls">
						<input type="text" name="shop.tel" value="${shop.tel}" class="input-xlarge" 
							maxlength="30" vMin="0" vType="length" onchange="onblurVali(this);" />
						<span class="help-inline"></span>
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
					<button type="button" class="btn" onclick="ajaxContentReturn();">取消</button>
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
			success : function(resp) {
				if("shopSnRepeat"==resp.error){
					setInputError($("#shop_add_from input[name='shop.sn']"),"商铺编号重复！");
					return;
				}else{
					ajaxContent('/business/org/shop',{orgId:'${shop.org_id}'});
				}
			}
	  	});
	}
	var mlab;
	function getAddr(lng,lat,addr){
		$("#shop_add_from input[name='shop.lng']").val(lng);
		$("#shop_add_from input[name='shop.lat']").val(lat);
		$("#shop_add_from input[name='shop.location']").val(addr);
		onblurVali($("#shop_add_from input[name='shop.location']"));
		mlab.hideModal();
	}
	function addUserCallback(data){//添加完新用户后回调
		
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
				url: "business/org/shop/mapLab?lng="+lng+"&lat="+lat
			}
		});
		mlab.showModal();
	}
</script>