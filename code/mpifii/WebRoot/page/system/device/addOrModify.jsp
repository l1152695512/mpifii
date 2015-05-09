<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.sn_style{
		font-size: 18px;
		font-weight: bold;
		line-height: 28px;
	}
</style>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/page');">设备管理</a><span class="divider">/</span></li>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty userInfo.id}">添加设备</c:when>
					<c:otherwise>修改设备</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/page/save" method="POST">
				<input type="hidden" name="id" value="${userInfo.id}">
				<div class="control-group">
					<label class="control-label">SN</label>
					<div class="controls">
						<c:choose>
							<c:when test="${empty userInfo.id}">
								<input type="text" name="sn"
									class="input-xlarge" autocomplete="off" maxlength="16" vMin='16'
									vType="letterNumber" placeholder='16位0-9或a-f'>
							</c:when>
							<c:otherwise>
								<span class="sn_style">${userInfo.sn}</span>
							</c:otherwise>
						</c:choose>
						<span class="help-inline"></span>
					</div>
				</div>
<!-- 				<div class="control-group"> -->
<!-- 					<label class="control-label">名称</label> -->
<!-- 					<div class="controls"> -->
<%-- 						<input type="text" name="device.name" value="${deviceInfo.name}" class="input-xlarge" --%>
<!-- 							maxlength="20" vMin="0" vType="length" placeholder='20位以内字母或数字' -->
<!-- 							onblur="onblurVali(this);"> -->
<!-- 						<span class="help-inline"></span> -->
<!-- 					</div> -->
<!-- 				</div> -->
	<%-- 			<div class="control-group">
					<label class="control-label">超时时间</label>
					<div class="controls">
						<input type="text" name="device.time_out" value="${deviceInfo.time_out}" class="input-xlarge"
							maxlength="5" vMin="0" vType="numberZ" placeholder='120分钟' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div> --%>
<!-- 				<div class="control-group"> -->
<!-- 					<label class="control-label">盒子主页地址</label> -->
<!-- 					<div class="controls"> -->
<%-- 						<input type="text" name="device.index_page_path" value="${deviceInfo.index_page_path}" class="input-xlarge" --%>
<!-- 							vType="url" placeholder='http://m.pifii.com/ifidc/pifii.html' onblur="onblurVali(this);"> -->
<!-- 						<span class="help-inline"></span> -->
<!-- 					</div> -->
<!-- 				</div> -->
	<%-- 			<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="device.des" value="${deviceInfo.des}" class="input-xlarge"
							maxlength="500" vMin="0" vType="length" placeholder='500位以内' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div> --%>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContent('/system/page');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	$("#editForm input[name='device.router_sn']").focusout(function(){
		var sn = $(this).val();
		if(sn.match(/[0-9a-f]{16}/gi)  == null){
			setInputError($(this),"16位0-9或a-f！");
		}else{
			hiddenInputColor($(this));
			showInputColor($(this), "success");
			$(this).next(".help-inline").text("");
		}
	});
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
// 		if('${deviceInfo.id}'== ''){
// 			var snObject = $("#editForm input[name='device.router_sn']");
// 			var sn = snObject.val();
// 			if(sn.match(/[0-9a-f]{16}/gi)  == null){
// 				hiddenInputColor(snObject);
// 				showInputColor(snObject, "error");
// 				snObject.next(".help-inline").text("16位0-9或a-f！");
// 				return;
// 			}
// 		}
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				if("snRepeat"==resp.error){
					setInputError($("#editForm input[name='device.router_sn']"),"该SN已存在！");
				}else{
					ajaxContent('/system/page/save');
				}
			}
		});
	}
</script>