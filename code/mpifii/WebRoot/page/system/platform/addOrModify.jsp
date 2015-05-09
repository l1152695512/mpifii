<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/platform');">平台管理</a><span class="divider">/</span></li>
<!-- 		<li><a href="#"></a></li> -->
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty userInfo.id}">添加平台</c:when>
					<c:otherwise>修改平台</c:otherwise>
				</c:choose>
			</h2>
<!-- 			<div class="box-icon"> -->
<!-- 				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
<!-- 			</div> -->
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/platform/save" method="POST">
				<input type="hidden" name="id" value="${userInfo.id}">
				<div class="control-group">
					<label class="control-label">平台名称</label>
					<div class="controls">
						<input type="text" name="name" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.name}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">下载地址</label>
					<div class="controls">
						<input type="text" name="down_url" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.down_url}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">认证地址</label>
					<div class="controls">
						<input type="text" name="auth_url" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.auth_url}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">登录ip</label>
					<div class="controls">
						<input type="text" name="log_ip" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.log_ip}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">登录端口</label>
					<div class="controls">
						<input type="text" name="log_port" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.log_port}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">主页地址</label>
					<div class="controls">
						<input type="text" name="home_url" <c:if test="${not empty userInfo.id}"></c:if> value="${userInfo.home_url}" class="input-xlarge"
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContentReturn();">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				if("userNameRepeat"==resp.error){
					setInputError($("#editForm input[name='user.name']"),"用户名重复！");
				}else{
					ajaxContentReturn(resp);
				}
			}
		});
	}
	$("#userImage").on("change", function() {
		var files = !!this.files ? this.files : [];
		if (!files.length || !window.FileReader)
			return;
		if (/^image/.test(files[0].type)) {
			var reader = new FileReader();
			reader.readAsDataURL(files[0]);
			reader.onloadend = function() {
				$("#icon").attr("src", this.result);
			};
		}
	});
</script>